/*
 * JoinServerMessage.java
 *
 * Created on December 20, 2006, 10:32 PM
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
public class JoinServerMessage extends NetMessage {
    
    /** Increase this by one for each change that breaks server<->client
     *  compatibility. */
    private static final long serialVersionUID = 1L;
    
    private String m_name, m_password, m_captcha;
    
    public int getMessage() {
        return JOIN_SERVER;
    }
    
    /**
     * Creates a new instance of JoinServerMessage
     */
    public JoinServerMessage(String user, String password, String captcha) {
        super(JOIN_SERVER);
        m_name = user;
        m_password = password;
        m_captcha = captcha;
    }
    
    public String getCaptcha() {
        return m_captcha;
    }
    
    public String getUserName() {
        return m_name;
    }
    
    public String getPasswordHash() {
        return m_password;
    }
}
