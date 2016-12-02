package com.carolinarollergirls.scoreboard.event;

public class ClockListenerManager extends ListenerManager implements ClockListener
{
	public void addClockListener(final ClockListener listener) {
		ManagerRunnable managerRunnable = new ManagerRunnable() {
				public void handleEvent(ScoreBoardEvent event) {
					listener.clockChange((ClockEvent)event);
				}
			};
		addEventListener(listener, managerRunnable);
	}

	public void removeClockListener(ClockListener listener) {
		removeEventListener(listener);
	}

	public void clockChange(ClockEvent event) { fireEvent(event); }
}
