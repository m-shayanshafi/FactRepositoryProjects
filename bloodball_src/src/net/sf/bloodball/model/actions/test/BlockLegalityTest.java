package net.sf.bloodball.model.actions.test;

import java.awt.Point;
import net.sf.bloodball.model.player.Player;

public class BlockLegalityTest extends ActionTest {
	
	public BlockLegalityTest(String name) {
		super(name);
	}
	
	public void testIsLegalToBlockEmtpyField() {
		setPlayerTo(getHomeTeamPlayer(), squareZeroOne);
		assertTrue(!block.isLegal(squareZeroOne, squareZeroTwo));
	}
	
	public void testFarOpponentPlayerNotBlockable() {
		setPlayerTo(getHomeTeamPlayer(), squareZeroOne);
		setPlayerTo(getGuestTeamPlayer(), squareZeroThree);
		assertTrue(!block.isLegal(squareZeroOne, squareZeroThree));
	}
	
	public void testMayBlockWithOpponentNeighbor() throws Exception {
		Player blocker = setPlayerTo(getHomeTeamPlayer(), squareZeroOne);
		setPlayerTo(getGuestTeamPlayer(), squareZeroTwo);
		assertTrue(block.mayBlock(blocker));
	}
	
	public void testNeighborOpponentPlayerBlockable() {
		setPlayerTo(getHomeTeamPlayer(), squareZeroOne);
		setPlayerTo(getGuestTeamPlayer(), squareZeroTwo);
		assertTrue(block.isLegal(squareZeroOne, squareZeroTwo));
	}
	
	public void testSelfNotBlockable() {
		setPlayerTo(getGuestTeamPlayer(), squareZeroOne);
		assertTrue(!block.isLegal(squareZeroOne, squareZeroOne));
	}

}
