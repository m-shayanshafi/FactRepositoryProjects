package com.carolinarollergirls.scoreboard.event;

import com.carolinarollergirls.scoreboard.*;

public class TeamTimeoutsEvent extends TeamEvent
{
	public TeamTimeoutsEvent(Team t, int to) {
		super(t);
		timeouts = to;
	}

	public int getTimeouts() { return timeouts; }

	public Object clone() { return new TeamTimeoutsEvent(getTeam(), getTimeouts()); }

	public void accept(ScoreBoardEventVisitor v) { accept((TeamEventVisitor)v); }
	public void accept(TeamEventVisitor v) { v.teamTimeoutsChange(getTeam(), getTimeouts()); }

	protected int timeouts;
}
