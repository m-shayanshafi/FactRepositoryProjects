import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;

public class EnergyPot extends Potion
{
    public EnergyPot(){
        super();
        this.name = "Potion of Energy";
        this.desc = "A yellow, light concoction. Invigorating and mildly sweet.";
        this.energy = 10;
        try {
            this.picture = ImageIO.read(new File("Resources/Potions/Energy Potion.png"));
        }   catch   (IOException ioe)   {
            ioe.printStackTrace();
        }
    }
}
