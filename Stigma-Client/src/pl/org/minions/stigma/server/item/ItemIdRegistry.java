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
package pl.org.minions.stigma.server.item;

import pl.org.minions.utils.logger.Log;

/**
 * Component which allows to maintain unique identifiers for
 * items.
 */
public class ItemIdRegistry
{
    private static ItemIdRegistry instance = new ItemIdRegistry();

    private int counter = Integer.MIN_VALUE;

    /**
     * Default constructor, hidden to provide Singleton
     * design pattern.
     */
    protected ItemIdRegistry()
    {
        this.counter = Integer.MIN_VALUE;
    }

    /**
     * Returns global instance.
     * @return global instance
     */
    public static ItemIdRegistry globalInstance()
    {
        return instance;
    }

    /**
     * Returns unique id for item (from id counter) and to
     * provide unique value of next id increments counter
     * value. Synchronized to assure no hazards.
     * @return unique identifier for item
     */
    public synchronized int nextId()
    {
        if (counter == Integer.MAX_VALUE)
        {
            Log.logger.fatal("Items id pool is empty.");
            System.exit(-1);
        }

        return counter++;
    }
}
