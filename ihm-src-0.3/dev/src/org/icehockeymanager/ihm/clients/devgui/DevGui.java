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
  
package org.icehockeymanager.ihm.clients.devgui;

/**
 * Main class for Development Test Gui
 * 
 * @author Bernhard von Gunten / Arik Dasen
 * @created December, 2001
 */
public class DevGui {

  /**
   * Construct the application
   * 
   * @param args
   *          The command line arguments
   */
  public DevGui(String[] args) {
    org.icehockeymanager.ihm.clients.devgui.controller.ClientController.getInstance().init(args);
  }

  /**
   * Main method
   * 
   * @param args
   *          The command line arguments
   */
  public static void main(String[] args) {
    // Construct application
    new DevGui(args);
  }
}