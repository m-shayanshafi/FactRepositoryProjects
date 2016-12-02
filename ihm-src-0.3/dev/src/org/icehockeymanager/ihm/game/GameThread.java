/************************************************************* 
 * 
 * Ice Hockey Manager 
 * ================== 
 * 
 * Copyright (C) by the IHM Team (see doc/credits.txt) 
 * 
 * This program is released under the GPL (see doc/gpl.txt) 
 * 
 * Further informations: http://www.icehockeymanager.org  
 * 
 *************************************************************/ 
  
package org.icehockeymanager.ihm.game;

import java.io.*;
import java.util.*;

import org.icehockeymanager.ihm.game.scheduler.*;
import org.icehockeymanager.ihm.game.scheduler.events.*;

/**
 * The GameThread class extends Threads, and is responsible for the loop of
 * events provided by the Scheduler.
 * <p>
 * 
 * The "play()" function of every Event is called. If the function
 * "needsonline()" of an event returns true, the thread will be suspended and a
 * "SchedulerEventPerformed" Event will be fired, which gives the GameController
 * the possibility to give the control back to the UserGui.
 * <p>
 * 
 * @author Bernhard von Gunten & Arik Dasen
 * @created December 2001
 */
public class GameThread extends Thread implements Serializable {

  static final long serialVersionUID = 8662723878240937702L;

  /** Listeners connected to the game class */
  private Vector<SchedulerListener> listener;

  /** Flag if thread is suspended */
  private volatile boolean suspended = false;

  /** Constructor for the Game object */
  public GameThread() {
  }

  /** should only be used after a loadScenario() */
  public synchronized void setSuspended() {
    suspended = true;
  }

  /** Run method. A bit complicated, cause suspend() and resume() are depricated */
  public void run() {
    while (true) {
      if (suspended) {
        synchronized (this) {
          while (suspended) {
            try {
              wait();
            } catch (InterruptedException e) {
            }
          }
        }
      } else {

        // Get the next Event in the controller
        SchedulerEvent event = GameController.getInstance().getScenario().getScheduler().getNextEvent();

        // Proceede if internal event, else stop & call the gui with the Event
        if (event instanceof SchedulerInternalEvent) {
          event.play();
          processSchedulerInternalEvent((SchedulerInternalEvent) event);
        } else if (event instanceof SchedulerBreakerGroupEvent) {
          synchronized (this) {
            suspended = true;
          }
          processSchedulerBreakerGroupEvent((SchedulerBreakerGroupEvent) event);
        } else if (event instanceof SchedulerSpecialEvent) {
          synchronized (this) {
            suspended = true;
          }
          processSchedulerSpecialEvent((SchedulerSpecialEvent) event);
        }
      }
    }
  }

  /** MoveOn method called by the controller */
  public synchronized void moveOn() {
    suspended = false;
    notify();
  }

  /**
   * event-handling
   * 
   * @param m
   *          The feature to be added to the SchedulerListener attribute
   */
  public synchronized void addSchedulerListener(SchedulerListener m) {
    if (listener == null) {
      listener = new Vector<SchedulerListener>(5, 5);
    }
    if (!listener.contains(m)) {
      listener.addElement(m);
    }
  }

  /**
   * event-handling
   * 
   * @param m
   *          Scheduler event to be processed.
   */
  private synchronized void processSchedulerSpecialEvent(SchedulerSpecialEvent m) {
    if (listener != null) {
      for (Enumeration<SchedulerListener> e = listener.elements(); e.hasMoreElements();) {
        (e.nextElement()).schedulerSpecialEvent(m);
      }
    }
  }

  private synchronized void processSchedulerInternalEvent(SchedulerInternalEvent m) {
    if (listener != null) {
      for (Enumeration<SchedulerListener> e = listener.elements(); e.hasMoreElements();) {
        (e.nextElement()).schedulerInternalEvent(m);
      }
    }
  }

  private synchronized void processSchedulerBreakerGroupEvent(SchedulerBreakerGroupEvent m) {
    if (listener != null) {
      for (Enumeration<SchedulerListener> e = listener.elements(); e.hasMoreElements();) {
        (e.nextElement()).schedulerBreakerGroupEvent(m);
      }
    }
  }

}
