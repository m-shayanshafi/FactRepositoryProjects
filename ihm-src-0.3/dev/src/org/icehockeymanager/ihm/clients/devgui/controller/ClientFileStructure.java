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
  
package org.icehockeymanager.ihm.clients.devgui.controller;

import java.io.*;

/**
 * ClientFileStructure contains static functions to create and check client
 * directories in the user's home directory. The class also contains static
 * functions, to provide locations (directories) of all relevant files (client &
 * game).
 * <p>
 * <bThis is the 200th IHM Class !!!! </b>
 * 
 * @author Bernhard von Gunten
 * @created January, 2005
 */
public class ClientFileStructure {

  /** Path & file to the application properties */
  private final static File homeDir = new File(System.getProperty("user.home") + "/.ihm/devgui");

  /**
   * Returns log file name
   * 
   * @return LogFileName
   */
  public static String getLogFileName() {
    return new File(getUserLogsPath(), "ihm.log").getAbsolutePath();
  }

  /**
   * Returns settings file name
   * 
   * @return SettingsFileName
   */
  public static File getClientSettingsFileName() {
    return new File(getUserSettingsPath(), "settings.xml");
  }

  /** Checks if directories exist and creates them if needed 
   * @throws Exception */
  public static void checkForDirectories() throws Exception {
    if (!homeDir.exists()) {
      homeDir.mkdirs();
    }

    File logs = new File(homeDir.getAbsolutePath() + "/logs");
    if (!logs.exists()) {
      logs.mkdirs();
    }

    File settings = new File(homeDir.getAbsolutePath() + "/settings");
    if (!settings.exists()) {
      settings.mkdirs();
    }

    File saveGames = new File(homeDir.getAbsolutePath() + "/savegames");
    if (!saveGames.exists()) {
      saveGames.mkdirs();
    }

    File client = new File(homeDir.getAbsolutePath() + "/client");
    if (!client.exists()) {
      client.mkdirs();
    }

    File translations = new File(client.getAbsolutePath() + "/translations");
    if (!translations.exists()) {
      translations.mkdirs();
    }

    File gui = new File(client.getAbsolutePath() + "/gui");
    if (!gui.exists()) {
      gui.mkdirs();
    }

    File guiIhm = new File(gui.getAbsolutePath() + "/ihm");
    if (!guiIhm.exists()) {
      guiIhm.mkdirs();
    }
  }

  public static File getUserHome() {
    return homeDir;
  }

  public static File getUserLogsPath() {
    return new File(homeDir.getAbsolutePath() + "/logs");
  }

  public static File getUserSettingsPath() {
    return new File(homeDir.getAbsolutePath() + "/settings");
  }

  public static File getUserSavegamesPath() {
    return new File(homeDir.getAbsolutePath() + "/savegames");
  }

  public static File getUserResourcesPath() {
    return new File(homeDir.getAbsolutePath() + "/client");
  }

  public static File getIhmResourcesPath() {
    return new File("./clients/devgui");
  }

  public static File getIhmScenariosPath() {
    return new File("./game/scenarios");
  }

  public static File getIhmScenarioSettingsPath() {
    return new File("./game/settings");
  }

  public static File getIhmInjuriesPath() {
    return new File("./game/injuries");
  }

  public static File getIhmRandomImpactsPath() {
    return new File("./game/randomimpacts");
  }

  public static File getIhmSponsoringPath() {
    return new File("./game/sponsoring");
  }

}
