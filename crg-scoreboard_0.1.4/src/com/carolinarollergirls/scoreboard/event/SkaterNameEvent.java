package com.carolinarollergirls.scoreboard.event;

import com.carolinarollergirls.scoreboard.*;

public class SkaterNameEvent extends SkaterEvent
{
	public SkaterNameEvent(Skater s, String n) {
		super(s);
		name = n;
	}

	public String getName() { return name; }

	public Object clone() { return new SkaterNameEvent(getSkater(), getName()); }

	public void accept(ScoreBoardEventVisitor v) { accept((SkaterEventVisitor)v); }
	public void accept(SkaterEventVisitor v) { v.skaterNameChange(getSkater(), getName()); }

	protected String name;
}
