/*
 * DatabaseImpl.java
 *
 * Created on March 27, 2007, 1:47 PM
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

package netbattle.database.registry;
import java.sql.*;
import netbattle.database.*;
import netbattle.messages.UserTableMessage;
import java.util.*;

/**
 * This class implements an AccountRegistry using a relational database
 * (MySQL by default). This is the preferred implementation to use, but if
 * a database is unavailable, a file-based implementation is also available.
 *
 * @see FileImpl
 * @author Colin
 */
public class DatabaseImpl extends AccountRegistry {
    
    /**
     * This object is our connection to the account registry.
     */
    private ConnectionPool m_pool = null;
    
    /**
     * Determine whether are are connected and if the connection is active.
     * If the connection is inactive then we will attempt to reopen it
     * because it probably closed due to inactivity.
     */
    public boolean isConnected() {
        return (m_pool != null);
    }
    
    /**
     * Connect to a MySql database on this server. The user running the
     * Shoddy Battle server must have sufficient privileges to perform
     * the SQL executed in the AccountRegistry class.
     */
    public boolean connect(String host, String database, String user, String password) {
        try {
            if (isConnected()) {
                m_pool.close();
            }
            
            String url = "jdbc:mysql://"
                    + host
                    + "/"
                    + database
                    + "?user=" + user
                    + "&password=" + password;
            m_pool = new ConnectionPool(url, 15);
        } catch (SQLException ex) {
            System.out.println("SQLException: " + ex.getMessage());
            System.out.println("SQLState: " + ex.getSQLState());
            System.out.println("VendorError: " + ex.getErrorCode());
            return false;
        }
        return true;
    }
    
    /**
     * Close this AccountRegistry.
     */
    public void close() {
        if (isConnected()) {
            m_pool.close();
        }
    }
    
    /**
     * Get the SHA-1 hash of a user's password.
     * @return the hash or null if the user does not exist
     */
    public String getPassword(String user) {
        if (!isConnected()) return null;
        
        final String text = "SELECT password FROM users WHERE name = ?";
        
        String ret = null;
        Connection conn = m_pool.getConnection();
        try {
            PreparedStatement stmt = conn.prepareStatement(text);
            stmt.setString(1, user);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                ret = rs.getString(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            m_pool.returnConnection(conn);
        }
        return ret;
    }
    
    /**
     * Get a list of all users in this registry.
     */
    public UserTableMessage getUserList() {
        if (!isConnected()) return null;
        
        final String text = "SELECT name, ip, level, unban FROM users";
        
        Connection conn = m_pool.getConnection();
        
        ArrayList user = new ArrayList();
        ArrayList ip = new ArrayList();
        ArrayList level = new ArrayList();
        ArrayList date = new ArrayList();
        
        try {
            PreparedStatement stmt = conn.prepareStatement(text);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                user.add(rs.getString(1));
                ip.add(rs.getString(2));
                level.add(new Integer(rs.getInt(3)));
                date.add(new Long(rs.getLong(4)));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            m_pool.returnConnection(conn);
        }
        
        String[] users = (String[])user.toArray(new String[user.size()]);
        String[] ips = (String[])ip.toArray(new String[ip.size()]);
        int levels[] = new int[level.size()];
        for (int i = 0; i < levels.length; ++i) {
            levels[i] = ((Integer)level.get(i)).intValue();
        }
        long dates[] = new long[date.size()];
        for (int i = 0; i < dates.length; ++i) {
            dates[i] = ((Long)date.get(i)).longValue();
        }
        
        return new UserTableMessage(users, ips, dates, levels);
    }
    
    /**
     * Add a new user to the database.
     *
     * @param user the user name of the user
     * @param password an SHA-1 hash of the user's password
     * @return whether the user was added successfully
     */
    public boolean addNewUser(String user, String password) {
        if (!isConnected()) {
            return false;
        }
        
        user = user.trim();
        if (getPassword(user) != null) {
            // This user name is already taken.
            return false;
        }
        
        boolean ret = false;
        Connection conn = m_pool.getConnection();
        
        try {
            final String text = "INSERT INTO users VALUES (0, ?, ?, 0, 0, 0)";
            PreparedStatement stmt = conn.prepareStatement(text);
            stmt.setString(1, user);
            stmt.setString(2, password);
            stmt.execute();
            ret = true;
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            m_pool.returnConnection(conn);
        }
        return ret;
    }
    
    /**
     * Determine when a user becomes unbanned.
     * @return a UNIX timestamp of the unban date
     */
    public long getUserUnbanDate(String user) {
        if (!isConnected()) return 0;
        
        final String text = "SELECT unban FROM users WHERE name = ?";
        
        long ret = 0;
        Connection conn = m_pool.getConnection();
        try {
            PreparedStatement stmt = conn.prepareStatement(text);
            stmt.setString(1, user);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                ret = rs.getLong(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            m_pool.returnConnection(conn);
        }
        return ret;
    }
    
    /**
     * Return the date upon which an IP address becomes unbanned.
     */
    public long getIpUnbanDate(String ip) {
        if (!isConnected()) return 0;
        
        final String text = "SELECT unban FROM users WHERE ip = ?";
        
        long ret = 0;
        Connection conn = m_pool.getConnection();
        try {
            PreparedStatement stmt = conn.prepareStatement(text);
            stmt.setString(1, ip);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                long ban = rs.getLong(1);
                if (ban > ret) {
                    ret = ban;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            m_pool.returnConnection(conn);
        }
        return ret;
    }
    
    /**
     * Set the date upon which a user becomes unbanned.
     */
    public boolean banUser(String user, long date) {
        if (!isConnected()) return false;
        
        final String text = "UPDATE users SET unban = ? WHERE name = ?";
        
        Connection conn = m_pool.getConnection();
        try {
            PreparedStatement stmt = conn.prepareStatement(text);
            stmt.setLong(1, date);
            stmt.setString(2, user);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } finally {
            m_pool.returnConnection(conn);
        }
        return true;
    }
    
    /**
     * Set the date upon which a user becomes unbanned.
     */
    public boolean banUser(String user, String ip, long date) {
        if (!isConnected()) return false;
        
        final String text = "UPDATE users SET unban = ?, ip = ? WHERE name = ?";
        
        Connection conn = m_pool.getConnection();
        try {
            PreparedStatement stmt = conn.prepareStatement(text);
            stmt.setLong(1, date);
            stmt.setString(2, ip);
            stmt.setString(3, user);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } finally {
            m_pool.returnConnection(conn);
        }
        return true;
    }
    
    /**
     * Get the level of a user.
     */
    public int getUserLevel(String user) {
        if (!isConnected()) return 0;
        
        final String text = "SELECT level FROM users WHERE name = ?";
        
        int ret = 0;
        Connection conn = m_pool.getConnection();
        try {
            PreparedStatement stmt = conn.prepareStatement(text);
            stmt.setString(1, user);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            m_pool.returnConnection(conn);
        }
        return ret;
    }
    
    /**
     * Get the IP Address of a particular user.
     */
    public String getIpAddress(String user) {
        if (!isConnected()) return null;
        
        final String text = "SELECT ip FROM users WHERE name = ?";
        
        Connection conn = m_pool.getConnection();
        try {
            PreparedStatement stmt = conn.prepareStatement(text);
            stmt.setString(1, user);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getString(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            m_pool.returnConnection(conn);
        }
        return null;
    }   
    
    /**
     * Get a List of users with a given IP Address.
     */
    public List getUsersByIp(String ip) {
        if (!isConnected()) return null;
        
        final String text = "SELECT name FROM users WHERE ip = ?";
        
        Connection conn = m_pool.getConnection();
        try {
            PreparedStatement stmt = conn.prepareStatement(text);
            stmt.setString(1, ip);
            ResultSet rs = stmt.executeQuery();
            List ret = new ArrayList();
            while (rs.next()) {
                ret.add(rs.getString(1));
            }
            return ret;
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            m_pool.returnConnection(conn);
        }
        return null;
    }
    
    /**
     * Set the level of a user.
     */
    public boolean setUserLevel(String user, int level) {
        if (!isConnected()) return false;
        
        final String text = "UPDATE users SET level = ? WHERE name = ?";
        
        Connection conn = m_pool.getConnection();
        try {
            PreparedStatement stmt = conn.prepareStatement(text);
            stmt.setInt(1, level);
            stmt.setString(2, user);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } finally {
            m_pool.returnConnection(conn);
        }
        return true;
    }
    
    /**
     * Initialise the MySql JDBC driver.
     */
    static {
        try {
            Class.forName("com.mysql.jdbc.Driver").newInstance();
        } catch (Exception e) {
            System.out.println("Cannot find mysql driver.");
        }
    }
    
}
