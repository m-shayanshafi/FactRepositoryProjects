package Items;

import Augments.BaseAugment;
import FightClasses.RandomGenerator;

/**
 * Created by piano_000 on 9/4/2015.
 */
public class Claw extends Weapon{
    RandomGenerator generator = new RandomGenerator();
    BaseAugment normal = new BaseAugment();

    public Claw(String name, String description, Boolean twohands){
        this.name = name;
        this.description = description;
        this.twoHanded = twohands;
        this.damage = generator.weaponDamage();
        this.augments = normal;
        this.intent = Intent.EQUIP;
    }
}
