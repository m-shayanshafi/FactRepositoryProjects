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
package pl.org.minions.stigma.client.ui.swing.game.components;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;

import pl.org.minions.stigma.client.images.ImageProxy;
import pl.org.minions.stigma.client.images.ImageProxy.LoadingState;

/**
 * Component that will display {@link ImageProxy}.
 * Additional images for other state than
 * {@link LoadingState#LOADED} are supported.
 * {@link #getPreferredSize()} will return size of image.
 */
public class ImageProxyComponent extends JPanel
{
    private static final long serialVersionUID = 1L;

    private ImageProxy proxy;
    private BufferedImage defaultImage;
    private BufferedImage notLoadedImage;

    /**
     * Constructor.
     * @param proxy
     *            proxy to be displayed by this component.
     */
    public ImageProxyComponent(ImageProxy proxy)
    {
        this.proxy = proxy;
    }

    /**
     * Returns proxy.
     * @return proxy
     */
    public ImageProxy getProxy()
    {
        return proxy;
    }

    /**
     * Sets new value of proxy.
     * @param proxy
     *            the proxy to set
     */
    public void setProxy(ImageProxy proxy)
    {
        this.proxy = proxy;
    }

    /**
     * Returns defaultImage.
     * @return defaultImage
     */
    public BufferedImage getDefaultImage()
    {
        return defaultImage;
    }

    /**
     * Sets new value of defaultImage. This image will be
     * displayed when {@link ImageProxy} is in
     * {@link LoadingState#LOADING} or
     * {@link LoadingState#FAILED} (only when
     * {@link #getNotLoadedImage()} is {@code null}) state.
     * @param defaultImage
     *            the defaultImage to set
     */
    public void setDefaultImage(BufferedImage defaultImage)
    {
        this.defaultImage = defaultImage;
    }

    /**
     * Returns notLoadedImage.
     * @return notLoadedImage
     */
    public BufferedImage getNotLoadedImage()
    {
        return notLoadedImage;
    }

    /**
     * Sets new value of notLoadedImage. This image will be
     * displayed when {@link ImageProxy} is in
     * {@link LoadingState#FAILED} state.
     * @param notLoadedImage
     *            the notLoadedImage to set
     */
    public void setNotLoadedImage(BufferedImage notLoadedImage)
    {
        this.notLoadedImage = notLoadedImage;
    }

    /**
     * Returns image that will be currently displayed by
     * this component.
     * @return image displayed by this component
     */
    public BufferedImage getCurrentImage()
    {
        if (proxy == null)
            return defaultImage;

        switch (proxy.getState())
        {
            case FAILED:
                if (notLoadedImage == null)
                    return notLoadedImage;
            case LOADING:
                return defaultImage;
            case LOADED:
                return proxy.getImage();
            default:
                assert "Unsupported state in switch".equals(null);
                return null;
        }
    }

    /** {@inheritDoc} */
    @Override
    protected void paintComponent(Graphics g)
    {
        super.paintComponent(g);

        Insets i = getInsets();

        BufferedImage image = getCurrentImage();
        if (image == null)
            return;
        Graphics2D g2 = (Graphics2D) g;
        g2.drawImage(image, null, i.left, i.top);
    }

    /** {@inheritDoc} */
    @Override
    public Dimension getPreferredSize()
    {
        if (isPreferredSizeSet())
            return super.getPreferredSize();
        BufferedImage image = getCurrentImage();
        if (image != null)
            return new Dimension(image.getWidth(), image.getHeight());
        return super.getPreferredSize();
    }

    /** {@inheritDoc} */
    @Override
    public Dimension getMinimumSize()
    {
        return getPreferredSize();
    }

    /** {@inheritDoc} */
    @Override
    public Dimension getMaximumSize()
    {
        return getPreferredSize();
    }

}
