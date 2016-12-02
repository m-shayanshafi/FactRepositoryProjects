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
package pl.org.minions.stigma.client.images;

import pl.org.minions.stigma.game.map.TerrainType;
import pl.org.minions.stigma.game.map.TileType;

/**
 * Class representing an image from an image set for given
 * terrain type.
 */
public class TerrainTypeImageId
{
    public static final int TERRAIN_EDGE_VARIATIONS = 15;
    public static final int TERRAIN_CORNER_VARIATIONS = 15;
    public static final int TERRAIN_BORDER_VARIATIONS =
            TERRAIN_EDGE_VARIATIONS + TERRAIN_CORNER_VARIATIONS;

    public static final short NW_MASK = 0x1;
    public static final short NE_MASK = 0x2;
    public static final short SE_MASK = 0x4;
    public static final short SW_MASK = 0x8;

    public static final short W_MASK = 0x1;
    public static final short N_MASK = 0x2;
    public static final short E_MASK = 0x4;
    public static final short S_MASK = 0x8;

    private TerrainType terrainType;
    private int imageId;

    /**
     * Creates a new image id.
     * @param terrainType
     *            terrain type
     * @param imageId
     *            image id
     */
    protected TerrainTypeImageId(TerrainType terrainType, int imageId)
    {
        this.terrainType = terrainType;
        this.imageId = imageId;
    }

    /**
     * Creates a new image id that represents a combination
     * of corners of the selected terrain type.
     * @param terrainType
     *            terrain type
     * @param cornerMask
     *            sum of at least one of {@link #NW_MASK},
     *            {@link #NE_MASK}, {@link #SE_MASK} or
     *            {@link #SW_MASK}
     * @return new image id
     */
    public static TerrainTypeImageId createCornerImageId(TerrainType terrainType,
                                                         int cornerMask)
    {
        return new TerrainTypeImageId(terrainType, cornerMask
            + TERRAIN_EDGE_VARIATIONS - 1);
    }

    /**
     * Creates a new image id that represents a combination
     * of edges of the selected terrain type.
     * @param terrainType
     *            terrain type
     * @param edgeMask
     *            sum of at least one of {@link #W_MASK} ,
     *            {@link #N_MASK}, {@link #E_MASK} or
     *            {@link #S_MASK}
     * @return new image id
     */
    public static TerrainTypeImageId createEdgeImageId(TerrainType terrainType,
                                                       int edgeMask)
    {
        return new TerrainTypeImageId(terrainType, edgeMask - 1);
    }

    /**
     * Creates a new image id that represents a tile type of
     * the selected tile type.
     * @param tileType
     *            tile type
     * @return new image id
     */
    public static TerrainTypeImageId createTileImageId(TileType tileType)
    {
        return new TerrainTypeImageId(tileType.getTerrainType(),
                                      tileType.getId()
                                          + TERRAIN_BORDER_VARIATIONS);
    }

    /**
     * Returns id of the image.
     * @return id of the image
     */
    public int getImageId()
    {
        return imageId;
    }

    /**
     * Returns terrain type.
     * @return terrain type
     */
    public TerrainType getTerrainType()
    {
        return terrainType;
    }

    /**
     * Checks if this image id represents a border image or
     * tile image.
     * @return <code>true</code> if represents a tile image.
     *         <code>false</code> if represents edges or
     *         corners.
     */
    public boolean representsTile()
    {
        return imageId >= TERRAIN_BORDER_VARIATIONS;
    }

    /**
     * Sets image id.
     * @param imageId
     *            image id
     */
    public void setImageId(short imageId)
    {
        this.imageId = imageId;
    }

    /**
     * Sets terrain type.
     * @param terrainType
     *            terrain type
     */
    public void setTerrainType(TerrainType terrainType)
    {
        this.terrainType = terrainType;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString()
    {
        return "TerrainTypeImageId[terrainType:" + terrainType + ",imageId:"
            + imageId + "]";
    }

}
