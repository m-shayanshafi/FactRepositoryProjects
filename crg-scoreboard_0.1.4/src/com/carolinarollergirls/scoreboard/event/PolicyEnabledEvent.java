package com.carolinarollergirls.scoreboard.event;

import com.carolinarollergirls.scoreboard.*;

public class PolicyEnabledEvent extends PolicyEvent
{
	public PolicyEnabledEvent(Policy p, boolean e) {
		super(p);
		enabled = e;
	}

	public boolean getEnabled() { return enabled; }

	public Object clone() { return new PolicyEnabledEvent(getPolicy(), getEnabled()); }

	public void accept(ScoreBoardEventVisitor v) { accept((PolicyEventVisitor)v); }
	public void accept(PolicyEventVisitor v) { v.policyEnabledChange(getPolicy(), getEnabled()); }

	protected boolean enabled;
}
