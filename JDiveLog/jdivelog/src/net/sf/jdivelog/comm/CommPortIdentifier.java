/*
 * Project: JDiveLog: A Dive Logbook written in Java
 * File: CommPortIdentifier.java
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
package net.sf.jdivelog.comm;

/**
 * API independant way to identify a Comm Port.
 * @author Pascal Pellmont <jdivelog@pellmont.dyndns.org>
 */
public class CommPortIdentifier {
    
    private final CommUtil.CommApi api;
    private final String nativeName;

    public CommPortIdentifier(CommUtil.CommApi api, String nativeName) {
        this.api = api;
        this.nativeName = nativeName;
    }
    
    public CommUtil.CommApi getApi() {
        return api;
    }
    
    public String getName() {
        StringBuffer sb = new StringBuffer();
        sb.append(nativeName);
        if (CommUtil.CommApi.JAVA_COMM_API == api) {
            sb.append(" (JavaCommAPI)");
        } else if (CommUtil.CommApi.RXTX_2_1 == api) {
            sb.append(" (RXTX-2.1)");
        } else {
            sb.append(" (???)");
        }
        return sb.toString();
    }
    
    public String getNativeName() {
        return nativeName;
    }    
    
}
