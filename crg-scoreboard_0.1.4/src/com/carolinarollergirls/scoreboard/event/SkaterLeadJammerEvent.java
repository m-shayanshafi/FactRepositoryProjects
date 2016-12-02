package com.carolinarollergirls.scoreboard.event;

import com.carolinarollergirls.scoreboard.*;

public class SkaterLeadJammerEvent extends SkaterEvent
{
	public SkaterLeadJammerEvent(Skater s, boolean lead) {
		super(s);
		leadJammer = lead;
	}

	public boolean isLeadJammer() { return leadJammer; }

	public Object clone() { return new SkaterLeadJammerEvent(getSkater(), isLeadJammer()); }

	public void accept(ScoreBoardEventVisitor v) { accept((SkaterEventVisitor)v); }
	public void accept(SkaterEventVisitor v) { v.skaterLeadJammerChange(getSkater(), isLeadJammer()); }

	protected boolean leadJammer;
}
