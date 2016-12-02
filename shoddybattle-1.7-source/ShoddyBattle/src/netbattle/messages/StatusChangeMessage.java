/*
 * StatusChangeMessage.java
 *
 * Created on December 21, 2006, 7:17 PM
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
public class StatusChangeMessage extends NetMessage {
    
    /** Increase this by one for each change that breaks server<->client
     *  compatibility. */
    private static final long serialVersionUID = 1L;
    
    public static final int STATUS_HERE = 0;
    public static final int STATUS_AWAY = 1;
    
    private boolean m_online = false;
    private String m_user = null;
    private int m_fid = 0;
    private String m_description = null;
    private int m_level, m_status;
    
    public int getMessage() {
        return STATUS_CHANGE;
    }
    
    public StatusChangeMessage(boolean online, int fid) {
        super(STATUS_CHANGE);
        m_online = online;
        m_fid = fid;
    }
    
    public StatusChangeMessage(boolean online, String description) {
        super(STATUS_CHANGE);
        m_online = online;
        m_description = description;
        m_fid = -2; // Joining new room.
    }
    
    /** Creates a new instance of StatusChangeMessage */
    public StatusChangeMessage(boolean online, String user, int level, int status, int fid) {
        super(STATUS_CHANGE);
        m_online = online;
        m_user = user;
        m_fid = fid;
        m_level = level;
        m_status = status;
    }
    
    public int getStatus() {
        return m_status;
    }
    
    public int getFieldId() {
        return m_fid;
    }
    
    public boolean isOnline() {
        return m_online;
    }
    
    public int getLevel() {
        return m_level;
    }
    
    public String getUserName() {
        return m_user;
    }
    
    public String getDescription() {
        return m_description;
    }
}
