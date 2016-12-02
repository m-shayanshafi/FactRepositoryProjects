package com.carolinarollergirls.scoreboard.defaults;

import java.util.*;

import com.carolinarollergirls.scoreboard.*;
import com.carolinarollergirls.scoreboard.event.*;
import com.carolinarollergirls.scoreboard.model.*;

public class DefaultSkaterModel extends SkaterListenerManager implements SkaterModel
{
	public DefaultSkaterModel(TeamModel tm, String i, String n, String num, String p) {
		teamModel = tm;
		id = i;
		setName(n);
		setNumber(num);
		setPosition(p);
	}

	public Team getTeam() { return teamModel.getTeam(); }
	public TeamModel getTeamModel() { return teamModel; }

	public String getId() { return id; }

	public Skater getSkater() { return this; }

	public String getName() { return name; }
	public void setName(String n) {
		synchronized (nameLock) {
			name = n;
			skaterChange(new SkaterNameEvent(getSkater(), name));
		}
	}

	public String getNumber() { return number; }
	public void setNumber(String n) {
		synchronized (numberLock) {
			number = n;
			skaterChange(new SkaterNumberEvent(getSkater(), number));
		}
	}

	public String getPosition() { return position; }
	public void setPosition(String p) {
		synchronized (positionLock) {
			if (!Team.POSITION_JAMMER.equals(p))
				setLeadJammer(false);
			position = checkPosition(p);
			if (getTeam().isLeadJammer())
				setLeadJammer(true);
			try {
				getTeamModel().setPosition(position, id);
			} catch ( SkaterNotFoundException snfE ) {
				/* This means we've been removed from our parent Team...not much we can do. */
			}
			skaterChange(new SkaterPositionEvent(getSkater(), position));
		}
	}
//FIXME - should this throw a PositionNotFoundException instead?
	protected String checkPosition(String p) {
		if (Arrays.asList(Team.POSITIONS).contains(p))
			return p;
		else
			return Team.POSITION_BENCH;
	}

	public boolean isLeadJammer() { return leadJammer; }
	public void setLeadJammer(boolean lead) {
		synchronized (positionLock) {
			if ((!Team.POSITION_JAMMER.equals(position)) && lead)
				return;
			leadJammer = lead;
			skaterChange(new SkaterLeadJammerEvent(this, leadJammer));

			if (Team.POSITION_JAMMER.equals(position) && (leadJammer != getTeamModel().isLeadJammer()))
				getTeamModel().setLeadJammer(leadJammer);
		}
	}

	public boolean isPenaltyBox() { return penaltyBox; }
	public void setPenaltyBox(boolean box) {
		synchronized (penaltyBoxLock) {
			penaltyBox = box;
			skaterChange(new SkaterPenaltyBoxEvent(this, penaltyBox));
		}
	}

	public int getCurrentPass() { return currentPass; }
	public void setCurrentPass(int p) {
		synchronized (passLock) {
			currentPass = p;
			skaterChange(new SkaterCurrentPassEvent(this, currentPass));
		}
	}
	public Pass getPass(int p) { return getPassModel(p); }
	public List<Integer> getPasses() { return new ArrayList<Integer>(passes.keySet()); }
	public PassModel getPassModel(int pass) {
		Integer p = Integer.valueOf(pass);
		synchronized (passesLock) {
			if (!passes.containsKey(p))
				passes.put(p, new DefaultPassModel(this, pass));

			return passes.get(p);
		}
	}

	protected TeamModel teamModel;

	protected String id;
	protected String name;
	protected String number;
	protected String position;
	protected boolean leadJammer = false;
	protected boolean penaltyBox = false;
	protected int currentPass = 1;
	protected HashMap<Integer,PassModel> passes = new HashMap<Integer,PassModel>();

	protected Object nameLock = new Object();
	protected Object numberLock = new Object();
	protected Object positionLock = new Object();
	protected Object penaltyBoxLock = new Object();
	protected Object passLock = new Object();
	protected Object passesLock = new Object();
}
