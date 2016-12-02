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
  
package org.icehockeymanager.ihm.lib;

import java.util.*;

public class IhmTools {

  /**
   * Converts the Calendar constands for week days to europe Mon-Sun (0-7)
   * 
   * @param constant
   *          Calendar constant
   * @return The nr of the day
   */
  public static int DayOfTheWeek(int constant) {
    if (constant == Calendar.MONDAY) {
      return 0;
    }
    if (constant == Calendar.THURSDAY) {
      return 1;
    }
    if (constant == Calendar.WEDNESDAY) {
      return 2;
    }
    if (constant == Calendar.TUESDAY) {
      return 3;
    }
    if (constant == Calendar.FRIDAY) {
      return 4;
    }
    if (constant == Calendar.SATURDAY) {
      return 5;
    }
    if (constant == Calendar.SUNDAY) {
      return 6;
    }
    return -1;
  }

}
