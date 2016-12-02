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
  
package org.icehockeymanager.ihm.clients.devgui.gui.player;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;
import javax.swing.table.*;

import org.icehockeymanager.ihm.clients.devgui.controller.*;
import org.icehockeymanager.ihm.clients.devgui.gui.lib.*;
import org.icehockeymanager.ihm.clients.devgui.gui.training.*;
import org.icehockeymanager.ihm.clients.devgui.gui.contract.*;
import org.icehockeymanager.ihm.clients.devgui.ihm.player.*;
import org.icehockeymanager.ihm.game.player.*;
import org.icehockeymanager.ihm.game.transfers.*;
import org.icehockeymanager.ihm.game.user.*;

/**
 * A panel to show the player with stats and players.
 * 
 * @author Bernhard von Gunten
 * @created December 29, 2001
 */
public class PanelPlayer extends JIhmPanel {
  static final long serialVersionUID = 7399706111561335512L;

  /** Player that is shown in this frame */
  private Player playerToShow = null;

  /** Training panel */
  private PanelPlayerTrainingSchedule traingingPanel = null;

  /* Gui controls */
  BorderLayout borderLayout1 = new BorderLayout();

  JIhmTabbedPane tabbedPanePlayer = new JIhmTabbedPane();

  JIhmPanel panelPlayerInfo = new JIhmPanel();

  JIhmPanel panelPlayerStats = new JIhmPanel();

  BorderLayout borderLayout2 = new BorderLayout();

  JIhmLabel txtPlayerName = new JIhmLabel();

  JIhmPanel panelLeagueElementStats = new JIhmPanel();

  BorderLayout borderLayout4 = new BorderLayout();

  JIhmLabel lblLEagueElementStats = new JIhmLabel();

  JScrollPane jScrollPane2 = new JScrollPane();

  JIhmTable jLeagueElementStatsTable = new JIhmTable();

  JIhmPanel panelNorth = new JIhmPanel();

  JScrollPane jScrollPane3 = new JScrollPane();

  JIhmTable jLeagueStatsTable = new JIhmTable();

  BorderLayout borderLayout5 = new BorderLayout();

  JIhmLabel lblLeagueStats = new JIhmLabel();

  JIhmPanel panelLeagueStats = new JIhmPanel();

  BorderLayout borderLayout6 = new BorderLayout();

  JIhmLabel lblTotalStats = new JIhmLabel();

  JScrollPane jScrollPane1 = new JScrollPane();

  JIhmPanel panelTotalStats = new JIhmPanel();

  BorderLayout borderLayout3 = new BorderLayout();

  JIhmTable jTotalStatsTable = new JIhmTable();

  JIhmPanel panelLeagueElementGroupsTable = new JIhmPanel();

  BorderLayout borderLayout7 = new BorderLayout();

  JIhmLabel lblLeagueElementGroupStats = new JIhmLabel();

  JScrollPane jScrollPane4 = new JScrollPane();

  JIhmTable jLeagueElementGroupStatsTable = new JIhmTable();

  JScrollPane jScrollPane6 = new JScrollPane();

  JIhmTable jPlayerAttributesTable = new JIhmTable();

  JIhmLabel lblPlayerAttributes = new JIhmLabel();

  JScrollPane jScrollPane5 = new JScrollPane();

  JTextArea txtPlayerSpecificAttributes = new JTextArea();

  JScrollPane jScrollPane7 = new JScrollPane();

  JTextArea txtPlayerAttributes = new JTextArea();

  JIhmLabel lblPlayerAttributesSpecific = new JIhmLabel();

  JIhmLabel lblPlayerAttributesCommon = new JIhmLabel();

  JIhmButton btnDebug = new JIhmButton();

  JTextArea txtPlayerInfos = new JTextArea();

  JScrollPane jScrollPane8 = new JScrollPane();

  private JIhmButton btnHireProspect;

  private JIhmButton btnTransferList;

  JIhmLabel lblPlayerInfos = new JIhmLabel();

  JIhmButton btnExtendContract = new JIhmButton();

  JIhmButton btnTransfer = new JIhmButton();

  ImageIcon playerIcon = new ImageIcon();

  ImageIcon transferIcon = new ImageIcon();

  ImageIcon extendcontractIcon = new ImageIcon();

  ImageIcon debugIcon = new ImageIcon();

  ImageIcon transferlistIcon = new ImageIcon();

  ImageIcon playerstatsIcon = new ImageIcon();

  ImageIcon trainingIcon = new ImageIcon();

  /**
   * Constructs the frame, sets the user, calls ihmInit
   * 
   * @param user
   *          User to show this frame for
   * @param playerToShow
   *          Player to show in this frame
   */
  public PanelPlayer(User user, Player playerToShow) {
    super(user);
    try {
      initGUI();
      ihmInit(playerToShow);
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
    this.setSize(730, 500);
    this.setLayout(borderLayout1);
    playerIcon = new ImageIcon(ClientController.getInstance().getGuiResource("icons/players18x18.png"));
    playerstatsIcon = new ImageIcon(ClientController.getInstance().getGuiResource("icons/playerstats18x18.png"));
    trainingIcon = new ImageIcon(ClientController.getInstance().getGuiResource("icons/training18x18.png"));
    panelPlayerStats.setLayout(borderLayout2);
    panelPlayerInfo.setLayout(null);
    txtPlayerName.setFont(new java.awt.Font("Dialog", 1, 16));
    txtPlayerName.setText("");
    txtPlayerName.setBounds(new Rectangle(5, 10, 518, 23));
    panelLeagueElementStats.setLayout(borderLayout4);
    lblLEagueElementStats.setText("ihm.leagueElementStats");
    lblLEagueElementStats.setMsgKey("ihm.leagueElementStats");

    panelLeagueElementStats.setMinimumSize(new Dimension(200, 200));
    panelLeagueElementStats.setPreferredSize(new Dimension(453, 150));
    lblLeagueStats.setText("ihm.leagueStats");
    lblLeagueStats.setMsgKey("ihm.leagueStats");

    panelLeagueStats.setPreferredSize(new Dimension(10, 100));
    panelLeagueStats.setLayout(borderLayout5);
    panelNorth.setLayout(borderLayout6);
    lblTotalStats.setText("ihm.totalStats");
    lblTotalStats.setMsgKey("ihm.totalStats");

    panelTotalStats.setLayout(borderLayout3);
    panelTotalStats.setMaximumSize(new Dimension(150, 150));
    panelTotalStats.setMinimumSize(new Dimension(45, 41));
    panelTotalStats.setPreferredSize(new Dimension(150, 90));
    panelLeagueElementGroupsTable.setLayout(borderLayout7);
    panelLeagueElementGroupsTable.setPreferredSize(new Dimension(0, 100));
    lblLeagueElementGroupStats.setText("ihm.leagueGroupStats");
    lblLeagueElementGroupStats.setMsgKey("ihm.leagueGroupStats");
    jScrollPane6.setBounds(new Rectangle(4, 81, 521, 61));
    lblPlayerAttributes.setBounds(new Rectangle(5, 56, 247, 15));
    lblPlayerAttributes.setText("player.attributesOverview");
    lblPlayerAttributes.setMsgKey("player.attributesOverview");
    lblPlayerAttributes.setFont(new java.awt.Font("Dialog", 1, 14));
    jScrollPane5.setBounds(new Rectangle(293, 180, 192, 213));
    txtPlayerSpecificAttributes.setEditable(false);
    txtPlayerSpecificAttributes.setFont(new java.awt.Font("Monospaced", 0, 12));
    jScrollPane7.setBounds(new Rectangle(497, 180, 192, 213));
    txtPlayerAttributes.setEditable(false);
    txtPlayerAttributes.setFont(new java.awt.Font("Monospaced", 0, 12));
    lblPlayerAttributesSpecific.setFont(new java.awt.Font("Dialog", 1, 14));
    lblPlayerAttributesSpecific.setText("player.attributesSpecific");
    lblPlayerAttributesSpecific.setMsgKey("player.attributesSpecific");
    lblPlayerAttributesSpecific.setBounds(new Rectangle(293, 153, 190, 15));
    lblPlayerAttributesCommon.setFont(new java.awt.Font("Dialog", 1, 14));
    lblPlayerAttributesCommon.setText("player.attributesCommon");
    lblPlayerAttributesCommon.setMsgKey("player.attributesCommon");
    lblPlayerAttributesCommon.setBounds(new Rectangle(497, 153, 191, 15));
    btnDebug.setText("Debug");
    btnDebug.setBounds(565, 410, 110, 25);
    debugIcon = new ImageIcon(ClientController.getInstance().getGuiResource("icons/debug18x18.png"));
    btnDebug.setIcon(debugIcon);
    btnDebug.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        btnDebug_actionPerformed(e);
      }
    });
    txtPlayerInfos.setEditable(false);
    txtPlayerInfos.setFont(new java.awt.Font("Monospaced", 0, 12));
    jScrollPane8.setBounds(new Rectangle(6, 180, 276, 213));
    lblPlayerInfos.setBounds(new Rectangle(5, 153, 191, 15));
    lblPlayerInfos.setText("player.infos");
    lblPlayerInfos.setMsgKey("player.infos");
    lblPlayerInfos.setFont(new java.awt.Font("Dialog", 1, 14));
    btnExtendContract.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        btnExtendContract_actionPerformed(e);
      }
    });
    btnExtendContract.setBounds(543, 46, 134, 25);
    btnExtendContract.setText("player.extendContract");
    btnExtendContract.setMsgKey("player.extendContract");
    extendcontractIcon = new ImageIcon(ClientController.getInstance().getGuiResource("icons/extendcontract18x18.png"));
    btnExtendContract.setIcon(extendcontractIcon);
    btnTransfer.setText("player.transfer");
    btnTransfer.setMsgKey("player.transfer");
    btnTransfer.setBounds(543, 77, 134, 25);
    this.add(tabbedPanePlayer, BorderLayout.CENTER);
    tabbedPanePlayer.addTab(ClientController.getInstance().getTranslation("ihm.player"), playerIcon, panelPlayerInfo, ClientController.getInstance().getTranslation("ihm.player"));
    panelPlayerInfo.add(lblPlayerAttributes, null);
    panelPlayerInfo.add(jScrollPane6, null);
    panelPlayerInfo.add(txtPlayerName, null);
    panelPlayerInfo.add(btnDebug, null);
    panelPlayerInfo.add(lblPlayerAttributesSpecific, null);
    panelPlayerInfo.add(jScrollPane5, null);
    panelPlayerInfo.add(lblPlayerInfos, null);
    panelPlayerInfo.add(jScrollPane8, null);
    panelPlayerInfo.add(lblPlayerAttributesCommon, null);
    panelPlayerInfo.add(jScrollPane7, null);
    panelPlayerInfo.add(btnExtendContract, null);
    jScrollPane7.getViewport().add(txtPlayerAttributes, null);
    jScrollPane8.getViewport().add(txtPlayerInfos, null);
    jScrollPane5.getViewport().add(txtPlayerSpecificAttributes, null);
    jScrollPane6.getViewport().add(jPlayerAttributesTable, null);
    tabbedPanePlayer.addTab(ClientController.getInstance().getTranslation("ihm.stats"), playerstatsIcon, panelPlayerStats, ClientController.getInstance().getTranslation("ihm.stats"));
    panelPlayerStats.add(panelLeagueElementStats, BorderLayout.SOUTH);
    panelLeagueElementStats.add(lblLEagueElementStats, BorderLayout.NORTH);
    panelLeagueElementStats.add(jScrollPane2, BorderLayout.CENTER);
    panelPlayerStats.add(panelNorth, BorderLayout.NORTH);
    panelNorth.add(panelLeagueStats, BorderLayout.SOUTH);
    panelLeagueStats.add(lblLeagueStats, BorderLayout.NORTH);
    panelLeagueStats.add(jScrollPane3, BorderLayout.CENTER);
    panelNorth.add(panelTotalStats, BorderLayout.NORTH);
    panelTotalStats.add(lblTotalStats, BorderLayout.NORTH);
    panelTotalStats.add(jScrollPane1, BorderLayout.CENTER);
    panelPlayerStats.add(panelLeagueElementGroupsTable, BorderLayout.CENTER);
    panelLeagueElementGroupsTable.add(lblLeagueElementGroupStats, BorderLayout.NORTH);
    panelLeagueElementGroupsTable.add(jScrollPane4, BorderLayout.CENTER);
    jScrollPane4.getViewport().add(jLeagueElementGroupStatsTable, null);
    jScrollPane1.getViewport().add(jTotalStatsTable, null);
    jScrollPane3.getViewport().add(jLeagueStatsTable, null);
    jScrollPane2.getViewport().add(jLeagueElementStatsTable, null);
    panelPlayerInfo.add(btnTransfer, null);
    transferIcon = new ImageIcon(ClientController.getInstance().getGuiResource("icons/transferplayer18x18.png"));
    btnTransfer.setIcon(transferIcon);
    btnTransfer.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent evt) {
        bntTransfer_actionPerformed(evt);
      }
    });
    {
      btnExtendContract.setBounds(540, 40, 150, 30);
    }
    {
      btnTransfer.setBounds(540, 75, 150, 30);
    }
    {
      btnTransferList = new JIhmButton();
      panelPlayerInfo.add(btnTransferList);
      btnTransferList.setText("player.putToTransferlist");

      transferlistIcon = new ImageIcon(ClientController.getInstance().getGuiResource("icons/transferlist18x18.png"));
      btnTransferList.setIcon(transferlistIcon);
      btnTransferList.setBounds(540, 110, 150, 30);
      btnTransferList.addActionListener(new ActionListener() {
        public void actionPerformed(ActionEvent evt) {
          btnTransferList_actionPerformed(evt);
        }
      });
    }
    {
      btnHireProspect = new JIhmButton();
      panelPlayerInfo.add(btnHireProspect);
      btnHireProspect.setText("player.hireProspect");
      btnHireProspect.setMsgKey("player.hireProspect");
      btnHireProspect.setBounds(540, 5, 150, 30);
      btnHireProspect.addActionListener(new ActionListener() {
        public void actionPerformed(ActionEvent evt) {
          btnHireProspect_actionPerformed(evt);
        }
      });
    }

    this.setPreferredSize(new java.awt.Dimension(731, 505));
    this.setBounds(0, 0, 731, 505);

  }

  /**
   * Create the panelLeagueStandings and add it to the frame
   * 
   * @param playerToshow
   *          Player to show in this frame
   */
  private void ihmInit(Player playerToshow) {
    this.playerToShow = playerToshow;
    displayPlayer();
  }

  /**
   * Sets the playerStats widhts
   * 
   * @param tcm
   *          The new playerStatsWidths value
   */
  public void setPlayerStatsWidths(TableColumnModel tcm) {
    tcm.getColumn(TMPlayerAttributes.COLUMN_PLAYER).setMinWidth(0);
    tcm.getColumn(TMPlayerAttributes.COLUMN_PLAYER).setMaxWidth(0);
    tcm.getColumn(TMPlayerStats.COLUMN_RANK).setMaxWidth(30);
    tcm.getColumn(TMPlayerStats.COLUMN_GOALS).setMaxWidth(30);
    tcm.getColumn(TMPlayerStats.COLUMN_POSITION).setMaxWidth(30);
    tcm.getColumn(TMPlayerStats.COLUMN_ASSISTS).setMaxWidth(30);
    tcm.getColumn(TMPlayerStats.COLUMN_SCORERPOINTS).setMaxWidth(30);
    tcm.getColumn(TMPlayerStats.COLUMN_GAMESPLAYED).setMaxWidth(30);
    tcm.getColumn(TMPlayerStats.COLUMN_PENALTYMINUTES).setMaxWidth(40);

    DefaultTableCellRenderer dtcr = new DefaultTableCellRenderer();
    dtcr.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);

    tcm.getColumn(TMPlayerStats.COLUMN_RANK).setCellRenderer(dtcr);
    tcm.getColumn(TMPlayerStats.COLUMN_GOALS).setCellRenderer(dtcr);
    tcm.getColumn(TMPlayerStats.COLUMN_ASSISTS).setCellRenderer(dtcr);
    tcm.getColumn(TMPlayerStats.COLUMN_SCORERPOINTS).setCellRenderer(dtcr);
    tcm.getColumn(TMPlayerStats.COLUMN_GAMESPLAYED).setCellRenderer(dtcr);
    tcm.getColumn(TMPlayerStats.COLUMN_PENALTYMINUTES).setCellRenderer(dtcr);
  }

  /** Display the player */
  private void displayPlayer() {

    this.setTitleKey("title.player");

    // General player infos
    txtPlayerName.setText(playerToShow.getPlayerInfo().getPlayerName() + "  (" + PlayerTools.getPositionStr(playerToShow.getPlayerAttributes()) + ")");

    this.txtPlayerInfos.setText(PlayerTools.getPlayerInfoTable(playerToShow));
    this.txtPlayerSpecificAttributes.setText(PlayerTools.getSpecificAttributesStringTable(playerToShow));
    this.txtPlayerAttributes.setText(PlayerTools.getCommonAttributesStringTable(playerToShow));
    this.txtPlayerSpecificAttributes.setCaretPosition(0);
    this.txtPlayerAttributes.setCaretPosition(0);

    // Player Attributes
    this.jPlayerAttributesTable.setModel(new TMPlayerAttributes(playerToShow));
    jPlayerAttributesTable.setColumnSelectionAllowed(false);
    jPlayerAttributesTable.activateSorting();

    TableColumnModel tcm = jPlayerAttributesTable.getColumnModel();

    tcm.getColumn(TMPlayerAttributes.COLUMN_PLAYER).setMinWidth(0);
    tcm.getColumn(TMPlayerAttributes.COLUMN_PLAYER).setMaxWidth(0);
    tcm.getColumn(TMPlayerAttributes.COLUMN_RANK).setMinWidth(0);
    tcm.getColumn(TMPlayerAttributes.COLUMN_RANK).setMaxWidth(0);
    tcm.getColumn(TMPlayerAttributes.COLUMN_FORM).setMaxWidth(30);
    tcm.getColumn(TMPlayerAttributes.COLUMN_ENERGY).setMaxWidth(30);
    tcm.getColumn(TMPlayerAttributes.COLUMN_MORALE).setMaxWidth(30);
    tcm.getColumn(TMPlayerAttributes.COLUMN_POSITION).setMaxWidth(30);
    tcm.getColumn(TMPlayerAttributes.COLUMN_SPECIFIC_ATTRIBUTES).setMaxWidth(30);
    tcm.getColumn(TMPlayerAttributes.COLUMN_COMMON_ATTRIBUTES).setMaxWidth(30);
    tcm.getColumn(TMPlayerAttributes.COLUMN_TOTAL_ATTRIBUTES).setMaxWidth(30);
    tcm.getColumn(TMPlayerAttributes.COLUMN_OVERALL).setMaxWidth(30);

    DefaultTableCellRenderer dtcr = new DefaultTableCellRenderer();
    dtcr.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);

    tcm.getColumn(TMPlayerAttributes.COLUMN_RANK).setCellRenderer(dtcr);
    tcm.getColumn(TMPlayerAttributes.COLUMN_FORM).setCellRenderer(dtcr);
    tcm.getColumn(TMPlayerAttributes.COLUMN_ENERGY).setCellRenderer(dtcr);
    tcm.getColumn(TMPlayerAttributes.COLUMN_MORALE).setCellRenderer(dtcr);
    tcm.getColumn(TMPlayerAttributes.COLUMN_SPECIFIC_ATTRIBUTES).setCellRenderer(dtcr);
    tcm.getColumn(TMPlayerAttributes.COLUMN_COMMON_ATTRIBUTES).setCellRenderer(dtcr);
    tcm.getColumn(TMPlayerAttributes.COLUMN_TOTAL_ATTRIBUTES).setCellRenderer(dtcr);
    tcm.getColumn(TMPlayerAttributes.COLUMN_OVERALL).setCellRenderer(dtcr);

    // Stats Table
    this.jTotalStatsTable.setModel(new TMPlayerStats(playerToShow.getPlayerStatsTotalArray()));
    jTotalStatsTable.setColumnSelectionAllowed(false);
    jTotalStatsTable.activateSorting();
    setPlayerStatsWidths(jTotalStatsTable.getColumnModel());

    this.jLeagueStatsTable.setModel(new TMPlayerStats(playerToShow.getAllLeagueStats()));
    jLeagueStatsTable.setColumnSelectionAllowed(false);
    jLeagueStatsTable.activateSorting();
    setPlayerStatsWidths(jLeagueStatsTable.getColumnModel());

    this.jLeagueElementGroupStatsTable.setModel(new TMPlayerStats(playerToShow.getAllLeagueElementGroupStats()));
    jLeagueElementGroupStatsTable.setColumnSelectionAllowed(false);
    jLeagueElementGroupStatsTable.activateSorting();
    setPlayerStatsWidths(jLeagueElementGroupStatsTable.getColumnModel());

    this.jLeagueElementStatsTable.setModel(new TMPlayerStats(playerToShow.getAllLeagueElementStats()));
    jLeagueElementStatsTable.setColumnSelectionAllowed(false);
    jLeagueElementStatsTable.activateSorting();
    setPlayerStatsWidths(jLeagueElementStatsTable.getColumnModel());

    // Team Training Schedule
    // Only showed for own team
    if (getOwner().getTeam().equals(playerToShow.getTeam()) && traingingPanel == null) {
      traingingPanel = new PanelPlayerTrainingSchedule(playerToShow);
      traingingPanel.updateTranslation();
      tabbedPanePlayer.addTab(ClientController.getInstance().getTranslation("title.training"), trainingIcon, traingingPanel, ClientController.getInstance().getTranslation("title.training"));
    }

    this.btnExtendContract.setEnabled(playerToShow.allowExtendContractForTeam(getOwner().getTeam()));
    this.btnTransfer.setEnabled(playerToShow.allowTransferToTeam(getOwner().getTeam()));
    this.btnHireProspect.setEnabled(playerToShow.allowProspectToBeHired(getOwner().getTeam()));
    this.btnTransferList.setEnabled(playerToShow.allowTransferListPlacement(getOwner().getTeam()));

    if (playerToShow.isOnTransferList()) {
      this.btnTransferList.setText(ClientController.getInstance().getTranslation("player.removeFromTransferlist"));
    } else {
      this.btnTransferList.setText(ClientController.getInstance().getTranslation("player.putOnTransferlist"));
    }

  }

  void btnDebug_actionPerformed(ActionEvent e) {
    ClientController.getInstance().getDesktop().showPanelDebug(playerToShow, playerToShow.getPlayerInfo().getPlayerName());
  }

  void btnExtendContract_actionPerformed(ActionEvent e) {
    ClientController.getInstance().getDesktop().showFrameContract(playerToShow, PanelContract.MODE_EXTEND_CONTRACT);
    this.ihmInit(playerToShow);
  }

  void bntTransfer_actionPerformed(ActionEvent e) {
    ClientController.getInstance().getDesktop().showFrameContract(playerToShow, PanelContract.MODE_TRANSFER);
    this.ihmInit(playerToShow);
  }

  void btnHireProspect_actionPerformed(ActionEvent e) {
    ClientController.getInstance().getDesktop().showFrameContract(playerToShow, PanelContract.MODE_HIRE_PROSPECT);
    this.ihmInit(playerToShow);
  }

  void btnTransferList_actionPerformed(ActionEvent e) {
    Transfers.switchTransferListFlag(playerToShow);

    if (playerToShow.isOnTransferList()) {
      this.btnTransferList.setText(ClientController.getInstance().getTranslation("player.removeFromTransferlist"));
    } else {
      this.btnTransferList.setText(ClientController.getInstance().getTranslation("player.putOnTransferlist"));
    }

    this.ihmInit(playerToShow);

  }

}
