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
import java.io.File;

import javax.swing.AbstractAction;
import javax.swing.JFileChooser;

import pl.org.minions.stigma.editor.gui.GUIConstants;
import pl.org.minions.stigma.editor.gui.MainFrame;
import pl.org.minions.stigma.editor.resourceset.ResourceSetModel;
import pl.org.minions.utils.i18n.Translated;

/**
 * Action used to load the resource set from a file.
 */
public class LoadResourceSetAction extends AbstractAction
{
    private static final long serialVersionUID = 1L;

    @Translated
    private static String ACTION_NAME = "Load resource set";
    @Translated
    private static String ACTION_DESC = "Load resource set action";

    /**
     * Constructor.
     */
    public LoadResourceSetAction()
    {
        super(ACTION_NAME);
        putValue(SHORT_DESCRIPTION, ACTION_DESC);
        putValue(MNEMONIC_KEY, KeyEvent.VK_L);
        putValue(LARGE_ICON_KEY, GUIConstants.LOAD_ICON);
    }

    /** {@inheritDoc} */
    @Override
    public void actionPerformed(ActionEvent e)
    {
        JFileChooser fc = new JFileChooser();
        fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        fc.setCurrentDirectory(new File("c:/ziemek/minions/stigma/res")); // TODO: remove this line...
        int returnVal = fc.showOpenDialog(MainFrame.getMainFrame());

        if (returnVal == JFileChooser.APPROVE_OPTION)
        {
            String path = fc.getSelectedFile().getPath();
            File f = new File(path);
            ResourceSetModel.getInstance().loadResourceSet(f.toURI());
        }
    }

}
