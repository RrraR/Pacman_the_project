package Characters;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class RedGhost implements Runnable {

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
    private int nodeTargetX;
    private int nodeTargetY;
    private int speed = 2;
    private Pacman pacman;

    private PathFinding pathfinding;
    private List<Node> path;
    private int pathIndex = 0;
    boolean inGame;

    public RedGhost(int boardDimensions, int[][] board, Pacman pacman, boolean inGame){
        this.board = board;
        this.boardDimensions = boardDimensions;
        loadImages();
        currentGhostImageIndex = 0;
        currentGhostOrientation = 1;
        this.pathfinding = new PathFinding(board);
        this.nodeTargetX = panelX;
        this.nodeTargetY = panelY;
        this.pacman = pacman;
        this.inGame = true;
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

    @Override
    public void run() {
        while (inGame){
            int pacmanPosX = pacman.panelX;
            int pacmanPosY = pacman.panelY;

            if (path == null || pathIndex >= path.size() || (path.size()/5 <= pathIndex && path.size()/5 > 1)) {
                path = pathfinding.findPath(panelX / boardDimensions, panelY / boardDimensions, pacmanPosX / boardDimensions, pacmanPosY / boardDimensions);
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

            updateImageIndex();

            try {
                Thread.sleep(30);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
//                inGame = false;
            }
        }
    }
}
