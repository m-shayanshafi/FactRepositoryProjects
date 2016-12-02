package com.carolinarollergirls.scoreboard.event;

import com.carolinarollergirls.scoreboard.*;

public class SkaterCurrentPassEvent extends SkaterEvent
{
	public SkaterCurrentPassEvent(Skater s, int p) {
		super(s);
		currentPass = p;
	}

	public int getCurrentPass() { return currentPass; }

	public Object clone() { return new SkaterCurrentPassEvent(getSkater(), getCurrentPass()); }

	public void accept(ScoreBoardEventVisitor v) { accept((SkaterEventVisitor)v); }
	public void accept(SkaterEventVisitor v) { v.skaterCurrentPassChange(getSkater(), getCurrentPass()); }

	protected int currentPass;
}
