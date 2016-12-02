package com.carolinarollergirls.scoreboard;

import java.util.*;

import com.carolinarollergirls.scoreboard.event.*;

public interface Skater
{
	public Team getTeam();

	public void addSkaterListener(SkaterListener listener);
	public void removeSkaterListener(SkaterListener listener);

	public String getId();

	public String getName();

	public String getNumber();

	public String getPosition();

	public boolean isLeadJammer();

	public boolean isPenaltyBox();

	public int getCurrentPass();
	public List<Integer> getPasses();
	public Pass getPass(int pass);
}
