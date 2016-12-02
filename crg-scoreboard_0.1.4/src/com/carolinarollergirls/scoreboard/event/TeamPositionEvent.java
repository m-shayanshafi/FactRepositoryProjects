package com.carolinarollergirls.scoreboard.event;

import com.carolinarollergirls.scoreboard.*;

public class TeamPositionEvent extends TeamEvent
{
	public TeamPositionEvent(Team t, String p, Skater s) {
		super(t);
		position = p;
		skater = s;
	}

	public String getPosition() { return position; }

	public Skater getSkater() { return skater; }

	public Object clone() { return new TeamPositionEvent(getTeam(), getPosition(), getSkater()); }

	public void accept(TeamEventVisitor v) { v.teamPositionChange(getTeam(), getPosition(), getSkater()); }

	protected String position;
	protected Skater skater;
}
