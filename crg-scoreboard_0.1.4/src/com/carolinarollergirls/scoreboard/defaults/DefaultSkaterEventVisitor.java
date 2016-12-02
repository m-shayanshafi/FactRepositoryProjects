package com.carolinarollergirls.scoreboard.defaults;

import com.carolinarollergirls.scoreboard.*;
import com.carolinarollergirls.scoreboard.event.*;

public class DefaultSkaterEventVisitor implements SkaterEventVisitor
{
	public DefaultSkaterEventVisitor() { }

	public DefaultSkaterEventVisitor(Skater skater) {
		SkaterListener listener = new SkaterListener() {
				public void skaterChange(SkaterEvent event) {
					event.accept(DefaultSkaterEventVisitor.this);
				}
			};
		skater.addSkaterListener(listener);
	}

	public void skaterUnknownChange(Skater skater) { }

	public void skaterNameChange(Skater skater, String name) { }

	public void skaterNumberChange(Skater skater, String number) { }

	public void skaterPositionChange(Skater skater, String position) { }

	public void skaterLeadJammerChange(Skater skater, boolean leadJammer) { }

	public void skaterPenaltyBoxChange(Skater skater, boolean penaltyBox) { }

	public void skaterCurrentPassChange(Skater skater, int currentPass) { }

	public void passUnknownChange(Pass pass) { }

	public void passScoreChange(Pass pass, int score) { }
}
