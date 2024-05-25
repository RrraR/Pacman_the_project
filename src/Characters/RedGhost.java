package Characters;

import javax.swing.*;
import java.awt.*;

public class RedGhost {

    private Image[] redGhostImagesRight;
    private Image[] redGhostImagesLeft;
    private Image[] redGhostImagesUp;
    private Image[] redGhostImagesDown;

    public RedGhost(){
        loadImages();
    }

    private void loadImages(){

        redGhostImagesRight = new Image[3];
        redGhostImagesLeft = new Image[3];
        redGhostImagesUp = new Image[3];
        redGhostImagesDown = new Image[3];

        for (int i = 0; i < 3; i++) {
            redGhostImagesRight[i] = new ImageIcon("D:\\Documents\\uni2\\sem 2\\GUI\\Project\\resources\\ghosts13\\blinky\\blinky-right-" + i + ".png").getImage();
            redGhostImagesLeft[i] = new ImageIcon("D:\\Documents\\uni2\\sem 2\\GUI\\Project\\resources\\ghosts13\\blinky\\blinky-left-" + i + ".png").getImage();
            redGhostImagesUp[i] = new ImageIcon("D:\\Documents\\uni2\\sem 2\\GUI\\Project\\resources\\ghosts13\\blinky\\blinky-up-" + i + ".png").getImage();
            redGhostImagesDown[i] = new ImageIcon("D:\\Documents\\uni2\\sem 2\\GUI\\Project\\resources\\ghosts13\\blinky\\blinky-down-" + i + ".png").getImage();
        }
    }
}
