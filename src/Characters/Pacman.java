package Characters;

import Components.Upgrade;

import javax.swing.*;
import java.util.*;
import java.awt.*;
import java.util.List;

import static Components.GameBoard.*;

public class Pacman implements Runnable {

    private final int startPositionX = 209;
    private final int startPositionY = 269;

    private final Object monitor;
    private int panelX = 209;
    private int panelY = 266;
    private int currentSpeedX = 3; // Change in x-coordinate per frame
    private int currentSpeedY = 0; // Change in y-coordinate per frame
    private final int initSpeedX = 3;
    private final int initSpeedY = 3;
    private int currentPacmanImageIndex;
    private int currentPacmanOrientation;
    private ImageIcon[] pacmanImagesRight;
    private ImageIcon[] pacmanImagesLeft;
    private ImageIcon[] pacmanImagesUp;
    private ImageIcon[] pacmanImagesDown;
    private ImageIcon[] pacmanDeathImages;
    private boolean inGame;
    public int lives = 3;
    public int amountOfFoodConsumed;
    private int currentPacmanDeathImageIndex;
    private JLabel pacmanLabel;
    private final Thread pacmanAnimationThread;
    private volatile boolean paused = false;
//    private List<Upgrade> upgrades;
    public boolean isInvincible;
    public boolean isGhostEater;
    public int scoreMultiplier;
//    private final TimeTracker timeTracker;
//    private Upgrade currentUpgrade;
    private List<Upgrade> activeUpgrades;
    private volatile boolean isCheckActiveUpdatesThreadStarted = false;

//    private final Thread checkActiveUpdatesThread;

    public Pacman(boolean inGame, Object monitor){
        loadImages();
        currentPacmanImageIndex = 0;
        currentPacmanOrientation = 1;
        this.inGame = inGame;
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
//        timeTracker = new TimeTracker();
        activeUpgrades = Collections.synchronizedList(new ArrayList<>());
//        currentUpgrade = null;
    }

    private void updatePacmanIconLoop() {
        while (inGame){
            if (!paused){
                switch (currentPacmanOrientation) {
                    case 0:
                        pacmanLabel.setIcon(pacmanImagesUp[currentPacmanImageIndex]);
                        break;
                    case 1:
                        pacmanLabel.setIcon(pacmanImagesRight[currentPacmanImageIndex]);
                        break;
                    case 2:
                        pacmanLabel.setIcon(pacmanImagesDown[currentPacmanImageIndex]);
                        break;
                    case 3:
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
                //todo fix this looks weird
                if (panelX - 13 > 0 && panelX < board.length * boardDimensions && panelX / boardDimensions < 22 && checkCollision()){
//                    return;
                }else {
                    panelX += currentSpeedX;
                    panelY += currentSpeedY;

                    //wall passing
                    if (panelX <= 0){
                        panelX = board.length * boardDimensions - 20;
                    } else if (panelX >= board.length * boardDimensions - boardDimensions) {
                        panelX = 0;
                    }

                    recenterPacman();
                    updatePacmanLabelPosition();
//                    System.out.println("checkActiveUpdatesThread " + checkActiveUpdatesThread.getState());
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

    private void removeUpgrade(Upgrade upgrade){
        synchronized (monitor){
            upgrade.stopUpgradeTimer();
            switch (upgrade.getType()) {
                case SPEED_BOOST:
                    switch (currentPacmanOrientation) {
                        case 0 -> currentSpeedY += 1;
                        case 1 -> currentSpeedX -= 1;
                        case 2 -> currentSpeedY -= 1;
                        case 3 -> currentSpeedX += 1;
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
                        case 0 -> currentSpeedY -= 1;
                        case 1 -> currentSpeedX += 1;
                        case 2 -> currentSpeedY += 1;
                        case 3 -> currentSpeedX -= 1;
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
        currentPacmanDeathImageIndex = currentPacmanOrientation;
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

    public int getPacmanOrientation(){
        synchronized (monitor) {
            return currentPacmanOrientation;
        }
    }

    public void updateAmountOfFoodEaten(){
        amountOfFoodConsumed++;
    }

    public void setMoveRight(){
        if (board[panelY/boardDimensions][(panelX)/boardDimensions + 1] != W) {
            currentSpeedY = 0;
            currentSpeedX = initSpeedX;
            currentPacmanOrientation = 1;
        }
    }

    public void setMoveLeft(){
        if (board[panelY / boardDimensions][(panelX + 13)/ boardDimensions - 1] != W){
            currentSpeedX = -initSpeedX;
            currentSpeedY = 0;
            currentPacmanOrientation = 3;
        }
    }

    public void setMoveUp(){
        if (board[panelY/boardDimensions - 1][panelX/boardDimensions] != W){
            currentSpeedY = -initSpeedY;
            currentSpeedX = 0;
            currentPacmanOrientation = 0;
        }
    }

    public void setMoveDown(){
        if (board[panelY/boardDimensions + 1][panelX/boardDimensions] != W){
            currentSpeedX = 0;
            currentSpeedY = initSpeedY;
            currentPacmanOrientation = 2;
        }
    }

    public void resetPosition() {
        synchronized (monitor) {
            panelX = startPositionX;
            panelY = startPositionY;
            currentSpeedX = 3;
            currentSpeedY = 0;
            currentPacmanOrientation = 1;
            currentPacmanImageIndex = 0;
            amountOfFoodConsumed = 0;
            activeUpgrades.clear();
            updatePacmanLabelPosition();
            resume();
        }
    }

    private void recenterPacman() {
        // recenter horizontally
        if (currentPacmanOrientation == 0 || currentPacmanOrientation == 2) {
            int offsetX = (panelX % boardDimensions < boardDimensions / 2) ? -(panelX % boardDimensions) : (boardDimensions - panelX % boardDimensions);
            panelX += offsetX + 3;
        }

        // recenter vertically
        if (currentPacmanOrientation == 1 || currentPacmanOrientation == 3) {
            int offsetY = (panelY % boardDimensions < boardDimensions / 2) ? -(panelY % boardDimensions) : (boardDimensions - panelY % boardDimensions);
            panelY += offsetY + 3;
        }
    }

    private boolean checkCollision(){
//        System.out.println(" panel:" + board[panelY/18][panelX/18] + ", row: " + panelY/18 + " cord y: " + panelY+ ", col: " + panelX/18 +  " cord x: " + panelX );

        // TODO: refactor orientations for all characters to be enum like Direction.LEFT
        if (currentPacmanOrientation == 0 && board[(panelY + boardDimensions - 3)/ boardDimensions - 1][panelX/ boardDimensions] == 1){
            currentSpeedY = 0;
            currentSpeedX = 0;
            return true;
        }
        if (currentPacmanOrientation == 1 && board[panelY/ boardDimensions][(panelX)/ boardDimensions + 1] == 1) {
            currentSpeedY = 0;
            currentSpeedX = 0;
            return true;
        }
        if (currentPacmanOrientation == 2 && board[(panelY - 1)/ boardDimensions + 1][panelX/ boardDimensions] == 1) {
            currentSpeedY = 0;
            currentSpeedX = 0;
            return true;
        }
        if (currentPacmanOrientation == 3 && board[panelY / boardDimensions][(panelX + boardDimensions - 3)/ boardDimensions - 1] == 1){
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
            pacmanImagesRight[i] = new ImageIcon("D:\\Documents\\uni2\\sem 2\\GUI\\Project\\resources\\pacman13\\mspacman-right_" + i + ".png");
            pacmanImagesLeft[i] = new ImageIcon("D:\\Documents\\uni2\\sem 2\\GUI\\Project\\resources\\pacman13\\mspacman-left_" + i + ".png");
            pacmanImagesUp[i] = new ImageIcon("D:\\Documents\\uni2\\sem 2\\GUI\\Project\\resources\\pacman13\\mspacman-up_" + i + ".png");
            pacmanImagesDown[i] = new ImageIcon("D:\\Documents\\uni2\\sem 2\\GUI\\Project\\resources\\pacman13\\mspacman-down_" + i + ".png");
        }

        pacmanDeathImages[0] = pacmanImagesUp[1];
        pacmanDeathImages[1] = pacmanImagesRight[1];
        pacmanDeathImages[2] = pacmanImagesDown[1];
        pacmanDeathImages[3] = pacmanImagesLeft[1];
    }

}
