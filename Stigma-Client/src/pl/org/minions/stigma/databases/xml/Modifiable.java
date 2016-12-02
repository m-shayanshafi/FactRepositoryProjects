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
package pl.org.minions.stigma.databases.xml;

/**
 * Represents "data" objects that can be modified.
 */
public interface Modifiable
{
    /**
     * Clears modification flag. Calls to
     * {@link #isModified()} will fail. Should also reset
     * children's flags (if any of them is
     * {@link Modifiable}).
     */
    void clearModified();

    /**
     * Returns {@code true} when object was modified.
     * @return {@code true} when object was modified.
     */
    boolean isModified();

    /**
     * Forces modification to {@code true}. Calls to
     * {@link #isModified()} will succeed. Should also check
     * children (if any of them is {@link Modifiable}).
     */
    void setModified();

}
