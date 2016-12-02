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
  
package org.icehockeymanager.ihm.game.ai.events;

import java.util.*;

import org.icehockeymanager.ihm.game.scheduler.events.*;
import org.icehockeymanager.ihm.game.sponsoring.Sponsoring;
import org.icehockeymanager.ihm.game.training.*;
import org.icehockeymanager.ihm.game.transfers.*;

/**
 * Weekly jobs for "Computer Players"
 * 
 * @author Bernhard von Gunten
 * @created September, 2004
 */
public class AIWeeklyEvent extends SchedulerInternalEvent {

  static final long serialVersionUID = -7486093274498482115L;

  /**
   * Constructor for the Weekly Computer Player Events
   * 
   * @param source
   *          Owner of the event
   * @param day
   *          Day of the event
   */
  public AIWeeklyEvent(Object source, Calendar day) {
    super(source, day, "events.ai.weekly");
  }

  /** Plays the event */
  public void play() {
    super.play();

    Training.aiGenerateTrainingPlans();

    Transfers.aiExtendContracts();
    Transfers.aiPlacePlayersOnTransferList();
    Transfers.aiTransferPlayers();

    Sponsoring.aiChooseSponsoringContracts();
  }

}