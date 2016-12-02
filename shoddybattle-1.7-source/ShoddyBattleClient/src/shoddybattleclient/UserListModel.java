/*
 * UserListModel.java
 *
 * Created on December 22, 2006, 10:33 AM
 *
 * This file is a part of Shoddy Battle.
 * Copyright (C) 2006  Colin Fitzpatrick
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
 * along with this program; if not, visit the Free Software Foundation, Inc.
 * online at http://gnu.org.
 */

package shoddybattleclient;
import java.util.*;
import javax.swing.*;
import javax.swing.event.*;

public class UserListModel implements ListModel {
    private ArrayList m_items, m_listeners;
    
    public UserListModel(ArrayList items) {
        m_items = items;
        m_listeners = new ArrayList();
    }
    
    public ArrayList getList() {
        return m_items;
    }
    
    public int getSize() {
        return m_items.size();
    }
    
    public Object getElementAt(int index) {
        try {
            return m_items.get(index);
        } catch (Exception e) {
            return null;
        }
    }
    
    public void addListDataListener(ListDataListener l) {
        m_listeners.add(l);
    }
    
    public void removeListDataListener(ListDataListener l) {
        m_listeners.remove(l);
    }
    
    public void add(Object user) {
        synchronized (m_items) {
            Iterator i = m_items.iterator();
            while (i.hasNext()) {
                Object item = (Object)i.next();
                if (item == null) {
                    continue;
                }
                if (item.equals(user)) {
                    return;
                }
            }
        }
        m_items.add(user);
    }
    
    public void remove(Object user) {
        m_items.remove(user);
    }
}
