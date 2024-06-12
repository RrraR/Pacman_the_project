package Components;

import java.io.Serializable;

public class HighScore implements Serializable {
    private static final long serialVersionUID = 1L;
    private String playerName;
    private int score;

    public HighScore(String playerName, int score) {
        this.playerName = playerName;
        this.score = score;
    }

    public String getPlayerName() {
        return playerName;
    }

    public int getScore() {
        return score;
    }
//
//    @Override
//    public String toString() {
//        return "HighScore{" +
//                "playerName='" + playerName + '\'' +
//                ", score=" + score +
//                '}';
//    }
}
