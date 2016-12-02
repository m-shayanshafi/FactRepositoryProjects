package com.carolinarollergirls.scoreboard.defaults;

import java.util.*;

import com.carolinarollergirls.scoreboard.*;
import com.carolinarollergirls.scoreboard.model.*;
import com.carolinarollergirls.scoreboard.event.*;
import com.carolinarollergirls.scoreboard.policy.ClockSyncPolicy;

public class DefaultClockModel extends ClockListenerManager implements ClockModel
{
	public DefaultClockModel(ScoreBoardModel sbm, String i, String n, int minN, int maxN, long minT, long maxT, boolean dir) {
		scoreBoardModel = sbm;
		id = i;

		setName(n);

		setMinimumNumber(minN);
		setMaximumNumber(maxN);
		setNumber(minN);

		setCountDirectionDown(dir);
		setMinimumTime(minT);
		setMaximumTime(maxT);
		setTime(dir?maxT:minT);
	}

	public ScoreBoard getScoreBoard() { return scoreBoardModel.getScoreBoard(); }
	public ScoreBoardModel getScoreBoardModel() { return scoreBoardModel; }

	public String getId() { return id; }

	public Clock getClock() { return this; }

	public String getName() { return name; }
	public void setName(String n) {
		synchronized (nameLock) {
			name = n;
			clockChange(new ClockNameEvent(getClock(), name));
		}
	}

	public int getNumber() { return number; }
	public void setNumber(int n) {
		synchronized (numberLock) {
			number = checkNewNumber(n);
			clockChange(new ClockNumberEvent(getClock(), number));
		}
	}
	public void changeNumber(int change) {
		synchronized (numberLock) {
			number = checkNewNumber(number + change);
			clockChange(new ClockNumberEvent(getClock(), number));
		}
	}
	protected int checkNewNumber(int n) {
		if (n < minimumNumber)
			return minimumNumber;
		else if (n > maximumNumber)
			return maximumNumber;
		else
			return n;
	}

	public int getMinimumNumber() { return minimumNumber; }
	public void setMinimumNumber(int n) {
		synchronized (numberLock) {
			minimumNumber = n;
			if (getNumber() != checkNewNumber(getNumber()))
				setNumber(getNumber());
			clockChange(new ClockMinimumNumberEvent(getClock(), minimumNumber));
		}
	}

	public int getMaximumNumber() { return maximumNumber; }
	public void setMaximumNumber(int n) {
		synchronized (numberLock) {
			maximumNumber = n;
			if (getNumber() != checkNewNumber(getNumber()))
				setNumber(getNumber());
			clockChange(new ClockMaximumNumberEvent(getClock(), maximumNumber));
		}
	}

	public long getTime() { return time; }
	public void setTime(long ms) {
		boolean doStop;
		synchronized (timeLock) {
			if (isRunning() && isSyncTime())
				ms = ((ms / 1000) * 1000) + (time % 1000);
			time = checkNewTime(ms);
			clockChange(new ClockTimeEvent(getClock(), time));
			doStop = checkStop();
		}
		if (doStop)
			stop();
	}
	public void changeTime(long change) { _changeTime(change, true); }
	protected void _changeTime(long change, boolean sync) {
		boolean doStop;
		synchronized (timeLock) {
			if (sync && isRunning() && isSyncTime())
				change = ((change / 1000) * 1000);
			time = checkNewTime(time + change);
			clockChange(new ClockTimeEvent(getClock(), time));
			doStop = checkStop();
		}
		if (doStop)
			stop();
	}
	public void resetTime() {
		if (isCountDirectionDown())
			setTime(getMaximumTime());
		else
			setTime(getMinimumTime());
	}
	protected long checkNewTime(long ms) {
		if (ms < minimumTime)
			return minimumTime;
		else if (ms > maximumTime)
			return maximumTime;
		else
			return ms;
	}
	protected boolean checkStop() {
		return (getTime() == (isCountDirectionDown() ? getMinimumTime() : getMaximumTime()));
	}

	public long getMinimumTime() { return minimumTime; }
	public void setMinimumTime(long ms) {
		synchronized (timeLock) {
			minimumTime = ms;
			if (getTime() != checkNewTime(getTime()))
				setTime(getTime());
			clockChange(new ClockMinimumTimeEvent(getClock(), minimumTime));
		}
	}
	public long getMaximumTime() { return maximumTime; }
	public void setMaximumTime(long ms) {
		synchronized (timeLock) {
			maximumTime = ms;
			if (getTime() != checkNewTime(getTime()))
				setTime(getTime());
			clockChange(new ClockMaximumTimeEvent(getClock(), maximumTime));
		}
	}

	public boolean isCountDirectionDown() { return countDown; }
	public void setCountDirectionDown(boolean down) {
		synchronized (timeLock) {
			countDown = down;
			clockChange(new ClockDirectionEvent(getClock(), countDown));
		}
	}

	public boolean isRunning() { return isRunning; }

	public void start() {
		synchronized (timeLock) {
			if (isRunning())
				return;

			isRunning = true;
			waitingForStart = true;
			unstartTime = getTime();

			long now = System.currentTimeMillis();
			long delayStartTime = 0;
			if (isSyncTime()) {
				// This syncs all the clocks to change second at the same time
				long timeMs = unstartTime % 1000;
				long nowMs = now % 1000;
				if (countDown)
					timeMs = (1000 - timeMs) % 1000;
				long delay = timeMs - nowMs;
				if (Math.abs(delay) >= 500)
					delay = (long)(Math.signum((float)-delay) * (1000 - Math.abs(delay)));
				lastTime = now + delay;
				delayStartTime = Math.max(0, delay);
			} else {
				lastTime = now;
			}
			timer.schedule(new StartTimerTask(), delayStartTime);
		}
	}
	public void stop() {
		ClockRunningEvent crE;
		synchronized (timeLock) {
			if (waitingForStart)
				try { timeLock.wait(2000); } catch ( InterruptedException iE ) { }

			if (!isRunning())
				return;

			isRunning = false;
			timerTask.cancel();
			unstopLastTime = lastTime;
			unstopTime = getTime();
			crE = new ClockRunningEvent(getClock(), isRunning());
		}

		clockChange(crE);
	}
	public void unstart() {
		synchronized (timeLock) {
			if (!isRunning())
				return;

			stop();
			setTime(unstartTime);
		}
	}
	public void unstop() {
		synchronized (timeLock) {
			if (isRunning())
				return;

			setTime(unstopTime);
			long change = System.currentTimeMillis() - unstopLastTime;
			changeTime(countDown?-change:change);
			start();
		}
	}

	protected void startTimer() {
		synchronized (timeLock) {
			clockChange(new ClockRunningEvent(getClock(), isRunning()));
			timerTask = new UpdateClockTimerTask();
			timer.scheduleAtFixedRate(timerTask, CLOCK_UPDATE_INTERVAL, CLOCK_UPDATE_INTERVAL);
			waitingForStart = false;
			timeLock.notifyAll();
		}
	}
	protected void timerTick() {
		if (!isRunning())
			return;

		long change = System.currentTimeMillis() - lastTime;
		lastTime += change;

		_changeTime(countDown?-change:change, false);
	}

	protected boolean isSyncTime() {
		Policy syncPolicy = getScoreBoard().getPolicy(ClockSyncPolicy.ID);
		return (syncPolicy == null ? true : syncPolicy.isEnabled());
	}

	protected ScoreBoardModel scoreBoardModel;

	protected String id;
	protected String name;
	protected int number;
	protected int minimumNumber;
	protected int maximumNumber;
	protected long time;
	protected long minimumTime;
	protected long maximumTime;
	protected boolean countDown;

	protected Timer timer = new Timer();
	protected TimerTask timerTask;
	protected long lastTime;
	protected boolean waitingForStart = false;
	protected boolean isRunning = false;

	protected long unstartTime = 0;
	protected long unstopTime = 0;
	protected long unstopLastTime = 0;

	protected Object nameLock = new Object();
	protected Object numberLock = new Object();
	protected Object timeLock = new Object();

	protected static final long CLOCK_UPDATE_INTERVAL = 25; /* in ms */

	protected class StartTimerTask extends TimerTask {
		public void run() {
			DefaultClockModel.this.startTimer();
		}
	}
	protected class UpdateClockTimerTask extends TimerTask {
		public void run() {
			synchronized (runLock) {
				if (running)
					DefaultClockModel.this.timerTick();
			}
		}
		public boolean cancel() {
			synchronized (runLock) {
				running = false;
			}
			return super.cancel();
		}
		protected boolean running = true;
		protected Object runLock = new Object();
	}
}
