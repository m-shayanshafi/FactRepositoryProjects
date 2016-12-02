package net.sf.bloodball.model.test;

import java.awt.Point;
import net.sf.bloodball.model.*;
import net.sf.bloodball.model.player.Player;
import net.sf.bloodball.test.ModelTest;
import net.sf.bloodball.util.Dices;

public class FieldTest extends ModelTest {
  private Point homeSetupZoneSquare;
  private Point guestSetupZoneSquare;
  private Point homeEndZoneSquare;
  private Point guestEndZoneSquare;

  public FieldTest(String name) {
    super(name);
  }

  private void assertPositionNotInField(Point square) {
    assertTrue(!getGame().getField().isInside(square));
  }

  protected void setUp() throws Exception {
    super.setUp();
    homeSetupZoneSquare = new Point(FieldExtents.HOME_SETUP_ZONE.getLowerBound(), 0);
    guestSetupZoneSquare = new Point(FieldExtents.GUEST_SETUP_ZONE.getLowerBound(), 0);
    homeEndZoneSquare = new Point(FieldExtents.HOME_END_ZONE.getLowerBound(), 0);
    guestEndZoneSquare = new Point(FieldExtents.GUEST_END_ZONE.getLowerBound(), 0);
  }

  public void testEmptySquareContent() throws Exception {
    assertSame(Player.NO_PLAYER, getGame().getField().getPlayer(squareOneOne));
  }

  public void testHorizontalLeftFieldBoundary() {
    assertPositionNotInField(new Point(-1, 1));
  }

  public void testHorizontalRightFieldBoundary() throws Exception {
    assertPositionNotInField(new Point(FieldExtents.SIZE.width, 1));
  }

  public void testInGuestEndZone() {
    assertTrue(getGame().getField().inEndZone(guestEndZoneSquare, getGuestTeam()));
  }

  public void testInGuestSetupZone() {
    assertTrue(getGame().getField().inSetupZone(guestSetupZoneSquare, getGuestTeam()));
  }

  public void testInHomeEndZone() {
    assertTrue(getGame().getField().inEndZone(homeEndZoneSquare, getHomeTeam()));
  }

  public void testInHomeSetupZone() {
    assertTrue(getGame().getField().inSetupZone(homeSetupZoneSquare, getHomeTeam()));
  }

  public void testInsideFieldBoundaries() {
    assertTrue(getGame().getField().isInside(new Point(0, 0)));
  }

  public void testPlayerSquareContent() throws Exception {
    Player player = getHomeTeamPlayer();
    getGame().getField().setPlayer(this.squareOneOne, player);
    assertSame(player, getGame().getField().getPlayer(this.squareOneOne));
  }

  public void testResetSquares() {
    getGame().getField().setPlayer(new Point(1, 1), getHomeTeamPlayer());
    getGame().getField().resetSquares();
    for (int x = 0; x < FieldExtents.SIZE.width; x++) {
      for (int y = 0; y < FieldExtents.SIZE.height; y++) {
        final Point position = new Point(x, y);
        assertSame("Must be NoPlayer at " + position, getGame().getField().getPlayer(position), Player.NO_PLAYER);
      }
    }
  }

  public void testVerticalBottomFieldBoundary() {
    assertPositionNotInField(new Point(1, FieldExtents.SIZE.height));
  }

  public void testVerticalTopFieldBoundary() {
    assertPositionNotInField(new Point(-1, 1));
  }

  public void testPronePlayersTacklingZone() throws Exception {
    setD6Results(1, 1);
    Player player = setPlayerTo(getGuestTeamPlayer(), squareOneOne);
    player.knockOver();
    assertEquals(0, getGame().getField().playersInTackleZoneCount(squareZeroOne, getGame().getTeams().getGuestTeam()));
  }

}