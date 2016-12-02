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
  
package org.icehockeymanager.ihm.game.match;

import java.awt.event.*;
import java.util.*;

import org.icehockeymanager.ihm.game.league.events.*;

/**
 * The MatchController is a Thread, started by a client. The match controller
 * plays all matches together (scene by scene) and may be suspended & restarted.
 * <p>
 * The match controller sleeps n milliseconds after all matches have been played
 * for one scene.
 * <p>
 * The match controller fires an ActionEvent when all games are finished.
 * 
 * @author Bernhard von Gunten
 * @created January 2005
 */
public class MatchController extends Thread {

  /** All matches to play */
  private Match[] matches = null;

  /** Listeners listening for the end of all matches */
  private Vector<ActionListener> actionListeners = null;

  /** Flag if there are still unfinished matches */
  private boolean openGames = true;

  /** Sleep for n ms after all matches have played a scene in the loop */
  private int sleepms = 0;

  /** Flag if thread is suspended */
  private volatile boolean suspended = false;

  /**
   * Constructs the MatchController.
   * 
   * @param gameDay
   *          GameDayMatchesEvent
   * @param sleepms
   *          Sleep for n ms after each match has played a scene
   */
  public MatchController(GameDayMatchesEvent gameDay, int sleepms) {
    this.matches = gameDay.getMatchesArray();
    this.sleepms = sleepms;
  }

  /**
   * Interrupt the MatchController
   */
  public synchronized void setSuspended() {
    suspended = true;
  }

  /** MoveOn method called by the controller */
  public synchronized void moveOn() {
    suspended = false;
    notify();
  }

  /**
   * The run methode loops trough all matches until they are finished. Sleeps
   * once per for n ms loop.
   */
  public void run() {

    while (openGames) {
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

        // Temporary set
        openGames = false;
        for (int i = 0; i < matches.length; i++) {
          if (!matches[i].isFinished()) {
            matches[i].moveOn();
            openGames = true;
          }
        }

        try {
          Thread.sleep(sleepms);
        } catch (InterruptedException ex) {
        }

      }
    }

    // Just if we're getting here because of all games are finished fire event
    if (!suspended) {
      fireActionPerformed(new ActionEvent(this, 0, "All games finished"));
    }

  }

  public synchronized void removeAllActionListeners() {
    actionListeners = null;
  }

  /**
   * Remove listenes from the MatchController
   * 
   * @param l
   *          Listener to remove
   */
  public synchronized void removeActionListener(ActionListener l) {
    if (actionListeners != null && actionListeners.contains(l)) {
      Vector<ActionListener> v = new Vector<ActionListener>(actionListeners);
      v.removeElement(l);
      actionListeners = v;
    }
  }

  /**
   * Adds a feature to the ActionListener attribute of the Desktop object
   * 
   * @param l
   *          The feature to be added to the ActionListener attribute
   */
  public synchronized void addActionListener(ActionListener l) {
    
    Vector<ActionListener> v = actionListeners == null ? new Vector<ActionListener>(2) : new Vector<ActionListener>(actionListeners);
    
    if (!v.contains(l)) {
      v.addElement(l);
      actionListeners = v;
    }
  }

  /**
   * Description of the Method
   * 
   * @param e
   *          Source event
   */
  protected void fireActionPerformed(ActionEvent e) {
    if (actionListeners != null) {
      Vector<ActionListener> listeners = actionListeners;
      int count = listeners.size();
      for (int i = 0; i < count; i++) {
        listeners.elementAt(i).actionPerformed(e);
      }
    }
  }

}