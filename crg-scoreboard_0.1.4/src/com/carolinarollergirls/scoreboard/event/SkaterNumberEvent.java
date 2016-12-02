package com.carolinarollergirls.scoreboard.event;

import com.carolinarollergirls.scoreboard.*;

public class SkaterNumberEvent extends SkaterEvent
{
	public SkaterNumberEvent(Skater s, String n) {
		super(s);
		number = n;
	}

	public String getNumber() { return number; }

	public Object clone() { return new SkaterNumberEvent(getSkater(), getNumber()); }

	public void accept(ScoreBoardEventVisitor v) { accept((SkaterEventVisitor)v); }
	public void accept(SkaterEventVisitor v) { v.skaterNumberChange(getSkater(), getNumber()); }

	protected String number;
}
