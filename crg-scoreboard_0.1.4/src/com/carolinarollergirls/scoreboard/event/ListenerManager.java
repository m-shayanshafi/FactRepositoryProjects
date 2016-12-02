package com.carolinarollergirls.scoreboard.event;

import java.util.*;

public class ListenerManager implements EventListener
{
	public ListenerManager() { }

	protected void fireEvent(ScoreBoardEvent event) {
		synchronized (listenersLock) {
			Iterator<ManagerRunnable> i = listeners.values().iterator();
			while (i.hasNext())
				i.next().addEvent((ScoreBoardEvent)event.clone());
		}
	}

	protected void addEventListener(EventListener listener, ManagerRunnable managerRunnable) {
		synchronized (listenersLock) {
			if (!listeners.containsKey(listener)) {
				Thread t = new Thread(managerRunnable);
				t.setDaemon(false);
				t.start();
				listeners.put(listener, managerRunnable);
			}
		}
	}
	protected void removeEventListener(EventListener listener) {
		ManagerRunnable mR;
		synchronized (listenersLock) {
			mR = listeners.remove(listener);
		}
		if (null != mR)
			mR.stop();
	}

	protected Object listenersLock = new Object();
	protected Hashtable<EventListener,ManagerRunnable> listeners = new Hashtable<EventListener,ManagerRunnable>();

	protected abstract class ManagerRunnable implements Runnable
	{
		public abstract void handleEvent(ScoreBoardEvent event);

		public void addEvent(ScoreBoardEvent event) {
			synchronized (eventLock) {
				eventQueue.add(event);
				eventLock.notifyAll();
			}
		}

		public void stop() {
			synchronized (eventLock) {
				running = false;
				eventLock.notifyAll();
			}
		}

		public void run() {
			while (running) {
				ScoreBoardEvent event;

				synchronized (eventLock) {
					if (null == (event = eventQueue.poll()))
						try { eventLock.wait(); }
						catch ( Exception e ) { }
				}

				if (null != event)
					handleEvent(event);
			}
		}

		protected boolean running = true;

		protected Object eventLock = new Object();
		protected Queue<ScoreBoardEvent> eventQueue = new LinkedList<ScoreBoardEvent>();
	}
}
