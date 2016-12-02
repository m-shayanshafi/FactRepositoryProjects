package GUI;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.IOException;

/**
 * Created by mertuygur on 21/12/14.
 */
public class MainMenu extends JPanel {

    private GameFrame gameFrame;
    private JButton startButton, highScoreButton ,settingsButton, helpButton, creditsButton, quitButton;
    @SuppressWarnings("FieldCanBeLocal")
    private JLabel picHolder, bannerHolder;
    private BufferedImage title, banner;

    public MainMenu(GameFrame frame) {
        gameFrame = frame;

        try {
            title = ImageIO.read(getClass().getResource("/GUI/title.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            banner = ImageIO.read(getClass().getResource("/GUI/zombie.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        bannerHolder = new JLabel(new ImageIcon(banner));
        picHolder = new JLabel(new ImageIcon(title));
        startButton = new JButton("Start Game");
        settingsButton = new JButton("Settings");
        helpButton = new JButton("Help");
        creditsButton = new JButton("Credits");
        quitButton = new JButton("Quit");
        highScoreButton = new JButton("Highscores");

        startButton.addActionListener(new ButtonListener());
        settingsButton.addActionListener(new ButtonListener());
        helpButton.addActionListener(new ButtonListener());
        creditsButton.addActionListener(new ButtonListener());
        quitButton.addActionListener(new ButtonListener());
        highScoreButton.addActionListener(new ButtonListener());

        setLayout(null);
        setBackground(new Color(238, 233, 233));

        add(startButton);
        add(settingsButton);
        add(helpButton);
        add(creditsButton);
        add(quitButton);
        add(highScoreButton);
        add(picHolder);
        add(bannerHolder);

        bannerHolder.setBounds(75,110, 200, 271);
        picHolder.setBounds(110, 30, 402, 56);
        startButton.setBounds(350, 120, 160, 50);
        settingsButton.setBounds(350, 180, 160, 50);
        helpButton.setBounds(350, 240, 160, 50);
        creditsButton.setBounds(350, 300, 160, 50);
        quitButton.setBounds(350, 360, 160,50);

        startButton.setFont(new Font("Arial", 1, 20));
        settingsButton.setFont(new Font("Arial", 1, 20));
        helpButton.setFont(new Font("Arial", 1, 20));
        creditsButton.setFont(new Font("Arial", 1, 20));
        quitButton.setFont(new Font("Arial", 1, 20));
        highScoreButton.setFont(new Font("Arial", 1, 20));
    }
    private class ButtonListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            if(e.getSource() == startButton){
                gameFrame.switchPanel("start");
            }
            else if(e.getSource() == settingsButton){
                gameFrame.switchPanel("Options");
            }
            else if(e.getSource() == helpButton){
                gameFrame.switchPanel("Help");
            }
            else if(e.getSource() == creditsButton){
                gameFrame.switchPanel("credits");
            }
            else if (e.getSource() == quitButton){
                System.exit(0);
            }
        }
    }

}
