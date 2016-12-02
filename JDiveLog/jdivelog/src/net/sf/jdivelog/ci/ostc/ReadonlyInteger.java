/*
 * Project: JDiveLog: A Dive Logbook written in Java
 * File: ReadonlyInteger.java
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
 * Type for Integer Values which can not be modified.
 * 
 * @author Pascal Pellmont <jdivelog@pellmont.dyndns.org>
 */
public class ReadonlyInteger implements OSTCValue {
    
    private final int intValue;
    
    public ReadonlyInteger(int intValue) {
        this.intValue = intValue;
    }
    
    public int intValue() {
        return intValue;
    }
    
    @Override
    public boolean equals(Object arg0) {
        if (arg0 instanceof ReadonlyInteger) {
            return intValue == ((ReadonlyInteger)arg0).intValue;
        }            
        return false;
    }
    
    @Override
    public int hashCode() {
        return intValue;
    }
    
    @Override
    public String toString() {
        return String.valueOf(intValue);
    }

}
