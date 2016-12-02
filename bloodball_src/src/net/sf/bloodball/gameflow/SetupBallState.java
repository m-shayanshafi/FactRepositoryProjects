package net.sf.bloodball.gameflow;

import net.sf.bloodball.model.*;
import net.sf.bloodball.model.actions.*;
import java.awt.Point;

public class SetupBallState extends State {
	private Setup setupPhase = new Setup(getGame());

	public SetupBallState(GameFlowController context) {
		super(context);
	}

	public void init() {
		setEndTurnOperation(EndTurnOperation.FINISH_BALL_SETUP);
		setMayEndTurn(false);
		setInTurnOperation(InTurnOperation.SET_UP_BALL);
	}

	public void performEndTurnOperation() {
		context.setState(new MoveSelectionState(context));
	}

	public void squareChoosen(Point position) {
		if (setupPhase.isLegalBallSetup(position)) {
			setupPhase.setupBall(position);
			setMayEndTurn(true);
		}
	}
}