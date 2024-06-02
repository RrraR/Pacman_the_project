package Characters;

import Components.GameBoard;
import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import static Components.GameBoard.getNumberOfPelletsLeft;

public class BlueGhost implements Runnable {

    private int panelX = 186;
    private int panelY = 209;
    private Image[] blueGhostImagesRight;
    private Image[] blueGhostImagesLeft;
    private Image[] blueGhostImagesUp;
    private Image[] blueGhostImagesDown;
    private final int[][] board;
    private final int boardDimensions;
    private int currentGhostImageIndex;
    private int currentGhostOrientation;

    private int nodeTargetX;
    private int nodeTargetY;
    private int speed = 2;
    private PathFinding pathfinding;
    private List<Node> path;
    private int pathIndex = 0;
    boolean inGame;
    private List<int[]> foodCells;
    private final Object lock;

    public BlueGhost(int boardDimensions, int[][] board, boolean inGame, List<int[]> foodCells, Object lock){
        this.board = board;
        this.boardDimensions = boardDimensions;
        loadImages();
        this.nodeTargetX = panelX;
        this.nodeTargetY = panelY;
        currentGhostImageIndex = 0;
        currentGhostOrientation = 1;
        this.pathfinding = new PathFinding(board);
        this.inGame = true;
        this.foodCells = foodCells;
        this.lock = lock;
    }

    public void drawBlueGhost(Graphics g){
        switch (currentGhostOrientation){
            case 0:
                g.drawImage(blueGhostImagesUp[currentGhostImageIndex], panelX + 3, panelY, null);
                break;
            case 1:
                g.drawImage(blueGhostImagesRight[currentGhostImageIndex], panelX, panelY + 3, null);
                break;
            case 2:
                g.drawImage(blueGhostImagesDown[currentGhostImageIndex], panelX + 3, panelY, null);
                break;
            case 3:
                g.drawImage(blueGhostImagesLeft[currentGhostImageIndex], panelX, panelY + 3, null);
                break;
        }
    }

    public void updateImageIndex() {
        currentGhostImageIndex = (currentGhostImageIndex + 1) % 2;
    }

    private void loadImages(){

        blueGhostImagesRight = new Image[2];
        blueGhostImagesLeft = new Image[2];
        blueGhostImagesUp = new Image[2];
        blueGhostImagesDown = new Image[2];

        for (int i = 0; i < 2; i++) {
            blueGhostImagesRight[i] = new ImageIcon("D:\\Documents\\uni2\\sem 2\\GUI\\Project\\resources\\ghosts13\\inky\\inky-right-" + i + ".png").getImage();
            blueGhostImagesLeft[i] = new ImageIcon("D:\\Documents\\uni2\\sem 2\\GUI\\Project\\resources\\ghosts13\\inky\\inky-left-" + i + ".png").getImage();
            blueGhostImagesUp[i] = new ImageIcon("D:\\Documents\\uni2\\sem 2\\GUI\\Project\\resources\\ghosts13\\inky\\inky-up-" + i + ".png").getImage();
            blueGhostImagesDown[i] = new ImageIcon("D:\\Documents\\uni2\\sem 2\\GUI\\Project\\resources\\ghosts13\\inky\\inky-down-" + i + ".png").getImage();
        }
    }

    public int getBlueGhostCordX(){
        synchronized (lock) {
            return panelX;
        }
    }

    public int getBlueGhostCordY() {
        synchronized (lock) {
            return panelY;
        }
    }

    @Override
    public void run() {
        while (inGame){
// todo possibly move the if to gameboard @run and start the thread when the condition is reached
            if (foodCells.size() - getNumberOfPelletsLeft() >= 30){
                int index = ThreadLocalRandom.current().nextInt(foodCells.size());
                int[] randomCell = foodCells.get(index);

                //TODO: possibly fix passing randomCell[1] * boardDimensions and randomCell[0] * boardDimensions
                moveBlueGhost(randomCell[1] * boardDimensions, randomCell[0] * boardDimensions);
            }

            updateImageIndex();

            try {
                Thread.sleep(30);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
//                inGame = false;
            }
        }
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
    }
}
