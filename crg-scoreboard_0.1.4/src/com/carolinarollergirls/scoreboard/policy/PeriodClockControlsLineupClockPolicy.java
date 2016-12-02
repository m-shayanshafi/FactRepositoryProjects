package com.carolinarollergirls.scoreboard.policy;

import com.carolinarollergirls.scoreboard.*;
import com.carolinarollergirls.scoreboard.model.*;
import com.carolinarollergirls.scoreboard.defaults.*;

public class PeriodClockControlsLineupClockPolicy extends AbstractClockRunningChangePolicy
{
	public PeriodClockControlsLineupClockPolicy() {
		super();
		addClock(Clock.ID_PERIOD);
		setDescription("This controls the Lineup clock based on the Period clock.  When the Period clock stops and its time is equal to its 0 (i.e. its minimum), and the Jam clock is also stopped, the Lineup clock is stopped and reset.");
	}

	public void clockRunningChange(Clock clock, boolean running) {
		ClockModel jc = getScoreBoardModel().getClockModel(Clock.ID_JAM);
		ClockModel lc = getScoreBoardModel().getClockModel(Clock.ID_LINEUP);
		if (!running) {
			if ((clock.getTime() == clock.getMinimumTime()) && !jc.isRunning()) {
				lc.stop();
				lc.resetTime();
			}
		}
	}
}
