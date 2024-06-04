package Components;

import Characters.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.List;

public class GameBoard extends JPanel implements KeyListener, Runnable {
    final static int W=1; // Wall.
    final static int F=2; // Crossroads with food
    final static int E=3; // Empty crossroads
    final static int D=4; // Door crossroad

    public static int board[][] = {
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

    private final Object monitor = new Object();

    private final int boardDimensions = 19;
    public Boolean inGame = false;
    private final Pacman pacman;
    private final RedGhost redGhost;
    private final PinkGhost pinkGhost;
    private final BlueGhost blueGhost;
    private final OrangeGhost orangeGhost;
    private final Thread pacmanThread;
    private final Thread redGhostThread;
    private final Thread pinkGhostThread;
    private final Thread blueGhostThread;
    private final Thread orangeGhostThread;
    public int score;
    private final List<int[]> foodCells;

    private boolean isPacmanDead;

    public GameBoard(){
        setPreferredSize(new Dimension(438, 457));
        setBackground(Color.BLACK);
        foodCells = new ArrayList<>();
        countAllFood();

        pacman = new Pacman(boardDimensions, board, inGame, monitor);
        redGhost = new RedGhost(boardDimensions, board, pacman, inGame, monitor);
        pinkGhost = new PinkGhost(boardDimensions, board, pacman, inGame, monitor);
        blueGhost = new BlueGhost(boardDimensions, board, pacman, inGame, foodCells, monitor);
        orangeGhost = new OrangeGhost(boardDimensions, board, pacman, inGame, foodCells, monitor);
        pacmanThread = new Thread(pacman);
        redGhostThread = new Thread(redGhost);
        pinkGhostThread = new Thread(pinkGhost);
        blueGhostThread = new Thread(blueGhost);
        orangeGhostThread = new Thread(orangeGhost);

        isPacmanDead = false;
    }

    private void countAllFood(){
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[0].length; j++) {
                if (board[i][j] == F) {
                    foodCells.add(new int[]{i, j});
                }
            }
        }
    }

    public static int getNumberOfFoodsLeft(){
        int number = 0;
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[0].length; j++) {
                if (board[i][j] == F) {
                    number++;
                }
            }
        }
        return number;
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
                if (board[i][j] == F)
                {
                    g.setColor(Color.black);
                    g.fillRect(j * boardDimensions,i * boardDimensions, boardDimensions, boardDimensions);
                    g.setColor(Color.yellow);
                    g.fillOval(j * boardDimensions + boardDimensions/2,i * boardDimensions + boardDimensions/2, boardDimensions/4, boardDimensions/4);

                } else if (board[i][j] == E) {
                    g.setColor(Color.black);
                    g.fillRect(j * boardDimensions,i * boardDimensions, boardDimensions, boardDimensions);

                } else if (board[i][j] == D){
                    g.setColor(Color.magenta);
                    g.fillRect(j * boardDimensions,i * boardDimensions + 6, boardDimensions, 6);
                }
            }
        }

        pacman.drawPacman(g, isPacmanDead);
        redGhost.drawRedGhost(g);
        pinkGhost.drawPinkGhost(g);
        blueGhost.drawBlueGhost(g);
        orangeGhost.drawPinkGhost(g);
    }

    @Override
    public void run() {
        try {
            Thread.sleep(1500);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        pacmanThread.start();
        redGhostThread.start();
        pinkGhostThread.start();
        blueGhostThread.start();
        orangeGhostThread.start();

        new Thread(this::ghostCollisionDetectionLoop).start();

        inGame = true;
        while (inGame){
            updateFoodCells();
            repaint();
            try {
                Thread.sleep(30);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                inGame = false;
            }
        }
    }

    private void ghostCollisionDetectionLoop() {
        while (inGame){
            synchronized (monitor){
                if (isCollision(pacman.getPacmanCordX(), pacman.getPacmanCordY(), redGhost.getRedGhostCordX(), redGhost.getRedGhostCordY()) ||
                        isCollision(pacman.getPacmanCordX(), pacman.getPacmanCordY(), pinkGhost.getPinkGhostCordX(), pinkGhost.getPinkGhostCordY()) ||
                        isCollision(pacman.getPacmanCordX(), pacman.getPacmanCordY(), blueGhost.getBlueGhostCordX(), blueGhost.getBlueGhostCordY()) ||
                        isCollision(pacman.getPacmanCordX(), pacman.getPacmanCordY(), orangeGhost.getOrangeGhostCordX(), orangeGhost.getOrangeGhostCordY())) {

                    stopCharacterMovement();
                    isPacmanDead = true;

                    Thread pacmanDeathAnimationThread = new Thread(this::deathAnimationLoop);
                    pacmanDeathAnimationThread.start();
                    try {
                        pacmanDeathAnimationThread.join();
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }

                    pacman.lives--;

                    if (pacman.lives <= 0) {
                        inGame = false;
                    } else {
                        resetPositions();
                        isPacmanDead = false;
                        try {
                            Thread.sleep(500);
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }
                    }

                }
            }
        }
    }

    private void deathAnimationLoop(){
        for (int i = 0; i < 4; i++){
            repaint();
            try {
                Thread.sleep(200);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private void stopCharacterMovement(){
        synchronized (monitor) {
            pacman.stopMovement();
            redGhost.stopMovement();
            pinkGhost.stopMovement();
            blueGhost.stopMovement();
            orangeGhost.stopMovement();
        }
    }

    private void resetPositions() {
        synchronized (monitor) {
            pacman.resetPosition();
            redGhost.resetPosition();
            pinkGhost.resetPosition();
            blueGhost.resetPosition();
            orangeGhost.resetPosition();
        }
    }

    private boolean isCollision(int pacmanX, int pacmanY, int ghostX, int ghostY) {
        return pacmanX/boardDimensions == ghostX/boardDimensions && pacmanY/boardDimensions == ghostY/boardDimensions;
    }

    public String getScore(){
        return String.valueOf(score);
    }

    public String getPacmanLives(){
        return String.valueOf(pacman.lives);
    }

    private void updateFoodCells(){
        int pacmanPosX, pacmanPosY;
        synchronized (monitor){
            pacmanPosX = pacman.getPacmanCordX();
            pacmanPosY = pacman.getPacmanCordY();
        }

        if (board[pacmanPosY/boardDimensions][pacmanPosX/boardDimensions] == F){
            board[pacmanPosY/boardDimensions][pacmanPosX/boardDimensions] = E;
            updateScore(10);
            pacman.updateAmountOfFoodEaten();
        }
    }

    private void updateScore(int addValue){
        score += addValue;
    }

    @Override
    public void keyPressed(KeyEvent e) {

        int key = e.getKeyCode();

        if (inGame) {
            if (key == KeyEvent.VK_W || key == KeyEvent.VK_UP) {
                pacman.setMoveUp();
            }
            if (key == KeyEvent.VK_D || key == KeyEvent.VK_RIGHT) {
                pacman.setMoveRight();
            }
            if (key == KeyEvent.VK_S || key == KeyEvent.VK_DOWN) {
                pacman.setMoveDown();
            }
            if (key == KeyEvent.VK_A || key == KeyEvent.VK_LEFT) {
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
