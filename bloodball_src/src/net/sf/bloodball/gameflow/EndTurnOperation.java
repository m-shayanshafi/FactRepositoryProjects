package net.sf.bloodball.gameflow;

public class EndTurnOperation {
	
	public final static EndTurnOperation END_TURN = new EndTurnOperation();
	public final static EndTurnOperation FINISH_BALL_SETUP = new EndTurnOperation();
	public final static EndTurnOperation FINISH_TEAM_SETUP = new EndTurnOperation();
	public final static EndTurnOperation PICK_UP_BALL = new EndTurnOperation();

	private EndTurnOperation() {
		super();
	}
}