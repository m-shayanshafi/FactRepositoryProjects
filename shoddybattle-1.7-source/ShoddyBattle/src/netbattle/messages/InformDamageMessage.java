/*
 * InformDamageMessage.java
 *
 * Created on February 5, 2007, 5:14 PM
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
public class InformDamageMessage extends NetMessage {
    
    /** Increase this by one for each change that breaks server<->client
     *  compatibility. */
    private static final long serialVersionUID = 1L;
    
    private int m_fid, m_party, m_target;
    private double m_ratio;
    private String m_name;
    
    public int getMessage() {
        return INFORM_DAMAGE;
    }
    
    /** Creates a new instance of InformDamageMessage */
    public InformDamageMessage(int fid,
            int party, int target, double ratio, String name) {
        super(INFORM_DAMAGE);
        m_fid = fid;
        m_party = party;
        m_target = target;
        m_ratio = ratio;
        m_name = name;
    }
    
    public int getFieldId() {
        return m_fid;
    }
    
    public int getParty() {
        return m_party;
    }
    
    public int getTarget() {
        return m_target;
    }
    
    public double getRatio() {
        return m_ratio;
    }
    
    public String getName() {
        return m_name;
    }
    
}
