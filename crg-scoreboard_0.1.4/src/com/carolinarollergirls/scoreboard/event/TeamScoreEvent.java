package com.carolinarollergirls.scoreboard.event;

import com.carolinarollergirls.scoreboard.*;

public class TeamScoreEvent extends TeamEvent
{
	public TeamScoreEvent(Team t, int s) {
		super(t);
		score = s;
	}

	public int getScore() { return score; }

	public Object clone() { return new TeamScoreEvent(getTeam(), getScore()); }

	public void accept(ScoreBoardEventVisitor v) { accept((TeamEventVisitor)v); }
	public void accept(TeamEventVisitor v) { v.teamScoreChange(getTeam(), getScore()); }

	protected int score;

}
