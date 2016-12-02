package GUI;

import GameCore.GameEngine;
import GameElements.Bullet;
import GameElements.Chest;
import GameElements.Enemy;
import GameElements.MainCharacter;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by mertuygur on 21/12/14.
 */
public class GamePanel extends JPanel {
    public final int HEIGHT = 480;
    public final int WIDTH = 640;
    private Image background;
    private MainCharacter mainCharacter;
    private ArrayList<Enemy> enemies;
    private ArrayList<Bullet> bullets;
    private GameEngine game;
    private ArrayList<Chest> chest;

    public GamePanel(GameEngine game, int ambient)
    {
        this.game = game;
        setLayout(null);
        if (ambient == 0) {
            try {
                background = ImageIO.read(getClass().getResource("/GUI/background-day.png"));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        else
            try {
                background = ImageIO.read(getClass().getResource("/GUI/background-night.png"));
            } catch (IOException e) {
                e.printStackTrace();
            }
        setSize(WIDTH, HEIGHT);

    }
    public void getMainCharacter(MainCharacter character)
    {
        mainCharacter = character;
    }
    public void getEnemies(ArrayList enemies)
    {
        this.enemies = enemies;
    }
    public void getBullets(ArrayList bullets)
    {
        this.bullets = bullets;
    }
    public void getChest(ArrayList chest)
    {
        this.chest = chest;
    }

    public void paintComponent (Graphics page)
    {
        super.paintComponent(page);
        Graphics2D g2d = (Graphics2D) page;
        g2d.drawImage(background, 0, 0, this);
        for (int i = 0; i < enemies.size(); i++) {
            g2d.drawImage(enemies.get(i).getImage(), enemies.get(i).getX(), enemies.get(i).getY(), this);
        }
        for (int i = 0; i < bullets.size(); i++) {
            g2d.drawImage(bullets.get(i).getImage(), bullets.get(i).getX(), bullets.get(i).getY(), this);
        }
        for (int i = 0; i < chest.size(); i++) {
            g2d.drawImage(chest.get(i).getImage(), chest.get(i).getX(), chest.get(i).getY(), this);
        }
        g2d.drawImage(mainCharacter.getImage(), mainCharacter.getX(), mainCharacter.getY(), this);
        g2d.drawRect(10, 10, 100, 20);
        g2d.setColor(Color.GREEN);
        g2d.fillRect(10, 10, mainCharacter.getHealth() * 10, 20);
        g2d.drawString("Score " + mainCharacter.getCurrentScore(), 500, 10);
        g2d.drawString("Lives" + mainCharacter.getLives(), 10, 50);
        Toolkit.getDefaultToolkit().sync();
        page.dispose();
    }

    public void keyPressed(KeyEvent e)
    {
        int key = e.getKeyCode();

        if(key == KeyEvent.VK_LEFT)
        {
            mainCharacter.setVelX(-5);
        }
        else if(key == KeyEvent.VK_RIGHT)
        {
            mainCharacter.setVelX(5);
        }
        else if(key == KeyEvent.VK_SPACE)
        {
            game.shoot();
        }

    }
    public void keyReleased(KeyEvent e)
    {
        int key = e.getKeyCode();

        if(key == KeyEvent.VK_LEFT)
        {
            mainCharacter.setVelX(0);
        }
        else if(key == KeyEvent.VK_RIGHT)
        {
            mainCharacter.setVelX(0);
        }
    }

}
