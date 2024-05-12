import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.TimerTask;

public class Animate extends TimerTask {

    private JPanel test;
    private Image pacman1;//= ImageIO.read(new File("D:\\Documents\\uni2\\sem 2\\GUI\\Project\\resources\\pacman_right_1.png"));;
    private Image pacman2;// = ImageIO.read(new File("D:\\Documents\\uni2\\sem 2\\GUI\\Project\\resources\\pacman_right_2.png"));
    private Image pacman3;// = ImageIO.read(new File("D:\\Documents\\uni2\\sem 2\\GUI\\Project\\resources\\pacman_whole.png"));

    public Animate(JPanel pacPanel) throws IOException {
        test = pacPanel;
    }

    @Override
    public void run() {

        int now = LocalDateTime.now().getSecond();
        System.out.println("animate "  + now);
        try {
            pacman1 = ImageIO.read(new File("D:\\Documents\\uni2\\sem 2\\GUI\\Project\\resources\\pacman_right_1.png"));
            pacman2 = ImageIO.read(new File("D:\\Documents\\uni2\\sem 2\\GUI\\Project\\resources\\pacman_right_2.png"));
            pacman3 = ImageIO.read(new File("D:\\Documents\\uni2\\sem 2\\GUI\\Project\\resources\\pacman_whole.png"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        if (now < 20){
            test.removeAll();
            test.revalidate();
            test.add(new JLabel(new ImageIcon(pacman1)));
        } else if (20 < now && now < 40) {
            test.removeAll();
            test.revalidate();
            test.add(new JLabel(new ImageIcon(pacman2)));
        } else {
            test.removeAll();
            test.revalidate();
            test.add(new JLabel(new ImageIcon(pacman3)));
        }

    }

}
