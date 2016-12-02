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
import java.net.*;
import java.util.*;
import java.util.logging.*;
import java.util.zip.*;

import javax.swing.*;

import org.icehockeymanager.ihm.clients.devgui.controller.helper.*;
import org.icehockeymanager.ihm.clients.devgui.gui.*;
import org.icehockeymanager.ihm.clients.devgui.ihm.league.*;
import org.icehockeymanager.ihm.game.*;
import org.icehockeymanager.ihm.game.injuries.*;
import org.icehockeymanager.ihm.game.league.*;
import org.icehockeymanager.ihm.game.league.events.*;
import org.icehockeymanager.ihm.game.league.std.events.*;
import org.icehockeymanager.ihm.game.match.*;
import org.icehockeymanager.ihm.game.player.*;
import org.icehockeymanager.ihm.game.randomimpacts.*;
import org.icehockeymanager.ihm.game.scenario.*;
import org.icehockeymanager.ihm.game.scenario.events.*;
import org.icehockeymanager.ihm.game.scheduler.*;
import org.icehockeymanager.ihm.game.scheduler.events.*;
import org.icehockeymanager.ihm.game.sponsoring.*;
import org.icehockeymanager.ihm.game.team.*;
import org.icehockeymanager.ihm.game.user.*;
import org.icehockeymanager.ihm.lib.*;
import org.w3c.dom.*;

/**
 * The Controller class is the "main class" of the game. It is also the
 * interface between the game and the gui part.
 * 
 * @author Bernhard von Gunten & Arik Dasen
 * @created December, 2001
 */
public class ClientController implements IhmLogging {

  /** The user settings */
  private ClientSettings settings = null;

  /** The resources */
  private ClientResources resources = null;

  /** Desktop of ihm */
  private Desktop desktop = null;

  /** The last date the "please wait" was showed */
  private Calendar lastMsgDay = null;

  /** Singelton instance */
  private static ClientController instance = null;

  /** Constructor for the Controller object */
  private ClientController() {
  }

  /** Returns the one and only instance of the ClientController 
   * @return ClientController*/
  public static ClientController getInstance() {
    if (instance == null) {
      instance = new ClientController();
    }

    return instance;
  }

  /** Initialize the controller (and so the game) 
   * @param args */
  public void init(String[] args) {
    try {

      // Check or create the directories in the users home folder
      ClientFileStructure.checkForDirectories();

      // Load client settings (if not found, try to save them for the first
      // time)
      try {
        settings = new ClientSettings(ClientFileStructure.getClientSettingsFileName());
      } catch (java.io.FileNotFoundException err) {
        settings = new ClientSettings();
        settings.writeSettingsToXml(ClientFileStructure.getClientSettingsFileName());
      }

      // Initialize logging
      LogManager.getLogManager().reset();

      FileHandler fileHandler = new FileHandler(ClientFileStructure.getLogFileName());
      fileHandler.setFormatter(new IhmLoggingFileFormatter());
      fileHandler.setLevel(Tools.getLevelByString(settings.LOGLEVEL));
      logger.addHandler(fileHandler);

      ConsoleHandler consoleHandler = new ConsoleHandler();
      consoleHandler.setFormatter(new IhmLoggingConsoleFormatter());
      consoleHandler.setLevel(Tools.getLevelByString(settings.LOGLEVEL));
      logger.addHandler(consoleHandler);

      logger.info("Starting Ice Hockey Manager version " + GameVersion.IHM_VERSION + " ...");

      logger.info("Starting DevGui client controller ...");
      logger.info("Loggers initialized successfully (" + settings.LOGLEVEL + ") ...");
      logger.info("Client settings initialized successfully  ...");

      logger.info("Client resources original path: " + ClientFileStructure.getIhmResourcesPath());
      logger.info("Client resources user path: " + ClientFileStructure.getUserResourcesPath());
      logger.info("Client resources language: " + settings.LANGUAGE);

      resources = new ClientResources(ClientFileStructure.getIhmResourcesPath(), ClientFileStructure.getUserResourcesPath(), settings.LANGUAGE, "");
      logger.info("Client resources initialized successfully ...");

      try {
        javax.swing.plaf.metal.MetalLookAndFeel.setCurrentTheme(new javax.swing.plaf.metal.DefaultMetalTheme());
        UIManager.setLookAndFeel("com.birosoft.liquid.LiquidLookAndFeel");
        logger.info("Swing Liquid look & feel initialized successfully ...");
      } catch (Exception ignored) {
        logger.warning("Swing Liquid look & feel failed ...");
      }

      boolean started = false;

      if (args.length > 0) {

        // Export scenario & exit
        if (args[0].equalsIgnoreCase("-e")) {
          logger.info("Starting scenario export ...");
          exportScenarioAndSettings("default.xml", "default.xml", "injuries.xml", "randomimpacts.xml", "sponsors.xml");
          logger.info("Scenario & settings exported ...");
          started = true;
        }

        if (args[0].equalsIgnoreCase("-l")) {
          desktop = new Desktop();
          desktop.setVisible(true);
          try {
            Thread.sleep(settings.DESKTOP_DELAY_MS);
            // Give the GUI some time to create
          } catch (Exception ignored) {
          }

          showShortMessage("IHM", "Loading scenario from file ...", false);
          newGameOnScenario("default.xml", "default.xml", "injuries.xml", "randomimpacts.xml", "sponsors.xml", false);
          started = true;
        }

        if (args[0].equalsIgnoreCase("-a")) {
          desktop = new Desktop();
          desktop.setVisible(true);
          try {
            Thread.sleep(settings.DESKTOP_DELAY_MS);
            // Give the GUI some time to create
          } catch (Exception ignored) {
          }

          showShortMessage("IHM", "Loading scenario from file ...", false);
          newGameOnScenario("default.xml", "default.xml", "injuries.xml", "randomimpacts.xml", "sponsors.xml", true);
          started = true;
        }

        if (args[0].equalsIgnoreCase("-d")) {
          logger.info("Starting desktop on development dummy ...");
          desktop = new Desktop();

          try {
            Thread.sleep(settings.DESKTOP_DELAY_MS);
            // Give the GUI some time to create
          } catch (Exception ignored) {
          }

          showShortMessage("IHM", "Loading scenario from dummy ...", false);
          newGameOnDummy("injuries.xml", "randomimpacts.xml", "sponsors.xml");
          started = true;
        }

      }

      if (!started) {
        logger.warning("Usage:");
        logger.warning("  ihm -e   Creates new default scenario & settings files.");
        logger.warning("  ihm -l   Starts GUI from standard scenario file.");
        logger.warning("  ihm -d   Starts GUI from dummy scenario.");
      }

    } catch (Exception e) {
      System.err.println("Client Controller INIT failed with error :");
      e.printStackTrace(System.err);
    }
  }

  /**
   * Creates a dummy scenario and exports it to scenario XML File.
   * 
   * @param scenarioFile
   *          Scneario file to create
   * @param settingsFile
   *          Settings file to read
   * @param injuriesFile
   *          injuries file to read
   * @param randomImpactsFile
   *          Random impacts file to read
   * @param sponsorsFile
   *          Sponsors file to read
   * @throws Exception
   */
  private void exportScenarioAndSettings(String scenarioFile, String settingsFile, String injuriesFile, String randomImpactsFile, String sponsorsFile) throws Exception {
    InjuryData[] injuries = Injuries.readInjuriesFromXMLDatabase(new File(ClientFileStructure.getIhmInjuriesPath().getAbsolutePath(), injuriesFile));
    RandomImpact[] randomImpacts = RandomImpacts.readRandomImpactsFromXMLDatabase(new File(ClientFileStructure.getIhmRandomImpactsPath().getAbsolutePath(), randomImpactsFile));
    SponsorData[] sponsors = Sponsoring.readSponsorsFromXMLDatabase(new File(ClientFileStructure.getIhmSponsoringPath().getAbsolutePath(), sponsorsFile));

    Scenario scenario = new ScenarioDummy(new ScenarioSettings(), injuries, randomImpacts, sponsors).getScenario();
    File xmlFile = new File(ClientFileStructure.getIhmScenariosPath().getAbsolutePath(), scenarioFile);
    ScenarioTools.writeScenarioToXMLDatabase(scenario, xmlFile);

    File settingsXML = new File(ClientFileStructure.getIhmScenarioSettingsPath(), settingsFile);
    new ScenarioSettings().writeSettingsToXml(settingsXML);

  }

  /**
   * Starts a new game based on scenario file.
   * 
   * @param scenarioFileName
   *          Scenario file to read
   * @param settingsFile
   *          Settings file to read
   * @param injuriesFile
   *          Injuries file to read
   * @param randomImpactsFile
   *          Random impacts file to read
   * @param sponsorsFile
   *          Sponsors file to read
   * @param autostart For development time , autostart with Team 0
   * @throws Exception
   */
  private void newGameOnScenario(String scenarioFileName, String settingsFile, String injuriesFile, String randomImpactsFile, String sponsorsFile, boolean autostart) throws Exception {
    try {

      File xmlFile = new File(ClientFileStructure.getIhmScenariosPath().getAbsolutePath(), scenarioFileName);
      logger.info("Loading scenario from : " + xmlFile.getName());

      Document doc = ToolsXML.loadXmlFile(xmlFile);

      Country[] countries = ScenarioTools.readCountriesFromXMLDatabase(doc);
      logger.info("Scenario: " + countries.length + " countries loaded.");
      Player[] players = ScenarioTools.readPlayersFromXMLDatabase(doc, countries);
      logger.info("Scenario: " + players.length + " players loaded.");
      Team[] teams = ScenarioTools.readTeamsFromXMLDatabase(doc, players);
      logger.info("Scenario: " + teams.length + " teams loaded.");
      LeagueOwner[] leagueOwners = ScenarioTools.readLeagueOwnersFromXMLDatabase(doc, teams);
      logger.info("Scenario: " + leagueOwners.length + " league owners loaded.");
      String[] firstNames = ScenarioTools.readFirstNamesFromXMLDatabase(doc);
      logger.info("Scenario: " + firstNames.length + " first names loaded.");
      String[] lastNames = ScenarioTools.readLasttNamesFromXMLDatabase(doc);
      logger.info("Scenario: " + lastNames.length + " last names loaded.");

      InjuryData[] injuries = Injuries.readInjuriesFromXMLDatabase(new File(ClientFileStructure.getIhmInjuriesPath().getAbsolutePath(), injuriesFile));
      logger.info("Scenario: " + injuries.length + " injuries loaded.");
      RandomImpact[] randomImpacts = RandomImpacts.readRandomImpactsFromXMLDatabase(new File(ClientFileStructure.getIhmRandomImpactsPath().getAbsolutePath(), randomImpactsFile));
      logger.info("Scenario: " + randomImpacts.length + " impacts loaded.");
      SponsorData[] sponsors = Sponsoring.readSponsorsFromXMLDatabase(new File(ClientFileStructure.getIhmSponsoringPath().getAbsolutePath(), sponsorsFile));
      logger.info("Scenario: " + sponsors.length + " sponsors loaded.");

      File settingsXML = new File(ClientFileStructure.getIhmScenarioSettingsPath().getAbsolutePath(), settingsFile);
      ScenarioSettings scenarioSettings = new ScenarioSettings(settingsXML);
      logger.info("Scenario: Settings loaded.");

      Scenario scenario = new Scenario(leagueOwners, teams, players, countries, injuries, randomImpacts, lastNames, firstNames, scenarioSettings, sponsors);
      logger.info("Scenario created successfully ...");

      logger.info("Starting desktop user interface ...");

      try {
        desktop.addActionListener(new DesktopListenerNotify(this, desktop));

        if (autostart) {
          Team team = scenario.getTeams()[0];
          User[] users = new User[1];
          User usr = new User("Mr. Auto User");
          usr.setTeam(team);
          users[0] = usr;
          scenario.setUsers(users);
          GameController.getInstance().init(scenario, ClientController.getInstance().getSchedulerListener());
        } else {
          desktop.showMainScreen(scenario);
          synchronized (this) {
            this.wait();
          }
        }
      } catch (InterruptedException err) {
      }

      GameController.getInstance().startGame();

    } catch (Exception err) {
      logger.log(Level.SEVERE, "Scenario/Desktop creation failed!", err);
    }

  }

  /**
   * Returns a scheduler listener for this ClientController
   * 
   * @return Scheduler listener
   */
  public SchedulerListener getSchedulerListener() {
    return new SchedulerListener() {
      public void schedulerSpecialEvent(SchedulerSpecialEvent e) {
        if (e instanceof GameDayMatchesEvent) {
          doSchedulerGameDayEvent((GameDayMatchesEvent) e);
        }
        e.play();
        moveOn();
      }

      public void schedulerBreakerGroupEvent(SchedulerBreakerGroupEvent e) {
        doSchedulerBreakerGroupEvent(e);
        e.play();
        moveOn();
      }

      public void schedulerInternalEvent(SchedulerInternalEvent e) {
        showCurrentDayAsMessage(e);
      }

    };

  }

  /**
   * Shows the current Day as shortmessage (only once per day)
   * 
   * @param e
   */
  private void showCurrentDayAsMessage(SchedulerEvent e) {

    if (lastMsgDay == null || lastMsgDay.before(e.getDay())) {
      try {
        showShortMessage("IHM", Tools.dateToString(e.getDay().getTime(), Tools.DATE_FORMAT_EU_DATE), false);
      } catch (Exception ignored) {
      }
      lastMsgDay = e.getDay();
    }

  }

  /**
   * Starts new game on dummy data
   * 
   * @param injuriesFile
   *          Injuries to read
   * @param randomImpactsFile
   *          Random impacts to read
   * @param sponsorsFile
   *          Sponsors to read
   * @throws Exception
   */
  public void newGameOnDummy(String injuriesFile, String randomImpactsFile, String sponsorsFile) throws Exception {

    // = getSchedulerListener();
    InjuryData[] injuries = Injuries.readInjuriesFromXMLDatabase(new File(ClientFileStructure.getIhmInjuriesPath().getAbsolutePath(), injuriesFile));
    RandomImpact[] randomImpacts = RandomImpacts.readRandomImpactsFromXMLDatabase(new File(ClientFileStructure.getIhmRandomImpactsPath().getAbsolutePath(), randomImpactsFile));
    SponsorData[] sponsors = Sponsoring.readSponsorsFromXMLDatabase(new File(ClientFileStructure.getIhmSponsoringPath().getAbsolutePath(), sponsorsFile));

    Scenario scenario = new ScenarioDummy(new ScenarioSettings(), injuries, randomImpacts, sponsors).getScenario();

    GameController.getInstance().init(scenario, ClientController.getInstance().getSchedulerListener());
    GameController.getInstance().startGame();
  }

  /** Saves the scenario */
  public void saveScenario() {

    String filename = new File(ClientFileStructure.getUserSavegamesPath(), "savegame.ihm").getAbsolutePath();
    ObjectOutputStream out;
    try {
      FileOutputStream fout = new FileOutputStream(filename);
      GZIPOutputStream gzout = new GZIPOutputStream(fout);
      // spart platz :-)
      out = new ObjectOutputStream(gzout);
      out.writeObject(GameController.getInstance().getScenario());
      out.flush();
      out.close();
    } catch (Exception e) {
      logger.log(Level.SEVERE, "Save scenario failed", e);
    }
  }

  /** Load a scenario 
   * @return Scenario */
  public Scenario loadScenario() {

    String filename = new File(ClientFileStructure.getUserSavegamesPath(), "savegame.ihm").getAbsolutePath();
    ObjectInputStream in;
    try {
      FileInputStream fin = new FileInputStream(filename);
      GZIPInputStream gzin = new GZIPInputStream(fin);
      in = new ObjectInputStream(gzin);
      Scenario loaded = (Scenario) in.readObject();
      in.close();
      return loaded;
    } catch (Exception e) {
      logger.log(Level.SEVERE, "Load scenario failed", e);
      return null;
    }

  }

  /**
   * Gets the settings attribute of the Controller class
   * 
   * @return The settings value
   */
  public ClientSettings getSettings() {
    return settings;
  }

  /**
   * Returns the desktop of this game
   * 
   * @return The desktop of this game
   */
  public Desktop getDesktop() {
    return desktop;
  }

  /**
   * Get Translation, returns TRANSLATION + <KEY>if not found.
   * 
   * @param key
   *          The key of a translation looking for
   * @return The translation or "TRANSLATION + <KEY>"
   */
  public String getTranslation(String key) {
    String result = getStrictTranslation(key);
    if (result == null) {
      logger.warning("Translation not found! Key: " + key);
      return "##TRANSLATION##" + key;
    }
    return result;
  }

  /**
   * Get Translation, returns null if not found. Usefull for translations with
   * MasterTranslations available.
   * 
   * @param key
   *          The key of a translation looking for
   * @return The translation
   */
  public String getStrictTranslation(String key) {
    if (key == null) {
      return null;
    }
    return resources.getTranslationsProperties().getProperty(key);
  }

  /**
   * Gets the guiResource attribute of the Controller class
   * 
   * @param file
   *          Description of the Parameter
   * @return The guiResource value
   */
  public URL getGuiResource(String file) {
    URL result = null;
    try {
      result = resources.getGuiResource(file);
      if (result == null) {
        logger.warning("GUI resource not found! File: " + file);
      }
    } catch (Exception err) {
      logger.log(Level.SEVERE, "Gui resource load failed!", err);
    }

    return result;
  }

  // ============== METHODS CALLED BY THE GUI ======================

  /** Exits game and saves the scenario */
  public void exit() {
    logger.info("Exiting the client ...");
    try {
      logger.info("Save settings ...");
      settings.writeSettingsToXml(ClientFileStructure.getClientSettingsFileName());
    } catch (Exception error) {
      logger.log(Level.SEVERE, "Settings save failed!", error);
    }
    logger.info("Exit");
    System.exit(0);
  }

  /** Save game */
  public void save() {
    try {
      logger.info("Save game ...");
      saveScenario();
    } catch (Exception error) {
      logger.log(Level.SEVERE, "Save failed!", error);
    }
  }

  /**
   * Move on in the game thread
   * 
   */
  public void moveOn() {
    showShortMessage("IHM", "Please wait ...", true);
    GameController.getInstance().moveOn();
  }

  // ======================= EVENT HELPERS =========================

  /**
   * Special method that displays a fullscreen message, and stops the gui
   * @param user 
   * @param events 
   */
  private void showDesktopForUser(final User user, final SchedulerBreakerEvent[] events) {
    try {
      desktop.addActionListener(new DesktopListenerNotify(getInstance(), desktop));
      desktop.doSchedulerBreakerGroupEvent(user, events);
      synchronized (getInstance()) {
        getInstance().wait();
      }
    } catch (InterruptedException err) {
    }
  }

  /**
   * Special method that displays a fullscreen message, and stops the gui
   * @param message 
   */
  private void showOnlineMessage(final String message) {
    try {
      desktop.addActionListener(new DesktopListenerNotify(getInstance(), desktop));
      desktop.showOnlineMessage(message);
      synchronized (getInstance()) {
        getInstance().wait();
      }
    } catch (InterruptedException err) {
    }
  }

  /**
   * Special method that displays a message on the desktop without stoping the
   * game thread
   * 
   * @param title
   *          The title for the short message
   * @param msg
   *          The message to show
   * @param sleep 
   */
  private void showShortMessage(String title, String msg, boolean sleep) {
    desktop.showShortMessage(title, msg);
    if (sleep) {
      try {
        Thread.sleep(100);
        // Stop everything for some time ;-)
      } catch (Exception ignored) {
      }
    }

  }

  // ************************** STANDARD EVENTS ******************

  /**
   * Play some ice hockey ...
   * 
   * @param e
   *          The SchedulerGameDayEvent
   */
  private void doSchedulerGameDayEvent(GameDayMatchesEvent e) {
    try {

      if (e.needsOnline()) {
        desktop.addActionListener(new DesktopListenerNotify(getInstance(), desktop));
        desktop.doSchedulerGameDayEvent(e);
        synchronized (getInstance()) {
          getInstance().wait();
        }
      } else {
        showShortMessage("IHM", LeagueTools.getGameDayDescription(e), true);
        MatchController controller = new MatchController(e, 0);
        controller.start();
        controller.join();
      }

    } catch (InterruptedException err) {
    }

  }

  /**
   * This is a special Event, called before all GameDays. So Users could make
   * changes.
   * 
   * @param e
   *          The SchedulerStopBeforeGameDayEvent
   */
  private void doSchedulerBreakerGroupEvent(SchedulerBreakerGroupEvent e) {
    // Here we check, if there are ShedulerBreaker events which are so
    // interessting, that we show a special info about them. Today we only show
    // a message on the screen.
    final SchedulerBreakerEvent[] allEvents = e.getAllBreakers();
    for (int i = 0; i < allEvents.length; i++) {
      if (allEvents[i] instanceof GameDayArrivedEvent) {
        // Consume, it's enough to show the GUI later on
      } else if (allEvents[i] instanceof SeasonStartBreakerEvent) {
        showOnlineMessage(getInstance().getTranslation("events.yearstart"));
      } else if (allEvents[i] instanceof SeasonEndBreakerEvent) {
        showOnlineMessage(getInstance().getTranslation("events.yearend"));
      } else if (allEvents[i] instanceof StdFirstPlaceKnownEvent) {
        StdFirstPlaceKnownEvent event = (StdFirstPlaceKnownEvent) allEvents[i];
        String winner = event.getWinner().getTeamInfo().getTeamName();
        String league = LeagueTools.getLeagueDescription(event.getLeague());
        String msg = league + " : " + getInstance().getTranslation("stdleague.firstplace") + " : " + winner;
        showOnlineMessage(msg);
      } else if (allEvents[i] instanceof StdLastPlaceKnownEvent) {
        StdLastPlaceKnownEvent event = (StdLastPlaceKnownEvent) allEvents[i];
        String loser = event.getLoser().getTeamInfo().getTeamName();
        String league = LeagueTools.getLeagueDescription(event.getLeague());
        String msg = league + " : " + getInstance().getTranslation("stdleague.lastplace") + " : " + loser;
        showOnlineMessage(msg);
      } else if (allEvents[i] instanceof StdRelegationPlayoffsFinishedEvent) {
        StdRelegationPlayoffsFinishedEvent event = (StdRelegationPlayoffsFinishedEvent) allEvents[i];
        if (event.relegation()) {
          String winner = event.getWinner().getTeamInfo().getTeamName();
          String loser = event.getLoser().getTeamInfo().getTeamName();
          String league = LeagueTools.getLeagueDescription(event.getLeague());
          String msg = getInstance().getTranslation("stdleague.nextSeason") + " " + league + " : " + winner + " " + getInstance().getTranslation("stdleague.replacing") + " " + loser;
          showOnlineMessage(msg);
        }
      } else {
        showOnlineMessage(allEvents[i].getMessageKey());
      }
    }

    // Now show the gui to any user who's interessted in at least one of this
    // Breakers
    User[] users = e.getUsersInterested();
    for (int i = 0; i < users.length; i++) {
      SchedulerBreakerEvent[] events = e.getSchedulerEventBreakersByUser(users[i]);
      showDesktopForUser(users[i], events);
    }

  }

}