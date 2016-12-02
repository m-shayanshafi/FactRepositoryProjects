package net.sf.bloodball.model.player;

import java.awt.*;

public class NoPlayer extends Player {
	
	protected NoPlayer() {
		super(null, PlayerType.NO_TYPE);
	}
	
	public int getRemainingMovePoints() {
		return 0;
	}

	public Point getPosition() {
		return null;
	}
	
	public int getProneTurns() {
		return 0;
	}

	public int getSquaresToGo() {
		return 0;
	}

	public Team getTeam() {
		return Team.NO_TEAM;
	}

	public boolean hasActed() {
		return false;
	}

	public boolean hasMoved() {
		return false;
	}

	public boolean inBallPossession() {
		return false;
	}

	public boolean isAtCall() {
		return false;
	}

	public boolean isInjured() {
		return false;
	}
	
	public boolean mayPickUpBall() {
		return false;
	}

	public boolean isProne() {
		return false;
	}
	
	public boolean isOnField() {
		return false;
	}

	public boolean isReserve() {
		return false;
	}
	
	public boolean inSprintMode() {
		return false;
	}

	public boolean hasSprinted() {
		return false;
	}

  public void move() {
    throw new UnsupportedOperationException();
  }

  public void injure() {
    throw new UnsupportedOperationException();
  }

  public void knockdown() {
    throw new UnsupportedOperationException();
  }
}