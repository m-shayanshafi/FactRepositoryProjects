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

import java.util.ArrayList;
import java.util.List;

/**
 * Class that is used to keep track of current relative time
 * and simultaneous animation of many animated values.
 */
public class Timeline
{
    private long currentTime;
    private List<AnimatedValue> values = new ArrayList<AnimatedValue>(2);

    /**
     * Returns current time for this timeline.
     * @return current time
     */
    public long getCurrentTime()
    {
        return currentTime;
    }

    /**
     * Changes the time of this timeline by given number of
     * milliseconds.
     * @param timeDelta
     *            time change in milliseconds.
     */
    public void animate(long timeDelta)
    {
        currentTime += timeDelta;
        for (AnimatedValue value : values)
            value.animate(timeDelta);
    }

    /**
     * Adds an {@link AnimatedValue} to this timeline.
     * @param value
     *            animated value to add
     */
    void addAnimatedValue(AnimatedValue value)
    {
        if (!values.contains(value))
            values.add(value);
    }

}
