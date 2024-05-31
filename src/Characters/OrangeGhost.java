package Characters;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class OrangeGhost implements Runnable {
    private Image[] orangeGhostImagesRight;
    private Image[] orangeGhostImagesLeft;
    private Image[] orangeGhostImagesUp;
    private Image[] orangeGhostImagesDown;

    public int panelX = 225;
    public int panelY = 155;
    private final int boardDimensions;
    private int currentGhostImageIndex;
    private int currentGhostOrientation;
    private int nodeTargetX;
    private int nodeTargetY;
    private int speed = 2;
    private PathFinding pathfinding;
    private List<Node> path;
    private int pathIndex = 0;
    private Pacman pacman;
    private final int[][] board;
    private boolean inGame;

    public OrangeGhost(int boardDimensions, int[][] board, Pacman pacman, boolean inGame){
        this.boardDimensions = boardDimensions;
        loadImages();
        currentGhostImageIndex = 0;
        currentGhostOrientation = 1;
        this.pathfinding = new PathFinding(board);
        this.nodeTargetX = panelX;
        this.nodeTargetY = panelY;
        this.pacman = pacman;
        this.board = board;
        this.inGame = true;
    }

    public void drawPinkGhost(Graphics g){
        switch (currentGhostOrientation){
            case 0:
                g.drawImage(orangeGhostImagesUp[currentGhostImageIndex], panelX + 3, panelY, null);
                break;
            case 1:
                g.drawImage(orangeGhostImagesRight[currentGhostImageIndex], panelX, panelY + 3, null);
                break;
            case 2:
                g.drawImage(orangeGhostImagesDown[currentGhostImageIndex], panelX + 3, panelY, null);
                break;
            case 3:
                g.drawImage(orangeGhostImagesLeft[currentGhostImageIndex], panelX, panelY + 3, null);
                break;
        }
    }

    public void updateImageIndex() {
        currentGhostImageIndex = (currentGhostImageIndex + 1) % 2;
    }

    private void loadImages(){
        orangeGhostImagesRight = new Image[2];
        orangeGhostImagesLeft = new Image[2];
        orangeGhostImagesUp = new Image[2];
        orangeGhostImagesDown = new Image[2];

        for (int i = 0; i < 2; i++) {
            orangeGhostImagesRight[i] = new ImageIcon("D:\\Documents\\uni2\\sem 2\\GUI\\Project\\resources\\ghosts13\\sue\\sue-right-" + i + ".png").getImage();
            orangeGhostImagesLeft[i] = new ImageIcon("D:\\Documents\\uni2\\sem 2\\GUI\\Project\\resources\\ghosts13\\sue\\sue-left-" + i + ".png").getImage();
            orangeGhostImagesUp[i] = new ImageIcon("D:\\Documents\\uni2\\sem 2\\GUI\\Project\\resources\\ghosts13\\sue\\sue-up-" + i + ".png").getImage();
            orangeGhostImagesDown[i] = new ImageIcon("D:\\Documents\\uni2\\sem 2\\GUI\\Project\\resources\\ghosts13\\sue\\sue-down-" + i + ".png").getImage();
        }
    }

    @Override
    public void run() {
        while (inGame){
            int pacmanPosX = pacman.panelX;
            int pacmanPosY = pacman.panelY;
            int targetGhostX = boardDimensions + 3;
            int targetGhostY = board.length * boardDimensions - boardDimensions * 2;

            if (Math.abs((pacmanPosX - panelX)/boardDimensions) > 8 || Math.abs((pacmanPosY - panelY)/boardDimensions) > 8){
                targetGhostX = pacmanPosX;
                targetGhostY = pacmanPosY;
            }else{
                targetGhostX = boardDimensions + 3;
                targetGhostY = board.length * boardDimensions - boardDimensions * 2;
            }

            moveOrangeGhost(targetGhostX, targetGhostY);
            updateImageIndex();

            try {
                Thread.sleep(30);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
//                inGame = false;
            }
        }
    }

    private void moveOrangeGhost(int targetX, int targetY){

        if (path == null || pathIndex >= path.size() || (path.size()/5 <= pathIndex && path.size()/5 > 1)) {
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
