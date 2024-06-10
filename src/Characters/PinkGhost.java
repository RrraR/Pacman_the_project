package Characters;

import Components.GhostState;
import Components.TimeTracker;
import Components.Upgrade;
import Components.UpgradeType;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import static Components.GameBoard.*;

public class PinkGhost implements Runnable, Ghost {

    private ImageIcon[] pinkGhostImagesRight;
    private ImageIcon[] pinkGhostImagesLeft;
    private ImageIcon[] pinkGhostImagesUp;
    private ImageIcon[] pinkGhostImagesDown;
    private ImageIcon[] frightenedGhostImages;
    private ImageIcon[] frightenedFlashingGhostImages;
    private ImageIcon eyesGhostImages;

    private final int startPositionX = 213;
    private final int startPositionY = 209;

    private int panelX = 213;
    private int panelY = 209;

    private int currentGhostImageIndex;
    private int currentGhostOrientation;
    private int speed = 2;
    private final Pacman pacman;
    private final Object monitor;

    private final PathFinding pathfinding;
    private List<Node> path;
    private int pathIndex = 0;
    private int nodeTargetX;
    private int nodeTargetY;

    private boolean inGame;
    private JLabel pinkGhostLabel;
    private volatile boolean paused = false;

    private final TimeTracker upgradesTimeTracker;
    private List<Upgrade> upgrades;

    private final TimeTracker frightTimeTracker;
    private GhostState ghostState;

    public PinkGhost(int[][] board, Pacman pacman, boolean inGame, Object monitor){
        loadImages();
        currentGhostImageIndex = 0;
        currentGhostOrientation = 1;
        this.pathfinding = new PathFinding(board);
        this.nodeTargetX = panelX;
        this.nodeTargetY = panelY;
        this.pacman = pacman;
        this.inGame = inGame;
        this.monitor = monitor;

        pinkGhostLabel = new JLabel(pinkGhostImagesRight[0]);
        pinkGhostLabel.setOpaque(true);
        pinkGhostLabel.setBounds(startPositionX, startPositionY, 13, 13);
        pinkGhostLabel.setBackground(Color.black);

        upgradesTimeTracker = new TimeTracker();
        frightTimeTracker = new TimeTracker();
        upgrades = new ArrayList<>();
        ghostState = GhostState.CHASE;
    }

    private void updateGhostIconLoop() {
        while (inGame){
            if (!paused){
                switch (getGhostState()){
                    case FRIGHTENED -> {
                        if (frightTimeTracker.getSecondsPassed() >= 4){
                            pinkGhostLabel.setIcon(frightenedFlashingGhostImages[currentGhostImageIndex]);
                        }else {
                            pinkGhostLabel.setIcon(frightenedGhostImages[currentGhostImageIndex]);
                        }
                    }
                    case SPAWN -> pinkGhostLabel.setIcon(eyesGhostImages);
                    default -> {
                        switch (currentGhostOrientation) {
                            case 0:
                                pinkGhostLabel.setIcon(pinkGhostImagesUp[currentGhostImageIndex]);
                                break;
                            case 1:
                                pinkGhostLabel.setIcon(pinkGhostImagesRight[currentGhostImageIndex]);
                                break;
                            case 2:
                                pinkGhostLabel.setIcon(pinkGhostImagesDown[currentGhostImageIndex]);
                                break;
                            case 3:
                                pinkGhostLabel.setIcon(pinkGhostImagesLeft[currentGhostImageIndex]);
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
        pinkGhostLabel.setBounds(getGhostCordX() + 3, getGhostCordY() + 3, 13, 13);
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

            //todo figure out where to move this
            if (ghostState == GhostState.SPAWN && (getGhostCordX()/boardDimensions == startPositionX/boardDimensions && getGhostCordY()/boardDimensions == startPositionY/boardDimensions)){
                synchronized (monitor){
                    ghostState = GhostState.CHASE;
                    speed = 2;
                }
            }

            boolean isInCage = getGhostCordX() >= cageTopLeftX && getGhostCordX() <= cageBottomRightX  &&
                    getGhostCordY() >= cageTopLeftY && getGhostCordY() <= cageBottomRightY;

            if (isInCage){
//              todo fix passing coords like this
                moveGhost(225, 153);
            }else {
                int[] ghostTarget = getGhostTarget();
                moveGhost(ghostTarget[0], ghostTarget[1]);
            }

            updateGhostLabelPosition();

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
            speed = 1;
        }
        new Thread(this::checkIfFrightenedLoop).start();
    }

    private void checkIfFrightenedLoop(){
        frightTimeTracker.resetTimePassed();
        while (getGhostState() == GhostState.FRIGHTENED){
            if (frightTimeTracker.getSecondsPassed() >= 6){
                synchronized (monitor){
                    speed = 2;
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
            case FRIGHTENED:
                int index = ThreadLocalRandom.current().nextInt(foodCells.size());
                int[] randomCell = foodCells.get(index);
                return new int[]{randomCell[1] * boardDimensions, randomCell[0] * boardDimensions};
            case SPAWN:
                return new int[]{startPositionX, startPositionY};
            default:
                int pacmanPosX, pacmanPosY, pacmanOrientation;
                synchronized (monitor){
                    pacmanPosX = pacman.getPacmanCordX();
                    pacmanPosY = pacman.getPacmanCordY();
                    pacmanOrientation = pacman.getPacmanOrientation();
                }

                int targetPacmanX = pacmanPosX;
                int targetPacmanY = pacmanPosY;
                for (int i = 0; i < 4; i++) {
                    switch (pacmanOrientation) {
                        case 0:
                            if (targetPacmanY - boardDimensions > 0 && board[(targetPacmanY - boardDimensions) / boardDimensions][pacmanPosX / boardDimensions] != 1) {
                                targetPacmanY -= boardDimensions;
                            }
                            break;
                        case 1:
                            if (targetPacmanX + boardDimensions >= board[0].length * boardDimensions ) {
                                targetPacmanX = boardDimensions * 4;
                                break;
                            } else if (board[pacmanPosY / boardDimensions][(targetPacmanX + boardDimensions) / boardDimensions] != 1
//                                && targetPacmanX + boardDimensions < boardDimensions * board[0].length
                            ) {
                                targetPacmanX += boardDimensions;
                            }
                            break;
                        case 2:
                            if (targetPacmanY + boardDimensions < boardDimensions * board.length && board[(targetPacmanY + boardDimensions) / boardDimensions][pacmanPosX / boardDimensions] != 1) {
                                targetPacmanY += boardDimensions;
                            }
                            break;
                        case 3:
                            if (targetPacmanX - boardDimensions <= 0) {
                                targetPacmanX = board[0].length * boardDimensions - boardDimensions * 4;
                                break;
                            }
                            if (targetPacmanX - boardDimensions > 0 && board[pacmanPosY / boardDimensions][(targetPacmanX - boardDimensions) / boardDimensions] != 1) {
                                targetPacmanX -= boardDimensions;
                            }
                            break;
                    }
                }

                int[] targetCell = new int[2];
                targetCell[0] = targetPacmanX;
                targetCell[1] = targetPacmanY;

                return targetCell;

        }
    }

    private void moveGhost(int targetX, int targetY){
        if (path == null || pathIndex >= path.size() || (path.size()/6 <= pathIndex && path.size()/6 > 1)) {
            path = pathfinding.findPath(panelX / boardDimensions, panelY / boardDimensions, targetX / boardDimensions, targetY / boardDimensions);
            pathIndex = 0;
        }

        if (path != null && !path.isEmpty() && pathIndex <= path.size() - 1) {
            Node nextNode = path.get(pathIndex);
            nodeTargetX = nextNode.x * boardDimensions;
            nodeTargetY = nextNode.y * boardDimensions;
        }

        if (panelX < nodeTargetX) {
            panelX += speed;
            currentGhostOrientation = 1;
            if (panelX > nodeTargetX) panelX = nodeTargetX;
        } else if (panelX > nodeTargetX) {
            panelX -= speed;
            currentGhostOrientation = 3;
            if (panelX < nodeTargetX) panelX = nodeTargetX;
        }

        if (panelY < nodeTargetY) {
            panelY += speed;
            currentGhostOrientation = 2;
            if (panelY > nodeTargetY) panelY = nodeTargetY;
        } else if (panelY > nodeTargetY) {
            panelY -= speed;
            currentGhostOrientation = 0;
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

    public JLabel getPinkGhostLabel() {
        return pinkGhostLabel;
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
            currentGhostOrientation = 1;
            speed = 2;
            upgrades.clear();
        }
        updateGhostLabelPosition();
        resume();
    }

    private void loadImages(){
        pinkGhostImagesRight = new ImageIcon[2];
        pinkGhostImagesLeft = new ImageIcon[2];
        pinkGhostImagesUp = new ImageIcon[2];
        pinkGhostImagesDown = new ImageIcon[2];
        frightenedGhostImages = new ImageIcon[2];
        frightenedFlashingGhostImages = new ImageIcon[2];
        eyesGhostImages = new ImageIcon(getClass().getClassLoader().getResource("resources\\ghosts13\\eye.png"));

        for (int i = 0; i < 2; i++) {
            pinkGhostImagesRight[i] = new ImageIcon(getClass().getClassLoader().getResource("resources\\ghosts13\\pinky\\pinky-right-" + i + ".png"));
            pinkGhostImagesLeft[i] = new ImageIcon(getClass().getClassLoader().getResource("resources\\ghosts13\\pinky\\pinky-left-" + i + ".png"));
            pinkGhostImagesUp[i] = new ImageIcon(getClass().getClassLoader().getResource("resources\\ghosts13\\pinky\\pinky-up-" + i + ".png"));
            pinkGhostImagesDown[i] = new ImageIcon(getClass().getClassLoader().getResource("resources\\ghosts13\\pinky\\pinky-down-" + i + ".png"));
            frightenedGhostImages[i] = new ImageIcon(getClass().getClassLoader().getResource("resources\\ghosts13\\edible-ghost-" + i + ".png"));
        }

        frightenedFlashingGhostImages[0] = frightenedGhostImages[0];
        frightenedFlashingGhostImages[1] = new ImageIcon(getClass().getClassLoader().getResource("resources\\ghosts13\\edible-ghost-blink-1.png"));
    }
}
