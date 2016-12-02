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
package pl.org.minions.stigma.editor.actions;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.UIManager.LookAndFeelInfo;

import pl.org.minions.stigma.editor.gui.MainFrame;
import pl.org.minions.utils.logger.Log;

/**
 * Action to choose LAF.
 */
public class ChooseLafAction extends AbstractAction
{
    private static final long serialVersionUID = 1L;

    private LookAndFeelInfo info;

    /**
     * Default constructor.
     * @param info
     *            info
     */
    public ChooseLafAction(LookAndFeelInfo info)
    {
        super(info.getName());
        this.info = info;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void actionPerformed(ActionEvent event)
    {
        try
        {
            UIManager.setLookAndFeel(info.getClassName());
            SwingUtilities.updateComponentTreeUI(MainFrame.getMainFrame());
        }
        catch (UnsupportedLookAndFeelException e)
        {
            Log.logger.warn("Look and feel not supported.", e);
        }
        catch (ClassNotFoundException e)
        {
            Log.logger.warn("Look and feel class not found.", e);
        }
        catch (InstantiationException e)
        {
            Log.logger.warn("Look and feel class instantiation error.", e);
        }
        catch (IllegalAccessException e)
        {
            Log.logger.warn("Look and feel class or initializer inaccessible.",
                            e);
        }
        Log.logger.debug("Look and feel changed to " + info.getName());
    }
}
