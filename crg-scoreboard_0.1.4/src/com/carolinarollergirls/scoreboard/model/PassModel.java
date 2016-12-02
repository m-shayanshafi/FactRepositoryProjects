package com.carolinarollergirls.scoreboard.model;

import com.carolinarollergirls.scoreboard.*;

public interface PassModel extends Pass
{
	public SkaterModel getSkaterModel();

	public void setScore(int score);
	public void changeScore(int score);
}
