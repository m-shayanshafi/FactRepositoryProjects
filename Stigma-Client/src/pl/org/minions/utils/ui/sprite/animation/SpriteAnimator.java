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

import pl.org.minions.utils.ui.sprite.Sprite;

/**
 * A class used to animate attributes of a sprite using
 * {@link AnimatedValue animated values}.
 * <p>
 * It allocates only the animated values that are
 * referenced.
 */
public class SpriteAnimator
{
    private Timeline timeline;
    private Sprite sprite;

    private AnimatedValue positionX;
    private AnimatedValue positionY;
    private AnimatedValue alpha;
    private AnimatedValue offsetX;
    private AnimatedValue offsetY;

    /**
     * Creates a new sprite animator that uses an existing
     * timeline.
     * @param sprite
     *            sprite to animate
     * @param timeline
     *            timeline for animation
     */
    public SpriteAnimator(Sprite sprite, Timeline timeline)
    {
        this.sprite = sprite;
        this.timeline = timeline;
    }

    /**
     * Creates a new sprite animator with timeline of its
     * own.
     * @param sprite
     *            sprite to animate
     */
    public SpriteAnimator(Sprite sprite)
    {
        this(sprite, new Timeline());
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
     * Returns timeline.
     * @return timeline
     */
    public final Timeline getTimeline()
    {
        return timeline;
    }

    /**
     * Returns positionX.
     * @return positionX
     */
    public final AnimatedValue getPositionX()
    {
        if (positionX == null)
            positionX =
                    new AnimatedValue(timeline, sprite.getPosition().getX());

        return positionX;
    }

    /**
     * Returns positionY.
     * @return positionY
     */
    public final AnimatedValue getPositionY()
    {
        if (positionY == null)
            positionY =
                    new AnimatedValue(timeline, sprite.getPosition().getY());

        return positionY;
    }

    /**
     * Returns alpha.
     * @return alpha
     */
    public final AnimatedValue getAlpha()
    {
        if (alpha == null)
            alpha = new AnimatedValue(timeline, sprite.getAlpha());

        return alpha;
    }

    /**
     * Returns offsetX.
     * @return offsetX
     */
    public final AnimatedValue getOffsetX()
    {
        if (offsetX == null)
            offsetX = new AnimatedValue(timeline, sprite.getOffset().getX());

        return offsetX;
    }

    /**
     * Returns offsetY.
     * @return offsetY
     */
    public final AnimatedValue getOffsetY()
    {
        if (offsetY == null)
            offsetY = new AnimatedValue(timeline, sprite.getOffset().getY());

        return offsetY;
    }

    /**
     * Applies animated values to the animated sprite.
     */
    public final void apply()
    {
        if (positionX != null || positionY != null)
        {
            double x =
                    positionX == null ? sprite.getPosition().getX()
                                     : positionX.getValue();
            double y =
                    positionY == null ? sprite.getPosition().getY()
                                     : positionY.getValue();
            sprite.getPosition().setLocation(x, y);
        }

        if (alpha != null)
            sprite.setAlpha((float) alpha.getValue());

        if (offsetX != null || offsetY != null)
        {
            double x =
                    offsetX == null ? sprite.getOffset().getX()
                                   : offsetX.getValue();
            double y =
                    offsetY == null ? sprite.getOffset().getY()
                                   : offsetY.getValue();
            sprite.getOffset().setLocation(x, y);
        }
    }
}
