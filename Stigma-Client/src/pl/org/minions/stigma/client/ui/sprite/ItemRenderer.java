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
package pl.org.minions.stigma.client.ui.sprite;

import java.awt.Color;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.text.MessageFormat;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import pl.org.minions.stigma.client.Client;
import pl.org.minions.stigma.client.images.ImageDB;
import pl.org.minions.stigma.client.images.ImageProxy;
import pl.org.minions.stigma.client.observers.CurrentSegmentChangeObserver;
import pl.org.minions.stigma.client.ui.Clearable;
import pl.org.minions.stigma.client.ui.VisualizationGlobals;
import pl.org.minions.stigma.client.ui.event.UiEventRegistry;
import pl.org.minions.stigma.client.ui.event.listeners.ItemAddedListener;
import pl.org.minions.stigma.client.ui.event.listeners.ItemDataChangedListener;
import pl.org.minions.stigma.client.ui.event.listeners.ItemDroppedListener;
import pl.org.minions.stigma.client.ui.event.listeners.ItemPickedUpListener;
import pl.org.minions.stigma.client.ui.event.listeners.ItemTypeLoadedListener;
import pl.org.minions.stigma.game.actor.Actor;
import pl.org.minions.stigma.game.event.Event;
import pl.org.minions.stigma.game.event.item.ItemAdded;
import pl.org.minions.stigma.game.event.item.ItemDropped;
import pl.org.minions.stigma.game.event.item.ItemPickedUp;
import pl.org.minions.stigma.game.item.Item;
import pl.org.minions.stigma.game.map.MapInstance;
import pl.org.minions.stigma.game.map.MapInstance.Segment;
import pl.org.minions.stigma.globals.Position;
import pl.org.minions.utils.logger.Log;
import pl.org.minions.utils.ui.sprite.ImageSprite;

/**
 * Makes sure that all {@link Item items} within field of
 * view are represented on sprite canvas by reacting to
 * {@link Event events} and current {@link Segment map
 * segment} change.
 */
public class ItemRenderer implements
                         CurrentSegmentChangeObserver,
                         Clearable,
                         ItemDroppedListener,
                         ItemPickedUpListener,
                         ItemAddedListener,
                         ItemDataChangedListener,
                         ItemTypeLoadedListener
{

    private ForegroundLayerGroup foreground;

    private static class ItemStack implements ItemTypeLoadedListener
    {
        private final Position position;
        private final ForegroundLayerGroup foreground;

        private int size;
        private Item lastItem;

        private final ImageSprite sprite = new ImageSprite();

        private ImageProxy proxy;

        private enum StackIconState
        {
            STACK_ICON_FINAL,
            STACK_ICON_LOADING_ITEM,
            STACK_ICON_LOADING_IMAGE,
        };

        private StackIconState iconState;

        public ItemStack(Position position,
                         Item item,
                         ForegroundLayerGroup foreground)
        {
            this.foreground = foreground;
            this.position = position;

            size = 1;
            lastItem = item;

            sprite.setOffset(new Point(VisualizationGlobals.MAP_TILE_WIDTH / 2,
                                       VisualizationGlobals.MAP_TILE_HEIGHT / 2));
            sprite.setColor(Color.orange);
            sprite.setPosition(new Point(position.getX()
                * VisualizationGlobals.MAP_TILE_WIDTH
                + VisualizationGlobals.MAP_TILE_WIDTH / 2, position.getY()
                * VisualizationGlobals.MAP_TILE_HEIGHT
                + VisualizationGlobals.MAP_TILE_HEIGHT / 2));

            sprite.setImage(fetchIcon());

            sprite.setVisible(true);

            foreground.getGroundEffectLayer().addSprite(sprite);
        }

        public void addItem(final Item item)
        {
            if (size++ == 1)
            {
                lastItem = null;
                sprite.setImage(fetchIcon());
            }
        }

        public void removeItem(final Item item)
        {
            if (--size == 1)
            {
                lastItem = findLastItem();
                sprite.setImage(fetchIcon());
            }
        }

        public boolean isEmpty()
        {
            return size == 0;
        }

        public Position getPosition()
        {
            return position;
        }

        public void remove()
        {
            foreground.getGroundEffectLayer().removeSprite(sprite);
        }

        /** {@inheritDoc} */
        @Override
        public int hashCode()
        {
            return position.hashCode();
        }

        private Item findLastItem()
        {
            final Client client = Client.globalInstance();
            final Actor playerActor = client.getPlayerActor();
            final MapInstance map =
                    client.getWorld().getMap(playerActor.getMapId(),
                                             playerActor.getMapInstanceNo());

            final List<Item> items = map.getItems(position);
            assert items != null;
            assert items.size() == 1;
            return items.get(0);
        }

        private BufferedImage fetchIcon()
        {
            if (size > 1)
            {
                proxy = null;
                iconState = StackIconState.STACK_ICON_FINAL;
                return VisualizationGlobals.ITEM_STACK_IMAGE;
            }

            if (!lastItem.isComplete())
            {
                proxy = null;
                iconState = StackIconState.STACK_ICON_LOADING_ITEM;
                return VisualizationGlobals.ITEM_STACK_IMAGE;
            }

            if (proxy == null)
            {
                final String iconPath = lastItem.getType().getOnGroundIcon();
                if (iconPath == null)
                {
                    proxy = null;
                    iconState = StackIconState.STACK_ICON_FINAL;
                    return VisualizationGlobals.ITEM_STACK_IMAGE;
                }

                proxy =
                        ImageDB.globalInstance()
                               .getItemIcon(lastItem.getKind(), iconPath);
            }

            switch (proxy.getState())
            {
                case LOADED:
                    final BufferedImage image = proxy.getImage();
                    proxy = null;
                    iconState = StackIconState.STACK_ICON_FINAL;
                    return image;
                case LOADING:
                    iconState = StackIconState.STACK_ICON_LOADING_IMAGE;
                    return VisualizationGlobals.ITEM_STACK_IMAGE;
                case FAILED:
                default:
                    proxy = null;
                    iconState = StackIconState.STACK_ICON_FINAL;
                    return VisualizationGlobals.ITEM_STACK_IMAGE;
            }
        }

        @Override
        public void itemTypeLoaded(short id)
        {
            if (iconState != StackIconState.STACK_ICON_LOADING_ITEM)
                return;

            if (lastItem.getType().getId() != id)
                return;

            sprite.setImage(fetchIcon());
        }

        public void checkForImage()
        {
            if (iconState != StackIconState.STACK_ICON_LOADING_IMAGE)
                return;
            assert lastItem != null;
            sprite.setImage(fetchIcon());
        }

    }

    private final Map<Position, ItemStack> stacks =
            new HashMap<Position, ItemStack>();

    /**
     * Creates a new item renderer instance.
     * @param foreground
     *            foreground canvas layer group
     */
    public ItemRenderer(ForegroundLayerGroup foreground)
    {
        this.foreground = foreground;
        Client client = Client.globalInstance();
        client.addCurrentSegmentChangeObserver(this);

        UiEventRegistry uiEventRegistry =
                Client.globalInstance().uiEventRegistry();

        uiEventRegistry.addItemDroppedListener(this);

        uiEventRegistry.addItemPickedUpListener(this);

        uiEventRegistry.addItemAddedListener(this);

        uiEventRegistry.addItemDataChangedListener(this);

        uiEventRegistry.addItemTypeLoadedListener(this);
    }

    /**
     * TODO: not sure what will be animated and how, yet.
     * @param milliseconds
     *            the time passed
     */
    public void animateAll(long milliseconds)
    {
        for (ItemStack stack : stacks.values())
        {
            stack.checkForImage();
        }
    }

    /** {@inheritDoc} */
    @Override
    public void currentSegmentChanged(Segment currentSegment,
                                      Segment previousSegment,
                                      Collection<Segment> appearingSegments,
                                      Collection<Segment> disappearingSegments)
    {
        if (currentSegment == null)
            removeItemStacks();
        else if (previousSegment != null
            && currentSegment.getParentMap() != previousSegment.getParentMap())
        {
            removeItemStacks();
        }
        else
            for (Segment segment : disappearingSegments)
            {
                for (Item item : segment.getItems())
                {
                    removeStackAt(item.getPosition());
                }
            }

        for (Segment segment : appearingSegments)
        {
            for (Item item : segment.getItems())
            {
                addItem(item);
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void itemDropped(ItemDropped event, boolean playerAction)
    {
        final Item item =
                Client.globalInstance().getWorld().getItem(event.getItemId());
        assert item != null;
        addItem(item);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void itemPickedUp(ItemPickedUp event, boolean playerActor)
    {
        final Item item =
                Client.globalInstance().getWorld().getItem(event.getItemId());
        assert item != null;
        removeItem(item);
    }

    /** {@inheritDoc} */
    @Override
    public void itemAdded(ItemAdded event)
    {
        addItem(event.getItem());
    }

    /** {@inheritDoc} */
    @Override
    public void itemDataChanged(int id)
    {
        // TODO Auto-generated method stub
    }

    /** {@inheritDoc} */
    @Override
    public void itemTypeLoaded(short id)
    {
        for (ItemStack stack : stacks.values())
        {
            stack.itemTypeLoaded(id);
        }
    }

    private void addItem(Item item)
    {
        if (Log.isDebugEnabled())
            Log.logger.debug(MessageFormat.format("Adding item {0} at {1}",
                                                  item,
                                                  item.getPosition()));
        final Position position = item.getPosition();

        final ItemStack stack = stacks.get(position);
        if (stack == null)
        {
            if (Log.isTraceEnabled())
                Log.logger.trace("Adding item stack at " + position);
            stacks.put(position, new ItemStack(position, item, foreground));
        }
        else
            stack.addItem(item);
    }

    /**
     * Removes a whole stack (used when a segment
     * disappears).
     */
    private void removeStackAt(Position position)
    {
        final ItemStack stack = stacks.get(position);

        if (stack == null)
            return;

        if (Log.isDebugEnabled())
            Log.logger.debug("Removing item stack at " + stack.getPosition());

        stack.remove();
        stacks.remove(position);
    }

    private void removeItem(Item item)
    {
        final Position position = item.getPosition();
        if (Log.isDebugEnabled())
            Log.logger.debug("Removing item " + item + " at " + position);
        final ItemStack stack = stacks.get(position);
        assert stack != null;

        stack.removeItem(item);

        if (stack.isEmpty())
        {
            if (Log.isTraceEnabled())
                Log.logger.trace("Removing empty item stack at "
                    + stack.getPosition());
            stack.remove();
            stacks.remove(position);
        }
    }

    /** {@inheritDoc} */
    @Override
    public void clear()
    {
        removeItemStacks();
    }

    private void removeItemStacks()
    {
        Log.logger.debug("Removing item stacks.");
        Iterator<ItemStack> iter = stacks.values().iterator();
        while (iter.hasNext())
        {
            iter.next().remove();
            iter.remove();
        }
    }
}
