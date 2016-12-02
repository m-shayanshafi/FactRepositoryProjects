/*
 * Project: JDiveLog: A Dive Logbook written in Java
 * File: ChartData.java
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
package net.sf.jdivelog.gui.statistics;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.TreeSet;

public class ChartData {
    
    private String name;
    private String catLabel;
    private String valLabel;
    private ArrayList<String> names;
    private HashMap<String, Integer> counter;
    
    public ChartData(String name, String catLabel, String valLabel) {
        this.name = name;
        this.catLabel = catLabel;
        this.valLabel = valLabel;
        names = new ArrayList<String>();
        counter = new HashMap<String, Integer>();
    }
    
    public String getName() {
        return name;
    }
    
    public String getCatLabel() {
        return catLabel;
    }
    
    public String getValLabel() {
        return valLabel;
    }
    
    public void add(String name) {
        if (name == null) {
            return;
        }
        Integer i = counter.get(name);
        if (i == null) {
            names.add(name);
            counter.put(name, 1);
        } else {
            counter.put(name, i+1);
        }
    }
    
    public void remove(String name) {
        counter.remove(name);
        names.remove(name);
    }
    
    public List<String> getNames() {
        return names;
    }
    
    public Integer getCount(String name) {
        return counter.get(name);
    }
    
    
    public void orderByNameAscending() {
        TreeSet<String> set = new TreeSet<String>();
        Iterator<String> it = names.iterator();
        while(it.hasNext()) {
            set.add(it.next());
        }
        it = set.iterator();
        names.clear();
        while (it.hasNext()) {
            names.add(it.next());
        }
    }
    
    public void orderByCountDescending() {
        Collections.sort(names, new NameByCountComparator());
    }
    
    /**
     * Removes all Entries after <code>maxElements</code>.
     * @param maxElements
     */
    public void firstElements(int maxElements) {
        ArrayList<String> toRemove = new ArrayList<String>();
        int count = 0;
        for (Iterator<String> it = names.iterator(); it.hasNext(); count++) {
            String n = it.next();
            if (count > maxElements) {
                toRemove.add(n);
            }
        }
        for (Iterator<String> it = toRemove.iterator(); it.hasNext(); ) {
            String n = it.next();
            remove(n);
        }
    }
    
    //
    // inner classes
    //
    
    private class NameByCountComparator implements Comparator<String> {

        public int compare(String name1, String name2) {
            return counter.get(name2) - counter.get(name1);
        }
        
    }

}
