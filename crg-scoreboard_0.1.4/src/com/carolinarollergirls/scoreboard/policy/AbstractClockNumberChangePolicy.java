package com.carolinarollergirls.scoreboard.policy;

import java.util.*;

import com.carolinarollergirls.scoreboard.*;
import com.carolinarollergirls.scoreboard.model.*;
import com.carolinarollergirls.scoreboard.defaults.*;

public abstract class AbstractClockNumberChangePolicy extends AbstractClockChangePolicy
{
	public AbstractClockNumberChangePolicy() {
		super();
	}
	public AbstractClockNumberChangePolicy(String id) {
		super(id);
	}

	public void setScoreBoardModel(ScoreBoardModel sbm) throws Exception {
		super.setScoreBoardModel(sbm);

		Iterator<String> i = clockIds.iterator();
		while (i.hasNext()) {
			new DefaultClockEventVisitor(getScoreBoard().getClock(i.next())) {
				public void clockNumberChange(Clock clock, int number) {
					if (AbstractClockNumberChangePolicy.this.isEnabled()) {
						synchronized (changeLock) {
							AbstractClockNumberChangePolicy.this.clockNumberChange(clock, number);
						}
					}
				}
			};
		}
	}

	protected abstract void clockNumberChange(Clock clock, int number);
}
