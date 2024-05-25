package Characters;

import javax.swing.*;
import java.awt.*;

public class BlueGhost {

    private Image[] blueGhostImagesRight;
    private Image[] blueGhostImagesLeft;
    private Image[] blueGhostImagesUp;
    private Image[] blueGhostImagesDown;

    public BlueGhost(){
        loadImages();
    }

    private void loadImages(){

        blueGhostImagesRight = new Image[3];
        blueGhostImagesLeft = new Image[3];
        blueGhostImagesUp = new Image[3];
        blueGhostImagesDown = new Image[3];

        for (int i = 0; i < 3; i++) {
            blueGhostImagesRight[i] = new ImageIcon("D:\\Documents\\uni2\\sem 2\\GUI\\Project\\resources\\ghosts13\\inky\\inky-right-" + i + ".png").getImage();
            blueGhostImagesLeft[i] = new ImageIcon("D:\\Documents\\uni2\\sem 2\\GUI\\Project\\resources\\ghosts13\\inky\\inky-left-" + i + ".png").getImage();
            blueGhostImagesUp[i] = new ImageIcon("D:\\Documents\\uni2\\sem 2\\GUI\\Project\\resources\\ghosts13\\inky\\inky-up-" + i + ".png").getImage();
            blueGhostImagesDown[i] = new ImageIcon("D:\\Documents\\uni2\\sem 2\\GUI\\Project\\resources\\ghosts13\\inky\\inky-down-" + i + ".png").getImage();
        }
    }
}
