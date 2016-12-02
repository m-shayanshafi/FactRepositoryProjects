package com.carolinarollergirls.scoreboard.event;

import com.carolinarollergirls.scoreboard.*;

public interface PolicyEventVisitor extends PolicyParameterEventVisitor
{
	public void policyUnknownChange(Policy policy);

	public void policyNameChange(Policy policy, String name);

	public void policyDescriptionChange(Policy policy, String description);

	public void policyEnabledChange(Policy policy, boolean enabled);
}
