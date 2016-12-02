package net.sf.bloodball.gameflow.test;

import java.awt.Point;
import net.sf.bloodball.gameflow.*;
import net.sf.bloodball.model.FieldExtents;
import net.sf.bloodball.model.Notifier;
import net.sf.bloodball.model.player.Player;

public class SetupPlayerStateTest extends ActionStateTest {

  private int playerNumber = 3;

  public SetupPlayerStateTest(String arg0) {
    super(arg0);
  }

  protected void assertTeamSetupState() {
    assertTrue(controller.getState() instanceof SetupTeamState);
  }

  protected void assertPlayerSetupState() {
    assertTrue(controller.getState() instanceof SetupPlayerState);
  }

  public State getActionState(GameFlowController context) {
    return new SetupPlayerState(context, getHomeTeam(), playerNumber);
  }

  public void testInvalidSetup() {
    controller.getState().squareChoosen(squareZeroFour);
    assertSame(Player.NO_PLAYER, getPlayerAt(squareZeroFour));
    assertPlayerSetupState();
  }

  public void testValidSetup() {
    Point legalSetupSquare = new Point(FieldExtents.HOME_SETUP_ZONE.getLowerBound(), 0);
    Player player = controller.getGame().getTeams().getHomeTeam().getPlayerByNumber(playerNumber);
    controller.getState().squareChoosen(legalSetupSquare);
    assertSame(player, getPlayerAt(legalSetupSquare));
    assertTeamSetupState();
  }
  
  protected void tearDown() throws Exception {
    super.tearDown();
    Notifier.setModelListener(null);
  }

}