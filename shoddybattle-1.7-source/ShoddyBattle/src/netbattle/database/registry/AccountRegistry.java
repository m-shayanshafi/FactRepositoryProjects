/*
 * AccountRegistry.java
 *
 * Created on December 29, 2006, 11:40 PM
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

package netbattle.database.registry;
import netbattle.messages.UserTableMessage;
import java.util.List;

/**
 * <p>This class represents an account registry on the server. The class itself
 * does not implement the registry; this is left to a subclass so that
 * a user without a database may rely on flat files instead.
 *
 * <p>The general contract of the methods of this class is that passwords are
 * always SHA-1 hashes; the <code>AccountRegistry</code> class does not preform
 * any hashing itself.
 *
 * @see DatabaseImpl
 * @see FileImpl
 * @author Colin
 */
public abstract class AccountRegistry {
    
    /**
     * User level constants.
     */
    public final static int LEVEL_USER = 0;
    public final static int LEVEL_MOD = 1;
    public final static int LEVEL_ADMIN = 2;
    
    /**
     * The single instance of the AccountRegistry.
     */
    private static AccountRegistry m_inst;
    
    /**
     * Set the single instance of this class.
     */
    public static void setInstance(AccountRegistry reg) {
        m_inst = reg;
    }
    
    /**
     * Return the single instance of this class.
     */
    public static AccountRegistry getInstance() {
        return m_inst;
    }
    
    /**
     * In case close() is not otherwise called, this finialiser ensures that
     * the account registry is properly closed.
     */
    protected void finalize() throws Throwable {
        close();
    }
    
    /**
     * Close this AccountRegistry.
     */
    public abstract void close();
    
    /**
     * Get the SHA-1 hash of a user's password.
     * @return the hash or null if the user does not exist
     */
    public abstract String getPassword(String user);
    
    /**
     * Add a new user to the registry.
     *
     * @param user the user name of the user
     * @param password an SHA-1 hash of the user's password
     * @return whether the user was added successfully
     */
    public abstract boolean addNewUser(String user, String password);
    
    /**
     * Set the date upon which a user becomes unbanned.
     */
    public abstract boolean banUser(String user, long date);
    public abstract boolean banUser(String user, String ip, long date);
    
    /**
     * Determine when a user becomes unbanned.
     * @return a UNIX timestamp of the unban date
     */
    public abstract long getUserUnbanDate(String user);
    
    /**
     * Return the date upon which an IP address becomes unbanned.
     */
    public abstract long getIpUnbanDate(String ip);
    
    /**
     * Get the level of a user.
     */
    public abstract int getUserLevel(String user);
    
    /**
     * Set the level of a user.
     */
    public abstract boolean setUserLevel(String user, int level);
    
    /**
     * Get a list of all users in this registry.
     */
    public abstract UserTableMessage getUserList();
    
    /**
     * Get the IP Address of a particular user.
     */
    public abstract String getIpAddress(String user);
    
    /**
     * Get a List of users with a given IP Address.
     */
    public abstract List getUsersByIp(String ip);
    
}
