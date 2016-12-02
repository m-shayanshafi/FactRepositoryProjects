/*
 * Memory.java
 *
 * Created on January 23, 2007, 11:50 AM
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

package mechanics.intelligence;
import mechanics.*;
import java.io.Serializable;

/**
 *
 * @author Colin
 */
public class Memory implements Serializable {
    
    protected PokemonType m_me, m_opponent;
    protected boolean m_low;
    protected int m_score = 1;
    protected String m_move = null;
    
    /**
     * @param me my type
     * @param opponent the type of my opponent
     * @param low whether my health is low (as defined by my own rules)
     */
    public Memory(PokemonType me, PokemonType opponent, boolean low, String move) {
        m_me = me;
        m_opponent = opponent;
        m_low = low;
        m_move = move;
    }
    
    /**
     * Return whether two memories are semantically equal.
     */
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        
        if (!obj.getClass().getName().equals(getClass().getName())) {
            return false;
        }
        Memory mem = (Memory)obj;
        
        if ((mem.m_me == null) || (mem.m_move == null) || (mem.m_opponent == null)) {
            return false;
        }
        
        return (mem.m_me.equals(m_me)
            && mem.m_opponent.equals(m_opponent)
            && (mem.m_low == m_low)
            && (mem.m_move.equals(m_move)));
    }
}
