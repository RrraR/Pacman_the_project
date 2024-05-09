import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.TimerTask;

public class Animate extends TimerTask {

    private JPanel test;
    private Image pacman1;
    private Image pacman2;
    private Image pacman3;

    public Animate(JPanel pacPanel) throws IOException {
        test = pacPanel;
        pacman1 = ImageIO.read(new File("D:\\Documents\\uni2\\sem 2\\GUI\\Project\\resources\\pacman_right_1.png"));
        pacman2 = ImageIO.read(new File("D:\\Documents\\uni2\\sem 2\\GUI\\Project\\resources\\pacman_right_2.png"));
        pacman3 = ImageIO.read(new File("D:\\Documents\\uni2\\sem 2\\GUI\\Project\\resources\\pacman_whole.png"));
        run();
    }

    @Override
    public void run() {
        int now = LocalDateTime.now().getSecond();
        new JLabel();
        JLabel picLabel;
        if (now < 20){
            picLabel = new JLabel(new ImageIcon(pacman1));
        } else if (20 < now && now < 40) {
            picLabel = new JLabel(new ImageIcon(pacman2));
        } else {
            picLabel = new JLabel(new ImageIcon(pacman3));
        }
        test.add(picLabel);
    }

}
