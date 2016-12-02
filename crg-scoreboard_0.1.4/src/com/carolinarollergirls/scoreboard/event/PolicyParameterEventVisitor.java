package com.carolinarollergirls.scoreboard.event;

import com.carolinarollergirls.scoreboard.*;

public interface PolicyParameterEventVisitor
{
	public void policyParameterUnknownChange(Policy.Parameter parameter);

	public void policyParameterValueChange(Policy.Parameter parameter, String value);
}
