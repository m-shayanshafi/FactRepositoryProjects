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
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;

import org.icehockeymanager.ihm.clients.devgui.controller.*;
import org.icehockeymanager.ihm.clients.devgui.gui.lib.*;
import org.icehockeymanager.ihm.clients.devgui.ihm.infrastructure.arena.*;
import org.icehockeymanager.ihm.game.infrastructure.arena.*;
import org.icehockeymanager.ihm.game.team.*;
import org.icehockeymanager.ihm.lib.*;

public class PanelArena extends JIhmPanel {

  static final long serialVersionUID = -6339301419498766861L;
  private JIhmLabel arenaImage;
  private ButtonCategory btnCategoryB;
  private ButtonCategory btnCategoryC;
  private ButtonCategory btnCategoryC1;
  private ButtonCategory btnCategoryD1;
  private ButtonCategory btnCategoryB1;
  private ButtonCategory btnCategoryA1;
  private ButtonCategory btnCategoryD;
  private ButtonCategory btnCategoryA;
  private JIhmLabel lblTotalCapacity;
  private JIhmButton btnMaintainAll;
  private JIhmLabel lblCategoriesOverview;
  private JIhmList categoryList;

  /** Team that is shown in this frame */
  private Team teamToShow = null;

  ImageIcon bookingsIcon = new ImageIcon();

  public PanelArena(Team teamToShow) {
    try {
      initGUI();
      ihmInit(teamToShow);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  private void initGUI() throws Exception {
    this.setLayout(null);
    this.setSize(730, 500);
    this.setPreferredSize(new java.awt.Dimension(822, 500));
    {
      lblTotalCapacity = new JIhmLabel();
      this.add(lblTotalCapacity);
      lblTotalCapacity.setText("Total capacity:");
      lblTotalCapacity.setBounds(536, 30, 229, 30);
    }
    {
      lblCategoriesOverview = new JIhmLabel();
      this.add(lblCategoriesOverview);
      lblCategoriesOverview.setText("Categories / Capacity / Condition");
      lblCategoriesOverview.setBounds(535, 70, 249, 30);
      lblCategoriesOverview.setMsgKey("arena.categoriesoverview");
    }
    {
      arenaImage = new JIhmLabel();
      this.add(arenaImage);

      
        // While working in jigloo use following line to get the image while designing screen and remark the "getGuiResource" line
        // arenaImage.setIcon(new ImageIcon(getClass().getClassLoader().getResource("org/icehockeymanager/ihm/clients/devgui/gui/arena/ArenaByCat.png")));
         arenaImage.setIcon(new ImageIcon(ClientController.getInstance().getGuiResource("ArenaByCat.png")));
        
        arenaImage.setHorizontalAlignment(SwingConstants.LEFT);
        arenaImage.setHorizontalTextPosition(SwingConstants.LEFT);
        arenaImage.setVerticalAlignment(SwingConstants.TOP);
        arenaImage.setVerticalTextPosition(SwingConstants.TOP);
        arenaImage.setBounds(0, 0, 520, 434);
        {
          btnCategoryA = new ButtonCategory();
          arenaImage.add(btnCategoryA);
          btnCategoryA.setText("A");
          btnCategoryA.setBounds(233, 281, 45, 30);
        }
        {
          btnCategoryB = new ButtonCategory();
          arenaImage.add(btnCategoryB);
          btnCategoryB.setText("B");
          btnCategoryB.setBounds(91, 200, 45, 30);
        }
        {
          btnCategoryC = new ButtonCategory();
          arenaImage.add(btnCategoryC);
          btnCategoryC.setText("C");
          btnCategoryC.setBounds(233, 117, 45, 30);
        }
        {
          btnCategoryD = new ButtonCategory();
          arenaImage.add(btnCategoryD);
          btnCategoryD.setText("D");
          btnCategoryD.setBounds(375, 200, 45, 30);
        }
        {
          btnCategoryA1 = new ButtonCategory();
          arenaImage.add(btnCategoryA1);
          btnCategoryA1.setText("A1");
          btnCategoryA1.setBounds(226, 342, 60, 30);
        }
        {
          btnCategoryC1 = new ButtonCategory();
          arenaImage.add(btnCategoryC1);
          btnCategoryC1.setText("C1");
          btnCategoryC1.setBounds(226, 60, 60, 30);
        }
        {
          btnCategoryB1 = new ButtonCategory();
          arenaImage.add(btnCategoryB1);
          btnCategoryB1.setText("B1");
          btnCategoryB1.setBounds(26, 200, 60, 30);
        }
        {
          btnCategoryD1 = new ButtonCategory();
          arenaImage.add(btnCategoryD1);
          btnCategoryD1.setText("D1");
          btnCategoryD1.setBounds(434, 200, 60, 30);
        }

    }
    {
      ListModel categoryListModel = new DefaultComboBoxModel(new String[] { "Item One", "Item Two" });
      {
        btnMaintainAll = new JIhmButton();
        this.add(btnMaintainAll);
        btnMaintainAll.setText("Maintain all");
        btnMaintainAll.setBounds(561, 319, 195, 30);
        btnMaintainAll.setMsgKey("arena.maintainall");
        btnMaintainAll.addActionListener(new ActionListener() {
          public void actionPerformed(ActionEvent evt) {
            btnMaintainAllActionPerformed(evt);
          }
        });
      }
      categoryList = new JIhmList();
      this.add(categoryList);
      categoryList.setModel(categoryListModel);
      categoryList.setBounds(532, 108, 260, 198);
      categoryList.setFont(new java.awt.Font("Monospaced",1,12));
    }

  }

  /**
   * Create the panelLeagueStandings and add it to the frame
   * 
   * @param teamToshow
   *          Team to show in this frame
   */
  private void ihmInit(Team teamToshow) {
    this.setTitleKey("title.arena");

    this.teamToShow = teamToshow;
    displayArena();
  }

  public void updateData() {
    super.updateData();
    displayArena();
  }
  
  private void displayArena() {
    // Total capacity
    this.lblTotalCapacity.setText(ArenaTools.getTotalCapacityLabel(teamToShow.getInfrastructure().getArena()));
    
    
    // Fill category lsit
    ArenaCategory[] categories = teamToShow.getInfrastructure().getArena().getArenaCategories();
    String[] items = new String[categories.length];
    for (int i = 0; i < categories.length; i ++) {
      if (categories[i].isBuilt()) {
        items[i] = Tools.rPad(categories[i].getCategoryTitle(), 5) + " " + Tools.rPad(String.valueOf(categories[i].getCapacity()), 6) + " " + categories[i].getCondition() + "%" + " " + Tools.lPad(ArenaTools.getCategoryComfort(categories[i].getCategoryComfort()), 10);
      } else {
        items[i] = Tools.rPad(categories[i].getCategoryTitle(), 5);
      }
      
    }
    categoryList.setModel(new DefaultComboBoxModel(items));
    categoryList.validate();
    categoryList.repaint();
    
   // Init Buttons
    btnCategoryA.setArenaCategory(categories[0]);
    btnCategoryB.setArenaCategory(categories[1]);
    btnCategoryC.setArenaCategory(categories[2]);
    btnCategoryD.setArenaCategory(categories[3]);
    btnCategoryA1.setArenaCategory(categories[4]);
    btnCategoryB1.setArenaCategory(categories[5]);
    btnCategoryC1.setArenaCategory(categories[6]);
    btnCategoryD1.setArenaCategory(categories[7]);

  }
  
  private void btnMaintainAllActionPerformed(ActionEvent evt) {
    try {
      int prize = teamToShow.getInfrastructure().getArena().calculatePriceForMaintenance();
     
      String msg = ClientController.getInstance().getTranslation("arena.maintenace");
      msg = Tools.replaceAllSubString(msg, "%0%", Tools.intToString(prize));

      if (ClientController.getInstance().getDesktop().showYesNoMessage(msg)) {
        teamToShow.getInfrastructure().getArena().maintain();
        displayArena();
      } 
    } catch (Exception err) {
      // TODO Exception Handling
      System.out.println(err);
    }
  }

}
