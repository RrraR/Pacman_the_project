import javax.swing.*;
import java.awt.*;

public class MainWindow{

    public JFrame frame;
    private JPanel statusBar;

    MainWindow(){
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

    }

    private void createStatusBar(){
        statusBar = new JPanel();
        statusBar.setPreferredSize(new Dimension(100, 50));
        statusBar.setBackground(Color.black);
    }

}
