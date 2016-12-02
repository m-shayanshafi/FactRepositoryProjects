package net.sf.bloodball.model.test;

import java.awt.Point;
import java.util.*;
import net.sf.bloodball.model.Ball;
import net.sf.bloodball.model.player.Player;
import net.sf.bloodball.test.ModelTest;

public class BallTest extends ModelTest {
  private List neighborSquaresOfSquareFiveZero = new ArrayList();
  private List neighborSquaresOfSquareTwoTwo = new ArrayList();
  private Ball ball;

  public BallTest(String name) {
    super(name);
  }

  private void assertBallPosition(Point position) {
    assertEquals(position, ball.getPosition());
  }

  private void buildNeighborsOfSquareFiveZero() {
    neighborSquaresOfSquareFiveZero.add(new Point(4, 1));
    neighborSquaresOfSquareFiveZero.add(new Point(5, 1));
    neighborSquaresOfSquareFiveZero.add(new Point(6, 1));
  }

  private void buildNeighborsOfSquareTwoTwo() {
    neighborSquaresOfSquareTwoTwo.add(new Point(1, 1));
    neighborSquaresOfSquareTwoTwo.add(new Point(1, 2));
    neighborSquaresOfSquareTwoTwo.add(new Point(1, 3));
    neighborSquaresOfSquareTwoTwo.add(new Point(2, 1));
    neighborSquaresOfSquareTwoTwo.add(new Point(2, 3));
    neighborSquaresOfSquareTwoTwo.add(new Point(3, 1));
    neighborSquaresOfSquareTwoTwo.add(new Point(3, 2));
    neighborSquaresOfSquareTwoTwo.add(new Point(3, 3));
  }

  protected void setUp() throws Exception {
    super.setUp();
    ball = new Ball(getGame().getField());
    buildNeighborsOfSquareFiveZero();
    buildNeighborsOfSquareTwoTwo();
  }

  public void testInitialPosition() {
    assertNull(ball.getPosition());
  }

  public void testOtherPosition() {
    ball.setPosition(squareThreeThree);
    assertBallPosition(squareThreeThree);
  }

  public void testPosition() {
    ball.setPosition(squareTwoTwo);
    assertBallPosition(squareTwoTwo);
  }

  public void testScattersAwayFromOrigin() {
    ball.setPosition(squareTwoTwo);
    ball.scatter();
    assertTrue(ball.getPosition() != this.squareTwoTwo);
  }

  public void testScattersToNeighborField() {
    ball.setPosition(this.squareTwoTwo);
    ball.scatter();
    assertTrue(neighborSquaresOfSquareTwoTwo.contains(ball.getPosition()));
  }

  public void testThrowInOnNeighborField() {
    ball.throwIn(squareFiveZero);
    assertTrue(neighborSquaresOfSquareFiveZero.contains(ball.getPosition()));
  }

  public void testThrowInOnPronePlayer() {
    for (int i = 0; i < neighborSquaresOfSquareFiveZero.size(); i++) {
      Player player = getHomeTeamPlayer();
      player.knockOver();
      setPlayerTo(player, (Point) neighborSquaresOfSquareFiveZero.get(i));
    }
    ball.throwIn(squareFiveZero);
    assertTrue(!neighborSquaresOfSquareFiveZero.contains(squareFiveZero));
  }
}