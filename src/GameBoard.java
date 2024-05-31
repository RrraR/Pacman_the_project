import Characters.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class GameBoard extends JPanel implements KeyListener, Runnable {
    final static int W=1; // Wall.
    final static int F=2; // Crossroads with food
    final static int E=3; // Empty crossroads
    final static int D=4; // Door crossroads

    private static int board[][] = {
        //-----------------------X---H-------------------------//
        //board.length - cols
        //board[0].length - rows
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
        {E,E,E,E,W,F,W,F,W,W,W,D,W,W,W,F,W,F,W,E,E,E,E},
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
    public Boolean inGame = false;
    private Pacman pacman;
    private RedGhost redGhost;
    private PinkGhost pinkGhost;
    private BlueGhost blueGhost;
    private OrangeGhost orangeGhost;
    private Thread pacmanThread;
    private Thread redGhostThread;
    private Thread pinkGhostThread;
    private Thread blueGhostThread;
    private Thread orangeGhostThread;

    GameBoard(){
        setPreferredSize(new Dimension(438, 457));
        setBackground(Color.BLACK);
        pacman = new Pacman(boardDimensions, board, inGame);
        redGhost = new RedGhost(boardDimensions, board, pacman, inGame);
        pinkGhost = new PinkGhost(boardDimensions, board, pacman, inGame);
        blueGhost = new BlueGhost(boardDimensions, board, pacman, redGhost, inGame);
        orangeGhost = new OrangeGhost(boardDimensions, board, pacman, inGame);
        pacmanThread = new Thread(pacman);
        redGhostThread = new Thread(redGhost);
        pinkGhostThread = new Thread(pinkGhost);
        blueGhostThread = new Thread(blueGhost);
        orangeGhostThread = new Thread(orangeGhost);
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
                else if (board[i][j] == D){
                    g.setColor(Color.magenta);
                    g.fillRect(j * boardDimensions,i * boardDimensions + 6, boardDimensions, 6);
                }
            }
        }

        pacman.drawPacman(g);
        redGhost.drawRedGhost(g);
        pinkGhost.drawPinkGhost(g);
        blueGhost.drawBlueGhost(g);
        orangeGhost.drawPinkGhost(g);
    }

    @Override
    public void run() {
        pacmanThread.start();
        redGhostThread.start();
        pinkGhostThread.start();
        blueGhostThread.start();
        orangeGhostThread.start();

        inGame = true;
        while (inGame){
//            updateCharacterImageIndex();
//            moveCharters();
            repaint();
            try {
                Thread.sleep(30);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                inGame = false;
            }
        }
    }

    private void moveCharters(){

        System.out.println("pacman thread " + pacmanThread.getState());
        System.out.println("redGhostThread thread " + redGhostThread.getState());
        System.out.println("pinkGhostThread thread " + pinkGhostThread.getState());
        System.out.println("blueGhostThread thread " + blueGhostThread.getState());
        System.out.println("orangeGhostThread thread " + orangeGhostThread.getState());

        System.out.println();

//        pacmanThread.run();
//        redGhostThread.run();
//        pinkGhostThread.run();
//        blueGhostThread.run();
    }

    private void updateCharacterImageIndex(){
        pacman.updateImageIndex();
        redGhost.updateImageIndex();
        pinkGhost.updateImageIndex();
        blueGhost.updateImageIndex();
        orangeGhost.updateImageIndex();
    }

    @Override
    public void keyPressed(KeyEvent e) {

        int key = e.getKeyCode();

        if (inGame) {
            if ((key == KeyEvent.VK_W || key == KeyEvent.VK_UP) && board[pacman.panelY/boardDimensions - 1][pacman.panelX/boardDimensions] != W) {
                pacman.setMoveUp();
            }
            if ((key == KeyEvent.VK_D || key == KeyEvent.VK_RIGHT) && board[pacman.panelY/boardDimensions][(pacman.panelX)/boardDimensions + 1] != W) {
                pacman.setMoveRight();
            }
            if ((key == KeyEvent.VK_S || key == KeyEvent.VK_DOWN) && board[pacman.panelY/boardDimensions + 1][pacman.panelX/boardDimensions] != W) {
                pacman.setMoveDown();
            }
            if ((key == KeyEvent.VK_A || key == KeyEvent.VK_LEFT) && board[pacman.panelY / boardDimensions][(pacman.panelX + 13)/ boardDimensions - 1] != W) {
                pacman.setMoveLeft();
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
