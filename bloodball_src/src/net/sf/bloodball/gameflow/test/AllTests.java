package net.sf.bloodball.gameflow.test;

import junit.framework.*;

public class AllTests {
  public AllTests() {
    super();
  }

  public static Test suite() {
    TestSuite suite = new TestSuite("model actionstates");
    suite.addTestSuite(GameFlowControllerTest.class);
    suite.addTestSuite(MoveActionStateTest.class);
    suite.addTestSuite(MoveSelectionStateTest.class);
    suite.addTestSuite(SetupBallStateTest.class);
    suite.addTestSuite(SetupPlayerStateTest.class);
    suite.addTestSuite(SetupTeamStateTest.class);
    suite.addTestSuite(SubstitutionStateTest.class);
    suite.addTestSuite(TurnBeginSelectionStateTest.class);
    return suite;
  }
}