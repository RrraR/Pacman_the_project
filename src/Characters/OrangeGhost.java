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
        orangeGhostImagesRight = new Image[3];
        orangeGhostImagesLeft = new Image[3];
        orangeGhostImagesUp = new Image[3];
        orangeGhostImagesDown = new Image[3];

        for (int i = 0; i < 3; i++) {
            orangeGhostImagesRight[i] = new ImageIcon("D:\\Documents\\uni2\\sem 2\\GUI\\Project\\resources\\ghosts13\\sue\\sue-right-" + i + ".png").getImage();
            orangeGhostImagesLeft[i] = new ImageIcon("D:\\Documents\\uni2\\sem 2\\GUI\\Project\\resources\\ghosts13\\sue\\sue-left-" + i + ".png").getImage();
            orangeGhostImagesUp[i] = new ImageIcon("D:\\Documents\\uni2\\sem 2\\GUI\\Project\\resources\\ghosts13\\sue\\sue-up-" + i + ".png").getImage();
            orangeGhostImagesDown[i] = new ImageIcon("D:\\Documents\\uni2\\sem 2\\GUI\\Project\\resources\\ghosts13\\sue\\sue-down-" + i + ".png").getImage();
        }
    }
}
