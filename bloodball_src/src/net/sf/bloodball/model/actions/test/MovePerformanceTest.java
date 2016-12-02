package net.sf.bloodball.model.actions.test;

import net.sf.bloodball.model.player.Player;

public class MovePerformanceTest extends ActionTest {
	
	public MovePerformanceTest(String name) {
		super(name);
	}
	
	public void testBallMovesWithPossessor() {
		Player actor = setPlayerWithBallTo(getHomeTeamPlayer(), squareZeroOne);
		moveAction.perform(squareZeroOne, squareZeroTwo);
		assertEquals(squareZeroTwo, getBallPosition());
		assertTrue(actor.inBallPossession());
	}
	
	public void testMoveToIllegalSquare() {
		setPlayerTo(getHomeTeamPlayer(), squareZeroOne);
		try {
			moveAction.perform(squareZeroOne, squareZeroThree);
			fail();
		} catch (IllegalStateException expected) {
		}
	}
	
	public void testPlayerPositionChangesWithLegalMove() {
		Player actor = setPlayerTo(getHomeTeamPlayer(), squareZeroOne);
		moveAction.perform(squareZeroOne, squareZeroTwo);
		assertSame(getLegalPlayerAt(squareZeroTwo), actor);
	}
	
	public void testPlayerTurnEndedAfterMoving() {
		Player homePlayer = getHomeTeamPlayer();
		setPlayerTo(homePlayer, squareZeroOne);
		moveAction.perform(squareZeroOne, squareZeroTwo);
		homePlayer.endTurn();
		assertTrue(!homePlayer.isAtCall());
	}
}