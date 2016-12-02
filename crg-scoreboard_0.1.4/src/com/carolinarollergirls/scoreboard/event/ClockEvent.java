package com.carolinarollergirls.scoreboard.event;

import com.carolinarollergirls.scoreboard.*;

public class ClockEvent extends ScoreBoardEvent
{
	public ClockEvent(Clock c) {
		super(c.getScoreBoard());
		clock = c;
	}

	public Clock getClock() { return clock; }

	public Object clone() { return new ClockEvent(getClock()); }

	public void accept(ScoreBoardEventVisitor v) { accept((ClockEventVisitor)v); }
	public void accept(ClockEventVisitor v) { v.clockUnknownChange(clock); }

	protected Clock clock;
}
