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

import org.icehockeymanager.ihm.clients.devgui.controller.*;
import org.icehockeymanager.ihm.clients.devgui.gui.lib.*;
import org.icehockeymanager.ihm.game.infrastructure.arena.*;

public class ButtonCategory extends JIhmButton {

  private static final long serialVersionUID = -5936852060324207498L;

  private ArenaCategory arenaCategory = null;
  
  
  public ButtonCategory() {
    initGUI();
  }  
  
  public ButtonCategory(ArenaCategory arenaCategory) {
    this();
    this.setArenaCategory(arenaCategory);
  }
  
  private void initGUI() {
    try {
      {
        this.addActionListener(new ActionListener() {
          public void actionPerformed(ActionEvent evt) {
            ClientController.getInstance().getDesktop().showFrameArenaCategory(arenaCategory);
          }
        });
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public void setArenaCategory(ArenaCategory arenaCategory) {
    this.arenaCategory = arenaCategory;
    this.setText(arenaCategory.getCategoryTitle());
  }
  
}
