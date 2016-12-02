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
package pl.org.minions.utils.ui.sprite.animation;

/**
 * Performs cubic Hermite interpolation. According to <A
 * HREF=http://en.wikipedia.org/wiki/Cubic_Hermite_spline
 * #Interpolation_on_a_single_interval>this Wikipedia
 * article</A>.
 * <p>
 * Singleton.
 */
public final class CubicHermiteInterpolator implements Interpolator
{

    /**
     * Interpolator instance.
     */
    public static final Interpolator INSTANCE = new CubicHermiteInterpolator();

    private CubicHermiteInterpolator()
    {

    }

    /** {@inheritDoc} */
    @Override
    public double interpolate(double startValue,
                              long startTime,
                              double startTangent,
                              double endValue,
                              long endTime,
                              double endTangent,
                              long time)
    {
        // CHECKSTYLE:OFF
        final double h = endTime - startTime;
        final double t = (time - startTime) / h;

        final double a = t - 1;
        final double b = t * t;
        final double c = a * a;

        final double h00 = (1 + 2 * t) * c;
        final double h10 = t * c;
        final double h01 = b * (3 - 2 * t);
        final double h11 = b * a;

        return h00 * startValue + h10 * h * startTangent + h01 * endValue + h11
            * h * endTangent;
        // CHECKSTYLE:ON
    }

    /** {@inheritDoc} */
    @Override
    public double getTangent(double startValue,
                             long startTime,
                             double startTangent,
                             double endValue,
                             long endTime,
                             double endTangent,
                             long time)
    {
        // CHECKSTYLE:OFF
        final double h_inv = 1 / (endTime - startTime);
        final double t = (time - startTime) * h_inv;

        final double a = t - 1;
        final double b = 3 * t;
        final double c = a * b;

        final double h00 = 2 * c;
        final double h10 = c - a;
        final double h01 = -h00;
        final double h11 = t * (b - 2);

        return h00 * h_inv * startValue + h10 * startTangent + h01 * h_inv
            * endValue + h11 * endTangent;
        // CHECKSTYLE:ON
    }
}
