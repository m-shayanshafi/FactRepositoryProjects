/*
 * Project: JDiveLog: A Dive Logbook written in Java
 * File: DefaultDictionary.java
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

import java.util.HashSet;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;

/**
 * A default implementation of the autocomplete dictionary. This implementation
 * is based upon the TreeSet collection class to provide quick lookups and
 * default sorting.
 * 
 */
public class DefaultDictionary extends TreeMap<String,String> implements AutoCompleteDictionary {

    private static final long serialVersionUID = 1L;

    /**
     * Adds an entry to the dictionary.
     * 
     * @param s
     *            The string to add to the dictionary.
     */
    public void addEntry(String s) {
        super.put(s.toLowerCase(), s);
    }

    /**
     * Removes an entry from the dictionary.
     * 
     * @param s
     *            The string to remove to the dictionary.
     * @return True if successful, false if the string is not contained or
     *         cannot be removed.
     */
    public boolean removeEntry(String s) {
        return super.remove(s) != null;
    }

    /**
     * Perform a lookup. This routine returns the closest matching string that
     * completely contains the given string, or null if none is found.
     * 
     * @param curr
     *            The string to use as the base for the lookup.
     * @return curr The closest matching string that completely contains the
     *         given string.
     */
    public String lookup(String curr) {
        if ("".equals(curr)) {
            return null;
        }
        try {
            SortedMap<String, String> tailSet = this.tailMap(curr.toLowerCase());
            if (tailSet != null) {
                Object firstKey = tailSet.firstKey();
                if (firstKey != null) {
                    String first = this.get(firstKey);
                    if (first.toLowerCase().startsWith(curr.toLowerCase())) {
                        return first;
                    }
                }
            }
        } catch (Exception e) {
            return null;
        }
        return null;
    }
    
    public Set<String> getAll() {
        return new HashSet<String>(values());
    }
}