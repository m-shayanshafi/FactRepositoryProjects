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
  
package org.icehockeymanager.ihm.clients.devgui.gui.playerattributes;

import java.awt.*;
import java.awt.event.*;
import java.util.*;

import javax.swing.*;
import javax.swing.table.*;

import org.icehockeymanager.ihm.clients.devgui.controller.*;
import org.icehockeymanager.ihm.clients.devgui.gui.lib.*;
import org.icehockeymanager.ihm.clients.devgui.ihm.league.*;
import org.icehockeymanager.ihm.clients.devgui.ihm.player.*;
import org.icehockeymanager.ihm.game.*;
import org.icehockeymanager.ihm.game.league.*;
import org.icehockeymanager.ihm.game.player.*;
import org.icehockeymanager.ihm.game.user.*;
import org.icehockeymanager.ihm.game.transfers.*;

/**
 * A panel with a player attributes grid including filtering.
 * 
 * @author Bernhard von Gunten
 * @created December 29, 2001
 */
public class PanelPlayerAttributes extends JIhmPanel {
  static final long serialVersionUID = -244977398460951619L;

  /** Possible owners of players */
  // TODO: Cleanup vector
  private Vector<PlayerOwner> playerOwners;

  /** Show transfer list */
  private boolean transferList;

  /* Gui controls */
  BorderLayout borderLayout1 = new BorderLayout();

  JIhmPanel panelButtons = new JIhmPanel();

  JScrollPane jScrollPane1 = new JScrollPane();

  JIhmPanel JIhmPanel1 = new JIhmPanel();

  JIhmComboBox cbPlayerOwners = new JIhmComboBox();

  JIhmButton cmdFilter = new JIhmButton();

  JIhmTable jPlayerAttributesTable = new JIhmTable();

  JIhmLabel lblFilter = new JIhmLabel();

  ImageIcon playerattributesIcon = new ImageIcon();

  ImageIcon playerfilterIcon = new ImageIcon();

  /**
   * Constructs the frame, sets the user, calls ihmInit
   * 
   * @param user
   *          User to show this frame for
   * @param transferlist 
   */
  public PanelPlayerAttributes(User user, boolean transferlist) {
    super(user);
    this.transferList = transferlist;
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
    playerfilterIcon = new ImageIcon(ClientController.getInstance().getGuiResource("icons/filterplayer18x18.png"));
    cmdFilter.setIcon(playerfilterIcon);
    cmdFilter.setText("");
    cmdFilter.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        cmdFilter_actionPerformed(e);
      }
    });
    cbPlayerOwners.setToolTipText("");
    lblFilter.setText("ihm.filter");
    lblFilter.setMsgKey("ihm.filter");
    jPlayerAttributesTable.addMouseListener(new java.awt.event.MouseAdapter() {
      public void mouseClicked(MouseEvent e) {
        jPlayerAttributesTable_mouseClicked(e);
      }
    });
    this.add(panelButtons, BorderLayout.SOUTH);
    this.add(jScrollPane1, BorderLayout.CENTER);
    jScrollPane1.getViewport().add(jPlayerAttributesTable, null);
    this.add(JIhmPanel1, BorderLayout.NORTH);
    JIhmPanel1.add(lblFilter, null);
    JIhmPanel1.add(cbPlayerOwners, null);
    JIhmPanel1.add(cmdFilter, null);
  }

  /** Create the panelLeagueStandings and add it to the frame */
  private void ihmInit() {

    if (!transferList) {
      this.setTitleKey("title.playersByAttributes");
      // Possible owners of the players :
      playerOwners = new Vector<PlayerOwner>();
      cbPlayerOwners.removeAllItems();

      // Scenario
      playerOwners.add(GameController.getInstance().getScenario());
      cbPlayerOwners.addItem(ClientController.getInstance().getTranslation("ihm.allPlayers"));

      LeagueOwner[] leagueOwners = GameController.getInstance().getScenario().getLeagueOwners();

      for (int i = 0; i < leagueOwners.length; i++) {
        League[] leagues = leagueOwners[i].getLeagues();
        playerOwners.add(leagueOwners[i]);
        cbPlayerOwners.addItem(leagueOwners[i].getName());

        for (int n = 0; n < leagues.length; n++) {
          playerOwners.add(leagues[n]);
          cbPlayerOwners.addItem(LeagueTools.getLeagueDescription(leagues[n]));
        }
      }
    } else {
      this.setTitleKey("title.transferList");

      cbPlayerOwners.addItem(ClientController.getInstance().getTranslation("transferlist.allPlayers"));
      cbPlayerOwners.addItem(ClientController.getInstance().getTranslation("transferlist.goalie"));
      cbPlayerOwners.addItem(ClientController.getInstance().getTranslation("transferlist.defense"));
      cbPlayerOwners.addItem(ClientController.getInstance().getTranslation("transferlist.wing"));
      cbPlayerOwners.addItem(ClientController.getInstance().getTranslation("transferlist.center"));
    }

    displayTable();

  }

  /** Displays all the user informations */
  private void displayTable() {

    if (!transferList) {
      // Different player owners ...
      PlayerOwner filter = playerOwners.get(cbPlayerOwners.getSelectedIndex());
      this.jPlayerAttributesTable.setModel(new TMPlayerAttributes(filter.getPlayers()));

    } else {
      // TODO : Update with something better
      if (cbPlayerOwners.getSelectedIndex() == 0) {
        this.jPlayerAttributesTable.setModel(new TMPlayerAttributes(Transfers.getTransferList(null)));
      } else if (cbPlayerOwners.getSelectedIndex() == 1) {
        this.jPlayerAttributesTable.setModel(new TMPlayerAttributes(Transfers.getTransferListByPosition(null, PlayerAttributes.POSITION_GOALTENDING)));
      } else if (cbPlayerOwners.getSelectedIndex() == 2) {
        this.jPlayerAttributesTable.setModel(new TMPlayerAttributes(Transfers.getTransferListByPosition(null, PlayerAttributes.POSITION_DEFENSE)));
      } else if (cbPlayerOwners.getSelectedIndex() == 3) {
        this.jPlayerAttributesTable.setModel(new TMPlayerAttributes(Transfers.getTransferListByPosition(null, PlayerAttributes.POSITION_WING)));
      } else if (cbPlayerOwners.getSelectedIndex() == 4) {
        this.jPlayerAttributesTable.setModel(new TMPlayerAttributes(Transfers.getTransferListByPosition(null, PlayerAttributes.POSITION_CENTER)));
      }
    }

    jPlayerAttributesTable.setColumnSelectionAllowed(false);
    jPlayerAttributesTable.activateSorting();

    TableColumnModel tcm = jPlayerAttributesTable.getColumnModel();

    tcm.getColumn(TMPlayerAttributes.COLUMN_RANK).setMaxWidth(30);
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
   * Description of the Method
   * 
   * @param e
   *          Description of the Parameter
   */
  void jPlayerAttributesTable_mouseClicked(MouseEvent e) {
    if (e.getClickCount() > 1) {
      TMPlayerAttributes ta = (TMPlayerAttributes) jPlayerAttributesTable.getModel();
      int row = jPlayerAttributesTable.getSelectedRow();
      Player player = ta.getPlayer(row);

      ClientController.getInstance().getDesktop().showPanelPlayer(player);
    }
  }

}
