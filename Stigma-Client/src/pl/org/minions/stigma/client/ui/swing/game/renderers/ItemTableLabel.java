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
package pl.org.minions.stigma.client.ui.swing.game.renderers;

import javax.swing.Icon;
import javax.swing.JLabel;

/**
 * {@link JLabel} extension, just to provide special name.
 * This class will help creating special painters just for
 * item tables.
 */
public class ItemTableLabel extends JLabel
{
    private static final long serialVersionUID = 1L;

    /**
     * Creates a label with default horizontal alignment.
     * @param text
     *            label text
     */
    public ItemTableLabel(String text)
    {
        super(text);
        setName(ItemTableLabel.class.getSimpleName());
    }

    /**
     * Creates a label with default horizontal alignment.
     * @param image
     *            label icon
     */
    public ItemTableLabel(Icon image)
    {
        super(image);
        setName(ItemTableLabel.class.getSimpleName());
    }

    /**
     * Constructor.
     * @param text
     *            label text
     * @param horizontalAlignment
     *            horizontal alignment
     * @see JLabel#setHorizontalAlignment(int)
     */
    public ItemTableLabel(String text, int horizontalAlignment)
    {
        this(text);
        setHorizontalAlignment(horizontalAlignment);
    }

    /**
     * Overridden for performance purposes - see bug <a
     * href=
     * "http://bugs.sun.com/bugdatabase/view_bug.do?bug_id=6700748"
     * >6700748</a> .
     * @return {@code false}
     */
    @Override
    public boolean isVisible()
    {
        return false;
    }

}
