/*
 * FieldTextMessage.java
 *
 * Created on December 19, 2006, 11:00 PM
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
import java.io.*;

/**
 *
 * @author Colin
 */
public class FieldTextMessage extends NetMessage implements Externalizable {
    
    /** Increase this by one for each change that breaks server<->client
     *  compatibility. */
    private static final long serialVersionUID = 2L;
    
    private int m_field;
    private String m_message;
    private boolean m_important = false;
    
    public FieldTextMessage() {
        // Required for externalisation.
        super(FIELD_TEXT);
    }
    
    public void setImportant(boolean important) {
        m_important = important;
    }
    
    public boolean isImportant() {
        return m_important;
    }
    
    public void writeExternal(ObjectOutput out) {
        try {
            out.writeInt(m_field);
            out.writeObject(m_message);
            out.writeBoolean(m_important);
        } catch (IOException e) {
            
        }
    }
    
    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
        m_field = in.readInt();
        m_message = (String)in.readObject();
        m_important = in.readBoolean();
    }
    
    public int getMessage() {
        return FIELD_TEXT;
    }
    
    /** Creates a new instance of FieldTextMessage */
    public FieldTextMessage(int field, String message) {
        super(FIELD_TEXT);
        m_field = field;
        m_message = message;
    }
    
    public int getFieldId() {
        return m_field;
    }
    
    public String getTextMessage() {
        return m_message;
    }
}
