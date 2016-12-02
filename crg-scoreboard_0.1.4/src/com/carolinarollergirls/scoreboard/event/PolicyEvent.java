package com.carolinarollergirls.scoreboard.event;

import com.carolinarollergirls.scoreboard.*;

public class PolicyEvent extends ScoreBoardEvent
{
	public PolicyEvent(Policy p) {
		super(p.getScoreBoard());
		policy = p;
	}

	public Policy getPolicy() { return policy; }

	public Object clone() { return new PolicyEvent(getPolicy()); }

	public void accept(ScoreBoardEventVisitor v) { accept((PolicyEventVisitor)v); }
	public void accept(PolicyEventVisitor v) { v.policyUnknownChange(getPolicy()); }

	protected Policy policy;
}
