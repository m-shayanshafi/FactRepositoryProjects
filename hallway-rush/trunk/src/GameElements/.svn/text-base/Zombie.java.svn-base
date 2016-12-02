package GameElements;

import javax.imageio.ImageIO;
import java.io.IOException;
import java.util.Random;

/**
 * Created by mertuygur on 23/12/14.
 */
public class Zombie extends Enemy {
    Random rand = new Random();

    public Zombie() {
        velY = 1;
        x = rand.nextInt(550 - 117) + 90;
        y = rand.nextInt(75) + 7;
        WIDTH = 31;
        HEIGHT = 34;
        try {
            image = ImageIO.read(getClass().getResource("/GameElements/zombiemob.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        isMob = false;
    }

}
