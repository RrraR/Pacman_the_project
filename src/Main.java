import javax.swing.*;
import java.awt.*;

public class Main {

    public JFrame frame;
    private JPanel statusBar;

    public Main(){
        frame = new JFrame("pacman");
//        frame.setSize(432, 529);

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());
        frame.setResizable(false);

        createStatusBar();
        frame.getContentPane().add(statusBar, BorderLayout.NORTH);

        GameBoard gameBoard = new GameBoard();
        frame.addKeyListener(gameBoard);
        frame.getContentPane().add(gameBoard);

        frame.pack();
        frame.setVisible(true);
        Thread thread = new Thread(gameBoard);
        thread.start();
    }

    public static void main(String[] args){
        new Main();
    }

    private void createStatusBar(){
        statusBar = new JPanel();
        statusBar.setPreferredSize(new Dimension(100, 50));
        statusBar.setBackground(Color.black);
    }

}

