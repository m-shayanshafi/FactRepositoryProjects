/*
 * UserListMessage.java
 *
 * Created on December 21, 2006, 7:11 PM
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
import mechanics.clauses.Clause.ClauseChoice;

/**
 *
 * @author Colin
 */
public class UserListMessage extends NetMessage {
    
    /** Increase this by one for each change that breaks server<->client
     *  compatibility. */
    private static final long serialVersionUID = 1L;
    
    private String[] m_users, m_battles;
    private int[] m_battleFid;
    private int m_fid;
    private int[] m_level, m_status;
    private ClauseChoice[] m_clauses;
    
    public int getMessage() {
        return USER_LIST;
    }
    
    /** Creates a new instance of UserListMessage */
    public UserListMessage(String[] users, int[] level, int[] status,
            String[] battles, int[] battleFid, int fid, ClauseChoice[] clauses) {
        super(USER_LIST);
        m_users = users;
        m_battles = battles;
        m_fid = fid;
        m_battleFid = battleFid;
        m_level = level;
        m_status = status;
        m_clauses = clauses;
    }
    
    public int[] getStatuses() {
        return m_status;
    }
    
    public int[] getLevels() {
        return m_level;
    }
    
    public int[] getBattleFids() {
        return m_battleFid;
    }
    
    public String[] getUserList() {
        return m_users;
    }
    
    public String[] getBattleList() {
        return m_battles;
    }
    
    public ClauseChoice[] getClauses() {
        return m_clauses;
    }
    
    public int getFieldId() {
        return m_fid;
    }
}
