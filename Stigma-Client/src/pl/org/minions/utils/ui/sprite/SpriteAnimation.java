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
package pl.org.minions.utils.ui.sprite;

/**
 * Abstract class to be used as a base for elementary sprite
 * animations.
 */
public abstract class SpriteAnimation
{
    private Sprite sprite;
    private long duration;
    private long elapsedTime;
    private boolean finished;

    /**
     * Creates an animation for specified sprite with
     * specified duration.
     * <p>
     * If the duration is set to zero, the animation should
     * end in the next {@link #update(long)} call.
     * @param sprite
     *            sprite to animate
     * @param duration
     *            animation duration, in milliseconds
     */
    public SpriteAnimation(Sprite sprite, long duration)
    {
        this.sprite = sprite;
        this.duration = duration;
    }

    /**
     * Updates the state of the sprite as a result of
     * passing time.
     * @param milliseconds
     *            time that passed since animation
     *            construction or last call to
     *            <code>animate</code>.
     * @return the number of milliseconds that the animation
     *         took. Might be less than
     *         <code>milliseconds</code> if the animation is
     *         at, or comes to, its end.
     */
    public long animate(long milliseconds)
    {
        if (hasFinished())
            return 0;

        elapsedTime += milliseconds;

        long timeLeft = duration - elapsedTime;
        if (timeLeft <= 0)
        {
            finished = true;
            //We might have abused given milliseconds.
            milliseconds += timeLeft; //Return unneeded milliseconds.
            elapsedTime = duration; //Clip elapsedTime to duration.
        }
        update(milliseconds);

        return milliseconds;
    }

    /**
     * Checks how big a part of animation is complete.
     * <p>
     * Returns a number from 0.0 to 1.0, where 0.0
     * represents the start of the animation and 1.0
     * represents the end.
     * @return how big a part of the animation is complete
     */
    public final double getCompletion()
    {
        return getDuration() != 0 ? (double) getElapsedTime()
            / (double) getDuration() : 1.0;
    }

    /**
     * Returns duration.
     * @return duration
     */
    public final long getDuration()
    {
        return duration;
    }

    /**
     * Returns elapsedTime.
     * @return elapsedTime
     */
    public final long getElapsedTime()
    {
        return elapsedTime;
    }

    /**
     * Returns sprite.
     * @return sprite
     */
    public final Sprite getSprite()
    {
        return sprite;
    }

    /**
     * Checks if the animation has finished.
     * @return <code>true</code> if the animation ended,
     *         <code>false</code> otherwise.
     * @see #getDuration()
     * @see #getElapsedTime()
     */
    public boolean hasFinished()
    {
        return finished;
    }

    /**
     * Override to update the state of the animated sprite
     * as a result of passing time.
     * @param milliseconds
     *            time that passed since last update
     */
    protected abstract void update(long milliseconds);
}
