package Characters;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class PinkGhost implements Runnable {

    private Image[] pinkGhostImagesRight;
    private Image[] pinkGhostImagesLeft;
    private Image[] pinkGhostImagesUp;
    private Image[] pinkGhostImagesDown;

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
    private final Object lock;

    public PinkGhost(int boardDimensions, int[][] board, Pacman pacman, boolean inGame, Object lock){
        this.boardDimensions = boardDimensions;
        loadImages();
        currentGhostImageIndex = 0;
        currentGhostOrientation = 1;
        this.pathfinding = new PathFinding(board);
        this.nodeTargetX = panelX;
        this.nodeTargetY = panelY;
        this.pacman = pacman;
        this.board = board;
        this.inGame = false;
        this.lock = lock;
    }

    public void drawPinkGhost(Graphics g){
        switch (currentGhostOrientation){
            case 0:
                g.drawImage(pinkGhostImagesUp[currentGhostImageIndex], panelX + 3, panelY, null);
                break;
            case 1:
                g.drawImage(pinkGhostImagesRight[currentGhostImageIndex], panelX, panelY + 3, null);
                break;
            case 2:
                g.drawImage(pinkGhostImagesDown[currentGhostImageIndex], panelX + 3, panelY, null);
                break;
            case 3:
                g.drawImage(pinkGhostImagesLeft[currentGhostImageIndex], panelX, panelY + 3, null);
                break;
        }
    }

    public void updateImageIndex() {
        currentGhostImageIndex = (currentGhostImageIndex + 1) % 2;
    }

    private void loadImages(){
        pinkGhostImagesRight = new Image[2];
        pinkGhostImagesLeft = new Image[2];
        pinkGhostImagesUp = new Image[2];
        pinkGhostImagesDown = new Image[2];

        for (int i = 0; i < 2; i++) {
            pinkGhostImagesRight[i] = new ImageIcon("D:\\Documents\\uni2\\sem 2\\GUI\\Project\\resources\\ghosts13\\pinky\\pinky-right-" + i + ".png").getImage();
            pinkGhostImagesLeft[i] = new ImageIcon("D:\\Documents\\uni2\\sem 2\\GUI\\Project\\resources\\ghosts13\\pinky\\pinky-left-" + i + ".png").getImage();
            pinkGhostImagesUp[i] = new ImageIcon("D:\\Documents\\uni2\\sem 2\\GUI\\Project\\resources\\ghosts13\\pinky\\pinky-up-" + i + ".png").getImage();
            pinkGhostImagesDown[i] = new ImageIcon("D:\\Documents\\uni2\\sem 2\\GUI\\Project\\resources\\ghosts13\\pinky\\pinky-down-" + i + ".png").getImage();
        }
    }

    public int getPinkGhostCordX(){
        synchronized (lock) {
            return panelX;
        }
    }

    public int getPinkGhostCordY() {
        synchronized (lock) {
            return panelY;
        }
    }

    @Override
    public void run() {
        inGame = true;

        while (inGame){

            int pacmanPosX, pacmanPosY, pacmanOrientation;

            synchronized (lock){
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
            movePinkGhost(targetPacmanX, targetPacmanY);
            updateImageIndex();

            try {
                Thread.sleep(30);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
//                inGame = false;
            }
        }

    }

    private void movePinkGhost(int targetX, int targetY){
        if (path == null || pathIndex >= path.size() || (path.size()/6 <= pathIndex && path.size()/6 > 1)) {
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
