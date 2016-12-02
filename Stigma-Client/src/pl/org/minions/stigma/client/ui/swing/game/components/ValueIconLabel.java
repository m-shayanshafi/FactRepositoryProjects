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
import java.awt.image.BufferedImage;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * Panel containing text label and icon, for easier
 * manipulation. Uses {@link ImagePane} so it's faster then
 * {@link JLabel}.
 */
public class ValueIconLabel extends JPanel
{
    private static final long serialVersionUID = 1L;
    private JLabel label;
    private ImagePane icon;

    /**
     * Constructor.
     */
    public ValueIconLabel()
    {
        initialize();
    }

    private void initialize()
    {
        label = new JLabel();
        icon = new ImagePane();

        this.setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
        this.add(label);
        this.add(Box.createHorizontalStrut(2));
        this.add(icon);
    }

    /**
     * Returns text displayed by this panel.
     * @return text displayed by this panel.
     */
    public String getText()
    {
        return label.getText();
    }

    /**
     * Sets text to be displayed by this panel.
     * @param text
     *            text to be displayed by this panel.
     */
    public void setText(String text)
    {
        label.setText(text);
    }

    /**
     * Returns image displayed by this component.
     * @return image displayed by this component
     */
    public BufferedImage getIcon()
    {
        return icon.getImage();
    }

    /**
     * Sets image to be displayed by this component.
     * @param icon
     *            image to be displayed by this component
     */
    public void setIcon(BufferedImage icon)
    {
        this.icon.setImage(icon);
    }

    /** {@inheritDoc} */
    @Override
    public Dimension getMaximumSize()
    {
        return new Dimension(super.getMaximumSize().width,
                             getMinimumSize().height);
    }
}
