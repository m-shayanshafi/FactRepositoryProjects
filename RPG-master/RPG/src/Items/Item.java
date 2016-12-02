package Items;
import Actor.*;
import Game.TextHandler;

import java.util.Vector;

public abstract class Item {

	protected String name;
	protected String description;
	protected int value; //Either damage or healing value
	protected Intent intent;
	protected Boolean AOE = false;
	protected TextHandler out = TextHandler.getInstance();
	
	public String getName() { return name; }
	
	public String getDescription() {
		return description;
	}

	public Intent getIntent() { return intent;}

	public double getValue() { return value;}

	public void use(Actor caster, Vector<Actor> targets, int target) {
		if(AOE) {
			for (int i = 0; i < targets.size(); i++) {
				Actor opponent = targets.get(i);
				if(intent == Intent.HARM && opponent.getHealth() > 0) {
					opponent.subHealth(value);
					out.printToConsole(caster.getName() + " dealt " + value + " damage to " + opponent.getName() + " with " + name);
				} else if(intent == Intent.HEAL && opponent.getHealth() > 0) {
					opponent.addHealth(value);
					out.printToConsole(caster.getName() + " healed " + value + " for" + opponent.getName() + " with " + name);
				} else if(intent == Intent.RESTORE && opponent.getHealth() > 0) {
					opponent.addTechniquePoints(value);
					out.printToConsole(caster.getName() + "restored " + value + " of TP for " + opponent.getName() + " with " + name);
				}
			}
		} else {
			Actor opponent = targets.get(target);
			if(intent == Intent.HARM && opponent.getHealth() > 0) {
				opponent.subHealth(value);
				out.printToConsole(caster.getName() + " dealt " + value + " damage to " + opponent.getName() + " with " + name);
			} else if(intent == Intent.HEAL && opponent.getHealth() > 0) {
				opponent.addHealth(value);
				out.printToConsole(caster.getName() + " healed " + value + " for" + opponent.getName() + " with " + name);
			} else if(intent == Intent.RESTORE && opponent.getHealth() > 0) {
				opponent.addTechniquePoints(value);
				out.printToConsole(caster.getName() + "restored " + value + " of TP for " + opponent.getName() + " with " + name);
			}
		}
		caster.getItems().remove(this);
	}
}
