package com.carolinarollergirls.scoreboard.defaults;

import java.util.*;

import com.carolinarollergirls.scoreboard.*;
import com.carolinarollergirls.scoreboard.event.*;
import com.carolinarollergirls.scoreboard.model.*;

public class DefaultPassModel extends PassListenerManager implements PassModel
{
	public DefaultPassModel(SkaterModel sm, int n) {
		skaterModel = sm;
		number = n;
	}

	public Skater getSkater() { return getSkaterModel(); }
	public SkaterModel getSkaterModel() { return skaterModel; }

	public String getId() { return String.valueOf(getNumber()); }
	public int getNumber() { return number; }

	public int getScore() { return score; }
	public void setScore(int s) {
		synchronized (scoreLock) {
			score = s;
			passChange(new PassScoreEvent(this, score));
		}
	}
	public void changeScore(int s) {
		synchronized (scoreLock) {
			score += s;
			passChange(new PassScoreEvent(this, score));
		}
	}

	protected SkaterModel skaterModel;
	protected int number;
	protected int score = 0;
	protected Object scoreLock = new Object();
}
