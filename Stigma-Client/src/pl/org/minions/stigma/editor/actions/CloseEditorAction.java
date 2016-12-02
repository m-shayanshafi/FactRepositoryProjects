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
import java.awt.event.KeyEvent;

import javax.swing.AbstractAction;
import javax.swing.JOptionPane;

import pl.org.minions.stigma.editor.gui.MainFrame;
import pl.org.minions.stigma.editor.resourceset.ResourceSetModel;
import pl.org.minions.utils.i18n.Translated;

/**
 * Action used to close the editor.
 */
public class CloseEditorAction extends AbstractAction
{
    private static final long serialVersionUID = 1L;

    @Translated
    private static String ACTION_NAME = "Exit";
    @Translated
    private static String ACTION_DESC = "Exit description";
    @Translated
    private static String SAVE_FILES_WARNING_TITLE = "Confirm exit";
    @Translated
    private static String SAVE_FILES_WARNING_MESSAGE =
            "You haven't saved the resouce set yet. Do you wish to do so?";

    /**
     * Constructor.
     */
    protected CloseEditorAction()
    {
        super(ACTION_NAME);
        putValue(SHORT_DESCRIPTION, ACTION_DESC);
        putValue(MNEMONIC_KEY, KeyEvent.VK_X);
    }

    /** {@inheritDoc} */
    @Override
    public void actionPerformed(ActionEvent e)
    {
        //TODO: ask if the user wants to save his work
        if (ResourceSetModel.getInstance().getWasModified())
        {
            int result =
                    JOptionPane.showConfirmDialog(MainFrame.getMainFrame(),
                                                  SAVE_FILES_WARNING_MESSAGE,
                                                  SAVE_FILES_WARNING_TITLE,
                                                  JOptionPane.YES_NO_CANCEL_OPTION,
                                                  JOptionPane.WARNING_MESSAGE);
            switch (result)
            {
                case JOptionPane.YES_OPTION:
                    ActionContainer.SAVE_RESOURCE_SET.actionPerformed(e);
                    break;
                case JOptionPane.NO_OPTION:
                    break;
                default:
                    return;
            }
        }
        System.exit(0);
    }
}
