package com.carolinarollergirls.scoreboard.defaults;

import com.carolinarollergirls.scoreboard.*;
import com.carolinarollergirls.scoreboard.event.*;

public class DefaultPolicyEventVisitor implements PolicyEventVisitor
{
	public DefaultPolicyEventVisitor() { }

	public DefaultPolicyEventVisitor(Policy policy) {
		PolicyListener listener = new PolicyListener() {
				public void policyChange(PolicyEvent event) {
					event.accept(DefaultPolicyEventVisitor.this);
				}
			};
		policy.addPolicyListener(listener);
	}

	public void policyUnknownChange(Policy policy) { }

	public void policyNameChange(Policy policy, String name) { }

	public void policyDescriptionChange(Policy policy, String description) { }

	public void policyEnabledChange(Policy policy, boolean enabled) { }

	public void policyParameterUnknownChange(Policy.Parameter parameter) { }

	public void policyParameterValueChange(Policy.Parameter parameter, String value) { }
}
