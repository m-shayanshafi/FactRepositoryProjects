package Skills;

import Actor.Actor;
import Game.TextHandler;
import Items.Intent;

import java.util.Vector;

/**
 * Created by piano_000 on 9/10/2015.
 */
public class FireBreath extends Skill {

    protected TextHandler out = TextHandler.getInstance();

    public FireBreath() {
        this.name = "Fire Breath";
        this.description = "Fire attack that hits the entire party";
        this.damage = 8;
        this.element = null;
        this.TPCost = 15;
        this.intent = Intent.HARM;
        this.AOE = true;
    }

    public double use(Actor attacker, Vector<Actor> opponents, int target) {

        double threatBuilt = 0;
        if ((attacker.getTechniquePoints() - TPCost) >= 0) {
            out.printToConsole(attacker.getName() + " used " + name + "!");
            for (int i = 0; i < opponents.size(); i++) {
                Actor opponent = opponents.get(i);
                opponent.setHealth(opponent.getHealth() - damage);
                out.printToConsole(attacker.getName() + " dealt " + this.damage + " damage to " + opponent.getName());
                threatBuilt = threatBuilt + damage;
            }
            attacker.subTechniquePoints(TPCost);
        } else {
            out.printToConsole("Not enough TP!");
        }
        return threatBuilt;
    }
}
