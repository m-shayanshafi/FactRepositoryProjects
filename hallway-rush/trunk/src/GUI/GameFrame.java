package GUI;

import GameCore.GameEngine;

import javax.swing.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

/**
 * Created by mertuygur on 21/12/14.
 */

public class GameFrame extends JFrame{

    public GamePanel gamePanel;
    private CreditsPanel creditsPanel;
    private HelpPanel helpPanel;
    private HighScorePanel highScorePanel;
    private SettingsPanel settingsPanel;
    private MainMenu mainMenu;
    private GameEngine game;


    public GameFrame(GameEngine game) {

        super("Hallway Rush");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.game = game;
        setSize(640, 480);
        setLocationRelativeTo(null);
        setResizable(true);
        setVisible(true);


        helpPanel = new HelpPanel(this);
        getContentPane().add(helpPanel);
        helpPanel.setVisible(false);

        mainMenu = new MainMenu(this);
        getContentPane().add(mainMenu);
        setContentPane(mainMenu);
        mainMenu.setVisible(true);

        creditsPanel = new CreditsPanel(this);
        getContentPane().add(creditsPanel);
        creditsPanel.setVisible(false);

        settingsPanel = new SettingsPanel(this);
        getContentPane().add(settingsPanel);
        settingsPanel.setVisible(false);

        highScorePanel = new HighScorePanel(this);
        getContentPane().add(highScorePanel);
        highScorePanel.setVisible(false);

        addKeyListener(new keyboardHandler());
    }

    public void switchPanel(String s)
    {
        if(s == "start")
        {
            gamePanel = new GamePanel(game, settingsPanel.ambientSelect);
            game.mainCharacter.setImage(settingsPanel.genderSelect);
            getContentPane().add(gamePanel);
            setContentPane(gamePanel);
            game.init();
            gamePanel.setVisible(true);
            game.start();
            requestFocusInWindow();
        }
        else if(s == "Options")
        {
            setContentPane(settingsPanel);
            settingsPanel.setVisible(true);
        }
        else if(s == "Help")
        {
            setContentPane(helpPanel);
            helpPanel.setVisible(true);
        }
        else if(s == "credits")
        {
            setContentPane(creditsPanel);
            creditsPanel.setVisible(true);
        }
        if(s == "main")
        {
            setContentPane(mainMenu);
            mainMenu.setVisible(true);
        }

    }
    private class keyboardHandler extends KeyAdapter
    {

        public void keyPressed(KeyEvent e)
        {
            int key = e.getKeyCode();
            if(key == KeyEvent.VK_M) {
                switchPanel("main");
                game.running = false;
            }
            else if(key == KeyEvent.VK_P){
                if(game.running == true)
                    game.running = false;
                else
                    game.start();
            }
            else
                gamePanel.keyPressed(e);
        }
        public void keyReleased(KeyEvent e)
        {
            gamePanel.keyReleased(e);
        }

    }


}
