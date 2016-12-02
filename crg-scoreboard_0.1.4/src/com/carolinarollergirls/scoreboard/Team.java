package com.carolinarollergirls.scoreboard;

import java.util.*;

import com.carolinarollergirls.scoreboard.event.*;

public interface Team
{
	public ScoreBoard getScoreBoard();

	public void addTeamListener(TeamListener listener);
	public void removeTeamListener(TeamListener listener);

	public String getId();

	public String getName();

	public TeamLogo getTeamLogo();

	public int getScore();

	public int getTimeouts();

	public List<Skater> getSkaters();
	public Skater getSkater(String id) throws SkaterNotFoundException;

	/* These methods do not include Bench positions */
	public List<String> getPositions();
	public Skater getPosition(String position);

	public boolean isLeadJammer();

	public static final String ID_1 = "1";
	public static final String ID_2 = "2";

	public static final String POSITION_BENCH = "Bench";
	public static final String POSITION_JAMMER = "Jammer";
	public static final String POSITION_PIVOT = "Pivot";
	public static final String POSITION_BLOCKER1 = "Blocker1";
	public static final String POSITION_BLOCKER2 = "Blocker2";
	public static final String POSITION_BLOCKER3 = "Blocker3";
	public static final String[] POSITIONS = { POSITION_BENCH, POSITION_JAMMER, POSITION_PIVOT, POSITION_BLOCKER1, POSITION_BLOCKER2, POSITION_BLOCKER3 };
	public static final String[] FLOOR_POSITIONS = { POSITION_JAMMER, POSITION_PIVOT, POSITION_BLOCKER1, POSITION_BLOCKER2, POSITION_BLOCKER3 };
}
