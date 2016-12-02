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
package pl.org.minions.utils.ui.synth;

import java.awt.Color;
import java.awt.Insets;

import javax.swing.JProgressBar;
import javax.swing.JScrollBar;
import javax.swing.plaf.synth.SynthPainter;

/**
 * Extends SynthPainter class by adding some helpful
 * methods.
 */
public abstract class SynthPainterPlus extends SynthPainter
{

    /**
     * Describes abstract component orientation (generalizes
     * {@link JScrollBar#getOrientation()} and
     * {@link JProgressBar#getOrientation()}).
     */
    protected static enum Orientation
    {
        HORIZONTAL, VERTICAL,
    }

    /**
     * Holds alpha value for opaque colors.
     */
    protected static final int ALPHA_OPAQUE = 255;

    /**
     * Determines progress bar orientation.
     * @param orientation
     *            {@link JProgressBar} orientation flag
     * @return actual orientation
     */
    protected static Orientation getProgressBarOrientation(int orientation)
    {
        switch (orientation)
        {
            case JProgressBar.HORIZONTAL:
                return Orientation.HORIZONTAL;
            case JProgressBar.VERTICAL:
                return Orientation.VERTICAL;
            default:
                return null;
        }
    }

    /**
     * Determines scroll bar orientation.
     * @param orientation
     *            {@link JScrollBar} orientation flag
     * @return actual orientation
     */
    protected static Orientation getScrollBarOrientation(int orientation)
    {
        switch (orientation)
        {
            case JScrollBar.HORIZONTAL:
                return Orientation.HORIZONTAL;
            case JScrollBar.VERTICAL:
                return Orientation.VERTICAL;
            default:
                return null;
        }
    }

    /**
     * Tests if given insets are symmetric.
     * @param insets
     *            insets to test
     * @return <code>true</code> if horizontal insets are
     *         equal and
     *         vertical insets are equal
     */
    protected static boolean areSymmetric(Insets insets)
    {
        return insets.left == insets.right && insets.top == insets.bottom;
    }

    /**
     * Tests if the color has full alpha.
     * @param color
     *            color to test
     * @return true if alpha value of the color is equal to
     *         {@link #ALPHA_OPAQUE}.
     */
    protected static boolean isOpaque(Color color)
    {
        return color.getAlpha() == ALPHA_OPAQUE;
    }

    /**
     * Tests if all the colors have full alpha.
     * @param colors
     *            colors to test
     * @return true if alpha values of all the colors are
     *         equal to {@link #ALPHA_OPAQUE}.
     */
    protected static boolean areOpaque(Color... colors)
    {
        for (Color color : colors)
            if (!isOpaque(color))
                return false;
        return true;
    }
}
