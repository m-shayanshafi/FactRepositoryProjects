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
package pl.org.minions.utils.ui.images;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsEnvironment;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.awt.image.BufferedImageFilter;
import java.awt.image.FilteredImageSource;
import java.awt.image.ImageFilter;
import java.awt.image.ImageProducer;

import javax.swing.GrayFilter;

/**
 * Class used to produce {@link BufferedImage
 * BufferedImages} compatible with selected
 * {@link GraphicsConfiguration} by applying an
 * {@link ImageFilter} to another {@link BufferedImage}.
 */
public class BufferedImageFiltering
{
    static final int BLUE_SHIFT = 0;
    static final int GREEN_SHIFT = 8;
    static final int RED_SHIFT = 16;
    static final int ALPHA_SHIFT = 24;
    static final int CHANNEL_MASK = 0xFF;
    static final int RGB_MASK =
            CHANNEL_MASK | CHANNEL_MASK << GREEN_SHIFT
                | CHANNEL_MASK << RED_SHIFT;
    static final int ALPHA_MASK = CHANNEL_MASK << ALPHA_SHIFT;

    private static final int GRAY_PERCENTS = 75;
    private static BufferedImageFiltering instance =
            new BufferedImageFiltering();

    private GraphicsConfiguration configuration;

    /**
     * Returns the global instance.
     * @return global instance
     */
    public static BufferedImageFiltering globalInstance()
    {
        return instance;
    }

    {
        GraphicsEnvironment ge =
                GraphicsEnvironment.getLocalGraphicsEnvironment();
        configuration = ge.getDefaultScreenDevice().getDefaultConfiguration();
    }

    /**
     * Creates a copy of the image with changed colors.
     * <p>
     * Equivalent to {@code filter(image, new
     * ColorizeFilter(color))}.
     * @param image
     *            image to change color
     * @param color
     *            color to alter the image with
     * @return the resulting image
     */
    public BufferedImage colorize(BufferedImage image, Color color)
    {
        return this.filter(image, new ColorizeFilter(color));
    }

    /**
     * Creates a new buffered image by applying the selected
     * filter to a selected image.
     * @param image
     *            source image
     * @param filter
     *            filter to apply to source image
     * @return the resulting image
     */
    public BufferedImage filter(BufferedImage image, ImageFilter filter)
    {
        BufferedImage result =
                configuration.createCompatibleImage(image.getWidth(),
                                                    image.getHeight(),
                                                    image.getTransparency());
        //result.getAlphaRaster().setRect(image.getAlphaRaster());
        ImageProducer ip = new FilteredImageSource(image.getSource(), filter);

        Image tempImage = Toolkit.getDefaultToolkit().createImage(ip);

        Graphics2D g = result.createGraphics();
        g.drawImage(tempImage, 0, 0, null);

        return result;

    }

    /**
     * Creates a mirrored image.
     * @param image
     *            image to mirror
     * @param horizontal
     *            should the image be mirrored horizontally
     * @param vertical
     *            should the image be mirrored vertically
     * @return a mirrored image
     */
    public BufferedImage flip(BufferedImage image,
                              boolean horizontal,
                              boolean vertical)
    {
        final AffineTransform trans =
                AffineTransform.getScaleInstance(horizontal ? -1 : 1,
                                                 vertical ? -1 : 1);
        trans.translate(horizontal ? -image.getWidth() : 0,
                        vertical ? -image.getHeight() : 0);
        final AffineTransformOp op =
                new AffineTransformOp(trans,
                                      AffineTransformOp.TYPE_NEAREST_NEIGHBOR);

        return this.filter(image, new BufferedImageFilter(op));
    }

    /**
     * Returns configuration.
     * @return configuration
     */
    public final GraphicsConfiguration getGraphicsConfiguration()
    {
        return configuration;
    }

    /**
     * Grays out the image.
     * @param image
     *            image to gray out
     * @return grayed out image
     */
    public BufferedImage gray(BufferedImage image)
    {
        return this.filter(image, new GrayFilter(false, GRAY_PERCENTS));
    }

    /**
     * Sets new value of configuration.
     * @param configuration
     *            the configuration to set
     */
    public final void setGraphicsConfiguration(GraphicsConfiguration configuration)
    {
        this.configuration = configuration;
    }
}
