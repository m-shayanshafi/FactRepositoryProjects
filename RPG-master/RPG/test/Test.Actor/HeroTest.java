package Test.Actor;

import Actor.Hero;
import FightClasses.BanditKing;
import org.junit.*;

/**
 * Created by piano_000 on 9/4/2015.
 */
public class HeroTest {

    Hero bk = null;

    @Before
    public void setup() {
        bk = new Hero("Test", new BanditKing());
    }


    @Test
    public void Testinit() {
        Assert.assertNotNull(bk);
    }

    @Test
    public void TestGetName() {
        Assert.assertEquals(bk.getName(), "Test");
    }

    @Test
    public void TestGetHealth() {
        BanditKing bandit = new BanditKing();
        Assert.assertEquals(bk.getHealth(), bandit.getHealth(), 0.0);
        Assert.assertEquals(bk.getMaxHealth(), bandit.getHealth(), 0.0);

        bk.setHealth(100);
        Assert.assertEquals(bk.getHealth(), 100.0, 0.0);
        bk.setMaxHealth(200);
        Assert.assertEquals(bk.getMaxHealth(), 200.0, 0.0);
    }
}
