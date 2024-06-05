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
    private int panelY = 266;
    private int currentSpeedX = 3; // Change in x-coordinate per frame
    private int currentSpeedY = 0; // Change in y-coordinate per frame
    private final int initSpeedX = 3;
    private final int initSpeedY = 3;
    private int currentPacmanImageIndex;
    private int currentPacmanOrientation;
    private ImageIcon[] pacmanImagesRight;
    private ImageIcon[] pacmanImagesLeft;
    private ImageIcon[] pacmanImagesUp;
    private ImageIcon[] pacmanImagesDown;
    private ImageIcon[] pacmanDeathImages;
    private boolean inGame;
    public int lives = 3;
    public int amountOfFoodConsumed;
    private int currentPacmanDeathImageIndex;
    private JLabel pacmanLabel;
    private final Thread pacmanAnimationThread;
    private volatile boolean paused = false;

    public Pacman(int boardDimensions, int[][] board, boolean inGame, Object lock){
        this.board = board;
        this.boardDimensions = boardDimensions;
        loadImages();
        currentPacmanImageIndex = 0;
        currentPacmanOrientation = 1;
        this.inGame = inGame;
        this.lock = lock;
        amountOfFoodConsumed = 0;
        pacmanLabel = new JLabel(pacmanImagesRight[0]);
        pacmanLabel.setOpaque(true);
        pacmanLabel.setBounds(panelX, panelY, 13, 13);
        pacmanLabel.setBackground(Color.black);
        pacmanAnimationThread = new Thread(this::updatePacmanIconLoop);
    }

    private void updatePacmanIconLoop() {
        while (inGame){
            if (!paused){
                switch (currentPacmanOrientation) {
                    case 0:
                        pacmanLabel.setIcon(pacmanImagesUp[currentPacmanImageIndex]);
                        break;
                    case 1:
                        pacmanLabel.setIcon(pacmanImagesRight[currentPacmanImageIndex]);
                        break;
                    case 2:
                        pacmanLabel.setIcon(pacmanImagesDown[currentPacmanImageIndex]);
                        break;
                    case 3:
                        pacmanLabel.setIcon(pacmanImagesLeft[currentPacmanImageIndex]);
                        break;
                }
                updateImageIndex();
                try {
                    Thread.sleep(40);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        }

    }

    private void updatePacmanLabelPosition(){
        pacmanLabel.setBounds(panelX, panelY, 13, 13);
    }

    public void updateImageIndex() {
        currentPacmanImageIndex = (currentPacmanImageIndex + 1) % 3;
    }

    @Override
    public void run() {
        pacmanAnimationThread.start();
        //todo this props should not look like this
        while (inGame){
            checkPaused();
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
                    updatePacmanLabelPosition();
                }
            }

            try {
                Thread.sleep(30);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

    public JLabel getPacmanLabel() {
        return pacmanLabel;
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

    public void deathAnimationLoop() {
        currentPacmanDeathImageIndex = currentPacmanOrientation;
//        System.out.println("pacmanAnimationThread " + pacmanAnimationThread.getState());

        for (int i = 0; i < 4; i++) {
            try {
                pacmanLabel.setIcon(pacmanDeathImages[currentPacmanDeathImageIndex]);
                updateDeathImageIndex();

//                System.out.println("pacman death");

                Thread.sleep(200);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public void updateDeathImageIndex() {
        currentPacmanDeathImageIndex = (currentPacmanDeathImageIndex + 1) % 4;
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

    public void updateAmountOfFoodEaten(){
        amountOfFoodConsumed++;
    }
//todo: fix usages of 1

    public void setMoveRight(){
        if (board[panelY/boardDimensions][(panelX)/boardDimensions + 1] != 1) {
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

    public void stopMovement(){
        currentSpeedX = 0;
        currentSpeedY = 0;
    }

    public void resetPosition() {
        synchronized (lock) {
            panelX = startPositionX;
            panelY = startPositionY;
            currentSpeedX = 3;
            currentSpeedY = 0;
            currentPacmanOrientation = 1;
            currentPacmanImageIndex = 0;
            amountOfFoodConsumed = 0;
            resume();
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
        if (currentPacmanOrientation == 0 && board[(panelY + boardDimensions - 3)/ boardDimensions - 1][panelX/ boardDimensions] == 1){
            currentSpeedY = 0;
            currentSpeedX = 0;
            return true;
        }
        if (currentPacmanOrientation == 1 && board[panelY/ boardDimensions][(panelX)/ boardDimensions + 1] == 1) {
            currentSpeedY = 0;
            currentSpeedX = 0;
            return true;
        }
        if (currentPacmanOrientation == 2 && board[(panelY - 1)/ boardDimensions + 1][panelX/ boardDimensions] == 1) {
            currentSpeedY = 0;
            currentSpeedX = 0;
            return true;
        }
        if (currentPacmanOrientation == 3 && board[panelY / boardDimensions][(panelX + boardDimensions - 3)/ boardDimensions - 1] == 1){
            currentSpeedY = 0;
            currentSpeedX = 0;
            return true;
        }

        return false;
    }

    private void loadImages(){
        pacmanImagesRight = new ImageIcon[3];
        pacmanImagesLeft = new ImageIcon[3];
        pacmanImagesUp = new ImageIcon[3];
        pacmanImagesDown = new ImageIcon[3];
        pacmanDeathImages = new ImageIcon[4];


        for (int i = 0; i < 3; i++) {
            pacmanImagesRight[i] = new ImageIcon("D:\\Documents\\uni2\\sem 2\\GUI\\Project\\resources\\pacman13\\mspacman-right_" + i + ".png");
            pacmanImagesLeft[i] = new ImageIcon("D:\\Documents\\uni2\\sem 2\\GUI\\Project\\resources\\pacman13\\mspacman-left_" + i + ".png");
            pacmanImagesUp[i] = new ImageIcon("D:\\Documents\\uni2\\sem 2\\GUI\\Project\\resources\\pacman13\\mspacman-up_" + i + ".png");
            pacmanImagesDown[i] = new ImageIcon("D:\\Documents\\uni2\\sem 2\\GUI\\Project\\resources\\pacman13\\mspacman-down_" + i + ".png");
        }

        pacmanDeathImages[0] = pacmanImagesUp[1];
        pacmanDeathImages[1] = pacmanImagesRight[1];
        pacmanDeathImages[2] = pacmanImagesDown[1];
        pacmanDeathImages[3] = pacmanImagesLeft[1];
    }

}
