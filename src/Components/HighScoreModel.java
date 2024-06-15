package Components;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

public class HighScoreModel extends AbstractListModel<String> {
    private final List<HighScore> highScores;
    private final List<String> displayData;
    private final boolean isNameList;

    public HighScoreModel(List<HighScore> highScores, boolean isNameList) {
        this.highScores = highScores;
        this.displayData = new ArrayList<>();
        this.isNameList = isNameList;
        updateDisplayData();
    }

    @Override
    public int getSize() {
        return displayData.size();
    }

    @Override
    public String getElementAt(int index) {
        return displayData.get(index);
    }

    public void updateHighScores(List<HighScore> newHighScores) {
        highScores.clear();
        highScores.addAll(newHighScores);
        updateDisplayData();
        fireContentsChanged(this, 0, getSize() - 1);
    }

    private void updateDisplayData() {
        displayData.clear();
        for (HighScore score : highScores) {
            if (isNameList) {
                displayData.add(score.getPlayerName());
            } else {
                displayData.add(String.valueOf(score.getScore()));
            }
        }
    }
}