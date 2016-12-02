package com.carolinarollergirls.scoreboard.event;

import com.carolinarollergirls.scoreboard.*;

public interface ScoreBoardImageEventVisitor
{
	public void scoreBoardImageUnknownChange(ScoreBoardImage scoreBoardImage);

	public void scoreBoardImageNameChange(ScoreBoardImage scoreBoardImage, String name);
}
