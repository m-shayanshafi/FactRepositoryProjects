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
  
package org.icehockeymanager.ihm.clients.devgui.ihm.scheduler;

import java.util.*;

import org.icehockeymanager.ihm.clients.devgui.ihm.team.*;
import org.icehockeymanager.ihm.game.*;
import org.icehockeymanager.ihm.game.league.*;
import org.icehockeymanager.ihm.game.league.helper.*;
import org.icehockeymanager.ihm.game.league.events.*;
import org.icehockeymanager.ihm.game.scheduler.events.*;
import org.icehockeymanager.ihm.game.team.*;
import org.icehockeymanager.ihm.lib.*;

public class SchedulerTools {

  /**
   * Returns table model with all events for a team
   * @param team 
   * 
   * @return TMTeamCalendar with all events
   */
  public static TMTeamCalendar getTMTeamCalendar(Team team) {
    Vector<SchedulerEvent> parm = new Vector<SchedulerEvent>();
    Vector<SchedulerEvent> events = GameController.getInstance().getScenario().getScheduler().getEvents();

    for (int i = 0; i < events.size(); i++) {
      if (events.get(i) instanceof GameDayMatchesEvent) {
        GameDayMatchesEvent tmp = (GameDayMatchesEvent) events.get(i);
        if (tmp.isTeamInvolved(team)) {
          parm.add(tmp);
        }
      }
    }

    java.util.Collections.sort(parm);
    return new TMTeamCalendar(parm, team);
  }

  /**
   * Returns table model with all events in the calendar
   * 
   * @return TMScheduler with all events
   */
  public static TMScheduler getTMSchedulerAllEvents() {
    Vector<SchedulerEvent> events = GameController.getInstance().getScenario().getScheduler().getEvents();
    java.util.Collections.sort(events);
    return new TMScheduler(events);
  }

  /**
   * Returns table model with all events in the calendar
   * 
   * @return TMScheduler with all events
   */
  public static TMScheduler getTMSchedulerPublicEvents() {
    Vector<SchedulerEvent> parm = new Vector<SchedulerEvent>();
    Vector<SchedulerEvent> events = GameController.getInstance().getScenario().getScheduler().getEvents();

    for (int i = 0; i < events.size(); i++) {
      SchedulerEvent tmp = events.get(i);
      parm.add(tmp);
    }

    java.util.Collections.sort(parm);
    return new TMScheduler(parm);
  }

  /**
   * Returns table model with all events in the calendar by specific source
   * <p> - Including also special behavior for sources that provide other
   * sources (like leagues)
   * 
   * @param source
   *          Source of the event
   * @param includeSub
   *          Describes if "sub" Sources will be returned too
   * @return TMScheduler with all events by specific source
   */
  public static TMScheduler getTMSchedulerEventsBySource(Object source, boolean includeSub) {
    Vector<SchedulerEvent> parm = new Vector<SchedulerEvent>();

    // First add all events for the source directly
    parm.addAll(GameController.getInstance().getScenario().getScheduler().getEventsBySource(source));

    // For special soruces, browse also trough "sub-sources"
    if (includeSub) {
      // Leagues
      if (source instanceof org.icehockeymanager.ihm.game.league.League) {
        League league = (League) source;
        Vector<LeagueElement> elements = league.getLeagueElements();
        for (int i = 0; i < elements.size(); i++) {
          parm.addAll(GameController.getInstance().getScenario().getScheduler().getEventsBySource(elements.get(i)));
        }
      }
    }

    java.util.Collections.sort(parm);
    return new TMScheduler(parm);
  }

  public static String calendarToString(Calendar date) {
    try {
      return Tools.dateToString(date.getTime(), Tools.DATE_FORMAT_EU_DATE);
    } catch (Exception todo) {
      return "##.##.####";
    }
  }

  public static String getLastDayMonth() {
    try {
      Calendar last = GameController.getInstance().getScenario().getScheduler().getLastDay();
      return Tools.dateToString(last.getTime(), "dd.MM");
    } catch (Exception todo) {
      return "##.##.####";
    }
  }

  public static String getNextYear() {
    try {
      return Tools.dateToString(GameController.getInstance().getScenario().getScheduler().getLastDayNextYear().getTime(), "yyyy");
    } catch (Exception todo) {
      return "##.##.####";
    }
  }

  public static String extractYear(Calendar cal) {
    try {
      return Tools.dateToString(cal.getTime(), "yyyy");
    } catch (Exception todo) {
      return "##.##.####";
    }
  }

}
