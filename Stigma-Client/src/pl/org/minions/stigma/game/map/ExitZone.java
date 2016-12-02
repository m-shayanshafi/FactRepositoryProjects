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

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import pl.org.minions.utils.logger.Log;

/**
 * Class which represent gate to another map. It hold
 * information about its id, tiles which it possess and and
 * the destination's map id and destination entrance. It is
 * connected with MapType class, please update MapType class
 * version after changing this class.
 */
@XmlRootElement(name = "exitZone")
@XmlType(propOrder = {})
public class ExitZone extends Zone
{
    private short destMap;
    private byte destEntryZone;

    /**
     * Default constructor needed by JAXB.
     */
    public ExitZone()
    {
        super();
        this.destEntryZone = -1;
        this.destMap = -1;
    }

    /**
     * Constructor.
     * @param id
     *            this zone id
     * @param destMap
     *            id of destination map
     * @param destGateId
     *            id of destination's map destination gate
     */
    public ExitZone(byte id, short destMap, byte destGateId)
    {
        super(id);
        this.destEntryZone = destGateId;
        this.destMap = destMap;
        if (Log.isTraceEnabled())
        {
            Log.logger.trace("CREATED: ExitZone: destination map id:"
                + this.destMap + "and destination map gate number: "
                + this.destEntryZone);
        }
    }

    /**
     * Returns destination map entrance id.
     * @return destination map entrance id
     */
    public byte getDestEntryZone()
    {
        return destEntryZone;
    }

    /**
     * Returns destination map type id.
     * @return destination map type id
     */
    public short getDestMap()
    {
        return destMap;
    }

    /**
     * Sets destination map entrance id.
     * @param destEntryZone
     *            new entrance id
     */
    @XmlAttribute(name = "destEntryZone", required = true)
    public void setDestEntryZone(byte destEntryZone)
    {
        this.destEntryZone = destEntryZone;
    }

    /**
     * Sets destination map type id.
     * @param destMap
     *            map id to set
     */
    @XmlAttribute(name = "destMap", required = true)
    public void setDestMap(short destMap)
    {
        this.destMap = destMap;
    }

    /** {@inheritDoc} */
    @Override
    public String toString()
    {
        String newline = System.getProperty("line.separator");
        StringBuffer out = new StringBuffer();
        out.append(super.toString()).append(newline);
        out.append("Dest. map:").append(destMap).append(newline);
        out.append("Dest. entry zone:").append(destEntryZone).append(newline);
        return out.toString();
    }
}
