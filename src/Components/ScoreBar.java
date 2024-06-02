package Components;

import javax.swing.*;
import java.awt.*;

public class ScoreBar extends JPanel implements Runnable {

    private boolean inGame;
    private GameBoard gameBoard;
    private JLabel scoreLabel;

    public ScoreBar(GameBoard gameBoard){
        setPreferredSize(new Dimension(100, 50));
        setBackground(Color.black);
        inGame = true;
        this.gameBoard = gameBoard;

    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawString("Score: " + gameBoard.getScore(), 20, 20);
    }

    @Override
    public void run() {
        while (inGame){
//            JLabel scoreLabel = new JLabel("Score: " + gameBoard.getScore());
//                scoreLabel.setBackground(Color.white);
//            String score = gameBoard.getScore();
//            scoreLabel.setText(score);
//
//            this.add(scoreLabel);
//            System.out.println(score);
            repaint();
            try {
                Thread.sleep(30);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                inGame = false;
            }
        }
    }
}
