package net.sf.bloodball.event.test;

import junit.framework.Test;
import junit.framework.TestSuite;

public class AllTests {

  public static Test suite() {
    TestSuite suite = new TestSuite("Test for net.sf.bloodball.event.test");
    //$JUnit-BEGIN$
    suite.addTest(new TestSuite(SquareContentChangedTest.class));
    //$JUnit-END$
    return suite;
  }
}
