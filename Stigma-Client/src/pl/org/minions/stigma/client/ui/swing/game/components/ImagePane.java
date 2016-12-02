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

import java.awt.AlphaComposite;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import javax.swing.JComponent;

/**
 * A lightweight component painting an image in its
 * upper-left corner.
 */
public class ImagePane extends JComponent
{
    private static final long serialVersionUID = -8584580828877707931L;

    private BufferedImage image;
    private boolean centered;

    /**
     * Creates an empty ImagePane.
     */
    public ImagePane()
    {
        this(null);
    }

    /**
     * Creates an ImagePane with an image to paint
     * specified.
     * @param image
     *            image to paint on the component
     */
    public ImagePane(BufferedImage image)
    {
        super();
        this.image = image;
    }

    /** {@inheritDoc} */
    @Override
    protected void paintComponent(Graphics g)
    {
        super.paintComponent(g);
        if (isOpaque())
        {
            g.setColor(getBackground());
            g.fillRect(0, 0, getWidth(), getHeight());
        }
        if (image == null)
            return;
        if (image.getColorModel().hasAlpha())
            ((Graphics2D) g).setComposite(AlphaComposite.SrcOver);
        int x = 0;
        int y = 0;
        if (centered)
        {
            x = (getWidth() - image.getWidth()) / 2;
            y = (getHeight() - image.getHeight()) / 2;
        }
        ((Graphics2D) g).drawImage(image, null, x, y);
    }

    /**
     * Returns image.
     * @return image
     */
    public BufferedImage getImage()
    {
        return image;
    }

    /**
     * Returns whether or not image is drawn centered.
     * @return whether or not image is drawn centered.
     */
    public boolean isCentered()
    {
        return centered;
    }

    /**
     * Changes 'centered' property. When {@code true} image
     * will be draw centered inside component bounds.
     * @param centered
     *            new value of property
     */
    public void setCentered(boolean centered)
    {
        this.centered = centered;
    }

    /**
     * Sets new value of image.
     * @param image
     *            the image to set
     */
    public void setImage(BufferedImage image)
    {
        this.image = image;

        if (image != null)
        {
            Dimension d = new Dimension(image.getWidth(), image.getHeight());
            setPreferredSize(d);
            setMinimumSize(d);
            setMaximumSize(d);
        }
        else
            setPreferredSize(new Dimension());

        invalidate();
        repaint();
    }
}
