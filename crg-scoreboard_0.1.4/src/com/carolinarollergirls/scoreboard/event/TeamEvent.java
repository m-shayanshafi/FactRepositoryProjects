package com.carolinarollergirls.scoreboard.event;

import com.carolinarollergirls.scoreboard.*;

public class TeamEvent extends ScoreBoardEvent
{
	public TeamEvent(Team t) {
		super(t.getScoreBoard());
		team = t;
	}

	public Team getTeam() { return team; }

	public Object clone() { return new TeamEvent(getTeam()); }

	public void accept(ScoreBoardEventVisitor v) { accept((TeamEventVisitor)v); }
	public void accept(TeamEventVisitor v) { v.teamUnknownChange(getTeam()); }

	protected Team team;
}
