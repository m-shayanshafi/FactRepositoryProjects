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

import java.awt.Dimension;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import pl.org.minions.stigma.client.Client;
import pl.org.minions.stigma.game.actor.Actor;
import pl.org.minions.stigma.game.item.Armor;
import pl.org.minions.stigma.game.item.Equipment;
import pl.org.minions.stigma.game.item.Item;
import pl.org.minions.stigma.game.item.Weapon;
import pl.org.minions.stigma.game.item.type.ItemType.ItemKind;

/**
 * Panel which presents (or hides) specific item details
 * sections regarding to: item kind and item type details.
 */
public class ItemPanel extends JPanel
{
    private static final long serialVersionUID = 1L;

    private static final EmptyBorder BORDER = new EmptyBorder(5, 5, 5, 5);

    private static final int MIN_WIDTH = 300;
    private static final int MIN_HEIGHT = 50;

    private ItemDescriptionPanel descriptionPanel;
    private ItemHeaderPanel headerPanel;
    private WeaponPanel weaponPanel;
    private ResistancesPanel armorPanel;
    private RequirementsPanel requirementsPanel;
    private LoadingItemTypePanel loadingPanel;

    private int itemId;

    /**
     * Default constructor.
     * @param item
     *            item to show
     */
    public ItemPanel(Item item)
    {
        itemId = item.getId();

        headerPanel = new ItemHeaderPanel(item);

        if (!item.isComplete())
        {
            loadingPanel = new LoadingItemTypePanel(item);
        }
        else
        {
            ItemKind ik = item.getKind();
            final Actor playerActor = Client.globalInstance().getPlayerActor();

            switch (ik)
            {
                case ARMOR:
                    this.armorPanel =
                            new ResistancesPanel(((Armor) item).getResistances());
                    this.requirementsPanel =
                            new RequirementsPanel((Equipment) item, playerActor);
                    break;
                case OTHER:
                    //here do nothing
                    break;
                case WEAPON:
                    this.weaponPanel =
                            new WeaponPanel((Weapon) item, playerActor);
                    this.requirementsPanel =
                            new RequirementsPanel((Equipment) item, playerActor);
                    break;
                default:
                    assert "Unsupported item kind: " + ik == null;
                    break;
            }

            if (item.getType().getDescription() != null
                && !item.getType().getDescription().isEmpty())
            {
                this.descriptionPanel = new ItemDescriptionPanel(item);
            }
        }
        initialize();
        postInit();
    }

    /**
     * Here panel controls are initialized.
     */
    private void initialize()
    {
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        this.add(headerPanel, null);
        this.add(Box.createVerticalStrut(2), null);

        if (loadingPanel != null)
        {
            this.add(loadingPanel, null);
            return;
        }
        if (weaponPanel != null)
        {
            this.add(weaponPanel, null);
        }
        if (armorPanel != null)
        {
            armorPanel.setAlignmentX(RIGHT_ALIGNMENT);
            this.add(armorPanel, null);
        }
        if (requirementsPanel != null)
        {
            this.add(Box.createVerticalStrut(2), null);
            this.add(requirementsPanel, null);
        }
        if (descriptionPanel != null)
        {
            this.add(Box.createVerticalStrut(2), null);
            this.add(descriptionPanel, null);
        }
    }

    private void postInit()
    {
        setBorder(BORDER);

        updateSize();
    }

    /**
     * Updates preferred and current size and actual size to
     * be no less
     * than <code>[MIN_WIDTH, MIN_HEIGHT]</code> .
     */
    protected void updateSize()
    {
        Dimension preferredSize = getPreferredSize();
        preferredSize.width = Math.max(preferredSize.width, MIN_WIDTH);
        preferredSize.height = Math.max(preferredSize.height, MIN_HEIGHT);
        setPreferredSize(preferredSize);
        setSize(preferredSize);
    }

    /**
     * Returns descriptionPanel.
     * @return descriptionPanel
     */
    protected ItemDescriptionPanel getDescriptionPanel()
    {
        return descriptionPanel;
    }

    /**
     * Returns headerPanel.
     * @return headerPanel
     */
    protected ItemHeaderPanel getHeaderPanel()
    {
        return headerPanel;
    }

    /**
     * Returns weaponPanel.
     * @return weaponPanel
     */
    protected WeaponPanel getWeaponPanel()
    {
        return weaponPanel;
    }

    /**
     * Returns armorPanel.
     * @return armorPanel
     */
    protected ResistancesPanel getArmorPanel()
    {
        return armorPanel;
    }

    /**
     * Returns requirementsPanel.
     * @return requirementsPanel
     */
    protected RequirementsPanel getRequirementsPanel()
    {
        return requirementsPanel;
    }

    /**
     * Returns loadingPanel.
     * @return loadingPanel
     */
    protected LoadingItemTypePanel getLoadingPanel()
    {
        return loadingPanel;
    }

    /**
     * Returns itemId.
     * @return itemId
     */
    protected int getItemId()
    {
        return itemId;
    }
}
