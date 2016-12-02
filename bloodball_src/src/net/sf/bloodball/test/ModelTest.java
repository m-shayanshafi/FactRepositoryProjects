package net.sf.bloodball.test;

import de.vestrial.util.awt.Points;
import de.vestrial.util.test.TestDice;
import java.awt.Point;
import junit.framework.TestCase;
import net.sf.bloodball.gameflow.*;
import net.sf.bloodball.model.Teams;
import net.sf.bloodball.model.actions.Move;
import net.sf.bloodball.model.player.Health;
import net.sf.bloodball.model.player.Player;
import net.sf.bloodball.model.player.Team;
import net.sf.bloodball.util.Dices;

public abstract class ModelTest extends TestCase implements Squares {
  private MockGame game;

  public ModelTest(String name) {
    super(name);
  }
  
  protected void beginGameWithGuestTeam() {
    game.newBeginWithGuestTeam();
  }

  protected Point getBallPosition() {
    return getGame().getBall().getPosition();
  }
  
  protected void setGame(MockGame game) {
  	this.game = game;
  	game.newBeginWithHomeTeam();
  }

  protected MockGame getGame() {
    return game;
  }

  protected Team getGuestTeam() {
    return game.getTeams().getGuestTeam();
  }

  protected Player getGuestTeamPlayer() {
    return getGuestTeam().getPlayerFromReserve();
  }

  protected Point getPosition(Player player) {
    return game.getField().getPlayerPosition(player);
  }

  protected Team getHomeTeam() {
    return game.getTeams().getHomeTeam();
  }

  protected Player getHomeTeamPlayer() {
    return getHomeTeam().getPlayerFromReserve();
  }

  protected Player getLegalPlayerAt(Point square) {
    return getGame().getField().getPlayer(square);
  }

  protected MoveActionState getMoveActionState() {
    GameFlowController context = new GameFlowController(getGame());
    return new MoveActionState(context, new MoveSelectionState(context));
  }

  protected Player getPlayerAt(Point position) {
    return getGame().getField().getPlayer(position);
  }

  protected Teams getTeams() {
    return game.getTeams();
  }

  protected void setBallPosition(Point position) {
    getGame().getBall().setPosition(position);
  }

  public void setMockBall() {
    getGame().setBall(new MockBall(getGame().getField()));
  }

  protected Player setPlayerTo(Player player, Point position) {
    getGame().getField().setPlayer(position, player);
    return player;
  }

  protected Player setPlayerWithBallTo(Player player, Point position) {
    setPlayerTo(player, position);
    setBallPosition(position);
    player.catchBall();
    return player;
  }

  protected void setUp() throws Exception {
    game = new MockGame();
    game.newBeginWithHomeTeam();
  }

  protected void tearDown() throws Exception {
    new Dices.TestInterface().resetD6();
    new Dices.TestInterface().resetD8();
  }

  protected void setD6Results(int[] values) {
    new Dices.TestInterface().setD6(new TestDice(values));
  }

  protected void setD6Result(int value) {
    setD6Results(new int[] { value });
  }

  protected void setD6Results(int first, int second) {
    setD6Results(new int[] { first, second, 1, 1});
  }

  protected void setD8Result(int value) {
    new Dices.TestInterface().setD8(new TestDice(new int[] { value }));
  }

  protected void placeAndInjure(Player player) {
    setPlayerTo(player, squareEighteenOne);
    player.injure(Health.INJURED);
  }

  protected void movePlayer(Player player, int squaresToGo) {
    for (int i = 0; i < squaresToGo; i++) {
      movePlayerByOneSquare(player);
    }
  }

  protected void movePlayerByOneSquare(Player player) {
    Point destination = Points.add(getPosition(player), new Point(1, 1));
    movePlayerToSquare(player, destination);
  }

  protected void movePlayerToSquare(Player player, Point destination) {
    Point position = getPosition(player);
    new Move(getGame()).perform(position, destination);
  }

}