package net.sf.bloodball.model.actions.test;

import net.sf.bloodball.model.player.Player;

public class TackleTest extends ActionTest {
	
	public static void main(String[] args) {
		new junit.swingui.TestRunner().run(TackleTest.class);
	}

	public TackleTest(String name) {
		super(name);
	}
	
	private void setFailingTackleDice() {
		setD6Result(4);
	}
	
	private void setSucceedingTackleDice() {
		setD6Result(5);
	}
	
	public void testFailedTackle() {
		setFailingTackleDice();
		Player actor = setPlayerTo(getHomeTeamPlayer(), squareOneTwo);
		setPlayerTo(getGuestTeamPlayer(), squareZeroOne);
		moveAction.perform(squareZeroOne, squareOneOne);
		assertTrue(!actor.isProne());
	}
	
	public void testMoveIntoTackleZone() {
		setSucceedingTackleDice();
		Player actor = setPlayerTo(getHomeTeamPlayer(), squareTwoTwo);
		setPlayerTo(getGuestTeamPlayer(), squareZeroTwo);
		moveAction.perform(squareTwoTwo, squareOneTwo);
		assertTrue(!actor.isProne());
	}
	
	public void testMoveNextToProneOpponent() {
		setSucceedingTackleDice();
		setPlayerTo(getHomeTeamPlayer(), squareOneTwo);
		setPlayerTo(getGuestTeamPlayer(), squareZeroOne);
		setPlayerTo(getGuestTeamPlayer(), squareZeroFour);
		getPlayerAt(squareZeroFour).knockOver();
		moveAction.perform(squareOneTwo, squareOneThree);
		assertTrue(!getPlayerAt(squareOneThree).isProne());
	}
	
	public void testMoveOutOffTackleZone() {
		setPlayerTo(getGuestTeamPlayer(), squareZeroOne);
		setPlayerTo(getHomeTeamPlayer(), squareOneTwo);
		setSucceedingTackleDice();
		moveAction.perform(squareOneTwo, squareTwoTwo);
		assertTrue(!getPlayerAt(squareTwoTwo).isProne());
	}
	
	public void testNoTackleWithSameTeam() {
		setSucceedingTackleDice();
		Player actor = setPlayerTo(getHomeTeamPlayer(), squareOneTwo);
		setPlayerTo(getHomeTeamPlayer(), squareZeroOne);
		moveAction.perform(squareZeroOne, squareOneOne);
		assertTrue(!actor.isProne());
	}
	
	public void testSuccessfulTackle() {
		setSucceedingTackleDice();
		Player actor = setPlayerWithBallTo(getHomeTeamPlayer(), squareOneTwo);
		setPlayerTo(getGuestTeamPlayer(), squareOneOne);
		moveAction.perform(squareOneTwo, squareTwoTwo);
		assertTrue(actor.isProne());
		assertTrue(getBallPosition() != squareTwoTwo);
	}
	
}
