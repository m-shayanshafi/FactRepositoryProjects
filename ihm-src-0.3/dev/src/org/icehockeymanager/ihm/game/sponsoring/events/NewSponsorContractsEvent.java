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
  
package org.icehockeymanager.ihm.game.sponsoring.events;

import java.util.*;

import org.icehockeymanager.ihm.game.*;
import org.icehockeymanager.ihm.game.scheduler.events.*;
import org.icehockeymanager.ihm.game.team.*;

/**
 * NewSponsorContractsEvent - checks and generates sponsoring contracts
 * 
 * @author adasen
 * @created Oct 19, 2004
 */
public class NewSponsorContractsEvent extends SchedulerInternalEvent {

  /**
   * Comment for <code>serialVersionUID</code>
   */
  private static final long serialVersionUID = 3906086754371842869L;

  /**
   * NewSponsorContractsEvent constructor
   * 
   * @param source
   * @param day
   */
  public NewSponsorContractsEvent(Object source, Calendar day) {
    super(source, day, "events.sponsoring.newSponsorContracts");
  }

  /**
   * check valid contracts and generate new offered contracts for each team
   */
  public void play() {
    super.play();

    Team[] teams = GameController.getInstance().getScenario().getTeams();

    for (int i = 0; i < teams.length; i++) {
      teams[i].getSponsoring().checkValidContracts();
      teams[i].getSponsoring().generateOfferedContracts();
    }

  }

}
