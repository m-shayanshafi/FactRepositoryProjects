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
  
package org.icehockeymanager.ihm.clients.devgui.gui.user;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;

import org.icehockeymanager.ihm.clients.devgui.controller.*;
import org.icehockeymanager.ihm.clients.devgui.gui.lib.*;
import org.icehockeymanager.ihm.clients.devgui.ihm.user.*;
import org.icehockeymanager.ihm.game.*;
import org.icehockeymanager.ihm.game.league.helper.*;
import org.icehockeymanager.ihm.game.user.*;

/**
 * A panel to set up the user configuration. - Username - User interests
 * 
 * @author Bernhard von Gunten
 * @created December 29, 2001
 */
public class PanelUser extends JIhmPanel {
  /**
   * Comment for <code>serialVersionUID</code>
   */
  private static final long serialVersionUID = 3256445811088110897L;

  /** User that is shown in this frame */
  private User userToShow = null;

  /* Gui controls */
  BorderLayout borderLayout1 = new BorderLayout();

  JIhmTabbedPane tabbedPane = new JIhmTabbedPane();

  JIhmPanel JIhmPanel1 = new JIhmPanel();

  JIhmPanel JIhmPanel2 = new JIhmPanel();

  JIhmLabel lblUsername = new JIhmLabel();

  JIhmLabel txtTeam = new JIhmLabel();

  JIhmLabel lblTeam = new JIhmLabel();

  JTextField txtUsername = new JTextField();

  JIhmCheckBox cbAutoTransfers;

  JIhmCheckBox cbAutoTraining;

  JIhmPanel panelButtons = new JIhmPanel();

  JIhmButton cmdOk = new JIhmButton();

  BorderLayout borderLayout2 = new BorderLayout();

  JScrollPane scrollPane = new JScrollPane();

  JTable tableUserInterests = new JTable();

  JIhmPanel panelGameStop = new JIhmPanel();

  JComboBox cbStopFor = new JComboBox();

  JIhmLabel lblStopFor = new JIhmLabel();

  ImageIcon userIcon = new ImageIcon();

  private JIhmCheckBox cbAutoSponsoring;

  private JIhmCheckBox cbAutoHiring;

  ImageIcon userinterestsIcon = new ImageIcon();

  /**
   * Constructs the frame, sets the user, calls ihmInit
   * 
   * @param user
   *          User to show this frame For
   * @param userToShow
   *          User to show in this frame
   */
  public PanelUser(User user, User userToShow) {
    super(user);
    try {
      jbInit();
      ihmInit(userToShow);
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
    userIcon = new ImageIcon(ClientController.getInstance().getGuiResource("icons/user18x18.png"));
    this.setSize(500, 400);
    this.setLayout(borderLayout1);
    JIhmPanel2.setLayout(borderLayout2);
    JIhmPanel1.setLayout(null);
    lblUsername.setText("ihm.userName");
    lblUsername.setMsgKey("ihm.userName");
    lblUsername.setBounds(new Rectangle(19, 28, 99, 15));
    txtTeam.setText("");
    txtTeam.setBounds(new Rectangle(131, 68, 344, 15));
    lblTeam.setBounds(19, 68, 99, 15);
    lblTeam.setText("ihm.team");
    lblTeam.setMsgKey("ihm.team");

    txtUsername.setBounds(new Rectangle(131, 26, 344, 19));
    panelButtons.setMaximumSize(new Dimension(50, 50));
    panelButtons.setMinimumSize(new Dimension(50, 50));
    cmdOk.setText("ihm.save");
    cmdOk.setMsgKey("ihm.save");
    cmdOk.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        cmdOk_actionPerformed(e);
      }
    });
    tableUserInterests.addMouseListener(new java.awt.event.MouseAdapter() {
      public void mouseClicked(MouseEvent e) {
        tableUserInterests_mouseClicked(e);
      }
    });
    userinterestsIcon = new ImageIcon(ClientController.getInstance().getGuiResource("icons/userinterests18x18.png"));
    lblStopFor.setText("user.stopFor");
    lblStopFor.setMsgKey("user.stopFor");

    this.add(tabbedPane, BorderLayout.CENTER);
    tabbedPane.addTab(ClientController.getInstance().getTranslation("ihm.user"), userIcon, JIhmPanel1, ClientController.getInstance().getTranslation("ihm.user"));
    JIhmPanel1.add(lblUsername, null);
    JIhmPanel1.add(txtTeam, null);
    JIhmPanel1.add(lblTeam, null);
    JIhmPanel1.add(txtUsername, null);
    {
      cbAutoTraining = new JIhmCheckBox();
      JIhmPanel1.add(cbAutoTraining);
      cbAutoTraining.setText("user.autoTraining");
      cbAutoTraining.setMsgKey("user.autoTraining");
      cbAutoTraining.setBounds(19, 102, 194, 30);
    }
    {
      cbAutoTransfers = new JIhmCheckBox();
      JIhmPanel1.add(cbAutoTransfers);
      cbAutoTransfers.setText("user.autoTransfer");
      cbAutoTransfers.setMsgKey("user.autoTransfer");
      cbAutoTransfers.setBounds(19, 145, 203, 30);
    }
    {
      cbAutoHiring = new JIhmCheckBox();
      JIhmPanel1.add(cbAutoHiring);
      cbAutoHiring.setText("user.autoProspectHiring");
      cbAutoHiring.setMsgKey("user.autoProspectHiring");
      cbAutoHiring.setBounds(19, 185, 146, 30);
    }
    {
      cbAutoSponsoring = new JIhmCheckBox();
      JIhmPanel1.add(cbAutoSponsoring);
      cbAutoSponsoring.setText("user.autoSponsoring");
      cbAutoSponsoring.setMsgKey("user.autoSponsoring");
      cbAutoSponsoring.setBounds(19, 225, 178, 30);
    }
    tabbedPane.addTab(ClientController.getInstance().getTranslation("user.userInterests"), userinterestsIcon, JIhmPanel2, ClientController.getInstance().getTranslation("user.userInterests"));
    JIhmPanel2.add(scrollPane, BorderLayout.CENTER);
    JIhmPanel2.add(panelGameStop, BorderLayout.SOUTH);
    panelGameStop.add(lblStopFor, null);
    panelGameStop.add(cbStopFor, null);
    scrollPane.getViewport().add(tableUserInterests, null);
    this.add(panelButtons, BorderLayout.SOUTH);
    panelButtons.add(cmdOk, null);
  }

  /**
   * Create the panelLeagueStandings and add it to the frame
   * 
   * @param userToshow
   *          User to show in this frame
   */
  private void ihmInit(User userToshow) {
    this.setTitleKey("title.user");
    this.userToShow = userToshow;
    this.cbStopFor.removeAllItems();
    // Items must be added like the static members in the User class
    this.cbStopFor.addItem(ClientController.getInstance().getTranslation("user.stopForAllGames"));
    this.cbStopFor.addItem(ClientController.getInstance().getTranslation("user.stopForUserGames"));
    displayUser();
  }

  /** Displays all the user informations */
  private void displayUser() {
    this.txtUsername.setText(userToShow.getUserName());
    this.txtTeam.setText(userToShow.getTeam().getTeamInfo().getTeamName());
    this.tableUserInterests.setModel(new TMUserInterests(GameController.getInstance().getScenario().getAllLeagueElements(), userToShow.getInterestedLeagueElements()));
    this.tableUserInterests.updateUI();
    this.cbStopFor.setSelectedIndex(userToShow.getStopForGames());
    this.cbAutoTraining.setSelected(userToShow.isAutoTraining());
    this.cbAutoTransfers.setSelected(userToShow.isAutoTransfers());
    this.cbAutoHiring.setSelected(userToShow.isAutoProspectHiring());
    this.cbAutoSponsoring.setSelected(userToShow.isAutoSponsoring());
  }

  /**
   * If user double clicks an entry, switch the interest of this leagueElement
   * 
   * @param e
   *          Source event
   */
  void tableUserInterests_mouseClicked(MouseEvent e) {
    if (e.getClickCount() > 1) {
      TMUserInterests tm = (TMUserInterests) tableUserInterests.getModel();
      int row = tableUserInterests.getSelectedRow();
      LeagueElement leagueElement = tm.getLeagueElement(row);
      if (userToShow.isInterestedInLeagueElement(leagueElement)) {
        userToShow.removeInterestedLeagueElement(leagueElement);
      } else {
        userToShow.addInterestedLeagueElement(leagueElement);
      }
      displayUser();
    }
  }

  /**
   * Saves username and closes frame
   * 
   * @param e
   *          Source event
   */
  void cmdOk_actionPerformed(ActionEvent e) {
    this.userToShow.setUserName(this.txtUsername.getText());
    this.userToShow.setStopForGames(cbStopFor.getSelectedIndex());
    this.userToShow.setAutoTraining(this.cbAutoTraining.isSelected());
    this.userToShow.setAutoTransfers(this.cbAutoTransfers.isSelected());
    this.userToShow.setAutoSponsoring(this.cbAutoSponsoring.isSelected());
    this.userToShow.setAutoProspectHiring(this.cbAutoHiring.isSelected());
    JOptionPane.showMessageDialog(ClientController.getInstance().getDesktop(), ClientController.getInstance().getTranslation("user.settingsSaved"));
  }

}
