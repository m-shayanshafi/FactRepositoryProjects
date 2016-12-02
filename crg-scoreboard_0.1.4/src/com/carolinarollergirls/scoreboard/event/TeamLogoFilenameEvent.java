package com.carolinarollergirls.scoreboard.event;

import com.carolinarollergirls.scoreboard.*;

public class TeamLogoFilenameEvent extends TeamLogoEvent
{
	public TeamLogoFilenameEvent(TeamLogo l, String f) {
		super(l);
		filename = f;
	}

	public String getFilename() { return filename; }

	public Object clone() { return new TeamLogoFilenameEvent(getTeamLogo(), getFilename()); }

	public void accept(ScoreBoardEventVisitor v) { accept((TeamLogoEventVisitor)v); }
	public void accept(TeamLogoEventVisitor v) { v.teamLogoFilenameChange(getTeamLogo(), getFilename()); }

	protected String filename;
}
