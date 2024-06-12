package Components;

import javax.swing.*;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

import static Components.GameBoard.inGame;

public class ScoreBar extends JPanel implements Runnable {

    private final GameBoard gameBoard;
    private final JLabel scoreTextLabel;
    private final JLabel scoreNumLabel;
    private final List<JLabel> livesLabels;
    private final ImageIcon heartIcon = new ImageIcon(getClass().getClassLoader().getResource("resources\\other\\heart13.png"));

    public ScoreBar(GameBoard gameBoard){
        setPreferredSize(new Dimension(437, 50));
        this.setLayout(null);
        setBackground(Color.black);

        this.gameBoard = gameBoard;

        scoreTextLabel = new JLabel("HIGH SCORE");
        scoreNumLabel = new JLabel(gameBoard.getScore());

        livesLabels = new ArrayList<>();

        initLabels();

        for (int i = 0; i < livesLabels.size(); i++){
            JLabel label = livesLabels.get(i);
            label.setBounds(15 * i + 10, 18, 13, 13);
            this.add(label);
        }

        this.add(scoreTextLabel);
        this.add(scoreNumLabel);

    }

    private void initLabels(){

        scoreTextLabel.setBackground(Color.black);
        scoreTextLabel.setForeground(Color.white);
        scoreTextLabel.setBounds(140, 12, 120, 25);

        scoreNumLabel.setBackground(Color.black);
        scoreNumLabel.setForeground(Color.white);
        scoreNumLabel.setBounds(250, 12, 100, 25);

        for (int i = 0; i < gameBoard.getPacmanLives(); i++){
            livesLabels.add(new JLabel(heartIcon));
        }

    }

    private void updateScore(){
        String score = gameBoard.getScore();
        scoreNumLabel.setText(score);
    }

    private void updateLivesCount(){
        int lives = gameBoard.getPacmanLives();
        if (livesLabels.size() > lives){
            this.remove(livesLabels.getLast());
            livesLabels.removeLast();
        } else if (livesLabels.size() < lives) {
            JLabel label = new JLabel(heartIcon);
            label.setBounds(15 * livesLabels.size() + 10, 18, 13, 13);
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
            try {
                Thread.sleep(30);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
//                inGame = false;
            }
        }
    }
}
