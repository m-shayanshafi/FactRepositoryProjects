package com.carolinarollergirls.scoreboard.event;

import com.carolinarollergirls.scoreboard.*;

public class TeamRemoveSkaterEvent extends TeamEvent
{
	public TeamRemoveSkaterEvent(Team t, Skater s) {
		super(t);
		skater = s;
	}

	public Skater getSkater() { return skater; }

	public Object clone() { return new TeamRemoveSkaterEvent(getTeam(), getSkater()); }

	public void accept(TeamEventVisitor v) { v.teamRemoveSkater(getTeam(), getSkater()); }

	protected Skater skater;
}
