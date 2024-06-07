package Components;

import javax.swing.*;

public class Upgrade {
    private int x;
    private int y;
    private UpgradeType type;
    private ImageIcon icon;
    private ImageIcon speedBoostIcon;
    private ImageIcon extraLifeIcon;
    private ImageIcon invincibilityIcon;
    private ImageIcon scoreMultiplierIcon;
    private ImageIcon ghostEaterIcon;
    private final TimeTracker timeTracker;

    public Upgrade(int x, int y, UpgradeType type) {
        loadImages();
        this.x = x;
        this.y = y;
        this.type = type;
        switch (type){
            case SPEED_BOOST -> this.icon = speedBoostIcon;
            case EXTRA_LIFE -> this.icon = extraLifeIcon;
            case INVINCIBILITY -> this.icon = invincibilityIcon;
            case SCORE_MULTIPLIER -> this.icon = scoreMultiplierIcon;
            case GHOST_EATER -> this.icon = ghostEaterIcon;
        }
        timeTracker = new TimeTracker();
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public UpgradeType getType() {
        return type;
    }

    public ImageIcon getIcon() {
        return icon;
    }

    private void loadImages(){
        speedBoostIcon = new ImageIcon("D:\\Documents\\uni2\\sem 2\\GUI\\Project\\resources\\food13\\speed_boost.png");
        extraLifeIcon = new ImageIcon("D:\\Documents\\uni2\\sem 2\\GUI\\Project\\resources\\food13\\extra_life.png");
        invincibilityIcon = new ImageIcon("D:\\Documents\\uni2\\sem 2\\GUI\\Project\\resources\\food13\\invincibility.png");
        scoreMultiplierIcon = new ImageIcon("D:\\Documents\\uni2\\sem 2\\GUI\\Project\\resources\\food13\\score_multiplier.png");
        ghostEaterIcon = new ImageIcon("D:\\Documents\\uni2\\sem 2\\GUI\\Project\\resources\\food13\\ghost_eater.png");
    }

    public void startUpgradeTimer(){
        timeTracker.start();
    }

    public void stopUpgradeTimer(){
        timeTracker.stopTracking();
    }

    public int getTimeOnUpgrade(){
        return timeTracker.getSecondsPassed();
    }
}
