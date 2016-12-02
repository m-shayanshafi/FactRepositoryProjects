/*
 * Project: JDiveLog: A Dive Logbook written in Java
 * File: ProfileReader.java
 * 
 * @author Pascal Pellmont <jdivelog@pellmont.dyndns.org>
 * 
 * This file is part of JDiveLog.
 * JDiveLog is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.

 * JDiveLog is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.

 * You should have received a copy of the GNU General Public License
 * along with JDiveLog; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 */
package net.sf.jdivelog.ci.ostc;


class ProfileReader {
    private byte[] data;
    private int pointer;
    public ProfileReader(byte[] data) {
        this.data = data;
        pointer = 0;
    }
    
    public byte[] nextProfile() {
        int start = ParseUtil.seekSequence(data, ParseUtil.SEQ_START_DIVE_HEADER, pointer);
        if (start == -1) {
            return null;
        }
        int end = ParseUtil.seekSequence(data, ParseUtil.SEQ_END_DIVE, start);
        if (end == -1) {
            return null;
        }
        int len = end+ParseUtil.SEQ_END_DIVE.length-start;
        byte[] result = new byte[len];
        System.arraycopy(data, start, result, 0, len);
        pointer = start+ParseUtil.SEQ_START_DIVE_HEADER.length;
        return result;
    }
}