package Test.Game;
import Actor.Actor;
import Actor.Hero;
import FightClasses.BanditKing;
import FightClasses.Knight;
import FightClasses.Wizard;
import Game.ThreatAnalyzer;
import Items.Bow;
import Items.Sword;
import org.junit.*;

import java.util.Vector;

/**
 * Created by piano_000 on 9/4/2015.
 */
public class ThreatAnalyzerTest {

    ThreatAnalyzer ta = null;
    Hero Link = null;
    Hero Zelda = null;
    Hero Ganon = null;
    Vector<Actor> party = null;

    @Before
    public void setup() {
        Link = new Hero("Link", new Knight());
        Zelda = new Hero("Zelda", new Wizard());
        Ganon = new Hero("Ganon", new BanditKing());

        Sword woodenSword = new Sword("Wooden Sword", "Just a sword made of wood", false);
        Bow woodenBow = new Bow("Wooden Bow", "A nice wooden bow", true);

        Link.setWeapon(woodenSword);
        Zelda.setWeapon(woodenBow);
        Ganon.setWeapon(woodenSword);

        party = new Vector<>();
        party.add(Link);
        party.add(Zelda);
        party.add(Ganon);
    }

    @After
    public void teardown() {
        party.removeAllElements();
    }

    @Test
    public void TestInit(){
        ta = new ThreatAnalyzer(party);
        Assert.assertNotNull(ta);
        Assert.assertEquals(ta.getGreatestSource(), -1, 0);
    }

    @Test
    public void TestFindThreat() {
        ta = new ThreatAnalyzer(party);
        ta.addThreat(0, 10.0);
        ta.addThreat(0, 15.0);
        ta.addThreat(1, 31.0);
        ta.addThreat(2, 15.0);

        ta.determineThreat();
        Assert.assertEquals(ta.getGreatestSource(), 1, 0);
    }
}
