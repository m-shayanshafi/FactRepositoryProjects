/*
 * Project: JDiveLog: A Dive Logbook written in Java
 * File: RadioButtonValue.java
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
 * Selection Value
 * 
 * @author Pascal Pellmont <jdivelog@pellmont.dyndns.org>
 */
public class RadioButtonValue implements OSTCValue {

    
    private final int selectedValue;
    private final int[] values;
    private final String[] descriptions;

    public RadioButtonValue(int selectedValue, int defaultValue, int[] values, String[] descriptions) {
        if (contains(values, selectedValue)) {
            this.selectedValue = selectedValue;
        } else {
            this.selectedValue = defaultValue;
        }
        this.values = values;
        this.descriptions = descriptions;
    }
    
    public RadioButtonValue(int selectedValue, int[] values, String[] descriptions) {
        if (!contains(values, selectedValue)) {
            throw new IllegalArgumentException("selectesValue is not allowed '"+selectedValue+"'");
        }
        this.selectedValue = selectedValue;
        this.values = values;
        this.descriptions = descriptions;
    }

    /**
     * @return Returns the descriptions.
     */
    public String[] getDescriptions() {
        return descriptions;
    }

    /**
     * @return Returns the selectedValue.
     */
    public int getSelectedValue() {
        return selectedValue;
    }

    /**
     * @return Returns the values.
     */
    public int[] getValues() {
        return values;
    }

    private static boolean contains(int[] values, int value) {
        for (int i=0;i<values.length;i++) {
            if (values[i] == value) {
                return true;
            }
        }
        return false;
    }
}
