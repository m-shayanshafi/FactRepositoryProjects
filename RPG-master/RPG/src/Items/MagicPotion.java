package Items;
import Actor.*;
import Game.TextHandler;

import java.util.Vector;

/**
 * Created by piano_000 on 5/29/2015.
 */
public class MagicPotion extends Item {

    protected int restore;
    protected TextHandler out = TextHandler.getInstance();

    public MagicPotion() {
        name = "Magic Potion";
        description = "A potion that heals 25 TP";
        value = 25;
        this.intent = Intent.RESTORE;
    }
/*
    @Override
    public void use(Actor caster, Vector<Actor> opponents, int target) {
        Actor opponent = opponents.get(target);
        if(opponent.getHealth() > 0) {
            opponent.addTechniquePoints(value);
            out.printToConsole(caster.getName() + "restored " + value + " of TP for " + opponent.getName());
            caster.getItems().remove(this);
        }
    }
    */
}
