package Characters;

import Components.Upgrade;

import javax.swing.*;
import java.util.List;

public interface Ghost {
    void removeProcessedUpgrades();
    void startFrightenedState();
    void ghostHasBeenEaten();
    GhostState getGhostState();
    void pause();
    void resume();
    List<Upgrade> getUpgrades();
    JLabel getGhostLabel();
    int getGhostCordX();
    int getGhostCordY();
    void resetPosition();

}
