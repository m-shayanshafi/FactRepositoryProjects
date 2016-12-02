package com.carolinarollergirls.scoreboard.policy;

import java.util.*;

import com.carolinarollergirls.scoreboard.*;
import com.carolinarollergirls.scoreboard.model.*;
import com.carolinarollergirls.scoreboard.defaults.*;

public abstract class AbstractClockRunningChangePolicy extends AbstractClockChangePolicy
{
	public AbstractClockRunningChangePolicy() {
		super();
	}
	public AbstractClockRunningChangePolicy(String id) {
		super(id);
	}

	public void setScoreBoardModel(ScoreBoardModel sbm) throws Exception {
		super.setScoreBoardModel(sbm);

		Iterator<String> i = clockIds.iterator();
		while (i.hasNext()) {
			new DefaultClockEventVisitor(getScoreBoard().getClock(i.next())) {
				public void clockRunningChange(Clock clock, boolean running) {
					if (AbstractClockRunningChangePolicy.this.isEnabled()) {
						synchronized (changeLock) {
							AbstractClockRunningChangePolicy.this.clockRunningChange(clock, running);
						}
					}
				}
			};
		}
	}

	protected abstract void clockRunningChange(Clock clock, boolean running);
}
