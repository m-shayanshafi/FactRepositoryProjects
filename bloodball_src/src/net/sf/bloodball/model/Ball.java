package net.sf.bloodball.model;

import de.vestrial.util.awt.Points;
import java.awt.Point;
import net.sf.bloodball.model.player.Player;
import net.sf.bloodball.util.Dices;

public class Ball {
  private Point position;
  private Field field;

  public Ball(Field field) {
    this.field = field;
  }

  private void assureNoPronePossession() {
    if (field.pronePlayerOn(position)) {
      scatter();
    }
  }

  public Point getPosition() {
    return position;
  }

  public Player getPossessingPlayer() {
    return field.getPlayer(position);
  }

  private static Point getScatterMove() {
    switch (Dices.D8.roll()) {
      case 1 :
        return new Point(1, 1);
      case 2 :
        return new Point(1, 0);
      case 3 :
        return new Point(1, -1);
      case 4 :
        return new Point(0, 1);
      case 5 :
        return new Point(0, -1);
      case 6 :
        return new Point(-1, 1);
      case 7 :
        return new Point(-1, 0);
      case 8 :
        return new Point(-1, -1);
    }
    return null;
  }

  private static Point getThrowInMove(Point position) {
    int diceResult = (Dices.D6.roll() - 1) / 2 - 1;
    return new Point(diceResult, position.y == 0 ? 1 : -1);
  }

  private void performMoveTo(Point newPosition) {
    if (Field.isInside(newPosition)) {
      setPosition(newPosition);
      assureNoPronePossession();
    } else {
      throwIn(position);
    }
  }

  public void remove() {
    if (position != null) {
      position = null;
    }
  }

  public void scatter() {
    performMoveTo(Points.add(position, getScatterMove()));
  }

  public void setPosition(Point newPosition) {
    Point oldPosition = position;
    position = newPosition;
    Notifier.fireSquareChangedEvent(newPosition);
    if (oldPosition != null) {
    	Notifier.fireSquareChangedEvent(oldPosition);
    }
  }

  public void throwIn(Point outPosition) {
    Point movePosition = getThrowInMove(outPosition);
    setPosition(Points.add(outPosition, movePosition));
    if (!Field.isInside(position)) {
      throwIn(outPosition);
    } else {
      assureNoPronePossession();
    }
  }
}