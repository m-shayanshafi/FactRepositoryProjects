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
package pl.org.minions.stigma.game.map.data;

import java.nio.ByteBuffer;
import java.nio.ShortBuffer;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlElements;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import pl.org.minions.stigma.databases.parsers.Parsable;
import pl.org.minions.stigma.databases.xml.Converter;
import pl.org.minions.stigma.game.map.EntryZone;
import pl.org.minions.stigma.game.map.ExitZone;
import pl.org.minions.stigma.game.map.MapType;
import pl.org.minions.utils.Version;

/**
 * Plain Old Java Object holding all data needed for
 * creation of {@link MapType}.
 */
@XmlRootElement(name = "map")
@XmlType(propOrder = {})
public class MapTypeData implements Parsable
{
    /**
     * Converter between {@link MapTypeData} and
     * {@link MapType}.
     */
    public static class DataConverter implements
                                     Converter<MapType, MapTypeData>
    {
        /**
         * Decodes array of bytes to list of tiles.
         * @param tiles
         *            array of bytes do decode
         * @return tilesList decoded list of tile id's
         */
        public static List<Short> decodeTilesList(byte[] tiles)
        {
            List<Short> tilesList = new LinkedList<Short>();

            ShortBuffer shortBuffer = ByteBuffer.wrap(tiles).asShortBuffer();
            while (shortBuffer.hasRemaining())
            {
                tilesList.add(shortBuffer.get());
            }

            return tilesList;
        }

        /**
         * Encodes list of tiles id's to array of bytes.
         * @param tiles
         *            tiles to save
         * @return array of bytes
         */
        public static byte[] encodeTilesList(List<Short> tiles)
        {
            // CHECKSTYLE:OFF
            byte[] bytes = new byte[2 * tiles.size()];
            int is = 0;
            for (Short s : tiles)
            {
                bytes[is] = (byte) (s.shortValue() >>> 8 & 0xFF);
                bytes[is + 1] = (byte) (s.shortValue() >>> 0 & 0xFF);
                is += 2;
            }
            return bytes;
            // CHECKSTYLE:ON
        }

        /** {@inheritDoc} */
        @Override
        public MapTypeData buildData(MapType object)
        {
            object.updateTilesList();

            return new MapTypeData(object.getId(),
                                   object.getTerrainSetId(),
                                   object.getSizeX(),
                                   object.getSizeY(),
                                   object.getSegmentSizeX(),
                                   object.getSegmentSizeY(),
                                   encodeTilesList(object.getTilesList()),
                                   object.getEntryZoneMap().values(),
                                   object.getExitZoneMap().values(),
                                   object.getName(),
                                   object.getDescription(),
                                   object.getMaxActors(),
                                   object.getSafeEntryZoneId());
        }

        /** {@inheritDoc} */
        @Override
        public MapType buildObject(MapTypeData data)
        {
            return new MapType(data.id,
                               data.terrainSetId,
                               data.sizeX,
                               data.sizeY,
                               data.segmentSizeX,
                               data.segmentSizeY,
                               decodeTilesList(data.tiles),
                               data.entryZoneList,
                               data.exitZoneList,
                               data.name,
                               data.description,
                               data.maxActors,
                               data.safeEntryZoneId);
        }
    }

    private short id;
    private short terrainSetId;
    private short sizeX;
    private short sizeY;
    private byte segmentSizeX;
    private byte segmentSizeY;
    private byte[] tiles;
    private List<EntryZone> entryZoneList;
    private List<ExitZone> exitZoneList;
    private String name;
    private String description;
    private short maxActors;
    private String version;

    private byte safeEntryZoneId;

    /**
     * Default constructor (for JAXB).
     */
    public MapTypeData()
    {
        this.id = -1;
        this.sizeX = -1;
        this.sizeY = -1;
        this.segmentSizeX = -1;
        this.segmentSizeY = -1;
        this.tiles = null;
        this.entryZoneList = new LinkedList<EntryZone>();
        this.exitZoneList = new LinkedList<ExitZone>();
    }

    /**
     * Constructor.
     * @param id
     *            id of map
     * @param terrainSetId
     *            id of terrain set
     * @param sizeX
     *            map width
     * @param sizeY
     *            map height
     * @param segmentSizeX
     *            single segment width
     * @param segmentSizeY
     *            single segment height
     * @param tiles
     *            array of encoded tiles
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
    public MapTypeData(short id,
                       short terrainSetId,
                       short sizeX,
                       short sizeY,
                       byte segmentSizeX,
                       byte segmentSizeY,
                       byte[] tiles,
                       Collection<EntryZone> entryZoneList,
                       Collection<ExitZone> exitZoneList,
                       String name,
                       String description,
                       short maxActors,
                       byte safeEntryZoneId)
    {
        super();
        this.id = id;
        this.terrainSetId = terrainSetId;
        this.sizeX = sizeX;
        this.sizeY = sizeY;
        this.segmentSizeX = segmentSizeX;
        this.segmentSizeY = segmentSizeY;
        this.entryZoneList = new LinkedList<EntryZone>(entryZoneList);
        this.exitZoneList = new LinkedList<ExitZone>(exitZoneList);
        this.name = name;
        this.description = description;
        this.maxActors = maxActors;
        this.safeEntryZoneId = safeEntryZoneId;
        this.version = Version.FULL_VERSION;
        this.tiles = tiles;
    }

    /**
     * Returns description.
     * @return description
     */
    @XmlElement
    public String getDescription()
    {
        return description;
    }

    /**
     * Returns gateInList.
     * @return gateInList
     */
    @XmlElementWrapper
    @XmlElements(value = @XmlElement(name = "entryZone", type = EntryZone.class))
    public List<EntryZone> getEntryZoneList()
    {
        return entryZoneList;
    }

    /**
     * Returns gateOutList.
     * @return gateOutList
     */
    @XmlElementWrapper
    @XmlElements(value = @XmlElement(name = "exitZone", type = ExitZone.class))
    public List<ExitZone> getExitZoneList()
    {
        return exitZoneList;
    }

    /**
     * Returns id.
     * @return id
     */
    @XmlAttribute(required = true)
    public short getId()
    {
        return id;
    }

    /**
     * Returns maxActors.
     * @return maxActors
     */
    @XmlAttribute(required = true)
    public short getMaxActors()
    {
        return maxActors;
    }

    /**
     * Returns name.
     * @return name
     */
    @XmlElement
    public String getName()
    {
        return name;
    }

    /**
     * Returns safeEntryZoneId.
     * @return safeEntryZoneId
     */
    @XmlAttribute(required = false)
    public byte getSafeEntryZoneId()
    {
        return safeEntryZoneId;
    }

    /**
     * Returns segmentSizeX.
     * @return segmentSizeX
     */
    @XmlAttribute(required = false)
    public byte getSegmentSizeX()
    {
        return segmentSizeX;
    }

    /**
     * Returns segmentSizeY.
     * @return segmentSizeY
     */
    @XmlAttribute(required = false)
    public byte getSegmentSizeY()
    {
        return segmentSizeY;
    }

    /**
     * Returns sizeX.
     * @return sizeX
     */
    @XmlAttribute(required = true)
    public short getSizeX()
    {
        return sizeX;
    }

    /**
     * Returns sizeY.
     * @return sizeY
     */
    @XmlAttribute(required = true)
    public short getSizeY()
    {
        return sizeY;
    }

    /**
     * Returns terrainSetId.
     * @return terrainSetId
     */
    @XmlAttribute
    public short getTerrainSetId()
    {
        return terrainSetId;
    }

    /**
     * Returns encodedTilesList.
     * @return encodedTilesList
     */
    public byte[] getTiles()
    {
        return tiles;
    }

    /** {@inheritDoc} */
    @Override
    @XmlAttribute(required = true)
    public String getVersion()
    {
        return version;
    }

    /** {@inheritDoc} */
    @Override
    public boolean isGood()
    {
        return this.id != 0 && this.sizeX > 0 && this.sizeY > 0
            && this.segmentSizeX > 0 && this.segmentSizeY > 0
            && this.tiles != null && this.tiles.length > 0
        //            && this.entryZoneList != null && !this.entryZoneList.isEmpty()
        //            && this.exitZoneList != null && !this.exitZoneList.isEmpty()
        ;
    }

    /**
     * Sets new value of description.
     * @param description
     *            the description to set
     */
    public void setDescription(String description)
    {
        this.description = description;
    }

    /**
     * Sets new value of id.
     * @param id
     *            the id to set
     */
    public void setId(short id)
    {
        this.id = id;
    }

    /**
     * Sets new value of maxActors.
     * @param maxActors
     *            the maxActors to set
     */
    public void setMaxActors(short maxActors)
    {
        this.maxActors = maxActors;
    }

    /**
     * Sets new value of name.
     * @param name
     *            the name to set
     */
    public void setName(String name)
    {
        this.name = name;
    }

    /**
     * Sets new value of safeEntryZoneId.
     * @param safeEntryZoneId
     *            the safeEntryZoneId to set
     */
    public void setSafeEntryZoneId(byte safeEntryZoneId)
    {
        this.safeEntryZoneId = safeEntryZoneId;
    }

    /**
     * Sets new value of segmentSizeX.
     * @param segmentSizeX
     *            the segmentSizeX to set
     */
    public void setSegmentSizeX(byte segmentSizeX)
    {
        this.segmentSizeX = segmentSizeX;
    }

    /**
     * Sets new value of segmentSizeY.
     * @param segmentSizeY
     *            the segmentSizeY to set
     */
    public void setSegmentSizeY(byte segmentSizeY)
    {
        this.segmentSizeY = segmentSizeY;
    }

    /**
     * Sets new value of sizeX.
     * @param sizeX
     *            the sizeX to set
     */
    public void setSizeX(short sizeX)
    {
        this.sizeX = sizeX;
    }

    /**
     * Sets new value of sizeY.
     * @param sizeY
     *            the sizeY to set
     */
    public void setSizeY(short sizeY)
    {
        this.sizeY = sizeY;
    }

    /**
     * Sets new value of terrainSetId.
     * @param terrainSetId
     *            the terrainSetId to set
     */
    public void setTerrainSetId(short terrainSetId)
    {
        this.terrainSetId = terrainSetId;
    }

    /**
     * Sets new value of tiles.
     * @param tiles
     *            the tiles to set
     */
    @XmlElement(name = "tiles")
    public void setTiles(byte[] tiles)
    {
        this.tiles = tiles;
    }

    /**
     * Sets new version.
     * @param version
     *            sets version
     */
    public void setVersion(String version)
    {
        this.version = version;
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
        for (Short sh : MapTypeData.DataConverter.decodeTilesList(tiles))
        {
            out.append(sh.shortValue()).append(' ');
        }
        out.append('\n');

        out.append("exits: \n");
        for (ExitZone exit : exitZoneList)
        {
            out.append(exit.toString());
        }

        out.append("entries: \n");
        for (EntryZone entrance : entryZoneList)
        {
            out.append(entrance.toString());
        }
        return out.toString();
    }
}
