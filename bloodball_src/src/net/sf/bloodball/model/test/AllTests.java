package net.sf.bloodball.model.test;

import junit.framework.*;

public class AllTests {

  public static void main(String[] args) {
    new junit.swingui.TestRunner().run(AllTests.class);
  }

  public static Test suite() {
    TestSuite suite = new TestSuite("model tests");
    suite.addTestSuite(BallTest.class);
    suite.addTestSuite(FieldTest.class);
    suite.addTestSuite(FieldExtentsTest.class);
    suite.addTestSuite(GameTest.class);
    suite.addTestSuite(SetupPhaseTest.class);
    suite.addTestSuite(TeamsTest.class);
    suite.addTest(net.sf.bloodball.model.actions.test.AllTests.suite());
    suite.addTest(net.sf.bloodball.model.player.test.AllTests.suite());
    return suite;
  }
}