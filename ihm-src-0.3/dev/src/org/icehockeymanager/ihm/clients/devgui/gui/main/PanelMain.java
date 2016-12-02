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
  
package org.icehockeymanager.ihm.clients.devgui.gui.main;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;
import javax.swing.table.*;

import org.icehockeymanager.ihm.clients.devgui.controller.*;
import org.icehockeymanager.ihm.clients.devgui.gui.*;
import org.icehockeymanager.ihm.clients.devgui.gui.lib.*;
import org.icehockeymanager.ihm.clients.devgui.ihm.team.*;
import org.icehockeymanager.ihm.game.*;
import org.icehockeymanager.ihm.game.scenario.*;
import org.icehockeymanager.ihm.game.team.*;
import org.icehockeymanager.ihm.game.user.*;

/**
 * The FrameMain contains: A simple frame to start or load the latest game
 * 
 * @author Bernhard von Gunten
 * @created January,2004
 */
public class PanelMain extends JIhmPanel {
  static final long serialVersionUID = 6359984001047281348L;

  private JIhmButton cmdStartGame;

  private JIhmLabel lblUserName;

  private JIhmTextField txtUserName;

  private JIhmLabel lblMessage;

  private JIhmButton btnLoadLatest;

  private JIhmLabel lblMainScreen;

  private JIhmLabel lblTitle;

  private JIhmTable tableTeams;

  private JIhmLabel lblTeam;

  private JScrollPane scrollPaneTeams;

  private Scenario scenario = null;

  private Desktop desktop = null;

  /**
   * Constructs the frame, sets the user, calls ihmInit
   * 
   * @param desktop
   *          Desktop
   * @param scenario
   *          Scenario
   */
  public PanelMain(Desktop desktop, Scenario scenario) {
    this.scenario = scenario;
    this.desktop = desktop;
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
    this.setLayout(null);
    this.setSize(new Dimension(561, 470));
    this.setPreferredSize(new java.awt.Dimension(561, 470));
    {
      cmdStartGame = new JIhmButton();
      this.add(cmdStartGame);
      cmdStartGame.setText("mainscreen.startNewGame");
      cmdStartGame.setMsgKey("mainscreen.startNewGame");
      cmdStartGame.setBounds(78, 370, 184, 30);
      cmdStartGame.addActionListener(new ActionListener() {
        public void actionPerformed(ActionEvent evt) {
          startNewGame();
        }
      });
    }
    {
      lblUserName = new JIhmLabel();
      this.add(lblUserName);
      lblUserName.setText("ihm.userName");
      lblUserName.setMsgKey("ihm.userName");
      lblUserName.setBounds(41, 110, 117, 30);
    }
    {
      txtUserName = new JIhmTextField();
      this.add(txtUserName);
      txtUserName.setText("Bill Gilligan");
      txtUserName.setMsgKey("txtUserName");
      txtUserName.setBounds(167, 110, 345, 30);
    }
    {
      scrollPaneTeams = new JScrollPane();
      this.add(scrollPaneTeams);
      scrollPaneTeams.setBounds(41, 186, 494, 171);
      {
        TableModel tableTeamsModel = new DefaultTableModel(new String[][] { { "One", "Two" }, { "Three", "Four" } }, new String[] { "Column 1", "Column 2" });
        tableTeams = new JIhmTable();
        scrollPaneTeams.setViewportView(tableTeams);
        tableTeams.setModel(tableTeamsModel);
      }
    }
    {
      lblTeam = new JIhmLabel();
      this.add(lblTeam);
      lblTeam.setText("ihm.team");
      lblTeam.setMsgKey("ihm.team");
      lblTeam.setBounds(41, 150, 60, 30);
    }
    {
      lblTitle = new JIhmLabel();
      this.add(lblTitle);
      lblTitle.setText("Title / Version");
      lblTitle.setBounds(9, 11, 543, 30);
      lblTitle.setFont(new java.awt.Font("Dialog", 1, 20));
      lblTitle.setHorizontalAlignment(SwingConstants.CENTER);
      lblTitle.setHorizontalTextPosition(SwingConstants.CENTER);
    }
    {
      lblMainScreen = new JIhmLabel();
      this.add(lblMainScreen);
      lblMainScreen.setText("title.mainScreen");
      lblMainScreen.setMsgKey("title.mainScreen");
      lblMainScreen.setBounds(12, 55, 539, 30);
      lblMainScreen.setFont(new java.awt.Font("Dialog", 1, 16));
      lblMainScreen.setHorizontalTextPosition(SwingConstants.CENTER);
      lblMainScreen.setHorizontalAlignment(SwingConstants.CENTER);
    }
    {
      lblMessage = new JIhmLabel();
      this.add(lblMessage);
      lblMessage.setBounds(15, 423, 528, 30);
      lblMessage.setHorizontalAlignment(SwingConstants.CENTER);
      lblMessage.setHorizontalTextPosition(SwingConstants.CENTER);
    }
    {
      btnLoadLatest = new JIhmButton();
      this.add(btnLoadLatest);
      btnLoadLatest.setMsgKey("mainscreen.load");
      btnLoadLatest.setText("mainscreen.load");
      btnLoadLatest.setBounds(312, 370, 184, 30);
      btnLoadLatest.addActionListener(new ActionListener() {
        public void actionPerformed(ActionEvent evt) {
          loadGame();
        }
      });
    }
  }

  /** Create the panelLeagueStandings and add it to the frame */
  private void ihmInit() {
    this.tableTeams.setModel(new TMTeamAttributes(scenario.getTeams()));

    tableTeams.setColumnSelectionAllowed(false);
    tableTeams.activateSorting();

    TableColumnModel tmp = tableTeams.getColumnModel();

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

    this.lblTitle.setText(ClientController.getInstance().getTranslation("app.title") + " " + GameVersion.IHM_VERSION);

  }

  private void startNewGame() {
    TMTeamAttributes ta = (TMTeamAttributes) tableTeams.getModel();
    int row = tableTeams.getSelectedRow();
    if (row >= 0) {
      Team team = ta.getTeam(row);
      User[] users = new User[1];
      User usr = new User(this.txtUserName.getText());
      usr.setTeam(team);
      users[0] = usr;
      scenario.setUsers(users);
      GameController.getInstance().init(scenario, ClientController.getInstance().getSchedulerListener());
      desktop.moveOn();
    } else {
      this.lblMessage.setText(ClientController.getInstance().getTranslation("mainscreen.selectTeam"));
    }
  }

  private void loadGame() {
    Scenario tmp = ClientController.getInstance().loadScenario();
    if (tmp != null) {
      GameController.getInstance().init(tmp, ClientController.getInstance().getSchedulerListener());
      desktop.moveOn();
    } else {
      this.lblMessage.setText(ClientController.getInstance().getTranslation("mainscreen.loadFailed"));
    }
  }

}
