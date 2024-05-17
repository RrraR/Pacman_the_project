import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class MainWindow{

    public JFrame frame;
    private JPanel statusBar;

    MainWindow() throws IOException {
        frame = new JFrame("Main Window Test");
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
        statusBar.setPreferredSize(new Dimension(420, 50));
        statusBar.setBackground(Color.black);
    }

}
