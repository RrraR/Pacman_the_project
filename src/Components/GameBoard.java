package Components;

import Characters.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.List;

public class GameBoard extends JPanel implements KeyListener, Runnable {
    public final static int W=1; // Wall.
    public final static int F=2; // Crossroads with food
    public final static int E=3; // Empty space
    public final static int D=4; // Door space
    public final static int U=5; // Ghost Upgrade space
    public final static int P=6; // Power Pellet

    public static int[][] board = {
        //-----------------------X---H-------------------------//
        //board.length - rows
        //board[0].length - cols
        //r24
        //c23                  r
        {W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W},
        {W,F,F,F,F,F,F,F,F,F,F,W,F,F,F,F,F,F,F,F,F,F,W},
        {W,F,W,W,W,F,W,W,W,W,F,W,F,W,W,W,W,F,W,W,W,F,W},
        {W,P,W,W,W,F,W,W,W,W,F,W,F,W,W,W,W,F,W,W,W,P,W},
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
        {W,P,F,F,W,F,F,F,F,F,F,F,F,F,F,F,F,F,W,F,F,P,W},
        {W,W,W,F,W,F,W,F,W,W,W,W,W,W,W,F,W,F,W,F,W,W,W},
        {W,F,F,F,F,F,W,F,F,F,F,W,F,F,F,F,W,F,F,F,F,F,W},
        {W,F,W,W,W,W,W,W,W,W,F,W,F,W,W,W,W,W,W,W,W,F,W},
        {W,F,F,F,F,F,F,F,F,F,F,F,F,F,F,F,F,F,F,F,F,F,W},
        {W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W}
    };

    public static int boardDimensions = 19;
    public static int cageTopLeftX = 152;
    public static int cageTopLeftY = 171;
    public static int cageBottomRightX = 266;
    public static int cageBottomRightY = 247;

    public static List<int[]> foodCells;

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
    private final Object monitor = new Object();
    public int score;
    private ImageIcon foodImage;
    private ImageIcon powerFoodImage;

    private JLabel[][] cells;
    private int mapHeight;
    private int mapWidth;
    private JLayeredPane multiBoard;
    private JPanel background;
    private List<Upgrade> upgrades;
    private int consecutiveGhostsEaten = 0;


    public GameBoard() {
        mapHeight = board.length * boardDimensions;
        mapWidth = board[0].length * boardDimensions;
        this.setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
        this.setBackground(Color.black);
        foodCells = new ArrayList<>();
        getAllFoodCells();
        inGame = true;
        foodImage = new ImageIcon("D:\\Documents\\uni2\\sem 2\\GUI\\Project\\resources\\food13\\food2.png");
        powerFoodImage = new ImageIcon("D:\\Documents\\uni2\\sem 2\\GUI\\Project\\resources\\food13\\Pfood.png");
        upgrades = new ArrayList<>();

//        this.setPreferredSize(new Dimension(mapHeight, mapWidth));

        cells = new JLabel[board.length][board[0].length];

        multiBoard = new JLayeredPane();
        multiBoard.setPreferredSize(new Dimension(mapWidth, mapHeight));

        JPanel background = createBackground();
        background.setBounds(0, 0, mapWidth, mapHeight);

        multiBoard.add(background, JLayeredPane.DEFAULT_LAYER);
        multiBoard.setVisible(true);

        pacman = new Pacman(inGame, monitor);
        JLabel pacmanLabel = pacman.getPacmanLabel();
        multiBoard.add(pacmanLabel, JLayeredPane.POPUP_LAYER);
        pacmanThread = new Thread(pacman);

        redGhost = new RedGhost(board, pacman, inGame, monitor);
        JLabel redGhostLabel = redGhost.getRedGhostLabel();
        multiBoard.add(redGhostLabel, JLayeredPane.POPUP_LAYER);
        redGhostThread = new Thread(redGhost);

        pinkGhost = new PinkGhost(board, pacman, inGame, monitor);
        JLabel pinkGhostLabel = pinkGhost.getPinkGhostLabel();
        multiBoard.add(pinkGhostLabel, JLayeredPane.POPUP_LAYER);
        pinkGhostThread = new Thread(pinkGhost);

        blueGhost = new BlueGhost(board, pacman, inGame, monitor);
        JLabel blueGhostLabel = blueGhost.getBlueGhostLabel();
        multiBoard.add(blueGhostLabel, JLayeredPane.POPUP_LAYER);
        blueGhostThread = new Thread(blueGhost);

        orangeGhost = new OrangeGhost(board, pacman, inGame, monitor);
        JLabel orangeGhostLabel = orangeGhost.getOrangeGhostLabel();
        multiBoard.add(orangeGhostLabel, JLayeredPane.POPUP_LAYER);
        orangeGhostThread = new Thread(orangeGhost);

        this.add(multiBoard);
    }

    private JPanel createBackground() {
        background = new JPanel();
        background.setLayout(null);

        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[0].length; j++) {

                if (board[i][j] == F){
                    cells[i][j] = new JLabel(foodImage);
                } else if (board[i][j] == P) {
                    cells[i][j] = new JLabel(powerFoodImage);
                }else {
                    cells[i][j] = new JLabel();
                }
                cells[i][j].setOpaque(true);
                cells[i][j].setBackground(getCellColor(board[i][j]));
                cells[i][j].setVisible(true);
                cells[i][j].setBounds(j * boardDimensions, i * boardDimensions, boardDimensions, boardDimensions);
                background.add(cells[i][j]);
            }
        }

        background.setVisible(true);
        return background;
    }

    private void getAllFoodCells(){
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

    private Color getCellColor(int cellType) {
        switch (cellType) {
            case W: return Color.BLUE;
            case D: return Color.MAGENTA;
            default: return Color.BLACK;
        }
    }

    private void checkFoodAndUpgradesCells(){
        int pacmanPosX, pacmanPosY;
        //todo possibly move get upgrades to a separate method
        List<Upgrade> redGhostUpgrades, pinkGhostUpgrades, blueGhostUpgrades, orangeGhostsUpgrades;
        synchronized (monitor){
            pacmanPosX = pacman.getPacmanCordX();
            pacmanPosY = pacman.getPacmanCordY();
            redGhostUpgrades = redGhost.getUpgrades();
            pinkGhostUpgrades = pinkGhost.getUpgrades();
            blueGhostUpgrades = blueGhost.getUpgrades();
            orangeGhostsUpgrades = orangeGhost.getUpgrades();
        }

        if (!redGhostUpgrades.isEmpty()){
            for (Upgrade upgrade : redGhostUpgrades) {
                upgrades.add(upgrade);
                board[upgrade.getY()][upgrade.getX()] = U;
                cells[upgrade.getY()][upgrade.getX()].setIcon(upgrade.getIcon());
            }
            redGhost.removeProcessedUpgrades();
        }

        if (!pinkGhostUpgrades.isEmpty()){
            for (Upgrade upgrade : pinkGhostUpgrades) {
                upgrades.add(upgrade);
                board[upgrade.getY()][upgrade.getX()] = U;
                cells[upgrade.getY()][upgrade.getX()].setIcon(upgrade.getIcon());
            }
            pinkGhost.removeProcessedUpgrades();
        }

        if (!blueGhostUpgrades.isEmpty()){
            for (Upgrade upgrade : blueGhostUpgrades) {
                upgrades.add(upgrade);
                board[upgrade.getY()][upgrade.getX()] = U;
                cells[upgrade.getY()][upgrade.getX()].setIcon(upgrade.getIcon());
            }
            blueGhost.removeProcessedUpgrades();
        }

        if (!orangeGhostsUpgrades.isEmpty()){
            for (Upgrade upgrade : orangeGhostsUpgrades) {
                upgrades.add(upgrade);
                board[upgrade.getY()][upgrade.getX()] = U;
                cells[upgrade.getY()][upgrade.getX()].setIcon(upgrade.getIcon());
            }
            orangeGhost.removeProcessedUpgrades();
        }

        if (board[pacmanPosY / boardDimensions][pacmanPosX / boardDimensions] == F) {
            board[pacmanPosY / boardDimensions][pacmanPosX / boardDimensions] = E;
            cells[pacmanPosY / boardDimensions][pacmanPosX / boardDimensions].setIcon(null);
            updateScore(10 * pacman.scoreMultiplier);
            pacman.updateAmountOfFoodEaten();
        } else if (board[pacmanPosY / boardDimensions][pacmanPosX / boardDimensions] == U) {
            Upgrade upgrade = getUpgrade( pacmanPosX / boardDimensions, pacmanPosY / boardDimensions);
            if (upgrade != null){
                board[pacmanPosY / boardDimensions][pacmanPosX / boardDimensions] = E;
                cells[pacmanPosY / boardDimensions][pacmanPosX / boardDimensions].setIcon(null);
                updateScore(20 * pacman.scoreMultiplier);
                pacman.applyUpgrade(upgrade);
                upgrades.remove(upgrade);
            }
        } else if (board[pacmanPosY / boardDimensions][pacmanPosX / boardDimensions] == P) {
            board[pacmanPosY / boardDimensions][pacmanPosX / boardDimensions] = E;
            cells[pacmanPosY / boardDimensions][pacmanPosX / boardDimensions].setIcon(null);
            startGhostsFrightenedState();
        }
    }

    private Upgrade getUpgrade(int x, int y){
        for (Upgrade upgrade : upgrades) {
            if (upgrade.getX() == x && upgrade.getY() == y){
                return upgrade;
            }
        }
        return null;
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

        new Thread(this::characterCollisionLoop).start();

        while (inGame){
            // todo possibly move or fix this
            if (consecutiveGhostsEaten > 0 &&
                    (redGhost.getGhostState() != GhostState.FRIGHTENED ||
                            pinkGhost.getGhostState() != GhostState.FRIGHTENED ||
                            blueGhost.getGhostState() != GhostState.FRIGHTENED ||
                            orangeGhost.getGhostState() != GhostState.FRIGHTENED)){
                consecutiveGhostsEaten = 0;
            }


            checkFoodAndUpgradesCells();
//            System.out.println("pacmanThread.getState() " + pacmanThread.getState());
//            System.out.println("redGhostThread.getState() " + redGhostThread.getState());
//            System.out.println("pinkGhostThread.getState() " + pinkGhostThread.getState());
//            System.out.println("blueGhostThread.getState() " + blueGhostThread.getState());
//            System.out.println("orangeGhostThread.getState() " + orangeGhostThread.getState());
//            System.out.println();

            try {
                Thread.sleep(30);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                inGame = false;
            }
        }
    }

    //todo Implement the application using good programming practices with complete event handling implemented by the delegated event handling model !!

    private void characterCollisionLoop() {
        while (inGame){
            synchronized (monitor){
                int pacmanX = pacman.getPacmanCordX();
                int pacmanY = pacman.getPacmanCordY();
                checkCollision(pacmanX, pacmanY, redGhost);
                checkCollision(pacmanX, pacmanY, pinkGhost);
                checkCollision(pacmanX, pacmanY, blueGhost);
                checkCollision(pacmanX, pacmanY, orangeGhost);
            }
        }
    }

    private void checkCollision(int pacmanX, int pacmanY, Ghost ghost){
        int ghostX, ghostY;
        GhostState ghostState;
        synchronized (monitor){
            ghostX = ghost.getGhostCordX();
            ghostY = ghost.getGhostCordY();
            ghostState = ghost.getGhostState();
        }

        if (isCollision(pacmanX, pacmanY, ghostX, ghostY) && ghostState != GhostState.SPAWN) {
            if (ghostState == GhostState.FRIGHTENED || pacman.isGhostEater) {
                // Pacman eats the ghost
                ghost.ghostHasBeenEaten();
                consecutiveGhostsEaten++;
                updateScore((int) (200 * Math.pow(2, consecutiveGhostsEaten)));

            } else if (!pacman.isInvincible) {
                // Pacman loses a life
                stopCharacterMovement();

                Thread pacmanDeathAnimationThread = new Thread(pacman::deathAnimationLoop);
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
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        }
    }

    private void startGhostsFrightenedState(){
        synchronized (monitor){
            redGhost.startFrightenedState();
            pinkGhost.startFrightenedState();
            blueGhost.startFrightenedState();
            orangeGhost.startFrightenedState();
        }
    }

    private void stopCharacterMovement(){
        synchronized (monitor) {
            pacman.pause();
            redGhost.pause();
            pinkGhost.pause();
            blueGhost.pause();
            orangeGhost.pause();
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
