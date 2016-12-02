/*
 * UpdatePokemonStatusMessage.java
 *
 * Created on June 29, 12:21.
 *
 * This file is a part of Shoddy Battle.
 * Copyright (C) 2007  Colin Fitzpatrick
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
 * along with this program; if not, write to the Free Software Foundation, Inc.,
 * 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 * The Free Software Foundation may be visited online at http://www.fsf.org.
 */

package netbattle.messages;

/**
 *
 * @author Colin
 */
public class UpdatePokemonStatusMessage extends NetMessage {

    /** Increase this by one for each change that breaks server<->client
     *  compatibility. */
    private static final long serialVersionUID = 1L;
    
    public static final int STATUS_ACTIVE = 0;
    public static final int STATUS_AFFLICTED = 1;
    public static final int STATUS_FAINTED = 2;
    
    private int m_fid, m_party, m_idx, m_state;
    private String m_status;
    
    /** Creates a new instance of UpdatePokemonStatusMessage */
    public UpdatePokemonStatusMessage(int fid, int party, int idx, int state, String status) {
        m_fid = fid;
        m_party = party;
        m_idx = idx;
        m_state = state;
        m_status = status;
    }
    
    public int getFid() {
        return m_fid;
    }
    
    public String getStatus() {
        return m_status;
    }
    
    public int getState() {
        return m_state;
    }
    
    public int getIdx() {
        return m_idx;
    }
    
    public int getParty() {
        return m_party;
    }
    
    public int getMessage() {
        return UPDATE_POKEMON_STATUS;
    }

}
