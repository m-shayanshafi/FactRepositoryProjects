package com.carolinarollergirls.scoreboard.defaults;

import java.awt.image.*;

import javax.imageio.*;

import java.io.*;
import java.util.*;

import com.carolinarollergirls.scoreboard.*;
import com.carolinarollergirls.scoreboard.event.*;
import com.carolinarollergirls.scoreboard.model.*;

public class DefaultTeamLogoModel extends TeamLogoListenerManager implements TeamLogoModel {
	public DefaultTeamLogoModel(TeamModel tM, String i) {
		teamModel = tM;
		setId(i);
		new ScoreBoardImageRemoveEventVisitor(getTeamModel().getScoreBoard());
	}

	public Team getTeam() { return getTeamModel().getTeam(); }
	public TeamModel getTeamModel() { return teamModel; }

	public TeamLogo getTeamLogo() { return this; }

	public String getId() { try { return scoreBoardImage.getId(); } catch ( NullPointerException npE ) { return ""; } }
	public void setId(String i) {
		scoreBoardImage = getTeam().getScoreBoard().getScoreBoardImage(i);

		teamLogoChange(new TeamLogoIdEvent(this, getId()));
		teamLogoChange(new TeamLogoTypeEvent(this, getType()));
		teamLogoChange(new TeamLogoNameEvent(this, getName()));
		teamLogoChange(new TeamLogoDirectoryEvent(this, getDirectory()));
		teamLogoChange(new TeamLogoFilenameEvent(this, getFilename()));
	}

	public String getType() { try { return scoreBoardImage.getType(); } catch ( NullPointerException npE ) { return ""; } }

	public String getName() { try { return scoreBoardImage.getName(); } catch ( NullPointerException npE ) { return ""; } }

	public String getDirectory() { try { return scoreBoardImage.getDirectory(); } catch ( NullPointerException npE ) { return ""; } }
	public String getFilename() { try { return scoreBoardImage.getFilename(); } catch ( NullPointerException npE ) { return ""; } }

	public BufferedImage getImage() { try {	return scoreBoardImage.getImage(); } catch ( NullPointerException npE ) { return null; } }

	protected TeamModel teamModel;

	protected ScoreBoardImage scoreBoardImage = null;


	protected class ScoreBoardImageRemoveEventVisitor extends DefaultScoreBoardEventVisitor
	{
		public ScoreBoardImageRemoveEventVisitor(ScoreBoard sB) {
			super(sB);
		}

		public void scoreBoardRemoveScoreBoardImage(ScoreBoard sB, ScoreBoardImage sbI) {
			try {
				if (scoreBoardImage.getId().equals(sbI.getId()))
					setId(null);
			} catch ( NullPointerException npE ) {
			}
		}
	}
}
