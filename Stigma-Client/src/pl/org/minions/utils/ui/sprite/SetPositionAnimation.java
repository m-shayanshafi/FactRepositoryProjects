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

import java.awt.Point;

/**
 * Animation that moves the sprite to the specified position
 * at its end.
 */
public class SetPositionAnimation extends SpriteAnimation
{
    private Point destination;

    /**
     * Creates a new animation that sets the position of the
     * sprite to the given value when finished.
     * @param sprite
     *            sprite to animate
     * @param duration
     *            animation duration
     * @param position
     *            move destination
     */
    public SetPositionAnimation(Sprite sprite, long duration, Point position)
    {
        super(sprite, duration);
        this.destination = position;
    }

    /** {@inheritDoc} */
    @Override
    protected void update(long milliseconds)
    {
        if (hasFinished())
        {
            final Point position = getSprite().getPosition();
            position.setLocation(destination);
            getSprite().setPosition(position);
        }

    }
}
