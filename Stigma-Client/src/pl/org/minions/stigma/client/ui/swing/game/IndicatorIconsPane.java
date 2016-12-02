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
package pl.org.minions.stigma.client.ui.swing.game;

import java.awt.FlowLayout;

import javax.swing.JPanel;

/**
 * Panel used for showing icons indicating actor status.
 * <p>
 * Remember to validate this container's layout after an
 * icon is added or removed.
 * @see IndicatorIcon
 */
public class IndicatorIconsPane extends JPanel
{
    private static final long serialVersionUID = -3396545484338463723L;
    private static final int ICON_GAP = 2;

    /**
     * Creates an instance of IndicatorIconsPane.
     */
    public IndicatorIconsPane()
    {
        super(new FlowLayout(FlowLayout.TRAILING, ICON_GAP, ICON_GAP));
        setName(IndicatorIconsPane.class.getSimpleName());
    }
}
