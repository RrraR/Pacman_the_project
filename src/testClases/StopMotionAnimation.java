package testClases;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class StopMotionAnimation extends JFrame {

    final static int W=1; // Wall.
    final static int F=2; // Crossroads with food
    final static int E=3; // Empty crossroads
    private static int board[][] = {
            //-----------------------X---H-------------------------//
            //C23
            //R24
            {W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W},
            {W,F,F,F,F,F,F,F,F,F,F,W,F,F,F,F,F,F,F,F,F,F,W},
            {W,F,W,W,W,F,W,W,W,W,F,W,F,W,W,W,W,F,W,W,W,F,W},
            {W,F,W,W,W,F,W,W,W,W,F,W,F,W,W,W,W,F,W,W,W,F,W},
            {W,F,F,F,F,F,F,F,F,F,F,F,F,F,F,F,F,F,F,F,F,F,W},
            {W,F,W,W,W,F,W,F,W,W,W,W,W,W,W,F,W,F,W,W,W,F,W},
            {W,F,F,F,F,F,W,F,F,F,F,W,F,F,F,F,W,F,F,F,F,F,W},
            {W,W,W,W,W,F,W,W,W,W,F,W,F,W,W,W,W,F,W,W,W,W,W},
            {E,E,E,E,W,F,W,F,F,F,F,F,F,F,F,F,W,F,W,E,E,E,E},
            {E,E,E,E,W,F,W,F,W,W,W,W,W,W,W,F,W,F,W,E,E,E,E},
            {W,W,W,W,W,F,W,F,W,E,E,E,E,E,W,F,W,F,W,W,W,W,W},
            {F,F,F,F,F,F,F,F,W,E,E,E,E,E,W,F,F,F,F,F,F,F,F},
            {W,W,W,W,W,F,W,F,W,E,E,E,E,E,W,F,W,F,W,W,W,W,W},
            {E,E,E,E,W,F,W,F,W,W,W,W,W,W,W,F,W,F,W,E,E,E,E},
            {E,E,E,E,W,F,W,F,F,F,F,F,F,F,F,F,W,F,W,E,E,E,E},
            {W,W,W,W,W,F,W,F,W,W,W,W,W,W,W,F,W,F,W,W,W,W,W},
            {W,F,F,F,F,F,F,F,F,F,F,W,F,F,F,F,F,F,F,F,F,F,W},
            {W,F,W,W,W,F,W,W,W,W,F,W,F,W,W,W,W,F,W,W,W,F,W},
            {W,F,F,F,W,F,F,F,F,F,F,F,F,F,F,F,F,F,W,F,F,F,W},
            {W,W,W,F,W,F,W,F,W,W,W,W,W,W,W,F,W,F,W,F,W,W,W},
            {W,F,F,F,F,F,W,F,F,F,F,W,F,F,F,F,W,F,F,F,F,F,W},
            {W,F,W,W,W,W,W,W,W,W,F,W,F,W,W,W,W,W,W,W,W,F,W},
            {W,F,F,F,F,F,F,F,F,F,F,F,F,F,F,F,F,F,F,F,F,F,W},
            {W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W}
    };
    private Timer timer;
    private int frameCount = 0;
    private JLabel frameLabel;

    public StopMotionAnimation() {
        setTitle("Stop Motion Animation");
        setSize(400, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        JPanel animationPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                // Draw animation frame based on frameCount
                // Example: Draw a bouncing ball
                int x = (frameCount % getWidth()) - 20;
                int y = (frameCount % getHeight()) - 20;
                g.setColor(Color.RED);
                g.fillOval(x, y, 40, 40);
            }
        };

        frameLabel = new JLabel("Frame: " + frameCount, JLabel.CENTER);
        JButton startButton = new JButton("Start");
        JButton stopButton = new JButton("Stop");

        startButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                startAnimation();
            }
        });

        stopButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                stopAnimation();
            }
        });

        JPanel controlPanel = new JPanel();
        controlPanel.add(startButton);
        controlPanel.add(stopButton);

        add(animationPanel, BorderLayout.CENTER);
        add(frameLabel, BorderLayout.NORTH);
        add(controlPanel, BorderLayout.SOUTH);
    }

    private void startAnimation() {
        timer = new Timer(50, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frameCount++;
                frameLabel.setText("Frame: " + frameCount);
                repaint();
            }
        });
        timer.start();
    }

    private void stopAnimation() {
        if (timer != null && timer.isRunning()) {
            timer.stop();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                StopMotionAnimation animation = new StopMotionAnimation();
                animation.setVisible(true);
            }
        });
    }
}
