package net.sf.bloodball.model.test;

import net.sf.bloodball.model.Teams;
import net.sf.bloodball.model.player.Team;
import net.sf.bloodball.test.ModelTest;

public class TeamsTest extends ModelTest {
  Teams teams;

  public TeamsTest(String name) {
    super(name);
  }

  protected void setUp() throws Exception {
    super.setUp();
    this.teams = getGame().getTeams();
  }

  public void testGuestScoreTouchdown() {
    teams.scoreTouchdown(teams.getGuestTeam());
    assertEquals(1, getGuestTeam().getTouchdownsScored());
  }

  public void testGuestTeamOpponent() {
    assertSame(teams.getHomeTeam(), teams.getOpponentTeam(teams.getGuestTeam()));
  }

  public void testHomeScoreTouchdown() {
    teams.scoreTouchdown(teams.getHomeTeam());
    assertEquals(1, getHomeTeam().getTouchdownsScored());
  }

  public void testHomeTeamOpponent() {
    assertSame(teams.getGuestTeam(), teams.getOpponentTeam(teams.getHomeTeam()));
  }

  public void testInactiveHomeTeamAfterNewTurn() {
    teams.startNewTurn();
    assertEquals(teams.getInactiveTeam(), teams.getHomeTeam());
  }

  public void testInitialActiveTeamGuestTeam() {
    getGame().newBeginWithGuestTeam();
    assertTrue(teams.isActiveTeam(teams.getGuestTeam()));
  }

  public void testInitialActiveTeamHomeTeam() {
    assertTrue(teams.isActiveTeam(teams.getHomeTeam()));
  }

  public void testInitialInactiveGuestTeam() {
    assertTrue(!teams.isActiveTeam(teams.getGuestTeam()));
  }

  public void testInitialInactiveHomeTeam() {
    getGame().newBeginWithGuestTeam();
    assertSame(teams.getHomeTeam(), teams.getInactiveTeam());
  }

  public void testInitialWinningTeam() {
    assertSame(Team.NO_TEAM, teams.getWinningTeam());
  }

  public void testSwitchFromGuestTeam() {
    getGame().newBeginWithGuestTeam();
    getGame().startNewTurn();
    assertTrue(teams.isActiveTeam(teams.getActiveTeam()));
  }

  public void testSwitchFromHomeTeam() {
    teams.startNewTurn();
    assertTrue(teams.isActiveTeam(teams.getGuestTeam()));
  }

  public void testWinningGuestTeam() {
    teams.scoreTouchdown(teams.getGuestTeam());
    teams.scoreTouchdown(teams.getGuestTeam());
    assertSame(teams.getGuestTeam(), teams.getWinningTeam());
    try {
      teams.scoreTouchdown(teams.getGuestTeam());
      fail();
    } catch (IllegalStateException expected) {
    }
  }

  public void testWinningHomeTeam() {
    teams.scoreTouchdown(teams.getHomeTeam());
    teams.scoreTouchdown(teams.getHomeTeam());
    assertSame(teams.getHomeTeam(), teams.getWinningTeam());
    try {
      teams.scoreTouchdown(teams.getHomeTeam());
      fail();
    } catch (IllegalStateException expected) {
    }
  }
}