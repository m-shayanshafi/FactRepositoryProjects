package Skills;

import Actor.Actor;
import Game.TextHandler;
import Items.Intent;
import Actor.*;

import java.util.Vector;

/**
 * Created by piano_000 on 5/24/2015.
 */
public class Heal extends Skill {

    protected TextHandler out = TextHandler.getInstance();

    public Heal() {
        this.name = "Heal";
        this.description = "Heals an ally for 20 HP";
        this.damage = 20; // TODO: I should probably rename damage to something else.
        this.element = null;
        this.TPCost = 15;
        this.intent = Intent.HEAL;
    }

    /*
    You cannot miss a heal
     */
    public double use(Actor attacker, Vector<Actor> opponents, int target) {
        double threatBuilt = 0;
        Actor opponent = opponents.get(target);
        if ((attacker.getTechniquePoints() - TPCost) >= 0) {

            out.printToConsole(attacker.getName() + " used " + name + "!");
            opponent.addHealth(damage);
            out.printToConsole(attacker.getName() + " healed " + opponent.getName() + " for " + damage);
            threatBuilt = this.damage;
            attacker.subTechniquePoints(TPCost);
        } else {
            out.printToConsole("Not enough TP!");
        }
        return threatBuilt;
    }
}
