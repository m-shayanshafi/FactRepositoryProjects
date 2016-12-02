package Actor;

import Items.Club;
import Actor.*;

public class Hobgoblin extends Actor {
	
	public Hobgoblin() {
		this.name = "Hobgoblin";
		this.description = "A forest Hobgoblin.";
		this.health = 14;
		this.maxHealth = 14;
		this.techniquePoints = 0;
		this.maxTechniquePoints = 0;
		this.damageMod = 1;
		this.items = null;
		this.weapon = new Club("Wooden Club", "A plain wooden club", false);
	}
}
