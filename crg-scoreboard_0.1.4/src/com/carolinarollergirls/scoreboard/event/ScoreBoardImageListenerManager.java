package com.carolinarollergirls.scoreboard.event;

public class ScoreBoardImageListenerManager extends ListenerManager implements ScoreBoardImageListener
{
	public void addScoreBoardImageListener(final ScoreBoardImageListener listener) {
		ManagerRunnable managerRunnable = new ManagerRunnable() {
				public void handleEvent(ScoreBoardEvent event) {
					listener.scoreBoardImageChange((ScoreBoardImageEvent)event);
				}
			};
		addEventListener(listener, managerRunnable);
	}

	public void removeScoreBoardImageListener(ScoreBoardImageListener listener) {
		removeEventListener(listener);
	}

	public void scoreBoardImageChange(ScoreBoardImageEvent event) { fireEvent(event); }
}
