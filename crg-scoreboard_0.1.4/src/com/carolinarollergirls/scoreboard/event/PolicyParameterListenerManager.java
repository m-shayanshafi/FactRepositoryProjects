package com.carolinarollergirls.scoreboard.event;

public class PolicyParameterListenerManager extends ListenerManager implements PolicyParameterListener
{
	public void addPolicyParameterListener(final PolicyParameterListener listener) {
		ManagerRunnable managerRunnable = new ManagerRunnable() {
				public void handleEvent(ScoreBoardEvent event) {
					listener.policyParameterChange((PolicyParameterEvent)event);
				}
			};
		addEventListener(listener, managerRunnable);
	}

	public void removePolicyParameterListener(PolicyParameterListener listener) {
		removeEventListener(listener);
	}

	public void policyParameterChange(PolicyParameterEvent event) { fireEvent(event); }
}
