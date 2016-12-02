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
import java.net.*;
import java.util.*;

/**
 * Resources returns Translations, gui elements like images, html etc.
 * 
 * @author Bernhard von Gunten
 * @created January 13, 2002
 */
public class ClientResources {

  private static final String DEFAULT_SKIN = "ihm";

  private static final String TRANSLATION_DIR = "/translations";

  private static final String TRANSLATION_FILE_NAME = "translations";

  private static final String TRANSLATION_FILE_EXTENSION = ".properties";

  /** Translations */
  private Properties translations = null;

  /** Current language */
  private String language = null;

  /** Ihm root directory */
  private File ihmDirectory = null;

  /** User ihm directory */
  private File homeDirectory = null;

  /** Current (gui) skin */
  private String skin = null;

  /**
   * Constructs the Resources, loads translations ito cache.
   * 
   * @param ihmResDir
   *          The ihm application root directory
   * @param homeResDir
   *          The ihm user root directory
   * @param language
   *          The current language
   * @param skin
   *          The current gui skin
   * @exception Exception
   *              Exception
   */
  public ClientResources(File ihmResDir, File homeResDir, String language, String skin) throws Exception {
    try {

      if (skin == null || skin.equals("")) {
        this.skin = DEFAULT_SKIN;
      } else {
        this.skin = skin;
      }

      ihmDirectory = ihmResDir;
      homeDirectory = homeResDir;

      this.language = language;

      loadTranslations();

    } catch (Exception e) {
      throw e;
    }
  }

  /**
   * Sets language, and reloads translations
   * 
   * @param language
   *          The new language value
   * @exception Exception
   *              Description of the Exception
   */
  public void setLanguage(String language) throws Exception {
    try {
      this.language = language;
      loadTranslations();
    } catch (Exception e) {
      throw e;
    }
  }

  /**
   * Load translations
   * 
   * @exception Exception
   *              Description of the Exception
   */
  public void loadTranslations() throws Exception {

    // First in the ihmHome directory of the user
    File translationFile = new File(homeDirectory.getAbsolutePath() + TRANSLATION_DIR, TRANSLATION_FILE_NAME + "_" + language + TRANSLATION_FILE_EXTENSION);

    // Then in the ihmDirectory itself
    if (!translationFile.exists()) {
      translationFile = new File(ihmDirectory.getAbsolutePath() + TRANSLATION_DIR, TRANSLATION_FILE_NAME + "_" + language + TRANSLATION_FILE_EXTENSION);
    }

    // Then the standard translations
    if (!translationFile.exists()) {
      translationFile = new File(ihmDirectory.getAbsolutePath() + TRANSLATION_DIR, TRANSLATION_FILE_NAME + TRANSLATION_FILE_EXTENSION);
    }

    if (!translationFile.exists()) {
      throw new Exception("Translations file not found (" + translationFile.getAbsolutePath() + ")");
    }

    translations = new Properties();
    translations.load(new FileInputStream(translationFile));
  }

  /**
   * Returns the translations as a Properties class
   * 
   * @return The translationsProperties value
   */
  public Properties getTranslationsProperties() {
    return translations;
  }

  /**
   * Returns a gui resource (jpg, html etc.) for the current language and skin.
   * 
   * @param fileToLoad
   *          Description of the Parameter
   * @return The guiResource value
   * @exception Exception
   *              Description of the Exception
   */
  public URL getGuiResource(String fileToLoad) throws Exception {
    int lastDot = fileToLoad.lastIndexOf(".");
    String file = fileToLoad.substring(0, lastDot);
    String extension = fileToLoad.substring(lastDot);

    String guiDir = "/gui/" + skin + "/";
    String guiOriginalDir = "/gui/ihm/";

    // First in the ihmHome directory of the user
    File guiFile = new File(homeDirectory.getAbsolutePath() + guiDir, file + "_" + language + extension);

    // Then in the ihmDirectory itself
    if (!guiFile.exists()) {
      guiFile = new File(ihmDirectory.getAbsolutePath() + guiDir, file + "_" + language + extension);
    }

    // Then without translation
    if (!guiFile.exists()) {
      guiFile = new File(ihmDirectory.getAbsolutePath() + guiDir, file + extension);
    }

    // Then the standard directory with translation
    if (!guiFile.exists()) {
      guiFile = new File(ihmDirectory.getAbsolutePath() + guiOriginalDir, file + "_" + language + extension);
    }

    // Then the standard directory without translation
    if (!guiFile.exists()) {
      guiFile = new File(ihmDirectory.getAbsolutePath() + guiOriginalDir, file + extension);
    }

    if (!guiFile.exists()) {
      throw new Exception("GUI file not found (" + guiFile.getAbsolutePath() + ")");
    }

    return guiFile.toURL();
  }

}
