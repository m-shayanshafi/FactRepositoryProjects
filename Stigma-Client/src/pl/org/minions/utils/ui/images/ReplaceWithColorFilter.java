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
package pl.org.minions.utils.ui.images;

import java.awt.Color;
import java.awt.image.RGBImageFilter;

/**
 * A {@link RGBImageFilter} that replaces all colors in the
 * filtered image with a selected {@link Color}, preserving
 * the alpha values.
 */
public final class ReplaceWithColorFilter extends RGBImageFilter
{
    private final int colorRGB;

    /**
     * Creates a new filter.
     * @param color
     *            color to replace colors in filtered images
     *            with
     */
    public ReplaceWithColorFilter(Color color)
    {
        this.colorRGB = color.getRGB() & BufferedImageFiltering.RGB_MASK;
    }

    /** {@inheritDoc} */
    @Override
    public int filterRGB(int x, int y, int rgb)
    {
        return rgb & BufferedImageFiltering.ALPHA_MASK | colorRGB;
    }
}
