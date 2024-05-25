package Characters;

import javax.swing.*;
import java.awt.*;

public class PinkGhost {

    private Image[] pinkGhostImagesRight;
    private Image[] pinkGhostImagesLeft;
    private Image[] pinkGhostImagesUp;
    private Image[] pinkGhostImagesDown;

    public PinkGhost(){
        loadImages();
    }

    private void loadImages(){
        pinkGhostImagesRight = new Image[3];
        pinkGhostImagesLeft = new Image[3];
        pinkGhostImagesUp = new Image[3];
        pinkGhostImagesDown = new Image[3];

        for (int i = 0; i < 3; i++) {
            pinkGhostImagesRight[i] = new ImageIcon("D:\\Documents\\uni2\\sem 2\\GUI\\Project\\resources\\ghosts13\\pinky\\pinky-right-" + i + ".png").getImage();
            pinkGhostImagesLeft[i] = new ImageIcon("D:\\Documents\\uni2\\sem 2\\GUI\\Project\\resources\\ghosts13\\pinky\\pinky-left-" + i + ".png").getImage();
            pinkGhostImagesUp[i] = new ImageIcon("D:\\Documents\\uni2\\sem 2\\GUI\\Project\\resources\\ghosts13\\pinky\\pinky-up-" + i + ".png").getImage();
            pinkGhostImagesDown[i] = new ImageIcon("D:\\Documents\\uni2\\sem 2\\GUI\\Project\\resources\\ghosts13\\pinky\\pinky-down-" + i + ".png").getImage();
        }
    }
}
