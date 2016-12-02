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
  
package org.icehockeymanager.ihm.game.scenario.events;

import java.util.*;

import org.icehockeymanager.ihm.game.*;
import org.icehockeymanager.ihm.game.ai.events.*;
import org.icehockeymanager.ihm.game.finance.events.*;
import org.icehockeymanager.ihm.game.infrastructure.arena.events.*;
import org.icehockeymanager.ihm.game.infrastructure.events.*;
import org.icehockeymanager.ihm.game.injuries.events.*;
import org.icehockeymanager.ihm.game.prospects.events.*;
import org.icehockeymanager.ihm.game.randomimpacts.events.*;
import org.icehockeymanager.ihm.game.scenario.log.*;
import org.icehockeymanager.ihm.game.scheduler.events.*;
import org.icehockeymanager.ihm.game.sponsoring.events.*;
import org.icehockeymanager.ihm.game.training.events.*;
import org.icehockeymanager.ihm.game.user.*;

/**
 * SchedulerEventSeasonStart Special event that resets the game and places
 * already a endYear event in the calendar.
 * 
 * @author Bernhard von Gunten / Arik Dasen
 * @created December, 2001
 */
public class SeasonStartEvent extends SchedulerInternalEvent {
  /**
   * Comment for <code>serialVersionUID</code>
   */
  private static final long serialVersionUID = 3256726160800101432L;

  /**
   * Creates the event
   * 
   * @param source
   *          The source of this event
   * @param day
   *          The day of this event
   */
  public SeasonStartEvent(Object source, Calendar day) {
    super(source, day, "events.internal.yearstart");
  }

  /**
   * Reset scenario and add an endYear event to the Scheduler
   */
  public void play() {
    super.play();
    GameController.getInstance().getScenario().newSeason();

    // Add a log message to every user that the saison has started
    for (int i = 0; i < GameController.getInstance().getScenario().getUsers().length; i++) {
      // Log
      SeasonStartEventLog event = new SeasonStartEventLog(this, GameController.getInstance().getScenario().getScheduler().getFirstDay(), GameController.getInstance().getScenario().getUsers()[i]);
      GameController.getInstance().getScenario().getEventLog().addEventLogEntry(event);
    }

    // Add a gui breaker for everyone
    SeasonStartBreakerEvent startBreaker = new SeasonStartBreakerEvent(this, GameController.getInstance().getScenario().getScheduler().getFirstDay());
    GameController.getInstance().getScenario().getScheduler().addEvent(startBreaker);

    // add season tickets event (season start + 1)
    Calendar seasonTicketDay = GameController.getInstance().getScenario().getScheduler().getFirstDay();
    seasonTicketDay.add(Calendar.DATE, 1);
    GameController.getInstance().getScenario().getScheduler().addEvent(new SeasonTicketsEvent(this, seasonTicketDay));

    // Add Weekly Computer AI Events (weekly, season start + 1)
    for (int i = 0; i < 52; i++) {
      Calendar weekly = GameController.getInstance().getScenario().getScheduler().getFirstDay();
      weekly.add(Calendar.DATE, (i * 7) + 1);
      if (GameController.getInstance().getScenario().getScheduler().getLastDay().after(weekly)) {
        GameController.getInstance().getScenario().getScheduler().addEvent(new AIWeeklyEvent(this, weekly));
      }
    }

    // Add sponsoring events (monthly)
    for (int i = 0; i < 12; i++) {
      Calendar day = GameController.getInstance().getScenario().getScheduler().getFirstDay();
      day.set(Calendar.DATE, 2);
      day.add(Calendar.MONTH, i);
      if (GameController.getInstance().getScenario().getScheduler().getLastDay().after(day)) {
        GameController.getInstance().getScenario().getScheduler().addEvent(new NewSponsorContractsEvent(this, day));
      }
    }
    
    // Lower Condition of infrastructures
    for (int i = 0; i < 12; i++) {
      Calendar day = GameController.getInstance().getScenario().getScheduler().getFirstDay();
      day.set(Calendar.DATE, 2);
      day.add(Calendar.MONTH, i);
      if (GameController.getInstance().getScenario().getScheduler().getLastDay().after(day)) {
        GameController.getInstance().getScenario().getScheduler().addEvent(new InfrastructureEvent(this, day));
      }
    }

    // Add finance events (monthly)
    for (int i = 0; i < 12; i++) {
      Calendar day = GameController.getInstance().getScenario().getScheduler().getFirstDay();
      day.set(Calendar.DATE, 25);
      day.add(Calendar.MONTH, i);
      if (GameController.getInstance().getScenario().getScheduler().getLastDay().after(day)) {
        GameController.getInstance().getScenario().getScheduler().addEvent(new AccountingEvent(this, day));
      }
    }

    // Add Training Events (daily)
    for (int i = 1; i < 365; i++) {
      Calendar trainingDay = GameController.getInstance().getScenario().getScheduler().getFirstDay();
      trainingDay.add(Calendar.DATE, i);
      if (GameController.getInstance().getScenario().getScheduler().getLastDay().after(trainingDay)) {
        GameController.getInstance().getScenario().getScheduler().addEvent(new TrainingEvent(this, trainingDay));
      }
    }

    // Add Injuries Events (daily)
    for (int i = 1; i < 365; i++) {
      Calendar injuryDay = GameController.getInstance().getScenario().getScheduler().getFirstDay();
      injuryDay.add(Calendar.DATE, i);
      if (GameController.getInstance().getScenario().getScheduler().getLastDay().after(injuryDay)) {
        GameController.getInstance().getScenario().getScheduler().addEvent(new InjuriesEvent(this, injuryDay));
      }
    }

    // Add GlobalImpacts Events (daily)
    for (int i = 1; i < 365; i++) {
      Calendar giDay = GameController.getInstance().getScenario().getScheduler().getFirstDay();
      giDay.add(Calendar.DATE, i);
      if (GameController.getInstance().getScenario().getScheduler().getLastDay().after(giDay)) {
        GameController.getInstance().getScenario().getScheduler().addEvent(new RandomImpactsEvent(this, giDay));
      }
    }

    // Add Prospects Training Events (weekly)
    for (int i = 1; i < 52; i++) {
      Calendar weekly = GameController.getInstance().getScenario().getScheduler().getFirstDay();
      weekly.add(Calendar.DATE, (i * 7) + 3);
      if (GameController.getInstance().getScenario().getScheduler().getLastDay().after(weekly)) {
        GameController.getInstance().getScenario().getScheduler().addEvent(new ProspectsTrainingEvent(this, weekly));
      }
    }

    // Add a log message for each user that the season is finished
    User[] users = GameController.getInstance().getScenario().getUsers();
    for (int i = 0; i < users.length; i++) {
      SeasonEndEventLog tmp = new SeasonEndEventLog(this, GameController.getInstance().getScenario().getScheduler().getLastDay(), users[i]);
      GameController.getInstance().getScenario().getEventLog().addEventLogEntry(tmp);
    }

    // Add the end of the year ai jobs
    Calendar allmostEnd = GameController.getInstance().getScenario().getScheduler().getLastDay();
    allmostEnd.add(Calendar.DATE, -1);
    GameController.getInstance().getScenario().getScheduler().addEvent(new AIYearEndEvent(this, allmostEnd));

    // Add the end of the year
    Calendar yearEnd = GameController.getInstance().getScenario().getScheduler().getLastDay();
    GameController.getInstance().getScenario().getScheduler().addEvent(new SeasonEndEvent(this, yearEnd));
  }

}