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
package pl.org.minions.stigma.server.resource;

/**
 * Representation of resource cache record. Contains
 * resource data with some meta data like its id or date of
 * last usage.
 */
public class ResourceCacheItem
{
    private String resourceName;
    private byte[] resourceData;
    private int timesUsed;

    /**
     * Creates resource cache item from external data.
     * @param name
     *            id of resource cache item - full path to
     *            file which contains item data
     * @param data
     *            array of bytes with item's data
     */
    public ResourceCacheItem(String name, byte[] data)
    {
        this.resourceData = data;
        this.resourceName = name;
        this.timesUsed = 1;
    }

    /**
     * Clears timesUsed counter.
     */
    public void clearTimesUsed()
    {
        this.timesUsed = 0;
    }

    /**
     * Returns resourceData.
     * @return resourceData
     */
    public byte[] getResourceData()
    {
        timesUsed += 1;
        return resourceData;
    }

    /**
     * Returns resourceName.
     * @return resourceName
     */
    public String getResourceName()
    {
        return resourceName;
    }

    /**
     * Returns timesUsed.
     * @return timesUsed
     */
    public int timesUsed()
    {
        return timesUsed;
    }
}
