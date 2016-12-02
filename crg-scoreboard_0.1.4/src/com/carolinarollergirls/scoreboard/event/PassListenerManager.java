package com.carolinarollergirls.scoreboard.event;

public class PassListenerManager extends ListenerManager implements PassListener
{
	public void addPassListener(final PassListener listener) {
		ManagerRunnable managerRunnable = new ManagerRunnable() {
				public void handleEvent(ScoreBoardEvent event) {
					listener.passChange((PassEvent)event);
				}
			};
		addEventListener(listener, managerRunnable);
	}

	public void removePassListener(PassListener listener) {
		removeEventListener(listener);
	}

	public void passChange(PassEvent event) { fireEvent(event); }
}
