package com.carolinarollergirls.scoreboard.event;

import com.carolinarollergirls.scoreboard.*;

public class ScoreBoardImageEvent extends ScoreBoardEvent
{
	public ScoreBoardImageEvent(ScoreBoardImage sbi) {
		super(sbi.getScoreBoard());
		scoreBoardImage = sbi;
	}

	public ScoreBoardImage getScoreBoardImage() { return scoreBoardImage; }

	public Object clone() { return new ScoreBoardImageEvent(getScoreBoardImage()); }

	public void accept(ScoreBoardEventVisitor v) { accept((ScoreBoardImageEventVisitor)v); }
	public void accept(ScoreBoardImageEventVisitor v) { v.scoreBoardImageUnknownChange(scoreBoardImage); }

	protected ScoreBoardImage scoreBoardImage;
}
