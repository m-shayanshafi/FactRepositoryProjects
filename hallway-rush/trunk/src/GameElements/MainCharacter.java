package GameElements;

import javax.imageio.ImageIO;
import java.io.IOException;

/**
 * Created by mertuygur on 21/12/14.
 */
public class MainCharacter extends DynamicObject {

    private static MainCharacter player = new MainCharacter();
    private int health;
    private int currentScore;
    private int lives;

    private MainCharacter()
    {
        lives = 3;
        health = 10;
        currentScore = 0;
        WIDTH = 27;
        HEIGHT = 32;
        x = 320;
        y = 420;

    }
    public static MainCharacter getInstance()
    {
        if(player == null)
        {
            player = new MainCharacter();
        }
        return player;
    }

    public void setImage(int gender)
    {
        if(gender == 1) {
            try {
                image = ImageIO.read(getClass().getResource("/GameElements/male.png"));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        else
        {
            try {
                image = ImageIO.read(getClass().getResource("/GameElements/female.png"));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    public void move() {
        x += velX;
    }

    public int getCurrentScore() {
        return currentScore;
    }

    public void setCurrentScore(int currentScore) {
        this.currentScore = currentScore;
    }

    public int getHealth() {
        return health;
    }

    public void setHealth(int health) {
        this.health = health;
    }

    public int getLives() {
        return lives;
    }

    public void setLives(int lives) {
        this.lives = lives;
    }
}
