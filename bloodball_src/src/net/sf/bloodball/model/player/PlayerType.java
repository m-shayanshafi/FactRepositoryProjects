package net.sf.bloodball.model.player;

import java.awt.Color;
import java.util.*;

public class PlayerType {

  private static final List all = new ArrayList();
  public final static PlayerType NO_TYPE = new PlayerType(0, null, 0, 0, 0, 0, 0) {
    public String getDescription() {
      return null;
    }
  };
  public final static PlayerType LINEMAN = new PlayerType(6, "Lineman", 2, 3, 0, 0, 9);
  public final static PlayerType BLITZER = new PlayerType(2, "Blitzer", 3, 4, 0, 0, 9);
  public final static PlayerType BLOCKER = new PlayerType(4, "Blocker", 1, 4, -1, -1, 10);
  public final static PlayerType CATCHER = new PlayerType(2, "Catcher", 4, 2, 0, 1, 8);
  public final static PlayerType THROWER = new PlayerType(2, "Thrower", 3, 3, 1, 0, 8);

  private int defaultCount;
  private String title;
  private int sprintAllowance;
  private int armourValue;
  private int strength;
  private int throwingSkill;
  private int coolness;

  private PlayerType(int defaultCount, String title, int sp, int st, int ts, int cl, int av) {
    this.defaultCount = defaultCount;
    this.title = title;
    this.sprintAllowance = sp;
    this.strength = st;
    this.throwingSkill = ts;
    this.coolness = cl;
    this.armourValue = av;

    all.add(this);
  }

  public int getSprintAllowance() {
    return sprintAllowance;
  }

  public int getMoveAllowance() {
    return 4;
  }

  public int getDefaultCount() {
    return defaultCount;
  }

  public static Iterator getPlayerTypes() {
    return all.iterator();
  }

  public String getDescription() {
    return "<div align=center><STRONG>" + title + "</STRONG></div>" 
      + "<TABLE CELLSPACING=\"0\" CELLPADDING=\"0\" BORDER=\"1\" width=\"100%\">"
      + "<TR><TH><SMALL>MA</SMALL></TH>"
      + "<TH><SMALL>SP</SMALL></TH>"
      + "<TH><SMALL>ST</SMALL></TH>"
      + "<TH><SMALL>TS</SMALL></TH>"
      + "<TH><SMALL>CL</SMALL></TH>"
      + "<TH><SMALL>AV</SMALL></TH></TR>"
      + "<TR><TD ALIGN=\"center\"><SMALL>"
      + getMoveAllowance()
      + "</TD><TD ALIGN=\"center\"><SMALL>"
      + getSprintAllowance()
      + "</TD><TD ALIGN=\"center\"><SMALL>"
      + getStrength()
      + "</TD><TD ALIGN=\"center\"><SMALL>"
      + getThrowingSkill()
      + "</TD><TD ALIGN=\"center\"><SMALL>"
      + getCoolness()
      + "</TD><TD ALIGN=\"center\"><SMALL>"
      + getArmourValue()
      + "</SMALL></TD></TR></TABLE>";
  }

  public String toString() {
    return title;
  }

  public int getArmourValue() {
    return armourValue;
  }

  public int getStrength() {
    return strength;
  }

  public int getCoolness() {
    return coolness;
  }

  public int getThrowingSkill() {
    return throwingSkill;
  }

}