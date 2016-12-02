package net.sf.bloodball.gameflow;

import de.vestrial.util.error.ShouldNeverBeReachedException;
import java.awt.Point;
import java.util.*;
import net.sf.bloodball.model.*;
import net.sf.bloodball.model.actions.*;
import net.sf.bloodball.model.player.*;

public class MoveActionState extends State {
  private List actions;
  private Block block;
  private State undoState;

  public MoveActionState(GameFlowController context, State undoState) {
    super(context);
    this.undoState = undoState;
    block = new Block(context.getGame());
    actions = new ArrayList();
    actions.add(block);
    actions.add(new HandOff(context.getGame()));
    actions.add(new Move(context.getGame()));
    actions.add(new Throw(context.getGame()));
  }

  public void deselectInTurnOperation() {
    if (!context.getActivePlayer().hasMoved()) {
      context.deactivatePlayer();
      inciteUndoState();
    } else {
      context.getActivePlayer().setMoveMode(new NoMoveMode(context.getActivePlayer()));
      context.deactivatePlayer();
      inciteMoveSelection();
    }
  }

  private void handleTouchdown() {
    if (isTouchdown()) {
      Team scoringTeam = getGame().getBall().getPossessingPlayer().getTeam();
      getGame().getTeams().scoreTouchdown(scoringTeam);
      context.getActivePlayer().dropBall();
      context.deactivatePlayer();
      if (getGame().getTeams().getWinningTeam() != Team.NO_TEAM) {
        Notifier.fireGameEndEvent();
      } else {
        prepareNewRound(getGame().getTeams().getOpponentTeam(scoringTeam));
        Notifier.fireTouchdownScoredEvent();
      }
    }
  }

  public void inciteMoveSelection() {
    context.setState(new MoveSelectionState(context));
  }

  public void inciteUndoState() {
    context.setState(undoState);
  }

  public void init() {
    setEndTurnOperation(EndTurnOperation.PICK_UP_BALL);
    Player player = context.getActivePlayer();
    setInTurnOperation(player != Player.NO_PLAYER && player.inNoMoveMode() ? InTurnOperation.HAND_OFF : InTurnOperation.MOVE);
    setMayEndTurn(context.isPlayerActive() && context.getActivePlayerPosition().equals(getGame().getBall().getPosition()));
  }

  private boolean isTouchdown() {
    if (getGame().getBall().getPossessingPlayer() == Player.NO_PLAYER) {
      return false;
    }
    Point ballPosition = getGame().getBall().getPosition();
    Team ballPossessingTeam = getGame().getField().getPlayer(ballPosition).getTeam();
    Team ballPossessorOpponent = getGame().getTeams().getOpponentTeam(ballPossessingTeam);
    return getGame().getField().inEndZone(getGame().getBall().getPosition(), ballPossessorOpponent);
  }

  public void performEndTurnOperation() {
    context.getActivePlayer().pickUpBall();
    setMayEndTurn(false);
    updateInturnOperation();
  }

  private void prepareNewRound(Team beginningTeam) {
    context.setState(new SetupTeamState(context));
    getGame().startNewRound(beginningTeam);
  }

  public void prepareNoFurtherMove() {
    if (!context.isPlayerActive()) {
      inciteMoveSelection();
    } else if (block.mayBlock(context.getActivePlayer())) {
      setInTurnOperation(InTurnOperation.BLOCK_AFTER_MOVE);
    } else if (hasToStopActing()) {
      context.deactivatePlayer();
      inciteMoveSelection();
    }
  }

  public void squareChoosen(Point position) {
    Point activePosition = context.getActivePlayerPosition();
    for (Iterator actions = this.actions.iterator(); actions.hasNext();) {
      MoveAction action = (MoveAction) actions.next();
      if (action.isLegal(context.getActivePlayerPosition(), position)) {
        action.perform(activePosition, position);
        setMayEndTurn(context.getActivePlayer().mayPickUpBall());
        updateInturnOperation();
        Notifier.fireSquareChangedEvent(activePosition);
        Notifier.fireSquareChangedEvent(position);
        if (action.endsTeamTurn()) {
          context.deactivatePlayer();
        }
        if (hasToStopActing()) {
          prepareNoFurtherMove();
        }
        handleTouchdown();
        return;
      }
    }
  }

  private void updateInturnOperation() {
    if (context.getActivePlayer().inExtraMoveMode()) {
      setInTurnOperation(InTurnOperation.EXTRA_MOVE);
    } else if (context.getActivePlayer().inSprintMode()) {
      setInTurnOperation(InTurnOperation.SPRINT);
    } else {
      setInTurnOperation(InTurnOperation.MOVE);
    }
  }

  private boolean hasToStopActing() {
    Player player = context.getActivePlayer();
    return player.hasToStopMoving() || player.hasActed() || player.isInjured() || player.isProne();
  }

}