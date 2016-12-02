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
  
package org.icehockeymanager.ihm.clients.devgui.gui.bookings;

import org.icehockeymanager.ihm.clients.devgui.gui.lib.*;
import org.icehockeymanager.ihm.game.team.*;
import org.icehockeymanager.ihm.clients.devgui.ihm.finance.*;
import java.awt.BorderLayout;
import org.icehockeymanager.ihm.lib.*;

import javax.swing.*;
import java.awt.*;
import javax.swing.table.*;

public class PanelBookings extends JIhmPanel {

  static final long serialVersionUID = -6339301419498766861L;

  /** Team that is shown in this frame */
  private Team teamToShow = null;

  JScrollPane jScrollPane1 = new JScrollPane();

  JIhmTable jBookingsTable = new JIhmTable();

  JIhmPanel jPanel1 = new JIhmPanel();

  JIhmLabel jLabel1 = new JIhmLabel();

  JIhmLabel lblBalance = new JIhmLabel();

  ImageIcon bookingsIcon = new ImageIcon();

  public PanelBookings(Team teamToShow) {
    try {
      initGUI();
      ihmInit(teamToShow);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  private void initGUI() throws Exception {
    this.setSize(730, 500);

    jPanel1.setMinimumSize(new java.awt.Dimension(10, 20));
    jPanel1.setLayout(null);
    jLabel1.setFont(new java.awt.Font("Dialog", 1, 11));
    jLabel1.setText("bookings.balance");
    jLabel1.setMsgKey("bookings.balance");
    jLabel1.setBounds(new Rectangle(15, 8, 101, 15));
    lblBalance.setFont(new java.awt.Font("Dialog", 1, 11));
    lblBalance.setHorizontalAlignment(SwingConstants.RIGHT);
    lblBalance.setText("0");
    lblBalance.setVerticalTextPosition(javax.swing.SwingConstants.CENTER);
    lblBalance.setBounds(new Rectangle(127, 8, 61, 15));
    jPanel1.add(jLabel1, null);
    jPanel1.add(lblBalance, null);
    jScrollPane1.getViewport().add(jBookingsTable, null);
  }

  /**
   * Create the panelLeagueStandings and add it to the frame
   * 
   * @param teamToshow
   *          Team to show in this frame
   */
  private void ihmInit(Team teamToshow) {
    this.setTitleKey("title.bookings");

    this.teamToShow = teamToshow;
    displayTeam();
  }

  private void displayTeam() {

    lblBalance.setText(Tools.doubleToStringC(teamToShow.getAccounting().getBalance()));

    BorderLayout thisLayout = new BorderLayout();
    this.setLayout(thisLayout);
    this.jBookingsTable.setModel(new TMBookings(teamToShow.getAccounting().getBookings()));
    this.add(jScrollPane1, BorderLayout.CENTER);
    this.add(jPanel1, BorderLayout.SOUTH);
    jPanel1.setPreferredSize(new java.awt.Dimension(10, 100));
    jPanel1.setSize(730, 30);
    jBookingsTable.setColumnSelectionAllowed(false);
    jBookingsTable.activateSorting();

    TableColumnModel tcm = jBookingsTable.getColumnModel();
    DefaultTableCellRenderer dtcr = new DefaultTableCellRenderer();
    dtcr.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);

    tcm.getColumn(TMBookings.COLUMN_AMOUNT).setCellRenderer(dtcr);

  }

}
