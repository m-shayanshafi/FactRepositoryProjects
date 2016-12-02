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
package pl.org.minions.stigma.game.map;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import pl.org.minions.utils.logger.Log;

/**
 * Class witch represent entrance to Map object. It contains
 * data about it's id and tiles which belong to it. It is
 * connected with MapType class, please update MapType class
 * version after changing this class.
 */
@XmlRootElement(name = "entryZone")
@XmlType(propOrder = {})
public class EntryZone extends Zone
{
    /**
     * Default constructor needed by JAXB.
     */
    public EntryZone()
    {
        super();
    }

    /**
     * Constructor.
     * @param id
     *            id of gate in
     */
    public EntryZone(byte id)
    {
        super(id);
        if (Log.isTraceEnabled())
        {
            Log.logger.trace("CREATED: EntryZone with id: " + getId());
        }
    }
}
