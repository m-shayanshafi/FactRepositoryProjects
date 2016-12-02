/*
 * Project: JDiveLog: A Dive Logbook written in Java
 * File: Messages.java
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
package net.sf.jdivelog.gui.resources;

import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

/**
 * Description: class in order to display messages, mainly when exceptions are raised
 * 
 * @author Pascal Pellmont <jdivelog@pellmont.dyndns.org>
 */
public class Messages {
    private static final String BUNDLE_NAME = "net.sf.jdivelog.gui.resources.messages";//$NON-NLS-1$

    private static ResourceBundle resourceBundle = ResourceBundle.getBundle(BUNDLE_NAME);

    public static String getString(String key) {
        try {
            return resourceBundle.getString(key);
        } catch (MissingResourceException e) {
            return '!' + key + '!';
        }
    }
    
    public static void setLocale(Locale newLocale) {
        Locale.setDefault(newLocale);
        resourceBundle = ResourceBundle.getBundle(BUNDLE_NAME, newLocale);
    }
    
    public static Locale getLocale() {
        return resourceBundle.getLocale();
    }
}