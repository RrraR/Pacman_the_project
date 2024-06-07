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
    final static int U=5; // Upgrade crossroad

    public static int board[][] = {
        //-----------------------X---H-------------------------//
        //board.length - rows
        //board[0].length - cols
        //r24
        //c23                  r
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
    private final TimeTracker timeTracker;
    private final Thread pacmanThread;
    private final Thread redGhostThread;
    private final Thread pinkGhostThread;
    private final Thread blueGhostThread;
    private final Thread orangeGhostThread;
    public int score;
    private final List<int[]> foodCells;
    private ImageIcon foodImage;

    private JLabel[][] cells;
    private int mapHeight;
    private int mapWidth;
    private JLayeredPane multiBoard;
    private JPanel background;
    private List<Upgrade> upgrades;


    public GameBoard() {
        mapHeight = board.length * boardDimensions;
        mapWidth = board[0].length * boardDimensions;
        this.setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
        this.setBackground(Color.black);
        foodCells = new ArrayList<>();
        getAllFoodCells();
        inGame = true;
        foodImage = new ImageIcon("D:\\Documents\\uni2\\sem 2\\GUI\\Project\\resources\\food13\\food2.png");
        timeTracker = new TimeTracker();
        upgrades = new ArrayList<>();

//        this.setPreferredSize(new Dimension(mapHeight, mapWidth));

        cells = new JLabel[board.length][board[0].length];

        multiBoard = new JLayeredPane();
        multiBoard.setPreferredSize(new Dimension(mapWidth, mapHeight));

        JPanel background = createBackground();
        background.setBounds(0, 0, mapWidth, mapHeight);

        multiBoard.add(background, JLayeredPane.DEFAULT_LAYER);
        multiBoard.setVisible(true);

        pacman = new Pacman(boardDimensions, board, inGame, monitor);
        JLabel pacmanLabel = pacman.getPacmanLabel();
        multiBoard.add(pacmanLabel, JLayeredPane.POPUP_LAYER);
        pacmanThread = new Thread(pacman);

        redGhost = new RedGhost(boardDimensions, board, pacman, inGame, monitor);
        JLabel redGhostLabel = redGhost.getRedGhostLabel();
        multiBoard.add(redGhostLabel, JLayeredPane.POPUP_LAYER);
        redGhostThread = new Thread(redGhost);

        pinkGhost = new PinkGhost(boardDimensions, board, pacman, inGame, monitor);
        JLabel pinkGhostLabel = pinkGhost.getPinkGhostLabel();
        multiBoard.add(pinkGhostLabel, JLayeredPane.POPUP_LAYER);
        pinkGhostThread = new Thread(pinkGhost);

        blueGhost = new BlueGhost(boardDimensions, board, pacman, inGame, foodCells, monitor);
        JLabel blueGhostLabel = blueGhost.getBlueGhostLabel();
        multiBoard.add(blueGhostLabel, JLayeredPane.POPUP_LAYER);
        blueGhostThread = new Thread(blueGhost);

        orangeGhost = new OrangeGhost(boardDimensions, board, pacman, inGame, foodCells, monitor);
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
                cells[i][j] = board[i][j] == F ? new JLabel(foodImage) : new JLabel();
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
            case F: return Color.BLACK;
            case E: return Color.BLACK;
            case D: return Color.MAGENTA;
            default: return Color.BLACK;
        }
    }

    private void checkFoodAndUpgradesCells(){
        int pacmanPosX, pacmanPosY;
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
        timeTracker.start();

        new Thread(this::ghostCollisionDetectionLoop).start();

        while (inGame){
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

    private void ghostCollisionDetectionLoop() {
        while (inGame){
            synchronized (monitor){
                if (!pacman.isInvincible) {
                    //todo possibly update this so the overlap between a ghost and pacman during collision is not as crazy.... crazy? i was crazy once. they locked me in a room, a rubber room a rubber room with rats and the rats make me crazy. crazy? i was crazy once
                    if (isCollision(pacman.getPacmanCordX(), pacman.getPacmanCordY(), redGhost.getRedGhostCordX(), redGhost.getRedGhostCordY()) ||
                            isCollision(pacman.getPacmanCordX(), pacman.getPacmanCordY(), pinkGhost.getPinkGhostCordX(), pinkGhost.getPinkGhostCordY()) ||
                            isCollision(pacman.getPacmanCordX(), pacman.getPacmanCordY(), blueGhost.getBlueGhostCordX(), blueGhost.getBlueGhostCordY()) ||
                            isCollision(pacman.getPacmanCordX(), pacman.getPacmanCordY(), orangeGhost.getOrangeGhostCordX(), orangeGhost.getOrangeGhostCordY())) {

                        stopCharacterMovement();

                        Thread pacmanDeathAnimationThread = new Thread(new Runnable() {
                            @Override
                            public void run() {
                                pacman.deathAnimationLoop();
                            }
                        });
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
