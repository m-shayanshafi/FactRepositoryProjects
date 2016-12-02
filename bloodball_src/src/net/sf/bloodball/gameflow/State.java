package net.sf.bloodball.gameflow;

import java.awt.Point;
import net.sf.bloodball.model.*;
import net.sf.bloodball.model.player.Team;

public abstract class State {

  protected GameFlowController context;
  private EndTurnOperation endTurnOperation;
  private InTurnOperation inTurnOperation;
  private boolean mayEndTurn;

  public State(GameFlowController context) {
    this.context = context;
  }

  public void deselectInTurnOperation() {
  }

  public EndTurnOperation getEndTurnOperation() {
    return endTurnOperation;
  }

  protected Game getGame() {
    return context.getGame();
  }

  public InTurnOperation getInTurnOperation() {
    return inTurnOperation;
  }

  public abstract void init();

  public boolean mayEndTurn() {
    return mayEndTurn;
  }

  public abstract void performEndTurnOperation();

  protected void setEndTurnOperation(EndTurnOperation newOperation) {
    endTurnOperation = newOperation;
    Notifier.fireEndTurnOperationChangedEvent();
  }
  
  protected void setInTurnOperation(InTurnOperation newInTurnOperation) {
    inTurnOperation = newInTurnOperation;
    Notifier.fireInTurnOperationChangedEvent();
  }

  protected void setMayEndTurn(boolean mayEndTurn) {
    this.mayEndTurn = mayEndTurn;
    Notifier.fireEndTurnOperationChangedEvent();
  }

  public abstract void squareChoosen(Point position);

  public void dugOutChoosen(Team team, int playerNumber) {
  }
}