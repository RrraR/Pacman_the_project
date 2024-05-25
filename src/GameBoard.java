import Characters.Pacman;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

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
    Pacman pacman;

    GameBoard(){
        setPreferredSize(new Dimension(438, 457));
        setBackground(Color.BLACK);
        pacman = new Pacman(boardDimensions, board);
        startAnimation();
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

        pacman.drawPacman(g);

//        System.out.println("x cord: " + panelX + ", y cord: " + panelY);
//        System.out.println("cord x: " + panelX + ", cord y: " + panelY + ", col: " + panelX / 15 + ", row: " + panelY / 15 + ", square: " + board[panelY/15][panelX/15]);

    }

    private void startAnimation() {
        inGame = true;
        Timer timer = new Timer(20, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                pacman.updateImageIndex();
                pacman.movePacman();
                repaint();
            }
        });

        timer.start();
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
