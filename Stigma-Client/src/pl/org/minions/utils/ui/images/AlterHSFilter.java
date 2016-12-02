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
 * HSB space for each filtered pixel, replacing the hue and
 * saturation values of a pixel with the hue and saturation
 * of a selected color.
 * @see Color#HSBtoRGB(float, float, float)
 */
public class AlterHSFilter extends RGBImageFilter
{
    private static final int CSIR_3 = 3;
    private final float hue;
    private final float saturation;
    private final float[] hsb = new float[CSIR_3];

    /**
     * Creates a new filter that will set the hue and
     * saturation values of filtered pixels to those of a
     * selected {@link Color}.
     * @param color
     *            color to take the values from
     */
    public AlterHSFilter(Color color)
    {
        Color.RGBtoHSB(color.getRed(), color.getGreen(), color.getBlue(), hsb);
        hue = hsb[0];
        saturation = hsb[1];
    }

    /**
     * Creates a new filter that will set the hue and
     * saturation values of filtered pixels to selected
     * values.
     * @see Color#HSBtoRGB(float, float, float)
     * @param hue
     *            hue to set
     * @param saturation
     *            saturation to set
     */
    public AlterHSFilter(float hue, float saturation)
    {
        this.hue = hue;
        this.saturation = saturation;
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
                       hsb);

        return Color.HSBtoRGB(hue, saturation, hsb[2])
            & BufferedImageFiltering.RGB_MASK | rgb
            & BufferedImageFiltering.ALPHA_MASK;
    }

}
