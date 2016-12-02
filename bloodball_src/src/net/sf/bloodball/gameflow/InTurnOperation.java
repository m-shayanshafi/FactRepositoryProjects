package net.sf.bloodball.gameflow;

public class InTurnOperation {
  public final static InTurnOperation BLOCK_AFTER_MOVE = new InTurnOperation();
  public final static InTurnOperation MOVE = new InTurnOperation();
  public final static InTurnOperation HAND_OFF = new InTurnOperation();
  public final static InTurnOperation SPRINT = new InTurnOperation();
  public final static InTurnOperation EXTRA_MOVE = new InTurnOperation();
  public final static InTurnOperation SELECT_ACTIVE_PLAYER = new InTurnOperation();
  public final static InTurnOperation SET_UP_BALL = new InTurnOperation();
  public final static InTurnOperation SET_UP_TEAM = new InTurnOperation();
  public final static InTurnOperation SUBSTITUTE = new InTurnOperation();

  private InTurnOperation() {
    super();
  }
}