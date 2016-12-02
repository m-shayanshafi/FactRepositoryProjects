package com.carolinarollergirls.scoreboard.event;

public class ScoreBoardListenerManager extends ListenerManager implements ScoreBoardListener
{
	public void addScoreBoardListener(final ScoreBoardListener listener) {
		ManagerRunnable managerRunnable = new ManagerRunnable() {
				public void handleEvent(ScoreBoardEvent event) {
					listener.scoreBoardChange(event);
				}
			};
		addEventListener(listener, managerRunnable);
	}

	public void removeScoreBoardListener(ScoreBoardListener listener) {
		removeEventListener(listener);
	}

	public void scoreBoardChange(ScoreBoardEvent event) { fireEvent(event); }
}
