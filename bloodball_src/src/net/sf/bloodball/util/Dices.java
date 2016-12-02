package net.sf.bloodball.util;

import de.vestrial.util.random.*;
import de.vestrial.util.random.Dice;

public class Dices {

  public static Dice D6 = new Dice(6, false);
  public static Dice D8 = new Dice(8, false);

  public static class TestInterface {
    public static void setD6(Dice dice) {
      D6 = dice;
    }

    public static void resetD6() {
      D6 = new Dice(6, false);
    }

    public static void setD8(Dice dice) {
      D8 = dice;
    }

    public static void resetD8() {
      D8 = new Dice(8, false);
    }

  }
}