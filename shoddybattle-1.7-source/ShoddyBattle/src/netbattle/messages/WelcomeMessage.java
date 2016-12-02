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
public class WelcomeMessage extends NetMessage {
    
    /** Increase this by one for each change that breaks server<->client
     *  compatibility. */
    private static final long serialVersionUID = 1L;
    
    private String m_message, m_name, m_unique;
    private int m_level;
    
    public int getMessage() {
        return WELCOME_MESSAGE;
    }
    
    public WelcomeMessage(String name, String message, String unique) {
        super(WELCOME_MESSAGE);
        m_message = message;
        m_name = name;
        m_unique = unique;
    }
    
    public WelcomeMessage(String name, int level) {
        super(WELCOME_MESSAGE);
        m_name = name;
        m_level = level;
    }
    
    public WelcomeMessage(int level) {
        super(WELCOME_MESSAGE);
        m_level = level;
    }
    
    public int getLevel() {
        return m_level;
    }
    
    public String getServerName() {
        return m_name;
    }
    
    public void setText(String text) {
        m_message = text;
    }
    
    public String getText() {
        return m_message;
    }
    
    public String getUniqueName() {
        return m_unique;
    }
    
    public boolean logInRequired() {
        return false;
    }
}
