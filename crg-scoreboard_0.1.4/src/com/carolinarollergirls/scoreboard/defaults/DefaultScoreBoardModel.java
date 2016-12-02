package com.carolinarollergirls.scoreboard.defaults;

import java.util.*;

import java.io.*;

import java.awt.image.*;

import javax.imageio.*;

import com.carolinarollergirls.scoreboard.*;
import com.carolinarollergirls.scoreboard.event.*;
import com.carolinarollergirls.scoreboard.model.*;

public class DefaultScoreBoardModel extends ScoreBoardListenerManager implements ScoreBoardModel
{
	public DefaultScoreBoardModel() {
		createModels();

		loadPolicies();

		loadImages();
	}

	protected void createModels() {
		createTeamModel(Team.ID_1, "Team 1");
		createTeamModel(Team.ID_2, "Team 2");

		createClockModel(Clock.ID_PERIOD, 1, 2, 0, 1800000, true);
		createClockModel(Clock.ID_JAM, 1, 999, 0, 120000, true);
		createClockModel(Clock.ID_LINEUP, 1, 999, 0, 3600000, false);
		createClockModel(Clock.ID_TIMEOUT, 1, 999, 0, 3600000, false);
		createClockModel(Clock.ID_INTERMISSION, 0, 999, 0, 900000, true);
		createClockModel(Clock.ID_COUNTDOWN, 1, 999, 0, 30000, true);
		createClockModel(Clock.ID_PENALTY_TEAM1_B1, 1, 999, 0, 60000, true);
		createClockModel(Clock.ID_PENALTY_TEAM1_B2, 1, 999, 0, 60000, true);
		createClockModel(Clock.ID_PENALTY_TEAM1_B3, 1, 999, 0, 60000, true);
		createClockModel(Clock.ID_PENALTY_TEAM1_J, 1, 999, 0, 60000, true);
		createClockModel(Clock.ID_PENALTY_TEAM2_B1, 1, 999, 0, 60000, true);
		createClockModel(Clock.ID_PENALTY_TEAM2_B2, 1, 999, 0, 60000, true);
		createClockModel(Clock.ID_PENALTY_TEAM2_B3, 1, 999, 0, 60000, true);
		createClockModel(Clock.ID_PENALTY_TEAM2_J, 1, 999, 0, 60000, true);
	}

	protected void loadPolicies() {
		Enumeration keys = ScoreBoardManager.getProperties().propertyNames();

		while (keys.hasMoreElements()) {
			String key = keys.nextElement().toString();
			if (!key.startsWith(POLICY_KEY+"."))
				continue;

			String name = ScoreBoardManager.getProperties().getProperty(key);

			try {
				PolicyModel policyModel = (PolicyModel)Class.forName(name).newInstance();
				addPolicyModel(policyModel);
			} catch ( Exception e ) {
				System.err.println("Could not load ScoreBoard policy : " + e.getMessage());
			}
		}
	}

	protected void loadImages() {
		Properties p = ScoreBoardManager.getProperties();
		String imagesTopDir = p.getProperty(IMAGES_DIR);
		if (null == imagesTopDir)
			return;

		Enumeration keys = p.propertyNames();

		while (keys.hasMoreElements()) {
			String key = keys.nextElement().toString();
			if (!key.startsWith(IMAGES_DIR+"."))
				continue;

			String type = key.replaceFirst(IMAGES_DIR+".", "");
			String dir = p.getProperty(key);

			scoreBoardImageAddUpdaters.add(new ScoreBoardImageAddUpdater(new File(imagesTopDir + "/" + dir), imagesTopDir, dir, type));
		}

		scoreBoardImageRemoveUpdaters.add(new ScoreBoardImageRemoveUpdater());

		updateScoreBoardImageModels();
	}

	public void updateScoreBoardImageModels() {
		Iterator<Runnable> updaters = scoreBoardImageRemoveUpdaters.iterator();
		while (updaters.hasNext())
			updaters.next().run();

		updaters = scoreBoardImageAddUpdaters.iterator();
		while (updaters.hasNext())
			updaters.next().run();
	}

	public ScoreBoard getScoreBoard() { return this; }

	public void startJam() {
		synchronized (runLock) {
			if (!getClock(Clock.ID_JAM).isRunning()) {
				ClockModel pc = getClockModel(Clock.ID_PERIOD);
				ClockModel jc = getClockModel(Clock.ID_JAM);
				ClockModel tc = getClockModel(Clock.ID_TIMEOUT);
				lineupClockWasRunning = getClockModel(Clock.ID_LINEUP).isRunning();

//FIXME - change to policies
				// If Period Clock is at end, increment number and reset time
				if (pc.getTime() == (pc.isCountDirectionDown() ? pc.getMinimumTime() : pc.getMaximumTime())) {
					pc.changeNumber(1);
					pc.resetTime();
				}
				periodClockWasRunning = pc.isRunning();
				pc.start();

				// If Jam Clock is not at start (2:00), increment number and reset time
				if (jc.getTime() != (jc.isCountDirectionDown() ? jc.getMaximumTime() : jc.getMinimumTime()))
					jc.changeNumber(1);
				jc.resetTime();
				jc.start();

				timeoutClockWasRunning = tc.isRunning();
				tc.stop();
			}
		}
	}
	public void stopJam() {
		synchronized (runLock) {
			if (getClockModel(Clock.ID_JAM).isRunning()) {
				getClockModel(Clock.ID_JAM).stop();
			}
		}
	}

	public void timeout() { timeout(null); }
	public void timeout(TeamModel team) {
		synchronized (runLock) {
			setTimeoutOwner(null==team?"":team.getId());
			if (!getClockModel(Clock.ID_TIMEOUT).isRunning()) {
//FIXME - change to policy?
				getClockModel(Clock.ID_PERIOD).stop();
				jamClockWasRunning = getClockModel(Clock.ID_JAM).isRunning();
				lineupClockWasRunning = getClockModel(Clock.ID_LINEUP).isRunning();
				getClockModel(Clock.ID_JAM).stop();
				getClockModel(Clock.ID_TIMEOUT).resetTime();
				getClockModel(Clock.ID_TIMEOUT).start();
			}
		}
	}

	public void unStartJam() {
		synchronized (runLock) {
			if (!getClock(Clock.ID_JAM).isRunning())
				return;

			if (lineupClockWasRunning)
				getClockModel(Clock.ID_LINEUP).unstop();
			if (timeoutClockWasRunning)
				getClockModel(Clock.ID_TIMEOUT).unstop();
			if (!periodClockWasRunning)
				getClockModel(Clock.ID_PERIOD).unstart();
			getClockModel(Clock.ID_JAM).unstart();
		}
	}
	public void unStopJam() {
		synchronized (runLock) {
			if (getClock(Clock.ID_JAM).isRunning())
				return;

			getClockModel(Clock.ID_LINEUP).stop();
			getClockModel(Clock.ID_JAM).unstop();
		}
	}
	public void unTimeout() {
		synchronized (runLock) {
			if (!getClock(Clock.ID_TIMEOUT).isRunning())
				return;

			if (lineupClockWasRunning)
				getClockModel(Clock.ID_LINEUP).unstop();
			if (jamClockWasRunning)
				getClockModel(Clock.ID_JAM).unstop();
			getClockModel(Clock.ID_PERIOD).unstop();
			getClockModel(Clock.ID_TIMEOUT).unstart();
		}
	}

	public List<ClockModel> getClockModels() { return new ArrayList<ClockModel>(clocks.values()); }
	public List<TeamModel> getTeamModels() { return new ArrayList<TeamModel>(teams.values()); }
	public List<ScoreBoardImageModel> getScoreBoardImageModels() { return new ArrayList<ScoreBoardImageModel>(scoreBoardImages.values()); }
	public List<PolicyModel> getPolicyModels() { return new ArrayList<PolicyModel>(policies.values()); }

	public List<Clock> getClocks() { return new ArrayList<Clock>(getClockModels()); }
	public List<Team> getTeams() { return new ArrayList<Team>(getTeamModels()); }
	public List<ScoreBoardImage> getScoreBoardImages() { return new ArrayList<ScoreBoardImage>(getScoreBoardImageModels()); }
	public List<Policy> getPolicies() { return new ArrayList<Policy>(getPolicyModels()); }

	public Clock getClock(String id) { return getClockModel(id).getClock(); }
	public Team getTeam(String id) { return getTeamModel(id).getTeam(); }
	public ScoreBoardImage getScoreBoardImage(String id) { try { return getScoreBoardImageModel(id).getScoreBoardImage(); } catch ( NullPointerException npE ) { return null; } }
	public Policy getPolicy(String id) { try { return getPolicyModel(id).getPolicy(); } catch ( NullPointerException npE ) { return null; } }

	public List<ScoreBoardImage> getScoreBoardImages(String type) { return new ArrayList<ScoreBoardImage>(getScoreBoardImageModels(type)); }
	public List<ScoreBoardImageModel> getScoreBoardImageModels(String type) {
		List<ScoreBoardImageModel> l = new LinkedList<ScoreBoardImageModel>(scoreBoardImages.values());
		for (int i=0; i<l.size(); i++) {
			ScoreBoardImageModel sbim = l.get(i);
			if (!type.equals(sbim.getType())) {
				l.remove(sbim);
				i--;
			}
		}
		return l;
	}

	public ClockModel getClockModel(String id) {
		synchronized (clocks) {
// FIXME - don't auto-create!  return null instead - or throw exception.  Need to update all callers to handle first.
			if (!clocks.containsKey(id))
				createClockModel(id, defaultMinNum, defaultMaxNum, defaultMinTime, defaultMaxTime, defaultCountDown);

			return clocks.get(id);
		}
	}

	public TeamModel getTeamModel(String id) {
		synchronized (teams) {
// FIXME - don't auto-create!  return null instead - or throw exception.  Need to update all callers to handle first.
			if (!teams.containsKey(id))
				createTeamModel(id, id);

			return teams.get(id);
		}
	}

	public ScoreBoardImageModel getScoreBoardImageModel(String id) {
		synchronized (scoreBoardImages) {
			return scoreBoardImages.get(id);
		}
	}

	public void addScoreBoardImageModel(ScoreBoardImageModel model) throws IllegalArgumentException {
		if ((model.getId() == null) || (model.getId().equals("")))
			throw new IllegalArgumentException("ScoreBoardImageModel has null or empty Id");

		synchronized (scoreBoardImages) {
			scoreBoardImages.put(model.getId(), model);
			model.addScoreBoardImageListener(scoreBoardImageListener);
			scoreBoardChange(new ScoreBoardAddScoreBoardImageEvent(this, model));
		}
	}

	public void removeScoreBoardImageModel(String id) {
		synchronized (scoreBoardImages) {
			ScoreBoardImageModel model = scoreBoardImages.remove(id);
			if (null != model) {
				model.removeScoreBoardImageListener(scoreBoardImageListener);
				scoreBoardChange(new ScoreBoardRemoveScoreBoardImageEvent(this, model));
			}
		}
	}

	public PolicyModel getPolicyModel(String id) {
		synchronized (policies) {
			return policies.get(id);
		}
	}
	public void addPolicyModel(PolicyModel model) throws IllegalArgumentException {
		if ((model.getId() == null) || (model.getId().equals("")))
			throw new IllegalArgumentException("PolicyModel has null or empty Id");

		try {
			model.setScoreBoardModel(this);
		} catch ( Exception e ) {
			throw new IllegalArgumentException("Exception while setting ScoreBoardModel on PolicyModel : "+e.getMessage());
		}

		synchronized (policies) {
			policies.put(model.getId(), model);
			model.addPolicyListener(policyListener);
		}
	}
	public void removePolicyModel(PolicyModel model) {
		synchronized (policies) {
			model.removePolicyListener(policyListener);
			policies.remove(model.getId());
		}
	}

	public String getTimeoutOwner() { return timeoutOwner; }
	public void setTimeoutOwner(String owner) {
		synchronized (timeoutOwnerLock) {
			timeoutOwner = owner;
			scoreBoardChange(new ScoreBoardTimeoutOwnerEvent(this, owner));
		}
	}

	protected void createClockModel(String id, int minNum, int maxNum, long minTime, long maxTime, boolean down) {
		createClockModel(id, id, minNum, maxNum, minTime, maxTime, down);
	}

	protected void createClockModel(String id, String name, int minNum, int maxNum, long minTime, long maxTime, boolean down) {
		if ((id == null) || (id.equals("")))
			return;

		minNum = getIntProp(CLOCK+"."+name+"."+NUM+"."+MIN, minNum);
		maxNum = getIntProp(CLOCK+"."+name+"."+NUM+"."+MAX, maxNum);

		minTime = getLongProp(CLOCK+"."+name+"."+TIME+"."+MIN, minTime);
		maxTime = getLongProp(CLOCK+"."+name+"."+TIME+"."+MAX, maxTime);

		down = getBooleanProp(CLOCK+"."+name+"."+COUNTDOWN, down);

		ClockModel model = new DefaultClockModel(this, id, name, minNum, maxNum, minTime, maxTime, down);
		model.addClockListener(clockListener);
		clocks.put(id, model);
	}

	protected void createTeamModel(String id, String name) {
		if ((id == null) || (id.equals("")))
			return;

		TeamModel model = new DefaultTeamModel(this, id, name, null, 0, 3);
		model.addTeamListener(teamListener);
		teams.put(id, model);
	}

	protected int getIntProp(String key, int def) {
		try {
			return Integer.parseInt(ScoreBoardManager.getProperties().getProperty(key));
		} catch ( Exception e ) {
			return def;
		}
	}

	protected long getLongProp(String key, long def) {
		try {
			return Long.parseLong(ScoreBoardManager.getProperties().getProperty(key));
		} catch ( Exception e ) {
			return def;
		}
	}

	protected boolean getBooleanProp(String key, boolean def) {
		try {
			String prop = ScoreBoardManager.getProperties().getProperty(key);
			if (null == prop)
				return def;
			else
				return Boolean.parseBoolean(prop.trim());
		} catch ( Exception e ) {
			return def;
		}
	}

	protected HashMap<String,ClockModel> clocks = new HashMap<String,ClockModel>();
	protected HashMap<String,TeamModel> teams = new HashMap<String,TeamModel>();
	protected HashMap<String,ScoreBoardImageModel> scoreBoardImages = new HashMap<String,ScoreBoardImageModel>();
	protected HashMap<String,PolicyModel> policies = new HashMap<String,PolicyModel>();

	protected Object runLock = new Object();

	protected String timeoutOwner = "";
	protected Object timeoutOwnerLock = new Object();

	protected boolean periodClockWasRunning = false;
	protected boolean jamClockWasRunning = false;
	protected boolean lineupClockWasRunning = false;
	protected boolean timeoutClockWasRunning = false;

	protected int defaultMinNum = 1;
	protected int defaultMaxNum = 999;
	protected int defaultMinTime = 0;
	protected int defaultMaxTime = 900000;
	protected boolean defaultCountDown = true;

	protected ClockListener clockListener = new ClockListener() {
			public void clockChange(ClockEvent event) { scoreBoardChange(event); }
		};
	protected TeamListener teamListener = new TeamListener() {
			public void teamChange(TeamEvent event) { scoreBoardChange(event); }
		};
	protected ScoreBoardImageListener scoreBoardImageListener = new ScoreBoardImageListener() {
			public void scoreBoardImageChange(ScoreBoardImageEvent event) { scoreBoardChange(event); }
		};
	protected PolicyListener policyListener = new PolicyListener() {
			public void policyChange(PolicyEvent event) { scoreBoardChange(event); }
		};

	protected List<Runnable> scoreBoardImageAddUpdaters = new ArrayList<Runnable>();
	protected List<Runnable> scoreBoardImageRemoveUpdaters = new ArrayList<Runnable>();

	public static final String CLOCK = DefaultScoreBoardModel.class.getName() + ".clock";

	public static final String NUM = "number";
	public static final String TIME = "time";

	public static final String MIN = "min";
	public static final String MAX = "max";

	public static final String COUNTDOWN = "countdown";

	public static final String IMAGES_DIR = DefaultScoreBoardModel.class.getName() + ".images.directory";

	public static final String POLICY_KEY = DefaultScoreBoardModel.class.getName() + ".policy";



	protected class ScoreBoardImageRemoveUpdater implements Runnable
	{
		public void run() {
			synchronized (scoreBoardImages) {
				Iterator<ScoreBoardImageModel> imgs = getScoreBoardImageModels().iterator();
				List<ScoreBoardImageModel> removeImages = new LinkedList<ScoreBoardImageModel>();
				while (imgs.hasNext()) {
					ScoreBoardImageModel sbiM = imgs.next();
					if (!(new File(sbiM.getDirectory() + "/" + sbiM.getFilename()).exists()))
						removeImages.add(sbiM);
				}
				Iterator<ScoreBoardImageModel> removeIterator = removeImages.iterator();
				while (removeIterator.hasNext())
					removeScoreBoardImageModel(removeIterator.next().getId());
			}
		}
	}

	protected class ScoreBoardImageAddUpdater implements Runnable
	{
		public ScoreBoardImageAddUpdater(File d, String topD, String dName, String t) {
			directory = d;
			topDirectory = topD;
			directoryName = dName;
			type = t;
		}

		public void run() {
			File[] files = directory.listFiles();
			for (int i=0; i<files.length; i++) {
				try {
					File f = files[i];
					if (f.isDirectory())
						continue;
					String name = f.getName();
					String id = type + "/" + name;
					String filename = "/" + directoryName + "/" + name;
					synchronized (scoreBoardImages) {
						if (!scoreBoardImages.containsKey(id))
							addScoreBoardImageModel(new DefaultScoreBoardImageModel(DefaultScoreBoardModel.this, id, type, topDirectory, filename, name));
					}
				} catch ( Exception e ) {
					System.err.println("Could not add image type "+type+" file "+files[i].getName());
				}
			}
		}

		public boolean running = true;

		protected File directory;
		protected String topDirectory;
		protected String type;
		protected String directoryName;
	}
}

