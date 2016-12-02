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

/**
 * Interface providing list of functions for adding/removing
 * all available listeners for UI events.
 */
public interface UiEventRegistry
{
    /**
     * Removes listener from all events it is connected to.
     * @param listener
     *            listener to remove
     */
    void removeFromAll(UiEventListener listener);

    /**
     * Adds listener for 'actor changed map'
     * event.
     * @param listener
     *            listener to add
     */
    void addActorChangedMapListener(ActorChangedMapListener listener);

    /**
     * Removes listener for 'actor changed map'
     * event.
     * @param listener
     *            listener to remove
     */
    void removeChangedMpaListener(ActorChangedMapListener listener);

    /**
     * Adds listener for 'actor walk' event.
     * @param listener
     *            listener to add
     */
    void addActorWalkListener(ActorWalkListener listener);

    /**
     * Removes listener for 'actor walk' event.
     * @param listener
     *            listener to remove
     */
    void removeActorWalkListener(ActorWalkListener listener);

    /**
     * Adds listener for 'equipped item' event.
     * @param listener
     *            listener to add
     */
    void addItemEquippedListener(ItemEquippedListener listener);

    /**
     * Removes listener for 'equipped item' event.
     * @param listener
     *            listener to remove
     */
    void removeItemEquippedListener(ItemEquippedListener listener);

    /**
     * Adds listener for 'un-equipped item' event.
     * @param listener
     *            listener to add
     */
    void addItemUnEquippedListener(ItemUnEquippedListener listener);

    /**
     * Removes listener for 'un-equipped item' event.
     * @param listener
     *            listener to remove
     */
    void removeItemUnEquippedListener(ItemUnEquippedListener listener);

    /**
     * Adds listener for 'item type data loaded' event.
     * @param listener
     *            listener to add
     */
    void addItemTypeLoadedListener(ItemTypeLoadedListener listener);

    /**
     * Removes listener for 'item type data loaded' event.
     * @param listener
     *            listener to remove
     */
    void removeItemTypeLoadedListener(ItemTypeLoadedListener listener);

    /**
     * Adds listener for 'item data changed' event.
     * @param listener
     *            listener to add
     */
    void addItemDataChangedListener(ItemDataChangedListener listener);

    /**
     * Removes listener for 'item data changed' event.
     * @param listener
     *            listener to remove
     */
    void removeItemDataChangedListener(ItemDataChangedListener listener);

    /**
     * Adds listener for 'item dropped' event.
     * @param listener
     *            listener to add
     */
    void addItemDroppedListener(ItemDroppedListener listener);

    /**
     * Removes listener for 'item dropped' event.
     * @param listener
     *            listener to remove
     */
    void removeItemDroppedListener(ItemDroppedListener listener);

    /**
     * Adds listener for 'item picked up' event.
     * @param listener
     *            listener to add
     */
    void addItemPickedUpListener(ItemPickedUpListener listener);

    /**
     * Removes listener for 'item picked up' event.
     * @param listener
     *            listener to remove
     */
    void removeItemPickedUpListener(ItemPickedUpListener listener);

    /**
     * Adds listener for 'item added' event.
     * @param listener
     *            listener to add
     */
    void addItemAddedListener(ItemAddedListener listener);

    /**
     * Removes listener for 'item added' event.
     * @param listener
     *            listener to remove
     */
    void removeItemAddedListener(ItemAddedListener listener);

    /**
     * Adds listener for 'actor added' event.
     * @param listener
     *            listener to add
     */
    void addActorAddedListener(ActorAddedListener listener);

    /**
     * Removes listener for 'actor added' event.
     * @param listener
     *            listener to remove
     */
    void removeActorAddedListener(ActorAddedListener listener);

    /**
     * Adds listener for 'actor removed' event.
     * @param listener
     *            listener to add
     */
    void addActorRemovedListener(ActorRemovedListener listener);

    /**
     * Removes listener for 'actor removed' event.
     * @param listener
     *            listener to remove
     */
    void removeActorRemovedListener(ActorRemovedListener listener);

    /**
     * Adds listener for all events that change actor's
     * data.
     * @param listener
     *            listener to add
     */
    void addActorDataChangedListener(ActorDataChangedListener listener);

    /**
     * Removes listener from all events that change actor's
     * data.
     * @param listener
     *            listener to remove
     */
    void removeActorDataChangedListener(ActorDataChangedListener listener);

    /**
     * Adds chat listener. Listeners will react on all
     * messages (no filtering will be applied).
     * @param listener
     *            listener to add
     */
    void addChatListener(ChatListener listener);

    /**
     * Removes chat listeners.
     * @param listener
     *            listener to remove
     */
    void removeChatListener(ChatListener listener);

    /**
     * Adds cooldown listener.
     * @param listener
     *            listener to add
     */
    void addCooldownChangedListener(CooldownChangedListener listener);

    /**
     * Removes cooldown listener.
     * @param listener
     *            listener to remove
     */
    void removeCooldownChangedListener(CooldownChangedListener listener);
}
