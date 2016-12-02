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
package pl.org.minions.stigma.game.map;

import java.awt.Dimension;
import java.text.MessageFormat;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import pl.org.minions.stigma.game.actor.Actor;
import pl.org.minions.stigma.game.item.Item;
import pl.org.minions.stigma.globals.Direction;
import pl.org.minions.stigma.globals.Position;
import pl.org.minions.utils.collections.CollectionFactory;
import pl.org.minions.utils.collections.SuperSet;

/**
 * Represents instance of map. Is described by
 * {@link MapType}. Map is composed of {@link Segment} which
 * are partial "views" of whole map. Every object laying on
 * map is described by it's own position - segment "groups"
 * neighbor objects.
 */
public class MapInstance
{
    /**
     * Iterator which iterates on "neighborhood" of segments
     * (including "central" segment). "Neighborhood" is nine
     * segments - "central" and all boundary to it.
     * Iteration begins on top left, go row by row and ends
     * on bottom right segment.
     */
    public final class NeighborIterator implements Iterator<Segment>
    {
        private Position start;
        private Position end;
        private Position current;

        private NeighborIterator(Position center)
        {
            this.start = center.newPosition(Direction.NW);
            this.end = center.newPosition(Direction.SE);

            if (start.getX() < 0)
                start.setX((short) 0);
            if (start.getY() < 0)
                start.setY((short) 0);

            if (end.getX() >= MapInstance.this.xSegmentCount)
                end.setX((short) (MapInstance.this.xSegmentCount - 1));
            if (end.getY() >= MapInstance.this.ySegmentCount)
                end.setY((short) (MapInstance.this.ySegmentCount - 1));

            current = new Position(start);
        }

        /** {@inheritDoc} */
        @Override
        public boolean hasNext()
        {
            return current != null;
        }

        /** {@inheritDoc} */
        @Override
        public Segment next()
        {
            Segment r =
                    MapInstance.this.segments[current.getX()][current.getY()];
            int nx = current.getX() + 1;
            if (nx > end.getX())
            {
                int ny = current.getY() + 1;
                if (ny > end.getY())
                {
                    current = null;
                    return r;
                }
                nx = 0;

                current.setY((short) ny);
            }
            current.setX((short) nx);
            return r;
        }

        /**
         * Unimplemented, throws exception. @throws
         * UnsupportedOperationException
         */
        @Override
        public void remove()
        {
            throw new java.lang.UnsupportedOperationException();
        }
    }

    /**
     * Represents part of map.
     */
    public class Segment
    {
        private Map<Position, Actor> actorsMap =
                CollectionFactory.getFactory().createHashMap();
        private Map<Position, List<Item>> itemsMap =
                CollectionFactory.getFactory().createHashMap();
        private Position topLeft;

        /**
         * Creates map segment with given top left position.
         * @param topLeft
         *            top left position included in this
         *            segment
         */
        public Segment(Position topLeft)
        {
            this.topLeft = topLeft;
        }

        /**
         * Removes all actor from segment.
         */
        public void clearActors()
        {
            if (!needsType())
                actorsMap.clear();
        }

        /**
         * Removes all items from segment.
         */
        public void clearItems()
        {
            if (!needsType())
                itemsMap.clear();
        }

        /**
         * Checks if given position lies within this
         * segment.
         * @param position
         *            position to check
         * @return <code>true</code> if the selected
         *         position lies within this segment
         */
        public boolean contains(Position position)
        {
            if (needsType())
                return true;
            return position.getX() >= getTopLeft().getX()
                && position.getY() >= getTopLeft().getY()
                && position.getX() < getTopLeft().getX() + getSize().width
                && position.getY() < getTopLeft().getY() + getSize().height;
        }

        /**
         * Returns actor on given position or {@code null}
         * if no one is standing there.
         * @param pos
         *            position of actor
         * @see MapInstance#getActor(Position)
         * @return actor on given position
         */
        public Actor getActor(Position pos)
        {
            assert MapInstance.this.needsType() || pos.getX() >= topLeft.getX();
            assert MapInstance.this.needsType() || pos.getY() >= topLeft.getY();
            assert MapInstance.this.needsType()
                || pos.getX() < topLeft.getX()
                    + MapInstance.this.getType().getSegmentSizeX();
            assert MapInstance.this.needsType()
                || pos.getY() < topLeft.getY()
                    + MapInstance.this.getType().getSegmentSizeY();
            return actorsMap.get(pos);
        }

        /**
         * Returns items on given position or {@code null}
         * if no item is laying there.
         * @param pos
         *            position of item
         * @see MapInstance#getItems(Position)
         * @return item on given position
         */
        public List<Item> getItems(Position pos)
        {
            assert MapInstance.this.needsType() || pos.getX() >= topLeft.getX();
            assert MapInstance.this.needsType() || pos.getY() >= topLeft.getY();
            assert MapInstance.this.needsType()
                || pos.getX() < topLeft.getX()
                    + MapInstance.this.getType().getSegmentSizeX();
            assert MapInstance.this.needsType()
                || pos.getY() < topLeft.getY()
                    + MapInstance.this.getType().getSegmentSizeY();
            return itemsMap.get(pos);
        }

        /**
         * Returns actors which are on this segment.
         * @return actors which are on this segment
         */
        public Collection<Actor> getActors()
        {
            return actorsMap.values();
        }

        /**
         * Returns items which are on this segment.
         * <p>
         * This method may be slow and should be used with
         * caution.
         * @return items which are on this segment
         */
        public Collection<Item> getItems()
        {
            SuperSet<Item> set = new SuperSet<Item>();
            for (Map.Entry<Position, List<Item>> entry : itemsMap.entrySet())
            {
                set.addAll(entry.getValue());
            }
            return set;
        }

        /**
         * Returns map to which this segment belongs.
         * @return map to which this segment belongs
         */
        public MapInstance getParentMap()
        {
            return MapInstance.this;
        }

        /**
         * Returns the horizontal and vertical number of
         * tiles in this sector.
         * <p>
         * This values might be different than
         * {@link MapType#getSegmentSizeX()} and
         * {@link MapType#getSegmentSizeY()} for segments
         * located on bottom and right edges of map.
         * @return size of this sector in tiles
         */
        public Dimension getSize()
        {
            if (needsType())
                return null;
            Dimension size =
                    new Dimension(mapType.getSizeX() - topLeft.getX(),
                                  mapType.getSizeY() - topLeft.getY());

            if (size.width > mapType.getSegmentSizeX())
                size.width = mapType.getSegmentSizeX();
            if (size.height > mapType.getSegmentSizeY())
                size.height = mapType.getSegmentSizeY();
            return size;
        }

        /**
         * Returns top left position which lies inside this
         * segment.
         * @return top left position which lies inside this
         *         segment
         */
        public Position getTopLeft()
        {
            return topLeft;
        }

        /**
         * Returns object returning iterator for
         * neighborhood of this segment. Before use read
         * warnings in
         * {@link MapInstance#neighborhood(Position)}.
         * @see #neighborIterator()
         * @see MapInstance#neighborhood(Position)
         * @return object returning iterator for
         *         neighborhood of this segment
         */
        public Iterable<Segment> neighborhood()
        {
            return MapInstance.this.neighborhood(topLeft);
        }

        /**
         * Returns iterator to neighborhood of this segment
         * (including it).
         * @return iterator to neighborhood of this segment
         */
        public NeighborIterator neighborIterator()
        {
            return MapInstance.this.neighborIterator(topLeft);
        }

        /**
         * Puts actor on given segment, using it's position.
         * Actor position should lay inside this segment.
         * @param a
         *            actor to add to segment
         */
        public void putActor(Actor a)
        {
            assert MapInstance.this.needsType()
                || a.getPosition().getX() >= topLeft.getX();
            assert MapInstance.this.needsType()
                || a.getPosition().getY() >= topLeft.getY();
            assert MapInstance.this.needsType()
                || a.getPosition().getX() < topLeft.getX()
                    + MapInstance.this.getType().getSegmentSizeX();
            assert MapInstance.this.needsType()
                || a.getPosition().getY() < topLeft.getY()
                    + MapInstance.this.getType().getSegmentSizeY();
            if (actorsMap.put(a.getPosition(), a) == null)
            {
                if (a.isPC())
                    ++playersActorsCount;
            }
        }

        /**
         * Puts item on given segment, using it's position.
         * Item position should lay inside this segment.
         * @param i
         *            item to add to segment
         */
        public void putItem(Item i)
        {
            assert MapInstance.this.needsType()
                || i.getPosition().getX() >= topLeft.getX();
            assert MapInstance.this.needsType()
                || i.getPosition().getY() >= topLeft.getY();
            assert MapInstance.this.needsType()
                || i.getPosition().getX() < topLeft.getX()
                    + MapInstance.this.getType().getSegmentSizeX();
            assert MapInstance.this.needsType()
                || i.getPosition().getY() < topLeft.getY()
                    + MapInstance.this.getType().getSegmentSizeY();
            List<Item> list = itemsMap.get(i.getPosition());
            if (list == null)
            {
                list = new LinkedList<Item>();
                itemsMap.put(i.getPosition(), list);
            }
            list.add(i);
        }

        /**
         * Removes and returns actor from given position.
         * @param pos
         *            position to remove actor
         * @return actor which was on given position
         */
        public Actor takeActor(Position pos)
        {
            Actor a = actorsMap.remove(pos);
            if (a != null)
            {
                if (a.isPC())
                    --playersActorsCount;
            }
            return a;
        }

        /**
         * Removes item from segment, based on item position
         * and id.
         * @param item
         *            item to remove
         */
        public void removeItem(Item item)
        {
            List<Item> items = getItems(item.getPosition());
            if (items == null)
                return;

            Iterator<Item> it = items.iterator();
            while (it.hasNext())
            {
                Item i = it.next();
                if (i.getId() == item.getId())
                {
                    it.remove();
                    return;
                }
            }
        }

        /**
         * Returns the vertical index of this segment on the
         * parent map.
         * @return segment row for this row
         */
        public int getRow()
        {
            if (needsType())
                return 0;
            return topLeft.getY() / mapType.getSegmentSizeY();
        }

        /**
         * Returns horizontal index of this segment on the
         * parent map.
         * @return segment column for this row
         */
        public int getColumn()
        {
            if (needsType())
                return 0;
            return topLeft.getX() / mapType.getSegmentSizeX();
        }

        /** {@inheritDoc} */
        @Override
        public String toString()
        {
            return MessageFormat.format("[Segment: {0} on map ({1},{2})]",
                                        topLeft.toString(),
                                        MapInstance.this.getType().getId(),
                                        MapInstance.this.getInstanceNo());
        }
    }

    private class DummySegment extends Segment
    {
        public DummySegment()
        {
            super(null);
        }

        @Override
        public String toString()
        {
            return MessageFormat.format("[DUMMY Segment on map ({0},{1})]",
                                        MapInstance.this.getType() != null ? MapInstance.this.getType()
                                                                                             .getId()
                                                                          : null,
                                        MapInstance.this.getInstanceNo());
        }
    }

    // those have proper values only on server
    private int playersActorsCount;

    private MapType mapType;
    private short instanceNo;
    private Segment[][] segments;
    private int ySegmentCount;
    private int xSegmentCount;

    /**
     * Creates map with given map type, terrain set and
     * instance number.
     * @param instanceNo
     *            instance number of new map
     * @param mapType
     *            map type of new map, , may be {@code null}
     *            - subsequent call to
     *            {@link #applyMapType(MapType)} will be
     *            needed to make all functionality available
     * @param terrainSet
     *            terrain set used by given map type, may be
     *            {@code null} - subsequent call to
     *            {@link #applyTerrainSet(TerrainSet)} will
     *            be needed to make all functionality
     *            available
     */
    public MapInstance(short instanceNo, MapType mapType, TerrainSet terrainSet)
    {
        this.instanceNo = instanceNo;

        if (mapType != null)
        {
            if (terrainSet != null)
                mapType.assignTerrainSet(terrainSet);
            applyMapType(mapType);
        }
        else
        {
            segments = new Segment[1][1];
            segments[0][0] = new DummySegment();
        }
    }

    /**
     * Assigns map type to given map.
     * @param mapType
     *            map type to assign
     */
    public void applyMapType(MapType mapType)
    {
        assert mapType != null;
        this.mapType = mapType;
        xSegmentCount = mapType.getXSegmentCount();
        ySegmentCount = mapType.getYSegmentCount();

        Segment dummy;
        if (this.segments != null)
            dummy = getDummySegment();
        else
            dummy = null;

        this.segments = new Segment[xSegmentCount][ySegmentCount];

        int sizeX = mapType.getSizeX();
        int sizeY = mapType.getSizeY();

        byte segX = mapType.getSegmentSizeX();
        byte segY = mapType.getSegmentSizeX();

        for (short y = 0; y < sizeY; y += segY)
            for (short x = 0; x < sizeX; x += segX)
            {
                Position topLeft = new Position(x, y);
                Position segPos = getSegmentPosition(topLeft);
                segments[segPos.getX()][segPos.getY()] = new Segment(topLeft);
            }

        if (dummy != null)
        {
            for (Actor a : dummy.getActors())
            {
                Segment s = getSegmentForPosition(a.getPosition());
                s.putActor(a);
            }
            for (Item i : dummy.getItems())
            {
                Segment s = getSegmentForPosition(i.getPosition());
                s.putItem(i);
            }
        }
    }

    /**
     * Assigns terrain set to map type.
     * @param terrainSet
     *            terrain set to assign
     * @see MapType#assignTerrainSet(TerrainSet)
     */
    public void applyTerrainSet(TerrainSet terrainSet)
    {
        getType().assignTerrainSet(terrainSet);
    }

    /**
     * Returns actor on given position, or {@code null} if
     * no actor is standing on given position.
     * @param pos
     *            position of actor
     * @return actor on given position or {@code null}
     */
    public Actor getActor(Position pos)
    {
        if (needsType())
            return getDummySegment().getActor(pos);

        return getSegmentForPosition(pos).getActor(pos);
    }

    /**
     * Returns items on given position, or {@code null} if
     * no items are laying on given position.
     * @param pos
     *            position of item
     * @return item on given position or {@code null}
     */
    public List<Item> getItems(Position pos)
    {
        assert pos != null;

        if (needsType())
            return getDummySegment().getItems(pos);

        return getSegmentForPosition(pos).getItems(pos);
    }

    /**
     * Returns number of players actors currently on map.
     * Works properly only on server.
     * @return number of players actors currently on map
     */
    public int getPlayersActorsCount()
    {
        return playersActorsCount;
    }

    private Segment getDummySegment()
    {
        return segments[0][0];
    }

    /**
     * Returns list of tiles associated with entrance which
     * id is provided as argument.
     * @param id
     *            id of map entry
     * @return map entry represented by Entrance or null if
     *         id does not indicate entry
     */
    public EntryZone getEntrance(byte id)
    {
        if (needsType())
            return null;
        return mapType.getEntryZoneMap().get(id);
    }

    /**
     * Gets exit associated with position passed as
     * argument.
     * @param pos
     *            position to check
     * @return Exit when position indicates exit, null
     *         otherwise
     */
    public ExitZone getExit(Position pos)
    {
        if (needsType())
            return null;

        return mapType.getExitZonePositionsCacheMap().get(pos);
    }

    /**
     * Returns instance number of map.
     * @return instance number of map
     */
    public short getInstanceNo()
    {
        return instanceNo;
    }

    /**
     * Returns segment from segments table.
     * @param x
     *            x position of segment in segments table
     * @param y
     *            y position of segment in segments table
     * @return segments on given table position
     */
    public Segment getSegment(int x, int y)
    {
        if (needsType())
            return getDummySegment();
        return segments[x][y];
    }

    /**
     * Returns segment containing given position.
     * @param p
     *            position for which segment should be
     *            returned
     * @return segment containing given position
     */
    public Segment getSegmentForPosition(Position p)
    {
        if (needsType())
            return getDummySegment();
        int sx = p.getX() / mapType.getSegmentSizeX();
        int sy = p.getY() / mapType.getSegmentSizeY();
        return segments[sx][sy];
    }

    private Position getSegmentPosition(Position p)
    {
        if (needsType())
            return new Position((short) 0, (short) 0);
        int sx = p.getX() / mapType.getSegmentSizeX();
        int sy = p.getY() / mapType.getSegmentSizeY();
        return new Position((short) sx, (short) sy);
    }

    /**
     * Returns object representing type of map.
     * @return object representing type of map.
     */
    public MapType getType()
    {
        return mapType;
    }

    /**
     * Checks if tile at given position is passable.
     * TerrainSet should be assigned or {@code false} will
     * be returned.
     * @param p
     *            position for which passable should be
     *            checked
     * @return {@code true} if actor can be moved to given
     *         position
     */
    public boolean isPassable(Position p)
    {
        if (needsType() || needsTerrainSet())
            return false;
        TileType t = getType().getTile(p);
        if (t == null)
            return false;
        if (getActor(p) != null)
            return false;
        return t.isPassable();
    }

    /**
     * Returns {@code true} when this instance is 'ready' -
     * more players can be added.
     * @return whether or not more players can be added to
     *         this map
     */
    public boolean isReady()
    {
        if (needsType() || needsTerrainSet())
            return false;
        final int max = getType().getMaxActors();
        return max == 0 || max > getPlayersActorsCount();
    }

    /**
     * Checks if terrain sets was assigned.
     * @return {@code true} when map (map type) needs
     *         terrain set assignment
     */
    public boolean needsTerrainSet()
    {
        return needsType() || getType().getTerrainSet() == null;
    }

    /**
     * Checks if map type was assigned.
     * @return {@code true} when map needs map type
     *         assignment
     */
    public boolean needsType()
    {
        return getType() == null;
    }

    /**
     * Function provided for convenience. Provides object
     * returning {@link NeighborIterator} for given
     * position. May be useful for "for each" instruction.
     * @see #neighborIterator(Position)
     * @param p
     *            position for which neighbor iterator
     *            should be created
     * @return object returning neighbor iterator for given
     *         position
     */
    public Iterable<Segment> neighborhood(final Position p)
    {
        return new Iterable<Segment>()
        {
            @Override
            public Iterator<Segment> iterator()
            {
                return new NeighborIterator(getSegmentPosition(p));
            }
        };
    }

    /**
     * Returns segment iterator on neighborhood of segment
     * containing given position.
     * @param p
     *            position inside segment for which iterator
     *            should be returned
     * @return segment iterator on neighborhood of segment
     *         containing given position
     */
    public NeighborIterator neighborIterator(Position p)
    {
        return new NeighborIterator(getSegmentPosition(p));
    }

    /** {@inheritDoc} */
    @Override
    public String toString()
    {
        return String.format("MapInstance[mapType: %s, instacnceNo: %d]",
                             mapType == null ? "null"
                                            : Short.toString(mapType.getId()),
                             instanceNo);
    }
}
