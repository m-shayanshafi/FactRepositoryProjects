package com.carolinarollergirls.scoreboard.policy;

import com.carolinarollergirls.scoreboard.*;
import com.carolinarollergirls.scoreboard.model.*;
import com.carolinarollergirls.scoreboard.defaults.*;

public class IntermissionClockStartPolicy extends AbstractClockRunningChangePolicy
{
	public IntermissionClockStartPolicy() {
		super();
		addClock(Clock.ID_JAM);
		addClock(Clock.ID_PERIOD);
		setDescription("When the Period is over, this sets and starts the Intermission clock time and optionally sets the number to the Period number.");

		addParameterModel(new DefaultPolicyModel.DefaultParameterModel(this, SET_INTERMISSION_NUMBER, "Boolean", String.valueOf(true)));
	}

	// FIXME - these should be common utility methods, and probably implemented using Formatter and Scanner.
	protected String msToMinSec(long ms) {
		long min = ms / 60000;
		long sec = (ms / 1000) % 60;
		if (min > 0)
			return Long.toString(min)+":"+(sec < 10?"0":"")+Long.toString(sec);
		return Long.toString(sec);
	}
	protected long minSecToMs(String time) throws NumberFormatException {
		int colon = time.indexOf(":");
		long min = 0;
		long sec = 0;
		if (0 > colon) {
			sec = Long.parseLong(time);
		} else {
			min = Long.parseLong(time.substring(0, colon));
			sec = Long.parseLong(time.substring(colon+1));
		}
		return ((min*60)+sec)*1000;
	}

	public void setScoreBoardModel(ScoreBoardModel sbm) throws Exception {
		addParameterModel(new DefaultPolicyModel.DefaultParameterModel(this, SET_INTERMISSION_TIME_TO, "String", msToMinSec(sbm.getClock(Clock.ID_INTERMISSION).getMaximumTime())));
		super.setScoreBoardModel(sbm);
	}

	protected void startIntermissionClock() {
		ClockModel ic = getScoreBoardModel().getClockModel(Clock.ID_INTERMISSION);
		if (!ic.isRunning()) {
			if (Boolean.parseBoolean(getParameter(SET_INTERMISSION_NUMBER).getValue()))
				ic.setNumber(getScoreBoardModel().getClockModel(Clock.ID_PERIOD).getNumber());
			//FIXME - might be better to have some validity checking/enforcement in property setting instead of at usage time here
			try {
				ic.setTime(minSecToMs(getParameter(SET_INTERMISSION_TIME_TO).getValue()));
			} catch ( NumberFormatException nfE ) {
				// This probably isn't really what is desired, but we should reset the time to something...
				ic.resetTime();
			}
			ic.start();
		}
	}

	public void clockRunningChange(Clock clock, boolean running) {
		ClockModel jc = getScoreBoardModel().getClockModel(Clock.ID_JAM);
		ClockModel pc = getScoreBoardModel().getClockModel(Clock.ID_PERIOD);
		if (!jc.isRunning() && !pc.isRunning() && (pc.getTime() == pc.getMinimumTime()))
			startIntermissionClock();
	}

	public static final String SET_INTERMISSION_TIME_TO = "SetIntermissionTimeTo";
	public static final String SET_INTERMISSION_NUMBER = "SetIntermissionNumber";
}
