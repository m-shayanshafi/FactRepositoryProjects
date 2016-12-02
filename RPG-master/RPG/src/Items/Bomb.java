package Items;
import Actor.*;
import Game.TextHandler;

import java.awt.*;
import java.util.Vector;

/**
 * Created by piano_000 on 5/23/2015.
 */
public class Bomb extends Item {

    public Bomb() {
        name = "Bomb";
        description = "A small throwable bomb.";
        value = 25;
        intent = Intent.HARM;
    }
/*
    @Override
    public void use(Actor caster, Vector<Actor> opponents, int target) {
        Boolean hit = caster.hitCalculator();
        Actor opponent = opponents.get(target);
        if (hit) {
            //Attack lands
            out.printToConsole(caster.getName() + " used " + name + "!");
            opponent.subHealth(value);
            out.printToConsole(caster.getName() + " dealt " + value + " damage to " + opponent.getName());
            if (opponent.getHealth() <= 0) {
                out.printToConsole(opponent.getName() + " has been felled!");
            }
        } else {
            //Attack missed
            out.printToConsole(caster.getName() + " missed their use!");
        }
        caster.getItems().remove(this);
    } */
}
