package com.carolinarollergirls.scoreboard.event;

import com.carolinarollergirls.scoreboard.*;

public class ScoreBoardTimeoutOwnerEvent extends ScoreBoardEvent
{
	public ScoreBoardTimeoutOwnerEvent(ScoreBoard sb, String owner) {
		super(sb);
		timeoutOwner = owner;
	}

	public String getTimeoutOwner() { return timeoutOwner; }

	public Object clone() { return new ScoreBoardTimeoutOwnerEvent(getScoreBoard(), getTimeoutOwner()); }

	public void accept(ScoreBoardEventVisitor v) { v.scoreBoardTimeoutOwnerChange(getScoreBoard(), getTimeoutOwner()); }

	protected String timeoutOwner;
}
