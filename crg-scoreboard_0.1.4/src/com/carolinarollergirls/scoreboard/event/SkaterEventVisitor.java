package com.carolinarollergirls.scoreboard.event;

import com.carolinarollergirls.scoreboard.*;

public interface SkaterEventVisitor extends PassEventVisitor
{
	public void skaterUnknownChange(Skater skater);

	public void skaterNameChange(Skater skater, String name);

	public void skaterNumberChange(Skater skater, String number);

	public void skaterPositionChange(Skater skater, String position);

	public void skaterLeadJammerChange(Skater skater, boolean leadJammer);

	public void skaterPenaltyBoxChange(Skater skater, boolean penaltyBox);

	public void skaterCurrentPassChange(Skater skater, int currentPass);
}
