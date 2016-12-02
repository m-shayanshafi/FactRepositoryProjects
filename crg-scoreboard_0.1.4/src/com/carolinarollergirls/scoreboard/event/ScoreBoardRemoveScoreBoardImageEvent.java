package com.carolinarollergirls.scoreboard.event;

import com.carolinarollergirls.scoreboard.*;

public class ScoreBoardRemoveScoreBoardImageEvent extends ScoreBoardEvent
{
	public ScoreBoardRemoveScoreBoardImageEvent(ScoreBoard sb, ScoreBoardImage sbI) {
		super(sb);
		scoreBoardImage = sbI;
	}

	public ScoreBoardImage getScoreBoardImage() { return scoreBoardImage; }

	public Object clone() { return new ScoreBoardRemoveScoreBoardImageEvent(getScoreBoard(), getScoreBoardImage()); }

	public void accept(ScoreBoardEventVisitor v) { v.scoreBoardRemoveScoreBoardImage(getScoreBoard(), getScoreBoardImage()); }

	protected ScoreBoardImage scoreBoardImage;
}
