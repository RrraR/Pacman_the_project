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
    final static int C=4; // Corner crossroads

//    private static int board[][] = {
//            //-----------------------X---H-------------------------//
//            {W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W},
//            {W,F,F,F,F,F,F,F,F,F,F,F,F,W,W,F,F,F,F,F,F,F,F,F,F,F,F,W},
//            {W,F,C,W,W,C,F,C,W,W,W,C,F,W,W,F,C,W,W,W,C,F,C,W,W,C,F,W},
//            {W,F,W,E,E,W,F,W,E,E,E,W,F,W,W,F,W,E,E,E,W,F,W,E,E,W,F,W},
//            {W,F,C,W,W,C,F,C,W,W,W,C,F,C,C,F,C,W,W,W,C,F,C,W,W,C,F,W},
//            {W,F,F,F,F,F,F,F,F,F,F,F,F,F,F,F,F,F,F,F,F,F,F,F,F,F,F,W},
//            {W,F,W,W,W,W,F,W,W,F,W,W,W,W,W,W,W,W,F,W,W,F,W,W,W,W,F,W},
//            {W,F,W,W,W,W,F,W,W,F,W,W,W,W,W,W,W,W,F,W,W,F,W,W,W,W,F,W},
//            {W,F,F,F,F,F,F,W,W,F,F,F,F,W,W,F,F,F,F,W,W,F,F,F,F,F,F,W},
//            {W,W,W,W,W,W,F,W,W,W,W,W,F,W,W,F,W,W,W,W,W,F,W,W,W,W,W,W},
//            {E,E,E,E,E,W,F,W,W,W,W,W,F,W,W,F,W,W,W,W,W,F,W,E,E,E,E,E},
//            {E,E,E,E,E,W,F,W,W,F,F,F,F,F,F,F,F,F,F,W,W,F,W,E,E,E,E,E},
//            {E,E,E,E,E,W,F,W,W,F,W,W,W,W,W,W,W,W,F,W,W,F,W,E,E,E,E,E},
//            {W,W,W,W,W,W,F,W,W,F,W,E,E,E,E,E,E,W,F,W,W,F,W,W,W,W,W,W},
//            {F,F,F,F,F,F,F,F,F,F,W,E,E,E,E,E,E,W,F,F,F,F,F,F,F,F,F,F},
//            {W,W,W,W,W,W,F,W,W,F,W,E,E,E,E,E,E,W,F,W,W,F,W,W,W,W,W,W},
//            {E,E,E,E,E,W,F,W,W,F,W,W,W,W,W,W,W,W,F,W,W,F,W,E,E,E,E,E},
//            {E,E,E,E,E,W,F,W,W,F,F,F,F,F,F,F,F,F,F,W,W,F,W,E,E,E,E,E},//r17
//            {E,E,E,E,E,W,F,W,W,F,W,W,W,W,W,W,W,W,F,W,W,F,W,E,E,E,E,E},
//            {W,W,W,W,W,W,F,W,W,F,W,W,W,W,W,W,W,W,F,W,W,F,W,W,W,W,W,W},
//            {W,F,F,F,F,F,F,F,F,F,F,F,F,W,W,F,F,F,F,F,F,F,F,F,F,F,F,W},
//            {W,F,W,W,W,W,F,W,W,W,W,W,F,W,W,F,W,W,W,W,W,F,W,W,W,W,F,W},
//            {W,F,W,W,W,W,F,W,W,W,W,W,F,W,W,F,W,W,W,W,W,F,W,W,W,W,F,W},
//            {W,F,F,F,W,W,F,F,F,F,F,F,F,F,F,F,F,F,F,F,F,F,W,W,F,F,F,W},
//            {W,W,W,F,W,W,F,W,W,F,W,W,W,W,W,W,W,W,F,W,W,F,W,W,F,W,W,W},
//            {W,W,W,F,W,W,F,W,W,F,W,W,W,W,W,W,W,W,F,W,W,F,W,W,F,W,W,W},
//            {W,F,F,F,F,F,F,W,W,F,F,F,F,W,W,F,F,F,F,W,W,F,F,F,F,F,F,W},
//            {W,F,W,W,W,W,W,W,W,W,W,W,F,W,W,F,W,W,W,W,W,W,W,W,W,W,F,W},
//            {W,F,W,W,W,W,W,W,W,W,W,W,F,W,W,F,W,W,W,W,W,W,W,W,W,W,F,W},
//            {W,F,F,F,F,F,F,F,F,F,F,F,F,F,F,F,F,F,F,F,F,F,F,F,F,F,F,W},
//            {W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W}
//    };

    private static int board[][] = {
        //-----------------------X---H-------------------------//
        //r23
        //c24
        {W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W},
        {W,F,F,F,F,F,F,F,F,F,F,W,F,F,F,F,F,F,F,F,F,F,W},
        {W,F,W,W,W,F,W,W,W,W,F,W,F,W,W,W,W,F,W,W,W,F,W},
        {W,F,W,W,W,F,W,W,W,W,F,W,F,W,W,W,W,F,W,W,W,F,W},
        {W,F,F,F,F,F,F,F,F,F,F,F,F,F,F,F,F,F,F,F,F,F,W},
        {W,F,W,W,W,F,W,F,W,W,W,W,W,W,W,F,W,F,W,W,W,F,W},
        {W,F,F,F,F,F,W,F,F,F,F,W,F,F,F,F,W,F,F,F,F,F,W},
        {W,W,W,W,W,F,W,W,W,W,F,W,F,W,W,W,W,F,W,W,W,W,W},
        {E,E,E,E,W,F,W,F,F,F,F,F,F,F,F,F,W,F,W,E,E,E,E},
        {E,E,E,E,W,F,W,F,W,W,W,W,W,W,W,F,W,F,W,E,E,E,E},
        {W,W,W,W,W,F,W,F,W,E,E,E,E,E,W,F,W,F,W,W,W,W,W},
        {F,F,F,F,F,F,F,F,W,E,E,E,E,E,W,F,F,F,F,F,F,F,F},
        {W,W,W,W,W,F,W,F,W,E,E,E,E,E,W,F,W,F,W,W,W,W,W},
        {E,E,E,E,W,F,W,F,W,W,W,W,W,W,W,F,W,F,W,E,E,E,E},
        {E,E,E,E,W,F,W,F,F,F,F,F,F,F,F,F,W,F,W,E,E,E,E},
        {W,W,W,W,W,F,W,F,W,W,W,W,W,W,W,F,W,F,W,W,W,W,W},
        {W,F,F,F,F,F,F,F,F,F,F,W,F,F,F,F,F,F,F,F,F,F,W},
        {W,F,W,W,W,F,W,W,W,W,F,W,F,W,W,W,W,F,W,W,W,F,W},
        {W,F,F,F,W,F,F,F,F,F,F,F,F,F,F,F,F,F,W,F,F,F,W},
        {W,W,W,F,W,F,W,F,W,W,W,W,W,W,W,F,W,F,W,F,W,W,W},
        {W,F,F,F,F,F,W,F,F,F,F,W,F,F,F,F,W,F,F,F,F,F,W},
        {W,F,W,W,W,W,W,W,W,W,F,W,F,W,W,W,W,W,W,W,W,F,W},
        {W,F,F,F,F,F,F,F,F,F,F,F,F,F,F,F,F,F,F,F,F,F,W},
        {W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W}
    };

    Boolean inGame = false;
    private int panelX = 210;
    private int panelY = 253;
    private int currentSpeedX = 0; // Change in x-coordinate per frame
    private int currentSpeedY = 0; // Change in y-coordinate per frame
    private int initSpeedX = 2;
    private int initSpeedY = 2;
    private Image[] pacmanImagesRight;
    private Image[] pacmanImagesLeft;
    private Image[] pacmanImagesUp;
    private Image[] pacmanImagesDown;
    private int currentImageIndex;
    private int currentOrientation;

    BufferedImage mapImg = ImageIO.read(new File("D:\\Documents\\uni2\\sem 2\\GUI\\Project\\resources\\pacmanAssets_resizefor10.png"));

    GameBoard() throws IOException {
        setPreferredSize(new Dimension(416, 435));
        setBackground(Color.BLACK);
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
            pacmanImagesRight[i] = new ImageIcon("D:\\Documents\\uni2\\sem 2\\GUI\\Project\\resources\\pacman13\\mspacman-right_" + i + ".png").getImage();
            pacmanImagesLeft[i] = new ImageIcon("D:\\Documents\\uni2\\sem 2\\GUI\\Project\\resources\\pacman13\\mspacman-left_" + i + ".png").getImage();
            pacmanImagesUp[i] = new ImageIcon("D:\\Documents\\uni2\\sem 2\\GUI\\Project\\resources\\pacman13\\mspacman-up_" + i + ".png").getImage();
            pacmanImagesDown[i] = new ImageIcon("D:\\Documents\\uni2\\sem 2\\GUI\\Project\\resources\\pacman13\\mspacman-down_" + i + ".png").getImage();
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        for (int i = 0; i < board[0].length; i++)
        {
            for (int j = 0; j < board.length; j++)
            {
//                if (board[j][i] == C){
//                    g.setColor(Color.blue);
//                    g.drawRoundRect(i * 18, j * 18, 18, 18, 4, 4);
//                }
//                else
                    if (board[j][i] == W)
                {
                    g.setColor(Color.blue);
                    g.drawRect(i * 18,j * 18, 18, 18);
                }
                else if (board[j][i] == F)
                {
                    g.setColor(Color.black);
                    g.fillRect(i * 18,j * 18, 18, 18);
                } else if (board[j][i] == E) {
                    g.setColor(Color.black);
                    g.fillRect(i * 18,j * 18, 18, 18);
                }

//                BufferedImage img = mapImg.getSubimage(i * 15, j * 15, 15, 15);
//                g.drawImage(img, i * 15, j * 15, null);
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
//        System.out.println("x cord: " + panelX + ", y cord: " + panelY);
//        System.out.println("cord x: " + panelX + ", cord y: " + panelY + ", col: " + panelX / 15 + ", row: " + panelY / 15 + ", square: " + board[panelY/15][panelX/15]);

    }

    private void startAnimation() {
        Timer timer = new Timer(40, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                currentImageIndex = (currentImageIndex + 1) % 3;
                movePacman();
                repaint();
            }
        });


        timer.start();
    }


    private boolean checkCollision(){
//        System.out.println(" panel:" + board[panelY/18][panelX/18] + ", row: " + panelY/18 + " cord y: " + panelY+ ", col: " + panelX/18 +  " cord x: " + panelX );

        if (currentOrientation == 0 && board[(panelY + 13)/18 - 1][panelX/18] == W){
            currentSpeedY = 0;
            currentSpeedX = 0;
            System.out.println("go up row: panel:" + board[panelY/18][panelX/18] + ", row: " + panelY/18 + " cord y: " + panelY+ ", col: " + panelX/18 +  " cord x: " + panelX );
            return true;
        }
        if (currentOrientation == 1 && board[panelY/18][(panelX - 2)/18 + 1] == W) {
            currentSpeedY = 0;
            currentSpeedX = 0;
            return true;
        }
        if (currentOrientation == 2 && board[(panelY - 2)/18 + 1][panelX/18] == W) {
            currentSpeedY = 0;
            currentSpeedX = 0;
            return true;
        }
        if (currentOrientation == 3 && board[panelY / 18][(panelX + 14)/ 18 - 1] == W){
            System.out.println("go left row: " + panelY/18 + " cord y: " + panelY + ", col: " + panelX/18 + " cord x: " + panelX);
            currentSpeedY = 0;
            currentSpeedX = 0;
            return true;
        }

        return false;
    }

    private void movePacman() {

        if (panelX - 13 > 0 && panelX < 415 && panelX/18 < 22 && checkCollision()){
            return;
        }

        panelX += currentSpeedX;
        panelY += currentSpeedY;

        //wall passing
        if (panelX <= 0){
            panelX = getWidth() - 20;
        } else if (panelX >= getWidth() - 18) {
            panelX = 0;
        }

        if (panelY <= 0){
            panelY = getHeight() - 20;
        } else if (panelY >= getHeight() - 18) {
            panelY = 0;
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {

        int key = e.getKeyCode();

        if (inGame) {
            if (key == KeyEvent.VK_W || key == KeyEvent.VK_UP) {
                currentSpeedY = -initSpeedY;
                currentSpeedX = 0;
                currentOrientation = 0;
            }
            if (key == KeyEvent.VK_D || key == KeyEvent.VK_RIGHT) {
                currentSpeedY = 0;
                currentSpeedX = initSpeedX;
                currentOrientation = 1;
            }
            if (key == KeyEvent.VK_S || key == KeyEvent.VK_DOWN) {
                currentSpeedX = 0;
                currentSpeedY = initSpeedY;
                currentOrientation = 2;
            }
            if (key == KeyEvent.VK_A || key == KeyEvent.VK_LEFT) {
                currentSpeedX = -initSpeedX;
                currentSpeedY = 0;
                currentOrientation = 3;
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
