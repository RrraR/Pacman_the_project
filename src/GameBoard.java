import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class GameBoard extends JPanel implements KeyListener {
    final static int W=1; // Wall.
    final static int F=2; // Crossroads with food
    final static int E=3; // Empty crossroads
    private static int board[][] = {
            //-----------------------X-----------------------------//
            {W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W},
            {W,F,F,F,F,F,F,F,F,F,F,F,F,W,W,F,F,F,F,F,F,F,F,F,F,F,F,W},
            {W,F,W,W,W,W,F,W,W,W,W,W,F,W,W,F,W,W,W,W,W,F,W,W,W,W,F,W},
            {W,F,W,E,E,W,F,W,E,E,E,W,F,W,W,F,W,E,E,E,W,F,W,E,E,W,F,W},
            {W,F,W,W,W,W,F,W,W,W,W,W,F,W,W,F,W,W,W,W,W,F,W,W,W,W,F,W},
            {W,F,F,F,F,F,F,F,F,F,F,F,F,F,F,F,F,F,F,F,F,F,F,F,F,F,F,W},
            {W,F,W,W,W,W,F,W,W,F,W,W,W,W,W,W,W,W,F,W,W,F,W,W,W,W,F,W},
            {W,F,W,W,W,W,F,W,W,F,W,W,W,W,W,W,W,W,F,W,W,F,W,W,W,W,F,W},
            {W,F,F,F,F,F,F,W,W,F,F,F,F,W,W,F,F,F,F,W,W,F,F,F,F,F,F,W},
            {W,W,W,W,W,W,F,W,W,W,W,W,F,W,W,F,W,W,W,W,W,F,W,W,W,W,W,W},
            {E,E,E,E,E,W,F,W,W,W,W,W,F,W,W,F,W,W,W,W,W,F,W,E,E,E,E,E},
            {E,E,E,E,E,W,F,W,W,F,F,F,F,F,F,F,F,F,F,W,W,F,W,E,E,E,E,E},
            {E,E,E,E,E,W,F,W,W,F,W,W,W,W,W,W,W,W,F,W,W,F,W,E,E,E,E,E},
            {W,W,W,W,W,W,F,W,W,F,W,E,E,E,E,E,E,W,F,W,W,F,W,W,W,W,W,W},
            {F,F,F,F,F,F,F,F,F,F,W,E,E,E,E,E,E,W,F,F,F,F,F,F,F,F,F,F},
            {W,W,W,W,W,W,F,W,W,F,W,E,E,E,E,E,E,W,F,W,W,F,W,W,W,W,W,W},
            {E,E,E,E,E,W,F,W,W,F,W,W,W,W,W,W,W,W,F,W,W,F,W,E,E,E,E,E},
            {E,E,E,E,E,W,F,W,W,F,F,F,F,F,F,F,F,F,F,W,W,F,W,E,E,E,E,E},
            {E,E,E,E,E,W,F,W,W,F,W,W,W,W,W,W,W,W,F,W,W,F,W,E,E,E,E,E},
            {W,W,W,W,W,W,F,W,W,F,W,W,W,W,W,W,W,W,F,W,W,F,W,W,W,W,W,W},
            {W,F,F,F,F,F,F,F,F,F,F,F,F,W,W,F,F,F,F,F,F,F,F,F,F,F,F,W},
            {W,F,W,W,W,W,F,W,W,W,W,W,F,W,W,F,W,W,W,W,W,F,W,W,W,W,F,W},
            {W,F,W,W,W,W,F,W,W,W,W,W,F,W,W,F,W,W,W,W,W,F,W,W,W,W,F,W},
            {W,F,F,F,W,W,F,F,F,F,F,F,F,F,F,F,F,F,F,F,F,F,W,W,F,F,F,W},
            {W,W,W,F,W,W,F,W,W,F,W,W,W,W,W,W,W,W,F,W,W,F,W,W,F,W,W,W},
            {W,W,W,F,W,W,F,W,W,F,W,W,W,W,W,W,W,W,F,W,W,F,W,W,F,W,W,W},
            {W,F,F,F,F,F,F,W,W,F,F,F,F,W,W,F,F,F,F,W,W,F,F,F,F,F,F,W},
            {W,F,W,W,W,W,W,W,W,W,W,W,F,W,W,F,W,W,W,W,W,W,W,W,W,W,F,W},
            {W,F,W,W,W,W,W,W,W,W,W,W,F,W,W,F,W,W,W,W,W,W,W,W,W,W,F,W},
            {W,F,F,F,F,F,F,F,F,F,F,F,F,F,F,F,F,F,F,F,F,F,F,F,F,F,F,W},
            {W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W}
    };

    Boolean inGame = false;
    private Timer timer;
    private int panelX = 210;
    private int panelY = 250;
    private int currentSpeedX = 0; // Change in x-coordinate per frame
    private int currentSpeedY = 0; // Change in y-coordinate per frame
    private int initSpeedX = 3;
    private int initSpeedY = 3;
    private Image[] pacmanImagesRight;
    private Image[] pacmanImagesLeft;
    private Image[] pacmanImagesUp;
    private Image[] pacmanImagesDown;
    private int currentImageIndex;
    private int currentOrientation;

    BufferedImage mapImg = ImageIO.read(new File("D:\\Documents\\uni2\\sem 2\\GUI\\Project\\resources\\pacmanAssets_resizefor15.png"));

    GameBoard() throws IOException {
        setPreferredSize(new Dimension(420, 465));
        currentImageIndex = 0;
        currentOrientation = 1;
        loadImages();
    }

    private void loadImages(){
        pacmanImagesRight = new Image[3];
        pacmanImagesLeft = new Image[3];
        pacmanImagesUp = new Image[3];
        pacmanImagesDown = new Image[3];
        for (int i = 0; i < 3; i++) {
            pacmanImagesRight[i] = new ImageIcon("D:\\Documents\\uni2\\sem 2\\GUI\\Project\\resources\\pacman\\mspacman-right_" + i + ".png").getImage();
            pacmanImagesLeft[i] = new ImageIcon("D:\\Documents\\uni2\\sem 2\\GUI\\Project\\resources\\pacman\\mspacman-left_" + i + ".png").getImage();
            pacmanImagesUp[i] = new ImageIcon("D:\\Documents\\uni2\\sem 2\\GUI\\Project\\resources\\pacman\\mspacman-up_" + i + ".png").getImage();
            pacmanImagesDown[i] = new ImageIcon("D:\\Documents\\uni2\\sem 2\\GUI\\Project\\resources\\pacman\\mspacman-down_" + i + ".png").getImage();
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        for (int i = 0; i < board[0].length; i++)
        {
            for (int j = 0; j < board.length; j++)
            {
                BufferedImage img = mapImg.getSubimage(i * 15, j * 15, 15, 15);
                g.drawImage(img, i * 15, j * 15, null);
            }
        }

        switch (currentOrientation){
            case 0:
                g.drawImage(pacmanImagesUp[currentImageIndex], panelX, panelY, null);
                break;
            case 1:
                g.drawImage(pacmanImagesRight[currentImageIndex], panelX, panelY, null);
                break;
            case 2:
                g.drawImage(pacmanImagesDown[currentImageIndex], panelX, panelY, null);
                break;
            case 3:
                g.drawImage(pacmanImagesLeft[currentImageIndex], panelX, panelY, null);
                break;
        }

    }

    private void startAnimation() {
        timer = new Timer(20, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                currentImageIndex = (currentImageIndex + 1) % 3;
                movePacman();
                repaint();
            }
        });


        timer.start();
    }

    private void movePacman() {
        panelX += currentSpeedX;
        panelY += currentSpeedY;

        if (panelX <= 0){
            panelX = getWidth() - 20;
        } else if (panelX >= getWidth() - 15) {
            panelX = 0;
        }

        if (panelY <= 0){
            panelY = getHeight() - 20;
        } else if (panelY >= getHeight() - 15) {
            panelY = 0;
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {

        int key = e.getKeyCode();

        if (inGame) {
            if (key == KeyEvent.VK_W) {
                currentSpeedY = -initSpeedY;
                currentSpeedX = 0;
                currentOrientation = 0;
            }
            if (key == KeyEvent.VK_A) {
                currentSpeedX = -initSpeedX;
                currentSpeedY = 0;
                currentOrientation = 3;
            }
            if (key == KeyEvent.VK_D) {
                currentSpeedY = 0;
                currentSpeedX = initSpeedX;
                currentOrientation = 1;
            }
            if (key == KeyEvent.VK_S) {
                currentSpeedX = 0;
                currentSpeedY = initSpeedY;
                currentOrientation = 2;
            }

        }
        else{
            if (key == KeyEvent.VK_SPACE) {
                inGame = true;
                currentSpeedY = 0;
                currentSpeedX = initSpeedX;
                startAnimation();
            }
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {

    }

    @Override
    public void keyTyped(KeyEvent e) {

    }
}
