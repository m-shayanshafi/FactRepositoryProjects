package Skills;

import Actor.Actor;
import Items.Intent;

import java.util.Vector;

/**
 * Created by piano_000 on 9/16/2015.
 */
public class WeakHealAll extends Skill {

    public WeakHealAll() {
        this.name = "Weak Heal All";
        this.description = "Heals all allies for 15 HP";
        this.damage = 15; // TODO: I should probably rename damage to something else.
        this.element = null;
        this.TPCost = 30;
        this.intent = Intent.HEAL;
        this.AOE = true;
    }

    public double use(Actor attacker, Vector<Actor> opponents, int target) {
        double threatBuilt = 0;
        if ((attacker.getTechniquePoints() - TPCost) >= 0) {
            out.printToConsole(attacker.getName() + " used " + name + "!");
            for(int i = 0; i < opponents.size(); i++) {
                Actor opponent = opponents.get(i);
                threatBuilt = threatBuilt + opponent.addHealth(damage);
                out.printToConsole(attacker.getName() + " healed " + opponent.getName() + " for " + damage);
            }
            attacker.setTechniquePoints(attacker.getTechniquePoints() - TPCost);
        } else {
            out.printToConsole("Not enough TP!");
        }
        return threatBuilt;
    }
}

