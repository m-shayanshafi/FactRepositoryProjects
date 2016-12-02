/*
 * ServerListEntry.java
 *
 * Created on February 7, 2007, 7:57 PM
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
 */

package netbattle.database;

/**
 *
 * @author Colin
 */
public class ServerListEntry implements Comparable {
    private String m_name, m_desc, m_host;
    private int m_port, m_max;
    /*package*/ int m_users;
    
    public ServerListEntry(String name, String description, String host, int port, int users, int max) {
        m_name = name;
        m_desc = description;
        m_port = port;
        m_host = host;
        m_users = users;
        m_max = max;
    }
    
    public int compareTo(Object o2) {
        ServerListEntry s2 = (ServerListEntry)o2;
        if (s2 == null)
            return -1;
        if (m_users < s2.m_users)
            return 1;
        if (m_users == s2.m_users)
            return 0;
        return -1;
    }
    
    public String toString() {
        return m_name
            + " ["
            + m_users
            + "/"
            + m_max
            + "]"
            + " ("
            + m_host
            + ":"
            + m_port
            + ")";
    }
    
    public void setName(String s) {
        m_name = s;
    }
    
    public String getName() {
        return m_name;
    }
    
    public String getDescription() {
        return m_desc;
    }
    
    public String getHost() {
        return m_host;
    }
    
    public int getPort() {
        return m_port;
    }
    
    public int getUsers() {
        return m_users;
    }
    
    public int getMaxUsers() {
        return m_max;
    }
}
