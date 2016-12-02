import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;

public class RejuvenationPot extends Potion
{
    public RejuvenationPot(){
        super();
        this.name = "Potion of Rejuvenation";
        this.desc = "A combination of other potions, it swirls and shakes itself, seeming conflicted.";
        this.health = 10;
        this.energy = 10;
        this.bloodlust = 2;
        try {
            this.picture = ImageIO.read(new File("Resources/Potions/Rejuvination Potion.png"));
        }   catch   (IOException ioe)   {
            ioe.printStackTrace();
        }
    }
}
