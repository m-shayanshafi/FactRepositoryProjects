package tests;
import static org.junit.Assert.assertTrue;
import main.java.Game;
import main.java.Map;
import main.java.Player;

import org.junit.Test;

/**
 * @author metalmatze
 * 
 */
public class TestGame {

	@Test(timeout = 1000)
	public void testGame() {
		Game testGame = new Game();
		assertTrue(testGame.getMap() != null);
		assertTrue(testGame.getPlayer() != null);

		Map testMap = new Map();

		// Test if returns the right tiles
		assertTrue(testMap.getMap(0, 0) == 1);
		assertTrue(testMap.getMap(8, 8) == 2);

		Player testPlayer = new Player();
		assertTrue(testPlayer.getPlayer() != null);
		assertTrue(testPlayer.getX() == 9);
		assertTrue(testPlayer.getY() == 9);
		testPlayer.move(1, 2);
		assertTrue(testPlayer.getX() == 10);
		assertTrue(testPlayer.getY() == 11);

		// move player against wall
		testPlayer.move(-7, -10);
		assertTrue(testPlayer.getX() == 5);
		assertTrue(testPlayer.getY() == 5);

		testPlayer.move(-100, -100);
		assertTrue(testPlayer.getX() == 5);
		assertTrue(testPlayer.getY() == 5);
	}
}