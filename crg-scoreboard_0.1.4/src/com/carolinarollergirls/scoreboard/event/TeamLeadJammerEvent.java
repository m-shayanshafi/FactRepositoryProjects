package com.carolinarollergirls.scoreboard.event;

import com.carolinarollergirls.scoreboard.*;

public class TeamLeadJammerEvent extends TeamEvent
{
	public TeamLeadJammerEvent(Team t, boolean l) {
		super(t);
		leadJammer = l;
	}

	public boolean getLeadJammer() { return leadJammer; }

	public Object clone() { return new TeamLeadJammerEvent(getTeam(), getLeadJammer()); }

	public void accept(ScoreBoardEventVisitor v) { accept((TeamEventVisitor)v); }
	public void accept(TeamEventVisitor v) { v.teamLeadJammerChange(getTeam(), getLeadJammer()); }

	protected boolean leadJammer;
}
