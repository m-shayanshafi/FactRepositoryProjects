/*
 * Project: JDiveLog: A Dive Logbook written in Java
 * File: DecoInfo.java
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
package net.sf.jdivelog.model.udcf;

public class DecoInfo implements Sample {

    private Double depth;
    private Double tfs;
    private Double tts;

    public int getType() {
        return Sample.TYPE_DECO;
    }

    /**
     * Returns the ceiling depth. 0 if in NDL.
     * @see net.sf.jdivelog.model.udcf.Sample#getValue()
     */
    public Double getValue() {
        return depth;
    }
    
    /**
     * @param value ceiling depth. 0 if in NDL.
     */
    public void setValue(Double value) {
        depth = value;
    }

    /**
     * @param tfs time of first stop
     */
    public void setTfs(Double tfs) {
        this.tfs = tfs;
    }

    /**
     * @return time of first stop
     */
    public Double getTfs() {
        return tfs;
    }

    /**
     * @return the time to surface
     */
    public Double getTts() {
        return tts;
    }

    /**
     * @param tts the time to surface
     */
    public void setTts(Double tts) {
        this.tts = tts;
    }
    
    @Override
    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append("<DECOINFO");
        if (tfs != null) {
            sb.append(" tfs=\"");
            sb.append(tfs);
            sb.append("\"");
        }
        if (tts != null) {
            sb.append(" tts=\"");
            sb.append(tts);
            sb.append("\"");
        }
        sb.append(">");
        sb.append(depth);
        sb.append("</DECOINFO>");
        return sb.toString();
    }

}
