package GameCore;

import GUI.GameFrame;
import GameElements.*;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;


/**
 * Created by mertuygur on 21/12/14.
 */
public class GameEngine  implements Runnable{

    private static GameEngine game = new GameEngine();
    private ArrayList<DynamicObject> enemies;
    private ArrayList<DynamicObject> bullets;
    private ArrayList<Chest> chests;
    private boolean isBossBeaten;
    public boolean running = false;
    private Thread thread;
    private GameFrame gameFrame;
    private CollisionController physics;
    public MainCharacter mainCharacter;
    private Timer zombieTimer;
    private Timer chestTimer;
    private Timer chestEndTimer;

    private GameEngine()
    {
        mainCharacter = MainCharacter.getInstance();
        enemies = new ArrayList<DynamicObject>();
        bullets = new ArrayList<DynamicObject>();
        chests = new ArrayList<Chest>();
        gameFrame = new GameFrame(this);
        isBossBeaten = false;
        zombieTimer = new Timer();
        chestTimer = new Timer();
        chestEndTimer = new Timer();
        chestEndTimer.schedule(new maintainChestObject(), 17000, 15000);
        chestTimer.schedule(new newChestObject(), 10000, 15000);
        zombieTimer.schedule(new maintainEnemyObjects(), 2000, 10000);

        physics = new CollisionController();
    }
    public void init()
    {
        gameFrame.gamePanel.getEnemies(enemies);
        gameFrame.gamePanel.getBullets(bullets);
        gameFrame.gamePanel.getMainCharacter(mainCharacter);
        gameFrame.gamePanel.getChest(chests);
    }
    public static GameEngine getInstance()
    {
        if (game == null)
        {
            game = new GameEngine();
        }
        return game;
    }

    private void addNewEnemy() {
        enemies.add(new Zombie());
    }

    private void addNewChest() {
        chests.add(new Chest());
    }

    private void removeChest()
    {
        if(!chests.isEmpty())
            chests.remove(0);
    }
    public boolean isBossBeaten() {
        return isBossBeaten;
    }

    public void setBossBeaten(boolean isBossBeaten) {
        this.isBossBeaten = isBossBeaten;
    }

    public synchronized void start()
    {
        if(running)
            return;

        running = true;
        thread = new Thread(this);
        thread.start();
    }

    public synchronized void stop()
    {
        if(!running)
            return;

        running = false;
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.exit(1);
    }

    @Override
    public void run() {
        long lastTime = System.nanoTime();
        final double amountOfTicks = 60.0;
        double ns = 1000000000 / amountOfTicks;
        double delta = 0;
        int updates = 0;
        int frames = 0;
        long timer = System.currentTimeMillis();

        while(running)
        {
            long now = System.nanoTime();
            delta += (now - lastTime) / ns;
            lastTime = now;
            if(delta >= 1) {
                tick();
                updates++;
                delta--;
            }
            render();
            frames++;


            if(System.currentTimeMillis() - timer > 1000){
                timer += 1000;
                System.out.println(updates + " Ticks, FPS " + frames);
                updates = 0;
                frames = 0;
            }

        }
        stop();


    }

    private void tick()
    {
        if (mainCharacter.getX() <= 90)
            mainCharacter.setX(90);
        if (mainCharacter.getX() >= 550 - 27)
            mainCharacter.setX(550 - 27);

        else if(physics.collision(mainCharacter, enemies))
            mainCharacter.setHealth(mainCharacter.getHealth() - 1);
        if(mainCharacter.getHealth() == 0) {
            mainCharacter.setLives(mainCharacter.getLives() - 1);
            mainCharacter.setHealth(10);
        }
        else if (mainCharacter.getLives() == 0) {
            gameFrame.switchPanel("main");
            stop();
        }
        mainCharacter.move();
        for (int i = 0; i < enemies.size(); i++) {
            enemies.get(i).move();
        }

        for (int i = 0; i < bullets.size(); i++)
        {
            bullets.get(i).move();
            if(physics.collision(bullets.get(i), enemies)) {
                bullets.remove(i);
                mainCharacter.setCurrentScore(mainCharacter.getCurrentScore() + 10);
            }
        }


        for (int i = 0; i < bullets.size(); i++) {
            if(chests.size() != 0){
                if (physics.staticCollision(bullets.get(i), chests.get(0))) {
                    applyPowerUp(chests.get(0).getPower());
                    chests.remove(0);
                    bullets.remove(i);
                }
            }
        }

    }
    private void applyPowerUp(PowerUp p)
    {
        int size = enemies.size();
        switch (p.getType()) {
            case hallwayrage:
                for(int i = 0; i < size; i++)
                 {
                     enemies.remove(0);
                 }
                mainCharacter.setCurrentScore(mainCharacter.getCurrentScore()+ (size * 10));
                break;
            case health:
                mainCharacter.setHealth(10);
                break;
            case pushback:
                for(int i = 0; i < size; i++)
                {
                    enemies.get(i).setY(enemies.get(i).getY() - 75);
                }
                break;
            case acidPuddle:
                mainCharacter.setHealth(mainCharacter.getHealth() - 3);
                break;
        }
    }
    private void render()
    {
        gameFrame.gamePanel.repaint();
    }

    public void shoot()
    {
        bullets.add(new Bullet(mainCharacter.getX(), mainCharacter.getY()));
    }

    private class maintainEnemyObjects extends TimerTask {
        public void run() {
            for (int i = 0; i < mainCharacter.getCurrentScore() / 10 + 1; i++)
                addNewEnemy();
            for (int i = 0; i < enemies.size(); i++) {
                if (enemies.get(i).getY() > 480)
                    enemies.remove(i);
            }
            for (int i = 0; i < bullets.size(); i++) {
                if (bullets.get(i).getY() < 0)
                    bullets.remove(i);
            }
        }
    }
    private class maintainChestObject extends TimerTask {
        public void run()
        {
            removeChest();
        }
    }
    private class newChestObject extends TimerTask {
        public void run()
        {
            addNewChest();
        }
    }

    public static void main(String[] args) {
        GameEngine gameEngine;
        gameEngine = GameEngine.getInstance();
    }

}
