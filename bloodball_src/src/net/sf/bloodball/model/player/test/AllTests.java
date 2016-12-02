package net.sf.bloodball.model.player.test;

import junit.framework.*;

public class AllTests {

	public AllTests() {
		super();
	}

	public static Test suite() {
		TestSuite suite = new TestSuite("model player");
		suite.addTestSuite(PlayerTest.class);
		suite.addTestSuite(TeamTest.class);
		return suite;
	}
}