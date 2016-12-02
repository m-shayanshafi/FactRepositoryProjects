package com.carolinarollergirls.scoreboard.event;

import com.carolinarollergirls.scoreboard.*;

public class PolicyDescriptionEvent extends PolicyEvent
{
	public PolicyDescriptionEvent(Policy p, String d) {
		super(p);
		description = d;
	}

	public String getDescription() { return description; }

	public Object clone() { return new PolicyDescriptionEvent(getPolicy(), getDescription()); }

	public void accept(ScoreBoardEventVisitor v) { accept((PolicyEventVisitor)v); }
	public void accept(PolicyEventVisitor v) { v.policyDescriptionChange(getPolicy(), getDescription()); }

	protected String description;
}
