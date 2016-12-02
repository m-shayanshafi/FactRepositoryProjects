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

package pl.org.minions.stigma.client.images.precedence.test;

import java.awt.Color;
import java.awt.Component;
import java.awt.Image;
import java.awt.MediaTracker;
import java.awt.Toolkit;
import java.awt.image.CropImageFilter;
import java.awt.image.FilteredImageSource;
import java.awt.image.RGBImageFilter;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import pl.org.minions.stigma.client.images.TerrainTypeImageId;
import pl.org.minions.stigma.game.map.TerrainType;

//CHECKSTYLE:OFF
public class Images
{
    class ColorMultiplyImageFilter extends RGBImageFilter
    {
        int rgb;

        public ColorMultiplyImageFilter(Color color)
        {
            rgb = color.getRGB();
        }

        /** {@inheritDoc} */
        @Override
        public int filterRGB(int x, int y, int rgb)
        {
            return rgb & this.rgb;
        }
    }

    private MediaTracker tracker;
    private static int trackedId = 0;
    private Toolkit tk = Toolkit.getDefaultToolkit();

    private Component comp;

    private Map<TerrainType, Image[]> tileImages;

    public Images(Component comp, Collection<TerrainType> terrainTypes)
    {
        this.comp = comp;
        tracker = new MediaTracker(comp);
        tileImages = new HashMap<TerrainType, Image[]>();

        for (TerrainType terrainType : terrainTypes)
        {
            tileImages.put(terrainType, loadTiles(terrainType));
        }
    }

    public Image getImage(TerrainTypeImageId terrainTypeImageId)
    {
        return tileImages.get(terrainTypeImageId.getTerrainType())[terrainTypeImageId.getImageId()];
    }

    public Image loadImage(String name)
    {

        Image tlo = null;
        try
        {
            tlo = tk.getImage(name);
            tracker.addImage(tlo, trackedId);

            tracker.waitForID(trackedId);
            if (tracker.isErrorID(trackedId))
                System.out.println("Image loading error.");

            ++trackedId;
        }
        catch (Exception e)
        {
        }

        return tlo;
    }

    private Image[] loadTiles(TerrainType terrainType)
    {
        Image[] tiles =
                new Image[TerrainTypeImageId.TERRAIN_BORDER_VARIATIONS
                    + terrainType.getNumberOfTileTypes()];

        Image tilesImg = loadImage(terrainType.getImageFileName());

        try
        {
            Image coloredImage =
                    comp.createImage(new FilteredImageSource(tilesImg.getSource(),
                                                             new ColorMultiplyImageFilter(terrainType.getColor())));
            tracker.addImage(coloredImage, trackedId);
            ++trackedId;

            tracker.waitForAll();
            if (tracker.isErrorAny())
                System.out.println("Image coloring error.");

            for (int i = 0; i < tiles.length; ++i)
            {
                tiles[i] =
                        comp.createImage(new FilteredImageSource(coloredImage.getSource(),
                                                                 new CropImageFilter(0,
                                                                                     i * 32,
                                                                                     32,
                                                                                     32)));
                tracker.addImage(tiles[i], trackedId);
            }

            tracker.waitForAll();
            if (tracker.isErrorAny())
                System.out.println("Image cropping error.");

        }
        catch (Exception e)
        {
        }

        return tiles;
    }

}
