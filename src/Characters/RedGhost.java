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

public class RedGhost implements Runnable, Ghost {

    private ImageIcon[] redGhostImagesRight;
    private ImageIcon[] redGhostImagesLeft;
    private ImageIcon[] redGhostImagesUp;
    private ImageIcon[] redGhostImagesDown;
    private ImageIcon[] frightenedGhostImages;
    private ImageIcon[] frightenedFlashingGhostImages;
    private ImageIcon eyesGhostImages;

    private final int startPositionX = 215;
    private final int startPositionY = 153;

    private int panelX = 215;
    private int panelY = 153;

    private final int[][] board;
    private final int boardDimensions;
    private int currentGhostImageIndex;
    private int currentGhostOrientation;
    private int speed = 2;
    private final Pacman pacman;
    private final Object monitor;

    private PathFinding pathfinding;
    private List<Node> path;
    private int pathIndex = 0;
    private int nodeTargetX;
    private int nodeTargetY;

    private boolean inGame;
    private volatile boolean ghostIsReleased = false;
    private JLabel redGhostLabel;
    private volatile boolean paused = false;

    private final TimeTracker upgradesTimeTracker;
    private List<Upgrade> upgrades;
    private final List<int[]> foodCells;

    private final TimeTracker frightTimeTracker;
    private GhostState ghostState;

    public RedGhost(int boardDimensions, int[][] board, Pacman pacman, boolean inGame, Object monitor, List<int[]> foodCells){
        this.board = board;
        this.boardDimensions = boardDimensions;
        loadImages();
        currentGhostImageIndex = 0;
        currentGhostOrientation = 1;
        this.pathfinding = new PathFinding(board);
        this.nodeTargetX = panelX;
        this.nodeTargetY = panelY;
        this.pacman = pacman;
        this.inGame = inGame;
        this.monitor = monitor;
        ghostIsReleased = false;
        this.foodCells = foodCells;

        redGhostLabel = new JLabel(redGhostImagesRight[0]);
        redGhostLabel.setOpaque(true);
        redGhostLabel.setBounds(startPositionX, startPositionY, 13, 13);
        redGhostLabel.setBackground(Color.black);

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
                            redGhostLabel.setIcon(frightenedFlashingGhostImages[currentGhostImageIndex]);
                        }else {
                            redGhostLabel.setIcon(frightenedGhostImages[currentGhostImageIndex]);
                        }
                    }
                    case SPAWN -> redGhostLabel.setIcon(eyesGhostImages);
                    default -> {
                        switch (currentGhostOrientation) {
                            case 0:
                                redGhostLabel.setIcon(redGhostImagesUp[currentGhostImageIndex]);
                                break;
                            case 1:
                                redGhostLabel.setIcon(redGhostImagesRight[currentGhostImageIndex]);
                                break;
                            case 2:
                                redGhostLabel.setIcon(redGhostImagesDown[currentGhostImageIndex]);
                                break;
                            case 3:
                                redGhostLabel.setIcon(redGhostImagesLeft[currentGhostImageIndex]);
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
        redGhostLabel.setBounds(panelX + 3, panelY + 3, 13, 13);
    }

    public void updateImageIndex() {
        currentGhostImageIndex = (currentGhostImageIndex + 1) % 2;
    }

    private void generateUpgradeLoop(){
        boolean upgradeGenerated = false;
        upgradesTimeTracker.start();
        while (inGame){
            if ((getGhostState() == GhostState.CHASE || getGhostState() == GhostState.SCATTER) && ghostIsReleased){
                if (upgradesTimeTracker.getSecondsPassed() % 5 == 0 && !upgradeGenerated){
                    if (ThreadLocalRandom.current().nextInt(100) < 25) {
                        int x = panelX/boardDimensions;
                        int y = panelY/boardDimensions;
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
            if (ghostState == GhostState.SPAWN && (panelX/boardDimensions == 213/boardDimensions && panelY/boardDimensions == 209/boardDimensions)){
                ghostState = GhostState.CHASE;
                speed = 2;
            }

            if (!ghostIsReleased){
//                //todo fix passing coords like this
                moveGhost(225, 153);
                synchronized (monitor){
                    ghostIsReleased = true;
                }
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
            ghostIsReleased = false;
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
            //todo
//            case SCATTER:
//                int pacmanPosX, pacmanPosY;
//                synchronized (monitor){
//                    pacmanPosX = pacman.getPacmanCordX();
//                    pacmanPosY = pacman.getPacmanCordY();
//                }
//                return new int[]{pacmanPosX, pacmanPosY};
            case FRIGHTENED:
                int index = ThreadLocalRandom.current().nextInt(foodCells.size());
                int[] randomCell = foodCells.get(index);
                return new int[]{randomCell[1] * boardDimensions, randomCell[0] * boardDimensions};
            case SPAWN:
                return new int[]{213, 209};
            default:
                int pacmanPosX, pacmanPosY;
                synchronized (monitor){
                    pacmanPosX = pacman.getPacmanCordX();
                    pacmanPosY = pacman.getPacmanCordY();
                }
                return new int[]{pacmanPosX, pacmanPosY};
        }
    }

    protected void moveGhost(int targetX, int targetY){
        if (path == null || pathIndex >= path.size() || (path.size()/5 <= pathIndex && path.size()/5 > 1)) {
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

    public JLabel getRedGhostLabel() {
        return redGhostLabel;
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
        panelX = startPositionX;
        panelY = startPositionY;
        path = null;
        pathIndex = 0;
        currentGhostImageIndex = 0;
        currentGhostOrientation = 1;
        speed = 2;
        ghostIsReleased = false;
        upgrades.clear();
        updateGhostLabelPosition();
        resume();
    }

    private void loadImages(){

        redGhostImagesRight = new ImageIcon[2];
        redGhostImagesLeft = new ImageIcon[2];
        redGhostImagesUp = new ImageIcon[2];
        redGhostImagesDown = new ImageIcon[2];
        frightenedGhostImages = new ImageIcon[2];
        frightenedFlashingGhostImages = new ImageIcon[2];
        eyesGhostImages = new ImageIcon("D:\\Documents\\uni2\\sem 2\\GUI\\Project\\resources\\ghosts13\\eye.png");

        for (int i = 0; i < 2; i++) {
            redGhostImagesRight[i] = new ImageIcon("D:\\Documents\\uni2\\sem 2\\GUI\\Project\\resources\\ghosts13\\blinky\\blinky-right-" + i + ".png");
            redGhostImagesLeft[i] = new ImageIcon("D:\\Documents\\uni2\\sem 2\\GUI\\Project\\resources\\ghosts13\\blinky\\blinky-left-" + i + ".png");
            redGhostImagesUp[i] = new ImageIcon("D:\\Documents\\uni2\\sem 2\\GUI\\Project\\resources\\ghosts13\\blinky\\blinky-up-" + i + ".png");
            redGhostImagesDown[i] = new ImageIcon("D:\\Documents\\uni2\\sem 2\\GUI\\Project\\resources\\ghosts13\\blinky\\blinky-down-" + i + ".png");
            frightenedGhostImages[i] = new ImageIcon("D:\\Documents\\uni2\\sem 2\\GUI\\Project\\resources\\ghosts13\\edible-ghost-" + i + ".png");
        }

        frightenedFlashingGhostImages[0] = frightenedGhostImages[0];
        frightenedFlashingGhostImages[1] = new ImageIcon("D:\\Documents\\uni2\\sem 2\\GUI\\Project\\resources\\ghosts13\\edible-ghost-blink-1.png");
//        frightenedFlashingGhostImages[2] = frightenedBlueGhostImages[1];
//        frightenedFlashingGhostImages[3] = frightenedWhiteGhostImages[0];

    }
}
