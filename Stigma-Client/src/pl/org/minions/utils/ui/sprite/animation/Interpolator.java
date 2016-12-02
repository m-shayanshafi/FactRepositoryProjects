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
 * Interpolates a time function basing on two given points
 * with derivative.
 */
public interface Interpolator
{
    /**
     * Interpolates between values of function on interval
     * of time.
     * @param startValue
     *            value at the start of time interval
     * @param startTime
     *            start of time interval
     * @param startTangent
     *            first derivative of interpolated function
     *            at the start of time interval
     * @param endValue
     *            value at the end of the interval
     * @param endTime
     *            end of time interval
     * @param endTangent
     *            first derivative of interpolated function
     *            at the end of time interval
     * @param time
     *            time to interpolate the value for
     * @return interpolated value
     */
    double interpolate(double startValue,
                       long startTime,
                       double startTangent,
                       double endValue,
                       long endTime,
                       double endTangent,
                       long time);

    /**
     * Calculates tangent of interpolation of function.
     * <p>
     * Should return actual tangent of the interpolation,
     * not some other heuristic guess of tangent of
     * approximated function.
     * @param startValue
     *            function value at the start of time
     *            interval
     * @param startTime
     *            start of time interval
     * @param startTangent
     *            first derivative of interpolated function
     *            at the start of time interval
     * @param endValue
     *            function value at the end of the interval
     * @param endTime
     *            end of time interval
     * @param endTangent
     *            first derivative of interpolated function
     *            at the end of time interval
     * @param time
     *            time to calculate the tangent for
     * @return calculated tangent
     */
    double getTangent(double startValue,
                      long startTime,
                      double startTangent,
                      double endValue,
                      long endTime,
                      double endTangent,
                      long time);
}
