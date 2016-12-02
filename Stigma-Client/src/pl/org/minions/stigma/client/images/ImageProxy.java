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
package pl.org.minions.stigma.client.images;

import java.awt.image.BufferedImage;

/**
 * Proxy class used by {@link ImageDB} for asynchronous
 * loading of images.
 * <p>
 * Consists of state of image loading and the loaded image
 * (when loading is finished).
 */
public class ImageProxy
{
    /**
     * Represents state of image wrapped by proxy.
     */
    public static enum LoadingState
    {
        /**
         * Image should be in queue and awaits to be loaded.
         */
        LOADING,
        /**
         * Image loaded successfully (can call
         * {@link ImageProxy#getImage()}.
         */
        LOADED,
        /** Image loading failed for some reasons. */
        FAILED
    }

    private LoadingState state;
    private BufferedImage image;

    /**
     * Creates a new image proxy in
     * {@link LoadingState#LOADING} state.
     */
    protected ImageProxy()
    {
        this.state = LoadingState.LOADING;
    }

    /**
     * Returns loaded image. If state is different than
     * {@link LoadingState#LOADED} returns {@code null}.
     * @return loaded image
     */
    public BufferedImage getImage()
    {
        return image;
    }

    /**
     * Returns current state of image.
     * @return current state of image
     */
    public LoadingState getState()
    {
        return state;
    }

    /**
     * Sets new image contents for this wrapper. Should be
     * called only by {@link ImageDB}.
     * @param img
     *            new image contents
     * @return <code>true</code> if everything went fine,
     *         <code>false</code> if there was an error in
     *         the image (<code>null</code> image, invalid
     *         size, etc.)
     */
    boolean setImage(BufferedImage img)
    {
        this.image = img;
        return img != null;
    }

    /**
     * Sets new state of image. Should be called only by
     * {@link ImageDB}.
     * @param state
     *            new state of image
     */
    void setState(LoadingState state)
    {
        this.state = state;
    }
}
