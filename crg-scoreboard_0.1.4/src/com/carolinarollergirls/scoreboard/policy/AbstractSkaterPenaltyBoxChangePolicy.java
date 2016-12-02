package com.carolinarollergirls.scoreboard.policy;

import java.util.*;

import com.carolinarollergirls.scoreboard.*;
import com.carolinarollergirls.scoreboard.model.*;
import com.carolinarollergirls.scoreboard.defaults.*;

public abstract class AbstractSkaterPenaltyBoxChangePolicy extends AbstractSkaterChangePolicy
{
	public AbstractSkaterPenaltyBoxChangePolicy() {
		super();
	}
	public AbstractSkaterPenaltyBoxChangePolicy(String id) {
		super(id);
	}

	public void setScoreBoardModel(ScoreBoardModel sbm) throws Exception {
		super.setScoreBoardModel(sbm);

		new DefaultScoreBoardEventVisitor(getScoreBoard()) {
			public void skaterPenaltyBoxChange(Skater skater, boolean box) {
				if (AbstractSkaterPenaltyBoxChangePolicy.this.isEnabled()) {
					synchronized (changeLock) {
						try {
							SkaterModel sM = getScoreBoardModel().getTeamModel(skater.getTeam().getId()).getSkaterModel(skater.getId());
							AbstractSkaterPenaltyBoxChangePolicy.this.skaterPenaltyBoxChange(sM, box);
						} catch ( SkaterNotFoundException snfE ) {
							System.err.println("ERROR: could not find SkaterModel for Skater '"+skater.getId()+"' : "+snfE.getMessage());
						}
					}
				}
			}
		};
	}

	protected abstract void skaterPenaltyBoxChange(SkaterModel skater, boolean box);
}
