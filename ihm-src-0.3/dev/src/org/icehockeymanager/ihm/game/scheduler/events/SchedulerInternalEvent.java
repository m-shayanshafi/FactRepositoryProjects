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
import java.util.*;

/**
 * SchedulerInternalEvent is the base class of all events which do never need
 * client control. Like execution of the daily training, generating injuries
 * etc.
 * 
 * @author Bernhard von Gunten
 * @created October 2004
 */
public class SchedulerInternalEvent extends SchedulerEvent implements Serializable {

  /**
   * Comment for <code>serialVersionUID</code>
   */
  private static final long serialVersionUID = 4049078228779807798L;

  /**
   * SchedulerInternalEvent Constructor
   * 
   * @param source
   *          Object
   * @param day
   *          Calendar
   * @param messageKey
   *          String
   */
  public SchedulerInternalEvent(Object source, Calendar day, String messageKey) {
    super(source, day, messageKey);
  }

}
