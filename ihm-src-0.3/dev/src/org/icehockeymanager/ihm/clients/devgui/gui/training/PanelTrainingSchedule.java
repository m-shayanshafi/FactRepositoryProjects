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
  
package org.icehockeymanager.ihm.clients.devgui.gui.training;

import java.awt.*;
import java.awt.event.*;

import org.icehockeymanager.ihm.clients.devgui.controller.*;
import org.icehockeymanager.ihm.clients.devgui.gui.lib.*;
import org.icehockeymanager.ihm.game.player.fieldplayer.*;
import org.icehockeymanager.ihm.game.player.goalie.*;
import org.icehockeymanager.ihm.game.training.*;

/**
 * The PanelTrainingSchedule contains a interface to change the training units
 * for a team or player
 * 
 * @author Bernhard von Gunten
 * @created July 9, 2002
 */
public class PanelTrainingSchedule extends JIhmPanel {

  /**
   * Comment for <code>serialVersionUID</code>
   */
  private static final long serialVersionUID = 3258413936901503284L;

  private String[] fieldPlayerKeys = null;

  private String[] fieldPlayerLabels = null;

  private String[] goalieKeys = null;

  private String[] goalieLabels = null;

  /** LeagueElement displayed on this panel */
  TrainingSchedule trainingSchedule = null;

  JIhmLabel lblTrainingFieldPlayer = new JIhmLabel();

  JIhmLabel lblFPMorning = new JIhmLabel();

  JIhmComboBox cbFieldMoMorning = new JIhmComboBox();

  JIhmComboBox cbFieldThMorning = new JIhmComboBox();

  JIhmComboBox cbFieldTuMorning = new JIhmComboBox();

  JIhmComboBox cbFieldWeMorning = new JIhmComboBox();

  JIhmComboBox cbFieldSaMorning = new JIhmComboBox();

  JIhmComboBox cbFieldSuMorning = new JIhmComboBox();

  JIhmComboBox cbFieldFrMorning = new JIhmComboBox();

  JIhmComboBox cbFieldTuEvening = new JIhmComboBox();

  JIhmComboBox cbFieldSuEvening = new JIhmComboBox();

  JIhmLabel lblFPEvening = new JIhmLabel();

  JIhmComboBox cbFieldWeEvening = new JIhmComboBox();

  JIhmComboBox cbFieldThEvening = new JIhmComboBox();

  JIhmComboBox cbFieldMoEvening = new JIhmComboBox();

  JIhmComboBox cbFieldSaEvening = new JIhmComboBox();

  JIhmComboBox cbFieldFrEvening = new JIhmComboBox();

  JIhmComboBox cbGoalieThMorning = new JIhmComboBox();

  JIhmComboBox cbGoalieSuEvening = new JIhmComboBox();

  JIhmComboBox cbGoalieSuMorning = new JIhmComboBox();

  JIhmLabel lblGOEvening = new JIhmLabel();

  JIhmLabel lblGOMorning = new JIhmLabel();

  JIhmComboBox cbGoalieSaEvening = new JIhmComboBox();

  JIhmComboBox cbGoalieTuEvening = new JIhmComboBox();

  JIhmComboBox cbGoalieTuMorning = new JIhmComboBox();

  JIhmComboBox cbGoalieMoEvening = new JIhmComboBox();

  JIhmComboBox cbGoalieWeEvening = new JIhmComboBox();

  JIhmComboBox cbGoalieMoMorning = new JIhmComboBox();

  JIhmComboBox cbGoalieThEvening = new JIhmComboBox();

  JIhmComboBox cbGoalieSaMorning = new JIhmComboBox();

  JIhmComboBox cbGoalieWeMorning = new JIhmComboBox();

  JIhmComboBox cbGoalieFrEvening = new JIhmComboBox();

  JIhmComboBox cbGoalieFrMorning = new JIhmComboBox();

  JIhmLabel lblTrainingGoalie = new JIhmLabel();

  JIhmButton cmdSave = new JIhmButton();

  JIhmLabel lblMo = new JIhmLabel();

  JIhmLabel lblTh = new JIhmLabel();

  JIhmLabel lblWe = new JIhmLabel();

  JIhmLabel lblTu = new JIhmLabel();

  JIhmLabel lblFr = new JIhmLabel();

  JIhmLabel lblSa = new JIhmLabel();

  JIhmLabel lblSu = new JIhmLabel();

  /**
   * Creates the panel, saves the leagueElement and calls ihmInit
   * 
   * @param trainingSchedule
   *          Description of the Parameter
   */
  public PanelTrainingSchedule(TrainingSchedule trainingSchedule) {
    try {
      this.trainingSchedule = trainingSchedule;
      jbInit();
      ihmInit();
    } catch (Exception ex) {
      ex.printStackTrace();
    }
  }

  /**
   * JBuilder stuff
   * 
   * @exception Exception
   *              Exception
   */
  void jbInit() throws Exception {
    lblTrainingFieldPlayer.setText("ihm.fieldPlayer");
    lblTrainingFieldPlayer.setMsgKey("ihm.fieldPlayer");
    lblTrainingFieldPlayer.setBounds(new Rectangle(5, 34, 127, 17));
    this.setLayout(null);
    lblFPMorning.setText("training.morning");
    lblFPMorning.setMsgKey("training.morning");
    lblFPMorning.setBounds(new Rectangle(5, 62, 72, 17));
    cbFieldMoMorning.setBounds(new Rectangle(81, 57, 86, 26));
    cbFieldThMorning.setBounds(new Rectangle(345, 57, 86, 26));
    cbFieldTuMorning.setBounds(new Rectangle(169, 57, 86, 26));
    cbFieldWeMorning.setBounds(new Rectangle(257, 57, 86, 26));
    cbFieldSaMorning.setBounds(new Rectangle(521, 57, 86, 26));
    cbFieldSuMorning.setBounds(new Rectangle(609, 57, 86, 26));
    cbFieldFrMorning.setBounds(new Rectangle(433, 57, 86, 26));
    cbFieldTuEvening.setBounds(new Rectangle(169, 98, 86, 26));
    cbFieldSuEvening.setBounds(new Rectangle(609, 98, 86, 26));
    lblFPEvening.setBounds(new Rectangle(5, 103, 72, 17));
    lblFPEvening.setText("training.evening");
    lblFPEvening.setMsgKey("training.evening");
    cbFieldWeEvening.setBounds(new Rectangle(257, 98, 86, 26));
    cbFieldThEvening.setBounds(new Rectangle(345, 98, 86, 26));
    cbFieldMoEvening.setBounds(new Rectangle(81, 98, 86, 26));
    cbFieldSaEvening.setBounds(new Rectangle(521, 98, 86, 26));
    cbFieldFrEvening.setBounds(new Rectangle(433, 98, 86, 26));
    cbGoalieThMorning.setBounds(new Rectangle(345, 180, 86, 26));
    cbGoalieSuEvening.setBounds(new Rectangle(609, 221, 86, 26));
    cbGoalieSuMorning.setBounds(new Rectangle(609, 180, 86, 26));
    lblGOEvening.setText("training.evening");
    lblGOEvening.setMsgKey("training.evening");
    lblGOEvening.setBounds(new Rectangle(5, 226, 72, 17));
    lblGOMorning.setBounds(new Rectangle(5, 185, 72, 17));
    lblGOMorning.setText("training.morning");
    lblGOMorning.setMsgKey("training.morning");
    cbGoalieSaEvening.setBounds(new Rectangle(521, 221, 86, 26));
    cbGoalieTuEvening.setBounds(new Rectangle(169, 221, 86, 26));
    cbGoalieTuMorning.setBounds(new Rectangle(169, 180, 86, 26));
    cbGoalieMoEvening.setBounds(new Rectangle(81, 221, 86, 26));
    cbGoalieWeEvening.setBounds(new Rectangle(257, 221, 86, 26));
    cbGoalieMoMorning.setBounds(new Rectangle(81, 180, 86, 26));
    cbGoalieThEvening.setBounds(new Rectangle(345, 221, 86, 26));
    cbGoalieSaMorning.setBounds(new Rectangle(521, 180, 86, 26));
    cbGoalieWeMorning.setBounds(new Rectangle(257, 180, 86, 26));
    cbGoalieFrEvening.setBounds(new Rectangle(433, 221, 86, 26));
    cbGoalieFrMorning.setBounds(new Rectangle(433, 180, 86, 26));
    lblTrainingGoalie.setBounds(new Rectangle(5, 151, 91, 17));
    lblTrainingGoalie.setText("ihm.goalie");
    lblTrainingGoalie.setMsgKey("ihm.goalie");
    cmdSave.setText("ihm.save");
    cmdSave.setMsgKey("ihm.save");
    cmdSave.setBounds(new Rectangle(613, 269, 81, 27));
    cmdSave.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        cmdSave_actionPerformed(e);
      }
    });
    lblMo.setText("ihm.monday");
    lblMo.setMsgKey("ihm.monday");
    lblMo.setBounds(new Rectangle(86, 8, 77, 15));
    lblTh.setBounds(new Rectangle(350, 8, 77, 15));
    lblTh.setText("ihm.thursday");
    lblTh.setMsgKey("ihm.thursday");
    lblWe.setBounds(new Rectangle(262, 8, 77, 15));
    lblWe.setText("ihm.wednesday");
    lblWe.setMsgKey("ihm.wednesday");
    lblTu.setBounds(new Rectangle(174, 8, 77, 15));
    lblTu.setText("ihm.tuesday");
    lblTu.setMsgKey("ihm.tuesday");
    lblFr.setBounds(new Rectangle(438, 8, 77, 15));
    lblFr.setText("ihm.friday");
    lblFr.setMsgKey("ihm.friday");
    lblSa.setBounds(new Rectangle(526, 8, 77, 15));
    lblSa.setText("ihm.saturday");
    lblSa.setMsgKey("ihm.saturday");
    lblSu.setBounds(new Rectangle(614, 8, 77, 15));
    lblSu.setText("ihm.sunday");
    lblSu.setMsgKey("ihm.sunday");
    this.add(cbFieldMoMorning, null);
    this.add(cbFieldTuEvening, null);
    this.add(lblFPEvening, null);
    this.add(cbFieldWeEvening, null);
    this.add(cbFieldThEvening, null);
    this.add(cbFieldMoEvening, null);
    this.add(lblFPMorning, null);
    this.add(cbFieldThMorning, null);
    this.add(cbFieldWeMorning, null);
    this.add(cbFieldTuMorning, null);
    this.add(cbGoalieThMorning, null);
    this.add(lblGOEvening, null);
    this.add(lblGOMorning, null);
    this.add(cbGoalieTuEvening, null);
    this.add(cbGoalieTuMorning, null);
    this.add(cbGoalieMoEvening, null);
    this.add(cbGoalieWeEvening, null);
    this.add(cbGoalieMoMorning, null);
    this.add(cbGoalieThEvening, null);
    this.add(cbGoalieWeMorning, null);
    this.add(lblTrainingGoalie, null);
    this.add(cmdSave, null);
    this.add(lblWe, null);
    this.add(lblMo, null);
    this.add(lblTh, null);
    this.add(lblTu, null);
    this.add(lblTrainingFieldPlayer, null);
    this.add(cbFieldSuMorning, null);
    this.add(cbFieldSuEvening, null);
    this.add(cbFieldSaEvening, null);
    this.add(cbFieldSaMorning, null);
    this.add(cbGoalieSuEvening, null);
    this.add(cbGoalieSuMorning, null);
    this.add(cbGoalieSaEvening, null);
    this.add(cbGoalieSaMorning, null);
    this.add(cbFieldFrMorning, null);
    this.add(cbFieldFrEvening, null);
    this.add(cbGoalieFrEvening, null);
    this.add(cbGoalieFrMorning, null);
    this.add(lblSu, null);
    this.add(lblSa, null);
    this.add(lblFr, null);
  }

  /**
   * Fills comboboxes and calls displayTraining that shows the selected training
   * units
   */
  private void ihmInit() {
    this.setTitleKey("title.training");

    fieldPlayerKeys = FieldPlayerAttributes.getAttributeKeys();
    fieldPlayerLabels = new String[fieldPlayerKeys.length];
    for (int i = 0; i < fieldPlayerKeys.length; i++) {
      fieldPlayerLabels[i] = ClientController.getInstance().getTranslation("player.attributes.field." + fieldPlayerKeys[i]);
    }

    goalieKeys = GoalieAttributes.getAttributeKeys();
    goalieLabels = new String[goalieKeys.length];
    for (int i = 0; i < goalieKeys.length; i++) {
      goalieLabels[i] = ClientController.getInstance().getTranslation("player.attributes.goalie." + goalieKeys[i]);
    }

    fillupTrainingUnits(cbFieldMoMorning, true);
    fillupTrainingUnits(cbFieldThMorning, true);
    fillupTrainingUnits(cbFieldWeMorning, true);
    fillupTrainingUnits(cbFieldTuMorning, true);
    fillupTrainingUnits(cbFieldFrMorning, true);
    fillupTrainingUnits(cbFieldSaMorning, true);
    fillupTrainingUnits(cbFieldSuMorning, true);

    fillupTrainingUnits(cbFieldMoEvening, true);
    fillupTrainingUnits(cbFieldThEvening, true);
    fillupTrainingUnits(cbFieldWeEvening, true);
    fillupTrainingUnits(cbFieldTuEvening, true);
    fillupTrainingUnits(cbFieldFrEvening, true);
    fillupTrainingUnits(cbFieldSaEvening, true);
    fillupTrainingUnits(cbFieldSuEvening, true);

    fillupTrainingUnits(cbGoalieMoMorning, false);
    fillupTrainingUnits(cbGoalieThMorning, false);
    fillupTrainingUnits(cbGoalieWeMorning, false);
    fillupTrainingUnits(cbGoalieTuMorning, false);
    fillupTrainingUnits(cbGoalieFrMorning, false);
    fillupTrainingUnits(cbGoalieSaMorning, false);
    fillupTrainingUnits(cbGoalieSuMorning, false);

    fillupTrainingUnits(cbGoalieMoEvening, false);
    fillupTrainingUnits(cbGoalieThEvening, false);
    fillupTrainingUnits(cbGoalieWeEvening, false);
    fillupTrainingUnits(cbGoalieTuEvening, false);
    fillupTrainingUnits(cbGoalieFrEvening, false);
    fillupTrainingUnits(cbGoalieSaEvening, false);
    fillupTrainingUnits(cbGoalieSuEvening, false);

    displayTraining();
  }

  /**
   * Saves training to trainingSchedule
   * 
   * @param cb
   *          Combobox
   * @param fieldplayer
   *          Is player fieldplayer
   * @param day
   *          Day
   * @param time
   *          Time
   */
  private void storeTrainingUnit(JIhmComboBox cb, boolean fieldplayer, int day, int time) {
    if (fieldplayer) {
      if (cb.getSelectedIndex() == 0) {
        trainingSchedule.setFieldPlayerTrainingAttribute(null, day, time);
      } else {
        trainingSchedule.setFieldPlayerTrainingAttribute(fieldPlayerKeys[cb.getSelectedIndex() - 1], day, time);
      }
    } else {
      if (cb.getSelectedIndex() == 0) {
        trainingSchedule.setGoalieTrainingAttribute(null, day, time);
      } else {
        trainingSchedule.setGoalieTrainingAttribute(goalieKeys[cb.getSelectedIndex() - 1], day, time);
      }
    }

  }

  /**
   * Fill comboboxes with training attributes
   * 
   * @param cb
   *          Combobox to fill
   * @param fieldplayer
   *          Is player fieldplayer
   */
  private void fillupTrainingUnits(JIhmComboBox cb, boolean fieldplayer) {
    cb.removeAllItems();
    cb.addItem(ClientController.getInstance().getTranslation("training.noTraining"));
    if (fieldplayer) {
      for (int i = 0; i < fieldPlayerLabels.length; i++) {
        cb.addItem(fieldPlayerLabels[i]);
      }
    } else {
      for (int i = 0; i < goalieLabels.length; i++) {
        cb.addItem(goalieLabels[i]);
      }
    }

  }

  /**
   * Converts a attribute key to a index in the comboBox
   * 
   * @param key
   *          Attribute key
   * @param fieldPlayer
   *          Is player fieldPlayer
   * @return Index of the attribute
   */
  private int keyToIndex(String key, boolean fieldPlayer) {
    if (fieldPlayer) {
      for (int i = 0; i < fieldPlayerKeys.length; i++) {
        if (fieldPlayerKeys[i].equals(key)) {
          return i + 1;
        }
      }
    } else {
      for (int i = 0; i < goalieKeys.length; i++) {
        if (goalieKeys[i].equals(key)) {
          return i + 1;
        }
      }
    }
    // No Training
    return 0;
  }

  /** Display the trainingSchedule */
  public void displayTraining() {

    cbFieldMoMorning.setSelectedIndex(keyToIndex(trainingSchedule.getFieldPlayerTrainingAttribute(0, 0), true));
    cbFieldTuMorning.setSelectedIndex(keyToIndex(trainingSchedule.getFieldPlayerTrainingAttribute(1, 0), true));
    cbFieldWeMorning.setSelectedIndex(keyToIndex(trainingSchedule.getFieldPlayerTrainingAttribute(2, 0), true));
    cbFieldThMorning.setSelectedIndex(keyToIndex(trainingSchedule.getFieldPlayerTrainingAttribute(3, 0), true));
    cbFieldFrMorning.setSelectedIndex(keyToIndex(trainingSchedule.getFieldPlayerTrainingAttribute(4, 0), true));
    cbFieldSaMorning.setSelectedIndex(keyToIndex(trainingSchedule.getFieldPlayerTrainingAttribute(5, 0), true));
    cbFieldSuMorning.setSelectedIndex(keyToIndex(trainingSchedule.getFieldPlayerTrainingAttribute(6, 0), true));

    cbFieldMoEvening.setSelectedIndex(keyToIndex(trainingSchedule.getFieldPlayerTrainingAttribute(0, 1), true));
    cbFieldTuEvening.setSelectedIndex(keyToIndex(trainingSchedule.getFieldPlayerTrainingAttribute(1, 1), true));
    cbFieldWeEvening.setSelectedIndex(keyToIndex(trainingSchedule.getFieldPlayerTrainingAttribute(2, 1), true));
    cbFieldThEvening.setSelectedIndex(keyToIndex(trainingSchedule.getFieldPlayerTrainingAttribute(3, 1), true));
    cbFieldFrEvening.setSelectedIndex(keyToIndex(trainingSchedule.getFieldPlayerTrainingAttribute(4, 1), true));
    cbFieldSaEvening.setSelectedIndex(keyToIndex(trainingSchedule.getFieldPlayerTrainingAttribute(5, 1), true));
    cbFieldSuEvening.setSelectedIndex(keyToIndex(trainingSchedule.getFieldPlayerTrainingAttribute(6, 1), true));

    cbGoalieMoMorning.setSelectedIndex(keyToIndex(trainingSchedule.getGoalieTrainingAttribute(0, 0), false));
    cbGoalieTuMorning.setSelectedIndex(keyToIndex(trainingSchedule.getGoalieTrainingAttribute(1, 0), false));
    cbGoalieWeMorning.setSelectedIndex(keyToIndex(trainingSchedule.getGoalieTrainingAttribute(2, 0), false));
    cbGoalieThMorning.setSelectedIndex(keyToIndex(trainingSchedule.getGoalieTrainingAttribute(3, 0), false));
    cbGoalieFrMorning.setSelectedIndex(keyToIndex(trainingSchedule.getGoalieTrainingAttribute(4, 0), false));
    cbGoalieSaMorning.setSelectedIndex(keyToIndex(trainingSchedule.getGoalieTrainingAttribute(5, 0), false));
    cbGoalieSuMorning.setSelectedIndex(keyToIndex(trainingSchedule.getGoalieTrainingAttribute(6, 0), false));

    cbGoalieMoEvening.setSelectedIndex(keyToIndex(trainingSchedule.getGoalieTrainingAttribute(0, 1), false));
    cbGoalieTuEvening.setSelectedIndex(keyToIndex(trainingSchedule.getGoalieTrainingAttribute(1, 1), false));
    cbGoalieWeEvening.setSelectedIndex(keyToIndex(trainingSchedule.getGoalieTrainingAttribute(2, 1), false));
    cbGoalieThEvening.setSelectedIndex(keyToIndex(trainingSchedule.getGoalieTrainingAttribute(3, 1), false));
    cbGoalieFrEvening.setSelectedIndex(keyToIndex(trainingSchedule.getGoalieTrainingAttribute(4, 1), false));
    cbGoalieSaEvening.setSelectedIndex(keyToIndex(trainingSchedule.getGoalieTrainingAttribute(5, 1), false));
    cbGoalieSuEvening.setSelectedIndex(keyToIndex(trainingSchedule.getGoalieTrainingAttribute(6, 1), false));
  }

  /**
   * Save all trainingUnits to the trainingSchedule.
   * 
   * @param e
   *          Description of the Parameter
   */
  void cmdSave_actionPerformed(ActionEvent e) {
    storeTrainingUnit(cbFieldMoMorning, true, 0, 0);
    storeTrainingUnit(cbFieldTuMorning, true, 1, 0);
    storeTrainingUnit(cbFieldWeMorning, true, 2, 0);
    storeTrainingUnit(cbFieldThMorning, true, 3, 0);
    storeTrainingUnit(cbFieldFrMorning, true, 4, 0);
    storeTrainingUnit(cbFieldSaMorning, true, 5, 0);
    storeTrainingUnit(cbFieldSuMorning, true, 6, 0);

    storeTrainingUnit(cbFieldMoEvening, true, 0, 1);
    storeTrainingUnit(cbFieldTuEvening, true, 1, 1);
    storeTrainingUnit(cbFieldWeEvening, true, 2, 1);
    storeTrainingUnit(cbFieldThEvening, true, 3, 1);
    storeTrainingUnit(cbFieldFrEvening, true, 4, 1);
    storeTrainingUnit(cbFieldSaEvening, true, 5, 1);
    storeTrainingUnit(cbFieldSuEvening, true, 6, 1);

    storeTrainingUnit(cbGoalieMoMorning, false, 0, 0);
    storeTrainingUnit(cbGoalieTuMorning, false, 1, 0);
    storeTrainingUnit(cbGoalieWeMorning, false, 2, 0);
    storeTrainingUnit(cbGoalieThMorning, false, 3, 0);
    storeTrainingUnit(cbGoalieFrMorning, false, 4, 0);
    storeTrainingUnit(cbGoalieSaMorning, false, 5, 0);
    storeTrainingUnit(cbGoalieSuMorning, false, 6, 0);

    storeTrainingUnit(cbGoalieMoEvening, false, 0, 1);
    storeTrainingUnit(cbGoalieTuEvening, false, 1, 1);
    storeTrainingUnit(cbGoalieWeEvening, false, 2, 1);
    storeTrainingUnit(cbGoalieThEvening, false, 3, 1);
    storeTrainingUnit(cbGoalieFrEvening, false, 4, 1);
    storeTrainingUnit(cbGoalieSaEvening, false, 5, 1);
    storeTrainingUnit(cbGoalieSuEvening, false, 6, 1);

  }

}
