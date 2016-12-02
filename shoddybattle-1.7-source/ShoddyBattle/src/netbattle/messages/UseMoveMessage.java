/*
 * UseMoveMessage.java
 *
 * Created on December 22, 2006, 11:34 AM
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
import shoddybattle.*;

/**
 *
 * @author Colin
 */
public class UseMoveMessage extends NetMessage {
    
    /** Increase this by one for each change that breaks server<->client
     *  compatibility. */
    private static final long serialVersionUID = 1L;
    
    private BattleTurn m_move;
    private int m_fid;
    
    public int getMessage() {
        return USE_MOVE;
    }
    
    /** Creates a new instance of UseMoveMessage */
    public UseMoveMessage(BattleTurn move, int fid) {
        super(USE_MOVE);
        m_move = move;
        m_fid = fid;
    }
    
    public int getFieldId() {
        return m_fid;
    }
    
    public BattleTurn getMove() {
        return m_move;
    }
    
}
