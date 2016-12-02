package com.carolinarollergirls.scoreboard.event;

import com.carolinarollergirls.scoreboard.*;

public class ScoreBoardImageNameEvent extends ScoreBoardImageEvent
{
	public ScoreBoardImageNameEvent(ScoreBoardImage sbi, String n) {
		super(sbi);
		name = n;
	}

	public String getName() { return name; }

	public Object clone() { return new ScoreBoardImageNameEvent(getScoreBoardImage(), getName()); }

	public void accept(ScoreBoardEventVisitor v) { accept((ScoreBoardImageEventVisitor)v); }
	public void accept(ScoreBoardImageEventVisitor v) { v.scoreBoardImageNameChange(getScoreBoardImage(), getName()); }

	protected String name;
}
