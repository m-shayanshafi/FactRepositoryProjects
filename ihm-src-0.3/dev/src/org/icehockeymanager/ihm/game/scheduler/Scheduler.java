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
  
package org.icehockeymanager.ihm.game.scheduler;

import java.io.*;
import java.util.*;

import org.icehockeymanager.ihm.clients.devgui.ihm.scheduler.SchedulerTools;
import org.icehockeymanager.ihm.game.scheduler.events.*;
import org.icehockeymanager.ihm.clients.devgui.ihm.scheduler.*;

/**
 * Scheduler Provides an event calendar.
 * 
 * <p>- Returns a Subset of a Calendar (SchedulerHelper) for creating new
 * events
 * 
 * <p>- Returns the next "unplayed" event - Uses singelton pattern.
 * 
 * @author Bernhard von Gunten
 * @created December 2001
 */
public class Scheduler implements SchedulerEventOwner, Serializable {

  /**
   * Comment for <code>serialVersionUID</code>
   */
  private static final long serialVersionUID = 3832897750118707767L;

  /** The events */
  private Vector<SchedulerEvent> events = new Vector<SchedulerEvent>();

  /** The date when the year started */
  private Calendar startDay = null;

  /** Today */
  private Calendar today = null;

  /** Constructor disabled */
  public Scheduler() {
  }

  /**
   * Sets the start date of this calendar
   * 
   * @param start
   *          The new startDate value
   */
  public void setStartDate(Calendar start) {
    startDay = (Calendar) start.clone();
    today = (Calendar) start.clone();
  }

  /**
   * Add new event to the schedule.
   * 
   * @param event
   *          SchedulerEvent to be added
   */
  public void addEvent(SchedulerSpecialEvent event) {
    events.add(event);
  }

  /**
   * Add new Event to the schedule.
   * 
   * @param event
   *          SchedulerInternalEvent
   */
  public void addEvent(SchedulerInternalEvent event) {
    events.add(event);
  }

  /**
   * Add new Event to the scheduler.
   * 
   * @param event
   *          SchedulerBreakerGroupEvent
   */
  public void addEvent(SchedulerBreakerGroupEvent event) {
    events.add(event);
  }

  /**
   * Add new Event to the scheduler. All SchedulerBreakerEvnet are internaly
   * grouped into a SchedulerBreakerGroupEvent.
   * 
   * @param event
   *          SchedulerBreakerEvent
   */
  public void addEvent(SchedulerBreakerEvent event) {
    SchedulerBreakerGroupEvent group = this.getSchedulerEventBreakerGroup(event.getDay());
    group.addSchedulerEventBreaker(event);
  }

  /**
   * Returns a SchedulerBreakerGroupEvent for a specific day. If none is found,
   * it will create one.
   * 
   * @param day
   *          Calendar
   * @return SchedulerBreakerGroupEvent
   */
  private SchedulerBreakerGroupEvent getSchedulerEventBreakerGroup(Calendar day) {
    Vector<SchedulerEvent> events = getEventsFrom(day);
    SchedulerBreakerGroupEvent result = null;
    // Look if event already exists on that day
    for (int i = 0; i < events.size(); i++) {
      if (events.get(i) instanceof SchedulerBreakerGroupEvent) {
        result = (SchedulerBreakerGroupEvent) events.get(i);
      }
    }

    // If not, insert a new one
    if (result == null) {
      result = new SchedulerBreakerGroupEvent(this, day, "event.schedulerbreaker");
      addEvent(result);
    }

    return result;
  }

  /**
   * Return a new SchedulerHelper which starts at day no 1
   * 
   * @return A new SchedulerHelper
   */
  public SchedulerHelper getSchedulerHelper() {
    return new SchedulerHelper((Calendar) startDay.clone());
  }

  /**
   * Return a new SchedulerHelper which starts today
   * 
   * @return A new SchedulerHelper starting by today
   */
  public SchedulerHelper getSchedulerHelperByToday() {
    return new SchedulerHelper((Calendar) today.clone());
  }

  /**
   * Return a new SchedulerHelper
   * 
   * @param day
   *          The day to start the SchedulerHelper
   * @return A new SchedulerHelper starting by the day passed by
   */
  public SchedulerHelper getSchedulerHelperByDay(Calendar day) {
    return new SchedulerHelper((Calendar) day.clone());
  }

  /**
   * Returns all events of a day
   * 
   * @param day
   *          The day of the events
   * @return All events from a day
   */
  public Vector<SchedulerEvent> getEventsFrom(Calendar day) {
    Vector<SchedulerEvent> result = new Vector<SchedulerEvent>();
    java.util.Collections.sort(events);
    for (int i = 0; i < events.size(); i++) {
      SchedulerEvent tmp = events.get(i);
      if (tmp.getDay().equals(day)) {
        result.add(tmp);
      }
    }
    return result;
  }

  /**
   * Returns all events until today
   * 
   * @return Vector
   */
  public Vector getDoneEvents() {
    Vector<SchedulerEvent> result = new Vector<SchedulerEvent>();
    java.util.Collections.sort(events);
    for (int i = 0; i < events.size(); i++) {
      SchedulerEvent tmp = events.get(i);
      if (tmp.isDone()) {
        result.add(tmp);
      }
    }
    return result;
  }

  /**
   * Returns all events from today
   * 
   * @return All events from today
   */
  public Vector<SchedulerEvent> getEventsToday() {
    return getEventsFrom(today);
  }

  /**
   * Returns "today" as Calendar
   * 
   * @return Calendar
   */
  public Calendar getToday() {
    return (Calendar) today.clone();
  }

  /**
   * Returns "Today" next Year.
   * 
   * @return Calendar
   */
  public Calendar getTodayNextYear() {
    Calendar result = getToday();
    result.add(Calendar.YEAR, 1);
    return result;
  }

  /**
   * Resets the calendar for a new season
   */
  public void endSeason() {
    startDay.add(Calendar.YEAR, 1);
    today = (Calendar) startDay.clone();
    removeEventsBeforeToday();
  }

  /**
   * Removes all events before today
   */
  private void removeEventsBeforeToday() {
    java.util.Collections.sort(events);
    // Look for events to delete
    Vector<SchedulerEvent> toDelete = new Vector<SchedulerEvent>();
    for (int i = 0; i < events.size(); i++) {
      SchedulerEvent tmp = events.get(i);
      if (tmp.getDay().before(getToday())) {
        toDelete.add(tmp);
      }
    }
    // And delete them ...
    for (int i = 0; i < toDelete.size(); i++) {
      events.remove(toDelete.get(i));
    }
  }

  /**
   * Returns the first day of the season
   * 
   * @return The first day of this season
   */
  public Calendar getFirstDay() {
    return (Calendar) startDay.clone();
  }

  /**
   * Returns first Day next year
   * 
   * @return Calendar
   */
  public Calendar getFirstDayNextYear() {
    Calendar result = getFirstDay();
    result.add(Calendar.YEAR, 1);
    return result;
  }

  /**
   * getLastDayNextYear
   * 
   * @return Calendar
   */
  public Calendar getLastDayNextYear() {
    Calendar result = getLastDay();
    result.add(Calendar.YEAR, 1);
    return result;
  }

  /**
   * Returns the last day of the season
   * 
   * @return Calendar
   */
  public Calendar getLastDay() {
    Calendar tmp = (Calendar) startDay.clone();
    tmp.add(Calendar.YEAR, 1);
    tmp.add(Calendar.DATE, -1);
    return tmp;
  }

  /**
   * Returns the next not processed event. Sorted by internal, breaker & special
   * Events.
   * 
   * @return The next unplayed event
   */
  public SchedulerEvent getNextEvent() {
    // Sort events
    java.util.Collections.sort(events);

    for (int i = 0; i < events.size(); i++) {
      SchedulerEvent tmp = events.get(i);
      if (!tmp.isDone()) {
        today = tmp.getDay();
        break;
      }
    }

    Vector<SchedulerEvent> todaysEvents = this.getEventsToday();

    // First return internal events
    for (int i = 0; i < todaysEvents.size(); i++) {
      SchedulerEvent tmp = todaysEvents.get(i);
      if (!tmp.isDone() && tmp instanceof SchedulerInternalEvent) {
        return tmp;
      }
    }

    // Second return breakers
    for (int i = 0; i < todaysEvents.size(); i++) {
      SchedulerEvent tmp = todaysEvents.get(i);
      if (!tmp.isDone() && tmp instanceof SchedulerBreakerGroupEvent) {
        return tmp;
      }
    }

    // And last return all special events for today
    for (int i = 0; i < todaysEvents.size(); i++) {
      SchedulerEvent tmp = todaysEvents.get(i);
      if (!tmp.isDone() && tmp instanceof SchedulerSpecialEvent) {
        return tmp;
      }
    }

    return null;
  }

  /**
   * Returns all events by source
   * 
   * @param source
   *          Description of the Parameter
   * @return SchedulerEvents by source
   */
  public Vector<SchedulerEvent> getEventsBySource(Object source) {
    Vector<SchedulerEvent> result = new Vector<SchedulerEvent>();

    // First add all events for the source directly
    for (int i = 0; i < events.size(); i++) {
      SchedulerEvent tmp = events.get(i);
      if (tmp.getSource().equals(source)) {
        result.add(tmp);
      }
    }
    return result;
  }

  /**
   * Returns all events.
   * 
   * @return Vector
   */
  public Vector<SchedulerEvent> getEvents() {
    return events;
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.icehockeymanager.ihm.game.scheduler.events.SchedulerEventOwner#getTMScheduler()
   */
  public TMScheduler getTMScheduler() {
    return SchedulerTools.getTMSchedulerPublicEvents();
  }
}
