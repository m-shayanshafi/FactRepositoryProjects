package Actor;
import Actor.*;
import Items.*;

import java.util.Vector;

public class Guard extends Actor {

	public Guard() {
		this.name = "Guard";
		this.description = "A palace guard";
		this.health = 21;
		this.maxHealth = 21;
		this.techniquePoints = 10;
		this.maxTechniquePoints = 10;
		this.damageMod = 2;
		this.items = new Vector<Item>();
		this.weapon = new Sword("Iron Sword", "An iron sword. Standard issue.", false);
	}
}
