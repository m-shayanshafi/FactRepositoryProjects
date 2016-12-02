package com.carolinarollergirls.scoreboard.defaults;

import java.awt.image.*;

import com.carolinarollergirls.scoreboard.*;
import com.carolinarollergirls.scoreboard.event.*;

public class DefaultScoreBoardEventVisitor implements ScoreBoardEventVisitor
{
	public DefaultScoreBoardEventVisitor() { }

	public DefaultScoreBoardEventVisitor(ScoreBoard scoreBoard) {
		final ScoreBoardEventVisitor v = this;
		ScoreBoardListener listener = new ScoreBoardListener() {
				public void scoreBoardChange(ScoreBoardEvent event) {
					event.accept(v);
				}
			};
		scoreBoard.addScoreBoardListener(listener);
	}

	public void setWarning(boolean b) {
		warning = b;
	}

	public boolean getWarning() {
		return warning;
	}

	protected void warn(String msg, Object o) {
		if (warning)
			System.err.println(getClass().getName()+": unhandled ScoreBoardEvent : "+msg+" with param "+o);
	}

	public void scoreBoardUnknownChange(ScoreBoard scoreBoard) {
		warn("scoreBoardUnknownChange", null);
	}

	public void scoreBoardAddScoreBoardImage(ScoreBoard scoreBoard, ScoreBoardImage scoreBoardImage) {
		warn("scoreBoardAddScoreBoardImage", scoreBoardImage);
	}
	public void scoreBoardRemoveScoreBoardImage(ScoreBoard scoreBoard, ScoreBoardImage scoreBoardImage) {
		warn("scoreBoardRemoveScoreBoardImage", scoreBoardImage);
	}

	public void scoreBoardTimeoutOwnerChange(ScoreBoard scoreBoard, String owner) {
		warn("scoreBoardTimeoutOwnerChange", owner);
	}

	public void scoreBoardImageUnknownChange(ScoreBoardImage scoreBoardImage) {
		warn("scoreBoardImageUnknownChange", null);
	}

	public void scoreBoardImageNameChange(ScoreBoardImage scoreBoardImage, String name) {
		warn("scoreBoardImageNameChange", name);
	}

	public void policyUnknownChange(Policy policy) {
		warn("policyUnknownChange", null);
	}

	public void policyNameChange(Policy policy, String name) {
		warn("policyNameChange", name);
	}

	public void policyDescriptionChange(Policy policy, String description) {
		warn("policyDescriptionChange", description);
	}

	public void policyEnabledChange(Policy policy, boolean enabled) {
		warn("policyEnabledChange", enabled);
	}

	public void policyParameterUnknownChange(Policy.Parameter parameter) {
		warn("policyParameterUnknownChange", null);
	}

	public void policyParameterValueChange(Policy.Parameter parameter, String value) {
		warn("policyParameterValueChange", value);
	}

	public void clockUnknownChange(Clock clock) {
		warn("clockUnknownChange", null);
	}

	public void clockNameChange(Clock clock, String name) {
		warn("clockNameChange", name);
	}

	public void clockNumberChange(Clock clock, int number) {
		warn("clockNumberChange", new Integer(number));
	}
	public void clockMinimumNumberChange(Clock clock, int min) {
		warn("clockMinimumNumberChange", new Integer(min));
	}
	public void clockMaximumNumberChange(Clock clock, int max) {
		warn("clockMaximumNumberChange", new Integer(max));
	}

	public void clockTimeChange(Clock clock, long time) {
		warn("clockTimeChange", new Long(time));
	}
	public void clockMinimumTimeChange(Clock clock, long min) {
		warn("clockMinimumTimeChange", new Long(min));
	}
	public void clockMaximumTimeChange(Clock clock, long max) {
		warn("clockMaximumTimeChange", new Long(max));
	}

	public void clockDirectionChange(Clock clock, boolean down) {
		warn("clockDirectionChange", new Boolean(down));
	}

	public void clockRunningChange(Clock clock, boolean running) {
		warn("clockRunningChange", new Boolean(running));
	}

	public void teamUnknownChange(Team team) {
		warn("teamUnknownChange", null);
	}

	public void teamNameChange(Team team, String name) {
		warn("teamNameChange", name);
	}

	public void teamScoreChange(Team team, int score) {
		warn("teamScoreChange", new Integer(score));
	}

	public void teamTimeoutsChange(Team team, int timeouts) {
		warn("teamTimeoutsChange", new Integer(timeouts));
	}

	public void teamAddSkater(Team team, Skater skater) {
		warn("teamAddSkater", skater);
	}
	public void teamRemoveSkater(Team team, Skater skater) {
		warn("teamRemoveSkater", skater);
	}

	public void teamPositionChange(Team team, String position, Skater skater) {
		warn("teamPositionChange", position+"="+(skater==null?"":skater.getId()));
	}

	public void teamLeadJammerChange(Team team, boolean leadJammer) {
		warn("teamLeadJammerChange", leadJammer);
	}

	public void teamLogoUnknownChange(TeamLogo teamLogo) {
		warn("teamLogoUnknownChange", null);
	}

	public void teamLogoIdChange(TeamLogo teamLogo, String id) {
		warn("teamLogoIdChange", id);
	}

	public void teamLogoTypeChange(TeamLogo teamLogo, String type) {
		warn("teamLogoTypeChange", type);
	}

	public void teamLogoNameChange(TeamLogo teamLogo, String name) {
		warn("teamLogoNameChange", name);
	}

	public void teamLogoDirectoryChange(TeamLogo teamLogo, String directory) {
		warn("teamLogoDirectoryChange", directory);
	}

	public void teamLogoFilenameChange(TeamLogo teamLogo, String filename) {
		warn("teamLogoFilenameChange", filename);
	}

	public void skaterUnknownChange(Skater skater) {
		warn("skaterUnknownChange", null);
	}

	public void skaterNameChange(Skater skater, String name) {
		warn("skaterNameChange", name);
	}

	public void skaterNumberChange(Skater skater, String number) {
		warn("skaterNumberChange", number);
	}

	public void skaterPositionChange(Skater skater, String position) {
		warn("skaterPositionChange", position);
	}

	public void skaterLeadJammerChange(Skater skater, boolean leadJammer) {
		warn("skaterLeadJammerChange", new Boolean(leadJammer));
	}

	public void skaterPenaltyBoxChange(Skater skater, boolean penaltyBox) {
		warn("skaterPenaltyBoxChange", new Boolean(penaltyBox));
	}

	public void skaterCurrentPassChange(Skater skater, int currentPass) {
		warn("skaterCurrentPassChange", new Integer(currentPass));
	}

	public void passUnknownChange(Pass pass) {
		warn("passUnknownChange", null);
	}

	public void passScoreChange(Pass pass, int score) {
		warn("passScoreChange", score);
	}

	protected boolean warning = false;
}
