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
  
package org.icehockeymanager.ihm.game.match.textengine;

import org.icehockeymanager.ihm.game.match.*;
import org.icehockeymanager.ihm.game.match.textengine.data.*;

/**
 * The MatchListener, based on EventListener, contains different match relevant
 * functions (like gameFinished, scoreChange etc.)
 * 
 * @author Bernhard von Gunten
 * @created January 2005
 */
public abstract interface TextMatchListener extends MatchListener {

  /** A faceOff has been played 
   * @param matchDataFaceOff */
  public abstract void faceOff(MatchDataFaceOff matchDataFaceOff);

  /** A situation has been played 
   * @param matchDataSituation */
  public abstract void situation(MatchDataSituation matchDataSituation);

}