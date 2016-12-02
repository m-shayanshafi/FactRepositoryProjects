package com.carolinarollergirls.scoreboard.event;

import com.carolinarollergirls.scoreboard.*;

public class ClockRunningEvent extends ClockEvent
{
	public ClockRunningEvent(Clock c, boolean r) {
		super(c);
		running = r;
	}

	public boolean isRunning() { return running; }

	public Object clone() { return new ClockRunningEvent(getClock(), isRunning()); }

	public void accept(ScoreBoardEventVisitor v) { accept((ClockEventVisitor)v); }
	public void accept(ClockEventVisitor v) { v.clockRunningChange(getClock(), isRunning()); }

	protected boolean running;
}
