package com.carolinarollergirls.scoreboard.event;

import com.carolinarollergirls.scoreboard.*;

public class ClockMaximumTimeEvent extends ClockEvent
{
	public ClockMaximumTimeEvent(Clock c, long max) {
		super(c);
		maximum = max;
	}

	public long getMaximumTime() { return maximum; }

	public Object clone() { return new ClockMaximumTimeEvent(getClock(), getMaximumTime()); }

	public void accept(ScoreBoardEventVisitor v) { accept((ClockEventVisitor)v); }
	public void accept(ClockEventVisitor v) { v.clockMaximumTimeChange(getClock(), getMaximumTime()); }

	protected long maximum;
}
