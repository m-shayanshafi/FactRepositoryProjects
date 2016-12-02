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

import java.awt.Color;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;

import pl.org.minions.stigma.client.Client;
import pl.org.minions.stigma.client.images.ImageDB;
import pl.org.minions.stigma.client.images.ImageProxy;
import pl.org.minions.stigma.client.ui.VisualizationGlobals;
import pl.org.minions.stigma.client.ui.swing.game.components.ImageProxyComponent;
import pl.org.minions.stigma.client.ui.swing.game.tooltips.ItemToolTip;
import pl.org.minions.stigma.game.actor.Actor;
import pl.org.minions.stigma.game.item.Equipment;
import pl.org.minions.stigma.game.item.Item;
import pl.org.minions.stigma.game.item.type.ItemType.ItemKind;

/**
 * Panel used to draw {@link ItemToolTip}.
 */
public class ItemHeaderPanel extends JPanel
{
    private static final long serialVersionUID = 1L;

    private JLabel itemNameLabel;
    private ImageProxyComponent itemIconComponent;
    private WeightValuePanel weightValuePanel;
    private ProficienciesListPanel proficienciesPanel;

    private JPanel namePanel;

    /**
     * Default constructor.
     * @param item
     *            Item to be drawn on panel
     */
    public ItemHeaderPanel(Item item)
    {
        if (item.getKind() == ItemKind.ARMOR
            || item.getKind() == ItemKind.WEAPON)
            proficienciesPanel = new ProficienciesListPanel((Equipment) item);

        weightValuePanel = new WeightValuePanel(item);

        initialize();
        postInit(item);
        this.setVisible(true);
    }

    private void initialize()
    {
        // CHECKSTYLE:OFF
        this.setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
        itemIconComponent = new ImageProxyComponent(null);
        itemIconComponent.setDefaultImage(VisualizationGlobals.DEFAULT_ITEM_IMAGE);
        this.add(getNamePanel(), null);
        this.add(Box.createHorizontalGlue(), null);
        this.add(weightValuePanel, null);
        this.add(Box.createHorizontalStrut(5), null);
        this.add(itemIconComponent, null);
        // CHECKSTYLE:ON
    }

    /**
     * Returns image proxy for item or <code>null</code> if
     * item is not loaded
     * @param item
     *            item to get image of
     * @return proxy object of image or <code>null</code> if
     *         not found
     */
    private ImageProxy getImageProxy(Item item)
    {
        ImageProxy ip = null;
        if (item.isComplete() && item.getType().getInventoryIcon() != null
            && !item.getType().getInventoryIcon().isEmpty())
        {
            ip =
                    ImageDB.globalInstance()
                           .getItemIcon(item.getKind(),
                                        item.getType().getInventoryIcon());
        }
        return ip;
    }

    /**
     * Function added to remove Visual Editor warnings.
     * @param item
     *            item to set values from
     */
    private void postInit(Item item)
    {
        updateData(item);
    }

    /**
     * Updates displayed data.
     * @param item
     *            item to set values from
     */
    void updateData(Item item)
    {
        itemNameLabel.setText(item.getName());
        itemIconComponent.setProxy(getImageProxy(item));

        switch (item.getKind())
        {
            case ARMOR:
            case WEAPON:
                final Actor player = Client.globalInstance().getPlayerActor();
                if (!player.canEquip(item))
                    itemNameLabel.setForeground(Color.red);
                break;
            default:
                break;
        }
    }

    /**
     * This method initializes namePanel
     * @return javax.swing.JPanel
     */
    private JPanel getNamePanel()
    {
        if (namePanel == null)
        {
            itemNameLabel = new JLabel("NAME");
            itemNameLabel.setName("BigLabel");
            namePanel = new JPanel();
            namePanel.setLayout(new BoxLayout(getNamePanel(), BoxLayout.Y_AXIS));
            namePanel.setAlignmentX(LEFT_ALIGNMENT);
            namePanel.add(itemNameLabel, null);
            if (proficienciesPanel != null)
            {
                proficienciesPanel.setAlignmentX(LEFT_ALIGNMENT);
                namePanel.add(proficienciesPanel, null);
            }
            namePanel.add(Box.createVerticalGlue(), null);
        }
        return namePanel;
    }
} //  @jve:decl-index=0:visual-constraint="17,13"
