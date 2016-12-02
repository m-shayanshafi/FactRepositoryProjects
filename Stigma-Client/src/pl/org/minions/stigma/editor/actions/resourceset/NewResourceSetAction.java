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
package pl.org.minions.stigma.editor.actions.resourceset;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import javax.swing.AbstractAction;

import pl.org.minions.stigma.editor.gui.GUIConstants;
import pl.org.minions.utils.i18n.Translated;

/**
 * Action used to create a new resource set.
 */
public class NewResourceSetAction extends AbstractAction
{
    private static final long serialVersionUID = 1L;

    @Translated
    private static String ACTION_NAME = "New resource set";
    @Translated
    private static String ACTION_DESC = "New resource set action";

    /**
     * Constructor.
     */
    public NewResourceSetAction()
    {
        super(ACTION_NAME);
        putValue(SHORT_DESCRIPTION, ACTION_DESC);
        putValue(MNEMONIC_KEY, KeyEvent.VK_N);
        putValue(LARGE_ICON_KEY, GUIConstants.NEW_ICON);
    }

    /** {@inheritDoc} */
    @Override
    public void actionPerformed(ActionEvent e)
    {
        // TODO Auto-generated method stub

    }

}
