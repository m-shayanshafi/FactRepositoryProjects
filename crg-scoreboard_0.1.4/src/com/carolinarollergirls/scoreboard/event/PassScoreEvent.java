package com.carolinarollergirls.scoreboard.event;

import com.carolinarollergirls.scoreboard.*;

public class PassScoreEvent extends PassEvent
{
	public PassScoreEvent(Pass p, int s) {
		super(p);
		score = s;
	}

	public int getScore() { return score; }

	public Object clone() { return new PassScoreEvent(getPass(), getScore()); }

	public void accept(ScoreBoardEventVisitor v) { accept((SkaterEventVisitor)v); }
	public void accept(PassEventVisitor v) { v.passScoreChange(getPass(), getScore()); }

	protected int score;
}
