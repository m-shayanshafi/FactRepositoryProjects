package com.carolinarollergirls.scoreboard.event;

import com.carolinarollergirls.scoreboard.*;

public class TeamNameEvent extends TeamEvent
{
	public TeamNameEvent(Team t, String n) {
		super(t);
		name = n;
	}

	public String getName() { return name; }

	public Object clone() { return new TeamNameEvent(getTeam(), getName()); }

	public void accept(ScoreBoardEventVisitor v) { accept((TeamEventVisitor)v); }
	public void accept(TeamEventVisitor v) { v.teamNameChange(getTeam(), getName()); }

	protected String name;
}
