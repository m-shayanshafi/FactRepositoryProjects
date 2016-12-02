package com.carolinarollergirls.scoreboard.event;

import com.carolinarollergirls.scoreboard.*;

public class SkaterPositionEvent extends SkaterEvent
{
	public SkaterPositionEvent(Skater s, String p) {
		super(s);
		position = p;
	}

	public String getPosition() { return position; }

	public Object clone() { return new SkaterPositionEvent(getSkater(), getPosition()); }

	public void accept(ScoreBoardEventVisitor v) { accept((SkaterEventVisitor)v); }
	public void accept(SkaterEventVisitor v) { v.skaterPositionChange(getSkater(), getPosition()); }

	protected String position;
}
