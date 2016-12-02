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
  
package org.icehockeymanager.ihm.clients.devgui.gui.gameday.simple;

import java.awt.*;
import java.awt.event.*;
import java.util.*;

import javax.swing.*;
import javax.swing.border.*;

import org.icehockeymanager.ihm.clients.devgui.controller.*;
import org.icehockeymanager.ihm.clients.devgui.gui.*;
import org.icehockeymanager.ihm.clients.devgui.gui.lib.*;
import org.icehockeymanager.ihm.clients.devgui.ihm.league.*;
import org.icehockeymanager.ihm.clients.devgui.ihm.match.*;
import org.icehockeymanager.ihm.game.league.events.*;
import org.icehockeymanager.ihm.game.match.*;
import org.icehockeymanager.ihm.game.match.textengine.data.*;
import org.icehockeymanager.ihm.game.user.*;

/**
 * PanelGameDay shows matches live via "radio" :-)
 * 
 * @author Bernhard von Gunten
 * @created January, 2005
 */
public class PanelSimpleGameDay extends JIhmPanel {

  static final long serialVersionUID = -2658453867616748321L;

  /** GameDayEvent */
  protected GameDayMatchesEvent gameDayEvent = null;

  protected Desktop desktop = null;

  protected Match currentRadioMatch = null;

  protected Hashtable<Match, StringBuffer> radioMatches = null;

  protected MatchListener allListener = null;

  protected MatchController controller = null;

  ImageIcon ihmIcon = new ImageIcon();

  private JIhmPanel jIhmPanel4;

  private JIhmLabel lblRadioFactTime;

  private JIhmLabel lblRadioFactScore;

  private JIhmPanel jIhmPanel7;

  private JIhmPanel jIhmPanel6;

  private JTextArea txtRadio;

  private JIhmButton bntRestart;

  public JIhmCheckBox cbStopForScoreChanges;

  public JIhmCheckBox cbStopForPeriodFinished;

  public JIhmCheckBox cbStopForSceneFinished;

  private JScrollPane jScrollPane2;

  private JIhmPanel jIhmPanel5;

  private JIhmPanel jIhmPanel3;

  private JIhmPanel panelHeader;

  protected JIhmLabel lblLeague;

  private JIhmLabel lblMatches;

  protected JList jMatchList;

  private JScrollPane jScrollPane1;

  public JIhmButton cmdMoveOn;

  protected JIhmButton cmdPlay;

  private JIhmPanel panelControl;

  public PanelSimpleGameDay(User user) {
    super(user);
  }
  
  /**
   * Conscructs the frame, sets the user (=null) and desktop of this frame
   * @param desktop 
   * 
   * @param user
   *          User to show this frame for (mostly null)
   * @param gameDayEvent
   *          GameDay to show in this frame
   */
  public PanelSimpleGameDay(Desktop desktop, User user, GameDayMatchesEvent gameDayEvent) {
    super(user);
    this.desktop = desktop;
    try {
      initGUI();
      ihmInit(gameDayEvent);
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
  protected void initGUI() throws Exception {

    this.setSize(400, 350);
    BorderLayout thisLayout = new BorderLayout();

    this.setLayout(thisLayout);

    this.setPreferredSize(new java.awt.Dimension(825, 668));
    {
      panelControl = new JIhmPanel();
      this.add(panelControl, BorderLayout.WEST);
      panelControl.setMinimumSize(new java.awt.Dimension(300, 400));
      panelControl.setLayout(null);
      panelControl.setSize(300, 400);
      panelControl.setBounds(16, 10, 371, 416);
      panelControl.setPreferredSize(new java.awt.Dimension(298, 434));
      panelControl.setBorder(new SoftBevelBorder(BevelBorder.LOWERED, null, null, null, null));
      {
        cmdPlay = new JIhmButton();
        panelControl.add(cmdPlay);
        cmdPlay.setText("match.playMatches");
        cmdPlay.setMsgKey("match.playMatches");
        cmdPlay.setBounds(17, 278, 247, 25);
        cmdPlay.addActionListener(new ActionListener() {
          public void actionPerformed(ActionEvent evt) {
            cmdPlay_actionPerformed(evt);
          }
        });
      }
      {
        cmdMoveOn = new JIhmButton();
        panelControl.add(cmdMoveOn);
        cmdMoveOn.setEnabled(false);
        cmdMoveOn.setText("ihm.moveOn");
        cmdMoveOn.setMsgKey("ihm.moveOn");
        cmdMoveOn.setBounds(17, 480, 247, 25);
        cmdMoveOn.addActionListener(new ActionListener() {
          public void actionPerformed(ActionEvent evt) {
            cmdMoveOn_actionPerformed(evt);
          }
        });
      }
      {
        jScrollPane1 = new JScrollPane();
        panelControl.add(jScrollPane1);
        jScrollPane1.setBounds(16, 63, 267, 185);
        {
          ListModel jMatchListModel = new DefaultComboBoxModel(new String[] { "Item One", "Item Two" });
          jMatchList = new JList();
          jScrollPane1.setViewportView(jMatchList);
          jMatchList.setModel(jMatchListModel);
          jMatchList.setPreferredSize(new java.awt.Dimension(263, 182));
          jMatchList.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
              jMatchList_mouseClicked(e);
            }
          });
        }
      }
      {
        lblMatches = new JIhmLabel();
        panelControl.add(lblMatches);
        lblMatches.setText("ihm.matches");
        lblMatches.setMsgKey("ihm.matches");
        lblMatches.setFont(new java.awt.Font("Dialog", 2, 12));
        lblMatches.setBounds(new Rectangle(14, 40, 140, 15));
      }
      {
        cbStopForSceneFinished = new JIhmCheckBox();
        panelControl.add(cbStopForSceneFinished);
        cbStopForSceneFinished.setText("match.stopAfterScenes");
        cbStopForSceneFinished.setMsgKey("match.stopAfterScenes");

        cbStopForSceneFinished.setBounds(17, 351, 175, 30);
        cbStopForSceneFinished.setSelected(true);
      }
      {
        cbStopForPeriodFinished = new JIhmCheckBox();
        panelControl.add(cbStopForPeriodFinished);
        cbStopForPeriodFinished.setText("match.stopAfterPeriods");
        cbStopForPeriodFinished.setMsgKey("match.stopAfterPeriods");
        cbStopForPeriodFinished.setBounds(17, 395, 175, 30);
        cbStopForPeriodFinished.setSelected(true);
      }
      {
        cbStopForScoreChanges = new JIhmCheckBox();
        panelControl.add(cbStopForScoreChanges);
        cbStopForScoreChanges.setText("match.stopAfterScoreChanges");
        cbStopForScoreChanges.setMsgKey("match.stopAfterScoreChanges");
        cbStopForScoreChanges.setBounds(17, 440, 175, 30);
        cbStopForScoreChanges.setSelected(true);
      }
      {
        bntRestart = new JIhmButton();
        panelControl.add(bntRestart);
        bntRestart.setText("match.continueGame");
        bntRestart.setMsgKey("match.continueGame");
        bntRestart.setBounds(17, 310, 247, 25);
        bntRestart.setEnabled(false);
        bntRestart.addActionListener(new ActionListener() {
          public void actionPerformed(ActionEvent evt) {
            moveOnGames();
          }
        });
      }
    }
    {
      panelHeader = new JIhmPanel();
      this.add(panelHeader, BorderLayout.NORTH);
      panelHeader.setLayout(null);
      panelHeader.setPreferredSize(new java.awt.Dimension(825, 24));
      panelHeader.setBorder(new SoftBevelBorder(BevelBorder.LOWERED, null, null, null, null));
      {
        lblLeague = new JIhmLabel();
        panelHeader.add(lblLeague);
        lblLeague.setText(".....");
        lblLeague.setFont(new java.awt.Font("Dialog", 1, 14));
        lblLeague.setBounds(11, 4, 784, 17);
      }
    }
    {
      jIhmPanel3 = new JIhmPanel();
      BorderLayout jIhmPanel3Layout = new BorderLayout();
      jIhmPanel3.setLayout(jIhmPanel3Layout);
      this.add(jIhmPanel3, BorderLayout.CENTER);
      {
        jIhmPanel4 = new JIhmPanel();
        jIhmPanel3.add(jIhmPanel4, BorderLayout.NORTH);
        jIhmPanel4.setPreferredSize(new java.awt.Dimension(517, 104));
        jIhmPanel4.setBorder(new SoftBevelBorder(BevelBorder.LOWERED, null, null, null, null));
        jIhmPanel4.setLayout(null);
        {
          lblRadioFactScore = new JIhmLabel();
          jIhmPanel4.add(lblRadioFactScore);
          lblRadioFactScore.setText("<score>");
          lblRadioFactScore.setBounds(81, 27, 364, 23);
          lblRadioFactScore.setFont(new java.awt.Font("Dialog", 1, 20));
          lblRadioFactScore.setHorizontalAlignment(SwingConstants.CENTER);
          lblRadioFactScore.setHorizontalTextPosition(SwingConstants.CENTER);
        }
        {
          lblRadioFactTime = new JIhmLabel();
          jIhmPanel4.add(lblRadioFactTime);
          lblRadioFactTime.setText("<time>");
          lblRadioFactTime.setBounds(16, 59, 122, 30);
          lblRadioFactTime.setHorizontalAlignment(SwingConstants.LEFT);
          lblRadioFactTime.setHorizontalTextPosition(SwingConstants.LEFT);
        }
      }
      {
        jIhmPanel5 = new JIhmPanel();
        BorderLayout jIhmPanel5Layout = new BorderLayout();
        jIhmPanel5.setLayout(jIhmPanel5Layout);
        jIhmPanel3.add(jIhmPanel5, BorderLayout.CENTER);
        {
          jScrollPane2 = new JScrollPane();
          jIhmPanel5.add(jScrollPane2, BorderLayout.CENTER);
          {
            txtRadio = new JTextArea();
            jScrollPane2.setViewportView(txtRadio);
            txtRadio.setEditable(false);
          }
        }
      }
    }
    {
      jIhmPanel6 = new JIhmPanel();
      this.add(jIhmPanel6, BorderLayout.EAST);
      jIhmPanel6.setBorder(new SoftBevelBorder(BevelBorder.LOWERED, null, null, null, null));
    }
    {
      jIhmPanel7 = new JIhmPanel();
      this.add(jIhmPanel7, BorderLayout.SOUTH);
      jIhmPanel7.setBorder(new SoftBevelBorder(BevelBorder.LOWERED, null, null, null, null));
    }

  }

  /**
   * Display the game Day
   * 
   * @param gameDayEvent
   *          GameDay to show in this frame
   */
  public void ihmInit(GameDayMatchesEvent gameDayEvent) {
    this.setTitleKey("menu.gameDay");
    this.gameDayEvent = gameDayEvent;
    this.lblLeague.setText(LeagueTools.getGameDayDescription(gameDayEvent));
    this.displayMatches();

    this.allListener = getAllMatchListener();

    this.currentRadioMatch = gameDayEvent.getMatch(this.getOwner());

    if (currentRadioMatch == null) {
      this.currentRadioMatch = gameDayEvent.getMatchesArray()[0];
    }

    updateRadio();
  }

  /** Display the results of the matches of this gameDay */
  protected void displayMatches() {
    jMatchList.setModel(new LMMatches(gameDayEvent.getMatches()));
    this.validate();
  }

  /**
   * Plays all matches
   * 
   * @param e
   *          Description of the Parameter
   */
  protected void cmdPlay_actionPerformed(ActionEvent e) {

    this.jMatchList.setEnabled(false);
    this.cmdPlay.setEnabled(false);
    this.radioMatches = new Hashtable<Match, StringBuffer>();

    Vector<Match> matches = gameDayEvent.getMatches();
    for (int i = 0; i < matches.size(); i++) {
      Match tmp = matches.get(i);
      // tmp.initGame();
      tmp.addMatchListener(allListener);

      RadioMatchListener rml = new RadioMatchListener(tmp);
      tmp.addMatchListener(rml);

      radioMatches.put(tmp, new StringBuffer());
    }

    controller = new MatchController(gameDayEvent, ClientController.getInstance().getSettings().MATCHCONTROLLER_DELAY);

    controller.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        cmdPlay.setEnabled(false);
        cmdMoveOn.setEnabled(true);
      }
    });

    controller.start();

  }

  /**
   * Closes screen and fires ActionEvent to all listeners
   * 
   * @param e
   *          Description of the Parameter
   */
  void cmdMoveOn_actionPerformed(ActionEvent e) {

    Vector<Match> matches = gameDayEvent.getMatches();
    for (int i = 0; i < matches.size(); i++) {
      Match tmp = matches.get(i);
      tmp.removeAllMatchListeners();
    }

    controller.removeAllActionListeners();
    desktop.moveOn();
  }

  void jMatchList_mouseClicked(MouseEvent e) {
    LMMatches lm = (LMMatches) jMatchList.getModel();
    int row = jMatchList.getSelectedIndex();
    if (row < 0)
      row = 0;

    this.currentRadioMatch = lm.getMatch(row);

    updateRadio();
  }

  protected void updateRadio() {
    try {
      StringBuffer untilNow = radioMatches.get(currentRadioMatch);
      txtRadio.setText(untilNow.toString());
    } catch (Exception ignored) {
    }
    updateRadioFacts("");
  }

  public void updateRadioFacts(String toAppend) {
    if (currentRadioMatch != null) {
      this.lblRadioFactScore.setText(MatchTools.getMatchDescription(currentRadioMatch));
      this.lblRadioFactTime.setText("Time: " + MatchTools.getMatchTime(currentRadioMatch.getSecondsPlayed()));
      this.txtRadio.append(toAppend);
      this.txtRadio.setCaretPosition(txtRadio.getText().length());
      try {
        Thread.sleep(ClientController.getInstance().getSettings().RADIO_DELAY);
      } catch (InterruptedException ex) {
      }
    }
  }

  public void appendToRadio(Match match, String txt) {
    StringBuffer oldText = radioMatches.get(match);
    oldText.append(txt + "\n");

    if (match.equals(currentRadioMatch)) {
      updateRadioFacts(txt + "\n");
    }

  }

  public void appendToRadio(Match match, String[] txt) {
    for (int i = 0; i < txt.length; i++) {
      appendToRadio(match, txt[i]);
    }
  }

  public void haltGames() {
    controller.setSuspended();
    this.bntRestart.setEnabled(true);
    this.jMatchList.setEnabled(true);
  }

  private void moveOnGames() {
    this.jMatchList.setEnabled(false);
    this.bntRestart.setEnabled(false);
    controller.moveOn();
  }

  protected MatchListener getAllMatchListener() {
    return new MatchListener() {

      public void regularPeriodStarted() {

      }

      public void overtimePeriodStarted() {

      }

      public void goToOvertime() {

      }

      public void regularPeriodFinished() {

      }

      public void overtimePeriodFinished() {

      }

      public void gameFinished() {

      }

      public void scoreChange() {
        displayMatches();
      }

      public void sceneFinished() {
      }

    };
  }

  public class RadioMatchListener implements MatchListener {

    private Match myMatch = null;

    public RadioMatchListener(Match match) {
      this.myMatch = match;
    }

    public void regularPeriodStarted() {
      appendToRadio(myMatch, "");
      appendToRadio(myMatch, "This regular period starts right now. Have fun !");
      appendToRadio(myMatch, "");
    }

    public void overtimePeriodStarted() {
      appendToRadio(myMatch, "");
      appendToRadio(myMatch, "This overtime period starts right now. Have fun !");
      appendToRadio(myMatch, "");
    }

    public void goToOvertime() {
      appendToRadio(myMatch, "");
      appendToRadio(myMatch, "The regular game is over, we're heading to overtime !");
      appendToRadio(myMatch, "");
      if (currentRadioMatch.equals(myMatch) && cbStopForPeriodFinished.isSelected()) {
        haltGames();
      }
    }

    public void regularPeriodFinished() {
      appendToRadio(myMatch, "");
      appendToRadio(myMatch, "This regular period is finished !");
      appendToRadio(myMatch, "");
      if (currentRadioMatch.equals(myMatch) && cbStopForPeriodFinished.isSelected()) {
        haltGames();
      }
    }

    public void overtimePeriodFinished() {
      appendToRadio(myMatch, "");
      appendToRadio(myMatch, "This overtime period is finished !");
      appendToRadio(myMatch, "");
      if (currentRadioMatch.equals(myMatch) && cbStopForPeriodFinished.isSelected()) {
        haltGames();
      }
    }

    public void sceneFinished() {
      if (currentRadioMatch.equals(myMatch) && cbStopForSceneFinished.isSelected()) {
        appendToRadio(myMatch, "Scene finished.");
        haltGames();
      }
    }

    public void faceOff(MatchDataFaceOff ev) {
      appendToRadio(myMatch, ev.getPlayerA().getPlayerInfo().getLastName() + " meets " + ev.getPlayerB().getPlayerInfo().getLastName() + " at the faceoff.");
      appendToRadio(myMatch, "The Puck goes to " + ev.getPuckHolder().getPlayerInfo().getLastName());
      appendToRadio(myMatch, "Possession " + ev.getPuckHolder().getTeam().getTeamInfo().getTeamName());
      appendToRadio(myMatch, "");
    }

    public void situation(MatchDataSituation ev) {
      appendToRadio(myMatch, MatchTools.generateSituationRadioMessage(ev));
    }

    public void gameFinished() {
      appendToRadio(myMatch, "");
      appendToRadio(myMatch, "This game is finished !");
      appendToRadio(myMatch, "");
    }

    public void scoreChange() {
      updateRadioFacts("");
      appendToRadio(myMatch, "Score by: " + myMatch.getScoreSheet().getLastGoal().getPlayer().getPlayerInfo().getLastName());
      if (currentRadioMatch.equals(myMatch) && cbStopForScoreChanges.isSelected()) {
        haltGames();
      }

    }

  }

}
