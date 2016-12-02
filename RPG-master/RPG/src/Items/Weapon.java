package Items;
import Augments.Augment;
import Actor.*;
import Game.TextHandler;

import java.util.Vector;

public abstract class Weapon extends Item {

	protected Boolean twoHanded;
	protected double damage;
	protected Augment augments;
	protected TextHandler out = TextHandler.getInstance();
	
	public Boolean getTwoHanded(){
		return twoHanded;
	}
	
	public double getDamage(){
		return damage + augments.getModifier();
	}
	
	public String getAugmentName() {
		return augments.getName();
	}
	
	public double getAugmentDmg() {
		return augments.getModifier();
	}
	
	public void setAugment(Augment a) {
		augments = a;
	}
 	
	public void getDetails() {
		out.printToConsole(name + " is " + description + ". It does " + damage + " damage.");
	}

	public void use(Actor caster, Vector<Actor> opponents, int target) {
		out.printToConsole("Cannot use " + name + ".");
	}
}
