package net.sf.bloodball.model.player;

import net.sf.bloodball.model.player.*;
import java.awt.Color;

public class NoTeam extends Team {
	
	public NoTeam() {
		super(null, "No Team", null);
	}

	public Player getPlayerFromReserve() {
		return Player.NO_PLAYER;
	}

	public boolean hasPlayerInReserve() {
		return false;
	}

	public boolean isHomeTeam() {
		return false;
	}

	public boolean isMember(Player player) {
		return false;
	}

}