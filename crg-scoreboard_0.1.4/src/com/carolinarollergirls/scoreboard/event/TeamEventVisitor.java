package com.carolinarollergirls.scoreboard.event;

import com.carolinarollergirls.scoreboard.*;

public interface TeamEventVisitor extends TeamLogoEventVisitor,SkaterEventVisitor
{
	public void teamUnknownChange(Team team);

	public void teamNameChange(Team team, String name);

	public void teamScoreChange(Team team, int score);

	public void teamTimeoutsChange(Team team, int timeouts);

	public void teamAddSkater(Team team, Skater skater);
	public void teamRemoveSkater(Team team, Skater skater);

	public void teamPositionChange(Team team, String position, Skater skater);

	public void teamLeadJammerChange(Team team, boolean leadJammer);
}
