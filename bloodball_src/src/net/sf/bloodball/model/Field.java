package net.sf.bloodball.model;

import de.vestrial.util.*;
import java.awt.Point;
import net.sf.bloodball.model.player.*;

public class Field {
  private Player[][] squares = new Player[FieldExtents.SIZE.width][FieldExtents.SIZE.height];

  public Field() {
    resetSquares();
  }

  private Range getEndZone(Team team) {
    return team.isHomeTeam() ? FieldExtents.HOME_END_ZONE : FieldExtents.GUEST_END_ZONE;
  }

  public RangedArea getSubstitutArea(Team team) {
    return team.isHomeTeam() ? FieldExtents.HOME_SUBSTITUTE_AREA : FieldExtents.GUEST_SUBSTITUTE_AREA;
  }

  public Player getPlayer(Point position) {
    return squares[position.x][position.y];
  }

  public Point getPlayerPosition(Player player) {
    for (int x = 0; x < squares.length; x++) {
      for (int y = 0; y < squares[x].length; y++) {
        if (squares[x][y] == player) {
          return new Point(x, y);
        }
      }
    }
    return null;
  }

  private Range getSetupZone(Team team) {
    return team.isHomeTeam() ? FieldExtents.HOME_SETUP_ZONE : FieldExtents.GUEST_SETUP_ZONE;
  }

  public boolean inEndZone(Point position, Team team) {
    return team == Team.NO_TEAM ? false : inZone(position, getEndZone(team));
  }

  public boolean inSetupZone(Point position, Team team) {
    return team == Team.NO_TEAM ? false : inZone(position, getSetupZone(team));
  }

  private static boolean inZone(Point position, Range zone) {
    return FieldExtents.VERTICAL_EXTENTS.contains(position.y) && zone.contains(position.x);
  }

  public static boolean isInside(Point position) {
    return position.x < FieldExtents.SIZE.width && position.x >= 0 && position.y < FieldExtents.SIZE.height && position.y >= 0;
  }

  public static boolean neighborSquares(Point first, Point second) {
    return (neighborValues(first.x, second.x) && neighborValues(first.y, second.y));
  }

  public boolean neighborPlayers(Player first, Player second) {
    if (!first.isOnField() || !second.isOnField()) {
      return false;
    }
    return neighborSquares(getPlayerPosition(first), getPlayerPosition(second));
  }

  private static boolean neighborValues(int first, int second) {
    return Math.abs(first - second) <= 1;
  }

  public boolean pronePlayerOn(Point position) {
    return getPlayer(position).isProne();
  }

  public void removePlayer(Point position) {
    squares[position.x][position.y] = Player.NO_PLAYER;
  }

  public void resetSquares() {
    for (int i = 0; i < FieldExtents.SIZE.width; i++) {
      for (int j = 0; j < FieldExtents.SIZE.height; j++) {
        squares[i][j] = Player.NO_PLAYER;
      }
    }
  }

  public void setPlayer(Point position, Player player) {
    squares[position.x][position.y] = player;
  }

  public boolean inTackleZone(Point position, Team team) {
    return playersInTackleZoneCount(position, team) > 0;
  }

  public int playersInTackleZoneCount(Point position, Team team) {
    if (!isInside(position)) {
      return 0;
    }
    int counter = 0;
    for (int dx = -1; dx <= 1; dx++) {
      for (int dy = -1; dy <= 1; dy++) {
        Point neighborPosition = new Point(position.x + dx, position.y + dy);
        if (neighborPosition.equals(position)) {
          continue;
        }
        if (Field.isInside(neighborPosition)) {
          Player player = getPlayer(neighborPosition);
          if (player.getTeam() == team && !player.isProne()) {
            counter++;
          }
        }
      }
    }
    return counter;
  }
}