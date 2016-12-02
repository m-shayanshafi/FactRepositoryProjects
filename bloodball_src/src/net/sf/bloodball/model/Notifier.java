package net.sf.bloodball.model;

import java.awt.Point;
import net.sf.bloodball.model.player.Team;

public class Notifier {
  static private ModelListener modelListener;

  private Notifier() {
  }

  public static void fireEndTurnOperationChangedEvent() {
    if (modelListener != null) {
      modelListener.endTurnOperationChanged();
    }
  }

  public static void fireTouchdownScoredEvent() {
    if (modelListener != null) {
      modelListener.touchdownScored();
    }
  }

  public static void fireInTurnOperationChangedEvent() {
    if (modelListener != null) {
      modelListener.inTurnOperationChanged();
    }
  }

  public static void fireSquareChangedEvent(Point position) {
    if (modelListener != null) {
      modelListener.squareContentChanged(position);
    }
  }

  public static void fireDugOutChangedEvent(Team team, int playerNumber) {
    if (modelListener != null) {
      modelListener.dugOutContentChanged(team, playerNumber);
    }
  }

  public static void fireGameEndEvent() {
    if (modelListener != null) {
      modelListener.gameEnded();
    }
  }

  public static void fireDugOutActivatedEvent(Team team, int playerNumber) {
    if (modelListener != null) {
      modelListener.dugOutPositionActivated(team, playerNumber);
    }
  }

  public static void fireDugOutDeactivatedEvent(Team team, int playerNumber) {
    if (modelListener != null) {
      modelListener.dugOutPositionDeactivated(team, playerNumber);
    }
  }

  public static void setModelListener(ModelListener listener) {
    modelListener = listener;
  }
}