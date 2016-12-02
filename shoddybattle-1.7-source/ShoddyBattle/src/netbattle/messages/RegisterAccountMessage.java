/*
 * RegisterAccountMessage.java
 *
 * Created on December 30, 2006, 1:32 AM
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
public class RegisterAccountMessage extends NetMessage {
    
    /** Increase this by one for each change that breaks server<->client
     *  compatibility. */
    private static final long serialVersionUID = 1L;
    
    private String m_user, m_password;
    
    public int getMessage() {
        return REGISTER_ACCOUNT;
    }
    
    /** Creates a new instance of RegisterAccountMessage */
    public RegisterAccountMessage(String user, String password) {
        super(REGISTER_ACCOUNT);
        m_user = user;
        m_password = password;
    }
    
    public String getUserName() {
        return m_user;
    }
    
    public String getPasswordHash() {
        return m_password;
    }
}
