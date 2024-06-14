package Components;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowEvent;

public class GameFrame extends JFrame implements GameEventListener {

    public GameFrame(String selectedBoardSize){
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.setLayout(new BorderLayout());
        GameBoard gameBoard = new GameBoard(this, selectedBoardSize);
        ScoreBar scoreBar = new ScoreBar(gameBoard);
        this.getContentPane().add(scoreBar, BorderLayout.NORTH);
        this.getContentPane().add(gameBoard);
        this.addKeyListener(gameBoard);
        this.pack();
        Thread gameBoardThread = new Thread(gameBoard);
        Thread scoreBarThread = new Thread(scoreBar);
        gameBoardThread.start();
        scoreBarThread.start();
    }

    @Override
    public void onEscapePressed() {
        this.dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));
    }
}
