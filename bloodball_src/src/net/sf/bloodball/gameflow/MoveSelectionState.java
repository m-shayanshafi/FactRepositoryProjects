package net.sf.bloodball.gameflow;

import java.awt.Point;
import net.sf.bloodball.model.*;
import net.sf.bloodball.model.actions.Move;
import net.sf.bloodball.model.player.Team;

public class MoveSelectionState extends State {
  public MoveSelectionState(GameFlowController context) {
    super(context);
  }

  public void init() {
    setEndTurnOperation(EndTurnOperation.END_TURN);
    setMayEndTurn(true);
    setInTurnOperation(InTurnOperation.SELECT_ACTIVE_PLAYER);
  }

  public void performEndTurnOperation() {
    getGame().startNewTurn();
    context.setState(new TurnBeginSelectionState(context));
  }

  public void squareChoosen(Point position) {
    if (new Move(context.getGame()).isMoveablePlayer(position)) {
      context.setActivePlayer(context.getGame().getField().getPlayer(position));
      context.setState(new MoveActionState(context, this));
      Notifier.fireSquareChangedEvent(position);
    }
  }

}