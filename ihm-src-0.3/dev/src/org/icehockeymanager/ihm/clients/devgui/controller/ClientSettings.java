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
  
package org.icehockeymanager.ihm.clients.devgui.controller;

import java.io.*;
import org.icehockeymanager.ihm.lib.*;

/**
 * ClientSettings contains all settings for the DevGui client
 * 
 * @author Bernhard von Gunten
 * @created January, 2005
 */
public class ClientSettings extends IhmSettingsXML {

  static final long serialVersionUID = -2677859126752507923L;

  public int DESKTOP_DELAY_MS = 300;

  public String DESKTOP_DELAY_MS_DOC = "Delay between desktop creation and first call";

  public String LANGUAGE = "en";

  public String LANGUAGE_DOC = "User interface language.";

  public String LOGLEVEL = "CONFIG";

  public String LOGLEVEL_DOC = "Client Loglevel.";

  public int MATCHCONTROLLER_DELAY = 100;

  public String MATCHCONTROLLER_DELAY_DOC = "Delay after every match has been played for one scene.";

  public int RADIO_DELAY = 200;

  public String RADIO_DELAY_DOC = "Delay after every situation the radio is showing on the gameday screen.";

  public ClientSettings() {
  }

  public ClientSettings(File fileToRead) throws Exception {
    this.readSettingsFromXml(fileToRead);
  }
}
