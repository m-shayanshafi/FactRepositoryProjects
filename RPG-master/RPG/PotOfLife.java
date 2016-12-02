import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;

public class PotOfLife extends Potion
{
    public PotOfLife(){
        super();
        this.name = "Potion of Life";
        this.desc = "A concentrated mixture of other potions, it bubbles and rattles violently.";
        this.health = 20;
        this.energy = 20;
        this.bloodlust = 3;
        try {
            this.picture = ImageIO.read(new File("Resources/Potions/Life Potion.png"));
        }   catch   (IOException ioe)   {
            ioe.printStackTrace();
        }
    }
}
