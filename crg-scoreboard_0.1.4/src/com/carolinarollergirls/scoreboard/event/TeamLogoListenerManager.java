package com.carolinarollergirls.scoreboard.event;

public class TeamLogoListenerManager extends ListenerManager implements TeamLogoListener
{
	public void addTeamLogoListener(final TeamLogoListener listener) {
		ManagerRunnable managerRunnable = new ManagerRunnable() {
				public void handleEvent(ScoreBoardEvent event) {
					listener.teamLogoChange((TeamLogoEvent)event);
				}
			};
		addEventListener(listener, managerRunnable);
	}

	public void removeTeamLogoListener(TeamLogoListener listener) {
		removeEventListener(listener);
	}

	public void teamLogoChange(TeamLogoEvent event) { fireEvent(event); }
}
