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

import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.awt.image.Raster;

/**
 * A {@link Sprite} that is rendered as an image.
 * <p>
 * The image is rendered aligned top-left with the bounds of
 * the {@link ImageSprite}.
 */
public class ImageSprite extends Sprite
{

    /**
     * Using a single instance to save on
     * allocation/deallocation time.
     * @see #renderSpriteContents(Graphics2D)
     */
    private static final AffineTransform SPRITE_WORLD_TRANSFORM =
            new AffineTransform();

    private BufferedImage image;

    /**
     * Creates an empty sprite with no image, trivial size,
     * position and position offset.
     */
    public ImageSprite()
    {
        super();
    }

    /**
     * Creates a sprite with given image and position
     * offset.
     * <p>
     * Sprite resizes to contain the image.
     * @see ImageSprite#resizeToContainImage(boolean)
     * @param image
     *            initial sprite image
     * @param positionOffset
     *            initial position offset
     */
    public ImageSprite(BufferedImage image, Point positionOffset)
    {
        super();
        this.image = image;
        setOffset(positionOffset);
        resizeToContainImage(true);
    }

    /**
     * Checks if image pixel below this point is opaque.
     * <p>
     * If the image is <code>null</code> all the pixels are
     * considered transparent.
     * <p>
     * All the pixels outside the image bounds are
     * considered transparent.
     * <p>
     * If the image has no alpha channel (indexed color
     * model has no separate alpha channel) all the pixels
     * within the image bounds are considered opaque.
     * @param point
     *            absolute point to check
     * @return <code>true</code> if the alpha value at pixel
     *         at this point is greater than <code>0</code>.
     */
    @Override
    public boolean containsPoint(Point point)
    {
        if (image == null)
            return false;
        int x = point.x - getMinX();
        int y = point.y - getMinY();
        if (x < 0 || y < 0 || x > image.getWidth() || y > image.getHeight())
            return false;
        Raster alphaRaster = image.getAlphaRaster();
        if (alphaRaster == null)
            return true;
        return alphaRaster.getSampleFloat(x, y, 0) > 0;
    }

    /**
     * Returns sprite current image.
     * @return image
     */
    public final BufferedImage getImage()
    {
        return image;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void renderSpriteContents(Graphics2D graphics)
    {
        if (null != image)
        {
            SPRITE_WORLD_TRANSFORM.setToTranslation(getMinX(), getMinY());
            graphics.drawRenderedImage(image, SPRITE_WORLD_TRANSFORM);
        }
        else
        {
            super.renderSpriteContents(graphics);
        }

    }

    /**
     * Updates the size of this sprite to be no less than
     * the size of its current image.
     * <p>
     * If the image is <code>null</code>, nothing will
     * happen.
     * @param tight
     *            if <code>true</code>, than the size of
     *            this sprite will also be cropped to the
     *            size of the image.
     */
    public void resizeToContainImage(boolean tight)
    {
        if (image == null)
            return;

        int imageW = image.getWidth();
        int imageH = image.getHeight();

        if (imageW > getWidth() || imageH > getHeight() || tight
            && (imageW < getWidth() || imageH < getHeight()))
        {
            setSize(imageW, imageH);
        }
    }

    /**
     * Sets sprite current image.
     * @param image
     *            the image to set
     */
    public final void setImage(BufferedImage image)
    {
        this.image = image;
    }

    /** {@inheritDoc} */
    @Override
    public String toString()
    {
        return super.toString() + " Image:" + (image != null);
    }
}
