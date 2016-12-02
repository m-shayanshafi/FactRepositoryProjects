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
  
package org.icehockeymanager.ihm.game.player.goalie;

import org.icehockeymanager.ihm.game.player.*;

/**
 * Goalie stats (analog to the team stats)
 * 
 * @author Bernhard von Gunten
 * @created January, 2002
 */
public class GoalieStats extends PlayerStats {

  /**
   * Comment for <code>serialVersionUID</code>
   */
  private static final long serialVersionUID = 3978984388229085233L;

  /**
   * Constructs new GoalieStats for a owner
   * 
   * @param player
   *          Player of this stats
   * @param owner
   *          Owner of this stats
   */
  public GoalieStats(Player player, Object owner) {
    super(player, owner);
  }

}
