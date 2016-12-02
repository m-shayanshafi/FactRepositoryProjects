package Skills;
import Items.Intent;

/**
 * Created by piano_000 on 5/22/2015.
 */
public class Fireball extends Skill{

    public Fireball() {
        this.name = "Fire Ball";
        this.description = "Launch a weak fireball";
        this.damage = 6;
        this.element = element.FIRE;
        this.TPCost = 8;
        this.intent = Intent.HARM;
    }
}
