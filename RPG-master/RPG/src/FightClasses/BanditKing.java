package FightClasses;

import Skills.TripleStrike;

/**
 * Created by piano_000 on 5/27/2015.
 */
public class BanditKing extends BattleClass {
    protected RandomGenerator fightMachine = new RandomGenerator();

    public BanditKing() {
        className = "Bandit King";
        health = 50.0;
        damageMod = fightMachine.battleRandom() + 0;
        skill = new TripleStrike();
        techniquePoints = 60;
    }
}
