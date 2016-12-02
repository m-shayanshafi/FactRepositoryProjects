/*
 * PartyMessage.java
 *
 * Created on December 22, 2006, 8:01 PM
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
public class PartyMessage extends NetMessage {
    
    /** Increase this by one for each change that breaks server<->client
     *  compatibility. */
    private static final long serialVersionUID = 1L;
    
    private String[] m_active;
    private int[] m_gender, m_ids;
    private boolean[] m_shiny;
    private int m_fid;
    
    public int getMessage() {
        return PARTY_MESSAGE;
    }
    
    /** Creates a new instance of PartyMessage */
    public PartyMessage(int[] ids, String[] active, int[] gender, boolean[] shiny, int fid) {
        super(PARTY_MESSAGE);
        m_ids = ids;
        m_active = active;
        m_fid = fid;
        m_gender = gender;
        m_shiny = shiny;
    }
    
    public int getFieldId() {
        return m_fid;
    }
    
    public String getActivePokemon(int i) {
        return m_active[i];
    }
    
    public int[] getIds() {
        return m_ids;
    }
    
    public boolean[] getShininess() {
        return m_shiny;
    }
    
    public int[] getGender() {
        return m_gender;
    }
    
}
