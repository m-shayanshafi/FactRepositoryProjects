package Test.AI;

import Actor.*;
import CombatAI.AI;
import FightClasses.BanditKing;
import FightClasses.Knight;
import FightClasses.Wizard;
import Items.Bow;
import Items.MagicPotion;
import Items.RedHerb;
import Items.Sword;
import Skills.FireBreath;
import Skills.Heal;
import Skills.WeakHealAll;
import org.junit.*;

import java.util.Vector;

/**
 * Created by piano_000 on 9/18/2015.
 */

public class AITest {


    Vector<Actor> enemies = new Vector<>();
    Vector<Actor> party = new Vector<>();
    Hero Link;
    Hero Zelda;
    Hero Ganon;

    Guard g1;
    Goblin g2;
    Dragon g3;

    Sword woodenSword = new Sword("Wooden Sword", "Just a sword made of wood", false);
    Bow woodenBow = new Bow("Wooden Bow", "A nice wooden bow", true);

    @Before
    public void setup() {
        Link = new Hero("Link", new Knight());
        Zelda = new Hero("Zelda", new Wizard());
        Ganon = new Hero("Ganon", new BanditKing());



        Link.setWeapon(woodenSword);
        Zelda.setWeapon(woodenBow);
        Ganon.setWeapon(woodenSword);

        Link.addSkill(new FireBreath());
        Zelda.addSkill(new WeakHealAll());


        party.add(Link);
        party.add(Zelda);
        party.add(Ganon);

        g1 = new Guard(); enemies.add(g1);
        g2 = new Goblin(); enemies.add(g2);
        g3 = new Dragon(); enemies.add(g3);
    }

    @After
    public void cleanup() {
        party.removeAllElements();
        enemies.removeAllElements();
    }

    @Test
    public void TestSeekWeakest() {
        AI ai = new AI(party, enemies);
        Assert.assertSame(Zelda, party.get(ai.seekWeakest()));
    }

    @Test
    public void TestSeekStrongest() {
        AI ai = new AI(party, enemies);
        Assert.assertSame(Ganon, party.get(ai.seekStrongest()));
    }

    @Test
    public void TestSeekFirst() {
        AI ai = new AI(party, enemies);
        Assert.assertSame(Link, party.get(ai.seekFirstHero()));
    }

    @Test
    public void TestSeekWeakestAlly() {
        AI ai = new AI(party, enemies);
        g3.setHealth(3);
        Assert.assertSame(g3, enemies.get(ai.getWeakestAlly()));
        g1.setHealth(1);
        Assert.assertSame(g1, enemies.get(ai.getWeakestAlly()));
    }

    @Test
    public void TestHealWounded() {
        enemies.get(0).addItem(new RedHerb());
        g3.setHealth(4);
        AI ai = new AI(party, enemies);
        ai.setCharacterTurn(0);
        Assert.assertEquals(g3.getHealth(), 4, 0);
        ai.healWounded();
        RedHerb red = new RedHerb();
        Assert.assertEquals(g3.getHealth(), (4 + red.getValue()), 0);

        g3.setHealth(10);
        enemies.get(0).addSkill(new Heal());
        enemies.get(0).setTechniquePoints(100);
        Assert.assertEquals(g3.getHealth(), 10, 0);
        ai.healWounded();
        Heal heal = new Heal();
        Assert.assertEquals(g3.getHealth(), (10 + heal.getDamage()), 0);

    }

    @Test
    public void TestRestoreTP() {
        enemies.get(0).addItem(new MagicPotion());
        g3.setTechniquePoints(0);
        AI ai = new AI(party, enemies);
        ai.setCharacterTurn(0);
        Assert.assertEquals(g3.getTechniquePoints(), 0, 0);
        ai.restoreTP();
        MagicPotion mp = new MagicPotion();
        Assert.assertEquals(g3.getTechniquePoints(), 25, 0);
    }


}
