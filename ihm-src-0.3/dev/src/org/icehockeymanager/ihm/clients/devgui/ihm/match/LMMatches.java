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
  
package org.icehockeymanager.ihm.clients.devgui.ihm.match;

import java.util.*;

import javax.swing.*;

import org.icehockeymanager.ihm.game.match.*;

/**
 * LMMatches provides a simple match list model
 * 
 * @author Bernhard von Gunten
 * @created January 2, 2002
 */
public class LMMatches extends AbstractListModel {

  static final long serialVersionUID = 1656742471045078854L;

  private Vector<Match> matches;

  /**
   * Constructor for the LMMatches object
   * 
   * @param matches
   *          The matches provided by this model
   */
  public LMMatches(Vector<Match> matches) {
    this.matches = matches;
  }

  /**
   * Gets the size attribute of the LMMatches object
   * 
   * @return The size value
   */
  public int getSize() {
    return matches.size();
  }

  /**
   * Gets the match description by row
   * 
   * @param row
   *          The row
   * @return The match description
   */
  public Object getElementAt(int row) {

    return MatchTools.getMatchDescription(getMatch(row));
  }

  /**
   * Returns the specific match for a row
   * 
   * @param row
   *          The row of a match
   * @return The match value
   */
  public Match getMatch(int row) {
    return matches.get(row);
  }

}
