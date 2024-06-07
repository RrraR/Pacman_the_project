package Characters;

import Components.TimeTracker;
import Components.Upgrade;
import Components.UpgradeType;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class PinkGhost implements Runnable {

    private ImageIcon[] pinkGhostImagesRight;
    private ImageIcon[] pinkGhostImagesLeft;
    private ImageIcon[] pinkGhostImagesUp;
    private ImageIcon[] pinkGhostImagesDown;

    private final int startPositionX = 213;
    private final int startPositionY = 209;

    private int panelX = 213;
    private int panelY = 209;
    private final int boardDimensions;
    private int currentGhostImageIndex;
    private int currentGhostOrientation;
    private int nodeTargetX;
    private int nodeTargetY;
    private int speed = 2;
    private PathFinding pathfinding;
    private List<Node> path;
    private int pathIndex = 0;
    private final Pacman pacman;
    private final int[][] board;
    private boolean inGame;
    private final Object monitor;
    private boolean ghostIsReleased;
    private JLabel pinkGhostLabel;
    private volatile boolean paused = false;
    private final TimeTracker timeTracker;
    private List<Upgrade> upgrades;
    private volatile boolean upgradeGenerated = false;


    public PinkGhost(int boardDimensions, int[][] board, Pacman pacman, boolean inGame, Object monitor){
        this.boardDimensions = boardDimensions;
        loadImages();
        currentGhostImageIndex = 0;
        currentGhostOrientation = 1;
        this.pathfinding = new PathFinding(board);
        this.nodeTargetX = panelX;
        this.nodeTargetY = panelY;
        this.pacman = pacman;
        this.board = board;
        this.inGame = inGame;
        this.monitor = monitor;
        ghostIsReleased = false;

        pinkGhostLabel = new JLabel(pinkGhostImagesRight[0]);
        pinkGhostLabel.setOpaque(true);
        pinkGhostLabel.setBounds(startPositionX, startPositionY, 13, 13);
        pinkGhostLabel.setBackground(Color.black);

        timeTracker = new TimeTracker();
        upgrades = new ArrayList<>();
    }

    private void updatePinkGhostIconLoop() {
        while (inGame){
            if (!paused){
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
                updateImageIndex();
            }
            try {
                Thread.sleep(40);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }

    }

    private void updatePinkGhostLabelPosition(){
        pinkGhostLabel.setBounds(panelX + 3, panelY + 3, 13, 13);
    }

    public void updateImageIndex() {
        currentGhostImageIndex = (currentGhostImageIndex + 1) % 2;
    }

    private void generateUpgrade(){
        if (timeTracker.getSecondsPassed() % 5 == 0 && !upgradeGenerated){
            Random random = new Random();
            if (random.nextInt(100) < 25) {
                int x = panelX/boardDimensions;
                int y = panelY/boardDimensions;
                UpgradeType type = UpgradeType.values()[random.nextInt(UpgradeType.values().length)];
                Upgrade upgrade = new Upgrade(x, y, type);
                synchronized (monitor) {
                    upgrades.add(upgrade);
                }
            }
            upgradeGenerated = true;
        } else if (timeTracker.getSecondsPassed() % 5 != 0){
            upgradeGenerated = false;
        }
    }

    public void removeProcessedUpgrades(){
        synchronized (monitor) {
            upgrades.clear();
        }
    }

    @Override
    public void run() {
        new Thread(this::updatePinkGhostIconLoop).start();
        timeTracker.start();
        while (inGame){
            checkPaused();
            if (ghostIsReleased){
                int pacmanPosX, pacmanPosY, pacmanOrientation;

                synchronized (monitor){
                    pacmanPosX = pacman.getPacmanCordX();
                    pacmanPosY = pacman.getPacmanCordY();
                    pacmanOrientation = pacman.getPacmanOrientation();
                }

                int[] target = getGhostTarget(pacmanPosX, pacmanPosY, pacmanOrientation);
                movePinkGhost(target[0], target[1]);
                generateUpgrade();
            }
            else {
                //todo fix passing coords like this
                ghostIsReleased = true;
                movePinkGhost(225, 153);
            }

            try {
                Thread.sleep(30);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }

    }

    private void movePinkGhost(int targetX, int targetY){
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

        updatePinkGhostLabelPosition();
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

    public int getPinkGhostCordX(){
        synchronized (monitor) {
            return panelX;
        }
    }

    public int getPinkGhostCordY() {
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
        updatePinkGhostLabelPosition();
        resume();
    }

    private int[] getGhostTarget(int pacmanPosX, int pacmanPosY, int pacmanOrientation){
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

    private void loadImages(){
        pinkGhostImagesRight = new ImageIcon[2];
        pinkGhostImagesLeft = new ImageIcon[2];
        pinkGhostImagesUp = new ImageIcon[2];
        pinkGhostImagesDown = new ImageIcon[2];

        for (int i = 0; i < 2; i++) {
            pinkGhostImagesRight[i] = new ImageIcon("D:\\Documents\\uni2\\sem 2\\GUI\\Project\\resources\\ghosts13\\pinky\\pinky-right-" + i + ".png");
            pinkGhostImagesLeft[i] = new ImageIcon("D:\\Documents\\uni2\\sem 2\\GUI\\Project\\resources\\ghosts13\\pinky\\pinky-left-" + i + ".png");
            pinkGhostImagesUp[i] = new ImageIcon("D:\\Documents\\uni2\\sem 2\\GUI\\Project\\resources\\ghosts13\\pinky\\pinky-up-" + i + ".png");
            pinkGhostImagesDown[i] = new ImageIcon("D:\\Documents\\uni2\\sem 2\\GUI\\Project\\resources\\ghosts13\\pinky\\pinky-down-" + i + ".png");
        }
    }
}
