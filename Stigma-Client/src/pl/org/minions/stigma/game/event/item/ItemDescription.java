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
package pl.org.minions.stigma.game.event.item;

import pl.org.minions.stigma.databases.item.ItemFactory;
import pl.org.minions.stigma.game.command.data.sink.DataRequestSinks;
import pl.org.minions.stigma.game.item.Item;
import pl.org.minions.stigma.game.item.type.ItemType.ItemKind;
import pl.org.minions.stigma.game.world.World;
import pl.org.minions.stigma.globals.SizeOf;
import pl.org.minions.stigma.network.Buffer;

/**
 * Utility class for sending description of item. Contains
 * all information for creation and synchronization of item
 * (id, type, kind and timestamp).
 */
final class ItemDescription
{
    private static final int SIZE = SizeOf.INT // id
        + SizeOf.SHORT // type
        + SizeOf.BYTE // kind
        + SizeOf.INT // TS 
    ;

    private int id;
    private short type;
    private ItemKind kind;
    private int ts;

    /**
     * Constructor.
     * @param i
     *            item which should be described by this
     *            class
     */
    public ItemDescription(Item i)
    {
        this.id = i.getId();
        this.type = i.getType().getId();
        this.kind = i.getKind();
        this.ts = i.getNewestTS();
    }

    /**
     * Constructor. Decodes item description from given
     * buffer.
     * @param buffer
     *            buffer to decode from
     */
    public ItemDescription(Buffer buffer)
    {
        this.id = buffer.decodeInt();
        this.type = buffer.decodeShort();
        this.kind = buffer.decodeEnum(ItemKind.class);
        this.ts = buffer.decodeInt();
    }

    /**
     * Returns size in bytes of this class encoded on
     * {@link Buffer}.
     * @return size needed to encode this class
     */
    public static int sizeOf()
    {
        return SIZE;
    }

    /**
     * Encodes object on given buffer.
     * @param buffer
     *            buffer to encode on
     */
    public void encode(Buffer buffer)
    {
        buffer.encode(id);
        buffer.encode(type);
        buffer.encode(kind);
        buffer.encode(ts);
    }

    /**
     * Return item described by this object. If no such item
     * - creates one. If data sink is provided -
     * synchronizes information about object.
     * @param world
     *            world to get/create item from/in
     * @param sink
     *            data sink for synchronization, may be
     *            {@code null}
     * @return item described by this object
     */
    public Item getItem(World world, DataRequestSinks sink)
    {
        Item i = world.getItem(id);

        if (i == null)
        {
            i = ItemFactory.getInstance().getItem(id, type, kind);

            world.addItem(i);

            if (sink != null)
                sink.getItemSink().forceAdd(i);
        }
        else if (sink != null)
            sink.getItemSink().compareAndAdd(i, ts);

        return i;
    }

    /**
     * Returns id.
     * @return id
     */
    public int getId()
    {
        return id;
    }

    /**
     * Returns type.
     * @return type
     */
    public short getType()
    {
        return type;
    }

    /**
     * Returns kind.
     * @return kind
     */
    public ItemKind getKind()
    {
        return kind;
    }
}
