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
import java.util.Collection;
import java.util.LinkedList;

import pl.org.minions.stigma.databases.item.ItemFactory;
import pl.org.minions.stigma.game.actor.Actor;
import pl.org.minions.stigma.game.command.data.sink.DataRequestSinks;
import pl.org.minions.stigma.game.data.WorldData;
import pl.org.minions.stigma.game.data.WorldDataType;
import pl.org.minions.stigma.game.item.Item;
import pl.org.minions.stigma.game.item.type.ItemType.ItemKind;
import pl.org.minions.stigma.game.map.MapInstance;
import pl.org.minions.stigma.game.world.World;
import pl.org.minions.stigma.globals.Position;
import pl.org.minions.stigma.globals.SizeOf;
import pl.org.minions.stigma.network.Buffer;

/**
 * Class containing full information about one segment. Not
 * a delta exactly, used for synchronization.
 */
public class SegmentInfo extends WorldData
{
    private static final int ACTOR_SIZE =
            SizeOf.INT + SizeOf.INT + Position.sizeOf();
    private static final int ITEM_SIZE =
            SizeOf.INT + SizeOf.SHORT + SizeOf.BYTE + SizeOf.INT
                + Position.sizeOf();
    private static final int MIN_SIZE =
            SizeOf.SHORT + SizeOf.SHORT + Position.sizeOf() + SizeOf.BYTE
                + SizeOf.SHORT;

    private short mapId;
    private short instanceNo;
    private Position topLeft;
    private Collection<ActorInfo> actors;
    private Collection<ItemInfo> items;

    private class ActorInfo
    {
        private int id;
        private int newestTS;
        private Position position;

        public ActorInfo(Actor a)
        {
            this.id = a.getId();
            this.newestTS = a.getNewestTS();
            this.position = a.getPosition().deepCopy();
        }

        public ActorInfo(Buffer buffer)
        {
            this.id = buffer.decodeInt();
            this.newestTS = buffer.decodeInt();
            this.position = buffer.decodePosition();
        }

        public void encode(Buffer buffer)
        {
            buffer.encode(id);
            buffer.encode(newestTS);
            buffer.encode(position);
        }

        public Actor apply(World world, DataRequestSinks dataRequestSink)
        {
            Actor a1 = world.getActor(id);
            if (a1 == null)
            {
                a1 = new Actor(id, null);
                a1.setPosition(position);
                a1.setMapId(mapId);
                a1.setMapInstanceNo(instanceNo);

                world.addActor(a1);
                if (dataRequestSink != null)
                    dataRequestSink.getActorSink().forceAdd(a1);
            }
            else
            {
                // remove for old instance, just for "sanity check"
                MapInstance oldMap =
                        world.getMap(a1.getMapId(), a1.getMapInstanceNo());
                if (oldMap != null)
                {
                    MapInstance.Segment oldSegment =
                            oldMap.getSegmentForPosition(a1.getPosition());
                    if (oldSegment != null)
                        oldSegment.takeActor(a1.getPosition());
                }

                // value change
                a1.setPosition(position);
                a1.setMapId(mapId);
                a1.setMapInstanceNo(instanceNo);

                if (dataRequestSink != null)
                    dataRequestSink.getActorSink().compareAndAdd(a1, newestTS);
            }
            return a1;
        }
    }

    private class ItemInfo
    {
        private int id;
        private short type;
        private ItemKind kind;
        private int newestTS;
        private Position position;

        public ItemInfo(Item i)
        {
            this.id = i.getId();
            this.type = i.getType().getId();
            this.kind = i.getKind();
            this.newestTS = i.getNewestTS();
            this.position = i.getPosition();
        }

        public ItemInfo(Buffer buffer)
        {
            this.id = buffer.decodeInt();
            this.type = buffer.decodeShort();
            this.kind = buffer.decodeEnum(ItemKind.class);
            this.newestTS = buffer.decodeInt();
            this.position = buffer.decodePosition();
        }

        public void encode(Buffer buffer)
        {
            buffer.encode(id);
            buffer.encode(type);
            buffer.encode(kind);
            buffer.encode(newestTS);
            buffer.encode(position);
        }

        public Item apply(World world, DataRequestSinks dataRequestSink)
        {
            Item i1 = world.getItem(id);
            if (i1 == null)
            {
                i1 = ItemFactory.getInstance().getItem(id, type, kind);

                i1.setPosition(position);
                i1.setMapId(mapId);
                i1.setMapInstanceNo(instanceNo);

                world.addItem(i1);
                if (dataRequestSink != null)
                    dataRequestSink.getItemSink().forceAdd(i1);
            }
            else
            {
                // remove for old instance, just for "sanity check"
                MapInstance oldMap =
                        world.getMap(i1.getMapId(), i1.getMapInstanceNo());
                if (oldMap != null)
                {
                    MapInstance.Segment oldSegment =
                            oldMap.getSegmentForPosition(i1.getPosition());
                    if (oldSegment != null)
                        oldSegment.removeItem(i1);
                }

                // value change
                i1.setPosition(position);
                i1.setMapId(mapId);
                i1.setMapInstanceNo(instanceNo);

                if (dataRequestSink != null)
                    dataRequestSink.getItemSink().compareAndAdd(i1, newestTS);
            }
            return i1;
        }
    }

    /** Creates empty delta - needed for decoding. */
    private SegmentInfo()
    {
        super(WorldDataType.SEGMENT_INFO);
    }

    /**
     * Creates new delta for given segment.
     * @param segment
     *            segment for which information should be
     *            generated
     */
    public SegmentInfo(MapInstance.Segment segment)
    {
        this(segment.getParentMap().getType().getId(),
             segment.getParentMap().getInstanceNo(),
             segment.getTopLeft(),
             segment.getActors(),
             segment.getItems());
    }

    /**
     * Creates new delta for given parameters.
     * @param mapId
     *            segment's parent map type
     * @param instanceNo
     *            segment's parent map instance number
     * @param topLeft
     *            top left tile of segment (identifies
     *            segment)
     * @param actors
     *            actors on segment (collection will be
     *            copied, objects not)
     * @param items
     *            items on segment (collection will be
     *            copied, objects not)
     */
    public SegmentInfo(short mapId,
                       short instanceNo,
                       Position topLeft,
                       Collection<Actor> actors,
                       Collection<Item> items)
    {
        this();
        this.mapId = mapId;
        this.instanceNo = instanceNo;
        this.topLeft = topLeft;
        this.actors = new LinkedList<ActorInfo>();
        for (Actor a : actors)
            this.actors.add(new ActorInfo(a));
        this.items = new LinkedList<ItemInfo>();
        for (Item i : items)
            this.items.add(new ItemInfo(i));
    }

    /**
     * Creates empty object (needed for decoding).
     * @return empty object
     */
    public static SegmentInfo create()
    {
        return new SegmentInfo();
    }

    /** {@inheritDoc} */
    @Override
    public boolean apply(World world, DataRequestSinks dataRequestSink)
    {
        MapInstance m = world.getMap(mapId, instanceNo);
        if (m == null)
        {
            m = world.createMap(mapId, instanceNo);
            if (m == null)
                return false;
        }
        MapInstance.Segment s = m.getSegmentForPosition(topLeft);
        if (s == null)
            return false;
        s.clearActors();
        for (ActorInfo a : actors)
            s.putActor(a.apply(world, dataRequestSink));

        s.clearItems();
        for (ItemInfo i : items)
            s.putItem(i.apply(world, dataRequestSink));
        return true;
    }

    /** {@inheritDoc} */
    @Override
    protected boolean decodeParams(Buffer buffer)
    {
        mapId = buffer.decodeShort();
        instanceNo = buffer.decodeShort();
        topLeft = buffer.decodePosition();
        actors = new LinkedList<ActorInfo>();

        byte len = buffer.decodeByte();
        while (len > 0)
        {
            --len;
            actors.add(new ActorInfo(buffer));
        }

        items = new LinkedList<ItemInfo>();
        short len2 = buffer.decodeShort();
        while (len2 > 0)
        {
            --len2;
            items.add(new ItemInfo(buffer));
        }
        return true;
    }

    /** {@inheritDoc} */
    @Override
    protected boolean encodeParams(Buffer buffer)
    {
        buffer.encode(mapId);
        buffer.encode(instanceNo);
        buffer.encode(topLeft);

        buffer.encode((byte) actors.size());
        for (ActorInfo a : actors)
            a.encode(buffer);

        buffer.encode((short) items.size());
        for (ItemInfo i : items)
            i.encode(buffer);

        return true;
    }

    /**
     * Returns instanceNo.
     * @return instanceNo
     */
    public short getInstanceNo()
    {
        return instanceNo;
    }

    /**
     * Returns mapId.
     * @return mapId
     */
    public short getMapId()
    {
        return mapId;
    }

    /**
     * Returns topLeft.
     * @return topLeft
     */
    public Position getTopLeft()
    {
        return topLeft;
    }

    /** {@inheritDoc} */
    @Override
    protected int innerGetParamsLength()
    {
        return MIN_SIZE + actors.size() * ACTOR_SIZE + items.size() * ITEM_SIZE;
    }

    /** {@inheritDoc} */
    @Override
    public String toString()
    {
        StringBuffer buf = new StringBuffer();
        for (ActorInfo a : actors)
            buf.append(MessageFormat.format("(id:{0},position:{1}), ",
                                            a.id,
                                            a.position));
        String actorsBuf = buf.toString();

        buf = new StringBuffer();
        for (ItemInfo i : items)
            buf.append(MessageFormat.format("(ID:{0},position:{1}), ",
                                            i.id,
                                            i.position));

        String itemsBuf = buf.toString();

        return MessageFormat.format("SegmentInfo: mapId:{0}, instanceNo:{1}, topLeft:{2}, actors:[{3}], items:[{4}]",
                                    mapId,
                                    instanceNo,
                                    topLeft,
                                    actorsBuf,
                                    itemsBuf);
    }

    /** {@inheritDoc} */
    @Override
    public Iterable<Integer> updatedIds()
    {
        return null;
    }
}
