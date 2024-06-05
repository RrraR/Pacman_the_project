package Characters;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class RedGhost implements Runnable {

    private final int startPositionX = 225;
    private final int startPositionY = 153;

    private int panelX = 225;
    private int panelY = 153;
    private ImageIcon[] redGhostImagesRight;
    private ImageIcon[] redGhostImagesLeft;
    private ImageIcon[] redGhostImagesUp;
    private ImageIcon[] redGhostImagesDown;
    private final int[][] board;
    private final int boardDimensions;
    private int currentGhostImageIndex;
    private int currentGhostOrientation;
    private int nodeTargetX;
    private int nodeTargetY;
    private int speed = 2;
    private final Pacman pacman;
    private final Object monitor;

    private PathFinding pathfinding;
    private List<Node> path;
    private int pathIndex = 0;
    boolean inGame;
    private boolean ghostIsReleased;
    private JLabel redGhostLabel;
    private volatile boolean paused = false;

    public RedGhost(int boardDimensions, int[][] board, Pacman pacman, boolean inGame, Object monitor){
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

        redGhostLabel = new JLabel(redGhostImagesRight[0]);
        redGhostLabel.setOpaque(true);
        redGhostLabel.setBounds(startPositionX, startPositionY, 13, 13);
        redGhostLabel.setBackground(Color.black);
    }

    private void updateRedGhostIconLoop() {
        while (inGame){
            if (!paused){
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
                updateImageIndex();
            }
            try {
                Thread.sleep(40);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }

    }

    private void updateRedGhostLabelPosition(){
        redGhostLabel.setBounds(panelX + 3, panelY + 3, 13, 13);
    }

    public void updateImageIndex() {
        currentGhostImageIndex = (currentGhostImageIndex + 1) % 2;
    }

    @Override
    public void run() {
        new Thread(this::updateRedGhostIconLoop).start();
        while (inGame){
            checkPaused();
            if (ghostIsReleased){
                int pacmanPosX, pacmanPosY;
                synchronized (monitor){
                    pacmanPosX = pacman.getPacmanCordX();
                    pacmanPosY = pacman.getPacmanCordY();
                }

                moveRedGhost(pacmanPosX, pacmanPosY);
            }
            else {
                ghostIsReleased = true;
                moveRedGhost(285, 153);
            }

            try {
                Thread.sleep(30);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
//                inGame = false;
            }

        }
    }

    private void moveRedGhost(int targetX, int targetY){
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

        updateRedGhostLabelPosition();

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

    public JLabel getRedGhostLabel() {
        return redGhostLabel;
    }

    public int getRedGhostCordX(){
        synchronized (monitor) {
            return panelX;
        }
    }

    public int getRedGhostCordY() {
        synchronized (monitor) {
            return panelY;
        }
    }

    public void stopMovement(){
        speed = 0;
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
        resume();
        updateRedGhostLabelPosition();

    }

    private void loadImages(){

        redGhostImagesRight = new ImageIcon[2];
        redGhostImagesLeft = new ImageIcon[2];
        redGhostImagesUp = new ImageIcon[2];
        redGhostImagesDown = new ImageIcon[2];

        for (int i = 0; i < 2; i++) {
            redGhostImagesRight[i] = new ImageIcon("D:\\Documents\\uni2\\sem 2\\GUI\\Project\\resources\\ghosts13\\blinky\\blinky-right-" + i + ".png");
            redGhostImagesLeft[i] = new ImageIcon("D:\\Documents\\uni2\\sem 2\\GUI\\Project\\resources\\ghosts13\\blinky\\blinky-left-" + i + ".png");
            redGhostImagesUp[i] = new ImageIcon("D:\\Documents\\uni2\\sem 2\\GUI\\Project\\resources\\ghosts13\\blinky\\blinky-up-" + i + ".png");
            redGhostImagesDown[i] = new ImageIcon("D:\\Documents\\uni2\\sem 2\\GUI\\Project\\resources\\ghosts13\\blinky\\blinky-down-" + i + ".png");
        }
    }
}
