package com.carolinarollergirls.scoreboard.event;

import com.carolinarollergirls.scoreboard.*;

public class ScoreBoardEvent implements Cloneable
{
	public ScoreBoardEvent(ScoreBoard sb) {
		scoreBoard = sb;
	}

	public ScoreBoard getScoreBoard() { return scoreBoard; }

	public Object clone() { return new ScoreBoardEvent(getScoreBoard()); }

	public void accept(ScoreBoardEventVisitor v) { v.scoreBoardUnknownChange(scoreBoard); }

	protected ScoreBoard scoreBoard;
}
