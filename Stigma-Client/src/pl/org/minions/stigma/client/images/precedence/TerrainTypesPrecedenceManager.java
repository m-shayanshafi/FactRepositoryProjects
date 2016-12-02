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
package pl.org.minions.stigma.client.images.precedence;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

import pl.org.minions.stigma.client.images.TerrainTypeImageId;
import pl.org.minions.stigma.game.map.MapType;
import pl.org.minions.stigma.game.map.TerrainSet;
import pl.org.minions.stigma.game.map.TerrainType;
import pl.org.minions.stigma.game.map.TileType;
import pl.org.minions.stigma.globals.Direction;
import pl.org.minions.stigma.globals.Position;

/**
 * Class used to determine terrain transitions on the game
 * map.
 */
public class TerrainTypesPrecedenceManager
{

    private SortedSet<TerrainType> terrainTypesPrecedenceSortedSet;

    /**
     * Constructs TerrainTypesPrecedenceManager based on a
     * given TerrainSet.
     * @param terrainSet
     *            terrain set
     */
    public TerrainTypesPrecedenceManager(TerrainSet terrainSet)
    {
        terrainTypesPrecedenceSortedSet =
                new TreeSet<TerrainType>(new Comparator<TerrainType>()
                {
                    @Override
                    public int compare(TerrainType o1, TerrainType o2)
                    {
                        return ((Integer) o1.getPriority()).compareTo(o2.getPriority());
                    }
                });

        terrainTypesPrecedenceSortedSet.addAll(terrainSet.getTerrainTypes());
    }

    private void getBorderImageIds(MapType map,
                                   Position position,
                                   TerrainType terrainType,
                                   List<TerrainTypeImageId> sink)
    {

        short cornersMask = 0;
        // NW corner
        if (map.getTerrainType(position.newPosition(Direction.NW))
               .equals(terrainType))
        {
            cornersMask |= TerrainTypeImageId.NW_MASK;
        }
        // NE corner
        if (map.getTerrainType(position.newPosition(Direction.NE))
               .equals(terrainType))
        {
            cornersMask |= TerrainTypeImageId.NE_MASK;
        }
        // SE corner
        if (map.getTerrainType(position.newPosition(Direction.SE))
               .equals(terrainType))
        {
            cornersMask |= TerrainTypeImageId.SE_MASK;
        }
        // SW corner
        if (map.getTerrainType(position.newPosition(Direction.SW))
               .equals(terrainType))
        {
            cornersMask |= TerrainTypeImageId.SW_MASK;
        }
        // place the corners image id of this terraintype in the list
        if (cornersMask != 0)
        {
            sink.add(TerrainTypeImageId.createCornerImageId(terrainType,
                                                            cornersMask));
        }

        short edgesMask = 0;
        // W edge
        if (map.getTerrainType(position.newPosition(Direction.W))
               .equals(terrainType))
        {
            edgesMask |= TerrainTypeImageId.W_MASK;
        }
        // N edge
        if (map.getTerrainType(position.newPosition(Direction.N))
               .equals(terrainType))
        {
            edgesMask |= TerrainTypeImageId.N_MASK;
        }
        // E edge
        if (map.getTerrainType(position.newPosition(Direction.E))
               .equals(terrainType))
        {
            edgesMask |= TerrainTypeImageId.E_MASK;
        }
        // S edge
        if (map.getTerrainType(position.newPosition(Direction.S))
               .equals(terrainType))
        {
            edgesMask |= TerrainTypeImageId.S_MASK;
        }
        // place the edges image id of this terrain type in the list
        if (edgesMask != 0)
        {
            sink.add(TerrainTypeImageId.createEdgeImageId(terrainType,
                                                          edgesMask));
        }

    }

    /**
     * Returns a TerrainTypeImageId queue for a given
     * position on the map.
     * @param map
     *            game map
     * @param position
     *            position on the map
     * @return queue of images to be drawn on the tile
     *         determined by the position.
     */
    public List<TerrainTypeImageId> getTerrainTypeImageIdsQueue(MapType map,
                                                                Position position)
    {
        List<TerrainTypeImageId> terrainTypeImageIdList =
                new ArrayList<TerrainTypeImageId>();

        //TerrainType currentTerrainType = map.getTerrainType(position);
        TileType currentTileType = map.getTile(position);
        TerrainType currentTerrainType = currentTileType.getTerrainType();

        // get the precedence order of terrain types
        Iterator<TerrainType> terrainTypeIterator =
                getTerrainTypesPrecedenceIterator();

        TerrainType terrainType = terrainTypeIterator.next();

        // skip all terrain types that are below our current one
        while (!terrainType.equals(currentTerrainType)
            && terrainTypeIterator.hasNext())
        {
            terrainType = terrainTypeIterator.next();
        }

        getTileImageId(currentTileType, terrainTypeImageIdList);

        while (terrainTypeIterator.hasNext())
        {
            terrainType = terrainTypeIterator.next();
            getBorderImageIds(map,
                              position,
                              terrainType,
                              terrainTypeImageIdList);
        }

        return terrainTypeImageIdList;
    }

    /**
     * Returns a TerrainTypeImageId queue for a given
     * position on the map of images representing the chosen
     * terrain type.
     * @param map
     *            game map
     * @param position
     *            position on the map
     * @param terrainType
     *            terrain type to check
     * @return queue of images to be drawn on the tile
     *         determined by the position and terrain type.
     */
    public List<TerrainTypeImageId> getTerrainTypeImageIdsQueue(MapType map,
                                                                Position position,
                                                                TerrainType terrainType)
    {
        List<TerrainTypeImageId> terrainTypeImageIdList =
                new ArrayList<TerrainTypeImageId>();

        TileType currentTileType = map.getTile(position);
        TerrainType currentTerrainType = currentTileType.getTerrainType();

        if (currentTerrainType.equals(terrainType))
            getTileImageId(currentTileType, terrainTypeImageIdList);
        else if (currentTerrainType.getPriority() < terrainType.getPriority())
            getBorderImageIds(map,
                              position,
                              terrainType,
                              terrainTypeImageIdList);

        return terrainTypeImageIdList;
    }

    private Iterator<TerrainType> getTerrainTypesPrecedenceIterator()
    {
        return terrainTypesPrecedenceSortedSet.iterator();
    }

    private void getTileImageId(TileType currentTileType,
                                List<TerrainTypeImageId> sink)
    {
        sink.add(TerrainTypeImageId.createTileImageId(currentTileType));
    }
}
