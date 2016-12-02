package Actor;
import java.util.Vector;
import Actor.*;
import Items.*;
import Skills.FireBreath;
import Skills.WeakHealAll;


/**
 * Created by piano_000 on 9/4/2015.
 */

public class Dragon extends Actor {

    public Dragon() {
        this.name = "Dragon";
        this.description = "A massive dragon.";
        this.health = 250;
        this.maxHealth = 250;
        this.techniquePoints = 100;
        this.maxTechniquePoints = 100;
        this.damageMod = 6;
        this.items = new Vector<Item>();
        this.weapon = new Claw("Dragon Claw", "A dragon claw", false);

        this.addSkill(new FireBreath());
    }
}