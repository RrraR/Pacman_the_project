package Characters;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.PriorityQueue;

public class RedGhost {

    final static int W=1; // Wall.
    final static int F=2; // Crossroads with food
    final static int E=3; // Empty crossroads

    public int panelX = 225;
    public int panelY = 155;
    private Image[] redGhostImagesRight;
    private Image[] redGhostImagesLeft;
    private Image[] redGhostImagesUp;
    private Image[] redGhostImagesDown;
    private final int[][] board;
    private final int boardDimensions;
    private int currentGhostImageIndex;
    private int currentGhostOrientation;
    private int currentSpeedX = 0; // Change in x-coordinate per frame
    private int currentSpeedY = 0; // Change in y-coordinate per frame
    private final int initSpeedX = 2;
    private final int initSpeedY = 2;
    private int targetX;
    private int targetY;
    private int speed = 2;

    private PathFinding pathfinding;
    private List<Node> path;
    private int pathIndex = 0;

    public RedGhost(int boardDimensions, int[][] board){
        this.board = board;
        this.boardDimensions = boardDimensions;
        loadImages();
        currentGhostImageIndex = 0;
        currentGhostOrientation = 1;
        this.pathfinding = new PathFinding(board);
        this.targetX = panelX;
        this.targetY = panelY;
    }

    public void drawRedGhost(Graphics g){
        switch (currentGhostOrientation){
            case 0:
                g.drawImage(redGhostImagesUp[currentGhostImageIndex], panelX + 3, panelY, null);
                break;
            case 1:
                g.drawImage(redGhostImagesRight[currentGhostImageIndex], panelX, panelY + 3, null);
                break;
            case 2:
                g.drawImage(redGhostImagesDown[currentGhostImageIndex], panelX + 3, panelY, null);
                break;
            case 3:
                g.drawImage(redGhostImagesLeft[currentGhostImageIndex], panelX, panelY + 3, null);
                break;
        }
    }

    public void updateImageIndex() {
        currentGhostImageIndex = (currentGhostImageIndex + 1) % 2;
    }

    private void loadImages(){

        redGhostImagesRight = new Image[2];
        redGhostImagesLeft = new Image[2];
        redGhostImagesUp = new Image[2];
        redGhostImagesDown = new Image[2];

        for (int i = 0; i < 2; i++) {
            redGhostImagesRight[i] = new ImageIcon("D:\\Documents\\uni2\\sem 2\\GUI\\Project\\resources\\ghosts13\\blinky\\blinky-right-" + i + ".png").getImage();
            redGhostImagesLeft[i] = new ImageIcon("D:\\Documents\\uni2\\sem 2\\GUI\\Project\\resources\\ghosts13\\blinky\\blinky-left-" + i + ".png").getImage();
            redGhostImagesUp[i] = new ImageIcon("D:\\Documents\\uni2\\sem 2\\GUI\\Project\\resources\\ghosts13\\blinky\\blinky-up-" + i + ".png").getImage();
            redGhostImagesDown[i] = new ImageIcon("D:\\Documents\\uni2\\sem 2\\GUI\\Project\\resources\\ghosts13\\blinky\\blinky-down-" + i + ".png").getImage();
        }
    }

    public void moveRedGhost(int pacmanPosX, int pacmanPosY){
        if (path == null || (panelX/boardDimensions == targetX/boardDimensions && panelY/boardDimensions == targetY/boardDimensions)) {
            List<Node> newPath = pathfinding.findPath(panelX / boardDimensions, panelY / boardDimensions, pacmanPosX / boardDimensions, pacmanPosY / boardDimensions);

            if (path == null || !path.equals(newPath)){
                pathIndex = 0;
                path = newPath;
            }
        }

        if (path != null && !path.isEmpty()) {
            Node nextNode = path.get(pathIndex);
            targetX = nextNode.x * boardDimensions;
            targetY = nextNode.y * boardDimensions;
        }

        if (panelX < targetX) {
            panelX += speed;
            currentGhostOrientation = 1;
            if (panelX > targetX) panelX = targetX;
        } else if (panelX > targetX) {
            panelX -= speed;
            currentGhostOrientation = 3;
            if (panelX < targetX) panelX = targetX;
        }

        if (panelY < targetY) {
            panelY += speed;
            currentGhostOrientation = 2;
            if (panelY > targetY) panelY = targetY;
        } else if (panelY > targetY) {
            panelY -= speed;
            currentGhostOrientation = 0;
            if (panelY < targetY) panelY = targetY;
        }

        if (panelX == targetX && panelY == targetY && path != null) {
            pathIndex++;
            if (pathIndex < path.size()) {
                Node nextNode = path.get(pathIndex);
                targetX = nextNode.x * boardDimensions;
                targetY = nextNode.y * boardDimensions;
            }
        }
    }
}
