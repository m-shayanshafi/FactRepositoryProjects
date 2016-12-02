package com.carolinarollergirls.scoreboard.event;

public class SkaterListenerManager extends ListenerManager implements SkaterListener
{
	public void addSkaterListener(final SkaterListener listener) {
		ManagerRunnable managerRunnable = new ManagerRunnable() {
				public void handleEvent(ScoreBoardEvent event) {
					listener.skaterChange((SkaterEvent)event);
				}
			};
		addEventListener(listener, managerRunnable);
	}

	public void removeSkaterListener(SkaterListener listener) {
		removeEventListener(listener);
	}

	public void skaterChange(SkaterEvent event) { fireEvent(event); }
}
