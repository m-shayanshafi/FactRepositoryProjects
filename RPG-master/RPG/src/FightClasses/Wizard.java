package FightClasses;

import Skills.Fireball;

public class Wizard extends BattleClass {
	protected RandomGenerator fightMachine = new RandomGenerator();;
	
	public Wizard() {
		className = "Wizard";
		health = 25.0;
		damageMod = fightMachine.battleRandom() + 1;
		skill = new Fireball();
		techniquePoints = 45;
	}
}
