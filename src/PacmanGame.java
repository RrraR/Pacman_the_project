import Components.*;

import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.plaf.FontUIResource;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.util.*;
import java.util.List;

import static Components.HighScoresManager.loadHighScores;

public class PacmanGame {

    public JFrame frame;
    private JPanel startGamePanel;
    private JScrollPane highScoresPanel;
    private String[] boardSized = { "23x24", "27x18", "21x21", "31x11", "15x21"};

    public PacmanGame(){
        frame = new JFrame("Pacman");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setBackground(Color.black);
        frame.setLayout(new CardLayout());
        frame.setFocusable(true);

        try {
            Font font = Font.createFont(Font.TRUETYPE_FONT, getClass().getClassLoader().getResource("resources\\fonts\\pacman.ttf").openStream());
            Font pacmanFont = font.deriveFont(15f);
            GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
            ge.registerFont(pacmanFont);

            setUIManagerVariables(new FontUIResource(pacmanFont));
        } catch (FontFormatException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        createStartScreen();

        frame.add(startGamePanel, "StartScreen");

        frame.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
                    showStartScreen();
                }
            }
        });

        frame.pack();
        showStartScreen();
        frame.setVisible(true);
    }

    public static void setUIManagerVariables(FontUIResource f) {
        Enumeration<Object> keys = UIManager.getDefaults().keys();
        while (keys.hasMoreElements()) {
            Object key = keys.nextElement();
            Object value = UIManager.get(key);
            if (value instanceof javax.swing.plaf.FontUIResource) {
                UIManager.put(key, f);
            }
        }

        UIManager.put("Panel.background", Color.BLACK);
        UIManager.put("OptionPane.background", Color.BLACK);
        UIManager.put("OptionPane.messageForeground", Color.WHITE);
    }

    private void createStartScreen() {
        startGamePanel = new JPanel();
        startGamePanel.setBackground(Color.black);
        startGamePanel.setLayout(new GridBagLayout());
        startGamePanel.setPreferredSize(new Dimension(285, 285));
        GridBagConstraints gbc = new GridBagConstraints();

        ImageIcon pacmanText = new ImageIcon(getClass().getClassLoader().getResource("resources\\other\\pacman_logo.png"));
        JLabel pacmanTextLabel = new JLabel(pacmanText);
        pacmanTextLabel.setBackground(Color.black);

        JButton newGameButton = new JButton("New Game");
        newGameButton.setBackground(Color.black);
        newGameButton.setForeground(Color.white);
        newGameButton.setBorder(new LineBorder(Color.BLUE));
        newGameButton.addActionListener(e -> createGameScreen());

        JButton highScoresButton = new JButton("High Scores");
        highScoresButton.setBackground(Color.black);
        highScoresButton.setForeground(Color.white);
        highScoresButton.setBorder(new LineBorder(Color.BLUE));
        highScoresButton.addActionListener(e -> createHighScoresPanel());

        JButton exitButton = new JButton("Exit");
        exitButton.setBackground(Color.black);
        exitButton.setForeground(Color.WHITE);
        exitButton.setBorder(new LineBorder(Color.BLUE));
        exitButton.addActionListener(e -> System.exit(0));

        Dimension buttonSize = new Dimension(150, 40);
        newGameButton.setPreferredSize(buttonSize);
        highScoresButton.setPreferredSize(buttonSize);
        exitButton.setPreferredSize(buttonSize);

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(10, 0, 10, 0);
        gbc.anchor = GridBagConstraints.CENTER;
        startGamePanel.add(pacmanTextLabel, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.insets = new Insets(5, 0, 5, 0);
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.fill = GridBagConstraints.NONE;
        startGamePanel.add(newGameButton, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.insets = new Insets(5, 0, 5, 0);
        startGamePanel.add(highScoresButton, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.insets = new Insets(5, 0, 20, 0);
        startGamePanel.add(exitButton, gbc);
//        frame.pack();
    }

    private void createGameScreen() {
        String selectedBoardSize = (String) JOptionPane.showInputDialog(frame, "Choose the board size", "Choose Board size", JOptionPane.QUESTION_MESSAGE, null, boardSized, boardSized[0]);
        if (selectedBoardSize == null){
            return;
        }

        GameFrame gameFrame = new GameFrame(selectedBoardSize);
        gameFrame.setVisible(true);
    }

    private void createHighScoresPanel() {
        highScoresPanel = new JScrollPane();
        highScoresPanel.setBackground(Color.black);
        highScoresPanel.setPreferredSize(startGamePanel.getPreferredSize());
        JPanel panel = new JPanel();
        panel.setBackground(Color.black);
        panel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();

        JLabel nameLabel = new JLabel("NAME");
        nameLabel.setBackground(Color.black);
        nameLabel.setForeground(Color.white);

        JLabel scoreLabel = new JLabel("SCORE");
        scoreLabel.setBackground(Color.black);
        scoreLabel.setForeground(Color.white);

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 0.5;
        gbc.anchor = GridBagConstraints.CENTER;
        panel.add(nameLabel, gbc);

        gbc.gridx = 1;
        gbc.gridy = 0;
        panel.add(scoreLabel, gbc);

        List<HighScore> scoresList = loadHighScores();
        scoresList.sort(Comparator.comparing(HighScore::getScore));
        List<String> playersNames = new ArrayList<>();
        List<String> playersScores = new ArrayList<>();
        for (HighScore score : scoresList.reversed()) {
            playersNames.add(score.getPlayerName());
            playersScores.add(String.valueOf(score.getScore()));
        }
        JList<String> namesJList = new JList<>(playersNames.toArray(new String[0]));
        namesJList.setBackground(Color.black);
        namesJList.setForeground(Color.white);

        JList<String> scoresJList = new JList<>(playersScores.toArray(new String[0]));
        scoresJList.setBackground(Color.black);
        scoresJList.setForeground(Color.white);

        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weightx = 0.5;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.BOTH;
        panel.add(namesJList, gbc);

        gbc.gridx = 1;
        gbc.gridy = 1;
        panel.add(scoresJList, gbc);

        highScoresPanel.setViewportView(panel);

        frame.add(highScoresPanel, "HighScoresScreen");

        CardLayout cl = (CardLayout) frame.getContentPane().getLayout();
        cl.show(frame.getContentPane(), "HighScoresScreen");
        frame.pack();
    }


    public void showStartScreen() {
        CardLayout cl = (CardLayout) frame.getContentPane().getLayout();
        cl.show(frame.getContentPane(), "StartScreen");
        frame.pack();
    }

    public static void main(String[] args){
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new PacmanGame();
            }
        });
    }
}

