package com.carolinarollergirls.scoreboard.event;

import com.carolinarollergirls.scoreboard.*;

public interface PassEventVisitor
{
	public void passUnknownChange(Pass pass);

	public void passScoreChange(Pass pass, int score);
}
