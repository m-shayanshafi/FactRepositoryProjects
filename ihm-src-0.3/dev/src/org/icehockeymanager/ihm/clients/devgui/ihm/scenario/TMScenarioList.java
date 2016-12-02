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
  
package org.icehockeymanager.ihm.clients.devgui.ihm.scenario;

import java.io.*;
import java.util.*;

import javax.swing.table.*;

import org.icehockeymanager.ihm.clients.devgui.controller.*;

/**
 * Provides a list of loadable scenarios
 * 
 * @author Bernhard von Gunten
 * @created January 16, 2002
 */
public class TMScenarioList extends AbstractTableModel implements FilenameFilter {

  /**
   * Comment for <code>serialVersionUID</code>
   */
  private static final long serialVersionUID = 4121138013301913145L;

  /** Column count */
  private final static int COLUMN_COUNT = 3;

  /** User interests */
  private Vector scenarioFiles = null;

  /** Constructor for the ScenarioList object */
  public TMScenarioList() {
    /*
     * scenarioFiles = new Vector(); // Ihm Directory first File dir =
     * Controller.getSettings().getIhmScenariosPath(); String[] strs =
     * dir.list(this); for (int i = 0; i < strs.length; i++) {
     * scenarioFiles.add(ScenarioXML.getScenarioFile(new File(dir, strs[i]))); } //
     * Then the user path dir = Controller.getSettings().getUserScenariosPath();
     * strs = dir.list(this); for (int i = 0; i < strs.length; i++) {
     * scenarioFiles.add(ScenarioXML.getScenarioFile(new File(dir, strs[i]))); }
     */
  }

  /**
   * Description of the Method
   * 
   * @param dir
   *          Description of the Parameter
   * @param name
   *          Description of the Parameter
   * @return Description of the Return Value
   */
  public boolean accept(File dir, String name) {
    return name.endsWith(".xml");
  }

  /**
   * Returns column count
   * 
   * @return The columnCount value
   */
  public int getColumnCount() {
    return COLUMN_COUNT;
  }

  /**
   * Returns leagueElement name or interested flag
   * 
   * @param row
   *          The row
   * @param column
   *          The collumn
   * @return The value of this field
   */
  public Object getValueAt(int row, int column) {
    return null;
    /*
     * ScenarioFile tmp = (ScenarioFile) scenarioFiles.get(row); if (tmp ==
     * null) return null; switch (column) { case 0: return tmp.getDescription();
     * case 1: return tmp.getScenarioFile().getName(); case 2: { if
     * (tmp.isIhmDBPath()) { return "IHM : " + tmp.getDBName(); } else { return
     * "USER : " + tmp.getDBName(); } } } return null;
     * 
     */
  }

  /**
   * Returns row count
   * 
   * @return The rowCount value
   */
  public int getRowCount() {
    return scenarioFiles.size();
  }

  /**
   * Gets the columnName attribute of the TMUserInterests object
   * 
   * @param column
   *          The column nr
   * @return The columnName value
   */
  public String getColumnName(int column) {
    switch (column) {
    case 0:
      return ClientController.getInstance().getTranslation("TMScenarioList.description");
    case 1:
      return ClientController.getInstance().getTranslation("TMScenarioList.fileName");
    case 2:
      return ClientController.getInstance().getTranslation("TMScenarioList.dbName");
    }

    return "";
  }

}
