import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.util.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Timer;

public class Pacman extends JPanel {

    private Timer timer;

    public Pacman(JPanel pacPanel) throws IOException {
        timer = new Timer();
        timer.scheduleAtFixedRate(new Animate(pacPanel), 100, 1000);
    }

//    @Override
//    public void actionPerformed(ActionEvent e) {
//
//    }
}
