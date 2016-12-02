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
  
package org.icehockeymanager.ihm.clients.devgui.gui.calendar;

import java.util.*;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.table.*;

import org.icehockeymanager.ihm.clients.devgui.controller.*;
import org.icehockeymanager.ihm.clients.devgui.gui.lib.*;
import org.icehockeymanager.ihm.clients.devgui.ihm.match.*;
import org.icehockeymanager.ihm.clients.devgui.ihm.scheduler.*;
import org.icehockeymanager.ihm.game.*;
import org.icehockeymanager.ihm.game.league.*;
import org.icehockeymanager.ihm.game.match.*;
import org.icehockeymanager.ihm.game.scheduler.events.*;
import org.icehockeymanager.ihm.game.user.*;
import org.icehockeymanager.ihm.game.league.events.*;

/**
 * A panel showing events in the calendar
 * 
 * @author Bernhard von Gunten
 * @created January 2, 2002
 */
public class PanelCalendar extends JIhmPanel {
  static final long serialVersionUID = -6381964250064521938L;

  /** Possible owners of events */
  // TODO: Cleanup vector
  private Vector<SchedulerEventOwner> eventOwners;

  private boolean debug = false;

  /* Gui controls */
  BorderLayout borderLayout1 = new BorderLayout();

  JIhmPanel panelButtons = new JIhmPanel();

  JIhmPanel panelFilter = new JIhmPanel();

  JComboBox cbEventOwners = new JComboBox();

  JIhmButton cmdFilter = new JIhmButton();

  JIhmLabel lblFilter = new JIhmLabel();

  JIhmPanel panelCalendar = new JIhmPanel();

  BorderLayout borderLayout2 = new BorderLayout();

  JScrollPane jScrollPane1 = new JScrollPane();

  JIhmTable jCalendarTable = new JIhmTable();

  BorderLayout borderLayout4 = new BorderLayout();

  JIhmPanel panelCalendarGrid = new JIhmPanel();

  BorderLayout borderLayout3 = new BorderLayout();

  JIhmPanel panelEvent = new JIhmPanel();

  JSplitPane jSplitPane1 = new JSplitPane();

  JIhmPanel panelGameDay = new JIhmPanel();

  JList jMatchList = new JList();

  JScrollPane jScrollPane2 = new JScrollPane();

  JIhmButton cmdDebug = new JIhmButton();

  JScrollPane jScrollPane3 = new JScrollPane();

  JTextArea txtEventDump = new JTextArea();

  JIhmPanel panelEmpty = new JIhmPanel();

  ImageIcon calendarIcon = new ImageIcon();

  ImageIcon clockIcon = new ImageIcon();

  /**
   * Constructs the frame, sets the user, calls ihmInit
   * 
   * @param user
   *          User to show this frame for
   * @param debug 
   */
  public PanelCalendar(User user, boolean debug) {

    this.debug = debug;
    try {
      initGUI();
      ihmInit();
    } catch (Exception e) {
      e.printStackTrace();
    }

  }

  /**
   * JBuilder stuff
   * 
   * @exception Exception
   *              Exception
   */
  private void initGUI() throws Exception {
    this.setSize(700, 400);
    this.setLayout(borderLayout1);
    calendarIcon = new ImageIcon(ClientController.getInstance().getGuiResource("icons/calendar18x18.png"));
    panelButtons.setMaximumSize(new Dimension(50, 50));
    panelButtons.setMinimumSize(new Dimension(50, 50));
    clockIcon = new ImageIcon(ClientController.getInstance().getGuiResource("icons/clock18x18.png"));
    cmdFilter.setIcon(clockIcon);
    cmdFilter.setText("");
    cmdFilter.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        cmdFilter_actionPerformed(e);
      }
    });
    cbEventOwners.setToolTipText("");
    lblFilter.setText("ihm.filter");
    lblFilter.setMsgKey("ihm.filter");
    panelCalendar.setLayout(borderLayout2);
    jScrollPane1.setMinimumSize(new Dimension(0, 0));
    panelCalendarGrid.setLayout(borderLayout4);
    panelCalendarGrid.setMaximumSize(new Dimension(99999, 99999));
    panelEvent.setLayout(borderLayout3);
    jSplitPane1.setContinuousLayout(true);
    jSplitPane1.setLeftComponent(panelCalendarGrid);
    jSplitPane1.setRightComponent(panelEvent);
    panelGameDay.setLayout(null);
    jCalendarTable.addMouseListener(new java.awt.event.MouseAdapter() {
      public void mouseClicked(MouseEvent e) {
        jCalendarTable_mouseClicked(e);
      }
    });
    jMatchList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    jMatchList.addMouseListener(new java.awt.event.MouseAdapter() {
      public void mouseClicked(MouseEvent e) {
        jMatchList_mouseClicked(e);
      }
    });
    jMatchList.setModel(new DefaultListModel());

    jScrollPane2.setBounds(new Rectangle(17, 12, 261, 256));
    panelGameDay.setPreferredSize(new Dimension(300, 300));
    jCalendarTable.addKeyListener(new java.awt.event.KeyAdapter() {
      public void keyPressed(KeyEvent e) {
        jCalendarTable_keyPressed(e);
      }
    });
    cmdDebug.setText("ihm.debugEvent");
    cmdDebug.setMsgKey("ihm.debugEvent");
    cmdDebug.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        cmdDebug_actionPerformed(e);
      }
    });
    this.txtEventDump.setText("...");
    this.add(panelButtons, BorderLayout.SOUTH);
    panelButtons.add(cmdDebug, null);
    this.add(panelFilter, BorderLayout.NORTH);
    panelFilter.add(lblFilter, null);
    panelFilter.add(cbEventOwners, null);
    panelFilter.add(cmdFilter, null);
    this.add(panelCalendar, BorderLayout.CENTER);
    panelCalendar.add(jSplitPane1, BorderLayout.CENTER);
    panelCalendarGrid.add(jScrollPane1, BorderLayout.CENTER);
    jScrollPane1.getViewport().add(jCalendarTable, null);
    panelGameDay.add(jScrollPane2, null);
    jScrollPane2.getViewport().add(jMatchList, null);
    panelEvent.add(jScrollPane3, BorderLayout.CENTER);
    jScrollPane3.getViewport().add(this.txtEventDump, null);
    jSplitPane1.setDividerLocation(350);
  }

  /** Creates the filters and shows the grid */
  private void ihmInit() {
    this.setTitleKey("title.calendar");

    // Possible owners of the events :
    eventOwners = new Vector<SchedulerEventOwner>();
    cbEventOwners.removeAllItems();

    if (!debug) {

      // Scenario
      // eventOwners.add(GameController.getInstance().getScenario().getScheduler());
      // cbEventOwners.addItem(ClientController.getInstance().getTranslation("calendar.allEvents"));

      LeagueOwner[] leagueOwners = GameController.getInstance().getScenario().getLeagueOwners();

      for (int i = 0; i < leagueOwners.length; i++) {
        League[] leagues = leagueOwners[i].getLeagues();
        for (int n = 0; n < leagues.length; n++) {
          eventOwners.add(leagues[n]);
          cbEventOwners.addItem(leagues[n].getName());
        }
      }

    } else {

      eventOwners.add(GameController.getInstance().getScenario().getScheduler());
      cbEventOwners.addItem(ClientController.getInstance().getTranslation("calendar.debugEvents"));
    }
    displayTable();
    jCalendarTable_mouseClicked(null);
  }

  /** Displays all the user informations */
  private void displayTable() {

    if (!debug) {

      // Different team owners ...
      SchedulerEventOwner filter = eventOwners.get(cbEventOwners.getSelectedIndex());
      this.jCalendarTable.setModel(filter.getTMScheduler());

    } else {
      this.jCalendarTable.setModel(SchedulerTools.getTMSchedulerAllEvents());
    }

    jCalendarTable.setColumnSelectionAllowed(false);

    TableColumnModel tcm = jCalendarTable.getColumnModel();
    tcm.getColumn(TMScheduler.COLUMN_DAY).setMaxWidth(100);

    jCalendarTable.setRowSelectionInterval(0, 0);

  }

  /**
   * Reset table
   * 
   * @param e
   *          Source event
   */
  void cmdFilter_actionPerformed(ActionEvent e) {
    displayTable();
    jCalendarTable_mouseClicked(null);
  }

  /**
   * Show selected event
   * 
   * @param e
   *          Source event
   */
  void jCalendarTable_mouseClicked(MouseEvent e) {
    int lastPosition = jSplitPane1.getDividerLocation();
    TMScheduler ta = (TMScheduler) jCalendarTable.getModel();
    int row = jCalendarTable.getSelectedRow();
    SchedulerEvent event = ta.getSchedulerEvent(row);

    if (debug) {
      jSplitPane1.setRightComponent(panelEvent);
      this.txtEventDump.setText(event.getDump());
    } else {
      // GameDay Event
      if (event instanceof org.icehockeymanager.ihm.game.league.events.GameDayMatchesEvent) {
        GameDayMatchesEvent gameDay = (GameDayMatchesEvent) event;
        jMatchList.setModel(new LMMatches(gameDay.getMatches()));
        jSplitPane1.setRightComponent(panelGameDay);
      } else {
        jSplitPane1.setRightComponent(panelEmpty);
      }
    }

    jSplitPane1.setDividerLocation(lastPosition);
  }

  void jMatchList_mouseClicked(MouseEvent e) {
    if (e.getClickCount() > 1) {
      LMMatches lm = (LMMatches) jMatchList.getModel();
      int row = jMatchList.getSelectedIndex();
      Match match = lm.getMatch(row);

      ClientController.getInstance().getDesktop().showPanelScoreSheet(match);
    }
  }

  void jCalendarTable_keyPressed(KeyEvent e) {
    jCalendarTable_mouseClicked(null);
  }

  void cmdDebug_actionPerformed(ActionEvent e) {
    TMScheduler ta = (TMScheduler) jCalendarTable.getModel();
    int row = jCalendarTable.getSelectedRow();
    SchedulerEvent event = ta.getSchedulerEvent(row);
    ClientController.getInstance().getDesktop().showPanelDebug(event, "SchedulerEvent");
  }

}
