package com.carolinarollergirls.scoreboard.policy;

import java.util.*;

import com.carolinarollergirls.scoreboard.*;
import com.carolinarollergirls.scoreboard.model.*;
import com.carolinarollergirls.scoreboard.defaults.*;

public class JamClockControlsTeamPositionsPolicy extends AbstractClockRunningChangePolicy
{
	public JamClockControlsTeamPositionsPolicy() {
		super();
		addClock(Clock.ID_JAM);
		setDescription("This clears all Team Positions (who are not in the Penalty Box) when the Jam clock is stopped, sets all Skaters to Not Lead Jammer, and sets the Team to Not Lead Jammer.");
	}

	public void clockRunningChange(Clock clock, boolean running) {
		if (!running) {
			List<TeamModel> teams = getScoreBoardModel().getTeamModels();
			for (int i=0; i<teams.size(); i++) {
				TeamModel teamModel = teams.get(i);
				Iterator<String> positions = teamModel.getPositions().iterator();
				while (positions.hasNext()) {
					SkaterModel sM = teamModel.getPositionModel(positions.next());
					if (sM != null && !sM.isPenaltyBox())
						sM.setPosition(Team.POSITION_BENCH);
				}
				Iterator<SkaterModel> skaters = teamModel.getSkaterModels().iterator();
				while (skaters.hasNext())
					skaters.next().setLeadJammer(false);
				teamModel.setLeadJammer(false);
			}
		}
	}
}
