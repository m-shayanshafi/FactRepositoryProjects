package net.sf.bloodball.view;

import de.vestrial.util.Range;
import java.awt.*;
import javax.swing.Icon;
import javax.swing.JComponent;
import net.sf.bloodball.ModelFacade;
import net.sf.bloodball.model.FieldExtents;
import net.sf.bloodball.model.player.*;
import net.sf.bloodball.view.event.*;

public class GameBoard extends JComponent {

  private GameBoardListener gameBoardListener;
  private ModelFacade modelFacade;
  private Icons icons;
  private final static int DUG_OUT_HEIGHT = 2;
  private final static int DUG_OUT_SIZE = DUG_OUT_HEIGHT * FieldExtents.SETUP_ZONE_WIDTH;

  public GameBoard(ModelFacade modelFacade) {
    this.modelFacade = modelFacade;
    this.icons = new Icons();
    setLayout(new GridLayout(FieldExtents.SIZE.height + 4, FieldExtents.SIZE.width, 1, 1));
    addRidgeSquares(FieldExtents.GUEST_SETUP_ZONE, modelFacade.getGuestTeam());
    addFieldSquares();
    addRidgeSquares(FieldExtents.HOME_SETUP_ZONE, modelFacade.getHomeTeam());
    initVisualization();
  }

  public void initVisualization() {
    for (int row = 0; row < FieldExtents.SIZE.height; row++) {
      for (int column = 0; column < FieldExtents.SIZE.width; column++) {
        final Square square = getFieldSquare(column, row);
        square.update(Player.NO_PLAYER, false);
      }
    }
    for (int index = 0; index < DUG_OUT_SIZE; index++) {
      Player player = modelFacade.getHomeTeam().getPlayerByNumber(index);
      getHomeDugOutSquare(index).update(player, false);
      getHomeDugOutSquare(index).setListener(new DugOutHandler(index, modelFacade.getHomeTeam(), this));
      player = modelFacade.getGuestTeam().getPlayerByNumber(index);
      getGuestDugOutSquare(index).update(player, false);
      getGuestDugOutSquare(index).setListener(new DugOutHandler(index, modelFacade.getGuestTeam(), this));
    }
  }

  public void updateSquare(Point position) {
    Square fieldSquare = getFieldSquare(position.x, position.y);
    Player player = modelFacade.getPlayerAt(position);
    if (!player.inBallPossession() && position.equals(modelFacade.getBallPosition())) {
      fieldSquare.update(player, Icons.getIconForSquareWithBall(player, fieldSquare), modelFacade.isPlayerActive(player));
    } else {
      fieldSquare.update(player, modelFacade.isPlayerActive(player));
    }
  }

  public void updateDugOut(Team team, int playerNumber) {
    Player player = team.getPlayerByNumber(playerNumber);
    getDugOutSquare(team, playerNumber).update(player.isOnField() ? Player.NO_PLAYER : player, false);
  }

  public GameBoardListener getGameBoardListener() {
    return gameBoardListener;
  }

  public Square getFieldSquare(int column, int row) {
    return (Square) getComponent((row + DUG_OUT_HEIGHT) * FieldExtents.SIZE.width + column);
  }

  public void paint(Graphics graphics) {
    paintBackground(graphics);
    super.paint(graphics);
    paintSplitLines(graphics);
    paintSubstitionBoundaries(graphics);
  }

  public void setGameBoardListener(GameBoardListener listener) {
    gameBoardListener = listener;
  }

  private void addRidgeSquares(Range setupZone, Team team) {
    for (int rows = 0; rows < 2; rows++) {
      for (int column = 0; column < FieldExtents.SIZE.width; column++) {
        add(getRidgeSquare(setupZone, column));
      }
    }
    for (int i = 0; i < Team.SIZE; i++) {
      getDugOutSquare(team, i).setListener(new DugOutHandler(i, team, this));
    }
  }

  private DugOutSquare getDugOutSquare(Team team, int playerNumber) {
    return team == modelFacade.getGuestTeam() ? getGuestDugOutSquare(playerNumber) : getHomeDugOutSquare(playerNumber);
  }

  private JComponent getRidgeSquare(Range setupZone, int column) {
    if (setupZone.contains(column)) {
      return new DugOutSquare();
    }
    return new SidelineSquare();
  }

  private void addFieldSquares() {
    for (int row = 0; row < FieldExtents.SIZE.height; row++) {
      addFieldSquares(FieldExtents.HOME_END_ZONE, row, this.modelFacade.getHomeTeam().getColor(), null);
      addFieldSquares(ModelFacade.PLAYING_ZONE, row, Colors.BACKGROUND, Icons.GRAS_IMAGE);
      addFieldSquares(FieldExtents.GUEST_END_ZONE, row, this.modelFacade.getGuestTeam().getColor(), null);
    }
  }

  private void addFieldSquares(Range range, int row, Color color, Image backgroundImage) {
    for (int column = range.getLowerBound(); column <= range.getUpperBound(); column++) {
      Square square = new Square(color, backgroundImage);
      add(square);
      square.setListener(new SquareHandler(new Point(column, row), this));
    }
  }

  private void drawVerticalLine(Graphics graphics, int position) {
    int dugOutHeight = DUG_OUT_HEIGHT * getSquareWidth();
    graphics.drawLine(position, dugOutHeight, position, getHeight() - dugOutHeight);
  }

  private DugOutSquare getGuestDugOutSquare(int index) {
    index = DUG_OUT_SIZE - index - 1;
    Range zone = FieldExtents.GUEST_SETUP_ZONE;
    int row = 0 + index / zone.getWidth();
    int column = zone.getLowerBound() + index % zone.getWidth();
    return (DugOutSquare) getComponent(row * FieldExtents.SIZE.width + column);
  }

  public DugOutSquare getHomeDugOutSquare(int index) {
    Range zone = FieldExtents.HOME_SETUP_ZONE;
    int row = DUG_OUT_HEIGHT + FieldExtents.SIZE.height + index / zone.getWidth();
    int column = zone.getLowerBound() + index % zone.getWidth();
    return (DugOutSquare) getComponent(row * FieldExtents.SIZE.width + column);
  }

  public ModelFacade getModelFacade() {
    return modelFacade;
  }

  private void paintBackground(Graphics graphics) {
    graphics.setColor(Colors.LINE);
    graphics.fillRect(0, 0, getWidth(), getHeight());
  }

  private void paintSplitLines(Graphics graphics) {
    graphics.setColor(Colors.SPLIT_LINE);
    drawVerticalLine(graphics, getSquareWidth() * (FieldExtents.END_ZONE_WIDTH + FieldExtents.SETUP_ZONE_WIDTH) - 1);
    drawVerticalLine(graphics, getSquareWidth() * (FieldExtents.END_ZONE_WIDTH + 2 * FieldExtents.SETUP_ZONE_WIDTH) - 1);
  }

  private void paintSubstitionBoundaries(Graphics graphics) {
    graphics.setColor(Color.red.brighter().brighter());
    int startPosition = getSquareWidth() * FieldExtents.GUEST_SETUP_ZONE.getLowerBound() + getSubstitutionZoneWidth();
    drawSubstitutionLine(graphics, startPosition, 2 * getSquareWidth() - 1);
    startPosition = getSquareWidth() * FieldExtents.HOME_SETUP_ZONE.getLowerBound();
    drawSubstitutionLine(graphics, startPosition, getSquareWidth() * (FieldExtents.SIZE.height + 2) - 1);
  }

  private void drawSubstitutionLine(Graphics graphics, int startPosition, int y) {
    graphics.drawLine(startPosition, y, startPosition + getSubstitutionZoneWidth(), y);
  }

  private int getSubstitutionZoneWidth() {
    return (FieldExtents.SETUP_ZONE_WIDTH / 2) * getSquareWidth() - 1;
  }

  private int getSquareWidth() {
    return getComponent(0).getWidth() + 1;
  }

  public void setDugOutSquareActivated(Team team, int playerNumber, boolean activated) {
    getDugOutSquare(team, playerNumber).setActivated(activated);
  }

}