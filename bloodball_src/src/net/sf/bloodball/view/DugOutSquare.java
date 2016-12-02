package net.sf.bloodball.view;

import java.awt.Color;
import net.sf.bloodball.model.player.Player;

public class DugOutSquare extends Square {

  public DugOutSquare() {
    super(Color.orange.darker().darker(), null);
  }

  public void setActivated(boolean activated) {
    if (activated) {
      setBrightColor();
    } else {
      setDefaultColor();
    }
  }

  protected void adjustSquareColor(Player player) {
  }
}