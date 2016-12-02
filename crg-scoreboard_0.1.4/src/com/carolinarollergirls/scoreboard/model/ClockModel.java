package com.carolinarollergirls.scoreboard.model;

import com.carolinarollergirls.scoreboard.*;

public interface ClockModel extends Clock
{
	public ScoreBoardModel getScoreBoardModel();

	public Clock getClock();

	public void start();
	public void stop();

	public void unstart();
	public void unstop();

	public void setName(String name);

	public void setNumber(int n);
	public void changeNumber(int n);
	public void setMinimumNumber(int ms);
	public void setMaximumNumber(int ms);

	public void setTime(long ms);
	public void changeTime(long ms);
	public void resetTime();
	public void setMinimumTime(long ms);
	public void setMaximumTime(long ms);

	public void setCountDirectionDown(boolean down);
}

