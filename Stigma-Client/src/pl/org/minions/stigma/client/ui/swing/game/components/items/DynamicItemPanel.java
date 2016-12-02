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

import javax.swing.event.AncestorEvent;
import javax.swing.event.AncestorListener;

import pl.org.minions.stigma.client.Client;
import pl.org.minions.stigma.client.ui.event.listeners.ItemDataChangedListener;
import pl.org.minions.stigma.game.actor.Actor;
import pl.org.minions.stigma.game.item.Armor;
import pl.org.minions.stigma.game.item.Equipment;
import pl.org.minions.stigma.game.item.Item;
import pl.org.minions.stigma.game.item.Weapon;

/**
 * An extension of {@link ItemPanel} that reacts to item
 * data changes.
 */
public class DynamicItemPanel extends ItemPanel implements
                                               ItemDataChangedListener
{
    /*
     * <i>Note, that dynamic item panel is only supposed to
     * be created for complete items, so loading panel
     * doesn't have to be handled.</i>
     */
    //TODO: should it also react to changes to player actor?

    private static final long serialVersionUID = 1L;

    /**
     * Constructor.
     * @param item
     *            item to show
     */
    public DynamicItemPanel(Item item)
    {
        super(item);

        addAncestorListener(new AncestorListener()
        {

            /** {@inheritDoc} */
            @Override
            public void ancestorRemoved(AncestorEvent event)
            {
                Client.globalInstance()
                      .uiEventRegistry()
                      .removeItemDataChangedListener(DynamicItemPanel.this);
            }

            /** {@inheritDoc} */
            @Override
            public void ancestorAdded(AncestorEvent event)
            {
                Client.globalInstance()
                      .uiEventRegistry()
                      .addItemDataChangedListener(DynamicItemPanel.this);
            }

            /** {@inheritDoc} */
            @Override
            public void ancestorMoved(AncestorEvent event)
            {

            }
        });
    }

    /** {@inheritDoc} */
    @Override
    public void itemDataChanged(int id)
    {
        if (id != getItemId())
            return;

        final Actor playerActor = Client.globalInstance().getPlayerActor();
        final Item item = Client.globalInstance().getWorld().getItem(id);

        if (getDescriptionPanel() != null)
            getDescriptionPanel().updateData(item);
        if (getHeaderPanel() != null)
            getHeaderPanel().updateData(item);
        if (getWeaponPanel() != null)
            getWeaponPanel().updateData((Weapon) item, playerActor);
        if (getArmorPanel() != null)
            getArmorPanel().setResistances(((Armor) item).getResistances());
        if (getRequirementsPanel() != null)
            getRequirementsPanel().updateData((Equipment) item, playerActor);

        updateSize();

    }
}
