package com.carolinarollergirls.scoreboard.event;

public class PolicyListenerManager extends ListenerManager implements PolicyListener
{
	public void addPolicyListener(final PolicyListener listener) {
		ManagerRunnable managerRunnable = new ManagerRunnable() {
				public void handleEvent(ScoreBoardEvent event) {
					listener.policyChange((PolicyEvent)event);
				}
			};
		addEventListener(listener, managerRunnable);
	}

	public void removePolicyListener(PolicyListener listener) {
		removeEventListener(listener);
	}

	public void policyChange(PolicyEvent event) { fireEvent(event); }
}
