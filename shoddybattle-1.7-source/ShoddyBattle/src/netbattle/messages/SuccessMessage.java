/*
 * SuccessMessage.java
 *
 * Created on December 21, 2006, 5:00 PM
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
public class SuccessMessage extends NetMessage {
    
    /** Increase this by one for each change that breaks server<->client
     *  compatibility. */
    private static final long serialVersionUID = 1L;
    
    private boolean m_success = false;
    private String m_message = null;
    private boolean m_captchaRequired = false;
    
    public int getMessage() {
        return SUCCESS_MESSAGE;
    }
    
    /** Creates a new instance of SuccessMessage */
    public SuccessMessage(boolean success, String message, boolean captchaRequired) {
        super(SUCCESS_MESSAGE);
        m_message = message;
        m_success = success;
        m_captchaRequired = captchaRequired;
    }
    
    public boolean isCaptchaRequired() {
        return m_captchaRequired;
    }
    
    public String getMessageText() {
        return m_message;
    }
    
    public boolean getSuccess() {
        return m_success;
    }

    public boolean logInRequired() {
        return false;
    }
}
