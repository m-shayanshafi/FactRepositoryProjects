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
 * An {@link RGBImageFilter} which performs a conversion in
 * HSB space for each filtered pixel, replacing the hue
 * value of a pixel with the hue of a selected color.
 * @see Color#HSBtoRGB(float, float, float)
 */
public final class AlterHueFilter extends RGBImageFilter
{
    private static final int CSIR_3 = 3;

    private final float hue;
    private final float[] hsv = new float[CSIR_3];

    /**
     * Creates a new filter that will set the hue value of
     * filtered pixels to the hue of a selected
     * {@link Color}.
     * @param color
     *            color to take the hue value from
     */
    public AlterHueFilter(Color color)
    {
        hue =
                Color.RGBtoHSB(color.getRed(),
                               color.getGreen(),
                               color.getBlue(),
                               hsv)[0];
    }

    /**
     * Creates a new filter that will set the hue value of
     * filtered pixels to selected value.
     * @see Color#HSBtoRGB(float, float, float)
     * @param hue
     *            hue to set
     */
    public AlterHueFilter(float hue)
    {
        this.hue = hue;
    }

    /** {@inheritDoc} */
    @Override
    public int filterRGB(int x, int y, int rgb)
    {

        Color.RGBtoHSB(rgb >>> BufferedImageFiltering.RED_SHIFT
                           & BufferedImageFiltering.CHANNEL_MASK,
                       rgb >>> BufferedImageFiltering.GREEN_SHIFT
                           & BufferedImageFiltering.CHANNEL_MASK,
                       rgb & BufferedImageFiltering.CHANNEL_MASK,
                       hsv);

        return Color.HSBtoRGB(hue, hsv[1], hsv[2])
            & BufferedImageFiltering.RGB_MASK | rgb
            & BufferedImageFiltering.ALPHA_MASK;
    }
}
