package com.carolinarollergirls.scoreboard.event;

import com.carolinarollergirls.scoreboard.*;

public class PolicyParameterValueEvent extends PolicyParameterEvent
{
	public PolicyParameterValueEvent(Policy.Parameter p, String v) {
		super(p);
		value = v;
	}

	public String getValue() { return value; }

	public Object clone() { return new PolicyParameterValueEvent(getParameter(), getValue()); }

	public void accept(ScoreBoardEventVisitor v) { accept((PolicyParameterEventVisitor)v); }
	public void accept(PolicyParameterEventVisitor v) { v.policyParameterValueChange(getParameter(), getValue()); }

	protected String value;
}
