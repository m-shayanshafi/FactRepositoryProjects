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

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Composite;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;

/**
 * Represents an dynamic or static object in the sprite
 * engine.
 * <p>
 * A sprite has positioned in the canvas coordinates in
 * pixels, has bounds, and position offset (between the
 * sprite position, and upper-left corner of its bounds).
 */
public abstract class Sprite
{
    private Point position = new Point(0, 0);
    private Point offset = new Point(0, 0);
    private int depthHint;

    private float alpha = 1.0f;
    private AlphaComposite alphaComposite;

    private Rectangle bounds = new Rectangle(0, 0, 0, 0);
    private Color color;

    private boolean visible = true;

    /**
     * Creates an empty sprite with trivial size, position
     * and position offset.
     */
    protected Sprite()
    {
    }

    /**
     * Checks if given point lies within the bounds of this
     * sprite.
     * @param point
     *            absolute point to check
     * @return true if the bounds of this sprite contain the
     *         given point
     */
    public final boolean boundsContainPoint(Point point)
    {
        return bounds.contains(point);
    }

    /**
     * Checks if the bounds of this sprite intersect the
     * given rectangle.
     * @param view
     *            rectangle to check
     * @return <code>true</code> if there are some common
     *         pixels
     */
    public final boolean boundsIntersect(Rectangle view)
    {
        return bounds.intersects(view);
    }

    /**
     * Checks if the specified point lies in an area this
     * sprite would render on.
     * <p>
     * Default implementation returns
     * {@link #boundsContainPoint(Point)}.
     * @param point
     *            absolute point to check
     * @return if the point is in the area covered by this
     *         sprite
     */
    public boolean containsPoint(Point point)
    {
        return boundsContainPoint(point);
    }

    /**
     * Returns alpha value.
     * @return alpha
     */
    public final float getAlpha()
    {
        return alpha;
    }

    /**
     * Returns color of this sprite.
     * <p>
     * The color is used to paint a rectangular shape when
     * no sprite image is set.
     * @return color
     */
    public final Color getColor()
    {
        return color;
    }

    /**
     * Returns the height of this sprite, which is equal or
     * greater than the height of the current image.
     * @return height
     */
    public int getHeight()
    {
        return bounds.height;
    }

    /**
     * Returns the horizontal coordinate of the lower-right
     * corner of this sprite.
     * @return right exclusive boundary of the sprite
     */
    public final int getMaxX()
    {
        return bounds.x + bounds.width;
    }

    /**
     * Returns the vertical coordinate of the lower-right
     * corner of this sprite.
     * @return bottom exclusive boundary of the sprite
     */
    public final int getMaxY()
    {
        return bounds.y + bounds.height;
    }

    /**
     * Returns the horizontal coordinate of the upper-left
     * corner of this sprite.
     * @return left inclusive boundary of the sprite
     */
    public final int getMinX()
    {
        return bounds.x;
    }

    /**
     * Returns the vertical coordinate of the upper-left
     * corner of this sprite.
     * @return top inclusive boundary of the sprite
     */
    public final int getMinY()
    {
        return bounds.y;
    }

    /**
     * Returns sprite depth hint.
     * @return depth hint
     */
    public int getDepthHint()
    {
        return depthHint;
    }

    /**
     * Returns offset between the upper-left corner of the
     * bounding rectangle and the position of this sprite.
     * @return offset in pixels
     */
    public final Point getOffset()
    {
        return offset;
    }

    /**
     * Returns the position of this sprite.
     * @return sprite position
     */
    public final Point getPosition()
    {
        return position;
    }

    /**
     * Returns the width of this sprite, which is equal or
     * greater than the width of the current image.
     * @return width
     */
    public final int getWidth()
    {
        return bounds.width;
    }

    /**
     * Returns visible.
     * @return visible
     */
    public final boolean isVisible()
    {
        return visible;
    }

    /**
     * Paints this sprite using given Graphics2D object.
     * @param graphics
     *            graphics object to paint on
     */
    public final void render(Graphics2D graphics)
    {
        if (!isVisible())
            return;

        boolean useAlphaComposite = alphaComposite != null;
        Composite defaultComposite = graphics.getComposite();

        if (useAlphaComposite)
            graphics.setComposite(alphaComposite);

        renderSpriteContents(graphics);

        if (useAlphaComposite)
            graphics.setComposite(defaultComposite);
    }

    /**
     * Paints this sprite using given Graphics2D object.
     * @param graphics
     *            graphics object to paint on
     */
    protected void renderSpriteContents(Graphics2D graphics)
    {
        if (color == null)
            return;

        graphics.setColor(getColor());
        graphics.fill(bounds);
    }

    /**
     * Sets new value of sprite alpha.
     * <p>
     * If this value is lesser than one, the alpha channel
     * of sprite image will be multiplied by sprite alpha
     * when drawing.
     * @param alpha
     *            the alpha to set
     */
    public final void setAlpha(float alpha)
    {
        if (this.alpha == alpha)
            return;
        this.alpha = alpha;

        if (alpha < 1.0)
            alphaComposite =
                    AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha);
        else
            alphaComposite = null;

    }

    /**
     * Sets new value of color.
     * <p>
     * The color is used to paint a rectangular shape when
     * no sprite image is set.
     * @param color
     *            the color to set
     */
    public final void setColor(Color color)
    {
        this.color = color;
    }

    /**
     * Sets new depth hint.
     * @param depthHint
     *            the depth hint value to set
     */
    public void setDepthHint(int depthHint)
    {
        this.depthHint = depthHint;
    }

    /**
     * Sets the offset between upper-left corner of the
     * bounding rectangle and the position of this sprite.
     * @param offset
     *            offset in pixels
     */
    public final void setOffset(Point offset)
    {
        this.offset = offset;
        updateBoundsLocation();
    }

    /**
     * Sets the position of the sprite.
     * @param newPosition
     *            new sprite position
     */
    public final void setPosition(Point newPosition)
    {
        Point oldPosition = position;
        position = newPosition;
        if (oldPosition != newPosition && oldPosition.equals(newPosition))
            return;
        else
            updateBoundsLocation();
    }

    /**
     * Sets the size of this sprite.
     * @param size
     *            size of this sprite
     */
    public final void setSize(Dimension size)
    {
        this.bounds.setSize(size);
    }

    /**
     * Sets the width and height of this sprite.
     * @param width
     *            sprite width to set
     * @param height
     *            sprite height to set
     */
    public final void setSize(int width, int height)
    {
        bounds.setSize(width, height);
    }

    /**
     * Sets new value of visible.
     * @param visible
     *            the visible to set
     */
    public final void setVisible(boolean visible)
    {
        this.visible = visible;
    }

    private void updateBoundsLocation()
    {
        bounds.setLocation(position.getLocation());
        bounds.translate(-offset.x, -offset.y);
    }

    /** {@inheritDoc} */
    @Override
    public String toString()
    {
        return this.getClass().getName() + position + " Visible:" + visible;
    }
}
