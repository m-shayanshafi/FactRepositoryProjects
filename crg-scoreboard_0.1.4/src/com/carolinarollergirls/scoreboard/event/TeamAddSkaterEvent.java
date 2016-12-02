package com.carolinarollergirls.scoreboard.event;

import com.carolinarollergirls.scoreboard.*;

public class TeamAddSkaterEvent extends TeamEvent
{
	public TeamAddSkaterEvent(Team t, Skater s) {
		super(t);
		skater = s;
	}

	public Skater getSkater() { return skater; }

	public Object clone() { return new TeamAddSkaterEvent(getTeam(), getSkater()); }

	public void accept(TeamEventVisitor v) { v.teamAddSkater(getTeam(), getSkater()); }

	protected Skater skater;
}
