package com.carolinarollergirls.scoreboard.event;

import com.carolinarollergirls.scoreboard.*;

public class TeamLogoEvent extends TeamEvent
{
	public TeamLogoEvent(TeamLogo l) {
		super(l.getTeam());
		teamLogo = l;
	}

	public TeamLogo getTeamLogo() { return teamLogo; }

	public Object clone() { return new TeamLogoEvent(getTeamLogo()); }

	public void accept(ScoreBoardEventVisitor v) { accept((TeamLogoEventVisitor)v); }
	public void accept(TeamLogoEventVisitor v) { v.teamLogoUnknownChange(getTeamLogo()); }

	protected TeamLogo teamLogo;
}
