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
package pl.org.minions.stigma.client;

import java.text.MessageFormat;
import java.util.Collection;

import pl.org.minions.stigma.databases.xml.client.XmlProgressMonitor;
import pl.org.minions.stigma.databases.xml.client.XmlProgressObserver;
import pl.org.minions.stigma.databases.xml.client.XmlReader;
import pl.org.minions.stigma.game.actor.Actor;
import pl.org.minions.stigma.game.map.MapInstance;
import pl.org.minions.stigma.game.map.MapType;
import pl.org.minions.stigma.game.map.TerrainSet;
import pl.org.minions.stigma.game.world.World;
import pl.org.minions.utils.collections.ThreadSafeCollection;
import pl.org.minions.utils.logger.Log;

/**
 * The class in charge of loading map data and completing
 * the current map instance for player actor.
 */
public class MapLoader implements XmlProgressObserver
{
    /**
     * Interface used to monitor map data loading progress.
     */
    public static interface MapLoadingObsrever
    {
        /**
         * Used to notify that the state of current map has
         * changed.
         * <p>
         * Called when either:
         * <ul>
         * <li>Current map changed to another map.</li>
         * <li>A map type has been applied to current map.</li>
         * <li>A terrain set has been applied to current
         * map.</li>
         * <li>The current map is fully loaded.</li>
         * </ul>
         */
        void mapLoadingStateChanged();
    }

    private XmlProgressMonitor monitor;
    private String resourceName;

    private MapInstance currentMapInstance;

    private final Collection<MapLoadingObsrever> mapLoadingObsrevers =
            new ThreadSafeCollection<MapLoadingObsrever>();

    private boolean mapChanging;

    private boolean mapInstanceComplete;

    /**
     * Creates a new MapLoader.
     * <p>
     * Registers the loader as an observer in XmlReader.
     */
    MapLoader()
    {
        reset();
        XmlReader.globalInstance().addObserver(this);
    }

    /**
     * Resets loader data.
     */
    public synchronized void reset()
    {
        monitor = null;
        resourceName = null;

        mapChanging = true;
        mapInstanceComplete = false;
        currentMapInstance = null;
    }

    /**
     * Tells whether current map instance is complete and up
     * to date.
     * <p>
     * @return <code>true</code> if the current map instance
     *         is complete and ready
     */
    public final synchronized boolean isMapReady()
    {
        return !mapChanging && mapInstanceComplete;
    }

    /**
     * Checks if a {@link TerrainSet} has been applied to
     * the current map.
     * @return <code>true</code> if terrain set has not yet
     *         been applied
     * @see pl.org.minions.stigma.game.map.MapInstance#needsTerrainSet()
     */
    public synchronized boolean mapNeedsTerrainSet()
    {
        if (currentMapInstance != null)
            return currentMapInstance.needsTerrainSet();
        else
            return true;
    }

    /**
     * Checks if {@link MapType} has been applied to the
     * current map.
     * @return <code>true</code> when type has not yet been
     *         applied
     * @see pl.org.minions.stigma.game.map.MapInstance#needsType()
     */
    public synchronized boolean mapNeedsType()
    {
        if (currentMapInstance != null)
            return currentMapInstance.needsType();
        else
            return true;
    }

    /**
     * Updates current map.
     * <p>
     * Checks if the current map instance is indeed the map
     * the player actor is on.
     * <p>
     * Applies {@link MapType} and {@link TerrainSet}, if
     * available.
     */
    public void updateMapInstance()
    {
        if (checkIfShouldChangeMap())
            updateCurrentMapInstance();
        if (!isMapReady())
            completeMapInstance();
    }

    /**
     * Checks if map is currently changing to
     * player-occupied map.
     * @return <code>true</code> if map is changing
     */
    public boolean isMapChanging()
    {
        return mapChanging;
    }

    /**
     * Checks if current map instance is the instance that
     * player actor is on.
     * @return <code>true</code> if map is changing
     */
    synchronized boolean checkIfShouldChangeMap()
    {
        if (mapChanging)
            return true;

        final Client client = Client.globalInstance();
        final Actor playerActor = client.getPlayerActor();

        if (playerActor == null)
            mapChanging = currentMapInstance != null;
        else if (currentMapInstance == null
            || currentMapInstance.getInstanceNo() != playerActor.getMapInstanceNo()
            || !currentMapInstance.needsType()
            && currentMapInstance.getType().getId() != playerActor.getMapId())
        {
            mapChanging = true;
        }

        return mapChanging;
    }

    /**
     * Returns mapInstanceComplete.
     * @return mapInstanceComplete
     */
    public boolean isMapInstanceComplete()
    {
        return mapInstanceComplete;
    }

    /**
     * Updates the current map instance to the one player
     * actor is on.
     * <p>
     * Notifies observers if successfull.
     */
    synchronized void updateCurrentMapInstance()
    {
        if (!mapChanging)
            return;

        final Client client = Client.globalInstance();
        final World world = client.getWorld();

        final Actor playerActor = client.getPlayerActor();

        if (playerActor == null)
        {
            currentMapInstance = null;
            mapChanging = false;
            mapInstanceComplete = false;

            Log.logger.debug("Current map instance changed to null.");
            notifyObservers();
            return;
        }

        currentMapInstance =
                world.getMap(playerActor.getMapId(),
                             playerActor.getMapInstanceNo());

        if (currentMapInstance != null)
        {
            mapInstanceComplete =
                    !currentMapInstance.needsType()
                        && !currentMapInstance.needsTerrainSet();
            mapChanging = false;

            if (Log.isDebugEnabled())
                Log.logger.debug("Current map instance changed: "
                    + currentMapInstance);
            notifyObservers();
            return;
        }
    }

    /**
     * If the current map is up to date but not complete
     * applies {@link MapType} and {@link TerrainSet}, if
     * available.
     * <p>
     * Notifies observers up to three times.
     */
    synchronized void completeMapInstance()
    {
        if (mapChanging || mapInstanceComplete || currentMapInstance == null)
            return;

        final Client client = Client.globalInstance();
        final World world = client.getWorld();

        final Actor playerActor = client.getPlayerActor();

        if (currentMapInstance.needsType())
        {
            final MapType mapType =
                    world.getMapDB().getMapType(playerActor.getMapId());

            if (mapType == null)
                return;

            currentMapInstance.applyMapType(mapType);
            if (Log.isDebugEnabled())
                Log.logger.debug("Map type applied to map instance: "
                    + currentMapInstance);

            notifyObservers();
        }

        if (currentMapInstance.needsTerrainSet())
        {
            final TerrainSet terrainSet =
                    world.getMapDB()
                         .getTerrainSet(currentMapInstance.getType()
                                                          .getTerrainSetId());
            if (terrainSet == null)
                return;

            currentMapInstance.applyTerrainSet(terrainSet);
            if (Log.isDebugEnabled())
                Log.logger.debug("Terrain set applied to map: "
                    + currentMapInstance);

            notifyObservers();
        }

        mapInstanceComplete = true;

        if (Log.isDebugEnabled())
            Log.logger.debug("Map instance complete: " + currentMapInstance);
        notifyObservers();
    }

    private void notifyObservers()
    {
        for (MapLoadingObsrever obsrever : mapLoadingObsrevers)
        {
            obsrever.mapLoadingStateChanged();
        }
    }

    /**
     * Adds a {@link MapLoadingObsrever} for monitoring map
     * data loading progress.
     * @param observer
     *            observer to add
     */
    public void addMapLoadingObsrever(MapLoadingObsrever observer)
    {
        if (Log.isDebugEnabled())
            Log.logger.debug(MessageFormat.format("Adding {0} : {1}",
                                                  MapLoadingObsrever.class.getName(),
                                                  observer.getClass()));
        if (mapLoadingObsrevers.add(observer))
            Log.logger.debug("Observer added.");
    }

    /**
     * Removes a {@link MapLoadingObsrever} that is
     * monitoring map data loading progress.
     * @param observer
     *            observer to remove
     */
    public void removeMapLoadingObserver(MapLoadingObsrever observer)
    {
        if (Log.isDebugEnabled())
            Log.logger.debug(MessageFormat.format("Removing {0} : {1}",
                                                  MapLoadingObsrever.class.getName(),
                                                  observer.getClass()));

        if (mapLoadingObsrevers.remove(observer))
            Log.logger.debug("Observer removed.");
    }

    /**
     * Returns current map that the player actor is on.
     * @return map instance
     */
    synchronized MapInstance getCurrentMapInstance()
    {
        return currentMapInstance;
    }

    /** {@inheritDoc} */
    @Override
    public void processingStarted(XmlProgressMonitor monitor)
    {
        if (monitor != this.monitor)
        {
            this.monitor = monitor;
            final String path = monitor.getUrl().getPath();
            int nameStart = path.lastIndexOf('/');
            if (nameStart < 0)
                nameStart = 0;
            resourceName = path.substring(nameStart, path.length());
        }
    }

    /**
     * Returns the amount of currently loaded file that has
     * already been acquired.
     * @return downloaded size, or <code>0</code>, if not
     *         available or no file is being downloaded
     * @see pl.org.minions.stigma.databases.xml.client.XmlProgressMonitor#getDownloadedSize()
     */
    public long getDownloadedSize()
    {
        if (monitor != null)
            return monitor.getDownloadedSize();
        else
            return 0L;
    }

    /**
     * Returns the size of currently downloaded file, if
     * available.
     * @return file size, or <code>0</code>, if not
     *         available or no file is being downloaded
     * @see pl.org.minions.stigma.databases.xml.client.XmlProgressMonitor#getTotalSize()
     */
    public long getTotalSize()
    {
        if (monitor != null)
            return monitor.getTotalSize();
        else
            return 0L;
    }

    /**
     * Returns the last part of path of currently loaded
     * resource.
     * @return short name of the resource file or
     *         <code>null</code>, if not available or no
     *         file is being downloaded
     * @see pl.org.minions.stigma.databases.xml.client.XmlProgressMonitor#getUrl()
     */
    public String getResourceName()
    {
        if (monitor != null)
        {
            return resourceName;
        }
        else
            return null;
    }

}
