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

import java.awt.BorderLayout;
import javax.swing.JFrame;

/**
 * JIhmDialog is a simple JDialog class to show panels as dialog.
 * 
 * @author Bernhard von Gunten
 * @created January, 2005
 */
public class JIhmDialog extends javax.swing.JDialog implements JIhmComponent {

  static final long serialVersionUID = -3366410381438975171L;

  public JIhmDialog(JFrame frame, JIhmPanel panelToShow) {
    super(frame);
    initGUI();
    initIhm(panelToShow);
  }

  private void initGUI() {
    try {
      setSize(400, 300);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public void initIhm(JIhmPanel panelToShow) {
    this.getContentPane().add(panelToShow, BorderLayout.CENTER);
    this.setSize(panelToShow.getSize());
  }

  public void updateTranslation() {
  }
}