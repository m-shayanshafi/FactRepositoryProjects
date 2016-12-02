/*
 * BattleReadyMessage.java
 *
 * Created on December 21, 2006, 10:56 PM
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
public class BattleReadyMessage extends NetMessage {
    
    /** Increase this by one for each change that breaks server<->client
     *  compatibility. */
    private static final long serialVersionUID = 1L;
    
    private int m_id, m_participant;
    private String m_users[], m_moves[][], m_party[];
    
    public int getMessage() {
        return BATTLE_READY;
    }
    
    /** Creates a new instance of BattleReadyMessage */
    public BattleReadyMessage(int id, int participant, String[] users, String[][] moves, String[] party) {
        super(BATTLE_READY);
        m_id = id;
        m_users = users;
        m_moves = moves;
        m_party = party;
        m_participant = participant;
    }
    
    public String[] getParty() {
        return m_party;
    }
    
    public String[][] getMoves() {
        return m_moves;
    }
    
    public String[] getUsers() {
        return m_users;
    }
    
    public int getFieldId() {
        return m_id;
    }
    
    public int getParticipant() {
        return m_participant;
    }
    
}
