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

import java.awt.Dimension;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import pl.org.minions.stigma.client.ui.VisualizationGlobals;
import pl.org.minions.stigma.game.map.TerrainType;
import pl.org.minions.utils.logger.Log;

/**
 * Image proxy that stores both the terrain image loaded
 * from resource and all its separate parts for representing
 * terrain edges, corners and tiles.
 */
class TerrainImageProxy extends ResourceImageProxy
{
    private ArrayList<ImageProxy> subProxies;

    /**
     * Creates an image proxy that will store images for
     * selected terrain type.
     * @param terrainType
     *            terrain type
     */
    public TerrainImageProxy(TerrainType terrainType)
    {
        super(ImageDB.getImageName("terrains", terrainType.getImageFileName()));

        final int capacity =
                terrainType.getNumberOfTileTypes()
                    + TerrainTypeImageId.TERRAIN_BORDER_VARIATIONS;

        subProxies = new ArrayList<ImageProxy>(capacity);
        for (int i = 0; i < capacity; ++i)
            subProxies.add(new ImageProxy());
    }

    /**
     * Returns image proxy for a terrain element with given
     * id.
     * @see TerrainTypeImageId#getImageId()
     * @param imageId
     *            terrain element id
     * @return sub image proxy
     */
    ImageProxy getSubProxy(int imageId)
    {
        return subProxies.get(imageId);
    }

    /** {@inheritDoc} */
    @Override
    boolean setImage(BufferedImage img)
    {
        super.setImage(img);
        Dimension tileSize =
                new Dimension(VisualizationGlobals.MAP_TILE_WIDTH,
                              VisualizationGlobals.MAP_TILE_HEIGHT);

        //If image is to small to extract all tiles, return an error.
        if (getImage().getHeight() < tileSize.height * subProxies.size()
            || getImage().getWidth() < tileSize.width)
        {
            Log.logger.error("Terrain image lesser than anticipated according to tile size and tile types number.");

            return false;
        }

        //Divide the image
        for (int i = 0; i < subProxies.size(); ++i)
        {
            ImageProxy proxy = subProxies.get(i);
            proxy.setImage(getImage().getSubimage(0,
                                                  i * tileSize.height,
                                                  tileSize.width,
                                                  tileSize.height));
        }
        return true;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    void setState(LoadingState state)
    {
        for (ImageProxy proxy : subProxies)
        {
            proxy.setState(state);
        }
    }
}
