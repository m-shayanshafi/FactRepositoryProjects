/*
 * Project: JDiveLog: A Dive Logbook written in Java
 * File: ParseUtil.java
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

/**
 * Some universal OSTC Profile Parsing Routines.
 * 
 * @author Pascal Pellmont <jdivelog@pellmont.dyndns.org>
 */
public class ParseUtil {

    static final byte[] SEQ_START_DIVE_HEADER = {ParseUtil.toByte(0xfa), ParseUtil.toByte(0xfa)};
    static final byte[] SEQ_END_DIVE_HEADER = {toByte(0xfb), toByte(0xfb)};
    static final byte[] SEQ_END_DIVE = {toByte(0xfd), toByte(0xfd)};
    static final byte[] SEQ_END = {toByte(0xfd), toByte(0xfd), toByte(0xfe)};

    static int seekSequence(byte[] data, byte[] sequence, int offset) {
        for (int i=offset; i<data.length-sequence.length; i++) {
            boolean match = true;
            for (int j=0; match && j<sequence.length; j++) {
                match &= data[i+j] == sequence[j];
            }
            if (match) {
                return i;
            }
        }
        return -1;
    }

    static byte toByte(int i) {
        return (byte)i;
    }

    static byte[] extractAndSortProfiles(byte[] data) {
        int configlength = 266;
        int len = data.length - configlength;
        byte[] profiles1 = new byte[len*3];
        // now we stretch the ring
        System.arraycopy(data, configlength, profiles1, 0, len);
        System.arraycopy(data, configlength, profiles1, len, len);
        System.arraycopy(data, configlength, profiles1, len*2, len);
                
        // find the 1st end tag
        int firstEnd = seekSequence(profiles1, ParseUtil.SEQ_END, 0);
        
        // from there find the first start tag
        int firstStart = seekSequence(profiles1, SEQ_START_DIVE_HEADER, firstEnd > 0 ? firstEnd : 0);
        
        // find the 2nd end tag
        int secondEnd = firstStart + len;
        
        byte[] profiles2 = new byte[secondEnd+ParseUtil.SEQ_END.length-firstStart];
        System.arraycopy(profiles1, firstStart, profiles2, 0, secondEnd+ParseUtil.SEQ_END.length-firstStart);
        
        return profiles2;
    }

    static int parseCFValue(byte[] data, int offset) {
        int result = (0xff & data[offset]) + 256 * (0x7f & data[offset+1]);
        return result;
    }

}
