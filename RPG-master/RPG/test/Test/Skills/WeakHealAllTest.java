package Test.Skills;

import Actor.*;
import FightClasses.Knight;
import FightClasses.Wizard;
import Items.Sword;
import Skills.WeakHealAll;
import org.junit.Assert;
import org.junit.Test;

import java.util.Vector;

/**
 * Created by piano_000 on 9/21/2015.
 */
public class WeakHealAllTest {

    @Test
    public void TestInit(){
        WeakHealAll fb = new WeakHealAll();
        Assert.assertNotNull(fb);
    }

    @Test
    public void TestUse() {
        Hero Link = new Hero("Link", new Knight());
        Hero Zelda = new Hero("Zelda", new Wizard());
        Hero Ganon = new Hero("Ganon", new Knight());
        Sword woodenSword = new Sword("Wooden Sword", "Just a sword made of wood", false);
        Link.setWeapon(woodenSword);

        Vector<Actor> party = new Vector<Actor>();
        party.add(Link);
        party.add(Zelda);
        party.add(Ganon);

        WeakHealAll HA = new WeakHealAll();
        Zelda.addSkill(HA);

        double threatcount = Zelda.getSkills().get(1).use(Zelda, party, 0);
        Assert.assertEquals(threatcount, 0, 0);

        Link.setHealth(5);
        Zelda.setHealth(5);
        Ganon.setHealth(5);
        Zelda.setTechniquePoints(50);
        threatcount = Zelda.getSkills().get(1).use(Zelda, party, 0);
        Assert.assertEquals(threatcount, (HA.getDamage() * 3), 0);

        Link.setHealth((Link.getMaxHealth() - 5));
        Zelda.setHealth(Zelda.getMaxHealth() - 10);
        Ganon.setHealth(Ganon.getMaxHealth() - 10);
        Zelda.setTechniquePoints(50);
        threatcount = Zelda.getSkills().get(1).use(Zelda, party, 0);
        Assert.assertEquals(threatcount, 25, 0);

    }
}
