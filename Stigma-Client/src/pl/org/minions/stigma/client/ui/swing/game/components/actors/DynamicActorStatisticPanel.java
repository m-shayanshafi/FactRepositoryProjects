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
package pl.org.minions.stigma.client.ui.swing.game.components.actors;

import javax.swing.event.AncestorEvent;
import javax.swing.event.AncestorListener;

import org.w3c.dom.events.UIEvent;

import pl.org.minions.stigma.client.Client;
import pl.org.minions.stigma.client.ui.event.UiEventRegistry;
import pl.org.minions.stigma.client.ui.event.listeners.ActorDataChangedListener;
import pl.org.minions.stigma.client.ui.event.listeners.ItemEquippedListener;
import pl.org.minions.stigma.client.ui.event.listeners.ItemTypeLoadedListener;
import pl.org.minions.stigma.client.ui.event.listeners.ItemUnEquippedListener;
import pl.org.minions.stigma.game.actor.Actor;
import pl.org.minions.stigma.game.event.item.ItemEquipped;
import pl.org.minions.stigma.game.event.item.ItemUnEquipped;

/**
 * A version of {@link ActorStatisticPanel} that reacts to
 * {@link UIEvent UI events} that affect the displayed
 * actor.
 */
public class DynamicActorStatisticPanel extends ActorStatisticPanel implements
                                                                   ActorDataChangedListener,
                                                                   ItemTypeLoadedListener,
                                                                   ItemEquippedListener,
                                                                   ItemUnEquippedListener
{
    private static final long serialVersionUID = 1L;

    private int actorId;

    /**
     * Constructor.
     * @param a
     *            actor to display
     */
    public DynamicActorStatisticPanel(Actor a)
    {
        super(a);
        this.actorId = a.getId();

        addAncestorListener(new AncestorListener()
        {

            @Override
            public void ancestorRemoved(AncestorEvent event)
            {
                final UiEventRegistry uiEvtReg =
                        Client.globalInstance().uiEventRegistry();
                uiEvtReg.removeActorDataChangedListener(DynamicActorStatisticPanel.this);
                uiEvtReg.removeItemTypeLoadedListener(DynamicActorStatisticPanel.this);
                uiEvtReg.removeItemEquippedListener(DynamicActorStatisticPanel.this);
                uiEvtReg.removeItemUnEquippedListener(DynamicActorStatisticPanel.this);
            }

            @Override
            public void ancestorMoved(AncestorEvent event)
            {
                //woah
            }

            @Override
            public void ancestorAdded(AncestorEvent event)
            {
                final UiEventRegistry uiEvtReg =
                        Client.globalInstance().uiEventRegistry();
                uiEvtReg.addActorDataChangedListener(DynamicActorStatisticPanel.this);
                uiEvtReg.addItemTypeLoadedListener(DynamicActorStatisticPanel.this);
                uiEvtReg.addItemEquippedListener(DynamicActorStatisticPanel.this);
                uiEvtReg.addItemUnEquippedListener(DynamicActorStatisticPanel.this);
            }
        });
    }

    /** {@inheritDoc} */
    @Override
    public void actorDataChanged(int id)
    {
        if (id == actorId)
            update(Client.globalInstance().getWorld().getActor(actorId));
    }

    /** {@inheritDoc} */
    @Override
    public void itemEquipped(ItemEquipped event, boolean playerActor)
    {
        if (event.getAffectedActors().contains(actorId))
            update(Client.globalInstance().getWorld().getActor(actorId));

        //TODO: should also react to item data changes?
    }

    /** {@inheritDoc} */
    @Override
    public void itemUnEquipped(ItemUnEquipped event, boolean playerActor)
    {
        if (event.getAffectedActors().contains(actorId))
            update(Client.globalInstance().getWorld().getActor(actorId));
    }

    /** {@inheritDoc} */
    @Override
    public void itemTypeLoaded(short id)
    {

        update(Client.globalInstance().getWorld().getActor(actorId));
    }

}
