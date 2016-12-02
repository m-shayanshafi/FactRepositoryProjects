package net.sf.bloodball.view.test;

import junit.framework.Test;
import junit.framework.TestSuite;

public class AllTests {

  public static Test suite() {
    TestSuite suite = new TestSuite("Test for net.sf.bloodball.view.test");
    //$JUnit-BEGIN$
    suite.addTest(new TestSuite(GameBoardTest.class));
    //$JUnit-END$
    return suite;
  }
}
