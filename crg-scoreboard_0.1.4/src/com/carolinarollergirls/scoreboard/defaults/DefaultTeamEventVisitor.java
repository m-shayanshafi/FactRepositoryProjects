package com.carolinarollergirls.scoreboard.defaults;

import com.carolinarollergirls.scoreboard.*;
import com.carolinarollergirls.scoreboard.event.*;

public class DefaultTeamEventVisitor implements TeamEventVisitor
{
	public DefaultTeamEventVisitor() { }

	public DefaultTeamEventVisitor(Team team) {
		TeamListener listener = new TeamListener() {
				public void teamChange(TeamEvent event) {
					event.accept(DefaultTeamEventVisitor.this);
				}
			};
		team.addTeamListener(listener);
	}

	public void teamUnknownChange(Team team) { }

	public void teamNameChange(Team team, String name) { }

	public void teamScoreChange(Team team, int score) { }

	public void teamTimeoutsChange(Team team, int timeouts) { }

	public void teamAddSkater(Team team, Skater skater) { }
	public void teamRemoveSkater(Team team, Skater skater) { }

	public void teamPositionChange(Team team, String position, Skater skater) { }

	public void teamLeadJammerChange(Team team, boolean leadJammer) { }

	public void teamLogoUnknownChange(TeamLogo teamLogo) { }

	public void teamLogoIdChange(TeamLogo teamLogo, String id) { }

	public void teamLogoTypeChange(TeamLogo teamLogo, String type) { }

	public void teamLogoNameChange(TeamLogo teamLogo, String name) { }

	public void teamLogoDirectoryChange(TeamLogo teamLogo, String directory) { }
	public void teamLogoFilenameChange(TeamLogo teamLogo, String filename) { }

	public void skaterUnknownChange(Skater skater) { }

	public void skaterNameChange(Skater skater, String name) { }

	public void skaterNumberChange(Skater skater, String number) { }

	public void skaterPositionChange(Skater skater, String position) { }

	public void skaterLeadJammerChange(Skater skater, boolean leadJammer) { }

	public void skaterPenaltyBoxChange(Skater skater, boolean penaltyBox) { }

	public void skaterCurrentPassChange(Skater skater, int currentPass) { }

	public void passUnknownChange(Pass pass) { }

	public void passScoreChange(Pass pass, int score) { }
}
