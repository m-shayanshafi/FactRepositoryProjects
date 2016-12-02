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
  
package org.icehockeymanager.ihm.clients.devgui.gui;

import java.awt.*;
import java.awt.event.*;
import java.util.*;

import javax.swing.*;

import org.icehockeymanager.ihm.clients.devgui.controller.*;
import org.icehockeymanager.ihm.clients.devgui.gui.about.*;
import org.icehockeymanager.ihm.clients.devgui.gui.bookings.*;
import org.icehockeymanager.ihm.clients.devgui.gui.calendar.*;
import org.icehockeymanager.ihm.clients.devgui.gui.contract.*;
import org.icehockeymanager.ihm.clients.devgui.gui.control.*;
import org.icehockeymanager.ihm.clients.devgui.gui.debug.*;
import org.icehockeymanager.ihm.clients.devgui.gui.eventlog.*;
import org.icehockeymanager.ihm.clients.devgui.gui.gameday.simple.*;
import org.icehockeymanager.ihm.clients.devgui.gui.gameday.text.*;
import org.icehockeymanager.ihm.clients.devgui.gui.lib.*;
import org.icehockeymanager.ihm.clients.devgui.gui.main.*;
import org.icehockeymanager.ihm.clients.devgui.gui.message.*;
import org.icehockeymanager.ihm.clients.devgui.gui.player.*;
import org.icehockeymanager.ihm.clients.devgui.gui.playerattributes.*;
import org.icehockeymanager.ihm.clients.devgui.gui.players.*;
import org.icehockeymanager.ihm.clients.devgui.gui.playerstats.*;
import org.icehockeymanager.ihm.clients.devgui.gui.scoresheet.*;
import org.icehockeymanager.ihm.clients.devgui.gui.sponsoring.*;
import org.icehockeymanager.ihm.clients.devgui.gui.standings.*;
import org.icehockeymanager.ihm.clients.devgui.gui.team.*;
import org.icehockeymanager.ihm.clients.devgui.gui.teamattributes.*;
import org.icehockeymanager.ihm.clients.devgui.gui.teamstats.*;
import org.icehockeymanager.ihm.clients.devgui.gui.training.*;
import org.icehockeymanager.ihm.clients.devgui.gui.arena.*;
import org.icehockeymanager.ihm.clients.devgui.gui.user.*;
import org.icehockeymanager.ihm.lib.*;
import org.icehockeymanager.ihm.game.*;
import org.icehockeymanager.ihm.game.infrastructure.arena.*;
import org.icehockeymanager.ihm.game.league.events.*;
import org.icehockeymanager.ihm.game.match.*;
import org.icehockeymanager.ihm.game.match.textengine.*;
import org.icehockeymanager.ihm.game.player.*;
import org.icehockeymanager.ihm.game.scenario.*;
import org.icehockeymanager.ihm.game.scheduler.events.*;
import org.icehockeymanager.ihm.game.team.*;
import org.icehockeymanager.ihm.game.user.*;

/**
 * Main frame for the game Contains:
 * <p>- Functions to provide a gui to users
 * <p>- Functions called by the controller
 * <p>- Menu
 * 
 * @author Bernhard von Gunten
 * @created December, 2001
 */
public class Desktop extends JFrame implements IhmLogging {

  static final long serialVersionUID = 6513697940702351562L;

  private static final int STATE_SHORTMESSAGE = 0;

  private static final int STATE_ONLINE = 1;

  private static final int STATE_SPECIAL = 2;

  private int state = STATE_SPECIAL;

  /** Pointer on the current user */
  User currentUser = null;

  /** Controls */
  BorderLayout borderLayout1 = new BorderLayout();

  FlowLayout flowLayout = new FlowLayout();

  /** current or last shortMessage panel */
  PanelShortMessage panelShortMessages = null;

  /** current user control panel */
  PanelControl panelControl = null;

  /** Current dialog box */
  JIhmDialog currentDialog = null;

  JMenuBar menuBar = new JMenuBar();

  JMenu menuFile = new JMenu();

  JMenuItem menuExit = new JMenuItem();

  JMenuItem menuSave = new JMenuItem();

  JMenu menuTools = new JMenu();

  JMenuItem menuShowUser = new JMenuItem();

  JMenu menuManager = new JMenu();

  JMenuItem menuShowTeam = new JMenuItem();

  JMenuItem menuShowPlayers = new JMenuItem();

  JMenuItem menuShowProspects = new JMenuItem();

  JMenuItem menuShowTraining = new JMenuItem();

  JMenuItem menuShowBookings = new JMenuItem();

  JMenu menuLeagues = new JMenu();

  JMenuItem menuSponsoring = new JMenuItem();

  JMenuItem menuArena = new JMenuItem();

  JMenuItem menuShowCalendar = new JMenuItem();

  JMenuItem menuShowStandings = new JMenuItem();

  JMenuItem menuShowTeamsAttributes = new JMenuItem();

  JMenuItem menuShowTeamsStats = new JMenuItem();

  JMenuItem menuShowPlayersAttributes = new JMenuItem();

  JMenuItem menuShowTransferList = new JMenuItem();

  JMenuItem menuShowPlayerStats = new JMenuItem();

  JMenu menuHelp = new JMenu();

  JMenuItem menuDebugScheduler = new JMenuItem();

  JMenuItem menuDebug = new JMenuItem();

  JMenuItem menuAbout = new JMenuItem();

  private transient Vector<ActionListener> actionListeners;

  /* Icons */

  /** Construct the frame, call ihmInit */
  public Desktop() {

    enableEvents(AWTEvent.WINDOW_EVENT_MASK);
    try {
      initGUI();
      // JBuilder Stuff
      ihmInit();
      // IHM Stuff
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  /**
   * JBuilder stuff (construct frame
   * 
   * @exception Exception
   *              Exception
   */
  private void initGUI() throws Exception {

    this.setSize(new Dimension(800, 600));
    this.getContentPane().setLayout(borderLayout1);
    this.setIconImage(new ImageIcon(ClientController.getInstance().getGuiResource("icons/ihm18x18.png")).getImage());

    // desktop = new JIhmDesktopPane();
    menuHelp.setText(ClientController.getInstance().getTranslation("menu.help"));
    menuFile.setText(ClientController.getInstance().getTranslation("menu.file"));
    menuTools.setText(ClientController.getInstance().getTranslation("menu.tools"));
    menuManager.setText(ClientController.getInstance().getTranslation("menu.manager"));
    menuLeagues.setText(ClientController.getInstance().getTranslation("menu.leagues"));

    menuExit.setText(ClientController.getInstance().getTranslation("menu.file.exit"));
    menuExit.setIcon(new ImageIcon(ClientController.getInstance().getGuiResource("icons/exit18x18.png")));
    menuExit.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        menuExit_actionPerformed(e);
      }
    });

    menuSave.setText(ClientController.getInstance().getTranslation("menu.file.save"));
    menuSave.setIcon(new ImageIcon(ClientController.getInstance().getGuiResource("icons/savegame18x18.png")));
    menuSave.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        menuSave_actionPerformed(e);
      }
    });

    menuShowStandings.setText(ClientController.getInstance().getTranslation("menu.leagues.standings"));
    menuShowStandings.setIcon(new ImageIcon(ClientController.getInstance().getGuiResource("icons/standings18x18.png")));
    menuShowStandings.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        menuShowStandings_actionPerformed(e);
      }
    });

    menuShowTeamsAttributes.setText(ClientController.getInstance().getTranslation("menu.leagues.teamsAttributes"));
    menuShowTeamsAttributes.setIcon(new ImageIcon(ClientController.getInstance().getGuiResource("icons/teamattributes18x18.png")));
    menuShowTeamsAttributes.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        menuShowTeamsAttributes_actionPerformed(e);
      }
    });

    menuShowTeamsStats.setText(ClientController.getInstance().getTranslation("menu.leagues.teamsStats"));
    menuShowTeamsStats.setIcon(new ImageIcon(ClientController.getInstance().getGuiResource("icons/teamstats18x18.png")));
    menuShowTeamsStats.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        menuShowTeamsStats_actionPerformed(e);
      }
    });

    menuShowPlayerStats.setText(ClientController.getInstance().getTranslation("menu.leagues.playersStats"));
    menuShowPlayerStats.setIcon(new ImageIcon(ClientController.getInstance().getGuiResource("icons/playerstats18x18.png")));
    menuShowPlayerStats.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        menuShowPlayerStats_actionPerformed(e);
      }
    });

    menuShowPlayersAttributes.setText(ClientController.getInstance().getTranslation("menu.leagues.playersAttributes"));
    menuShowPlayersAttributes.setIcon(new ImageIcon(ClientController.getInstance().getGuiResource("icons/playerattributes18x18.png")));
    menuShowPlayersAttributes.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        menuShowPlayersAttributes_actionPerformed(e);
      }
    });

    menuShowTransferList.setText(ClientController.getInstance().getTranslation("menu.leagues.transferList"));
    menuShowTransferList.setIcon(new ImageIcon(ClientController.getInstance().getGuiResource("icons/transferlist18x18.png")));
    menuShowTransferList.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        menuShowTransferList_actionPerformed(e);
      }
    });

    menuShowUser.setText(ClientController.getInstance().getTranslation("menu.tools.user"));
    menuShowUser.setIcon(new ImageIcon(ClientController.getInstance().getGuiResource("icons/user18x18.png")));
    menuShowUser.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        menuShowUser_actionPerformed(e);
      }
    });

    menuShowTeam.setText(ClientController.getInstance().getTranslation("menu.manager.team"));
    menuShowTeam.setIcon(new ImageIcon(ClientController.getInstance().getGuiResource("icons/team18x18.png")));
    menuShowTeam.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        menuShowTeam_actionPerformed(e);
      }
    });

    menuShowCalendar.setText(ClientController.getInstance().getTranslation("menu.leagues.calendar"));
    menuShowCalendar.setIcon(new ImageIcon(ClientController.getInstance().getGuiResource("icons/calendar18x18.png")));
    menuShowCalendar.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        menuShowCalendar_actionPerformed(e);
      }
    });

    menuShowPlayers.setText(ClientController.getInstance().getTranslation("menu.manager.players"));
    menuShowPlayers.setIcon(new ImageIcon(ClientController.getInstance().getGuiResource("icons/players18x18.png")));
    menuShowPlayers.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        menuShowPlayers_actionPerformed(e);
      }
    });

    menuShowProspects.setText(ClientController.getInstance().getTranslation("menu.manager.prospects"));
    menuShowProspects.setIcon(new ImageIcon(ClientController.getInstance().getGuiResource("icons/prospects18x18.png")));
    menuShowProspects.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        menuShowProspects_actionPerformed(e);
      }
    });

    menuShowTraining.setText(ClientController.getInstance().getTranslation("menu.manager.training"));
    menuShowTraining.setIcon(new ImageIcon(ClientController.getInstance().getGuiResource("icons/training18x18.png")));
    menuShowTraining.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        menuShowTraining_actionPerformed(e);
      }
    });

    menuShowBookings.setText(ClientController.getInstance().getTranslation("menu.manager.bookings"));
    menuShowBookings.setIcon(new ImageIcon(ClientController.getInstance().getGuiResource("icons/bookings18x18.png")));
    menuShowBookings.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        menuShowBookings_actionPerformed(e);
      }
    });

    menuAbout.setText(ClientController.getInstance().getTranslation("menu.help.about"));
    menuAbout.setIcon(new ImageIcon(ClientController.getInstance().getGuiResource("icons/about18x18.png")));
    menuAbout.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        menuAbout_actionPerformed(e);
      }
    });

    menuDebug.setText(ClientController.getInstance().getTranslation("menu.help.debug"));
    menuDebug.setIcon(new ImageIcon(ClientController.getInstance().getGuiResource("icons/debug18x18.png")));
    menuDebug.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        menuDebug_actionPerformed(e);
      }
    });

    menuDebugScheduler.setText(ClientController.getInstance().getTranslation("menu.help.debugScheduler"));
    menuDebugScheduler.setIcon(new ImageIcon(ClientController.getInstance().getGuiResource("icons/debug18x18.png")));
    menuDebugScheduler.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        menuDebugScheduler_actionPerformed(e);
      }
    });

    menuSponsoring.setText(ClientController.getInstance().getTranslation("menu.manager.sponsoring"));
    menuSponsoring.setIcon(new ImageIcon(ClientController.getInstance().getGuiResource("icons/debug18x18.png")));
    menuSponsoring.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent evt) {
        menuShowSponsoring_actionPerformed(evt);
      }
    });

    menuArena.setText(ClientController.getInstance().getTranslation("menu.manager.arena"));
    menuArena.setIcon(new ImageIcon(ClientController.getInstance().getGuiResource("icons/debug18x18.png")));
    menuArena.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent evt) {
        menuShowArena_actionPerformed(evt);
      }
    });

    menuBar.setMargin(new Insets(0, 0, 0, 0));

    menuBar.add(menuFile);
    menuBar.add(menuTools);
    menuBar.add(menuManager);
    menuBar.add(menuLeagues);
    menuBar.add(menuHelp);
    menuFile.add(menuSave);
    menuFile.addSeparator();
    menuFile.add(menuExit);
    menuHelp.add(menuDebugScheduler);
    menuHelp.add(menuDebug);
    menuHelp.addSeparator();
    menuHelp.add(menuAbout);
    menuLeagues.add(menuShowCalendar);
    menuLeagues.addSeparator();
    menuLeagues.add(menuShowStandings);
    menuLeagues.addSeparator();
    menuLeagues.add(menuShowTransferList);
    menuLeagues.addSeparator();
    menuLeagues.add(menuShowTeamsAttributes);
    menuLeagues.add(menuShowTeamsStats);
    menuLeagues.addSeparator();
    menuLeagues.add(menuShowPlayersAttributes);
    menuLeagues.add(menuShowPlayerStats);
    menuTools.add(menuShowUser);
    menuManager.add(menuShowTeam);
    menuManager.addSeparator();
    menuManager.add(menuShowPlayers);
    menuManager.addSeparator();
    menuManager.add(menuShowProspects);
    menuManager.addSeparator();
    menuManager.add(menuShowBookings);
    menuManager.addSeparator();
    menuManager.add(menuShowTraining);
    menuManager.add(menuSponsoring);
    menuManager.add(menuArena);
    this.setJMenuBar(menuBar);
  }

  /*
   * ==============================================================================
   * DESKTOP METHODS CALLED INTERNAL OR FROM OTHER FRAMES
   * ==============================================================================
   */

  private void ihmInit() {
    String title = ClientController.getInstance().getTranslation("app.title") + " " + GameVersion.IHM_VERSION;
    this.setTitle(title);

    this.showShortMessagePanel();
  }

  protected void processWindowEvent(WindowEvent e) {
    super.processWindowEvent(e);
    if (e.getID() == WindowEvent.WINDOW_CLOSING) {
      ClientController.getInstance().exit();
    }
  }

  /** Turns Menu on */
  private void turnMenuOn() {
    // Remove the menus
    this.getJMenuBar().setVisible(true);
    this.getJMenuBar().setEnabled(true);
  }

  /** Turns Menu off */
  private void turnMenuOff() {
    // Remove the menus
    this.getJMenuBar().setVisible(false);
    this.getJMenuBar().setEnabled(false);
  }

  /** Removes all panels from the desktop */
  private void cleanUp() {
    // Set to border layout
    this.getContentPane().setLayout(borderLayout1);

    // Remove all existing panels
    this.getContentPane().removeAll();

    // Remove current dialog
    this.closeDialog();

    // Update content pane
    this.getContentPane().validate();

    try {
      Thread.sleep(100);
    } catch (Exception err) {
    }
  }

  /**
   * Centers a dialog
   * 
   * @param doc
   */
  private void centerDialog(JIhmDialog doc) {
    int x = (this.getWidth() - doc.getWidth()) / 2;
    int y = (this.getHeight() - doc.getHeight()) / 2;
    doc.setLocation(x, y);
  }

  /**
   * Shows panel as dialog box
   * 
   * @param panel
   */
  private void showPanelAsDialog(JIhmPanel panel) {
    panel.updateTranslation();
    currentDialog = new JIhmDialog(this, panel);
    currentDialog.setModal(true);
    currentDialog.setResizable(false);
    centerDialog(currentDialog);
    currentDialog.setVisible(true);
  }

  /**
   * Closes the current dialog
   * 
   */
  public void closeDialog() {
    // Remove dialog if needed
    if (currentDialog != null) {
      currentDialog.dispose();
      currentDialog = null;
    }
    
    // After a dialog has been closed, update all data on the lower panel
    if (panelControl != null && panelControl.getContent() != null) { 
      panelControl.getContent().updateData();
    }
  }

  /**
   * Shows Panel in control panel (main screen)
   * 
   * @param panel
   */
  private void showPanelInControl(JIhmPanel panel) {
    // There could be open dialogs
    closeDialog();

    // Update datea & translation
    panel.updateData();
    panel.updateTranslation();

    // Show panel
    panelControl.setContent(panel, true);
  }

  private void showPanelInControlFlow(JIhmPanel panel) {
    // There could be open dialogs
    closeDialog();

    // Update translation
    panel.updateTranslation();

    // Show panel
    panelControl.setContent(panel, false);
  }

  /*
   * ==============================================================================
   * FUNCTIONS DIRECTLY CALLED FROM USER (I.E. MENU)
   * ==============================================================================
   */
  /**
   * Shows a TeamScoreSheet
   * 
   * @param match
   *          The match to show
   */
  public void showPanelScoreSheet(Match match) {
    PanelScoreSheet doc = new PanelScoreSheet(currentUser, match);
    showPanelAsDialog(doc);
  }

  /**
   * Shows a TeamFrame
   * 
   * @param team
   *          The team to show
   */
  public void showPanelTeam(Team team) {
    PanelTeam doc = new PanelTeam(currentUser, team);
    showPanelInControl(doc);
  }

  /**
   * Shows a PlayerFrame
   * 
   * @param player
   *          The player to show
   */
  public void showPanelPlayer(Player player) {
    PanelPlayer doc = new PanelPlayer(currentUser, player);
    showPanelInControl(doc);
  }

  /**
   * Shows debug frame
   * 
   * @param obj
   *          Object
   * @param name
   *          String
   */
  public void showPanelDebug(Object obj, String name) {
    PanelDebug doc = new PanelDebug(currentUser, obj, name);
    showPanelInControl(doc);
  }

  /**
   * Exit game
   * 
   * @param e
   *          Source event
   */
  void menuExit_actionPerformed(ActionEvent e) {
    ClientController.getInstance().exit();
  }

  /**
   * Save game
   * 
   * @param e
   *          Source event
   */
  void menuSave_actionPerformed(ActionEvent e) {
    ClientController.getInstance().save();
  }

  /**
   * Create new Standings frame for the current user
   * 
   * @param e
   *          Source event
   */
  void menuShowStandings_actionPerformed(ActionEvent e) {
    PanelStandings doc = new PanelStandings(currentUser);
    showPanelInControl(doc);
  }

  /**
   * Create new TeamAttributes frame for the current user
   * 
   * @param e
   *          Source event
   */
  void menuShowTeamsAttributes_actionPerformed(ActionEvent e) {
    PanelTeamAttributes doc = new PanelTeamAttributes(currentUser);
    showPanelInControl(doc);
  }

  /**
   * Create new TeamStats frame for the current user
   * 
   * @param e
   *          Source event
   */
  void menuShowTeamsStats_actionPerformed(ActionEvent e) {
    PanelTeamStats doc = new PanelTeamStats(currentUser);
    showPanelInControl(doc);
  }

  /**
   * Create new PlayerStats frame for the current user
   * 
   * @param e
   *          Source event
   */
  void menuShowPlayerStats_actionPerformed(ActionEvent e) {
    PanelPlayerStats doc = new PanelPlayerStats(currentUser);
    showPanelInControl(doc);
  }

  /**
   * Create new PlayerAttributes frame for the current user
   * 
   * @param e
   *          Source event
   */
  void menuShowPlayersAttributes_actionPerformed(ActionEvent e) {
    PanelPlayerAttributes doc = new PanelPlayerAttributes(currentUser, false);
    showPanelInControl(doc);
  }

  void menuShowTransferList_actionPerformed(ActionEvent e) {
    PanelPlayerAttributes doc = new PanelPlayerAttributes(currentUser, true);
    showPanelInControl(doc);
  }

  /**
   * Create new User frame for the current user
   * 
   * @param e
   *          Source event
   */
  void menuShowUser_actionPerformed(ActionEvent e) {
    PanelUser doc = new PanelUser(currentUser, currentUser);
    showPanelInControl(doc);
  }

  /**
   * Create new Team frame for the current user
   * 
   * @param e
   *          Source event
   */
  void menuShowTeam_actionPerformed(ActionEvent e) {
    PanelTeam doc = new PanelTeam(currentUser, currentUser.getTeam());
    showPanelInControl(doc);
  }

  /**
   * Create new Team frame for the current user
   * 
   * @param e
   *          Source event
   */
  void menuShowPlayers_actionPerformed(ActionEvent e) {
    PanelPlayers doc = new PanelPlayers(currentUser, currentUser.getTeam(), false);
    showPanelInControl(doc);
  }

  void menuShowProspects_actionPerformed(ActionEvent e) {
    PanelPlayers doc = new PanelPlayers(currentUser, currentUser.getTeam(), true);
    showPanelInControl(doc);
  }

  /**
   * Create new Calendar frame for the current user
   * 
   * @param e
   *          Source event
   */
  void menuShowCalendar_actionPerformed(ActionEvent e) {
    PanelCalendar doc = new PanelCalendar(currentUser, false);
    showPanelInControl(doc);
  }

  void menuDebugScheduler_actionPerformed(ActionEvent e) {
    PanelCalendar doc = new PanelCalendar(currentUser, true);
    showPanelInControl(doc);
  }

  /**
   * Fancy About screen
   * 
   * @param e
   *          Source event
   */
  void menuAbout_actionPerformed(ActionEvent e) {
    PanelAbout doc = new PanelAbout(currentUser);
    showPanelInControlFlow(doc);
  }

  /**
   * Debug screen
   * 
   * @param e
   *          Source event
   */
  void menuDebug_actionPerformed(ActionEvent e) {
    PanelDebug doc = new PanelDebug(currentUser, GameController.getInstance().getScenario(), "Scenario");
    showPanelInControl(doc);
  }

  void menuShowBookings_actionPerformed(ActionEvent e) {
    PanelBookings doc = new PanelBookings(currentUser.getTeam());
    showPanelInControl(doc);
  }

  void menuShowSponsoring_actionPerformed(ActionEvent e) {
    PanelSponsoring doc = new PanelSponsoring(currentUser.getTeam());
    showPanelInControl(doc);
  }
  
  void menuShowArena_actionPerformed(ActionEvent e) {
    PanelArena doc = new PanelArena(currentUser.getTeam());
    showPanelInControl(doc);
  }

  void menuShowTraining_actionPerformed(ActionEvent e) {
    PanelTraining doc = new PanelTraining(currentUser, currentUser.getTeam());
    showPanelInControl(doc);
  }

  public void showFrameContract(Player player, int mode) {
    PanelContract doc = new PanelContract(currentUser, player, mode);
    this.showPanelAsDialog(doc);
  }

  public void showFrameArenaCategory(ArenaCategory arenaCategory) {
    PanelArenaCategory doc = new PanelArenaCategory(currentUser, arenaCategory);
    this.showPanelAsDialog(doc);
  }

  public void showPanelEventLog() {
    PanelEventLog doc = new PanelEventLog(currentUser);
    this.showPanelInControl(doc);
  }

  /*
   * ==============================================================================
   * FUNCTIONS CALLED BY THE CONTROLLER
   * ==============================================================================
   */
  /**
   * Show short Message
   * 
   * @param title
   *          Title of the message
   * @param msg
   *          Message to show
   */
  public void showShortMessage(String title, String msg) {
    if (this.state != STATE_SHORTMESSAGE) {
      this.showShortMessagePanel();
    }
    this.panelShortMessages.showMessage(title + " : " + msg);
  }

  /**
   * Show short Message
   * 
   * @param msg
   *          Message to show
   */
  public void showShortMessage(String msg) {
    if (this.state != STATE_SHORTMESSAGE) {
      this.showShortMessagePanel();
    }
    this.panelShortMessages.showMessage(msg);
  }

  /**
   * Show online Message
   * 
   * @param msg
   *          Message to show
   */
  public void showOnlineMessage(String msg) {

    this.cleanUp();
    this.turnMenuOff();

    PanelMessage panel = new PanelMessage(this, msg);
    panel.updateTranslation();
    this.getContentPane().add(panel, BorderLayout.CENTER);

    // Update content pane
    this.getContentPane().validate();

    this.state = STATE_SPECIAL;

  }

  /**
   * Returns true, if the user clicks yes to the given question
   * 
   * @param message
   * @return boolean (YES = true)
   */
  public boolean showYesNoMessage(String message) {
    
    UIManager.put("OptionPane.yesButtonText", ClientController.getInstance().getTranslation("ihm.yes"));
    UIManager.put("OptionPane.noButtonText", ClientController.getInstance().getTranslation("ihm.no"));
    
    int result = JOptionPane.showConfirmDialog(this, message, "alert", JOptionPane.YES_NO_OPTION);

    return (result == JOptionPane.YES_OPTION);
  }
  
  
  
  public void moveOn() {
    fireActionPerformed(new ActionEvent(this, 0, "DesktopFinish"));
  }

  /**
   * Show Matches online
   * 
   * @param event
   *          Source event
   */
  public void doSchedulerGameDayEvent(GameDayMatchesEvent event) {
    this.cleanUp();
    this.turnMenuOff();

    // Show simple match gui for every engine, or show specified gui for each engine
    
    if (GameController.getInstance().getScenario().getScenarioSettings().MATCH_USE_SIMPLEGUI == 1) {

      PanelSimpleGameDay panel = new PanelSimpleGameDay(this, currentUser, event);
      panel.updateTranslation();
      this.getContentPane().add(panel, BorderLayout.CENTER);

      
    } else {
    
      Class matchClass = null;
      
      try {
        matchClass = Class.forName(GameController.getInstance().getScenario().getScenarioSettings().MATCH_ENGINE);
      } catch (Exception err) {
        //TODO: Handle exception
      }
  
      
      if (TextMatch.class.isAssignableFrom(matchClass)) {
        PanelTextGameDay panel = new PanelTextGameDay(this, currentUser, event);
        panel.updateTranslation();
        this.getContentPane().add(panel, BorderLayout.CENTER);
      }
      
      // Add other guis here
      
    }
    

    // Update content pane
    this.getContentPane().validate();

    this.state = STATE_SPECIAL;

  }

  public void doSchedulerBreakerGroupEvent(User user, SchedulerBreakerEvent[] events) {
    this.currentUser = user;

    // Clean up
    this.cleanUp();

    // Turn menus on
    this.turnMenuOn();

    // Start new user control panel
    this.panelControl = new PanelControl(this, user, events);
    panelControl.updateTranslation();

    // Set panel
    this.getContentPane().add(panelControl, BorderLayout.CENTER);

    // Update content pane
    this.getContentPane().validate();

    this.showPanelEventLog();

    this.state = STATE_ONLINE;

  }

  // Action event procedures ...

  /**
   * Description of the Method
   * 
   * @param l
   *          Listener to remove
   */
  public synchronized void removeActionListener(ActionListener l) {
    if (actionListeners != null && actionListeners.contains(l)) {
      Vector<ActionListener> v = new Vector<ActionListener>(actionListeners);
      v.removeElement(l);
      actionListeners = v;
    }
  }

  /**
   * Adds a feature to the ActionListener attribute of the Desktop object
   * 
   * @param l
   *          The feature to be added to the ActionListener attribute
   */
  public synchronized void addActionListener(ActionListener l) {
    Vector<ActionListener> v = actionListeners == null ? new Vector<ActionListener>(2) : new Vector<ActionListener>(actionListeners);
    if (!v.contains(l)) {
      v.addElement(l);
      actionListeners = v;
    }
  }

  /**
   * Description of the Method
   * 
   * @param e
   *          Source event
   */
  protected synchronized void fireActionPerformed(ActionEvent e) {
    if (actionListeners != null) {
      Vector<ActionListener> listeners = actionListeners;
      int count = listeners.size();
      for (int i = 0; i < count; i++) {
        listeners.elementAt(i).actionPerformed(e);
      }
    }
  }

  /** Turns the desktop off, and shows the short message screen */
  private void showShortMessagePanel() {
    // cleanUp
    this.cleanUp();

    // menus off
    this.turnMenuOff();

    // Show blank screen
    this.panelShortMessages = new PanelShortMessage();

    this.getContentPane().add(panelShortMessages, BorderLayout.CENTER);

    // Update content pane
    this.getContentPane().validate();

    this.state = STATE_SHORTMESSAGE;
  }

  /** Show the main screen 
   * @param scenario */
  public void showMainScreen(Scenario scenario) {
    // cleanUp
    this.cleanUp();

    this.getContentPane().setLayout(flowLayout);

    this.turnMenuOff();

    PanelMain panel = new PanelMain(this, scenario);
    panel.updateTranslation();

    this.getContentPane().add(panel, BorderLayout.CENTER);
    this.getContentPane().validate();

    this.state = STATE_SPECIAL;

  }

}