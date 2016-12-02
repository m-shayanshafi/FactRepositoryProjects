package Test.FightClasses;

import FightClasses.BanditKing;
import Skills.TripleStrike;
import org.junit.*;

/**
 * Created by piano_000 on 9/4/2015.
 */
public class BanditKingTest {

    @Test
    public void TestInit(){
        BanditKing k = new BanditKing();
        Assert.assertNotNull(k);
    }

    @Test
    public void TestGetName(){
        BanditKing k = new BanditKing();
        Assert.assertEquals(k.getClassName(), "Bandit King");
    }

    @Test
    public void TestGetHealth(){
        BanditKing k = new BanditKing();
        Assert.assertNotNull(k.getHealth());
    }

    @Test
    public void TestGetDmgMod(){
        BanditKing k = new BanditKing();
        Assert.assertNotNull(k.getdamageMod());
    }

    @Test
    public void TestGetTechniquePoints(){
        BanditKing k = new BanditKing();
        Assert.assertNotNull(k.getTechniquePoints());
    }

    @Test
    public void TestGetSkills(){
        BanditKing k = new BanditKing();

        Assert.assertTrue(k.getSkill() instanceof TripleStrike);
    }
}
