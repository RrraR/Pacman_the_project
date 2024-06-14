package Components;

import javax.swing.*;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

import static Components.GameBoard.boardDimensions;
import static Components.GameBoard.inGame;

public class ScoreBar extends JPanel implements Runnable {

    private final GameBoard gameBoard;
    private final JLabel scoreTextLabel;
    private final JLabel scoreNumLabel;
    private final List<JLabel> livesLabels;
    private final JLabel timeLabel;
    private final TimeTracker timeTracker;
    private final ImageIcon heartIcon = new ImageIcon(getClass().getClassLoader().getResource("resources\\other\\heart13.png"));

    public ScoreBar(GameBoard gameBoard){

        this.setLayout(null);
        setBackground(Color.black);

        this.gameBoard = gameBoard;
        timeTracker = new TimeTracker();

        setPreferredSize(new Dimension(GameBoard.board[0].length * boardDimensions, 50));

        scoreTextLabel = new JLabel("HIGH SCORE");
        scoreNumLabel = new JLabel(gameBoard.getScore());

        timeTracker.start();
        livesLabels = new ArrayList<>();

        timeLabel = new JLabel(formatSeconds(timeTracker.getSecondsPassed()));

        initLabels();

        for (int i = 0; i < livesLabels.size(); i++){
            JLabel label = livesLabels.get(i);
            label.setBounds(12 * i + 10, 18, 10, 10);
            this.add(label);
        }

        this.add(scoreTextLabel);
        this.add(scoreNumLabel);
        this.add(timeLabel);

    }

    private void initLabels(){

        scoreTextLabel.setBackground(Color.black);
        scoreTextLabel.setForeground(Color.white);
        int xBounds = GameBoard.board[0].length * boardDimensions / 3 - 5;
        scoreTextLabel.setBounds(xBounds , 12, 100, 25);

        scoreNumLabel.setBackground(Color.black);
        scoreNumLabel.setForeground(Color.white);
        scoreNumLabel.setBounds(xBounds + 100, 12, 90, 25);

        timeLabel.setBackground(Color.black);
        timeLabel.setForeground(Color.white);
        timeLabel.setBounds(GameBoard.board[0].length * boardDimensions - 50, 12, 50, 25);

        for (int i = 0; i < gameBoard.getPacmanLives(); i++){
            livesLabels.add(new JLabel(heartIcon));
        }

    }

    public static String formatSeconds(int totalSeconds) {
        int minutes = totalSeconds / 60;
        int seconds = totalSeconds % 60;

        return String.format("%02d:%02d", minutes, seconds);
    }


    private void updateScore(){
        String score = gameBoard.getScore();
        scoreNumLabel.setText(score);
    }

    private void updateTime(){
        timeLabel.setText(formatSeconds(timeTracker.getSecondsPassed()));
    }

    private void updateLivesCount(){
        int lives = gameBoard.getPacmanLives();
        if (livesLabels.size() > lives){
            this.remove(livesLabels.getLast());
            livesLabels.removeLast();
        } else if (livesLabels.size() < lives) {
            JLabel label = new JLabel(heartIcon);
            label.setBounds(12 * livesLabels.size() + 10, 18, 10, 10);
            livesLabels.add(label);
            this.add(label);
        }
        repaint();
    }

    @Override
    public void run() {
        while (inGame){
            updateScore();
            updateLivesCount();
            updateTime();

            try {
                Thread.sleep(30);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }
}
