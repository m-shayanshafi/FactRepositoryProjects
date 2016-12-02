package com.carolinarollergirls.scoreboard.policy;

import java.util.*;

import com.carolinarollergirls.scoreboard.*;
import com.carolinarollergirls.scoreboard.model.*;
import com.carolinarollergirls.scoreboard.defaults.*;

public abstract class AbstractSkaterPositionChangePolicy extends AbstractSkaterChangePolicy
{
	public AbstractSkaterPositionChangePolicy() {
		super();
	}
	public AbstractSkaterPositionChangePolicy(String id) {
		super(id);
	}

	public void setScoreBoardModel(ScoreBoardModel sbm) throws Exception {
		super.setScoreBoardModel(sbm);

		new DefaultScoreBoardEventVisitor(getScoreBoard()) {
			public void skaterPositionChange(Skater skater, String position) {
				if (AbstractSkaterPositionChangePolicy.this.isEnabled()) {
					synchronized (changeLock) {
						try {
							SkaterModel sM = getScoreBoardModel().getTeamModel(skater.getTeam().getId()).getSkaterModel(skater.getId());
							AbstractSkaterPositionChangePolicy.this.skaterPositionChange(sM, position);
						} catch ( SkaterNotFoundException snfE ) {
							System.err.println("ERROR: could not find SkaterModel for Skater '"+skater.getId()+"' : "+snfE.getMessage());
						}
					}
				}
			}
		};
	}

	protected abstract void skaterPositionChange(SkaterModel skater, String position);
}
