package net.sf.bloodball.model.actions.test;

import junit.framework.*;

public class AllTests {

	public AllTests() {
		super();
	}
	
	public static Test suite() {
		TestSuite suite = new TestSuite("model actions");
		suite.addTestSuite(BlockLegalityTest.class);
		suite.addTestSuite(BlockPerformanceTest.class);
		suite.addTestSuite(HandOffTest.class);
		suite.addTestSuite(MoveLegalityTest.class);
		suite.addTestSuite(MovePerformanceTest.class);
		suite.addTestSuite(TackleTest.class);
		suite.addTestSuite(ThrowLegalityTest.class);
		suite.addTestSuite(ThrowPerformanceTest.class);
		return suite;
	}
}
