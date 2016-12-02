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
package pl.org.minions.stigma.editor.resourceset;

import javax.swing.Icon;

/**
 * Base class for all of the objects within the resource set
 * available within the Resource set tree.
 */
public abstract class ResourceSetViewNode
{
    /**
     * Returns the description of the node.
     * @return description
     */
    public abstract String getDescription();

    /**
     * Returns the icon of the node.
     * @return icon
     */
    public abstract Icon getIcon();

    /**
     * Returns the name of the node.
     * @return name
     */
    public abstract String getName();

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString()
    {
        return getName();
    }
}
