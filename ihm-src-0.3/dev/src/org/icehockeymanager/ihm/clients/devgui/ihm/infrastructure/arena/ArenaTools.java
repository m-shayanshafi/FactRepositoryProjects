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
  
package org.icehockeymanager.ihm.clients.devgui.ihm.infrastructure.arena;

import org.icehockeymanager.ihm.clients.devgui.controller.*;
import org.icehockeymanager.ihm.game.infrastructure.arena.*;

public class ArenaTools {

  public static String getTotalCapacityLabel(Arena arena) {
    return ClientController.getInstance().getTranslation("arena.totalcapacity") + " : " + arena.getTotalCapacity();
  }
  
  public static String getCategoryCapacityLabel(ArenaCategory category) {
    if (category == null || !category.isBuilt()) {
      return ClientController.getInstance().getTranslation("arena.categorynotbuilt");
    } else {
      return ClientController.getInstance().getTranslation("arena.categorycapacity") + " : " + category.getCapacity();  
    }
    
  }
  
  public static String getCategoryCondition(ArenaCategory category) {
    if (category == null || !category.isBuilt()) {
      return ClientController.getInstance().getTranslation("arena.condition") + " : 0%";
    } else {
      return ClientController.getInstance().getTranslation("arena.condition") + " : " + category.getCondition() + "%";
    }
  }
  
  public static String getCategoryComfort(int comfort) {
  
    if (comfort == ArenaCategory.COMFORT_CHEAP) {
      return ClientController.getInstance().getTranslation("arena.comfort.cheap");
    } else if (comfort == ArenaCategory.COMFORT_NORMAL) {
      return ClientController.getInstance().getTranslation("arena.comfort.normal");
    } else {
      return ClientController.getInstance().getTranslation("arena.comfort.deluxe");
    } 
    
  }
    
  public static String[] getCagegoryComforts() {
    return new String[] {ClientController.getInstance().getTranslation("arena.comfort.cheap"), ClientController.getInstance().getTranslation("arena.comfort.normal"), ClientController.getInstance().getTranslation("arena.comfort.deluxe") }; 
  }
  
  
}
