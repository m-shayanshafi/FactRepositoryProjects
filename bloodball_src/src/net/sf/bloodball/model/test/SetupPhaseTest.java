package net.sf.bloodball.model.test;

import java.awt.Point;
import net.sf.bloodball.model.*;
import net.sf.bloodball.model.actions.*;
import net.sf.bloodball.test.ModelTest;

public class SetupPhaseTest extends ModelTest {

  private Setup setupPhase;
  private Point homeSetupZoneSquare;
  private Point guestSetupZoneSquare;

  public SetupPhaseTest(String name) {
    super(name);
  }

  protected void setUp() throws Exception {
    super.setUp();
    setupPhase = new Setup(getGame());
    guestSetupZoneSquare = new Point(FieldExtents.GUEST_SETUP_ZONE.getLowerBound(), 0);
    homeSetupZoneSquare = new Point(FieldExtents.HOME_SETUP_ZONE.getLowerBound(), 0);
  }

  private void setupHomePlayerToHomeSetupZoneSquare() {
    setPlayerTo(getHomeTeamPlayer(), homeSetupZoneSquare);
  }

  public void testBallPositionAfterSetup() {
    setPlayerTo(getHomeTeamPlayer(), homeSetupZoneSquare);
    setupPhase.setupBall(homeSetupZoneSquare);
    assertEquals(homeSetupZoneSquare, getBallPosition());
  }

  public void testIllegalBallSetup() {
    try {
      setupPhase.setupBall(guestSetupZoneSquare);
      fail();
    } catch (IllegalArgumentException expected) {
    }
  }

  public void testLegalityOfZones() {
    assertTrue(setupPhase.isLegalPlayerSetup(homeSetupZoneSquare));
    assertTrue(!setupPhase.isLegalPlayerSetup(guestSetupZoneSquare));
    beginGameWithGuestTeam();
    assertTrue(setupPhase.isLegalPlayerSetup(guestSetupZoneSquare));
    assertTrue(!setupPhase.isLegalPlayerSetup(homeSetupZoneSquare));
  }

  public void testLegalityOfNonemptySquare() {
    setupHomePlayerToHomeSetupZoneSquare();
    assertTrue(!setupPhase.isLegalPlayerSetup(homeSetupZoneSquare));
  }

  public void testIsLegalBallSetupToBeginningGuestPlayerSquare() {
    beginGameWithGuestTeam();
    setPlayerTo(getGuestTeamPlayer(), guestSetupZoneSquare);
    assertTrue(setupPhase.isLegalBallSetup(guestSetupZoneSquare));
  }

  public void testIsLegalBallSetupToBeginningHomePlayerSquare() {
    setupHomePlayerToHomeSetupZoneSquare();
    assertTrue(setupPhase.isLegalBallSetup(homeSetupZoneSquare));
  }

  public void testIsLegalBallSetupToEmptySquare() {
    assertTrue(!setupPhase.isLegalBallSetup(homeSetupZoneSquare));
  }

  public void testIsLegalBallSetupToNonBeginningGuestPlayerSquare() {
    setPlayerTo(getGuestTeamPlayer(), guestSetupZoneSquare);
    assertTrue(!setupPhase.isLegalBallSetup(guestSetupZoneSquare));
  }

  public void testIsLegalBallSetupToNonBeginningHomePlayerSquare() {
    beginGameWithGuestTeam();
    setPlayerTo(getHomeTeamPlayer(), homeSetupZoneSquare);
    assertTrue(!setupPhase.isLegalBallSetup(homeSetupZoneSquare));
  }

  public void testIsLegalGuestPlayerSetup() {
    beginGameWithGuestTeam();
    assertTrue(setupPhase.isLegalPlayerSetup(guestSetupZoneSquare));
  }

  public void testIsLegalHomePlayerSetup() {
    assertTrue(setupPhase.isLegalPlayerSetup(homeSetupZoneSquare));
  }

  public void testRemovePlayer() {
    assertTrue(!setupPhase.removablePlayerAt(homeSetupZoneSquare));
    setupHomePlayerToHomeSetupZoneSquare();
    assertTrue(setupPhase.removablePlayerAt(homeSetupZoneSquare));
    setupPhase.removePlayer(homeSetupZoneSquare);
    setPlayerTo(getGuestTeamPlayer(), homeSetupZoneSquare);
    assertTrue(!setupPhase.removablePlayerAt(homeSetupZoneSquare));
  }

}