package com.carolinarollergirls.scoreboard.policy;

import com.carolinarollergirls.scoreboard.*;
import com.carolinarollergirls.scoreboard.model.*;
import com.carolinarollergirls.scoreboard.defaults.*;

public class CountdownClockControlsStartJamPolicy extends AbstractClockRunningChangePolicy
{
	public CountdownClockControlsStartJamPolicy() {
		super();
		addClock(Clock.ID_COUNTDOWN);
		setDescription("This starts the Jam by calling the ScoreBoardModel.startJam() method when the Countdown clock reaches its minimum value (by default 0).");
	}

	public void clockRunningChange(Clock clock, boolean running) {
		ClockModel cc = getScoreBoardModel().getClockModel(Clock.ID_COUNTDOWN);
		long endTime = cc.isCountDirectionDown() ? cc.getMinimumTime() : cc.getMaximumTime();
		if (!running && cc.getTime() == endTime) {
			getScoreBoardModel().startJam();
		}
	}
}
