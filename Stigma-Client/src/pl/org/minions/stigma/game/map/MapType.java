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

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;

import pl.org.minions.stigma.databases.xml.XmlDbElem;
import pl.org.minions.stigma.globals.Position;
import pl.org.minions.utils.logger.Log;

/**
 * This class represents all static map data. (without
 * actors and etc.): - entry and exit zones - list of tiles
 * - other important data It is constructed as a bean to be
 * easily filled by MapParser
 */
public class MapType implements XmlDbElem
{
    public static final byte DEFAULT_SEGMENT_WIDTH = 10;
    public static final byte DEFAULT_SEGMENT_HEIGHT = 10;

    private short id;
    private short terrainSetId;
    private short sizeX;
    private short sizeY;
    private TileType[][] tiles;
    private byte segmentSizeX = DEFAULT_SEGMENT_WIDTH;
    private byte segmentSizeY = DEFAULT_SEGMENT_HEIGHT;
    private List<Short> tilesList;
    private String name;
    private String description;
    private Map<Byte, ExitZone> exitsMap;
    private Map<Byte, EntryZone> entriesMap;
    private byte safeEntryZoneId;
    private short maxActors;
    private TerrainSet terrainSet;
    private boolean modified;

    private Map<Position, ExitZone> exitsCacheMap;

    /**
     * Constructor.
     * @param id
     *            id of map
     * @param terrainSetId
     *            id of terrainSet
     * @param sizeX
     *            map width
     * @param sizeY
     *            map height
     * @param segmentSizeX
     *            single segment width
     * @param segmentSizeY
     *            single segment height
     * @param tilesList
     *            list of tiles
     * @param entryZoneList
     *            list of entry zones
     * @param exitZoneList
     *            list of exit zones
     * @param name
     *            name of map
     * @param description
     *            description of map
     * @param maxActors
     *            maximum players available on this map
     * @param safeEntryZoneId
     *            'safe' entry zone id
     */
    public MapType(short id,
                   short terrainSetId,
                   short sizeX,
                   short sizeY,
                   byte segmentSizeX,
                   byte segmentSizeY,
                   List<Short> tilesList,
                   Collection<EntryZone> entryZoneList,
                   Collection<ExitZone> exitZoneList,
                   String name,
                   String description,
                   short maxActors,
                   byte safeEntryZoneId)
    {
        this.id = id;
        this.terrainSetId = terrainSetId;
        this.sizeX = sizeX;
        this.sizeY = sizeY;
        this.segmentSizeX = segmentSizeX;
        this.segmentSizeY = segmentSizeY;
        this.tilesList = tilesList;
        this.description = description;
        this.name = name;
        this.maxActors = maxActors;
        this.safeEntryZoneId = safeEntryZoneId;

        this.exitsMap = new HashMap<Byte, ExitZone>();
        this.exitsCacheMap = new HashMap<Position, ExitZone>();
        for (ExitZone out : exitZoneList)
        {
            exitsMap.put(out.getId(), out);

            // create exits cache
            ListIterator<Position> posIt =
                    out.getPositionsList().listIterator();
            while (posIt.hasNext())
            {
                exitsCacheMap.put(posIt.next(), out);
            }
        }

        this.entriesMap = new HashMap<Byte, EntryZone>();
        for (EntryZone in : entryZoneList)
        {
            entriesMap.put(in.getId(), in);
        }
        if (Log.isTraceEnabled())
        {
            Log.logger.trace("CREATED: MapType with id: " + this.id);
        }
    }

    /**
     * Assigns object representing tile set. After this
     * {@link #getTile(Position)},
     * {@link #getTile(int, int)} and
     * {@link #getTerrainSet()} methods are available. This
     * function will create two dimensional table of
     * {@link TileType} so it may take a while. But it will
     * do it only if table wasn't created earlier (by
     * earlier call to this method).
     * @param terrainSet
     *            object representing terrain set used by
     *            this map, should have proper id and cannot
     *            be {@code null}
     */
    public void assignTerrainSet(TerrainSet terrainSet)
    {
        if (tiles != null)
            return;
        assert terrainSet != null;
        assert terrainSet.getId() == getTerrainSetId();

        this.terrainSet = terrainSet;
        this.tiles = new TileType[getSizeX()][getSizeY()];
        ListIterator<Short> it = tilesList.listIterator();
        for (int y = 0; y < getSizeY(); ++y)
            for (int x = 0; x < getSizeX(); ++x)
            {
                assert it.hasNext();
                tiles[x][y] = terrainSet.getType(it.next(), it.next());
            }
    }

    /** {@inheritDoc} */
    @Override
    public void clearModified()
    {
        this.modified = false;
        for (ExitZone e : exitsMap.values())
            e.clearModified();
        for (EntryZone e : entriesMap.values())
            e.clearModified();
    }

    /**
     * Returns map type description.
     * @return description
     */
    public String getDescription()
    {
        return description;
    }

    /**
     * Not serializable method used to set map of entrances.
     * @return the entriesMap
     */
    public Map<Byte, EntryZone> getEntryZoneMap()
    {
        return entriesMap;
    }

    /**
     * Not serializable method used to get map of exits.
     * @return the exitsMap
     */
    public Map<Byte, ExitZone> getExitZoneMap()
    {
        return exitsMap;
    }

    /**
     * Not serializable method used to get map of exits.
     * @return the exitsMap
     */
    public Map<Position, ExitZone> getExitZonePositionsCacheMap()
    {
        return exitsCacheMap;
    }

    /**
     * Returns unique map type identifier.
     * @return unique identifier.
     */
    public short getId()
    {
        return id;
    }

    /**
     * Returns max actors allowed on this map type.
     * @return max actors
     */
    public short getMaxActors()
    {
        return maxActors;
    }

    /**
     * Returns map type name.
     * @return name of map type
     */
    public String getName()
    {
        return name;
    }

    /**
     * Returns {@link EntryZone} which will be used on
     * 'safe' map. This entry zone will be used for logging
     * in players, re-spawning them etc. Zero means there is
     * no such entry (this map is not 'safe').
     * @return 'safe' entry zone
     * @see #isSafeMap()
     */
    public byte getSafeEntryZoneId()
    {
        return safeEntryZoneId;
    }

    /**
     * Returns horizontal size of map's segments.
     * @return horizontal size of map's segments
     */
    public byte getSegmentSizeX()
    {
        return segmentSizeX;
    }

    /**
     * Returns vertical size of map's segments.
     * @return vertical size of map's segments
     */
    public byte getSegmentSizeY()
    {
        return segmentSizeY;
    }

    /**
     * Return vertical size of map.
     * @return vertical size
     */
    public short getSizeX()
    {
        return sizeX;
    }

    /**
     * Returns horizontal size of map.
     * @return horizontal size.
     */
    public short getSizeY()
    {
        return sizeY;
    }

    /**
     * Returns assigned (by call to
     * {@link #assignTerrainSet(TerrainSet)}) terrain set or
     * {@code null}.
     * @return assigned (by call to
     *         {@link #assignTerrainSet(TerrainSet)})
     *         terrain set or {@code null}.
     */

    public TerrainSet getTerrainSet()
    {
        return terrainSet;
    }

    /**
     * Return terrain set id.
     * @return terrain set id
     */
    public short getTerrainSetId()
    {
        return terrainSetId;
    }

    /**
     * Returns {@link TerrainType} for given position.
     * @param p
     *            tile position
     * @return terrain type for given position
     */
    public TerrainType getTerrainType(Position p)
    {
        TileType tileType = getTile(p);
        return tileType != null ? tileType.getTerrainType()
                               : TerrainType.EMPTY_TERRAIN;
    }

    /**
     * Returns {@link TileType} for given position, or
     * {@code null} if terrain set wasn't assigned to this
     * map type (by calling
     * {@link #assignTerrainSet(TerrainSet)}).
     * @param x
     *            x coordinate of tile position
     * @param y
     *            y coordinate of tile position
     * @return tile type for given position or {@code null}
     */
    public TileType getTile(int x, int y)
    {
        if (tiles == null)
            return null;
        if (x < 0 || x >= tiles.length || y < 0 || y >= tiles[0].length)
            return null;
        return tiles[x][y];
    }

    /**
     * Returns {@link TileType} for given position, or
     * {@code null} if terrain set wasn't assigned to this
     * map type (by calling
     * {@link #assignTerrainSet(TerrainSet)}).
     * @param p
     *            tile position
     * @return tile type for given position or {@code null}
     */
    public TileType getTile(Position p)
    {
        return getTile(p.getX(), p.getY());
    }

    /**
     * Returns tilesList.
     * @return tilesList
     */
    public List<Short> getTilesList()
    {
        return tilesList;
    }

    /**
     * Updates stored in map type tiles ids list to match
     * double array of tiles objects.
     *@see #getTilesList()
     */
    public void updateTilesList()
    {
        tilesList = new ArrayList<Short>(getSizeX() * getSizeY() * 2);

        for (int y = 0; y < getSizeY(); ++y)
            for (int x = 0; x < getSizeX(); ++x)
            {
                Short terrainTypeId =
                        (short) tiles[x][y].getTerrainType().getId();
                Short tileTypeId = (short) tiles[x][y].getId();

                tilesList.add(terrainTypeId);
                tilesList.add(tileTypeId);
            }
    }

    /**
     * Returns vertical count of segments. This function
     * makes a little calculation so it good idea to cache
     * it result.
     * @return vertical count of segments.
     */
    public short getXSegmentCount()
    {
        return (short) Math.ceil(getSizeX() / (double) getSegmentSizeX());
    }

    /**
     * Returns horizontal count of segments. This function
     * makes a little calculation so it good idea to cache
     * it result.
     * @return horizontal count of segments.
     */
    public short getYSegmentCount()
    {
        return (short) Math.ceil(getSizeY() / (double) getSegmentSizeY());
    }

    /** {@inheritDoc} */
    @Override
    public boolean isModified()
    {
        if (modified)
            return true;
        for (ExitZone e : exitsMap.values())
            if (e.isModified())
                return true;
        for (EntryZone e : entriesMap.values())
            if (e.isModified())
                return true;
        return false;
    }

    /**
     * Returns {@code true} when map was declared 'safe'.
     * Safe maps are maps to which player will be logged in,
     * re-spawned etc. This is equivalent to {@code
     * getSafeEntryZoneId() != 0}.
     * @return 'safe' attribute value
     */
    public boolean isSafeMap()
    {
        return safeEntryZoneId != 0;
    }

    /**
     * Sets description.
     * @param description
     *            the description to set
     */
    public void setDescription(String description)
    {
        this.description = description;
    }

    /**
     * Sets new map type id.
     * @param id
     *            new map type id
     */
    public void setId(short id)
    {
        this.id = id;
    }

    /**
     * Sets max actors.
     * @param maxActors
     *            new max actors value
     */
    public void setMaxActors(short maxActors)
    {
        this.maxActors = maxActors;
    }

    /** {@inheritDoc} */
    @Override
    public void setModified()
    {
        this.modified = true;
    }

    /**
     * Sets map type name.
     * @param name
     *            the name to set
     */
    public void setName(String name)
    {
        this.name = name;
    }

    /**
     * Sets new 'safe' entry zone id.
     * @see #getSafeEntryZoneId()
     * @param safeEntryZoneId
     *            new 'safe' entry zone
     */
    public void setSafeEntryZoneId(byte safeEntryZoneId)
    {
        this.safeEntryZoneId = safeEntryZoneId;
    }

    /**
     * Sets new horizontal size of map's segments.
     * @param segmentSizeX
     *            new horizontal size of map's segments
     */
    public void setSegmentSizeX(byte segmentSizeX)
    {
        this.segmentSizeX = segmentSizeX;
    }

    /**
     * Sets new vertical size of map's segments.
     * @param segmentSizeY
     *            new vertical size of map's segments
     */
    public void setSegmentSizeY(byte segmentSizeY)
    {
        this.segmentSizeY = segmentSizeY;
    }

    /**
     * Sets vertical size of map.
     * @param sizeX
     *            vertical size to set
     */
    public void setSizeX(short sizeX)
    {
        this.sizeX = sizeX;
    }

    /**
     * Sets horizontal size.
     * @param sizeY
     *            horizontal size to set
     */
    public void setSizeY(short sizeY)
    {
        this.sizeY = sizeY;
    }

    /**
     * Sets terrain set id.
     * @param terrainSet
     *            terrain set id to set
     */
    public void setTerrainSetId(short terrainSet)
    {
        this.terrainSetId = terrainSet;
    }

    /**
     * Sets {@link TileType} for given position.
     * @param x
     *            x coordinate of tile position
     * @param y
     *            y coordinate of tile position
     * @param tile
     *            tile type for given position
     */
    public void setTile(int x, int y, TileType tile)
    {
        if (tiles == null)
            return;
        if (x < 0 || x >= tiles.length || y < 0 || y >= tiles[0].length)
            return;
        tiles[x][y] = tile;
    }

    /**
     * Sets {@link TileType} for given position.
     * @param p
     *            tile position
     * @param tile
     *            tile type for given position
     */
    public void setTile(Position p, TileType tile)
    {
        setTile(p.getX(), p.getY(), tile);
    }

    /** {@inheritDoc} */
    @Override
    public String toString()
    {
        StringBuffer out = new StringBuffer();

        out.append("id: ").append(id).append('\n');
        out.append("name: ").append(name).append('\n');
        out.append("description: ").append(description).append('\n');
        out.append("terrainSetId: ").append(terrainSetId).append('\n');
        out.append("sizeX: ").append(sizeX).append('\n');
        out.append("sizeY: ").append(sizeY).append('\n');
        out.append("maxActors: ").append(maxActors).append('\n');
        out.append("safeEntryZoneId: ").append(safeEntryZoneId).append('\n');
        out.append("tilesList: ");
        for (Short sh : tilesList)
        {
            out.append(sh.shortValue()).append(' ');
        }
        out.append('\n');

        out.append("exitsMap: \n");
        for (ExitZone exit : exitsMap.values())
        {
            out.append(exit.toString());
        }

        out.append("entriesMap: \n");
        for (EntryZone entrance : entriesMap.values())
        {
            out.append(entrance.toString());
        }
        return out.toString();
    }
}
