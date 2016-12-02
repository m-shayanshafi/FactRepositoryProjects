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
 * Interpolator that returns <code>startValue</code> for
 * <code>time < endTime</code>.
 * <p>
 * Singleton.
 */
public final class StepInterpolator implements Interpolator
{

    /**
     * Interpolator instance.
     */
    public static final Interpolator INSTANCE = new StepInterpolator();

    private StepInterpolator()
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
        if (time < endTime)
            return startValue;
        else
            return endValue;
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
        return 0;
    }
}
