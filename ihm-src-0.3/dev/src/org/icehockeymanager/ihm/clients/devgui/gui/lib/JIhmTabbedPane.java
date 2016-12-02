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
  
package org.icehockeymanager.ihm.clients.devgui.gui.lib;

import java.awt.*;
import javax.swing.*;
import org.icehockeymanager.ihm.game.user.*;

/**
 * JIhmTabbedPane is a simple JTabbedPane class with a msgKey string for
 * automatic translations update. Implements JIhmComponens.
 * 
 * @author Bernhard von Gunten
 * @created January, 2005
 */
public class JIhmTabbedPane extends JTabbedPane implements JIhmComponent {
  static final long serialVersionUID = 5029238090952437569L;

  private User owner = null;

  private String titleKey = null;

  /** Constructor for the JIhmPanel object */
  public JIhmTabbedPane() {
  }

  public JIhmTabbedPane(User owner) {
    this.owner = owner;
  }

  public User getOwner() {
    return owner;
  }

  public void updateTranslation() {
    Component[] allComp = this.getComponents();
    for (int i = 0; i < allComp.length; i++) {
      if (allComp[i] instanceof JIhmComponent) {
        JIhmComponent tmp = (JIhmComponent) allComp[i];
        tmp.updateTranslation();
      }
    }
  }

  public String getTitleKey() {
    return titleKey;
  }

  public void setTitleKey(String titleKey) {
    this.titleKey = titleKey;
  }

}
