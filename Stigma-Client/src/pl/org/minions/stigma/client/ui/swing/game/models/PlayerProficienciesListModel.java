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
package pl.org.minions.stigma.client.ui.swing.game.models;

import java.util.Arrays;

import javax.swing.AbstractListModel;
import javax.swing.ListModel;
import javax.swing.event.ListDataListener;

import pl.org.minions.stigma.client.Client;
import pl.org.minions.stigma.client.ui.event.listeners.ActorAddedListener;
import pl.org.minions.stigma.client.ui.event.listeners.ActorDataChangedListener;
import pl.org.minions.stigma.game.actor.Actor;
import pl.org.minions.stigma.game.actor.Proficiency;
import pl.org.minions.stigma.game.event.actor.ActorAdded;

/**
 * A {@link ListModel} implementation that provides a list
 * of player proficiencies.
 */
public class PlayerProficienciesListModel extends AbstractListModel
{
    private final class EventListener implements
                                     ActorAddedListener,
                                     ActorDataChangedListener
    {
        /** {@inheritDoc} */
        @Override
        public void actorDataChanged(int id)
        {
            final Actor playerActor = Client.globalInstance().getPlayerActor();
            if (id == playerActor.getId())
                updateCachedIds(playerActor);
        }

        /** {@inheritDoc} */
        @Override
        public void actorAdded(ActorAdded event, boolean playerActor)
        {
            if (playerActor)
                updateCachedIds(event.getActor());
        }
    }

    private static final long serialVersionUID = 1L;
    private static final Short[] EMPTY_SHORT_ARRAY = new Short[0];
    private Short[] cachedIds = {};
    private EventListener eventListener;

    /** {@inheritDoc} */
    @Override
    public void removeListDataListener(ListDataListener l)
    {
        super.removeListDataListener(l);

        if (getListDataListeners().length == 0)
        {
            final Client client = Client.globalInstance();
            if (client != null)
                client.uiEventRegistry().removeFromAll(eventListener);
        }
    }

    /** {@inheritDoc} */
    @Override
    public void addListDataListener(ListDataListener l)
    {
        super.addListDataListener(l);

        if (getListDataListeners().length == 1)
        {
            final Client client = Client.globalInstance();
            if (client != null)
            {
                eventListener = new EventListener();
                client.uiEventRegistry().addActorAddedListener(eventListener);
            }
        }
    }

    /** {@inheritDoc} */
    @Override
    public Object getElementAt(int index)
    {
        final Client client = Client.globalInstance();
        if (client == null) //So that Visual Editor doesn't go crazy.
            return "";

        Proficiency proficiency =
                client.getProficiencyDB().getProficiency(cachedIds[index]);

        return proficiency.getName();
    }

    /** {@inheritDoc} */
    @Override
    public int getSize()
    {
        return cachedIds.length;
    }

    private void updateCachedIds(Actor actor)
    {
        final Short[] playerProficiencies =
                actor.getProficiencies().toArray(EMPTY_SHORT_ARRAY);
        if (Arrays.equals(cachedIds, playerProficiencies))
            return;
        fireIntervalRemoved(this, 0, cachedIds.length);
        cachedIds = playerProficiencies.clone();
        fireIntervalAdded(this, 0, cachedIds.length);
    }

}
