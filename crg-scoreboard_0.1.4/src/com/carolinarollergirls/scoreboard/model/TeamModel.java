package com.carolinarollergirls.scoreboard.model;

import java.util.*;

import com.carolinarollergirls.scoreboard.*;

public interface TeamModel extends Team
{
	public ScoreBoardModel getScoreBoardModel();

	public Team getTeam();

	public void setName(String name);

	public TeamLogoModel getTeamLogoModel();

	public void timeout();

	public void setScore(int score);
	public void changeScore(int change);

	public void setTimeouts(int timeouts);
	public void changeTimeouts(int change);

	public void addSkaterModel(SkaterModel skater);
	public void removeSkaterModel(String name) throws SkaterNotFoundException;

	public List<SkaterModel> getSkaterModels();
	public SkaterModel getSkaterModel(String name) throws SkaterNotFoundException;

	public SkaterModel getPositionModel(String position);
	public void setPosition(String position, String name) throws SkaterNotFoundException;
	public void clearPosition(String position);

	public void setLeadJammer(boolean lead);
}
