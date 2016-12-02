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

import java.util.EnumMap;
import java.util.LinkedList;
import java.util.List;

import pl.org.minions.stigma.chat.Chat;
import pl.org.minions.stigma.client.ui.event.listeners.ActorAddedListener;
import pl.org.minions.stigma.client.ui.event.listeners.ActorChangedMapListener;
import pl.org.minions.stigma.client.ui.event.listeners.ActorDataChangedListener;
import pl.org.minions.stigma.client.ui.event.listeners.ActorRemovedListener;
import pl.org.minions.stigma.client.ui.event.listeners.ActorWalkListener;
import pl.org.minions.stigma.client.ui.event.listeners.ChatListener;
import pl.org.minions.stigma.client.ui.event.listeners.CooldownChangedListener;
import pl.org.minions.stigma.client.ui.event.listeners.ItemAddedListener;
import pl.org.minions.stigma.client.ui.event.listeners.ItemDataChangedListener;
import pl.org.minions.stigma.client.ui.event.listeners.ItemDroppedListener;
import pl.org.minions.stigma.client.ui.event.listeners.ItemEquippedListener;
import pl.org.minions.stigma.client.ui.event.listeners.ItemPickedUpListener;
import pl.org.minions.stigma.client.ui.event.listeners.ItemTypeLoadedListener;
import pl.org.minions.stigma.client.ui.event.listeners.ItemUnEquippedListener;
import pl.org.minions.stigma.databases.item.client.ItemTypeDBAsync;
import pl.org.minions.stigma.databases.xml.client.XmlCompletionObserver;
import pl.org.minions.stigma.game.command.Command;
import pl.org.minions.stigma.game.data.WorldData;
import pl.org.minions.stigma.game.data.WorldDataType;
import pl.org.minions.stigma.game.event.Event;
import pl.org.minions.stigma.game.event.EventType;

/**
 * Dispatcher of 'UI events'. It converts 'game events' into
 * 'UI events' and generates {@link Runnable} that will send
 * notifications to all registered listeners. This class has
 * protected constructor to enforce double-check of default
 * implementation of dispatching (see
 * {@link #performDispatch(Runnable,boolean)}).
 */
public class UiEventDispatcher implements UiEventRegistry
{
    private EnumMap<EventType, UiEvent> eventListenersRegistry;
    private EnumMap<WorldDataType, UiEvent> dataListenersRegistry;
    private List<ChatListener> chatListeners = new LinkedList<ChatListener>();
    private ItemTypeDBAsync itemTypeDB;
    private CompletionListenerLink itemLink;

    private class CompletionListenerLink implements XmlCompletionObserver
    {
        private UiEvent event;

        public CompletionListenerLink(UiEvent event)
        {
            this.event = event;
        }

        /** {@inheritDoc} */
        @Override
        public void objectComplete(final short id)
        {
            performDispatch(new Runnable()
            {
                @Override
                public void run()
                {
                    event.notifyAllListeners((WorldData) null, id);
                }
            }, false);
        }
    }

    /**
     * Constructor. May be time consuming. For reasoning for
     * it 'protection' see {@link UiEventDispatcher} and
     * {@link #performDispatch(Runnable,boolean)}.
     */
    protected UiEventDispatcher()
    {
        eventListenersRegistry =
                new EnumMap<EventType, UiEvent>(EventType.class);
        dataListenersRegistry =
                new EnumMap<WorldDataType, UiEvent>(WorldDataType.class);

        for (UiEvent event : UiEvent.class.getEnumConstants())
        {
            if (!event.isEventBased())
            {
                WorldDataType type = event.getDataType();
                if (type == null)
                    continue;
                UiEvent oldEvent =
                        dataListenersRegistry.put(event.getDataType(), event);
                assert oldEvent == null;
                continue;
            }

            UiEvent oldEvent =
                    eventListenersRegistry.put(event.getEventType(), event);
            assert oldEvent == null;
        }
    }

    /**
     * Dispatches chat message to all listeners.
     * @param chat
     *            chat to dispatch
     */
    public final void dispatch(final Chat chat)
    {
        for (final ChatListener l : chatListeners)
            performDispatch(new Runnable()
            {
                @Override
                public void run()
                {
                    l.chatReceived(chat);
                }
            }, false);
    }

    /**
     * Dispatches all events generated from game events
     * connected with this command.
     * @param command
     *            command to process
     */
    public final void dispatch(final Command command)
    {
        for (final Event event : command.events())
        {
            EventType type = event.getType();

            final UiEvent uiEvent = eventListenersRegistry.get(type);
            if (uiEvent != null && uiEvent.hasListeners())
                performDispatch(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        uiEvent.notifyAllListeners(event, command);
                    }
                }, false);

        }

        for (final WorldData data : command.datas())
        {
            final UiEvent event = dataListenersRegistry.get(data.getType());
            if (event != null && event.hasListeners())
                performDispatch(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        for (int id : data.updatedIds())
                            event.notifyAllListeners(data, id);
                    }
                }, false);
        }
    }

    /**
     * Method responsible for real dispatching. Executing
     * {@link Runnable#run() run()} method of given argument
     * will result in notification of all listeners (each
     * UI-event generates different runnable). Overloading
     * this method may allow to make this notification in
     * other thread etc. Default implementation just calls
     * {@code runnable.run()}.
     * @param runnable
     *            runnable which will call notification
     *            methods of proper listeners when run.
     * @param wait
     *            when {@code true} calling thread will be
     *            blocked until given runnable is finished
     *            in event thread
     */
    protected void performDispatch(Runnable runnable, boolean wait)
    {
        runnable.run();
    }

    /**
     * Executes given runnable (by calling
     * {@link Runnable#run()}) in same thread as all
     * dispatched events.
     * @param runnable
     *            runnable to execute in event thread
     * @param wait
     *            when {@code true} calling thread will be
     *            blocked until given runnable is finished
     *            in event thread
     */
    public final void performInDispatchThread(Runnable runnable, boolean wait)
    {
        performDispatch(runnable, wait);
    }

    /** {@inheritDoc} */
    @Override
    public final void addItemTypeLoadedListener(ItemTypeLoadedListener listener)
    {
        UiEvent.ITEM_TYPE_LOADED.addListener(listener);
    }

    /** {@inheritDoc} */
    @Override
    public final void removeItemTypeLoadedListener(ItemTypeLoadedListener listener)
    {
        UiEvent.ITEM_TYPE_LOADED.removeListener(listener);
    }

    /**
     * Connects to item type database. Completion
     * notifications will create proper events. Disconnects
     * old connection to same type database.
     * @param itemTypeDB
     *            item type database to connect to.
     */
    public final void connectDB(ItemTypeDBAsync itemTypeDB)
    {
        if (this.itemTypeDB != null)
            this.itemTypeDB.removeCompletionObserver(itemLink);
        itemLink = new CompletionListenerLink(UiEvent.ITEM_TYPE_LOADED);
        itemTypeDB.addCompletionObserver(itemLink);
        this.itemTypeDB = itemTypeDB;
    }

    /** {@inheritDoc} */
    @Override
    public final void addItemDroppedListener(ItemDroppedListener listener)
    {
        UiEvent.ITEM_DROPPED.addListener(listener);
    }

    /** {@inheritDoc} */
    @Override
    public final void removeItemDroppedListener(ItemDroppedListener listener)
    {
        UiEvent.ITEM_DROPPED.removeListener(listener);
    }

    /** {@inheritDoc} */
    @Override
    public final void addItemPickedUpListener(ItemPickedUpListener listener)
    {
        UiEvent.ITEM_PICKED_UP.addListener(listener);
    }

    /** {@inheritDoc} */
    @Override
    public final void removeItemPickedUpListener(ItemPickedUpListener listener)
    {
        UiEvent.ITEM_PICKED_UP.removeListener(listener);
    }

    /** {@inheritDoc} */
    @Override
    public final void addItemAddedListener(ItemAddedListener listener)
    {
        UiEvent.ITEM_ADDED.addListener(listener);
    }

    /** {@inheritDoc} */
    @Override
    public final void removeItemAddedListener(ItemAddedListener listener)
    {
        UiEvent.ITEM_ADDED.removeListener(listener);
    }

    /** {@inheritDoc} */
    @Override
    public final void addActorChangedMapListener(ActorChangedMapListener listener)
    {
        UiEvent.ACTOR_CHANGED_MAP.addListener(listener);
    }

    /** {@inheritDoc} */
    @Override
    public final void removeChangedMpaListener(ActorChangedMapListener listener)
    {
        UiEvent.ACTOR_CHANGED_MAP.removeListener(listener);
    }

    /** {@inheritDoc} */
    @Override
    public final void addActorAddedListener(ActorAddedListener listener)
    {
        UiEvent.ACTOR_ADDED.addListener(listener);
    }

    /** {@inheritDoc} */
    @Override
    public final void removeActorAddedListener(ActorAddedListener listener)
    {
        UiEvent.ACTOR_ADDED.removeListener(listener);
    }

    /** {@inheritDoc} */
    @Override
    public final void addActorRemovedListener(ActorRemovedListener listener)
    {
        UiEvent.ACTOR_REMOVED.addListener(listener);
    }

    /** {@inheritDoc} */
    @Override
    public final void removeActorRemovedListener(ActorRemovedListener listener)
    {
        UiEvent.ACTOR_REMOVED.removeListener(listener);
    }

    /** {@inheritDoc} */
    @Override
    public final void addItemEquippedListener(ItemEquippedListener listener)
    {
        UiEvent.EQUIPPED_ITEM.addListener(listener);
    }

    /** {@inheritDoc} */
    @Override
    public final void addItemUnEquippedListener(ItemUnEquippedListener listener)
    {
        UiEvent.UNEQUIPPED_ITEM.addListener(listener);
    }

    /** {@inheritDoc} */
    @Override
    public final void removeItemEquippedListener(ItemEquippedListener listener)
    {
        UiEvent.EQUIPPED_ITEM.removeListener(listener);
    }

    /** {@inheritDoc} */
    @Override
    public final void removeItemUnEquippedListener(ItemUnEquippedListener listener)
    {
        UiEvent.UNEQUIPPED_ITEM.removeListener(listener);
    }

    /** {@inheritDoc} */
    @Override
    public final void addActorDataChangedListener(ActorDataChangedListener listener)
    {
        UiEvent.ACTOR_FAST_DATA.addListener(listener);
        UiEvent.ACTOR_DATA.addListener(listener);
    }

    /** {@inheritDoc} */
    @Override
    public final void removeActorDataChangedListener(ActorDataChangedListener listener)
    {
        UiEvent.ACTOR_FAST_DATA.removeListener(listener);
        UiEvent.ACTOR_DATA.removeListener(listener);
    }

    /** {@inheritDoc} */
    @Override
    public final void addItemDataChangedListener(ItemDataChangedListener listener)
    {
        UiEvent.ITEM_DATA.addListener(listener);
    }

    /** {@inheritDoc} */
    @Override
    public final void removeItemDataChangedListener(ItemDataChangedListener listener)
    {
        UiEvent.ITEM_DATA.removeListener(listener);
    }

    /** {@inheritDoc} */
    @Override
    public final void removeFromAll(UiEventListener listener)
    {
        for (UiEvent event : UiEvent.ELEMENTS)
            event.removeListener(listener);
        chatListeners.remove(listener);
    }

    /** {@inheritDoc} */
    @Override
    public final void addActorWalkListener(ActorWalkListener listener)
    {
        UiEvent.ACTOR_WALK.addListener(listener);

    }

    /** {@inheritDoc} */
    @Override
    public final void removeActorWalkListener(ActorWalkListener listener)
    {
        UiEvent.ACTOR_WALK.removeListener(listener);
    }

    /** {@inheritDoc} */
    @Override
    public final void addChatListener(ChatListener listener)
    {
        chatListeners.add(listener);
    }

    /** {@inheritDoc} */
    @Override
    public final void removeChatListener(ChatListener listener)
    {
        chatListeners.remove(listener);
    }

    /** {@inheritDoc} */
    @Override
    public final void addCooldownChangedListener(CooldownChangedListener listener)
    {
        UiEvent.COOLDOWN_CHANGED.addListener(listener);
    }

    /** {@inheritDoc} */
    @Override
    public final void removeCooldownChangedListener(CooldownChangedListener listener)
    {
        UiEvent.COOLDOWN_CHANGED.removeListener(listener);
    }
}
