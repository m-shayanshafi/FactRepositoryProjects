package com.carolinarollergirls.scoreboard.event;

import com.carolinarollergirls.scoreboard.*;

public class ClockNameEvent extends ClockEvent
{
	public ClockNameEvent(Clock c, String n) {
		super(c);
		name = n;
	}

	public String getName() { return name; }

	public Object clone() { return new ClockNameEvent(getClock(), getName()); }

	public void accept(ScoreBoardEventVisitor v) { accept((ClockEventVisitor)v); }
	public void accept(ClockEventVisitor v) { v.clockNameChange(getClock(), getName()); }

	protected String name;
}
