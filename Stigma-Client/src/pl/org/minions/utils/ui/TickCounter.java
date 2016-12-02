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
package pl.org.minions.utils.ui;

/**
 * Class that provides capability to calculate frequency of
 * some events (called <i>'ticks'</i>) over time (ie.
 * frames).
 * <p>
 * Currently, it updates ticks per second every 10 frames.
 * <p>
 * It can either use provided elapsed time values or use
 * {@link System#currentTimeMillis()} to measure duration of
 * ticks.
 */
public class TickCounter
{
    private static final int FULL_SECOND = 1000;
    private static final int SAMPLE_SIZE = 10;

    private int frame;

    private long accumulatedTime;

    private long lastCheck = System.currentTimeMillis();

    private double fps;

    /**
     * Notify that a counted event has happened.
     * <p>
     * Uses {@link System#currentTimeMillis()} to count time
     * difference.
     */
    public void tick()
    {
        long now = System.currentTimeMillis();
        long delta = now - lastCheck;
        tick(delta);
    }

    /**
     * Notify that counted event has happened, after given
     * number of milliseconds.
     * @param timeElapsed
     *            time that elapsed since last call
     */
    public void tick(long timeElapsed)
    {
        ++frame;
        accumulatedTime += timeElapsed;
        lastCheck += timeElapsed;

        if (frame == SAMPLE_SIZE)
        {
            double newFps = SAMPLE_SIZE * FULL_SECOND / accumulatedTime;
            fps = newFps;
            frame = 0;
            accumulatedTime = 0;
        }
    }

    /**
     * Returns the frequency for last ten ticks (per
     * second).
     * @return frequency
     */
    public double getTicksPerSecond()
    {
        return fps;
    }
}
