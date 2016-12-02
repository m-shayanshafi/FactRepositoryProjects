package com.carolinarollergirls.scoreboard.event;

import com.carolinarollergirls.scoreboard.*;

public interface ScoreBoardEventVisitor extends PolicyEventVisitor,ClockEventVisitor,TeamEventVisitor
{
	public void scoreBoardUnknownChange(ScoreBoard scoreBoard);

	public void scoreBoardAddScoreBoardImage(ScoreBoard scoreBoard, ScoreBoardImage scoreBoardImage);
	public void scoreBoardRemoveScoreBoardImage(ScoreBoard scoreBoard, ScoreBoardImage scoreBoardImage);

	public void scoreBoardTimeoutOwnerChange(ScoreBoard scoreBoard, String owner);
}
