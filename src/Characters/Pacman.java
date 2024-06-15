package Characters;

import Components.Upgrade;

import javax.swing.*;
import java.util.*;
import java.awt.*;
import java.util.List;

import static Components.GameBoard.*;
import static Components.Boards.*;

public class Pacman implements Runnable {

    private int startPositionX;
    private int startPositionY;

    private final Object monitor;
    private int panelX;
    private int panelY;
    private final int initSpeedX = 3;
    private final int initSpeedY = 3;
    private int currentSpeedX = initSpeedX;
    private int currentSpeedY = 0;
    private int currentPacmanImageIndex;
    private Directions currentPacmanOrientation;
    private ImageIcon[] pacmanImagesRight;
    private ImageIcon[] pacmanImagesLeft;
    private ImageIcon[] pacmanImagesUp;
    private ImageIcon[] pacmanImagesDown;
    private ImageIcon[] pacmanDeathImages;
    public int lives = 3;
    public int amountOfFoodConsumed;
    private int currentPacmanDeathImageIndex;
    private JLabel pacmanLabel;
    private final Thread pacmanAnimationThread;
    private volatile boolean paused = false;
    public boolean isInvincible;
    public boolean isGhostEater;
    public int scoreMultiplier;
    private List<Upgrade> activeUpgrades;
    private volatile boolean isCheckActiveUpdatesThreadStarted = false;

    public Pacman(Object monitor, String boardSize){
        loadImages();
        initInitialVals(boardSize);
        currentPacmanImageIndex = 0;
        currentPacmanOrientation = Directions.RIGHT;
        this.monitor = monitor;
        amountOfFoodConsumed = 0;
        pacmanLabel = new JLabel(pacmanImagesRight[0]);
        pacmanLabel.setOpaque(true);
        pacmanLabel.setBounds(panelX, panelY, 13, 13);
        pacmanLabel.setBackground(Color.black);
        pacmanAnimationThread = new Thread(this::updatePacmanIconLoop);
        isInvincible = false;
        isGhostEater = false;
        scoreMultiplier = 1;
        activeUpgrades = Collections.synchronizedList(new ArrayList<>());
    }

    private void initInitialVals(String boardSize){
        switch (boardSize){
            case "23x24":
                startPositionX = 209;
                startPositionY = 269;
                panelX = startPositionX;
                panelY = startPositionY;
                break;
            case "27x18":
                startPositionX = 247;
                startPositionY = 250;
                panelX = startPositionX;
                panelY = startPositionY;
                break;
            case "21x21":
                startPositionX = 190;
                startPositionY = 250;
                panelX = startPositionX;
                panelY = startPositionY;
                break;
            case "31x11":
                startPositionX = 285;
                startPositionY = 136;
                panelX = startPositionX;
                panelY = startPositionY;
                break;
            case "15x21":
                startPositionX = 136;
                startPositionY = 288;
                panelX = startPositionX;
                panelY = startPositionY;
                break;
        }
    }

    private void updatePacmanIconLoop() {
        while (inGame){
            if (!paused){
                switch (currentPacmanOrientation) {
                    case UP:
                        pacmanLabel.setIcon(pacmanImagesUp[currentPacmanImageIndex]);
                        break;
                    case RIGHT:
                        pacmanLabel.setIcon(pacmanImagesRight[currentPacmanImageIndex]);
                        break;
                    case DOWN:
                        pacmanLabel.setIcon(pacmanImagesDown[currentPacmanImageIndex]);
                        break;
                    case LEFT:
                        pacmanLabel.setIcon(pacmanImagesLeft[currentPacmanImageIndex]);
                        break;
                }
                updateImageIndex();
                try {
                    Thread.sleep(40);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        }

    }

    private void updatePacmanLabelPosition(){
        pacmanLabel.setBounds(panelX, panelY, 13, 13);
    }

    private void updateImageIndex() {
        currentPacmanImageIndex = (currentPacmanImageIndex + 1) % 3;
    }

    @Override
    public void run() {
        pacmanAnimationThread.start();
        while (inGame){
            checkPaused();
            synchronized (monitor){

                if (panelX - 13 < 0 || panelX/boardDimensions >= board[0].length - 1 || !checkCollision()){
                    panelX += currentSpeedX;
                    panelY += currentSpeedY;

                    if (panelX <= 0){
                        panelX = board[0].length * boardDimensions - 13;
                    } else if (panelX >= board[0].length * boardDimensions - 13) {
                        panelX = 0;
                    }

                    recenterPacman();
                    updatePacmanLabelPosition();
                    if (!activeUpgrades.isEmpty() && !isCheckActiveUpdatesThreadStarted){
                        synchronized (monitor){
                            if (!isCheckActiveUpdatesThreadStarted){
                                isCheckActiveUpdatesThreadStarted = true;
                                new Thread(this::checkActiveUpgradesCollectionLoop).start();
                            }
                        }
                    }

                }
            }

            try {
                Thread.sleep(30);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

    public void removeLive(){
        synchronized (monitor){
            lives--;
        }
    }

    public int getLives(){
        synchronized (monitor){
            return lives;
        }
    }

    private void removeUpgrade(Upgrade upgrade){
        synchronized (monitor){
            upgrade.stopUpgradeTimer();
            switch (upgrade.getType()) {
                case SPEED_BOOST:
                    switch (currentPacmanOrientation) {
                        case UP -> currentSpeedY = -initSpeedY;
                        case RIGHT -> currentSpeedX = initSpeedX;
                        case DOWN -> currentSpeedY = initSpeedY;
                        case LEFT -> currentSpeedX = -initSpeedX;
                    }
                    break;
                case EXTRA_LIFE:
                    break;
                case INVINCIBILITY:
                    isInvincible = false;
                    break;
                case SCORE_MULTIPLIER:
                    scoreMultiplier = 1;
                    break;
                case GHOST_EATER:
                    isGhostEater = false;
                    break;
            }
        }
    }

    private void checkActiveUpgradesCollectionLoop() {
        while (!activeUpgrades.isEmpty()) {
            List<Upgrade> upgradesToRemove = new ArrayList<>();

            synchronized (monitor) {
                Iterator<Upgrade> iterator = activeUpgrades.iterator();
                while (iterator.hasNext()) {
                    Upgrade upgrade = iterator.next();
                    if (upgrade.getTimeOnUpgrade() >= 5) {
                        upgradesToRemove.add(upgrade);
                    }
                }
            }

            for (Upgrade upgrade : upgradesToRemove) {
                removeUpgrade(upgrade);
                synchronized (monitor) {
                    activeUpgrades.remove(upgrade);
                }
            }

            try {
                Thread.sleep(30);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }

        synchronized (monitor) {
            isCheckActiveUpdatesThreadStarted = false;
        }
    }


    public void applyUpgrade(Upgrade upgrade) {
        synchronized (monitor) {
            activeUpgrades.add(upgrade);
            upgrade.startUpgradeTimer();
            switch (upgrade.getType()) {
                case SPEED_BOOST:
                    switch (currentPacmanOrientation) {
                        case UP -> currentSpeedY = -initSpeedY - 1;
                        case RIGHT -> currentSpeedX = initSpeedX + 1;
                        case DOWN -> currentSpeedY = initSpeedY + 1;
                        case LEFT -> currentSpeedX = -initSpeedX - 1;
                    }
                    break;
                case EXTRA_LIFE:
                    lives++;
                    break;
                case INVINCIBILITY:
                    isInvincible = true;
                    break;
                case SCORE_MULTIPLIER:
                    scoreMultiplier = 2;
                    break;
                case GHOST_EATER:
                    isGhostEater = true;
                    break;
            }
        }
    }

    public JLabel getPacmanLabel() {
        return pacmanLabel;
    }

    public synchronized void pause() {
        paused = true;
    }

    public synchronized void resume() {
        paused = false;
        notify();
    }

    private synchronized void checkPaused() {
        while (paused) {
            try {
                wait();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

    public void deathAnimationLoop() {
        currentPacmanDeathImageIndex = currentPacmanOrientation.ordinal();
        for (int i = 0; i < 4; i++) {
            try {
                pacmanLabel.setIcon(pacmanDeathImages[currentPacmanDeathImageIndex]);
                updateDeathImageIndex();
                Thread.sleep(200);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public void updateDeathImageIndex() {
        currentPacmanDeathImageIndex = (currentPacmanDeathImageIndex + 1) % 4;
    }

    public int getPacmanCordX(){
        synchronized (monitor) {
            return panelX;
        }
    }

    public int getPacmanCordY() {
        synchronized (monitor) {
            return panelY;
        }
    }

    public Directions getPacmanOrientation(){
        synchronized (monitor) {
            return currentPacmanOrientation;
        }
    }

    public void updateAmountOfFoodEaten(){
        amountOfFoodConsumed++;
    }

    public void setMoveRight(){
        if (board[panelY/boardDimensions][(panelX)/boardDimensions + 1] != W) {
            synchronized (monitor){
                currentSpeedY = 0;
                currentSpeedX = initSpeedX;
                currentPacmanOrientation = Directions.RIGHT;
            }
        }
    }

    public void setMoveLeft(){
        if (board[panelY / boardDimensions][(panelX + boardDimensions)/ boardDimensions - 1] != W){
            synchronized (monitor){
                currentSpeedX = -initSpeedX;
                currentSpeedY = 0;
                currentPacmanOrientation = Directions.LEFT;
            }
        }
    }

    public void setMoveUp(){
        if (board[panelY/boardDimensions - 1][panelX/boardDimensions] != W){
            synchronized (monitor){
                currentSpeedY = -initSpeedY;
                currentSpeedX = 0;
                currentPacmanOrientation = Directions.UP;
            }
        }
    }

    public void setMoveDown(){
        if (board[panelY/boardDimensions + 1][panelX/boardDimensions] != W){
            synchronized (monitor){
                currentSpeedX = 0;
                currentSpeedY = initSpeedY;
                currentPacmanOrientation = Directions.DOWN;
            }
        }
    }

    public void resetPosition() {
        synchronized (monitor) {
            panelX = startPositionX;
            panelY = startPositionY;
            currentSpeedX = initSpeedX;
            currentSpeedY = 0;
            currentPacmanOrientation = Directions.RIGHT;
            currentPacmanImageIndex = 0;
            amountOfFoodConsumed = 0;
            activeUpgrades.clear();
            updatePacmanLabelPosition();
            resume();
        }
    }

    private void recenterPacman() {
        if (currentPacmanOrientation == Directions.UP || currentPacmanOrientation == Directions.DOWN) {
            int offsetX = (panelX % boardDimensions < boardDimensions / 2) ? -(panelX % boardDimensions) : (boardDimensions - panelX % boardDimensions);
            panelX += offsetX + 3;
        }

        if (currentPacmanOrientation == Directions.RIGHT || currentPacmanOrientation == Directions.LEFT) {
            int offsetY = (panelY % boardDimensions < boardDimensions / 2) ? -(panelY % boardDimensions) : (boardDimensions - panelY % boardDimensions);
            panelY += offsetY + 3;
        }
    }

    private boolean checkCollision(){
        if (currentPacmanOrientation == Directions.UP && board[(panelY + boardDimensions - 4)/ boardDimensions - 1][panelX/ boardDimensions] == 1){
            currentSpeedY = 0;
            currentSpeedX = 0;
            return true;
        }
        if (currentPacmanOrientation == Directions.RIGHT && board[panelY/ boardDimensions][(panelX)/ boardDimensions + 1] == 1) {
            currentSpeedY = 0;
            currentSpeedX = 0;
            return true;
        }
        if (currentPacmanOrientation == Directions.DOWN && board[(panelY - 1)/ boardDimensions + 1][panelX/ boardDimensions] == 1) {
            currentSpeedY = 0;
            currentSpeedX = 0;
            return true;
        }
        if (currentPacmanOrientation == Directions.LEFT && board[panelY / boardDimensions][(panelX + boardDimensions - 4)/ boardDimensions - 1] == 1){
            currentSpeedY = 0;
            currentSpeedX = 0;
            return true;
        }

        return false;
    }

    private void loadImages(){
        pacmanImagesRight = new ImageIcon[3];
        pacmanImagesLeft = new ImageIcon[3];
        pacmanImagesUp = new ImageIcon[3];
        pacmanImagesDown = new ImageIcon[3];
        pacmanDeathImages = new ImageIcon[4];


        for (int i = 0; i < 3; i++) {
            pacmanImagesRight[i] = new ImageIcon(getClass().getClassLoader().getResource("resources\\pacman13\\mspacman-right_" + i + ".png"));
            pacmanImagesLeft[i] = new ImageIcon(getClass().getClassLoader().getResource("resources\\pacman13\\mspacman-left_" + i + ".png"));
            pacmanImagesUp[i] = new ImageIcon(getClass().getClassLoader().getResource("resources\\pacman13\\mspacman-up_" + i + ".png"));
            pacmanImagesDown[i] = new ImageIcon(getClass().getClassLoader().getResource("resources\\pacman13\\mspacman-down_" + i + ".png"));
        }

        pacmanDeathImages[0] = pacmanImagesUp[1];
        pacmanDeathImages[1] = pacmanImagesRight[1];
        pacmanDeathImages[2] = pacmanImagesDown[1];
        pacmanDeathImages[3] = pacmanImagesLeft[1];
    }

}
