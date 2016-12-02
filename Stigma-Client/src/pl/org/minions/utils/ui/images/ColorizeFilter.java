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
 * Filter that performs performs a binary <code>and</code>
 * operation on rgba values of every pixel with the rgba
 * value of selected color.
 */
public final class ColorizeFilter extends RGBImageFilter
{
    private Color color;

    /**
     * Creates a new filter.
     * @param color
     *            color to colourize images with
     */
    public ColorizeFilter(Color color)
    {
        this.color = color;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int filterRGB(int x, int y, int rgb)
    {
        return rgb & color.getRGB();
    }
}
