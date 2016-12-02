package com.carolinarollergirls.scoreboard.event;

import com.carolinarollergirls.scoreboard.*;

public class ScoreBoardAddScoreBoardImageEvent extends ScoreBoardEvent
{
	public ScoreBoardAddScoreBoardImageEvent(ScoreBoard sb, ScoreBoardImage sbI) {
		super(sb);
		scoreBoardImage = sbI;
	}

	public ScoreBoardImage getScoreBoardImage() { return scoreBoardImage; }

	public Object clone() { return new ScoreBoardAddScoreBoardImageEvent(getScoreBoard(), getScoreBoardImage()); }

	public void accept(ScoreBoardEventVisitor v) { v.scoreBoardAddScoreBoardImage(getScoreBoard(), getScoreBoardImage()); }

	protected ScoreBoardImage scoreBoardImage;
}
