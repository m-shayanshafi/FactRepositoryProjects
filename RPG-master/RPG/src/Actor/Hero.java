package Actor;
import FightClasses.BattleClass;
import Items.*;

public class Hero extends Actor {
	protected BattleClass fightclass;
	
	public Hero(String name, BattleClass type) {
		this.name = name;
		fightclass = type;
		weapon = null;
		health = fightclass.getHealth();
		maxHealth = fightclass.getHealth();
		techniquePoints = fightclass.getTechniquePoints();
		maxTechniquePoints = fightclass.getTechniquePoints();
		skills.add(fightclass.getSkill());
	}
	
	public int getDamageMod() {
		return fightclass.getdamageMod();
	}
	
	public void setWeapon(Weapon w) {
		this.weapon = w;
	}
	
	public double getCombatDmg() {
		return getDamageMod() + weapon.getDamage();
	}
}
