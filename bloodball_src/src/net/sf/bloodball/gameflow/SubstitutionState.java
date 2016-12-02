package net.sf.bloodball.gameflow;

import java.awt.Point;
import net.sf.bloodball.model.*;
import net.sf.bloodball.model.actions.*;
import net.sf.bloodball.model.player.Team;

public class SubstitutionState extends State {
  
  private Substitution substitution = new Substitution(getGame());
  private int playerNumber;

	public SubstitutionState(GameFlowController context, int playerNumber) {
		super(context);
		this.playerNumber = playerNumber;
	}

	public void init() {
    setEndTurnOperation(EndTurnOperation.END_TURN);
    setInTurnOperation(InTurnOperation.SUBSTITUTE);
    Notifier.fireDugOutActivatedEvent(getActiveTeam(), playerNumber);
	}

	public void performEndTurnOperation() {
	}

	public void squareChoosen(Point position) {
    if (substitution.isLegal(position)) {
      substitution.perform(position, playerNumber);
      inciteTurnBeginSelectionState();
      Notifier.fireSquareChangedEvent(position);
      Notifier.fireDugOutChangedEvent(getActiveTeam(), playerNumber);
    }
	}

	protected void inciteTurnBeginSelectionState() {
		context.setState(new TurnBeginSelectionState(context));
		Notifier.fireDugOutDeactivatedEvent(getActiveTeam(), playerNumber);
	}
  
	public void deselectInTurnOperation() {
		inciteTurnBeginSelectionState();
	}
	
	private Team getActiveTeam() {
		return getGame().getTeams().getActiveTeam();
	}

}
