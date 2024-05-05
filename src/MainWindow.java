import javax.swing.*;
import java.awt.*;

public class MainWindow{
    public JFrame frame;
    public GameFrame2 gameFrame1;
    private JPanel mapPanel;
    private JPanel statusBar;

    MainWindow(){
        //extra height 35
        //extra width 12
        frame = new JFrame("Main Window Test");
        frame.setSize(431, 529);
//        frame.setPreferredSize(new Dimension(572, 655));
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());
        frame.setResizable(false);

        statusBar = new JPanel();
        statusBar.setPreferredSize(new Dimension(572, 30));
        statusBar.setBackground(Color.black);
        statusBar.setVisible(true);
        frame.getContentPane().add(statusBar, BorderLayout.NORTH);

        gameFrame1 = new GameFrame2();
        gameFrame1.setLayout(null);
        frame.getContentPane().add(gameFrame1);
        frame.setVisible(true);


//        gameFrame1.setPreferredSize(new Dimension(600,120));
//        gameFrame1.setBackground(Color.black);

//        gameFrame2 = new GameFrame2();
//        gameFrame2.setPreferredSize(new Dimension(600,580));
//        gameFrame2.setBackground(Color.BLACK);
//        frame.getContentPane().add(gameFrame2, BorderLayout.SOUTH);

    }

    private JPanel createStatusBer(){
        JPanel statusBar = new JPanel();

        return statusBar;
    }

    private void drawBoardBackground(){

    }

}
