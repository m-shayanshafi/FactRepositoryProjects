package com.carolinarollergirls.scoreboard.event;

import com.carolinarollergirls.scoreboard.*;

public class TeamLogoIdEvent extends TeamLogoEvent
{
	public TeamLogoIdEvent(TeamLogo l, String i) {
		super(l);
		id = i;
	}

	public String getId() { return id; }

	public Object clone() { return new TeamLogoIdEvent(getTeamLogo(), getId()); }

	public void accept(ScoreBoardEventVisitor v) { accept((TeamLogoEventVisitor)v); }
	public void accept(TeamLogoEventVisitor v) { v.teamLogoIdChange(getTeamLogo(), getId()); }

	protected String id;
}
