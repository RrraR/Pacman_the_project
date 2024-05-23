import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.IOException;

public class GameBoard extends JPanel implements KeyListener {
    final static int W=1; // Wall.
    final static int F=2; // Crossroads with food
    final static int E=3; // Empty crossroads

    private static int board[][] = {
        //-----------------------X---H-------------------------//
        //r23
        //c24                  r
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
        {E,E,E,E,W,F,W,F,F,F,F,F,F,F,F,F,W,F,W,E,E,E,E},//r14
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

    private final int boardDimensions = 19;
    Boolean inGame = false;
    private int panelX = 209;
    private int panelY = 269;
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

    GameBoard() throws IOException {
        setPreferredSize(new Dimension(438, 457));
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

        for (int i = 0; i < board.length; i++)
        {
            for (int j = 0; j < board[0].length; j++)
            {
                if (board[i][j] == W)
                {
                    g.setColor(Color.blue);
                    g.drawRect(j * boardDimensions,i * boardDimensions, boardDimensions, boardDimensions);
                }
            }
        }

        for (int i = 0; i < board.length; i++)
        {
            for (int j = 0; j < board[0].length; j++)
            {
                if (board[i][j] == F || board[i][j] == E)
                {
                    g.setColor(Color.black);
                    g.fillRect(j * boardDimensions,i * boardDimensions, boardDimensions, boardDimensions);
                }
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
        Timer timer = new Timer(20, new ActionListener() {
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

        if (currentOrientation == 0 && board[(panelY + 13)/boardDimensions - 1][panelX/boardDimensions] == W){
            currentSpeedY = 0;
            currentSpeedX = 0;
            return true;
        }
        if (currentOrientation == 1 && board[panelY/boardDimensions][(panelX - 2)/boardDimensions + 1] == W) {
            currentSpeedY = 0;
            currentSpeedX = 0;
            return true;
        }
        if (currentOrientation == 2 && board[(panelY - 2)/boardDimensions + 1][panelX/boardDimensions] == W) {
            currentSpeedY = 0;
            currentSpeedX = 0;
            return true;
        }
        if (currentOrientation == 3 && board[panelY / boardDimensions][(panelX + 13)/ boardDimensions - 1] == W){
            currentSpeedY = 0;
            currentSpeedX = 0;
            return true;
        }

        return false;
    }

    private void recenterPacman() {
        // recenter horizontally
        if (currentOrientation == 0 || currentOrientation == 2) {
            int offsetX = (panelX % boardDimensions < boardDimensions / 2) ? -(panelX % boardDimensions) : (boardDimensions - panelX % boardDimensions);
            panelX += offsetX + 3;
        }

        // recenter vertically
        if (currentOrientation == 1 || currentOrientation == 3) {
            int offsetY = (panelY % boardDimensions < boardDimensions / 2) ? -(panelY % boardDimensions) : (boardDimensions - panelY % boardDimensions);
            panelY += offsetY + 3;
        }
    }

    private void movePacman() {

        if (panelX - 13 > 0 && panelX < 456 && panelX/boardDimensions < 22 && checkCollision()){
            return;
        }

        panelX += currentSpeedX;
        panelY += currentSpeedY;

        //TODO:
        // if we are moving horizontally and there is no turn possible we should only be able to go left or right and no up and down
        // same for up/down

        //wall passing
        if (panelX <= 0){
            panelX = getWidth() - 20;
        } else if (panelX >= getWidth() - boardDimensions) {
            panelX = 0;
        }

        if (panelY <= 0){
            panelY = getHeight() - 20;
        } else if (panelY >= getHeight() - boardDimensions) {
            panelY = 0;
        }

        recenterPacman();
    }

    @Override
    public void keyPressed(KeyEvent e) {

        int key = e.getKeyCode();

        if (inGame) {
            if ((key == KeyEvent.VK_W || key == KeyEvent.VK_UP) && board[panelY/boardDimensions - 1][panelX/boardDimensions] != W) {
                currentSpeedY = -initSpeedY;
                currentSpeedX = 0;
                currentOrientation = 0;
            }
            if ((key == KeyEvent.VK_D || key == KeyEvent.VK_RIGHT) && board[panelY/boardDimensions][(panelX)/boardDimensions + 1] != W) {
                currentSpeedY = 0;
                currentSpeedX = initSpeedX;
                currentOrientation = 1;
            }
            if ((key == KeyEvent.VK_S || key == KeyEvent.VK_DOWN) && board[panelY/boardDimensions + 1][panelX/boardDimensions] != W) {
                currentSpeedX = 0;
                currentSpeedY = initSpeedY;
                currentOrientation = 2;
            }
            if ((key == KeyEvent.VK_A || key == KeyEvent.VK_LEFT) && board[panelY / boardDimensions][(panelX + 13)/ boardDimensions - 1] != W) {
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
