/*
 * StatRefreshMessage.java
 *
 * Created on December 22, 2006, 3:14 PM
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
public class StatRefreshMessage extends NetMessage {
    
    /** Increase this by one for each change that breaks server<->client
     *  compatibility. */
    private static final long serialVersionUID = 1L;
    
    private int m_hp, m_max, m_fid, m_pp[], m_maxpp[];
    private String m_item;
    private double m_ratio;
    private boolean[] m_moves;
    
    public int getMessage() {
        return STAT_REFRESH;
    }
    
    /** Creates a new instance of StatRefreshMessage */
    public StatRefreshMessage(int fid,
            int hp,
            int max,
            int[] pp,
            int[] maxpp,
            String item,
            double ratio,
            boolean[] moves) {
        super(STAT_REFRESH);
        m_fid = fid;
        m_hp = hp;
        m_max = max;
        m_pp = pp;
        m_maxpp = maxpp;
        m_item = item;
        m_ratio = ratio;
        m_moves = moves;
    }
    
    public boolean isMoveEnabled(int i) {
        if (m_moves == null)
            return true;
        return m_moves[i];
    }
    
    public double getEnemyRatio() {
        return m_ratio;
    }
    
    public int getFieldId() {
        return m_fid;
    }
    
    public int getHp() {
        return m_hp;
    }
    
    public int getMaxHp() {
        return m_max;
    }
    
    public int getPp(int i) {
        return m_pp[i];
    }
    
    public int getMaxPp(int i) {
        return m_maxpp[i];
    }
    
    public String getItem() {
        return m_item;
    }
    
}
