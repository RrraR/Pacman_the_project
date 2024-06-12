package Components;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class HighScoresManager {
    private static final String highScoreFile = "src\\resources\\highScores.txt";

    public static void saveHighScore(HighScore highScore) {
        List<HighScore> highScores = loadHighScores();

        highScores.add(highScore);

        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(highScoreFile))) {
            oos.writeObject(highScores);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static List<HighScore> loadHighScores() {
        List<HighScore> highScores = new ArrayList<>();

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(highScoreFile))) {
            highScores = (List<HighScore>) ois.readObject();
        } catch (FileNotFoundException e) {
//            e.printStackTrace();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }

        return highScores;
    }


}
