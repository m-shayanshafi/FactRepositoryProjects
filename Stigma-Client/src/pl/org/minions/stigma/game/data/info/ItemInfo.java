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
package pl.org.minions.stigma.game.data.info;

import java.text.MessageFormat;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import pl.org.minions.stigma.game.command.data.sink.DataRequestSinks;
import pl.org.minions.stigma.game.data.WorldData;
import pl.org.minions.stigma.game.data.WorldDataType;
import pl.org.minions.stigma.game.item.Item;
import pl.org.minions.stigma.game.world.World;
import pl.org.minions.stigma.globals.SizeOf;
import pl.org.minions.stigma.network.Buffer;
import pl.org.minions.utils.logger.Log;

/**
 * Class representing update of item statistics. Updates
 * multiple items.
 */
public class ItemInfo extends WorldData
{
    private static class ItemInfoData
    {
        private static final int CONST_SIZE = SizeOf.INT + SizeOf.INT;

        private int itemId;
        private String name;
        private int ts;

        public ItemInfoData(Buffer buf)
        {
            this.itemId = buf.decodeInt();
            this.name = buf.decodeString();
            this.ts = buf.decodeInt();
        }

        public ItemInfoData(Item i)
        {
            this.itemId = i.getId();
            this.name = i.getName();
            this.ts = i.getNewestTS();
        }

        public void encode(Buffer buf)
        {
            buf.encode(itemId);
            buf.encode(name);
            buf.encode(ts);
        }

        public int getItemId()
        {
            return itemId;
        }

        public String getName()
        {
            return name;
        }

        public int getTS()
        {
            return ts;
        }

        public int size()
        {
            return CONST_SIZE + Buffer.stringBytesCount(name);
        }
    }

    private List<ItemInfo.ItemInfoData> list =
            new LinkedList<ItemInfo.ItemInfoData>();

    /**
     * Constructor.
     */
    public ItemInfo()
    {
        super(WorldDataType.ITEM_INFO);
    }

    /**
     * Adds item to list.
     * @param i
     *            item which should be included in update
     */
    public void addItem(Item i)
    {
        list.add(new ItemInfoData(i));

    }

    /** {@inheritDoc} */
    @Override
    public boolean apply(World world, DataRequestSinks dataRequestSink)
    {
        for (ItemInfo.ItemInfoData data : list)
        {
            Item i = world.getItem(data.getItemId());
            if (i == null)
            {
                Log.logger.warn("ItemInfo for null item");
                return false;
            }

            i.setName(data.getName());
            i.setTS(data.getTS());
        }
        return true;
    }

    /** {@inheritDoc} */
    @Override
    protected boolean decodeParams(Buffer buffer)
    {
        byte size = buffer.decodeByte();
        list.clear();
        for (int i = 0; i < size; ++i)
            list.add(new ItemInfoData(buffer));
        return true;
    }

    /** {@inheritDoc} */
    @Override
    protected boolean encodeParams(Buffer buffer)
    {
        buffer.encode((byte) list.size());
        for (ItemInfo.ItemInfoData data : list)
            data.encode(buffer);
        return true;
    }

    /** {@inheritDoc} */
    @Override
    protected int innerGetParamsLength()
    {
        int r = SizeOf.BYTE;
        for (ItemInfo.ItemInfoData data : list)
            r += data.size();
        return r;
    }

    /**
     * Returns {@code true} when no item was added to this
     * delta.
     * @return {@code true} when items info list is empty
     */
    public boolean isEmpty()
    {
        return list.isEmpty();
    }

    /** {@inheritDoc} */
    @Override
    public String toString()
    {
        StringBuilder buf = new StringBuilder();
        buf.append("ItemInfo: ");
        for (ItemInfo.ItemInfoData data : list)
            buf.append(MessageFormat.format("[itemId:{0}, name: {1}]",
                                            data.getItemId(),
                                            data.getName()));
        return buf.toString();
    }

    /** {@inheritDoc} */
    @Override
    public Iterable<Integer> updatedIds()
    {
        return new Iterable<Integer>()
        {
            @Override
            public Iterator<Integer> iterator()
            {
                return new Iterator<Integer>()
                {
                    private Iterator<ItemInfoData> it = list.iterator();

                    @Override
                    public boolean hasNext()
                    {
                        return it.hasNext();
                    }

                    @Override
                    public Integer next()
                    {
                        return it.next().getItemId();
                    }

                    @Override
                    public void remove()
                    {
                        throw new UnsupportedOperationException();
                    }
                };
            }
        };
    }

}
