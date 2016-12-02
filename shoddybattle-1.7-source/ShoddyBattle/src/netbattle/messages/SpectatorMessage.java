/*
 * SpectatorMessage.java
 *
 * Created on June 29, 2007, 12:43 AM.
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
public class SpectatorMessage extends NetMessage {

    /** Increase this by one for each change that breaks server<->client
     *  compatibility. */
    private static final long serialVersionUID = 1L;
    
    private int m_fid;
    private int[][] m_state;
    private String[][][] m_statuses;
    
    /** Creates a new instance of SpectatorMessage */
    public SpectatorMessage(int fid, int[][] state, String[][][] statuses) {
        m_fid = fid;
        m_state = state;
        m_statuses = statuses;
    }
    
    public int getFid() {
        return m_fid;
    }
    
    public int[][] getState() {
        return m_state;
    }
    
    public String[][][] getStatuses() {
        return m_statuses;
    }
    
    public int getMessage() {
        return SPECTATOR_MESSAGE;
    }

}
