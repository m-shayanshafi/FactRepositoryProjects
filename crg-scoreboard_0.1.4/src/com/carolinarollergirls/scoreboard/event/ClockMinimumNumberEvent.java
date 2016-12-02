package com.carolinarollergirls.scoreboard.event;

import com.carolinarollergirls.scoreboard.*;

public class ClockMinimumNumberEvent extends ClockEvent
{
	public ClockMinimumNumberEvent(Clock c, int min) {
		super(c);
		minimum = min;
	}

	public int getMinimumNumber() { return minimum; }

	public Object clone() { return new ClockMinimumNumberEvent(getClock(), getMinimumNumber()); }

	public void accept(ScoreBoardEventVisitor v) { accept((ClockEventVisitor)v); }
	public void accept(ClockEventVisitor v) { v.clockMinimumNumberChange(getClock(), getMinimumNumber()); }

	protected int minimum;
}
