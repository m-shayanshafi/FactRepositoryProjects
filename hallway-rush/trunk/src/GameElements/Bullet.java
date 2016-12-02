package GameElements;

import javax.imageio.ImageIO;
import java.io.IOException;

/**
 * Created by mertuygur on 23/12/14.
 */
public class Bullet extends DynamicObject{


    public Bullet(int x, int y)
    {
        this.x = x;
        this.y = y;
        WIDTH = 5;
        HEIGHT = 14;
        velY = -3;
        try {
            image = ImageIO.read(getClass().getResource("/GameElements/bullet.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void move() {
        y += velY;
    }

}
