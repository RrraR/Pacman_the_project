package Characters;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import static Components.GameBoard.getNumberOfFoodsLeft;

public class BlueGhost implements Runnable {

    private final int startPositionX = 186;
    private final int startPositionY = 209;

    private int panelX = 186;
    private int panelY = 209;
    private ImageIcon[] blueGhostImagesRight;
    private ImageIcon[] blueGhostImagesLeft;
    private ImageIcon[] blueGhostImagesUp;
    private ImageIcon[] blueGhostImagesDown;
    private final int[][] board;
    private final int boardDimensions;
    private int currentGhostImageIndex;
    private int currentGhostOrientation;
    private final Pacman pacman;

    private int nodeTargetX;
    private int nodeTargetY;
    private int speed = 2;
    private PathFinding pathfinding;
    private List<Node> path;
    private int pathIndex = 0;
    boolean inGame;
    private List<int[]> foodCells;
    private final Object monitor;
    private boolean ghostIsReleased;
    private JLabel blueGhostLabel;
    private volatile boolean paused = false;
    

    public BlueGhost(int boardDimensions, int[][] board, Pacman pacman, boolean inGame, List<int[]> foodCells, Object monitor){
        this.board = board;
        this.boardDimensions = boardDimensions;
        this.pacman = pacman;
        loadImages();
        this.nodeTargetX = panelX;
        this.nodeTargetY = panelY;
        currentGhostImageIndex = 0;
        currentGhostOrientation = 1;
        this.pathfinding = new PathFinding(board);
        this.inGame = inGame;
        this.foodCells = foodCells;
        this.monitor = monitor;
        ghostIsReleased = false;

        blueGhostLabel = new JLabel(blueGhostImagesRight[0]);
        blueGhostLabel.setOpaque(true);
        blueGhostLabel.setBounds(startPositionX, startPositionY, 13, 13);
        blueGhostLabel.setBackground(Color.black);
    }

    private void updateBlueGhostIconLoop() {
        while (inGame){
            if (!paused){
                switch (currentGhostOrientation) {
                    case 0:
                        blueGhostLabel.setIcon(blueGhostImagesUp[currentGhostImageIndex]);
                        break;
                    case 1:
                        blueGhostLabel.setIcon(blueGhostImagesRight[currentGhostImageIndex]);
                        break;
                    case 2:
                        blueGhostLabel.setIcon(blueGhostImagesDown[currentGhostImageIndex]);
                        break;
                    case 3:
                        blueGhostLabel.setIcon(blueGhostImagesLeft[currentGhostImageIndex]);
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

    private void updateBlueGhostLabelPosition(){
        blueGhostLabel.setBounds(panelX + 3, panelY + 3, 13, 13);
    }

    public void updateImageIndex() {
        currentGhostImageIndex = (currentGhostImageIndex + 1) % 2;
    }

    @Override
    public void run() {
        new Thread(this::updateBlueGhostIconLoop).start();
        while (inGame){
            checkPaused();
            if (pacman.amountOfFoodConsumed >= getNumberOfFoodsLeft()/3){
                if (ghostIsReleased){
                    int index = ThreadLocalRandom.current().nextInt(foodCells.size());
                    int[] randomCell = foodCells.get(index);

                    //TODO: possibly fix passing randomCell[1] * boardDimensions and randomCell[0] * boardDimensions
                    moveBlueGhost(randomCell[1] * boardDimensions, randomCell[0] * boardDimensions);
                }else{
                    ghostIsReleased = true;
                    //todo fix passing coords like this
                    moveBlueGhost(225, 153);
                }

            }

            try {
                Thread.sleep(30);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

    public JLabel getBlueGhostLabel() {
        return blueGhostLabel;
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

    public int getBlueGhostCordX(){
        synchronized (monitor) {
            return panelX;
        }
    }

    public int getBlueGhostCordY() {
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
        resume();
        updateBlueGhostLabelPosition();
    }

    private void moveBlueGhost(int targetX, int targetY){
        if (path == null || pathIndex >= path.size()) {
            path = pathfinding.findPath(panelX / boardDimensions, panelY / boardDimensions, targetX / boardDimensions, targetY / boardDimensions);
//            for (Node node: path) {
//                System.out.println("Node " + node.x + " " + node.y);
//            }
//            System.out.println("Pathindex " + pathIndex);
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

        updateBlueGhostLabelPosition();
    }

    private void loadImages(){

        blueGhostImagesRight = new ImageIcon[2];
        blueGhostImagesLeft = new ImageIcon[2];
        blueGhostImagesUp = new ImageIcon[2];
        blueGhostImagesDown = new ImageIcon[2];

        for (int i = 0; i < 2; i++) {
            blueGhostImagesRight[i] = new ImageIcon("D:\\Documents\\uni2\\sem 2\\GUI\\Project\\resources\\ghosts13\\inky\\inky-right-" + i + ".png");
            blueGhostImagesLeft[i] = new ImageIcon("D:\\Documents\\uni2\\sem 2\\GUI\\Project\\resources\\ghosts13\\inky\\inky-left-" + i + ".png");
            blueGhostImagesUp[i] = new ImageIcon("D:\\Documents\\uni2\\sem 2\\GUI\\Project\\resources\\ghosts13\\inky\\inky-up-" + i + ".png");
            blueGhostImagesDown[i] = new ImageIcon("D:\\Documents\\uni2\\sem 2\\GUI\\Project\\resources\\ghosts13\\inky\\inky-down-" + i + ".png");
        }
    }
}
