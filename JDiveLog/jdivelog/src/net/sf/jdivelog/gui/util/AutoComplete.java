/*
 * Project: JDiveLog: A Dive Logbook written in Java
 * File: AutoComplete.java
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
package net.sf.jdivelog.gui.util;

/**
 * Defines the API that autocomplete components must implement
 */
public interface AutoComplete {
    /**
     * Set the dictionary that autocomplete lookup should be performed by.
     * 
     * @param dict
     *            The dictionary that will be used for the autocomplete lookups.
     *            The dictionary implemetation should be very fast at lookups to
     *            avoid delays as the user types.
     */
    public void setDictionary(AutoCompleteDictionary dict);

    /**
     * Gets the dictionary currently used for lookups.
     * 
     * @return dict The dictionary that will be used for the autocomplete
     *         lookups. The dictionary implemetation should be very fast at
     *         lookups to avoid delays as the user types.
     */
    public AutoCompleteDictionary getDictionary();

    /**
     * Sets whether the component is currently performing autocomplete lookups
     * as keystrokes are performed.
     * 
     * @param val
     *            True or false.
     */
    public void setAutoComplete(boolean val);

    /**
     * Gets whether the component is currently performing autocomplete lookups
     * as keystrokes are performed.
     * 
     * @return True or false.
     */
    public boolean getAutoComplete();
}
