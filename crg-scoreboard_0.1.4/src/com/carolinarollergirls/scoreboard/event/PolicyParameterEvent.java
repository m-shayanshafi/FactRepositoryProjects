package com.carolinarollergirls.scoreboard.event;

import com.carolinarollergirls.scoreboard.*;

public class PolicyParameterEvent extends PolicyEvent
{
	public PolicyParameterEvent(Policy.Parameter p) {
		super(p.getPolicy());
		parameter = p;
	}

	public Policy.Parameter getParameter() { return parameter; }

	public Object clone() { return new PolicyParameterEvent(getParameter()); }

	public void accept(ScoreBoardEventVisitor v) { accept((PolicyParameterEventVisitor)v); }
	public void accept(PolicyParameterEventVisitor v) { v.policyParameterUnknownChange(getParameter()); }

	protected Policy.Parameter parameter;
}
