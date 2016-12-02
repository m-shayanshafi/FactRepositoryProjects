/*
 * HoldItemData.java
 *
 * Created on May 19, 2007, 4:33 PM
 *
 * This file is a part of Shoddy Battle.
 * Copyright (C) 2007  Colin Fitzpatrick
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software Foundation, Inc.,
 * 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 * The Free Software Foundation may be visited online at http://www.fsf.org.
 */

package mechanics.statuses.items;
import java.util.*;
import java.io.*;

/**
 *
 * @author Colin
 */
public class HoldItemData {
    
    /*package*/ TreeSet m_items = new TreeSet();
    /*package*/ HashMap m_exclusives = new HashMap();
    
    /**
     * Return whether the named species can use a particular item.
     */
    public boolean canUseItem(String species, String item) {
        if (m_items.contains(item)) {
            return true;
        }
        Object o = m_exclusives.get(species);
        if (o == null) {
            return false;
        }
        return ((HashSet)o).contains(item);
    }
    
    /**
     * Get an item set corresponding to the named species.
     */
    public SortedSet getItemSet(String species) {
        Object o = m_exclusives.get(species);
        if (o == null) {
            return m_items;
        }
        SortedSet items = (SortedSet)m_items.clone();
        items.addAll((Collection)o);
        return items;
    }
    
    /**
     * Write item data to an arbitrary output stream.
     */
    public void saveItemData(OutputStream output) throws IOException {
        ObjectOutputStream stream = new ObjectOutputStream(output);
        stream.writeObject(m_items);
        stream.writeObject(m_exclusives);
        stream.flush();
    }
    
    /**
     * Read item data in from an arbitrary input stream.
     * To be only only by the client - does not initialise for battles!
     */
    public void loadItemData(InputStream input) throws IOException, FileNotFoundException {
        ObjectInputStream stream = new ObjectInputStream(input);
        try {
            m_items = (TreeSet)stream.readObject();
            m_exclusives = (HashMap)stream.readObject();
        } catch (ClassNotFoundException e) {
            
        }
    }
    
    /**
     *  Remove an exclusive item from a pokemon.
     */
    public void removeExclusiveItem(String name, String pokemon) {
        Object o = m_exclusives.get(pokemon);
        if (o == null) {
            return;
        }

        ((HashSet)o).remove(name);
    }
    
    /**
     * Add an exclusive item to a pokemon.
     */
    public void addExclusiveItem(String name, String pokemon) {
        Object o = m_exclusives.get(pokemon);
        if (o == null) {
            HashSet set = new HashSet();
            set.add(name);
            m_exclusives.put(pokemon, set);
        } else {
            ((HashSet)o).add(name);
        }
    }
}
