/*
 * AddBattleMessage.java
 *
 * Created on December 28, 2006, 9:53 PM
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

package netbattle.messages;

/**
 *
 * @author Colin
 */
public class AddBattleMessage extends NetMessage {
    
    /** Increase this by one for each change that breaks server<->client
     *  compatibility. */
    private static final long serialVersionUID = 1L;
    
    private String m_battle = null;
    private boolean m_online = false;
    private int m_id = 0;
    
    public int getMessage() {
        return ADD_BATTLE;
    }
    
    /** Creates a new instance of AddBattleMessage */
    public AddBattleMessage(int id, String battle, boolean online) {
        super(ADD_BATTLE);
        m_id = id;
        m_battle = battle;
        m_online = online;
    }
    
    public int getId() {
        return m_id;
    }
    
    public String getBattle() {
        return m_battle;
    }
    
    public boolean isOnline() {
        return m_online;
    }
    
}
