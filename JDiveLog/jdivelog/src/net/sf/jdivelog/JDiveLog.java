/*
 * Project: JDiveLog: A Dive Logbook written in Java
 * File: JDiveLog.java
 * 
 * @author Pascal Pellmont <jdivelog@pellmont.dyndns.org>
 * 
 * This file is part of JDiveLog.
 * JDiveLog is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.

 * JDiveLog is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.

 * You should have received a copy of the GNU General Public License
 * along with JDiveLog; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 */
package net.sf.jdivelog;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Locale;
import java.util.Properties;
import java.util.StringTokenizer;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import net.sf.jdivelog.gui.FileChooser;
import net.sf.jdivelog.gui.MainWindow;
import net.sf.jdivelog.gui.resources.Messages;

/**
 * Description: Load of the different parameters and properties.
 * Once it is done, load the software's main window
 * 
 * @author Pascal Pellmont <jdivelog@pellmont.dyndns.org>
 */
public class JDiveLog {
    
    private static final Logger LOGGER = Logger.getLogger(JDiveLog.class.getName());
    
    public static final File CONFIG_FILE = new File(System.getProperty("user.home"), ".jdivelog");
    public static final String PROPERTY_LOCALE = "locale";
    public static final String PROPERTY_SYSTEM = "system";
    public static final String PROPERTY_WINDOW_X = "window_x";
    public static final String PROPERTY_WINDOW_Y = "window_y";
    public static final String PROPERTY_WINDOW_WIDTH = "window_width";
    public static final String PROPERTY_WINDOW_HEIGHT = "window_height";
    public static final String PROPERTY_LASTFILE = "last_file";
    public static final String PROPERTY_DATEFORMAT = "dateformat";
    public static final String PROPERTY_DATETIMEFORMAT = "datetime_format";
    public static final String PROPERTY_WORKING_DIRECTORY = "working_directory";
    public static final String PROPERTY_SYSTEM_LOOK = "system_look";
    public static final String PROPERTY_LOOK_AND_FEEL = "look_and_feel";
    public static final String PROPERTY_PREVIEW = "preview";
    public static final String PROPERTY_STARTSORT = "startsort";
    public static final String PROPERTY_DOCUMENTS_PATH = "documents_path";
    
    
    /**
     * main method
     * @param args
     */
    public static void main(final String[] args) {
        if (CONFIG_FILE.exists() && CONFIG_FILE.canRead()) {            
            Properties p = new Properties();
            try {
                p.load(new FileInputStream(CONFIG_FILE));
                String locale = p.getProperty(PROPERTY_LOCALE);
                String system = p.getProperty(PROPERTY_SYSTEM);
                String dateformat = p.getProperty(PROPERTY_DATEFORMAT);
                String datetimeformat = p.getProperty(PROPERTY_DATETIMEFORMAT);
                String workingDirectory = p.getProperty(PROPERTY_WORKING_DIRECTORY);
                String systemLook = p.getProperty(PROPERTY_SYSTEM_LOOK);
                String lookAndFeel = p.getProperty(PROPERTY_LOOK_AND_FEEL);
                String preview = p.getProperty(PROPERTY_PREVIEW);
                String startsort = p.getProperty(PROPERTY_STARTSORT);
                String documents_path = p.getProperty(PROPERTY_DOCUMENTS_PATH);
                if (locale != null) {
                    String lang;
                    String country = "";
                    String variant = "";
                    StringTokenizer st = new StringTokenizer(locale, "_");
                    lang = st.nextToken();
                    if (st.hasMoreTokens()) {
                        country = st.nextToken();
                        if (st.hasMoreTokens()) {
                            variant = st.nextToken();
                        }
                    }
                    Messages.setLocale(new Locale(lang, country, variant));
                }
                if (system != null) {
                    System.setProperty(PROPERTY_SYSTEM, system);
                }
                if (dateformat != null) {
                    System.setProperty(PROPERTY_DATEFORMAT, dateformat);
                }
                if (datetimeformat != null) {
                    System.setProperty(PROPERTY_DATETIMEFORMAT, datetimeformat);
                }
                if (workingDirectory != null) {
                    FileChooser.setWorkingDirectory(workingDirectory);
                }
                if (systemLook != null) {
                    System.setProperty(PROPERTY_SYSTEM_LOOK, systemLook);
                } else {
                    System.setProperty(PROPERTY_SYSTEM_LOOK, "true");
                }
                if (lookAndFeel != null) {
                    System.setProperty(PROPERTY_LOOK_AND_FEEL, lookAndFeel);
                }
                if (preview != null) {
                    System.setProperty(PROPERTY_PREVIEW, preview);
                }
                if (startsort != null) {
                    System.setProperty(PROPERTY_STARTSORT, startsort);
                }
                if (documents_path != null) {
                    System.setProperty(PROPERTY_DOCUMENTS_PATH, documents_path);
                }
                else {
                    System.setProperty(PROPERTY_DOCUMENTS_PATH, "");
                }
            } catch (FileNotFoundException e) {
                LOGGER.log(Level.SEVERE, "failed to load properties", e);
            } catch (IOException e) {
                LOGGER.log(Level.SEVERE, "failed to load properties", e);
            }
        }   
        
        // System look and feel
        /* Commented for release version */
        Boolean systemLook = Boolean.parseBoolean(System.getProperty(PROPERTY_SYSTEM_LOOK));
        String lookAndFeel = System.getProperty(PROPERTY_LOOK_AND_FEEL);
        if (systemLook || systemLook == null)
        {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (ClassNotFoundException ex) {
                ex.printStackTrace();
            } catch (InstantiationException ex) {
                ex.printStackTrace();
            } catch (IllegalAccessException ex) {
                ex.printStackTrace();
            } catch (UnsupportedLookAndFeelException ex) {
                ex.printStackTrace();
            }
        } else if (lookAndFeel != null) {
            try {
                UIManager.setLookAndFeel(lookAndFeel);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (UnsupportedLookAndFeelException e) {
                e.printStackTrace();
            }
        }
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                MainWindow.main(args);
            }
        });
    }
}
