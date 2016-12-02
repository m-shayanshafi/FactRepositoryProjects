package com.carolinarollergirls.scoreboard.event;

import com.carolinarollergirls.scoreboard.*;

public interface TeamLogoEventVisitor
{
	public void teamLogoUnknownChange(TeamLogo teamLogo);

	public void teamLogoIdChange(TeamLogo teamLogo, String id);

	public void teamLogoTypeChange(TeamLogo teamLogo, String type);

	public void teamLogoNameChange(TeamLogo teamLogo, String name);

	public void teamLogoDirectoryChange(TeamLogo teamLogo, String directory);
	public void teamLogoFilenameChange(TeamLogo teamLogo, String filename);
}
