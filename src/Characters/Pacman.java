package Characters;

import javax.swing.*;
import java.awt.*;

public class Pacman implements Runnable {

    private final int startPositionX = 209;
    private final int startPositionY = 269;

    private final int[][] board;
    private final int boardDimensions;
    private final Object lock;
    private int panelX = 209;
    private int panelY = 269;
    private int currentSpeedX = 3; // Change in x-coordinate per frame
    private int currentSpeedY = 0; // Change in y-coordinate per frame
    private final int initSpeedX = 3;
    private final int initSpeedY = 3;
    private int currentPacmanImageIndex;
    private int currentPacmanOrientation;
    private Image[] pacmanImagesRight;
    private Image[] pacmanImagesLeft;
    private Image[] pacmanImagesUp;
    private Image[] pacmanImagesDown;
    private Image[] pacmanDeathImages;
    private boolean inGame;
    public int lives = 3;

    public OnDeathCallback deathCallback;


    public Pacman(int boardDimensions, int[][] board, boolean inGame, OnDeathCallback deathCallback, Object lock){
        this.board = board;
        this.boardDimensions = boardDimensions;
        loadImages();
        currentPacmanImageIndex = 0;
        currentPacmanOrientation = 1;
        this.inGame = true;
        this.deathCallback = deathCallback;
        this.lock = lock;
    }

    public int getPacmanCordX(){
        synchronized (lock) {
            return panelX;
        }
    }

    public int getPacmanCordY() {
        synchronized (lock) {
            return panelY;
        }
    }

    public int getPacmanOrientation(){
        synchronized (lock) {
            return currentPacmanOrientation;
        }
    }

    public void drawPacman(Graphics g){
        switch (currentPacmanOrientation){
            case 0:
                g.drawImage(pacmanImagesUp[currentPacmanImageIndex], panelX, panelY, null);
                break;
            case 1:
                g.drawImage(pacmanImagesRight[currentPacmanImageIndex], panelX, panelY, null);
                break;
            case 2:
                g.drawImage(pacmanImagesDown[currentPacmanImageIndex], panelX, panelY, null);
                break;
            case 3:
                g.drawImage(pacmanImagesLeft[currentPacmanImageIndex], panelX, panelY, null);
                break;
        }
    }

    public void updateImageIndex() {
        currentPacmanImageIndex = (currentPacmanImageIndex + 1) % 3;
    }
//todo: fix usages of 1

    public void setMoveRight(){
        if (board[panelY/boardDimensions][(panelX)/boardDimensions + 1] != 1)
        {
            currentSpeedY = 0;
            currentSpeedX = initSpeedX;
            currentPacmanOrientation = 1;
        }
    }

    public void setMoveLeft(){
        if (board[panelY / boardDimensions][(panelX + 13)/ boardDimensions - 1] != 1){
            currentSpeedX = -initSpeedX;
            currentSpeedY = 0;
            currentPacmanOrientation = 3;
        }
    }

    public void setMoveUp(){
        if (board[panelY/boardDimensions - 1][panelX/boardDimensions] != 1){
            currentSpeedY = -initSpeedY;
            currentSpeedX = 0;
            currentPacmanOrientation = 0;
        }
    }

    public void setMoveDown(){
        if (board[panelY/boardDimensions + 1][panelX/boardDimensions] != 1){
            currentSpeedX = 0;
            currentSpeedY = initSpeedY;
            currentPacmanOrientation = 2;
        }
    }

    @Override
    public void run() {
        while (inGame){
            synchronized (lock){
                if (panelX - 13 > 0 && panelX < board.length * boardDimensions && panelX / boardDimensions < 22 && checkCollision()){
//                    return;
                }else {
                    panelX += currentSpeedX;
                    panelY += currentSpeedY;

                    //wall passing
                    if (panelX <= 0){
                        panelX = board.length * boardDimensions - 20;
                    } else if (panelX >= board.length * boardDimensions - boardDimensions) {
                        panelX = 0;
                    }

                    recenterPacman();
                }
            }

            updateImageIndex();

            try {
                Thread.sleep(30);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

    private void recenterPacman() {
        // recenter horizontally
        if (currentPacmanOrientation == 0 || currentPacmanOrientation == 2) {
            int offsetX = (panelX % boardDimensions < boardDimensions / 2) ? -(panelX % boardDimensions) : (boardDimensions - panelX % boardDimensions);
            panelX += offsetX + 3;
        }

        // recenter vertically
        if (currentPacmanOrientation == 1 || currentPacmanOrientation == 3) {
            int offsetY = (panelY % boardDimensions < boardDimensions / 2) ? -(panelY % boardDimensions) : (boardDimensions - panelY % boardDimensions);
            panelY += offsetY + 3;
        }
    }

    private boolean checkCollision(){
//        System.out.println(" panel:" + board[panelY/18][panelX/18] + ", row: " + panelY/18 + " cord y: " + panelY+ ", col: " + panelX/18 +  " cord x: " + panelX );

        // TODO: refactor orientations for all characters to be enum like Direction.LEFT
        if (currentPacmanOrientation == 0 && board[(panelY + 13)/ boardDimensions - 1][panelX/ boardDimensions] == 1){
            currentSpeedY = 0;
            currentSpeedX = 0;
            return true;
        }
        if (currentPacmanOrientation == 1 && board[panelY/ boardDimensions][(panelX - 2)/ boardDimensions + 1] == 1) {
            currentSpeedY = 0;
            currentSpeedX = 0;
            return true;
        }
        if (currentPacmanOrientation == 2 && board[(panelY - 2)/ boardDimensions + 1][panelX/ boardDimensions] == 1) {
            currentSpeedY = 0;
            currentSpeedX = 0;
            return true;
        }
        if (currentPacmanOrientation == 3 && board[panelY / boardDimensions][(panelX + 13)/ boardDimensions - 1] == 1){
            currentSpeedY = 0;
            currentSpeedX = 0;
            return true;
        }

//       todo add ghost collision detection
//        synchronized (lock) {
//            if (deathCallback != null) {
//                System.out.println("pacman collision ");
//            }
//        }

        return false;
    }

    private void loadImages(){
        pacmanImagesRight = new Image[3];
        pacmanImagesLeft = new Image[3];
        pacmanImagesUp = new Image[3];
        pacmanImagesDown = new Image[3];
        pacmanDeathImages = new Image[4];


        for (int i = 0; i < 3; i++) {
            pacmanImagesRight[i] = new ImageIcon("D:\\Documents\\uni2\\sem 2\\GUI\\Project\\resources\\pacman13\\mspacman-right_" + i + ".png").getImage();
            pacmanImagesLeft[i] = new ImageIcon("D:\\Documents\\uni2\\sem 2\\GUI\\Project\\resources\\pacman13\\mspacman-left_" + i + ".png").getImage();
            pacmanImagesUp[i] = new ImageIcon("D:\\Documents\\uni2\\sem 2\\GUI\\Project\\resources\\pacman13\\mspacman-up_" + i + ".png").getImage();
            pacmanImagesDown[i] = new ImageIcon("D:\\Documents\\uni2\\sem 2\\GUI\\Project\\resources\\pacman13\\mspacman-down_" + i + ".png").getImage();
        }

        pacmanDeathImages[0] = pacmanImagesRight[1];
        pacmanDeathImages[1] = pacmanImagesLeft[1];
        pacmanDeathImages[2] = pacmanImagesUp[1];
        pacmanDeathImages[3] = pacmanImagesDown[1];


    }

}
