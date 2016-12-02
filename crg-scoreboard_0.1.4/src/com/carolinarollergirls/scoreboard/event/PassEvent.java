package com.carolinarollergirls.scoreboard.event;

import com.carolinarollergirls.scoreboard.*;

public class PassEvent extends SkaterEvent
{
	public PassEvent(Pass p) {
		super(p.getSkater());
		pass = p;
	}

	public Pass getPass() { return pass; }

	public Object clone() { return new PassEvent(getPass()); }

	public void accept(ScoreBoardEventVisitor v) { accept((SkaterEventVisitor)v); }
	public void accept(SkaterEventVisitor v) { v.passUnknownChange(getPass()); }

	protected Pass pass;
}
