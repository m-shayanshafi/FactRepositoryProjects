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

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import pl.org.minions.stigma.server.ServerConfig;

/**
 * Class used to store frequently used resources in memory.
 */
public final class ResourceCache
{
    private static ResourceCache instance;
    private Map<String, ResourceCacheItem> cache;

    private Date lastCleanTime;

    private ResourceCache()
    {
        lastCleanTime = new Date();
        cache = new HashMap<String, ResourceCacheItem>();
    }

    /**
     * Gets instance of resource cache.
     * @return instance of resource cache.
     */
    public static ResourceCache getInstance()
    {
        if (instance == null)
        {
            instance = new ResourceCache();
        }
        return instance;
    }

    /**
     * Adds resource to cache. Resource data is already read
     * and passed as array of bytes.
     * @param path
     *            name used to identify cache record
     * @param data
     *            cached data
     */
    public void addResource(String path, byte[] data)
    {
        ResourceCacheItem item = new ResourceCacheItem(path, data);
        cache.put(path, item);
    }

    /**
     * Cleans cache - searches cache for items that have not
     * been used for more than indicated in variable
     * resourceCacheValidityPeriod.
     */
    public void cleanCache()
    {
        ResourceCacheItem[] items =
                cache.values().toArray(new ResourceCacheItem[0]);
        for (ResourceCacheItem item : items)
        {
            if (item.timesUsed() < ServerConfig.globalInstance()
                                               .getResourceCacheUseLimit())
            {
                cache.remove(item.getResourceName());
            }
            item.clearTimesUsed();
        }
        lastCleanTime = new Date();
    }

    /**
     * Returns lastCleanTime.
     * @return lastCleanTime
     */
    public Date getLastCleanTime()
    {
        return lastCleanTime;
    }

    /**
     * Returns resource from cache.
     * @param resourcePath
     *            path to resource, indicates which resource
     *            will be returned
     * @return resource data as array of bytes or null if no
     *         resource was found
     */
    public byte[] getResource(String resourcePath)
    {
        byte[] result = null;
        if (cache.containsKey(resourcePath))
        {
            result = cache.get(resourcePath).getResourceData();
        }
        return result;
    }
}
