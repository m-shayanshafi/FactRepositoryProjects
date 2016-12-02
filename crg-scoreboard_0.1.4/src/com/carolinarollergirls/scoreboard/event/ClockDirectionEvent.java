package com.carolinarollergirls.scoreboard.event;

import com.carolinarollergirls.scoreboard.*;

public class ClockDirectionEvent extends ClockEvent
{
	public ClockDirectionEvent(Clock c, boolean down) {
		super(c);
		countDown = down;
	}

	public boolean isCountDirectionDown() { return countDown; }

	public Object clone() { return new ClockDirectionEvent(getClock(), isCountDirectionDown()); }

	public void accept(ScoreBoardEventVisitor v) { accept((ClockEventVisitor)v); }
	public void accept(ClockEventVisitor v) { v.clockDirectionChange(getClock(), isCountDirectionDown()); }

	protected boolean countDown;
}
