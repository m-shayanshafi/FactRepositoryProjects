package com.carolinarollergirls.scoreboard.event;

public class TeamListenerManager extends ListenerManager implements TeamListener
{
	public void addTeamListener(final TeamListener listener) {
		ManagerRunnable managerRunnable = new ManagerRunnable() {
				public void handleEvent(ScoreBoardEvent event) {
					listener.teamChange((TeamEvent)event);
				}
			};
		addEventListener(listener, managerRunnable);
	}

	public void removeTeamListener(TeamListener listener) {
		removeEventListener(listener);
	}

	public void teamChange(TeamEvent event) { fireEvent(event); }
}
