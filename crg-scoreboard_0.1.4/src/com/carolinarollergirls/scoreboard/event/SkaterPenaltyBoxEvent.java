package com.carolinarollergirls.scoreboard.event;

import com.carolinarollergirls.scoreboard.*;

public class SkaterPenaltyBoxEvent extends SkaterEvent
{
	public SkaterPenaltyBoxEvent(Skater s, boolean box) {
		super(s);
		penaltyBox = box;
	}

	public boolean isPenaltyBox() { return penaltyBox; }

	public Object clone() { return new SkaterPenaltyBoxEvent(getSkater(), isPenaltyBox()); }

	public void accept(ScoreBoardEventVisitor v) { accept((SkaterEventVisitor)v); }
	public void accept(SkaterEventVisitor v) { v.skaterPenaltyBoxChange(getSkater(), isPenaltyBox()); }

	protected boolean penaltyBox;
}
