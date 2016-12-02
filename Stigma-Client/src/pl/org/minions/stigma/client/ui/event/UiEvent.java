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
package pl.org.minions.stigma.client.ui.event;

import java.awt.Component;
import java.awt.event.HierarchyEvent;
import java.awt.event.HierarchyListener;
import java.util.LinkedList;
import java.util.List;

import pl.org.minions.stigma.client.Client;
import pl.org.minions.stigma.client.ui.event.listeners.ActorAddedListener;
import pl.org.minions.stigma.client.ui.event.listeners.ActorChangedMapListener;
import pl.org.minions.stigma.client.ui.event.listeners.ActorDataChangedListener;
import pl.org.minions.stigma.client.ui.event.listeners.ActorRemovedListener;
import pl.org.minions.stigma.client.ui.event.listeners.ActorWalkListener;
import pl.org.minions.stigma.client.ui.event.listeners.CooldownChangedListener;
import pl.org.minions.stigma.client.ui.event.listeners.ItemAddedListener;
import pl.org.minions.stigma.client.ui.event.listeners.ItemDataChangedListener;
import pl.org.minions.stigma.client.ui.event.listeners.ItemDroppedListener;
import pl.org.minions.stigma.client.ui.event.listeners.ItemEquippedListener;
import pl.org.minions.stigma.client.ui.event.listeners.ItemPickedUpListener;
import pl.org.minions.stigma.client.ui.event.listeners.ItemTypeLoadedListener;
import pl.org.minions.stigma.client.ui.event.listeners.ItemUnEquippedListener;
import pl.org.minions.stigma.game.command.Command;
import pl.org.minions.stigma.game.command.CommandType;
import pl.org.minions.stigma.game.command.request.Move;
import pl.org.minions.stigma.game.data.WorldData;
import pl.org.minions.stigma.game.data.WorldDataType;
import pl.org.minions.stigma.game.data.actor.ActorCooldownChanged;
import pl.org.minions.stigma.game.event.Event;
import pl.org.minions.stigma.game.event.EventType;
import pl.org.minions.stigma.game.event.actor.ActorAdded;
import pl.org.minions.stigma.game.event.actor.ActorChangedMap;
import pl.org.minions.stigma.game.event.actor.ActorRemoved;
import pl.org.minions.stigma.game.event.actor.ActorWalk;
import pl.org.minions.stigma.game.event.item.ItemAdded;
import pl.org.minions.stigma.game.event.item.ItemDropped;
import pl.org.minions.stigma.game.event.item.ItemEquipped;
import pl.org.minions.stigma.game.event.item.ItemPickedUp;
import pl.org.minions.stigma.game.event.item.ItemUnEquipped;
import pl.org.minions.utils.logger.Log;

/**
 * Class containing all UI events available in system. It is
 * used only internally for filtering events and aggregating
 * listeners.
 */
enum UiEvent
{
    EQUIPPED_ITEM(EventType.ITEM_EQUIPPED)
    {
        /** {@inheritDoc} */
        @Override
        public void notify(UiEventListener listener,
                           Object event,
                           boolean playerActor,
                           Command parentCommand,
                           int id)
        {
            ((ItemEquippedListener) listener).itemEquipped((ItemEquipped) event,
                                                           playerActor);
        }
    },
    UNEQUIPPED_ITEM(EventType.ITEM_UNEQUIPPED)
    {
        /** {@inheritDoc} */
        @Override
        public void notify(UiEventListener listener,
                           Object event,
                           boolean playerActor,
                           Command parentCommand,
                           int id)
        {
            ((ItemUnEquippedListener) listener).itemUnEquipped((ItemUnEquipped) event,
                                                               playerActor);
        }
    },
    ITEM_TYPE_LOADED((WorldDataType) null)
    {
        /** {@inheritDoc} */
        @Override
        public void notify(UiEventListener listener,
                           Object event,
                           boolean playerActor,
                           Command parentCommand,
                           int id)
        {
            ((ItemTypeLoadedListener) listener).itemTypeLoaded((short) id);
        }
    },
    ITEM_DATA(WorldDataType.ITEM_INFO)
    {
        /** {@inheritDoc} */
        @Override
        public void notify(UiEventListener listener,
                           Object event,
                           boolean playerActor,
                           Command parentCommand,
                           int id)
        {
            ((ItemDataChangedListener) listener).itemDataChanged(id);
        }
    },
    ITEM_DROPPED(EventType.ITEM_DROPPED)
    {
        /** {@inheritDoc} */
        @Override
        public void notify(UiEventListener listener,
                           Object event,
                           boolean playerActor,
                           Command parentCommand,
                           int id)
        {
            ((ItemDroppedListener) listener).itemDropped((ItemDropped) event,
                                                         playerActor);
        }
    },
    ITEM_PICKED_UP(EventType.ITEM_PICKED_UP)
    {
        /** {@inheritDoc} */
        @Override
        public void notify(UiEventListener listener,
                           Object event,
                           boolean playerActor,
                           Command parentCommand,
                           int id)
        {
            ((ItemPickedUpListener) listener).itemPickedUp((ItemPickedUp) event,
                                                           playerActor);
        }
    },
    ITEM_ADDED(EventType.ITEM_ADDED)
    {
        /** {@inheritDoc} */
        @Override
        public void notify(UiEventListener listener,
                           Object event,
                           boolean playerActor,
                           Command parentCommand,
                           int id)
        {
            ((ItemAddedListener) listener).itemAdded((ItemAdded) event);
        }
    },
    ACTOR_WALK(EventType.ACTOR_WALK)
    {
        /** {@inheritDoc} */
        @Override
        protected void notify(UiEventListener listener,
                              Object event,
                              boolean playerActor,
                              Command parentCommand,
                              int id)
        {
            if (parentCommand.getType() == CommandType.MOVE)
                ((ActorWalkListener) listener).actorWalked((ActorWalk) event,
                                                           (Move) parentCommand,
                                                           playerActor);
            else
                ((ActorWalkListener) listener).actorWalked((ActorWalk) event,
                                                           null,
                                                           playerActor);
        }
    },
    ACTOR_CHANGED_MAP(EventType.ACTOR_CHANGED_MAP)
    {
        /** {@inheritDoc} */
        @Override
        public void notify(UiEventListener listener,
                           Object event,
                           boolean playerActor,
                           Command parentCommand,
                           int id)
        {
            ((ActorChangedMapListener) listener).actorChangedMap((ActorChangedMap) event,
                                                                 playerActor);
        }
    },
    ACTOR_ADDED(EventType.ACTOR_ADDED)
    {
        /** {@inheritDoc} */
        @Override
        public void notify(UiEventListener listener,
                           Object event,
                           boolean playerActor,
                           Command parentCommand,
                           int id)
        {
            ((ActorAddedListener) listener).actorAdded((ActorAdded) event,
                                                       playerActor);
        }
    },

    ACTOR_REMOVED(EventType.ACTOR_REMOVED)
    {
        /** {@inheritDoc} */
        @Override
        public void notify(UiEventListener listener,
                           Object event,
                           boolean playerActor,
                           Command parentCommand,
                           int id)
        {
            ((ActorRemovedListener) listener).actorRemoved((ActorRemoved) event);
        }
    },

    ACTOR_FAST_DATA(WorldDataType.ACTOR_FAST_INFO)
    {
        @Override
        public void notify(UiEventListener listener,
                           Object event,
                           boolean playerActor,
                           Command parentCommand,
                           int id)
        {
            ((ActorDataChangedListener) listener).actorDataChanged(id);
        }
    },
    ACTOR_DATA(WorldDataType.ACTOR_INFO)
    {
        @Override
        public void notify(UiEventListener listener,
                           Object event,
                           boolean playerActor,
                           Command parentCommand,
                           int id)
        {
            ((ActorDataChangedListener) listener).actorDataChanged(id);
        }
    },
    COOLDOWN_CHANGED(WorldDataType.ACTOR_COOLDOWN_CHANGED)
    {
        @Override
        public void notify(UiEventListener listener,
                           Object object,
                           boolean playerActor,
                           Command parentCommand,
                           int id)
        {
            assert playerActor;
            ((CooldownChangedListener) listener).cooldownChanged(((ActorCooldownChanged) object).getCooldownChange());
        }
    },
    ;

    public static final UiEvent[] ELEMENTS = UiEvent.class.getEnumConstants();

    private EventType eventType;
    private WorldDataType dataType;
    private List<UiEventListener> listeners = new LinkedList<UiEventListener>();

    private UiEvent(EventType eventType)
    {
        this.eventType = eventType;
    }

    private UiEvent(WorldDataType dataType)
    {
        this.dataType = dataType;
    }

    /**
     * Called to notify given listener. Should be
     * re-implemented to provide proper casts for real
     * implementation of listeners.
     * @param listener
     *            listener which should be called
     * @param object
     *            object that caused this UI event, may be
     *            game event, world data, or {@code null}.
     * @param playerActor
     *            {@code true} when player actor was
     *            affected
     * @param parentCommand
     *            game event's parent game command
     * @param id
     *            id of object that caused event, important
     *            only when {@code event} is {@code null}
     */
    protected abstract void notify(UiEventListener listener,
                                   Object object,
                                   boolean playerActor,
                                   Command parentCommand,
                                   int id);

    /**
     * Returns {@code true} when ui-event is based on
     * game-event.
     * @return {@code true} when ui-event is based on
     *         game-event.
     */
    public boolean isEventBased()
    {
        return eventType != null;
    }

    /**
     * Returns game event type that may cause this event.
     * @return game event type that may cause this event.
     */
    public EventType getEventType()
    {
        return eventType;
    }

    /**
     * Returns game data type that may cause this event.
     * @return game data type that may cause this event.
     */
    public WorldDataType getDataType()
    {
        return dataType;
    }

    /**
     * Adds listener.
     * @param listener
     *            listener to add
     */
    public void addListener(final UiEventListener listener)
    {
        listeners.add(listener);

        if (listener instanceof Component) // ugly, but it seems to be the only way
        {
            Component component = (Component) listener;
            component.addHierarchyListener(new HierarchyListener()
            {
                @Override
                public void hierarchyChanged(HierarchyEvent e)
                {
                    if ((e.getChangeFlags() & HierarchyEvent.DISPLAYABILITY_CHANGED) == 0)
                        return;

                    if (!e.getComponent().isDisplayable())
                    {
                        Log.logger.debug("Removing non-displayed UiEventListener");
                        listeners.remove(listener);
                    }
                }
            });
        }
    }

    /**
     * Removes listener.
     * @param listener
     *            listener to remove
     */
    public void removeListener(UiEventListener listener)
    {
        listeners.remove(listener);
    }

    /**
     * Notifies all listeners registered for given event.
     * @param event
     *            game event that caused this event (may be
     *            used in notification).
     * @param command
     *            event's parent command
     */
    public void notifyAllListeners(Event event, Command command)
    {
        assert eventType != null;

        boolean playerActor =
                event.getAffectedActors().contains(Client.globalInstance()
                                                         .getPlayerActor()
                                                         .getId());

        for (UiEventListener listener : listeners)
            notify(listener, event, playerActor, command, (short) 0);
    }

    /**
     * Notifies all listeners registered for given event.
     * @param data
     *            data that caused this event
     * @param id
     *            id of object that caused this event (may
     *            be used in notification).
     */
    public void notifyAllListeners(WorldData data, int id)
    {
        assert eventType == null;

        boolean playerActor =
                id == Client.globalInstance().getPlayerActor().getId();
        for (UiEventListener listener : listeners)
            notify(listener, data, playerActor, null, id);
    }

    /**
     * Checks if this event has any registered listeners.
     * @return {@code true} when this event has any
     *         registered events
     */
    public boolean hasListeners()
    {
        return !listeners.isEmpty();
    }
}
