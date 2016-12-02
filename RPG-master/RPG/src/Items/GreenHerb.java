package Items;
import Actor.*;
import Game.TextHandler;

import java.util.Vector;

/**
 * Created by piano_000 on 5/22/2015.
 */
public class GreenHerb extends Item {

    public GreenHerb() {
        name = "Green Herb";
        description = "A natural herb that heals 20 HP";
        value = 20;
        intent = Intent.HEAL;
    }

    /*
     Only works on non-dead party members

    @Override
    public void use(Actor caster, Vector<Actor> opponents, int target) {
        Actor opponent = opponents.get(target);
        if(opponent.getHealth() > 0) {
            opponent.addHealth(value);
            out.printToConsole(opponent.getName() + " was healed for " + value + " HP");
            caster.getItems().remove(this);
        }
    }
    */
}
