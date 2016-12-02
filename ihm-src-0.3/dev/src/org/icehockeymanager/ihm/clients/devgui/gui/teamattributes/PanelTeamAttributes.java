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
  
package org.icehockeymanager.ihm.clients.devgui.gui.teamattributes;

import java.util.*;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.table.*;

import org.icehockeymanager.ihm.clients.devgui.controller.*;
import org.icehockeymanager.ihm.clients.devgui.gui.lib.*;
import org.icehockeymanager.ihm.clients.devgui.ihm.league.*;
import org.icehockeymanager.ihm.clients.devgui.ihm.team.*;
import org.icehockeymanager.ihm.game.*;
import org.icehockeymanager.ihm.game.league.*;
import org.icehockeymanager.ihm.game.team.*;
import org.icehockeymanager.ihm.game.user.*;

/**
 * A panel with a team attributes grid including filtering.
 * 
 * @author Bernhard von Gunten
 * @created December 29, 2001
 */
public class PanelTeamAttributes extends JIhmPanel {
  /**
   * Comment for <code>serialVersionUID</code>
   */
  private static final long serialVersionUID = 3906653020140811318L;

  /** Possible owners of teams */
  private Vector<TeamOwner> teamOwners;

  /* Gui controls */
  BorderLayout borderLayout1 = new BorderLayout();

  JIhmPanel panelButtons = new JIhmPanel();

  JScrollPane jScrollPane1 = new JScrollPane();

  JIhmPanel JIhmPanel1 = new JIhmPanel();

  JComboBox cbTeamOwners = new JComboBox();

  JIhmButton cmdFilter = new JIhmButton();

  JIhmTable jTeamAttributesTable = new JIhmTable();

  JIhmLabel lblFilter = new JIhmLabel();

  ImageIcon teamattributesIcon = new ImageIcon();

  ImageIcon generalfilterIcon = new ImageIcon();

  /**
   * Constructs the frame, sets the user, calls ihmInit
   * 
   * @param user
   *          User to show this frame for
   */
  public PanelTeamAttributes(User user) {
    super(user);
    try {
      jbInit();
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
  private void jbInit() throws Exception {
    this.setSize(500, 400);
    this.setLayout(borderLayout1);
    panelButtons.setMaximumSize(new Dimension(50, 50));
    panelButtons.setMinimumSize(new Dimension(50, 50));
    generalfilterIcon = new ImageIcon(ClientController.getInstance().getGuiResource("icons/generalfilter18x18.png"));
    cmdFilter.setIcon(generalfilterIcon);
    cmdFilter.setText("");
    cmdFilter.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        cmdFilter_actionPerformed(e);
      }
    });
    cbTeamOwners.setToolTipText("");
    lblFilter.setText("ihm.filter");
    lblFilter.setMsgKey("ihm.filter");
    jTeamAttributesTable.addMouseListener(new java.awt.event.MouseAdapter() {
      public void mouseClicked(MouseEvent e) {
        jTeamAttributesTable_mouseClicked(e);
      }
    });
    this.add(panelButtons, BorderLayout.SOUTH);
    this.add(jScrollPane1, BorderLayout.CENTER);
    jScrollPane1.getViewport().add(jTeamAttributesTable, null);
    this.add(JIhmPanel1, BorderLayout.NORTH);
    JIhmPanel1.add(lblFilter, null);
    JIhmPanel1.add(cbTeamOwners, null);
    JIhmPanel1.add(cmdFilter, null);
  }

  /** Creates the filters and shows the grid */
  private void ihmInit() {
    this.setTitleKey("title.teamsByAttributes");

    // Possible owners of the teams :
    teamOwners = new Vector<TeamOwner>();
    cbTeamOwners.removeAllItems();

    // Scenario
    teamOwners.add(GameController.getInstance().getScenario());
    cbTeamOwners.addItem(ClientController.getInstance().getTranslation("ihm.allTeams"));

    LeagueOwner[] leagueOwners = GameController.getInstance().getScenario().getLeagueOwners();

    for (int i = 0; i < leagueOwners.length; i++) {
      League[] leagues = leagueOwners[i].getLeagues();
      teamOwners.add(leagueOwners[i]);
      cbTeamOwners.addItem(leagueOwners[i].getName());

      for (int n = 0; n < leagues.length; n++) {
        teamOwners.add(leagues[n]);
        cbTeamOwners.addItem(LeagueTools.getLeagueDescription(leagues[n]));
      }
    }

    displayTable();

  }

  /** Displays all the user informations */
  private void displayTable() {

    // Different team owners ...
    TeamOwner filter = teamOwners.get(cbTeamOwners.getSelectedIndex());
    this.jTeamAttributesTable.setModel(new TMTeamAttributes(filter.getTeams()));

    jTeamAttributesTable.setColumnSelectionAllowed(false);
    jTeamAttributesTable.activateSorting();

    TableColumnModel tmp = jTeamAttributesTable.getColumnModel();

    tmp.getColumn(TMTeamAttributes.COLUMN_RANK).setMaxWidth(30);
    tmp.getColumn(TMTeamAttributes.COLUMN_GOALTENDING).setMaxWidth(60);
    tmp.getColumn(TMTeamAttributes.COLUMN_DEFENSE).setMaxWidth(60);
    tmp.getColumn(TMTeamAttributes.COLUMN_OFFENSE).setMaxWidth(60);
    tmp.getColumn(TMTeamAttributes.COLUMN_OVERALL).setMaxWidth(60);

    DefaultTableCellRenderer dtcr = new DefaultTableCellRenderer();
    dtcr.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);

    tmp.getColumn(TMTeamAttributes.COLUMN_RANK).setCellRenderer(dtcr);
    tmp.getColumn(TMTeamAttributes.COLUMN_GOALTENDING).setCellRenderer(dtcr);
    tmp.getColumn(TMTeamAttributes.COLUMN_DEFENSE).setCellRenderer(dtcr);
    tmp.getColumn(TMTeamAttributes.COLUMN_OFFENSE).setCellRenderer(dtcr);
    tmp.getColumn(TMTeamAttributes.COLUMN_OVERALL).setCellRenderer(dtcr);

  }

  /**
   * Reset table
   * 
   * @param e
   *          Source event
   */
  void cmdFilter_actionPerformed(ActionEvent e) {
    displayTable();
  }

  /**
   * Show selected team in a new frame.
   * 
   * @param e
   *          Source event
   */
  void jTeamAttributesTable_mouseClicked(MouseEvent e) {
    if (e.getClickCount() > 1) {
      TMTeamAttributes ta = (TMTeamAttributes) jTeamAttributesTable.getModel();
      int row = jTeamAttributesTable.getSelectedRow();
      Team team = ta.getTeam(row);

      ClientController.getInstance().getDesktop().showPanelTeam(team);
    }
  }

}
