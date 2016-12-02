package FightClasses;

import Skills.DoubleStrike;

public class Knight extends BattleClass {

	protected RandomGenerator fightMachine = new RandomGenerator();
	
	public Knight() {
		className = "Knight";
		health = 35.0;
		damageMod = fightMachine.battleRandom() + 3;
		skill = new DoubleStrike();
		techniquePoints = 20;
	}
}
