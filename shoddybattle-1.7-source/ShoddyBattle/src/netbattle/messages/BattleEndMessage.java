/*
 * BattleEndMessage.java
 *
 * Created on May 9, 2007, 5:40 PM
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
public class BattleEndMessage extends NetMessage {
    
    /** Increase this by one for each change that breaks server<->client
     *  compatibility. */
    private static final long serialVersionUID = 1L;
    
    private int m_victor = -1, m_fid = -1;
    
    public int getMessage() {
        return BATTLE_END;
    }
    
    /**
     * Creates a new instance of BattleEndMessage
     */
    public BattleEndMessage(int victor, int fid) {
        super(BATTLE_END);
        m_victor = victor;
        m_fid = fid;
    }
    
    public BattleEndMessage(int fid) {
        super(BATTLE_END);
        m_fid = fid;
    }
    
    public int getFieldId() {
        return m_fid;
    }
    
    public int getVictor() {
        return m_victor;
    }
    
}
