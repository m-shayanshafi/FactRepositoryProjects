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

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

import javax.swing.JToggleButton;

import pl.org.minions.stigma.client.ui.swing.StigmaInternalFrame;

/**
 * Subclass of {!link JToggleButton} specialized for
 * toggling {@link StigmaInternalFrame}.
 */
public class ToggleFrameButton extends JToggleButton
{
    private static final long serialVersionUID = 1L;

    /**
     * Sets frame to be toggled by this window.
     * @param frame
     *            frame to be toggled by this button
     */
    public void setToggledFrame(final StigmaInternalFrame frame)
    {
        addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                if (!frame.isVisible())
                {
                    frame.restoreDefaultLocation();
                    frame.setVisible(true);
                }
                else
                    frame.setVisible(false);
            }
        });

        frame.addComponentListener(new ComponentAdapter()
        {
            @Override
            public void componentShown(ComponentEvent e)
            {
                ToggleFrameButton.this.setSelected(true);
            }

            @Override
            public void componentHidden(ComponentEvent e)
            {
                ToggleFrameButton.this.setSelected(false);
            }
        });
    }
}
