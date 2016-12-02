import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;

public class HealthPot extends Potion
{
    public HealthPot(){
        super();
        this.name = "Potion of Health";
        this.desc = "A healing concoction. Red and thick, much like blood.";
        this.health = 10;
        try {
            this.picture = ImageIO.read(new File("Resources/Potions/Health Potion.png"));
        }   catch   (IOException ioe)   {
            ioe.printStackTrace();
        }
    }
}
