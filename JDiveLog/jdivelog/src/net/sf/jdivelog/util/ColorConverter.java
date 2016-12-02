/*
 * Project: JDiveLog: A Dive Logbook written in Java
 * File: ColorConverter.java
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
package net.sf.jdivelog.util;

import java.awt.Color;

import net.sf.jdivelog.gui.resources.Messages;

public class ColorConverter {
    
    public static String convertColor(Color color) {
        StringBuffer sb = new StringBuffer("#"); //$NON-NLS-1$
        String red = Integer.toHexString(color.getRed());
        String green = Integer.toHexString(color.getGreen());
        String blue = Integer.toHexString(color.getBlue());
        if (red.length() < 2) sb.append("0"); //$NON-NLS-1$
        sb.append(red);
        if (green.length() < 2) sb.append("0"); //$NON-NLS-1$
        sb.append(green);
        if (blue.length() < 2) sb.append("0"); //$NON-NLS-1$
        sb.append(blue);
        return sb.toString();
    }

    public static Color convertColor(String color) {
        String red = color.substring(1, 3);
        String green = color.substring(3, 5);
        String blue = color.substring(5, 7);
        int r = parseHex(red);
        int g = parseHex(green);
        int b = parseHex(blue);
        return new Color(r, g, b);
    }

    private static int parseHex(String hexString) {
        int result = 0;
        int factor = 1;
        for (int i = hexString.length() - 1; i >= 0; i--) {
            int val = parseHexChar(hexString.charAt(i));
            result += val * factor;
            factor *= 16;
        }
        return result;
    }

    private static int parseHexChar(char c) {
        switch (c) {
        case '0':
            return 0;
        case '1':
            return 1;
        case '2':
            return 2;
        case '3':
            return 3;
        case '4':
            return 4;
        case '5':
            return 5;
        case '6':
            return 6;
        case '7':
            return 7;
        case '8':
            return 8;
        case '9':
            return 9;
        case 'a':
            return 10;
        case 'b':
            return 11;
        case 'c':
            return 12;
        case 'd':
            return 13;
        case 'e':
            return 14;
        case 'f':
            return 15;
        case 'A':
            return 10;
        case 'B':
            return 11;
        case 'C':
            return 12;
        case 'D':
            return 13;
        case 'E':
            return 14;
        case 'F':
            return 15;
        }
        throw new NumberFormatException(Messages.getString("not_in_hex_range") + c + "'"); //$NON-NLS-1$ //$NON-NLS-2$
    }

}
