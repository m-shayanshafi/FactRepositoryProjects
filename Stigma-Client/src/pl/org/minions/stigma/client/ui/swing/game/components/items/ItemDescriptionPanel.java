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
package pl.org.minions.stigma.client.ui.swing.game.components.items;

import java.awt.Component;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.border.EmptyBorder;

import pl.org.minions.stigma.game.item.Item;
import pl.org.minions.utils.i18n.Translated;

/**
 * Panel used to draw description details for item on
 * {@link ItemPanel}.
 */
public class ItemDescriptionPanel extends JPanel
{
    private static final long serialVersionUID = 1L;

    @Translated
    private static String DESCRIPTION = "Description";

    private JLabel description;
    private JTextArea descriptionValue;

    /**
     * Default constructor.
     * @param item
     *            Item which detailed data will be displayed
     */
    public ItemDescriptionPanel(Item item)
    {
        initialize();
        postInit(item);
    }

    private void postInit(Item item)
    {
        updateData(item);
    }

    /**
     * Updates the description from given item.
     * @param item
     *            Item which detailed data will be displayed
     */
    void updateData(Item item)
    {
        descriptionValue.setText(item.getType().getDescription());
    }

    private void initialize()
    {
        //CHECKSTYLE:OFF
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        description = new JLabel(DESCRIPTION);
        description.setAlignmentX(Component.LEFT_ALIGNMENT);
        descriptionValue = new JTextArea();
        descriptionValue.setEditable(false);
        descriptionValue.setAlignmentX(Component.LEFT_ALIGNMENT);
        descriptionValue.setBorder(new EmptyBorder(0, 10, 0, 0));

        this.setAlignmentX(Component.CENTER_ALIGNMENT);
        this.add(description, null);
        this.add(Box.createVerticalStrut(2), null);
        this.add(descriptionValue, null);
        this.add(Box.createVerticalGlue(), null);
    }
} //  @jve:decl-index=0:visual-constraint="10,10"
