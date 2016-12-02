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

import java.util.*;

/**
 * The MatchListener, based on EventListener, contains different match relevant
 * functions (like gameFinished, scoreChange etc.)
 * 
 * @author Bernhard von Gunten
 * @created January 2005
 */
public abstract interface MatchListener extends EventListener {

  /** A regular period has been started */
  public abstract void regularPeriodStarted();

  /** An overtime period has been started */
  public abstract void overtimePeriodStarted();

  /** A regular period has been finished */
  public abstract void regularPeriodFinished();

  /** An overtme period has been finished */
  public abstract void overtimePeriodFinished();

  /** The match has been finished */
  public abstract void gameFinished();

  /** We're heading to overtime */
  public abstract void goToOvertime();

  /** A scene has been finished */
  public abstract void sceneFinished();

  /** The score has been changed */
  public abstract void scoreChange();

}