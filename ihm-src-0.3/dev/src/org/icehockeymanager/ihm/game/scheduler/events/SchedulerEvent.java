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
  
package org.icehockeymanager.ihm.game.scheduler.events;

import java.io.*;
import java.text.*;
import java.util.*;

/**
 * SchedulerEvent ist the base class of all Scheduler Events. Containts the
 * date, Source and MessageKey of a Event.
 * 
 * @author Bernhard von Gunten
 *         </p>
 * @created October 2004
 *          </p>
 */
public abstract class SchedulerEvent implements Comparable<SchedulerEvent>, Serializable {

  /** Flag if event has already been played */
  private boolean isDone = false;

  /** Date of event */
  protected Calendar day = null;

  /** Description of event */
  protected String messageKey = null;

  /** Source of this event */
  protected Object source = null;

  /**
   * Constructs event, sets soruce, date and description of event
   * 
   * @param source
   *          The source of this event
   * @param day
   *          The day of this event
   * @param messageKey
   *          The message key (important for clients)
   */
  public SchedulerEvent(Object source, Calendar day, String messageKey) {
    this.day = day;
    this.messageKey = messageKey;
    this.source = source;
  }

  /**
   * Returns boolean if event has already been played
   * 
   * @return The done value
   */
  public boolean isDone() {
    return isDone;
  }

  /**
   * Returns date of event
   * 
   * @return The day value
   */
  public Calendar getDay() {
    return day;
  }

  /**
   * Returns formated date
   * 
   * @return The formatedDate value
   */
  public String getFormatedDate() {
    SimpleDateFormat df = new SimpleDateFormat("dd.MM.yyyy");
    return df.format(day.getTime());
  }

  /**
   * Returns description fo event
   * 
   * @return The description value
   */
  public String getMessageKey() {
    return messageKey;
  }

  /** Override this methid to reformat (replace %n's) your message string 
   * @param string 
   * @return reformated string */
  public String reformat(String string) {
    return string;
  }

  /** Play method called by game thread */
  public void play() {
    this.isDone = true;
  }

  /**
   * Returns "sort value" (date) of the event
   * 
   * @return The sortValue value
   */
  public long getSortValue() {
    return day.getTime().getTime();
  }

  /**
   * Returns a simple Dump of this class. For debug output only.
   * 
   * @return String
   */
  public String getDump() {
    String result = this.getClass().getName() + "\n";
    return result;
  }

  /**
   * Implements the comparable interface
   * 
   * @param o
   *          The object to compare
   * @return Standard compare values
   */
  public int compareTo(SchedulerEvent o) {
    SchedulerEvent tmp = o;
    if (tmp.getSortValue() > getSortValue()) {
      return -1;
    }
    if (tmp.getSortValue() < getSortValue()) {
      return 1;
    }
    return 0;
  }

  /**
   * Returns the source of this event.
   * 
   * @return Object
   */
  public Object getSource() {
    return source;
  }

}
