package Test.Skills;
import Actor.Actor;
import Actor.Hero;
import FightClasses.Knight;
import FightClasses.Wizard;
import Items.Sword;
import Skills.DoubleStrike;
import org.junit.*;

import java.util.Vector;

/**
 * Created by piano_000 on 9/4/2015.
 */
public class DoubleStrikeTest {

    @Test
    public void TestInit(){
        DoubleStrike ds = new DoubleStrike();
        Assert.assertNotNull(ds);
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

        double threatcount = Link.getSkills().get(0).use(Link, party, 1);
        Assert.assertNotNull(threatcount);
    }
}
