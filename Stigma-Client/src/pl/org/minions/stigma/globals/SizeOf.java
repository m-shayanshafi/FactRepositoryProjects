/**
 *   Stigma - Multiplayer online RPG - http://stigma.sourceforge.net
 *   Copyright (C) 2005-2009 Minions Studio
 *
 *   This program is free software: you can redistribute it and/or modify
 *   it under the terms of the GNU General Public License as published by
 *   the Free Software Foundation, either version 3 of the License, or
 *   (at your option) any later version.
 *
 *   This program is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *   GNU General Public License for more details.
 *
 *   You should have received a copy of the GNU General Public License
 *   along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *   
 */
package pl.org.minions.stigma.globals;

/**
 * Utility class containing constants defining byte size of
 * various types.
 */
public final class SizeOf
{
    public static final int LONG = Long.SIZE / Byte.SIZE;

    public static final int INT = Integer.SIZE / Byte.SIZE;
    public static final int SHORT = Short.SIZE / Byte.SIZE;
    public static final int BOOLEAN = SizeOf.BYTE;
    public static final int BYTE = 1;

    private SizeOf()
    {
    }
}
