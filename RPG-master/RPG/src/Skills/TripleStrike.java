package Skills;
import Actor.Actor;
import Game.TextHandler;
import Items.Intent;

import java.util.Vector;

/**
 * Created by piano_000 on 5/24/2015.
 */
public class TripleStrike extends Skill {

    protected TextHandler out = TextHandler.getInstance();

    public TripleStrike() {
        this.name = "Triple Strike";
        this.description = "Attack the opponent with three strikes";
        this.damage = 0;
        this.element = null;
        this.TPCost = 9;
        this.intent = Intent.HARM;
    }

    @Override
    public double use(Actor attacker, Vector<Actor> opponents, int target) {
        double threatBuilt = 0;
        Actor opponent = opponents.get(target);
        if((attacker.getTechniquePoints() - TPCost) >= 0) {
            out.printToConsole(attacker.getName() + " used " + name + "!");

            //First Attack
            if(attacker.hitCalculator()) {
                threatBuilt = attacker.attack(opponent);
            } else {
                out.printToConsole(attacker.getName() + " missed their attack!");
            }

            //Second Attack
            if(opponent.getHealth() > 0 && attacker.hitCalculator()) { //If the opponent isn't dead, do the second use
                threatBuilt = threatBuilt + attacker.attack(opponent);
            } else {
                out.printToConsole(attacker.getName() + " missed their attack!");
            }

            //Third Attack
            if(opponent.getHealth() > 0 && attacker.hitCalculator()) { //If the opponent isn't dead, do the second use
                threatBuilt = threatBuilt + attacker.attack(opponent);
            } else {
                out.printToConsole(attacker.getName() + " missed their attack!");
            }

            attacker.subTechniquePoints(TPCost);
        } else {
            out.printToConsole("Not enough TP!");
        }
        return threatBuilt;
    }
}
