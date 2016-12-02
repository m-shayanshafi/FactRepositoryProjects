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
package pl.org.minions.stigma.client.ui.swing.game.tooltips;

import javax.swing.JToolTip;

import pl.org.minions.stigma.client.ui.swing.game.components.items.ItemPanel;
import pl.org.minions.stigma.game.item.Item;

/**
 * Stigma customized ToolTip used to draw items.
 */
public class ItemToolTip extends JToolTip
{
    private static final long serialVersionUID = 1L;

    private ItemPanel ittpanel;

    /**
     * Default constructor.
     * @param item
     *            Item to be drawn
     */
    public ItemToolTip(Item item)
    {
        assert item != null;
        ittpanel = new ItemPanel(item);

        this.add(ittpanel);

        this.setPreferredSize(ittpanel.getPreferredSize());
    }

    /**
     * {@inheritDoc}
     * <p>
     * This implementation ignores the argument and does
     * nothing.
     */
    @Override
    public void setTipText(String tipText)
    {
        ;
    }
}
