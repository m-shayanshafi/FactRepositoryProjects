package net.sf.bloodball.model.player.test;

import net.sf.bloodball.model.*;
import net.sf.bloodball.model.player.*;
import net.sf.bloodball.test.*;
import java.awt.*;

public class TeamTest extends ModelTest {

  private static final int TEAM_SIZE = 16;

  public TeamTest(String name) {
    super(name);
  }

  private void assureTeamMembershipOfPlayersInReserve(Team team) {
    for (int i = 0; i < team.getPlayersInReserveCount(); i++) {
      Player player = team.getPlayerByNumber(i);
      assertTrue(team.isMember(player));
    }
  }

  private void setAndCheckColor(Color teamColor) {
    Team team = new Team(getGame(), "Test", teamColor);
    assertEquals(teamColor, team.getColor());
  }

  protected void setUp() throws Exception {
    super.setUp();
  }

  public void testColor() {
    setAndCheckColor(Color.red);
  }

  public void testGuestTeamPlayersInReserve() {
    assureTeamMembershipOfPlayersInReserve(getGuestTeam());
  }

  public void testHomeTeamPlayersInReserve() {
    assureTeamMembershipOfPlayersInReserve(getHomeTeam());
  }

  public void testInitialGuestTeamPlayerCount() {
    assertEquals(TEAM_SIZE, getGuestTeam().getPlayersInReserveCount());
  }

  public void testInitialHomeTeamPlayerCount() {
    assertEquals(TEAM_SIZE, getHomeTeam().getPlayersInReserveCount());
  }

  public void testInitialTouchdowns() {
    assertEquals(0, getHomeTeam().getTouchdownsScored());
  }

  public void testIsHomeTeam() {
    assertTrue(getTeams().getHomeTeam().isHomeTeam());
    assertTrue(!getTeams().getGuestTeam().isHomeTeam());
    assertTrue(!Team.NO_TEAM.isHomeTeam());
    assertTrue(!new Team(getGame(), "test", Color.black).isHomeTeam());
  }

  public void testOtherColor() {
    setAndCheckColor(Color.blue);
  }

  public void testPlayerInReserveReduction() {
    setPlayerTo(getHomeTeam().getPlayerFromReserve(), squareOneOne);
    assertEquals(TEAM_SIZE - 1, getHomeTeam().getPlayersInReserveCount());
  }

  public void testPlayerSetupCount() {
    assertEquals(11, getHomeTeam().getPlayersToSetupCount());
    setPlayerTo(getHomeTeamPlayer(), squareOneOne);
    assertEquals(10, getHomeTeam().getPlayersToSetupCount());
  }

  public void testPlayerSetupCountWithInjured() {
    for (int i = 0; i < 6; i++) {
      placeAndInjure(getHomeTeamPlayer());
    }
    assertEquals(10, getHomeTeam().getPlayersToSetupCount());
  }

}