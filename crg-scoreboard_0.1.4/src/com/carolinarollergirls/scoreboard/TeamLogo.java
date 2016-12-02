package com.carolinarollergirls.scoreboard;

import java.awt.image.*;

import com.carolinarollergirls.scoreboard.event.*;

public interface TeamLogo
{
	public Team getTeam();

	public void addTeamLogoListener(TeamLogoListener listener);
	public void removeTeamLogoListener(TeamLogoListener listener);

	public String getId();

	public String getType();

	public String getName();

	public String getDirectory();
	public String getFilename();

	public BufferedImage getImage();
}
