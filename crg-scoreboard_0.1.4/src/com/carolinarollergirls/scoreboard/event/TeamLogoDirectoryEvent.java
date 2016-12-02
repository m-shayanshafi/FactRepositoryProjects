package com.carolinarollergirls.scoreboard.event;

import com.carolinarollergirls.scoreboard.*;

public class TeamLogoDirectoryEvent extends TeamLogoEvent
{
	public TeamLogoDirectoryEvent(TeamLogo l, String d) {
		super(l);
		directory = d;
	}

	public String getDirectory() { return directory; }

	public Object clone() { return new TeamLogoDirectoryEvent(getTeamLogo(), getDirectory()); }

	public void accept(ScoreBoardEventVisitor v) { accept((TeamLogoEventVisitor)v); }
	public void accept(TeamLogoEventVisitor v) { v.teamLogoDirectoryChange(getTeamLogo(), getDirectory()); }

	protected String directory;
}
