package Test.Skills;

import Actor.*;
import FightClasses.Knight;
import FightClasses.Wizard;
import Items.Sword;
import Skills.FireBreath;
import org.junit.Assert;
import org.junit.Test;

import java.util.Vector;

/**
 * Created by piano_000 on 9/21/2015.
 */
public class FireBreathTest {

    @Test
    public void TestInit(){
        FireBreath fb = new FireBreath();
        Assert.assertNotNull(fb);
    }

    @Test
    public void TestUse() {
        Hero Link = new Hero("Link", new Knight());
        Hero Zelda = new Hero("Zelda", new Wizard());
        Sword woodenSword = new Sword("Wooden Sword", "Just a sword made of wood", false);
        Link.setWeapon(woodenSword);

        Vector<Actor> party = new Vector<Actor>();
        party.add(Link);
        party.add(Zelda);

        Dragon dragon = new Dragon();
        FireBreath fb = new FireBreath();
        dragon.addSkill(fb);


        double threatcount = dragon.getSkills().get(0).use(dragon, party, 0);
        Assert.assertEquals((fb.getDamage() * 2), threatcount, 0);
    }
}
