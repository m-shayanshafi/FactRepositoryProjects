package net.sf.bloodball.test;

import net.sf.bloodball.model.*;

public class MockBall extends Ball {
  private boolean scattered;

  public MockBall(Field field) {
    super(field);
  }

  public boolean hasScattered() {
    return scattered;
  }

  public void scatter() {
    super.scatter();
    scattered = true;
  }
}