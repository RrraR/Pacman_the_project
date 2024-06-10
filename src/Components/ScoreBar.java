package Components;

import javax.swing.*;

import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ScoreBar extends JPanel implements Runnable {

    private Font pacmanFont;
    private boolean inGame;
    private GameBoard gameBoard;
    private JLabel scoreTextLabel;
    private JLabel scoreNumLabel;
    private List<JLabel> livesLabels;
    private ImageIcon heartIcon = new ImageIcon(getClass().getClassLoader().getResource("resources\\heart13.png"));
//    private String score;

    public ScoreBar(GameBoard gameBoard){
        setPreferredSize(new Dimension(437, 50));
        this.setLayout(null);
        setBackground(Color.black);

        try {
            Font font = Font.createFont(Font.TRUETYPE_FONT, getClass().getClassLoader().getResource("resources\\fonts\\pacman.ttf").openStream());
            pacmanFont = font.deriveFont(15f);
        } catch (FontFormatException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        inGame = true;
        this.gameBoard = gameBoard;

        scoreTextLabel = new JLabel("HIGH SCORE");
        scoreNumLabel = new JLabel(gameBoard.getScore());

        livesLabels = new ArrayList<>();

        initLabels();

        for (int i = 0; i < livesLabels.size(); i++){
            JLabel label = livesLabels.get(i);
            label.setBounds(13 * i + 2, 12, 13, 13);
            this.add(label);
        }

        this.add(scoreTextLabel);
        this.add(scoreNumLabel);

    }

    private void initLabels(){

        scoreTextLabel.setFont(pacmanFont);
        scoreTextLabel.setBackground(Color.black);
        scoreTextLabel.setForeground(Color.white);
        scoreTextLabel.setBounds(140, 12, 120, 25);

        scoreNumLabel.setFont(pacmanFont);
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
        System.out.println("livesLabels.size() " + livesLabels.size());
        int lives = gameBoard.getPacmanLives();
        if (livesLabels.size() > lives){
            this.remove(livesLabels.getLast());
            livesLabels.removeLast();
        } else if (livesLabels.size() < lives) {
            JLabel label = new JLabel(heartIcon);
            label.setBounds(13 * livesLabels.size() + 2, 12, 13, 13);
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
                inGame = false;
            }
        }
    }
}
