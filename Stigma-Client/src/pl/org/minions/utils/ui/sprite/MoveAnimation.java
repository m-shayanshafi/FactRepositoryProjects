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

import java.awt.Dimension;
import java.awt.Point;

/**
 * This animation moves a given distance with constant
 * velocity over time.
 * <p>
 * The current position of sprite is not taken into account.
 */
public class MoveAnimation extends SpriteAnimation
{
    private Dimension vector;
    private Dimension moved = new Dimension(0, 0);

    /**
     * Creates a new move animation for given sprite.
     * <p>
     * This animation moves the sprite from its current
     * position a given vector in given time.
     * @param sprite
     *            sprite to animate
     * @param duration
     *            animation duration
     * @param vector
     *            sprite position change
     */
    public MoveAnimation(Sprite sprite, long duration, Dimension vector)
    {
        super(sprite, duration);
        this.vector = vector;
    }

    /** {@inheritDoc} */
    @Override
    protected void update(long milliseconds)
    {
        final int x =
                getDuration() > 0 ? (int) (vector.width * getElapsedTime() / getDuration())
                                 : vector.width;
        final int y =
                getDuration() > 0 ? (int) (vector.height * getElapsedTime() / getDuration())
                                 : vector.height;

        final int dx = x - moved.width;
        final int dy = y - moved.height;

        final Point spritePosition = getSprite().getPosition();
        spritePosition.translate(dx, dy);
        getSprite().setPosition(spritePosition);

        moved.setSize(x, y);
    }
}
