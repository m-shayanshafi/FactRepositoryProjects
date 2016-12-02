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
import pl.org.minions.stigma.game.map.TerrainSet;
import pl.org.minions.stigma.game.map.TerrainType;
import pl.org.minions.utils.Version;

/**
 * Plain Old Java Object holding all data needed for
 * creation of {@link TerrainSet}.
 */
@XmlRootElement(name = "terrainset")
@XmlType(propOrder = {})
public class TerrainSetData implements Parsable
{
    /**
     * Converter between {@link TerrainSetData} and
     * {@link TerrainSet}.
     */
    public static class DataConverter implements
                                     Converter<TerrainSet, TerrainSetData>
    {
        private Converter<TerrainType, TerrainTypeData> terrainConverter =
                new TerrainTypeData.DataConverter();

        /** {@inheritDoc} */
        @Override
        public TerrainSetData buildData(TerrainSet object)
        {
            List<TerrainTypeData> list = new LinkedList<TerrainTypeData>();
            for (TerrainType tt : object.getTerrainTypes())
                list.add(terrainConverter.buildData(tt));

            return new TerrainSetData(list,
                                      object.getId(),
                                      object.getName(),
                                      object.getDescription());
        }

        /** {@inheritDoc} */
        @Override
        public TerrainSet buildObject(TerrainSetData data)
        {
            TerrainSet r = new TerrainSet(data.id);
            r.setName(data.name);
            r.setDescription(data.description);
            for (TerrainTypeData t : data.terrainList)
                r.addTerrainType(terrainConverter.buildObject(t));
            return r;
        }
    }

    private String version;
    private List<TerrainTypeData> terrainList;
    private short id;
    private String name;

    private String description;

    /**
     * Default constructor (for JAXB).
     */
    public TerrainSetData()
    {
        this.id = -1;
        this.terrainList = new LinkedList<TerrainTypeData>();
    }

    /**
     * Constructor.
     * @param terrainList
     *            terrain for this terrain set
     * @param id
     *            id of this terrain set
     * @param name
     *            name of this terrain set
     * @param description
     *            description of this terrain set
     */
    public TerrainSetData(List<TerrainTypeData> terrainList,
                          short id,
                          String name,
                          String description)
    {
        this.terrainList = terrainList;
        this.id = id;
        this.name = name;
        this.description = description;
        this.version = Version.FULL_VERSION;
    }

    /**
     * Returns description.
     * @return description
     */
    public String getDescription()
    {
        return description;
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
     * Returns name.
     * @return name
     */
    public String getName()
    {
        return name;
    }

    /**
     * Returns terrainList.
     * @return terrainList
     */
    @XmlElementWrapper
    @XmlElements(value = @XmlElement(name = "terrain", type = TerrainTypeData.class))
    public List<TerrainTypeData> getTerrainList()
    {
        return terrainList;
    }

    /** {@inheritDoc} */
    @XmlAttribute(required = true)
    @Override
    public String getVersion()
    {
        return version;
    }

    /** {@inheritDoc} */
    @Override
    public boolean isGood()
    {
        if (id == -1 || terrainList == null || terrainList.isEmpty())
            return false;
        for (TerrainTypeData t : terrainList)
            if (!t.isGood())
                return false;
        return true;
    }

    /**
     * Sets new value of description.
     * @param description
     *            the description to set
     */
    @XmlElement(name = "desc", required = false)
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
     * Sets new value of name.
     * @param name
     *            the name to set
     */
    @XmlElement(name = "name", required = false)
    public void setName(String name)
    {
        this.name = name;
    }

    /**
     * Sets new version.
     * @param version
     *            new version
     */
    public void setVersion(String version)
    {
        this.version = version;

    }

    /** {@inheritDoc} */
    @Override
    public String toString()
    {
        String newline = System.getProperty("line.separator");
        StringBuffer out = new StringBuffer();
        out.append("id: ").append(id).append(newline);
        out.append("name: ").append(name).append(newline);
        out.append("description: ").append(description).append(newline);
        out.append("tilesMap:").append(newline);
        for (TerrainTypeData tt : terrainList)
            out.append("[" + tt.getId() + ": " + tt.isPassable());
        return out.toString();
    }
}
