package Characters;

import Components.*;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import static Components.GameBoard.*;
import static Components.GameBoard.getNumberOfFoodsLeft;

public class BlueGhost implements Runnable, Ghost {

    private ImageIcon[] blueGhostImagesRight;
    private ImageIcon[] blueGhostImagesLeft;
    private ImageIcon[] blueGhostImagesUp;
    private ImageIcon[] blueGhostImagesDown;
    private ImageIcon[] frightenedGhostImages;
    private ImageIcon[] frightenedFlashingGhostImages;
    private ImageIcon eyesGhostImages;

    private int startPositionX;
    private int startPositionY;

    private int panelX;
    private int panelY;

    private int initMovementX;
    private int initMovementY;

    private int currentGhostImageIndex;
    private Directions currentGhostOrientation;
    private final int initSpeed = 2;
    private int speed = initSpeed;
    private final Pacman pacman;
    private final Object monitor;

    private final PathFinding pathfinding;
    private List<Node> path;
    private int pathIndex = 0;
    private int nodeTargetX;
    private int nodeTargetY;

    private final JLabel blueGhostLabel;
    private volatile boolean paused = false;

    private final TimeTracker upgradesTimeTracker;
    private List<Upgrade> upgrades;

    private final TimeTracker frightTimeTracker;
    private GhostState ghostState;

    public BlueGhost(Pacman pacman, Object monitor, String boardSize){
        loadImages();
        initInitialCoords(boardSize);
        currentGhostImageIndex = 0;
        currentGhostOrientation = Directions.RIGHT;
        this.pathfinding = new PathFinding();
        this.nodeTargetX = panelX;
        this.nodeTargetY = panelY;
        this.pacman = pacman;
        this.monitor = monitor;

        blueGhostLabel = new JLabel(blueGhostImagesRight[0]);
        blueGhostLabel.setOpaque(true);
        blueGhostLabel.setBounds(startPositionX, startPositionY, 13, 13);
        blueGhostLabel.setBackground(Color.black);

        upgradesTimeTracker = new TimeTracker();
        frightTimeTracker = new TimeTracker();
        upgrades = new ArrayList<>();
        ghostState = GhostState.CHASE;
    }

    private void initInitialCoords(String boardSize){
        switch (boardSize){
            case "23x24":
                startPositionX = 186;
                startPositionY = 209;
                panelX = startPositionX;
                panelY = startPositionY;
                initMovementX = 213;
                initMovementY = 153;
                break;
            case "27x18":
                startPositionX = 218;
                startPositionY = 193;
                panelX = startPositionX;
                panelY = startPositionY;
                initMovementX = 247;
                initMovementY = 136;
                break;
            case "21x21":
                startPositionX = 170;
                startPositionY = 193;
                panelX = startPositionX;
                panelY = startPositionY;
                initMovementX = 190;
                initMovementY = 136;
                break;
            case "31x11":
                startPositionX = 263;
                startPositionY = 98;
                panelX = startPositionX;
                panelY = startPositionY;
                initMovementX = 285;
                initMovementY = 60;
                break;
            case "15x21":
                startPositionX = 111;
                startPositionY = 230;
                panelX = startPositionX;
                panelY = startPositionY;
                initMovementX = 135;
                initMovementY = 174;
                break;
        }
    }

    private void updateGhostIconLoop() {
        while (inGame){
            if (!paused){
                switch (getGhostState()){
                    case FRIGHTENED -> {
                        if (frightTimeTracker.getSecondsPassed() >= 4){
                            blueGhostLabel.setIcon(frightenedFlashingGhostImages[currentGhostImageIndex]);
                        }else {
                            blueGhostLabel.setIcon(frightenedGhostImages[currentGhostImageIndex]);
                        }
                    }
                    case SPAWN -> blueGhostLabel.setIcon(eyesGhostImages);
                    default -> {
                        switch (currentGhostOrientation) {
                            case UP:
                                blueGhostLabel.setIcon(blueGhostImagesUp[currentGhostImageIndex]);
                                break;
                            case RIGHT:
                                blueGhostLabel.setIcon(blueGhostImagesRight[currentGhostImageIndex]);
                                break;
                            case DOWN:
                                blueGhostLabel.setIcon(blueGhostImagesDown[currentGhostImageIndex]);
                                break;
                            case LEFT:
                                blueGhostLabel.setIcon(blueGhostImagesLeft[currentGhostImageIndex]);
                                break;
                        }
                    }
                }
                updateImageIndex();
            }
            try {
                Thread.sleep(120);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }

    }

    private void updateGhostLabelPosition(){
        blueGhostLabel.setBounds(getGhostCordX() + 3, getGhostCordY() + 3, 13, 13);
    }

    public void updateImageIndex() {
        currentGhostImageIndex = (currentGhostImageIndex + 1) % 2;
    }

    private void generateUpgradeLoop(){
        boolean upgradeGenerated = false;
        upgradesTimeTracker.start();
        while (inGame){
            boolean isInCage = getGhostCordX() >= cageTopLeftX && getGhostCordX() <= cageBottomRightX  &&
                    getGhostCordY() >= cageTopLeftY && getGhostCordY() <= cageBottomRightY;

            if ((getGhostState() == GhostState.CHASE || getGhostState() == GhostState.SCATTER) && !isInCage){
                if (upgradesTimeTracker.getSecondsPassed() % 5 == 0 && !upgradeGenerated){
                    if (ThreadLocalRandom.current().nextInt(100) < 25) {
                        int x = getGhostCordX()/boardDimensions;
                        int y = getGhostCordY()/boardDimensions;
                        UpgradeType type = UpgradeType.values()[ThreadLocalRandom.current().nextInt(UpgradeType.values().length)];
                        Upgrade upgrade = new Upgrade(x, y, type);
                        synchronized (monitor) {
                            upgrades.add(upgrade);
                        }
                    }
                    upgradeGenerated = true;
                } else if (upgradesTimeTracker.getSecondsPassed() % 5 != 0){
                    upgradeGenerated = false;
                }
            }
        }
    }

    public void removeProcessedUpgrades(){
        synchronized (monitor) {
            upgrades.clear();
        }
    }

    @Override
    public void run() {
        new Thread(this::updateGhostIconLoop).start();
        new Thread(this::generateUpgradeLoop).start();
        frightTimeTracker.start();
        while (inGame){
            checkPaused();

            if (ghostState == GhostState.SPAWN && (getGhostCordX()/boardDimensions == startPositionX/boardDimensions && getGhostCordY()/boardDimensions == startPositionY/boardDimensions)){
                synchronized (monitor){
                    ghostState = GhostState.CHASE;
                    speed = initSpeed;
                }
            }

            if (pacman.amountOfFoodConsumed >= getNumberOfFoodsLeft()/3){

                boolean isInCage = getGhostCordX() >= cageTopLeftX && getGhostCordX() <= cageBottomRightX  &&
                        getGhostCordY() >= cageTopLeftY && getGhostCordY() <= cageBottomRightY;


                if (isInCage){
                    moveGhost(initMovementX, initMovementY);
                }else {
                    int[] ghostTarget = getGhostTarget();
                    moveGhost(ghostTarget[0], ghostTarget[1]);
                }

                updateGhostLabelPosition();
            }

            try {
                Thread.sleep(30);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

    public void startFrightenedState(){
        synchronized (monitor){
            ghostState = GhostState.FRIGHTENED;
            speed = initSpeed - 1;
        }
        new Thread(this::checkIfFrightenedLoop).start();
    }

    private void checkIfFrightenedLoop(){
        frightTimeTracker.resetTimePassed();
        while (getGhostState() == GhostState.FRIGHTENED){
            if (frightTimeTracker.getSecondsPassed() >= 6){
                synchronized (monitor){
                    speed = initSpeed;
                    ghostState = GhostState.CHASE;
                }
            }
        }
    }

    public void ghostHasBeenEaten(){
        synchronized (monitor){
            speed = 5;
            ghostState = GhostState.SPAWN;
        }
    }

    public GhostState getGhostState(){
        synchronized (monitor){
            return ghostState;
        }
    }

    private int[] getGhostTarget(){

        switch (getGhostState()){
            case SPAWN:
                return new int[]{startPositionX, startPositionY};
            default:
                int index = ThreadLocalRandom.current().nextInt(foodCells.size());
                int[] randomCell = foodCells.get(index);
                return new int[]{randomCell[1] * boardDimensions, randomCell[0] * boardDimensions};
        }
    }

    private void moveGhost(int targetX, int targetY){
        if (path == null || pathIndex >= path.size()) {
            synchronized (monitor){
                path = pathfinding.findPath(panelX / boardDimensions, panelY / boardDimensions, targetX / boardDimensions, targetY / boardDimensions);
                pathIndex = 0;
            }
        }

        if (path != null && !path.isEmpty() && pathIndex <= path.size() - 1) {
            Node nextNode = path.get(pathIndex);
            nodeTargetX = nextNode.getX() * boardDimensions;
            nodeTargetY = nextNode.getY() * boardDimensions;
        }

        if (panelX < nodeTargetX) {
            panelX += speed;
            currentGhostOrientation = Directions.RIGHT;
            if (panelX > nodeTargetX) panelX = nodeTargetX;
        } else if (panelX > nodeTargetX) {
            panelX -= speed;
            currentGhostOrientation = Directions.LEFT;
            if (panelX < nodeTargetX) panelX = nodeTargetX;
        }

        if (panelY < nodeTargetY) {
            panelY += speed;
            currentGhostOrientation = Directions.DOWN;
            if (panelY > nodeTargetY) panelY = nodeTargetY;
        } else if (panelY > nodeTargetY) {
            panelY -= speed;
            currentGhostOrientation = Directions.UP;
            if (panelY < nodeTargetY) panelY = nodeTargetY;
        }

        if (panelX == nodeTargetX && panelY == nodeTargetY && path != null) {
            pathIndex++;
        }
    }

    public synchronized void pause() {
        paused = true;
    }

    public synchronized void resume() {
        paused = false;
        notify();
    }

    private synchronized void checkPaused() {
        while (paused) {
            try {
                wait();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

    public List<Upgrade> getUpgrades() {
        synchronized (monitor) {
            return new ArrayList<>(upgrades);
        }
    }

    public JLabel getGhostLabel() {
        return blueGhostLabel;
    }

    public int getGhostCordX(){
        synchronized (monitor) {
            return panelX;
        }
    }

    public int getGhostCordY() {
        synchronized (monitor) {
            return panelY;
        }
    }

    public void resetPosition() {
        synchronized (monitor){
            panelX = startPositionX;
            panelY = startPositionY;
            path = null;
            pathIndex = 0;
            currentGhostImageIndex = 0;
            currentGhostOrientation = Directions.RIGHT;
            speed = initSpeed;
            upgrades.clear();
        }
        updateGhostLabelPosition();
        resume();
    }

    private void loadImages(){

        blueGhostImagesRight = new ImageIcon[2];
        blueGhostImagesLeft = new ImageIcon[2];
        blueGhostImagesUp = new ImageIcon[2];
        blueGhostImagesDown = new ImageIcon[2];
        frightenedGhostImages = new ImageIcon[2];
        frightenedFlashingGhostImages = new ImageIcon[2];
        eyesGhostImages = new ImageIcon(getClass().getClassLoader().getResource("resources\\ghosts13\\eye.png"));


        for (int i = 0; i < 2; i++) {
            blueGhostImagesRight[i] = new ImageIcon(getClass().getClassLoader().getResource("resources\\ghosts13\\inky\\inky-right-" + i + ".png"));
            blueGhostImagesLeft[i] = new ImageIcon(getClass().getClassLoader().getResource("resources\\ghosts13\\inky\\inky-left-" + i + ".png"));
            blueGhostImagesUp[i] = new ImageIcon(getClass().getClassLoader().getResource("resources\\ghosts13\\inky\\inky-up-" + i + ".png"));
            blueGhostImagesDown[i] = new ImageIcon(getClass().getClassLoader().getResource("resources\\ghosts13\\inky\\inky-down-" + i + ".png"));
            frightenedGhostImages[i] = new ImageIcon(getClass().getClassLoader().getResource("resources\\ghosts13\\edible-ghost-" + i + ".png"));
        }

        frightenedFlashingGhostImages[0] = frightenedGhostImages[0];
        frightenedFlashingGhostImages[1] = new ImageIcon(getClass().getClassLoader().getResource("resources\\ghosts13\\edible-ghost-blink-1.png"));
    }
}
