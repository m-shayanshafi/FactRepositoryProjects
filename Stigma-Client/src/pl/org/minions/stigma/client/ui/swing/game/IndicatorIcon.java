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

import javax.swing.Icon;
import javax.swing.JButton;

/**
 * Button prepared to be used for indicating actor status on
 * {@link IndicatorIconsPane}.
 */
public class IndicatorIcon extends JButton
{
    private static final long serialVersionUID = 1L;

    /**
     * Creates an indicator icon.
     * @param icon
     *            indicator icon to show
     * @param description
     *            tool-tip text
     */
    public IndicatorIcon(Icon icon, String description)
    {
        super(icon);
        setName(IndicatorIcon.class.getSimpleName());

        setDisabledIcon(icon);
        setToolTipText(description);
        setEnabled(false);

        if (icon == null)
            setText("!");
    }
}
