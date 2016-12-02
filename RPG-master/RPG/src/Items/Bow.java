package Items;

import Augments.BaseAugment;
import FightClasses.RandomGenerator;

public class Bow extends Weapon{
	RandomGenerator generator = new RandomGenerator();
	BaseAugment normal = new BaseAugment();
	
	public Bow(String name, String description, Boolean twohands){
		this.name = name;
		this.description = description;
		this.twoHanded = twohands;
		this.damage = generator.weaponDamage();
		this.augments = normal;
		this.intent = Intent.EQUIP;
	}
}