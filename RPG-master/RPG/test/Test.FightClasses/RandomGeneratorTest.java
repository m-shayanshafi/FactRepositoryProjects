package Test.FightClasses;

import FightClasses.RandomGenerator;
import org.junit.*;

/**
 * Created by piano_000 on 9/3/2015.
 */
public class RandomGeneratorTest {
    RandomGenerator rg = new RandomGenerator();

    @Test
    public void Testinit() {
        Assert.assertNotNull(rg);
    }

    @Test
    public void TestBattleRandom() {
        int value = rg.battleRandom();
        if (value >= 0 && value < 2) {
            Assert.assertTrue(true);
        } else {
            Assert.assertTrue(false);
        }
    }

    @Test
    public void TestgetNumberBetween() {
        int value1 = rg.getNumberBetween(3, 5);
        if (value1 >= 3 && value1 <= 5) {
            Assert.assertTrue(true);
        } else {
            Assert.assertTrue(false);
        }

        int value2 = rg.getNumberBetween(5, 5);
        Assert.assertEquals(value2, 5);
    }
}

