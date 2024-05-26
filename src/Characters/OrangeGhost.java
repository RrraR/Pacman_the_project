package Characters;

import javax.swing.*;
import java.awt.*;

public class OrangeGhost {
    private Image[] orangeGhostImagesRight;
    private Image[] orangeGhostImagesLeft;
    private Image[] orangeGhostImagesUp;
    private Image[] orangeGhostImagesDown;

    public OrangeGhost(){
        loadImages();
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
}
