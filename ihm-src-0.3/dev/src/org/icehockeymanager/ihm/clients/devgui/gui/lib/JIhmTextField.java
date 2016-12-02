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

import javax.swing.*;

/**
 * JIhmTextField is a simple JTextField class with a msgKey string for automatic
 * translations update. Implements JIhmComponens.
 * 
 * @author Bernhard von Gunten
 * @created January, 2005
 */
public class JIhmTextField extends JTextField implements JIhmComponent {

  static final long serialVersionUID = 6981372027630415887L;

  private String msgKey = null;

  public String getMsgKey() {
    return msgKey;
  }

  public void setMsgKey(String msgKey) {
    this.msgKey = msgKey;
  }

  public void updateTranslation() {
  }

}