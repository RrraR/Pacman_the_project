import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

public class Pacman extends JPanel {

    private Timer timer;
    private int frameCount = 0;

    public Pacman() {
        startAnimation();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        int x = (frameCount % getWidth()) - 20;
        int y = (frameCount % getHeight()) - 20;
        g.setColor(Color.RED);
        g.fillOval(x, y, 20, 20);
    }

    private void startAnimation() {
        timer = new Timer(50, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frameCount++;
                System.out.println(frameCount);
                repaint();
            }
        });
        timer.start();
    }
}
