package com.carolinarollergirls.scoreboard.event;

import com.carolinarollergirls.scoreboard.*;

public class SkaterEvent extends TeamEvent
{
	public SkaterEvent(Skater s) {
		super(s.getTeam());
		skater = s;
	}

	public Skater getSkater() { return skater; }

	public Object clone() { return new SkaterEvent(getSkater()); }

	public void accept(ScoreBoardEventVisitor v) { accept((SkaterEventVisitor)v); }
	public void accept(SkaterEventVisitor v) { v.skaterUnknownChange(getSkater()); }

	protected Skater skater;
}
