package com.carolinarollergirls.scoreboard.event;

import com.carolinarollergirls.scoreboard.*;

public class ClockMinimumTimeEvent extends ClockEvent
{
	public ClockMinimumTimeEvent(Clock c, long min) {
		super(c);
		minimum = min;
	}

	public long getMinimumTime() { return minimum; }

	public Object clone() { return new ClockMinimumTimeEvent(getClock(), getMinimumTime()); }

	public void accept(ScoreBoardEventVisitor v) { accept((ClockEventVisitor)v); }
	public void accept(ClockEventVisitor v) { v.clockMinimumTimeChange(getClock(), getMinimumTime()); }

	protected long minimum;
}
