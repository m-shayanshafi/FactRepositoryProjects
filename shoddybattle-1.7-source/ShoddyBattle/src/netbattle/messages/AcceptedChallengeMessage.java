/*
 * AcceptedChallengeMessage.java
 *
 * Created on December 21, 2006, 10:24 PM
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
public class AcceptedChallengeMessage extends NetMessage {
    
    /** Increase this by one for each change that breaks server<->client
     *  compatibility. */
    private static final long serialVersionUID = 1L;
    
    private int m_id = 0;
    private String m_source;
    
    public int getMessage() {
        return ACCEPTED_CHALLENGE;
    }
    
    /** Creates a new instance of AcceptedChallengeMessage */
    public AcceptedChallengeMessage(int id, String source) {
        super(ACCEPTED_CHALLENGE);
        m_id = id;
        m_source = source;
    }
    
    public String getOpponent() {
        return m_source;
    }
    
    public int getFieldId() {
        return m_id;
    }
    
}
