package Characters;

import javax.swing.*;
import java.awt.*;

public class Pacman {

    final static int W=1; // Wall.
    final static int F=2; // Crossroads with food
    final static int E=3; // Empty crossroads
    private final int[][] board;
    private final int boardDimensions;

    public int panelX = 209;
    public int panelY = 269;
    private int currentSpeedX = 2; // Change in x-coordinate per frame
    private int currentSpeedY = 0; // Change in y-coordinate per frame
    private final int initSpeedX = 2;
    private final int initSpeedY = 2;
    private int currentPacmanImageIndex;
    private int currentPacmanOrientation;
    private Image[] pacmanImagesRight;
    private Image[] pacmanImagesLeft;
    private Image[] pacmanImagesUp;
    private Image[] pacmanImagesDown;

    public Pacman(int boardDimensions, int[][] board){
        this.board = board;
        this.boardDimensions = boardDimensions;
        loadImages();
        currentPacmanImageIndex = 0;
        currentPacmanOrientation = 1;
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

    public void setMoveRight(){
        currentSpeedY = 0;
        currentSpeedX = initSpeedX;
        currentPacmanOrientation = 1;
    }

    public void setMoveLeft(){
        currentSpeedX = -initSpeedX;
        currentSpeedY = 0;
        currentPacmanOrientation = 3;
    }

    public void setMoveUp(){
        currentSpeedY = -initSpeedY;
        currentSpeedX = 0;
        currentPacmanOrientation = 0;
    }

    public void setMoveDown(){
        currentSpeedX = 0;
        currentSpeedY = initSpeedY;
        currentPacmanOrientation = 2;
    }


    public void movePacman() {

        if (panelX - 13 > 0 && panelX < board.length * 19 && panelX/ boardDimensions < 22 && checkCollision()){
            return;
        }

        panelX += currentSpeedX;
        panelY += currentSpeedY;

        //TODO:
        // if we are moving horizontally and there is no turn possible we should only be able to go left or right and no up and down
        // same for up/down

        //wall passing
        if (panelX <= 0){
            panelX = board.length * boardDimensions - boardDimensions;
        } else if (panelX >= board.length * boardDimensions - boardDimensions) {
            panelX = 0;
        }

        recenterPacman();
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

        if (currentPacmanOrientation == 0 && board[(panelY + 13)/ boardDimensions - 1][panelX/ boardDimensions] == W){
            currentSpeedY = 0;
            currentSpeedX = 0;
            return true;
        }
        if (currentPacmanOrientation == 1 && board[panelY/ boardDimensions][(panelX - 2)/ boardDimensions + 1] == W) {
            currentSpeedY = 0;
            currentSpeedX = 0;
            return true;
        }
        if (currentPacmanOrientation == 2 && board[(panelY - 2)/ boardDimensions + 1][panelX/ boardDimensions] == W) {
            currentSpeedY = 0;
            currentSpeedX = 0;
            return true;
        }
        if (currentPacmanOrientation == 3 && board[panelY / boardDimensions][(panelX + 13)/ boardDimensions - 1] == W){
            currentSpeedY = 0;
            currentSpeedX = 0;
            return true;
        }

        return false;
    }

    private void loadImages(){
        pacmanImagesRight = new Image[3];
        pacmanImagesLeft = new Image[3];
        pacmanImagesUp = new Image[3];
        pacmanImagesDown = new Image[3];

        for (int i = 0; i < 3; i++) {
            pacmanImagesRight[i] = new ImageIcon("D:\\Documents\\uni2\\sem 2\\GUI\\Project\\resources\\pacman13\\mspacman-right_" + i + ".png").getImage();
            pacmanImagesLeft[i] = new ImageIcon("D:\\Documents\\uni2\\sem 2\\GUI\\Project\\resources\\pacman13\\mspacman-left_" + i + ".png").getImage();
            pacmanImagesUp[i] = new ImageIcon("D:\\Documents\\uni2\\sem 2\\GUI\\Project\\resources\\pacman13\\mspacman-up_" + i + ".png").getImage();
            pacmanImagesDown[i] = new ImageIcon("D:\\Documents\\uni2\\sem 2\\GUI\\Project\\resources\\pacman13\\mspacman-down_" + i + ".png").getImage();
        }
    }

}
