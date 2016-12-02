/*
 * ConnectionPool.java
 *
 * Created on January 1, 2007, 4:06 PM
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
import java.util.*;
import java.sql.*;

class PoolEntry {
    private boolean m_free = true;
    private Connection m_conn = null;
    
    public PoolEntry(Connection conn, boolean freedom) {
        m_free = freedom;
        m_conn = conn;
    }
    
    public boolean isFree() {
        return m_free;
    }
    
    public void setFreedom(boolean b) {
        m_free = b;
    }
    
    public Connection getConnection() {
        return m_conn;
    }
}

/**
 *
 * @author Colin
 */
public class ConnectionPool {
    
    private String m_url = null;
    private Set m_pool = null;
    
    /**
     * Creates a new ConnectionPool that connects to the given url.
     *
     * @param url the url to connection to
     * @param size the initial size of the pool
     * @throws SQLException if a connection could not be established
     */
    public ConnectionPool(String url, int size) throws SQLException {
        m_url = url;
        m_pool = Collections.synchronizedSet(new HashSet(size));
        for (int i = 0; i < size; ++i) {
            Connection c = DriverManager.getConnection(url);
            m_pool.add(new PoolEntry(c, true));
        }
    }
    
    /**
     * Close all of the Connections in this pool.
     */
    public void close() {
        synchronized (m_pool) {
            Iterator i = m_pool.iterator();
            while (i.hasNext()) {
                PoolEntry entry = (PoolEntry)i.next();
                try {
                    entry.getConnection().close();
                } catch (SQLException e) {

                }
            }
        }
    }
    
    /**
     * Finalise this pool.
     */
    protected void finalize() {
        close();
    }
    
    /**
     * Get a Connection object for exclusive use. In order to allow other
     * objects to use this Connection, the caller should return to it the
     * pool by calling ConnectionPool.returnConnection().
     */
    public Connection getConnection() {
        synchronized (m_pool) {
            Iterator i = m_pool.iterator();
            while (i.hasNext()) {
                PoolEntry entry = (PoolEntry)i.next();
                if (!entry.isFree()) {
                    continue;
                }
                entry.setFreedom(false);
                Connection c = entry.getConnection();
                try {
                    if (c.isClosed()) {
                        i.remove();
                    } else {
                        return c;
                    }
                } catch (SQLException e) {
                    i.remove();
                }
            } // while (i.hasNext())
        }
        
        // The pool is all used up, so we have to create a new entry.
        try {
            Connection c = DriverManager.getConnection(m_url);
            m_pool.add(new PoolEntry(c, false));
            return c;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }
    
    /**
     * Return a Connection object that was obtained with getConnection() to
     * the pool.
     */
    public void returnConnection(Connection conn) {
        synchronized (m_pool) {
            Iterator i = m_pool.iterator();
            while (i.hasNext()) {
                PoolEntry entry = (PoolEntry)i.next();
                if (entry.getConnection() == conn) {
                    entry.setFreedom(true);
                    return;
                }
            }
        }
    }
    
}
