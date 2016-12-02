/*
 * WelcomeMessage.java
 *
 * Created on December 20, 2006, 11:36 PM
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

package netbattle.messages;

import netbattle.*;

/**
 *
 * @author Colin
 */
public class BanMessage extends NetMessage {
    
    /** Increase this by one for each change that breaks server<->client
     *  compatibility. */
    private static final long serialVersionUID = 1L;
    
    private String m_user;
    private long m_date;
    
    public int getMessage() {
        return BAN_USER;
    }
    
    /**
     * Create a new message that requests a ban or a kick.
     * @param date the date to unban the user, or indicates that the user
     *             should be kicked if the value is -1. If this parameter
     *             is zero then the user is unbanned.
     */
    public BanMessage(String user, long date) {
        super(BAN_USER);
        m_user = user;
        m_date = date;
    }
    
    public String getUser() {
        return m_user;
    }
    
    public long getDate() {
        return m_date;
    }
}
