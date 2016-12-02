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

import javax.xml.bind.annotation.XmlAttribute;

import pl.org.minions.stigma.databases.xml.Converter;
import pl.org.minions.stigma.game.map.TileType;

/**
 * Plain Old Java Object representing data of
 * {@link TileType}.
 */
public class TileTypeData
{
    /**
     * Converter between {@link TileType} and
     * {@link TileTypeData}.
     */
    public static class DataConverter implements
                                     Converter<TileType, TileTypeData>
    {
        /** {@inheritDoc} */
        @Override
        public TileTypeData buildData(TileType object)
        {
            TileTypeData data = new TileTypeData();
            data.setId(object.getId());
            return data;
        }

        /** {@inheritDoc} */
        @Override
        public TileType buildObject(TileTypeData data)
        {
            TileType r = new TileType(data.id);
            return r;
        }
    }

    private int id;

    /**
     * Default constructor (for JAXB).
     */
    public TileTypeData()
    {
        this.id = -1;
    }

    /**
     * Constructor.
     * @param id
     *            id of tile type
     */
    public TileTypeData(int id)
    {
        super();
        this.id = id;
    }

    /**
     * Returns id.
     * @return id
     */
    @XmlAttribute
    public int getId()
    {
        return id;
    }

    /**
     * Returns {@code true} when data is good.
     * @return {@code true} when data is good
     */
    public boolean isGood()
    {
        return id != -1;
    }

    /**
     * Sets new value of id.
     * @param id
     *            the id to set
     */
    public void setId(int id)
    {
        this.id = id;
    }
}
