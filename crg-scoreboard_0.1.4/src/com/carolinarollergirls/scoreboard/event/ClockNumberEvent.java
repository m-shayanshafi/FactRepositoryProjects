package com.carolinarollergirls.scoreboard.event;

import com.carolinarollergirls.scoreboard.*;

public class ClockNumberEvent extends ClockEvent
{
	public ClockNumberEvent(Clock c, int n) {
		super(c);
		number = n;
	}

	public int getNumber() { return number; }

	public Object clone() { return new ClockNumberEvent(getClock(), getNumber()); }

	public void accept(ScoreBoardEventVisitor v) { accept((ClockEventVisitor)v); }
	public void accept(ClockEventVisitor v) { v.clockNumberChange(getClock(), getNumber()); }

	protected int number;
}
