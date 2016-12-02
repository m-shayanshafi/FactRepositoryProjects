/*
 * FileImpl.java
 *
 * Created on March 27, 2007, 1:54 PM
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
import java.util.*;
import java.io.*;
import netbattle.messages.UserTableMessage;

/**
 * This is an implementation of the AccountRegistry that uses only flat files.
 * The impl. works by internally storing the registry data as a HashMap. When
 * an instance is created, the map is unseralised from a file. When the
 * <code>close</code> method is called, the map is seralised to the same file.
 * To reduce susceptibility to data loss, the data is also serialised
 * periodically, as specified when the class is instantiated.
 *
 * @author Colin
 */
public class FileImpl extends AccountRegistry {
    
    /**
     * An entry in the flat file.
     */
    private static class UserEntry implements Serializable {
        public String password, ip;
        public long unban = 0;
        public int level = 0;
    }
    
    /**
     * File to serialise the registry to and from.
     */
    private File m_file;
    
    /**
     * Internel representation of the registry.
     */
    private Map m_map;
    
    /**
     * Time between each seralisation of the map in milliseconds.
     */
    private long m_delta;
    
    /**
     * Creates a new instance of FileImpl
     * @param delta the number of milliseconds between each backup of the map
     */
    public FileImpl(String file, long delta) {
        m_file = new File(file);
        m_delta = delta;
        unserialiseMap(m_file);
        new Thread(new BackupThread()).start();
    }
    
    /**
     * A thread that makes backups periodically.
     */
    private class BackupThread implements Runnable {
        public void run() {
            Object o = new Object();
            while (true) {
                serialiseMap(m_file);
                try {
                    synchronized (o) {
                        o.wait(m_delta);
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    
    /**
     * Close this AccountRegistry.
     */
    public void close() {
        serialiseMap(m_file);
    }
    
    /**
     * Serialise the internal representation of the registry.
     */
    private synchronized void serialiseMap(File f) {
        try {
            ObjectOutputStream stream = new ObjectOutputStream(new FileOutputStream(f));
            try {
                stream.writeObject(m_map);
            } finally {
                stream.close();
            }
            System.out.println("Saved AccountRegistry to file.");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            System.out.println("Could not serialise AccountRegistry");
        }
    }
    
    /**
     * Read the registry in from a file.
     */
    private synchronized void unserialiseMap(File f) {
        try {
            ObjectInputStream stream = new ObjectInputStream(new FileInputStream(f));
            try {
                m_map = (Map)stream.readObject();
            } finally {
                stream.close();
            }
        } catch (FileNotFoundException e) {
            
        } catch (IOException e) {
            
        } catch (ClassNotFoundException e) {
            
        }
        if (m_map == null) {
            m_map = new HashMap();
        }
        m_map = Collections.synchronizedMap(m_map);
    }
    
    /**
     * Get the SHA-1 hash of a user's password.
     * @return the hash or null if the user does not exist
     */
    public String getPassword(String user) {
        UserEntry entry = (UserEntry)m_map.get(user.toUpperCase());
        if (entry == null) {
            return null;
        }
        return entry.password;
    }
    
    /**
     * Add a new user to the registry.
     *
     * @param user the user name of the user
     * @param password an SHA-1 hash of the user's password
     * @return whether the user was added successfully
     */
    public boolean addNewUser(String user, String password) {
        String ucase = user.toUpperCase();
        if (m_map.containsKey(ucase)) {
            return false;
        }
        UserEntry entry = new UserEntry();
        entry.password = password;
        m_map.put(ucase, entry);
        return true;
    }
    
    /**
     * Set the date upon which a user becomes unbanned.
     */
    public boolean banUser(String user, long date) {
        return banUser(user, null, date);
    }
    
    /**
     * Set the date upon which a user becomes unbanned.
     */
    public boolean banUser(String user, String ip, long date) {
        UserEntry entry = (UserEntry)m_map.get(user.toUpperCase());
        if (entry == null) {
            return false;
        }
        if (ip != null) {
            entry.ip = ip;
        }
        entry.unban = date;
        return true;
    }
    
    /**
     * Determine when a user becomes unbanned.
     * @return a UNIX timestamp of the unban date
     */
    public long getUserUnbanDate(String user) {
        if (user == null) {
            return 0;
        }
        UserEntry entry = (UserEntry)m_map.get(user.toUpperCase());
        if (entry == null) {
            return 0;
        }
        return entry.unban;
    }
    
    /**
     * Return the date upon which an IP address becomes unbanned.
     */
    public long getIpUnbanDate(String ip) {
        long ret = 0;
        synchronized (m_map) {
            Iterator i = m_map.values().iterator();
            while (i.hasNext()) {
                UserEntry user = (UserEntry)i.next();
                if (ip.equals(user.ip) && (user.unban > ret)) {
                    ret = user.unban;
                }
            }
        }
        return ret;
    }
    
    /**
     * Get a list of all users in this registry.
     */
    public UserTableMessage getUserList() {
        final int length = m_map.size();
        String[] user = new String[length];
        String[] ip = new String[length];
        long[] date = new long[length];
        int[] level = new int[length];
        
        synchronized (m_map) {
            Iterator i = m_map.values().iterator();
            Iterator j = m_map.keySet().iterator();
            int k = -1;

            while (i.hasNext()) {
                ++k;
                user[k] = (String)j.next();
                UserEntry entry = (UserEntry)i.next();
                ip[k] = entry.ip;
                date[k] = entry.unban;
                level[k] = entry.level;
            }
        }
        
        return new UserTableMessage(user, ip, date, level);
    }
    
    /**
     * Get the IP Address of a particular user.
     */
    public String getIpAddress(String user) {
        UserEntry entry = (UserEntry)m_map.get(user.toUpperCase());
        if (entry == null) {
            return null;
        }
        return entry.ip;
    }
    
    /**
     * Get a List of users with a given IP Address.
     */
    public List getUsersByIp(String ip) {
        List ret = new ArrayList();
        synchronized (m_map) {
            Iterator i = m_map.keySet().iterator();
            Iterator j = m_map.values().iterator();
            while (i.hasNext()) {
                String name = (String)i.next();
                UserEntry entry = (UserEntry)j.next();
                if (ip.equals(entry.ip)) {
                    ret.add(name);
                }
            }
        }
        return ret;
    }
    
    /**
     * Get the level of a user.
     */
    public int getUserLevel(String user) {
        if (user == null) {
            return 0;
        }
        UserEntry entry = (UserEntry)m_map.get(user.toUpperCase());
        if (entry == null) {
            return 0;
        }
        return entry.level;
    }
    
    /**
     * Set the level of a user.
     */
    public boolean setUserLevel(String user, int level) {
        if (user == null) {
            return false;
        }
        UserEntry entry = (UserEntry)m_map.get(user.toUpperCase());
        if (entry == null) {
            return false;
        }
        entry.level = level;
        return true;
    }
    
}
