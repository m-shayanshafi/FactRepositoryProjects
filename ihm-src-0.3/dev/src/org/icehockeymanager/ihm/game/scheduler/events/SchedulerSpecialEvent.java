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
 * SchedulerSpecialEvent is the base class for every special event, who need
 * special client control. Like the GameDay etc.
 * 
 * @author Bernhard von Gunten
 * @created October 2004
 */
public class SchedulerSpecialEvent extends SchedulerEvent implements Serializable {

  /**
   * Comment for <code>serialVersionUID</code>
   */
  private static final long serialVersionUID = 3978141032695411508L;

  /**
   * SchedulerSpecialEvent constructor
   * 
   * @param source
   *          Object
   * @param day
   *          Calendar
   * @param messageKey
   *          String
   */
  public SchedulerSpecialEvent(Object source, Calendar day, String messageKey) {
    super(source, day, messageKey);
  }

  /**
   * Plays the super.play() function and "finalizes" this function, so no real
   * SpecialEvent may have a play function.
   */
  public final void play() {
    super.play();
  }

}
