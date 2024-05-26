package Characters;

import javax.swing.*;
import java.awt.*;

public class BlueGhost {

    public int panelX = 209;
    public int panelY = 200;
    private Image[] blueGhostImagesRight;
    private Image[] blueGhostImagesLeft;
    private Image[] blueGhostImagesUp;
    private Image[] blueGhostImagesDown;
    private final int[][] board;
    private final int boardDimensions;
    private int currentGhostImageIndex;
    private int currentGhostOrientation;

    public BlueGhost(int boardDimensions, int[][] board){
        this.board = board;
        this.boardDimensions = boardDimensions;
        loadImages();
        currentGhostImageIndex = 0;
        currentGhostOrientation = 1;
    }

    public void drawBlueGhost(Graphics g){
        switch (currentGhostOrientation){
            case 0:
                g.drawImage(blueGhostImagesUp[currentGhostImageIndex], panelX, panelY, null);
                break;
            case 1:
                g.drawImage(blueGhostImagesRight[currentGhostImageIndex], panelX, panelY, null);
                break;
            case 2:
                g.drawImage(blueGhostImagesDown[currentGhostImageIndex], panelX, panelY, null);
                break;
            case 3:
                g.drawImage(blueGhostImagesLeft[currentGhostImageIndex], panelX, panelY, null);
                break;
        }
    }

    public void moveBlueGhost(){
        //TODO: ghost movement
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
}
