/*
 * This file is part of JMines.
 * Copyright (C) 2009 Zleurtor
 *
 * JMines is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * JMines is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with JMines.  If not, see <http://www.gnu.org/licenses/>.
 */
package jmines.view.persistence;

import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;

import javax.imageio.ImageIO;

/**
 * The class used to manage image files.
 *
 * @author Zleurtor
 */
public final class ImageAccess {

    //==========================================================================
    // Static attributes
    //==========================================================================

    //==========================================================================
    // Attributes
    //==========================================================================

    //==========================================================================
    // Constructors
    //==========================================================================
    /**
     * Construct a new Image Manager. Do nothing. Never used.
     */
    private ImageAccess() {
    }

    //==========================================================================
    // Getters
    //==========================================================================

    //==========================================================================
    // Setters
    //==========================================================================

    //==========================================================================
    // Inherited methods
    //==========================================================================

    //==========================================================================
    // Static methods
    //==========================================================================
    /**
     * Save the given Image in the file system.
     *
     * @param image The image to save.
     * @param formatName The saved image file format name (png, jpeg ...).
     * @param file The file to save the image in.
     * @throws IOException If an exception occurs while saving the image.
     */
    public static void saveImage(final RenderedImage image, final String formatName, final File file) throws IOException {
        ImageIO.write(image, formatName, file);
    }

    /**
     * Read the given file and return the image contained in.
     *
     * @param url The URL to read.
     * @return The image at the given URL.
     * @throws IOException If an exception occurs while loading the board.
     */
    public static BufferedImage loadImage(final URL url) throws IOException {
        return ImageIO.read(url);
    }

}
