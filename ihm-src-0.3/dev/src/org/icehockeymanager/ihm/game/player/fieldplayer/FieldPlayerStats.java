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
  
package org.icehockeymanager.ihm.game.player.fieldplayer;

import org.icehockeymanager.ihm.game.player.*;

/**
 * FieldPlayer stats (analog to the team stats)
 * 
 * @author Bernhard von Gunten
 * @created January, 2002
 */
public class FieldPlayerStats extends PlayerStats {

  /**
   * Comment for <code>serialVersionUID</code>
   */
  private static final long serialVersionUID = 3256441391499785521L;

  /**
   * Constructs new FieldPlayerStats for a owner
   * 
   * @param player
   *          Player of this stats
   * @param owner
   *          Owner of this stats
   */
  public FieldPlayerStats(Player player, Object owner) {
    super(player, owner);
  }

}
