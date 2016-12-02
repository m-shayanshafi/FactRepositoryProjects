package com.carolinarollergirls.scoreboard.event;

import com.carolinarollergirls.scoreboard.*;

public class TeamLogoTypeEvent extends TeamLogoEvent
{
	public TeamLogoTypeEvent(TeamLogo l, String t) {
		super(l);
		type = t;
	}

	public String getType() { return type; }

	public Object clone() { return new TeamLogoTypeEvent(getTeamLogo(), getType()); }

	public void accept(ScoreBoardEventVisitor v) { accept((TeamLogoEventVisitor)v); }
	public void accept(TeamLogoEventVisitor v) { v.teamLogoTypeChange(getTeamLogo(), getType()); }

	protected String type;
}
