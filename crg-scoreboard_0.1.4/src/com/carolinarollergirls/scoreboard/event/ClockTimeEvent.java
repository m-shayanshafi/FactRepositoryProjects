package com.carolinarollergirls.scoreboard.event;

import com.carolinarollergirls.scoreboard.*;

public class ClockTimeEvent extends ClockEvent
{
	public ClockTimeEvent(Clock c, long ms) {
		super(c);
		time = ms;
	}

	public long getTime() { return time; }

	public Object clone() { return new ClockTimeEvent(getClock(), getTime()); }

	public void accept(ScoreBoardEventVisitor v) { accept((ClockEventVisitor)v); }
	public void accept(ClockEventVisitor v) { v.clockTimeChange(getClock(), getTime()); }

	protected long time;
}
