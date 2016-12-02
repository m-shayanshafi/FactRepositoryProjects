package net.sf.bloodball.gameflow;

import net.sf.bloodball.model.actions.Substitution;
import net.sf.bloodball.model.player.*;

public class TurnBeginSelectionState extends MoveSelectionState {
	
	private Substitution substitution;

	public TurnBeginSelectionState(GameFlowController context) {
		super(context);
		substitution = new Substitution(getGame());
	}

  public void dugOutChoosen(Team team, int playerNumber) {
    if (substitution.isLegalSetupPlayer(team, playerNumber)) {
      context.setState(new SubstitutionState(context, playerNumber));
    }
  }
  
}
