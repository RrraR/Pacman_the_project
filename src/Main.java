import Components.GameBoard;
import Components.ScoreBar;

import javax.swing.*;
import java.awt.*;

public class Main {

    public JFrame frame;
    private GameBoard gameBoard;
    private ScoreBar scoreBar;

    public Main(){
        frame = new JFrame("pacman");
//        frame.setSize(432, 529);

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());
        frame.setResizable(false);

        gameBoard = new GameBoard();
        scoreBar = new ScoreBar(gameBoard);

//        createStatusBar();
        frame.getContentPane().add(scoreBar, BorderLayout.NORTH);

        frame.addKeyListener(gameBoard);
        frame.getContentPane().add(gameBoard);

        frame.pack();
        frame.setVisible(true);
        Thread gameBoardThread = new Thread(gameBoard);
        Thread scoreBarThread = new Thread(scoreBar);

        gameBoardThread.start();
        scoreBarThread.start();
    }

    public static void main(String[] args){
        new Main();
    }

//    private void createStatusBar(){
//        statusBar = new JPanel();
//        statusBar.setPreferredSize(new Dimension(100, 50));
//        statusBar.setBackground(Color.black);
////        JLabel scoreLabel = new JLabel("Score: " + gameBoard.getScore());
////        scoreLabel.setBackground(Color.white);
////        statusBar.add(scoreLabel);
//
//        Timer timer = new Timer(20, new ActionListener() {
//            @Override
//            public void actionPerformed(ActionEvent e) {
//                JLabel scoreLabel = new JLabel("Score: " + gameBoard.getScore());
//                scoreLabel.setBackground(Color.white);
//                statusBar.add(scoreLabel);
//            }
//        });
//
//
//        timer.start();
//    }

}

