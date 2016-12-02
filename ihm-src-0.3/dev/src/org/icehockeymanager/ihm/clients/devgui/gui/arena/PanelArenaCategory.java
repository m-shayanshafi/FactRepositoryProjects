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
  
package org.icehockeymanager.ihm.clients.devgui.gui.arena;

import java.awt.event.*;

import javax.swing.*;
import javax.swing.border.*;

import org.icehockeymanager.ihm.clients.devgui.controller.*;
import org.icehockeymanager.ihm.clients.devgui.gui.lib.*;
import org.icehockeymanager.ihm.clients.devgui.ihm.infrastructure.arena.*;
import org.icehockeymanager.ihm.game.infrastructure.arena.*;
import org.icehockeymanager.ihm.game.user.*;
import org.icehockeymanager.ihm.lib.*;

public class PanelArenaCategory extends JIhmPanel {

  private static final long serialVersionUID = 63366449699025540L;
  private JIhmLabel lblCurrentCapacity;
  private JIhmTextField txtNewCapacity;
  private JIhmPanel pnlIncreaseCap;
  private JIhmPanel pnlCondition;
  private JIhmLabel lblCondition;
  private JIhmButton btnComfort;
  private JIhmComboBox cbComfort;
  private JIhmButton btnClose;
  private JIhmPanel pnlComfort;
  private JIhmButton btnMaintain;
  private JIhmButton btnIncreaseCapacity;

  private ArenaCategory category;

  public PanelArenaCategory(User user, ArenaCategory arenaCategory) {
    super(user);
    try {
      initGUI();
      ihmInit(arenaCategory);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  private void initGUI() {
    try {
      {
        this.setSize(new java.awt.Dimension(438, 344));
        this.setLayout(null);
        this.setPreferredSize(new java.awt.Dimension(438, 344));
        {
          lblCurrentCapacity = new JIhmLabel();
          this.add(lblCurrentCapacity);
          lblCurrentCapacity.setText("Current capacity :");
          lblCurrentCapacity.setBounds(22, 12, 425, 24);
          lblCurrentCapacity.setFont(new java.awt.Font("Dialog", 1, 14));
        }
        {
          pnlIncreaseCap = new JIhmPanel();
          this.add(pnlIncreaseCap);
          pnlIncreaseCap.setBounds(17, 45, 404, 60);
          pnlIncreaseCap.setLayout(null);
          pnlIncreaseCap.setBorder(new LineBorder(new java.awt.Color(0, 0, 0), 1, false));
          {
            txtNewCapacity = new JIhmTextField();
            pnlIncreaseCap.add(txtNewCapacity);
            txtNewCapacity.setBounds(22, 18, 121, 30);
          }
          {
            btnIncreaseCapacity = new JIhmButton();
            pnlIncreaseCap.add(btnIncreaseCapacity);
            btnIncreaseCapacity.setText("Increase capacity");
            btnIncreaseCapacity.setBounds(217, 18, 165, 30);
            btnIncreaseCapacity.setMsgKey("arena.increasecapacity");
            btnIncreaseCapacity.addActionListener(new ActionListener() {
              public void actionPerformed(ActionEvent evt) {
                btnIncreaseCapacityActionPerformed(evt);
              }        

            });
          }
        }
        {
          pnlCondition = new JIhmPanel();
          this.add(pnlCondition);
          pnlCondition.setBounds(17, 187, 404, 60);
          pnlCondition.setLayout(null);
          pnlCondition.setBorder(new LineBorder(new java.awt.Color(0, 0, 0), 1, false));
          {
            btnMaintain = new JIhmButton();
            pnlCondition.add(btnMaintain);
            btnMaintain.setText("Maintain Category");
            btnMaintain.setBounds(217, 18, 165, 30);
            btnMaintain.setMsgKey("arena.maintain");
            btnMaintain.addActionListener(new ActionListener() {
              public void actionPerformed(ActionEvent evt) {
                btnMaintainActionPerformed(evt);
              }
            });
          }
          {
            lblCondition = new JIhmLabel();
            pnlCondition.add(lblCondition);
            lblCondition.setText("lblCondition");
            lblCondition.setBounds(24, 17, 110, 30);
          }
        }
        {
          pnlComfort = new JIhmPanel();
          this.add(pnlComfort);
          pnlComfort.setBounds(17, 115, 404, 60);
          pnlComfort.setLayout(null);
          pnlComfort.setBorder(new LineBorder(new java.awt.Color(0, 0, 0), 1, false));
          {
            btnComfort = new JIhmButton();
            pnlComfort.add(btnComfort);
            btnComfort.setText("Improve Comfort");
            btnComfort.setBounds(217, 16, 165, 30);
            btnComfort.setMsgKey("arena.improvecomfort");
            btnComfort.addActionListener(new ActionListener() {
              public void actionPerformed(ActionEvent evt) {
                btnComfortActionPerformed(evt);
              }
            });
          }
          {
            ComboBoxModel jIhmComboBox1Model = new DefaultComboBoxModel(new String[] { "Cheap", "Normal", "Deluxe" });
            cbComfort = new JIhmComboBox();
            pnlComfort.add(cbComfort);
            cbComfort.setModel(jIhmComboBox1Model);
            cbComfort.setBounds(24, 19, 163, 24);
          }
        }
        {
          btnClose = new JIhmButton();
          this.add(btnClose);
          btnClose.setText("Close");
          btnClose.setBounds(169, 257, 112, 30);
          btnClose.setMsgKey("ihm.close");
          btnClose.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
              btnCloseActionPerformed(evt);
            }
          });
        }
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  private void ihmInit(ArenaCategory pcategory) {

    this.category = pcategory;

    
    ComboBoxModel comfModel = new DefaultComboBoxModel(ArenaTools.getCagegoryComforts());
    cbComfort.setModel(comfModel);

    updateData();
    
  }

  public void updateData() {
    if (category.isBuilt()) {
      this.lblCurrentCapacity.setText(ArenaTools.getCategoryCapacityLabel(category));
      this.lblCondition.setText(ArenaTools.getCategoryCondition(category));
      
      if (category.getCategoryComfort() == ArenaCategory.COMFORT_CHEAP) {
        cbComfort.setSelectedIndex(0);
      } else if (category.getCategoryComfort() == ArenaCategory.COMFORT_NORMAL) {
        cbComfort.setSelectedIndex(1);
      } else {
        cbComfort.setSelectedIndex(2);
      }

      this.btnMaintain.setEnabled(true);
      this.btnComfort.setEnabled(true);
      this.cbComfort.setEnabled(true);

      
    } else {
      this.lblCurrentCapacity.setText(ArenaTools.getCategoryCapacityLabel(category));
      this.lblCondition.setText(ArenaTools.getCategoryCondition(category));
      this.btnMaintain.setEnabled(false);
      this.btnComfort.setEnabled(false);
      this.cbComfort.setEnabled(false);
    }

  }
  
  private void btnComfortActionPerformed(ActionEvent evt) {
    try {
      int newComfort = 0;
      
      if (cbComfort.getSelectedIndex() == 0) {
        newComfort = ArenaCategory.COMFORT_CHEAP;
      } else if (cbComfort.getSelectedIndex() == 1) {
        newComfort = ArenaCategory.COMFORT_NORMAL;
      } else {
        newComfort = ArenaCategory.COMFORT_DELUXE;
      }
      
      int price = this.category.calculatePriceForComfortIncrease(category.getCapacity(), newComfort);

      String msg = ClientController.getInstance().getTranslation("arena.increaseComfortCosts");
      msg = Tools.replaceAllSubString(msg, "%0%", ArenaTools.getCategoryComfort(newComfort));
      msg = Tools.replaceAllSubString(msg, "%1%", Tools.intToString(price));

      if (ClientController.getInstance().getDesktop().showYesNoMessage(msg)) {
        category.increaseComfort(newComfort);
        btnCloseActionPerformed(null);
        this.updateData();
      } 
    } catch (Exception err) {
      // TODO Exception Handling
      System.out.println(err);
    }
  }

  private void btnCloseActionPerformed(ActionEvent evt) {
    ClientController.getInstance().getDesktop().closeDialog();
  }

  private void btnIncreaseCapacityActionPerformed(ActionEvent evt) {
    try {
      int comfort = category.getCategoryComfort();
      int prize = category.calculatePriceForSeatsIncrease(Tools.stringToInt(this.txtNewCapacity.getText()), comfort);
      int newSize = this.category.getCapacity() + Tools.stringToInt(this.txtNewCapacity.getText());

      String msg = ClientController.getInstance().getTranslation("arena.increaseSeatsCosts");
      msg = Tools.replaceAllSubString(msg, "%0%", Tools.intToString(category.getCapacity()));
      msg = Tools.replaceAllSubString(msg, "%1%", Tools.intToString(newSize));
      msg = Tools.replaceAllSubString(msg, "%2%", ArenaTools.getCategoryComfort(comfort));
      msg = Tools.replaceAllSubString(msg, "%3%", Tools.intToString(prize));

      if (ClientController.getInstance().getDesktop().showYesNoMessage(msg)) {
        category.increaseCapacity(Tools.stringToInt(this.txtNewCapacity.getText()));
        btnCloseActionPerformed(null);
        this.updateData();
      } 
    } catch (Exception err) {
      // TODO Exception Handling
      System.out.println(err);
    }
  }
  
  private void btnMaintainActionPerformed(ActionEvent evt) {
    try {
      int prize = category.calculatePriceForMaintenance();
     
      String msg = ClientController.getInstance().getTranslation("arena.categoryMaintenance");
      msg = Tools.replaceAllSubString(msg, "%0%", Tools.intToString(prize));

      if (ClientController.getInstance().getDesktop().showYesNoMessage(msg)) {
        category.maintain();
        btnCloseActionPerformed(null);
        this.updateData();
      } 
    } catch (Exception err) {
      // TODO Exception Handling
      System.out.println(err);
    }
  }

}
