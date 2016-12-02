package GameElements;

import javax.imageio.ImageIO;
import java.io.IOException;
import java.util.Random;

/**
 * Created by doga on 23/12/14.
 */
public class Chest extends GameObject {

    private PowerUp power;

    private int randomNum;
    Random rand;

    public Chest() {

        rand = new Random();
        x = rand.nextInt(550 - 117) + 90;
        y = rand.nextInt(340) + 20;
        WIDTH = 33;
        HEIGHT = 26;


        try {
            image = ImageIO.read((getClass().getResource("/GameElements/chestfinal.png")));
        } catch (IOException e) {
            e.printStackTrace();
        }

            this.randomNum = rand.nextInt(3);

            switch (randomNum) {
                case 0:
                    this.power = new PowerUp(PowerUpType.hallwayrage);
                    break;
                case 1:
                    this.power = new PowerUp(PowerUpType.health);
                    break;
                case 2:
                    this.power = new PowerUp(PowerUpType.pushback);
                    break;
            }

        }
    public PowerUp getPower()
    {
        return power;
    }
}
