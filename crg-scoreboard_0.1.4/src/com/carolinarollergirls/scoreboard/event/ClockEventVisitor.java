package com.carolinarollergirls.scoreboard.event;

import com.carolinarollergirls.scoreboard.*;

public interface ClockEventVisitor
{
	public void clockUnknownChange(Clock clock);

	public void clockNameChange(Clock clock, String name);

	public void clockNumberChange(Clock clock, int number);
	public void clockMinimumNumberChange(Clock clock, int min);
	public void clockMaximumNumberChange(Clock clock, int max);

	public void clockTimeChange(Clock clock, long time);
	public void clockMinimumTimeChange(Clock clock, long min);
	public void clockMaximumTimeChange(Clock clock, long max);

	public void clockDirectionChange(Clock clock, boolean down);

	public void clockRunningChange(Clock clock, boolean running);
}
