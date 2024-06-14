package Components;

import Characters.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.List;

import static Components.Boards.*;

public class GameBoard extends JPanel implements KeyListener, Runnable {

    public static int[][] board;

    public static int boardDimensions = 19;
    public static int cageTopLeftX;
    public static int cageTopLeftY;
    public static int cageBottomRightX;
    public static int cageBottomRightY;

    public static List<int[]> foodCells;

    public static Boolean inGame = false;
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
    private ImageIcon youWonImage;
    private ImageIcon gameOverImage;

    private JLabel[][] cells;
    private int mapHeight;
    private int mapWidth;
    private JLayeredPane gameBoard;
    private JPanel background;
    private List<Upgrade> upgrades;
    private int consecutiveGhostsEaten = 0;
    private GameEventListener listener;


    public GameBoard(GameEventListener listener, String boardSize) {
        initBoard(boardSize);
        this.listener = listener;
        mapHeight = board.length * boardDimensions;
        mapWidth = board[0].length * boardDimensions;
        this.setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
        this.setBackground(Color.black);
        foodCells = new ArrayList<>();
        getAllFoodCells();
        inGame = true;
        foodImage = new ImageIcon(getClass().getClassLoader().getResource("resources\\food13\\food2.png"));
        powerFoodImage = new ImageIcon(getClass().getClassLoader().getResource("resources\\food13\\Pfood.png"));
        gameOverImage = new ImageIcon(getClass().getClassLoader().getResource("resources\\other\\gameover.png"));
        youWonImage = new ImageIcon(getClass().getClassLoader().getResource("resources\\other\\victory.png"));
        upgrades = new ArrayList<>();

        this.setPreferredSize(new Dimension(mapWidth, mapHeight));

        cells = new JLabel[board.length][board[0].length];

        gameBoard = new JLayeredPane();
        gameBoard.setPreferredSize(new Dimension(mapWidth, mapHeight));

        JPanel background = createBackground();
        background.setBounds(0, 0, mapWidth, mapHeight);

        gameBoard.add(background, JLayeredPane.DEFAULT_LAYER);
        gameBoard.setVisible(true);

        pacman = new Pacman(monitor, boardSize);
        JLabel pacmanLabel = pacman.getPacmanLabel();
        gameBoard.add(pacmanLabel, JLayeredPane.POPUP_LAYER);
        pacmanThread = new Thread(pacman);

        redGhost = new RedGhost(pacman, monitor, boardSize);
        JLabel redGhostLabel = redGhost.getRedGhostLabel();
        gameBoard.add(redGhostLabel, JLayeredPane.POPUP_LAYER);
        redGhostThread = new Thread(redGhost);

        pinkGhost = new PinkGhost(pacman, monitor, boardSize);
        JLabel pinkGhostLabel = pinkGhost.getPinkGhostLabel();
        gameBoard.add(pinkGhostLabel, JLayeredPane.POPUP_LAYER);
        pinkGhostThread = new Thread(pinkGhost);

        blueGhost = new BlueGhost(pacman, monitor, boardSize);
        JLabel blueGhostLabel = blueGhost.getBlueGhostLabel();
        gameBoard.add(blueGhostLabel, JLayeredPane.POPUP_LAYER);
        blueGhostThread = new Thread(blueGhost);

        orangeGhost = new OrangeGhost(pacman, monitor, boardSize);
        JLabel orangeGhostLabel = orangeGhost.getOrangeGhostLabel();
        gameBoard.add(orangeGhostLabel, JLayeredPane.POPUP_LAYER);
        orangeGhostThread = new Thread(orangeGhost);

        this.add(gameBoard);
    }

    public void initBoard(String input){
        //todo dunno this looks weird
        switch (input){
            case "23x24":
                board = getBoardCopy(Boards.board23x24);
                cageTopLeftX = 152;
                cageTopLeftY = 171;
                cageBottomRightX = 266;
                cageBottomRightY = 247;
                break;
            case "27x18":
                board = getBoardCopy(Boards.board27x18);
                cageTopLeftX = 190;
                cageTopLeftY = 152;
                cageBottomRightX = 304;
                cageBottomRightY = 228;
                break;
            case "21x21":
                board = getBoardCopy(Boards.board21x21);
                cageTopLeftX = 133;
                cageTopLeftY = 152;
                cageBottomRightX = 247;
                cageBottomRightY = 228;
                break;
            case "31x11":
                board = getBoardCopy(Boards.board31x11);
                cageTopLeftX = 228;
                cageTopLeftY = 76;
                cageBottomRightX = 342;
                cageBottomRightY = 114;
                break;
            case "15x21":
                board = getBoardCopy(Boards.board15x21);
                cageTopLeftX = 76;
                cageTopLeftY = 190;
                cageBottomRightX = 190;
                cageBottomRightY = 266;
                break;
        }
    }
    private static int[][] getBoardCopy(int[][] original) {
        int[][] copy = new int[original.length][];
        for (int i = 0; i < original.length; i++) {
            copy[i] = original[i].clone();
        }
        return copy;
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
        synchronized (monitor){
            pacmanPosX = pacman.getPacmanCordX();
            pacmanPosY = pacman.getPacmanCordY();
        }

        synchronized (monitor){
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
    }

    private void collectUpgradesFromGhosts(){
        while (inGame){
            synchronized (monitor){
                List<Upgrade> redGhostUpgrades, pinkGhostUpgrades, blueGhostUpgrades, orangeGhostsUpgrades;
                redGhostUpgrades = redGhost.getUpgrades();
                pinkGhostUpgrades = pinkGhost.getUpgrades();
                blueGhostUpgrades = blueGhost.getUpgrades();
                orangeGhostsUpgrades = orangeGhost.getUpgrades();


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
            }

            try {
                Thread.sleep(30);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

    private Upgrade getUpgrade(int x, int y){
        synchronized (monitor){
            for (Upgrade upgrade : upgrades) {
                if (upgrade.getX() == x && upgrade.getY() == y){
                    return upgrade;
                }
            }
            return null;
        }
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
        new Thread(this::collectUpgradesFromGhosts).start();

        while (inGame){
            // todo possibly move or fix this
            if (consecutiveGhostsEaten > 0 &&
                    (redGhost.getGhostState() != GhostState.FRIGHTENED ||
                            pinkGhost.getGhostState() != GhostState.FRIGHTENED ||
                            blueGhost.getGhostState() != GhostState.FRIGHTENED ||
                            orangeGhost.getGhostState() != GhostState.FRIGHTENED)){
                consecutiveGhostsEaten = 0;
            }

            if(getNumberOfFoodsLeft() <= 0){
                gameWon();
            }

            checkFoodAndUpgradesCells();

            try {
                Thread.sleep(30);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                inGame = false;
            }
        }

    }

    private void characterCollisionLoop() {
        while (inGame){
            synchronized (monitor){
                checkCollision(redGhost);
                checkCollision(pinkGhost);
                checkCollision(blueGhost);
                checkCollision(orangeGhost);
            }
        }
    }

    private void checkCollision(Ghost ghost){
        int pacmanX, pacmanY, ghostX, ghostY;
        GhostState ghostState;
        synchronized (monitor){
            pacmanX = pacman.getPacmanCordX();
            pacmanY = pacman.getPacmanCordY();
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

                pacman.removeLive();
                if (pacman.getLives() <= 0) {
                    gameOver();
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

    private void gameOver(){
        synchronized (monitor){
            inGame = false;
        }
        String nameInput = (String) JOptionPane.showInputDialog(this, "What is your name?", "Game Over", JOptionPane.QUESTION_MESSAGE, gameOverImage, null,null);
        if (nameInput != null){
            HighScoresManager.saveHighScore(new HighScore(nameInput, score));
        }
        listener.onEscapePressed();
    }

    private void gameWon() {
        synchronized (monitor) {
            inGame = false;
        }
        String nameInput = (String) JOptionPane.showInputDialog(this, "What is your name?", "You Won!", JOptionPane.QUESTION_MESSAGE, youWonImage, null, null);
        if (nameInput != null){
            HighScoresManager.saveHighScore(new HighScore(nameInput, score));
        }
        listener.onEscapePressed();
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
        synchronized (monitor){
            return String.valueOf(score);
        }
    }

    public int getPacmanLives(){
        synchronized (monitor){
            return pacman.lives;
        }
    }

    private void updateScore(int addValue){
        score += addValue;
    }

    @Override
    public void keyPressed(KeyEvent e) {

        int key = e.getKeyCode();

        if (key == KeyEvent.VK_ESCAPE){
            inGame = false;
            listener.onEscapePressed();
        }

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
