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
  
package org.icehockeymanager.ihm.clients.devgui.controller.helper;

import java.awt.event.*;
import org.icehockeymanager.ihm.clients.devgui.gui.*;

/**
 * DesktopListenerNotify is a implemented ActionListener. Which is used by the
 * ClientController as listener to the desktop. The actionPerformed function
 * releases the listener from the desktop and notifies the ClientController whos
 * probably "waiting"
 * 
 * @author Bernhard von Gunten
 * @created January 13, 2002
 */
public class DesktopListenerNotify implements ActionListener {
  /** Ref. to the ClientController */
  private Object objRef = null;

  /** Ref. to the Desktop */
  private Desktop desktop = null;

  /**
   * Constructs the Listener
   * 
   * @param objRef
   * @param desktop
   */
  public DesktopListenerNotify(Object objRef, Desktop desktop) {
    this.objRef = objRef;
    this.desktop = desktop;
  }

  /**
   * Removes listener from desktop and notifies the ClientController.
   * @param ev 
   */
  public void actionPerformed(ActionEvent ev) {
    synchronized (objRef) {
      desktop.removeActionListener(this);
      objRef.notify();
    }
  }

}
