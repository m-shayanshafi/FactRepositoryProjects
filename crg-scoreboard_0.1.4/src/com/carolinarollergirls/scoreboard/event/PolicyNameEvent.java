package com.carolinarollergirls.scoreboard.event;

import com.carolinarollergirls.scoreboard.*;

public class PolicyNameEvent extends PolicyEvent
{
	public PolicyNameEvent(Policy p, String n) {
		super(p);
		name = n;
	}

	public String getName() { return name; }

	public Object clone() { return new PolicyNameEvent(getPolicy(), getName()); }

	public void accept(ScoreBoardEventVisitor v) { accept((PolicyEventVisitor)v); }
	public void accept(PolicyEventVisitor v) { v.policyNameChange(getPolicy(), getName()); }

	protected String name;
}
