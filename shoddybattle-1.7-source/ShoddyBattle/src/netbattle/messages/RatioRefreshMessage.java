/*
 * RatioRefreshMessage.java
 *
 * Created on May 18, 2007, 10:37 PM
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
public class RatioRefreshMessage extends NetMessage {
    
    /** Increase this by one for each change that breaks server<->client
     *  compatibility. */
    private static final long serialVersionUID = 1L;
    
    private int m_fid;
    private double[] m_ratio;
    
    public int getMessage() {
        return RATIO_REFRESH;
    }
    
    /** Creates a new instance of RatioRefreshMessage */
    public RatioRefreshMessage(int fid, double[] ratio) {
        super(RATIO_REFRESH);
        m_fid = fid;
        m_ratio = ratio;
    }
    
    public int getFieldId() {
        return m_fid;
    }
    
    public double[] getRatios() {
        return m_ratio;
    }
    
}
