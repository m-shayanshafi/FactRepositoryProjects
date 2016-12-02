package com.carolinarollergirls.scoreboard.defaults;

import java.util.*;
import java.util.concurrent.*;

import java.awt.image.*;

import com.carolinarollergirls.scoreboard.*;
import com.carolinarollergirls.scoreboard.event.*;
import com.carolinarollergirls.scoreboard.model.*;

public class DefaultTeamModel extends TeamListenerManager implements TeamModel
{
	public DefaultTeamModel(ScoreBoardModel sbm, String i, String n, String l, int s, int to) {
		scoreBoardModel = sbm;
		id = i;
		setName(n);
		setScore(s);
		setTimeouts(to);
		setLeadJammer(false);
		teamLogoModel = new DefaultTeamLogoModel(this, l);
		teamLogoModel.addTeamLogoListener(teamLogoListener);
	}

	public ScoreBoard getScoreBoard() { return scoreBoardModel.getScoreBoard(); }
	public ScoreBoardModel getScoreBoardModel() { return scoreBoardModel; }

	public String getId() { return id; }

	public Team getTeam() { return this; }

	public String getName() { return name; }
	public void setName(String n) {
		name = n;
		teamChange(new TeamNameEvent(getTeam(), name));
	}

	public TeamLogo getTeamLogo() { return getTeamLogoModel().getTeamLogo(); }
	public TeamLogoModel getTeamLogoModel() { return teamLogoModel; }

	public void timeout() {
		if (getTimeouts() > 0) {
			changeTimeouts(-1);
			getScoreBoardModel().timeout(this);
		}
	}

	public int getScore() { return score; }
	public void setScore(int s) {
		if (0 > s)
			s = 0;
		score = s;
		teamChange(new TeamScoreEvent(getTeam(), score));
	}
	public void changeScore(int c) {
		setScore(getScore() + c);
	}

	public int getTimeouts() { return timeouts; }
//FIXME - ad MinimumTimeout and MaximumTimeout instead of hardcoding 0 and 3
	public void setTimeouts(int t) {
		if (0 > t)
			t = 0;
		if (3 < t)
			t = 3;
		timeouts = t;
		teamChange(new TeamTimeoutsEvent(getTeam(), timeouts));
	}
	public void changeTimeouts(int c) {
		setTimeouts(getTimeouts() + c);
	}

	public List<SkaterModel> getSkaterModels() { return Collections.unmodifiableList(new ArrayList<SkaterModel>(skaters.values())); }
	public List<Skater> getSkaters() {
		List<Skater> list = new ArrayList<Skater>(skaters.size());
		Iterator<SkaterModel> i = getSkaterModels().iterator();
		while (i.hasNext())
			list.add(i.next().getSkater());
		return Collections.unmodifiableList(list);
	}

	public Skater getSkater(String id) throws SkaterNotFoundException { return getSkaterModel(id); }
	public SkaterModel getSkaterModel(String id) throws SkaterNotFoundException {
		SkaterModel sm = null;
		synchronized (skaterLock) {
			sm = (SkaterModel)skaters.get(id);
		}
		if (null == sm)
			throw new SkaterNotFoundException(id);
		return sm;
	}
	public void addSkaterModel(SkaterModel skater) {
		synchronized (skaterLock) {
			if (null == skater.getId() || "".equals(skater.getId()) || skaters.containsKey(skater.getId()))
				return;

			skaters.put(skater.getId(), skater);
			skater.addSkaterListener(skaterListener);
			teamChange(new TeamAddSkaterEvent(getTeam(), skater));
		}
	}
	public void removeSkaterModel(String id) throws SkaterNotFoundException {
		SkaterModel sm;
		synchronized (skaterLock) {
			sm = getSkaterModel(id);
			sm.removeSkaterListener(skaterListener);
			skaters.remove(id);
			clearPosition(sm);
			teamChange(new TeamRemoveSkaterEvent(getTeam(), sm));
		}
	}

	public List<String> getPositions() { return Collections.unmodifiableList(Arrays.asList(FLOOR_POSITIONS)); }
	public SkaterModel getPositionModel(String position) { return positions.get(position); }
	public Skater getPosition(String position) { return getPositionModel(position); }
	public void setPosition(String p, String id) throws SkaterNotFoundException {
		if (id == null || id.equals(""))
			clearPosition(p);

		SkaterModel sm;
		SkaterModel psm;
		String position = checkPosition(p);
		synchronized (skaterLock) {
			if (settingPosition) /* prevent infinte loop of calling between this and skater.setPosition() */
				return;
			try {
				settingPosition = true;
				sm = getSkaterModel(id);
				clearPosition(sm);
				if (!position.equals(POSITION_BENCH)) {
					if (null != (psm = positions.put(position, sm)))
						psm.setPosition(POSITION_BENCH);
					teamChange(new TeamPositionEvent(getTeam(), position, sm));
				}
				sm.setPosition(position);
			} finally {
				settingPosition = false;
			}
		}
	}
	public void clearPosition(String position) {
		SkaterModel sm;
		synchronized (skaterLock) {
			if (null != (sm = positions.remove(position))) {
				sm.setPosition(POSITION_BENCH);
				teamChange(new TeamPositionEvent(getTeam(), position, null));
			}
		}
	}
	protected void clearPosition(Skater s) {
		synchronized (skaterLock) {
			clearPosition(getPosition(s));
		}
	}
	protected String getPosition(Skater s) {
		synchronized (skaterLock) {
			if (positions.containsValue(s)) {
				Iterator<String> i = positions.keySet().iterator();
				while (i.hasNext()) {
					String p = i.next();
					if (s == positions.get(p))
						return p;
				}
			}
		}
		return POSITION_BENCH;
	}
//FIXME - should this throw a PositionNotFoundException instead?
	protected String checkPosition(String p) {
		if (Arrays.asList(POSITIONS).contains(p))
			return p;
		else
			return POSITION_BENCH;
	}

	public boolean isLeadJammer() { return leadJammer; }
	public void setLeadJammer(boolean lead) {
		synchronized (skaterLock) {
			leadJammer = lead;
			teamChange(new TeamLeadJammerEvent(getTeam(), lead));
			try {
				SkaterModel sM = getPositionModel(POSITION_JAMMER);
				if (lead != sM.isLeadJammer())
					sM.setLeadJammer(lead);
			} catch ( NullPointerException npE ) {
				/* No Jammer set - probably not tracking Skaters */
			}
		}
	}


	protected ScoreBoardModel scoreBoardModel;

	protected String id;
	protected String name;
	protected TeamLogoModel teamLogoModel;
	protected int score;
	protected int timeouts;
	protected boolean leadJammer;

	protected Hashtable<String,SkaterModel> skaters = new Hashtable<String,SkaterModel>();
	protected Hashtable<String,SkaterModel> positions = new Hashtable<String,SkaterModel>();
	protected Object skaterLock = new Object();
	protected boolean settingPosition = false;

	protected TeamLogoListener teamLogoListener = new TeamLogoListener() {
			public void teamLogoChange(TeamLogoEvent event) { teamChange(event); }
		};

	protected SkaterListener skaterListener = new SkaterListener() {
			public void skaterChange(SkaterEvent event) { teamChange(event); }
		};
}
