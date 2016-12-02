package com.carolinarollergirls.scoreboard.defaults;

import com.carolinarollergirls.scoreboard.*;
import com.carolinarollergirls.scoreboard.event.*;

public class DefaultPolicyParameterEventVisitor implements PolicyParameterEventVisitor
{
	public DefaultPolicyParameterEventVisitor() { }

	public DefaultPolicyParameterEventVisitor(Policy.Parameter parameter) {
		PolicyParameterListener listener = new PolicyParameterListener() {
				public void policyParameterChange(PolicyParameterEvent event) {
					event.accept(DefaultPolicyParameterEventVisitor.this);
				}
			};
		parameter.addPolicyParameterListener(listener);
	}

	public void policyParameterUnknownChange(Policy.Parameter parameter) { }

	public void policyParameterValueChange(Policy.Parameter parameter, String value) { }
}
