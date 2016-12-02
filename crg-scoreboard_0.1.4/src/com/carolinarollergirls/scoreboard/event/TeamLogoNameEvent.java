package com.carolinarollergirls.scoreboard.event;

import com.carolinarollergirls.scoreboard.*;

public class TeamLogoNameEvent extends TeamLogoEvent
{
	public TeamLogoNameEvent(TeamLogo l, String n) {
		super(l);
		name = n;
	}

	public String getName() { return name; }

	public Object clone() { return new TeamLogoNameEvent(getTeamLogo(), getName()); }

	public void accept(ScoreBoardEventVisitor v) { accept((TeamLogoEventVisitor)v); }
	public void accept(TeamLogoEventVisitor v) { v.teamLogoNameChange(getTeamLogo(), getName()); }

	protected String name;
}
