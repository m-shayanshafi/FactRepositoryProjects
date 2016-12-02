/*
 * WelcomeTextMessage.java
 *
 * Created on July 5, 2007, 2:15 AM
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
public class WelcomeTextMessage extends NetMessage {
    
    /** Increase this by one for each change that breaks server<->client
     *  compatibility. */
    private static final long serialVersionUID = 1L;
    
    private String m_message;
    
    public int getMessage() {
        return WELCOME_TEXT_MESSAGE;
    }
    
    /**
     * @param message a null message requests that the welcome text be
     *                sent. A non-null message sets the welcome text.
     */
    public WelcomeTextMessage(String message) {
        super(WELCOME_MESSAGE);
        m_message = message;
    }
    
    public void setText(String text) {
        m_message = text;
    }
    
    public String getText() {
        return m_message;
    }
}
