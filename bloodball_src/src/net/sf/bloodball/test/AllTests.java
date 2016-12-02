package net.sf.bloodball.test;

import junit.framework.*;

public class AllTests {
  public static void main(String[] args) {
    new junit.swingui.TestRunner().run(AllTests.class);
  }

  public static Test suite() {
    TestSuite suite = new TestSuite("blood ball");
    suite.addTest(new TestSuite(GameControllerTest.class));
    suite.addTest(net.sf.bloodball.event.test.AllTests.suite());
    suite.addTest(net.sf.bloodball.model.test.AllTests.suite());
    suite.addTest(net.sf.bloodball.gameflow.test.AllTests.suite());
    suite.addTest(net.sf.bloodball.view.test.AllTests.suite());
    return suite;
  }
}