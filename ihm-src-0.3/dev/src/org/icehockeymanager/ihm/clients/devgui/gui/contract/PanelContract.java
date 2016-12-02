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
  
package org.icehockeymanager.ihm.clients.devgui.gui.contract;

import java.awt.*;
import java.awt.event.*;
import java.util.*;

import javax.swing.*;

import org.icehockeymanager.ihm.clients.devgui.controller.*;
import org.icehockeymanager.ihm.clients.devgui.gui.lib.*;
import org.icehockeymanager.ihm.clients.devgui.ihm.player.*;
import org.icehockeymanager.ihm.clients.devgui.ihm.scheduler.*;
import org.icehockeymanager.ihm.game.*;
import org.icehockeymanager.ihm.game.player.*;
import org.icehockeymanager.ihm.game.prospects.*;
import org.icehockeymanager.ihm.game.transfers.*;
import org.icehockeymanager.ihm.game.user.*;
import org.icehockeymanager.ihm.lib.*;

/**
 * A panel to show the player with stats and players.
 * 
 * @author Bernhard von Gunten
 * @created December 29, 2001
 */
public class PanelContract extends JIhmPanel {

  static final long serialVersionUID = 7914122697009902378L;

  public static final int MODE_EXTEND_CONTRACT = 1;

  public static final int MODE_TRANSFER = 2;

  public static final int MODE_HIRE_PROSPECT = 3;

  Player playerToShow = null;

  int mode = -1;

  JIhmLabel lblStartDate = new JIhmLabel();

  JIhmLabel lblEndDate = new JIhmLabel();

  JIhmLabel txtStart = new JIhmLabel();

  JIhmLabel txtEndDateMonth = new JIhmLabel();

  JTextField txtEndYear = new JTextField();

  JIhmButton cmdOk = new JIhmButton();

  JIhmButton cmdCancel = new JIhmButton();

  JIhmLabel lblPlayerName = new JIhmLabel();

  private JTextField txtSalary;

  private JIhmLabel lblSalary;

  JIhmCheckBox cbToday = new JIhmCheckBox();

  JIhmLabel lblTransferFee = new JIhmLabel();

  JIhmLabel txtTransferFee = new JIhmLabel();

  ImageIcon contractIcon = new ImageIcon();

  /* Gui controls */

  /**
   * Constructs the frame, sets the user, calls ihmInit
   * 
   * @param user
   *          User to show this frame for
   * @param playerToShow
   *          Player to show in this frame
   * @param mode Operation modeof the screen (by constants)
   */
  public PanelContract(User user, Player playerToShow, int mode) {
    super(user);
    try {
      initGUI();
      ihmInit(playerToShow, mode);
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
    lblStartDate.setText("contract.startDate");
    lblStartDate.setMsgKey("contract.startDate");
    lblStartDate.setBounds(35, 83, 120, 10);
    this.setSize(new Dimension(395, 302));
    this.setLayout(null);
    this.setForeground(Color.black);
    this.setDoubleBuffered(false);
    lblEndDate.setBounds(35, 123, 120, 10);
    lblEndDate.setText("contract.endDate");
    lblEndDate.setMsgKey("contract.endDate");
    txtStart.setText("##.##.####");
    txtStart.setBounds(175, 83, 80, 10);
    txtEndDateMonth.setBounds(175, 123, 40, 10);
    txtEndDateMonth.setText("##.##.####");
    txtEndYear.setText("");
    txtEndYear.setBounds(224, 117, 70, 20);
    cmdOk.setBounds(72, 223, 79, 24);
    cmdOk.setText("ihm.ok");
    cmdOk.setMsgKey("ihm.ok");
    cmdCancel.setBounds(206, 223, 79, 24);
    cmdCancel.setText("ihm.cancel");
    cmdCancel.setMsgKey("ihm.cancel");
    lblPlayerName.setFont(new java.awt.Font("Dialog", 0, 13));
    lblPlayerName.setText("ihm.playerName");
    lblPlayerName.setMsgKey("ihm.playerName");
    lblPlayerName.setBounds(new Rectangle(40, 18, 310, 22));
    cbToday.setText("contract.today");
    cbToday.setMsgKey("contract.today");
    cbToday.setBounds(new Rectangle(260, 77, 88, 22));
    cbToday.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        cbToday_actionPerformed(e);
      }
    });
    lblTransferFee.setText("contract.transferFee");
    lblTransferFee.setMsgKey("contract.transferFee");
    lblTransferFee.setBounds(35, 183, 120, 10);
    txtTransferFee.setText("0");
    txtTransferFee.setBounds(175, 183, 96, 12);
    this.add(cmdOk, null);
    cmdOk.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent evt) {
        cmdOk_actionPerformed(evt);
      }
    });
    this.add(cmdCancel, null);
    cmdCancel.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent evt) {
        cmdCancel_actionPerformed(evt);
      }
    });

    this.add(lblStartDate, null);
    this.add(lblEndDate, null);
    this.add(lblPlayerName, null);
    this.add(txtEndYear, null);
    this.add(cbToday, null);
    this.add(txtEndDateMonth, null);
    this.add(txtStart, null);
    this.add(lblTransferFee, null);
    this.add(txtTransferFee, null);
    {
      lblSalary = new JIhmLabel();
      this.add(lblSalary);
      lblSalary.setText("contract.salary");
      lblSalary.setMsgKey("contract.salary");
      lblSalary.setBounds(35, 153, 60, 10);
    }
    {
      txtSalary = new JTextField();
      this.add(txtSalary);
      txtSalary.setText("0");
      txtSalary.setBounds(176, 150, 90, 20);
    }
  }

  /**
   * Create the panelLeagueStandings and add it to the frame
   * 
   * @param playerToshow
   *          Player to show in this frame
   * @param mode Operation mode of the Panel
   */
  private void ihmInit(Player playerToshow, int mode) {
    this.playerToShow = playerToshow;
    this.mode = mode;

    this.lblPlayerName.setText(playerToshow.getPlayerInfo().getPlayerName() + "  (" + PlayerTools.getPositionStr(playerToshow.getPlayerAttributes()) + ")");

    this.txtEndDateMonth.setText(SchedulerTools.getLastDayMonth());

    Calendar start = null;
    if (mode == MODE_EXTEND_CONTRACT) {
      start = playerToshow.getPlayerContractCurrent().getStartDate();
    } else {
      start = playerToshow.getFirstPossibleWorkingDay();
    }

    if (mode == MODE_EXTEND_CONTRACT) {
      this.txtSalary.setText(Tools.doubleToStringC(playerToShow.getPlayerContractCurrent().getAmount()));
      this.txtTransferFee.setText(Tools.doubleToStringC(playerToShow.getPlayerContractCurrent().getTransferFee()));
    } else {
      this.txtSalary.setText(Tools.doubleToStringC(playerToShow.getFairSalary()));
      this.txtTransferFee.setText(Tools.doubleToStringC(playerToShow.getFairTransferFee()));
    }

    this.txtStart.setText(SchedulerTools.calendarToString(start));

    int years = Transfers.getAIRandomContractYears() + 1;
    start.add(Calendar.YEAR, years);

    this.txtEndYear.setText(SchedulerTools.extractYear(start));

    this.setPreferredSize(new java.awt.Dimension(394, 304));
    this.setBounds(0, 0, 394, 304);

    if (mode == MODE_EXTEND_CONTRACT || mode == MODE_HIRE_PROSPECT || playerToShow.getPlayerContractCurrent() == null) {
      this.cbToday.setVisible(false);
    }
  }

  void cmdCancel_actionPerformed(ActionEvent e) {
    ClientController.getInstance().getDesktop().closeDialog();
  }

  void cmdOk_actionPerformed(ActionEvent e) {
    Calendar end = GameController.getInstance().getScenario().getScheduler().getLastDay();
    end.set(Calendar.YEAR, Integer.valueOf(this.txtEndYear.getText()).intValue());

    if (mode == MODE_EXTEND_CONTRACT) {

      Transfers.extendContract(playerToShow, end, Double.valueOf(txtSalary.getText()).doubleValue());

    } else if (mode == MODE_TRANSFER) {

      Calendar begin = null;
      if (this.cbToday.isSelected() || playerToShow.getPlayerContractCurrent() == null) {
        begin = GameController.getInstance().getScenario().getScheduler().getToday();
        begin.add(Calendar.DATE, 1);
      } else {
        begin = playerToShow.getFirstPossibleWorkingDay();
      }

      Transfers.transferPlayer(playerToShow, getOwner().getTeam(), begin, end, Double.valueOf(txtSalary.getText()).doubleValue(), Double.valueOf(txtTransferFee.getText()).doubleValue());

    } else if (mode == MODE_HIRE_PROSPECT) {
      Calendar begin = playerToShow.getFirstPossibleWorkingDay();
      Prospects.hireProspect(playerToShow, getOwner().getTeam(), begin, end, Double.valueOf(txtSalary.getText()).doubleValue(), Double.valueOf(txtTransferFee.getText()).doubleValue());
    }

    ClientController.getInstance().getDesktop().closeDialog();
  }

  void cbToday_actionPerformed(ActionEvent e) {
    Calendar start = null;
    PlayerContract contract = playerToShow.getPlayerContractCurrent();

    if (this.cbToday.isSelected()) {
      start = GameController.getInstance().getScenario().getScheduler().getToday();
      start.add(Calendar.DATE, 1);
      this.txtTransferFee.setText(String.valueOf(contract.getTransferFee()));
    } else {
      start = playerToShow.getFirstPossibleWorkingDay();
      this.txtTransferFee.setText("0");
    }
    this.txtStart.setText(SchedulerTools.calendarToString(start));
  }

}
