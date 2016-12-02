package net.sf.bloodball.model.test;

import net.sf.bloodball.model.player.Player;
import net.sf.bloodball.test.*;
import net.sf.bloodball.util.Dices.TestInterface;

public class GameTest extends ModelTest {
	private MockGame game;

	public GameTest(String name) {
		super(name);
	}

	public void setUp() {
		game = new MockGame();
		game.newBeginWithHomeTeam();
	}

	public void testInitialBallPosition() {
		assertTrue(game.getBall().getPosition() == null);
	}

	public void testInitialOffensiveGuestTeam() {
		game.newBeginWithGuestTeam();
		assertEquals(game.getTeams().getOffensiveTeam(), game.getTeams().getGuestTeam());
	}

	public void testInitialOffensiveHomeTeam() {
		game.newBeginWithHomeTeam();
		assertEquals(game.getTeams().getOffensiveTeam(), game.getTeams().getHomeTeam());
	}

	public void testOffensiveTeamAfterNewTurn() {
		game.newBeginWithHomeTeam();
		game.startNewTurn();
		assertSame(game.getTeams().getOffensiveTeam(), game.getTeams().getHomeTeam());
	}

}