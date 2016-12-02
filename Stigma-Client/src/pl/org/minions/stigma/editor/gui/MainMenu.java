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
package pl.org.minions.stigma.editor.gui;

import javax.swing.ButtonGroup;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;

import pl.org.minions.stigma.editor.actions.ActionContainer;
import pl.org.minions.stigma.editor.actions.ChooseLafAction;
import pl.org.minions.utils.i18n.Translated;

/**
 * Main menu bar of the editor.
 */
public class MainMenu extends JMenuBar
{
    private static final long serialVersionUID = 1L;

    @Translated
    private static String FILE_LABEL = "File";
    @Translated
    private static String LAF_LABEL = "Look and feel";
    @Translated
    private static String HELP_LABEL = "Help";

    //    @Translated
    //    private static String ABOUT_LABEL = "About";

    /**
     * Constructor.
     */
    public MainMenu()
    {
        super();
        JMenu menu = new JMenu(FILE_LABEL);

        menu.add(ActionContainer.NEW_RESOURCE_SET);
        menu.add(ActionContainer.LOAD_RESOURCE_SET);
        menu.add(ActionContainer.SAVE_RESOURCE_SET);
        menu.addSeparator();
        menu.add(ActionContainer.CLOSE_EDITOR);

        this.add(menu);

        menu = new JMenu(LAF_LABEL);
        //LAF START
        final ButtonGroup buttonGroup = new ButtonGroup();
        final LookAndFeelInfo[] lafs = UIManager.getInstalledLookAndFeels();
        final String currentLafName =
                UIManager.getLookAndFeel().getClass().getName();

        JRadioButtonMenuItem item;
        for (LookAndFeelInfo lookAndFeelInfo : lafs)
        {
            item =
                    new JRadioButtonMenuItem(lookAndFeelInfo.getName(),
                                             lookAndFeelInfo.getClassName()
                                                            .equals(currentLafName));
            item.setAction(new ChooseLafAction(lookAndFeelInfo));
            buttonGroup.add(item);
            menu.add(item);
        }
        //LAF END
        this.add(menu);

        menu = new JMenu(HELP_LABEL);
        menu.add(ActionContainer.ABOUT_EDITOR);
        this.add(menu);
    }
}
