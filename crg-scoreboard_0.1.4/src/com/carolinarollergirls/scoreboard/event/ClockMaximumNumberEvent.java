package com.carolinarollergirls.scoreboard.event;

import com.carolinarollergirls.scoreboard.*;

public class ClockMaximumNumberEvent extends ClockEvent
{
	public ClockMaximumNumberEvent(Clock c, int max) {
		super(c);
		maximum = max;
	}

	public int getMaximumNumber() { return maximum; }

	public Object clone() { return new ClockMaximumNumberEvent(getClock(), getMaximumNumber()); }

	public void accept(ScoreBoardEventVisitor v) { accept((ClockEventVisitor)v); }
	public void accept(ClockEventVisitor v) { v.clockMaximumNumberChange(getClock(), getMaximumNumber()); }

	protected int maximum;
}
