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
package pl.org.minions.stigma.game.command.data;

import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import pl.org.minions.stigma.game.TimestampedObject;
import pl.org.minions.stigma.game.command.Command;
import pl.org.minions.stigma.game.command.CommandType;
import pl.org.minions.stigma.game.command.data.sink.DataRequestSink;
import pl.org.minions.stigma.game.data.WorldData;
import pl.org.minions.stigma.game.event.Event;
import pl.org.minions.stigma.game.map.MapInstance.Segment;
import pl.org.minions.stigma.game.world.ExtendedWorld;
import pl.org.minions.stigma.globals.SizeOf;
import pl.org.minions.stigma.network.Buffer;

/**
 * Class representing generic request for data using
 * 'time-stamp protocol'.
 * @param <ObjectType>
 *            object type synchronized using this command
 */
abstract class GenericDataRequest<ObjectType extends TimestampedObject<ObjectType>> extends
                                                                                    Command implements
                                                                                           DataRequestSink<ObjectType>
{
    private List<RequestDesc> list = new LinkedList<RequestDesc>();

    /**
     * Constructor for derived types.
     * @param type
     *            type of command represented by this
     *            request
     */
    protected GenericDataRequest(CommandType type)
    {
        super(type);
    }

    /** {@inheritDoc} */
    @Override
    public final void add(ObjectType o)
    {
        list.add(new RequestDesc(o));
    }

    /** {@inheritDoc} */
    @Override
    protected final boolean commandSpecificDecode(Buffer buffer)
    {
        int size = buffer.decodeByte();
        list = new LinkedList<RequestDesc>();
        for (int i = 0; i < size; ++i)
            list.add(new RequestDesc(buffer));
        return true;
    }

    /** {@inheritDoc} */
    @Override
    protected final boolean commandSpecificEncode(Buffer buffer)
    {
        buffer.encode((byte) list.size());
        for (RequestDesc desc : list)
            if (!desc.encode(buffer))
                return false;
        return true;
    }

    /** {@inheritDoc} */
    @Override
    protected final int commandSpecificLength()
    {
        return SizeOf.BYTE + list.size() * RequestDesc.sizeOf();
    }

    /** {@inheritDoc} */
    @Override
    public final void compareAndAdd(ObjectType currentObject, int newestTS)
    {
        if (currentObject.getNewestTS() < newestTS)
            add(currentObject);
    }

    /** {@inheritDoc} */
    @Override
    public final void compareAndAdd(ObjectType currentObject,
                                    ObjectType newObject)
    {
        if (currentObject.compareTS(newObject))
            add(currentObject);
    }

    /**
     * Object-specific implementation of
     * {@link Command#innerExecute}. Should fill {@code
     * deltasForSender} basing on request descriptions from
     * {@code list}.
     * @param world
     *            [in] world to which requests of
     *            synchronization applies
     * @param deltasForSender
     *            [out] output deltas containing
     *            synchronization informations
     * @param list
     *            [in] requests list
     * @return {@code true} on successful processing of
     *         requests
     */
    protected abstract boolean fillDeltas(ExtendedWorld world,
                                          List<WorldData> deltasForSender,
                                          List<RequestDesc> list);

    /** {@inheritDoc} */
    @Override
    public final void forceAdd(ObjectType object)
    {
        // TS == 0 <- this will force synchronization
        list.add(new RequestDesc(object.getId(), 0));
    }

    /** {@inheritDoc} */
    @Override
    protected final boolean innerExecute(ExtendedWorld world,
                                         List<Event> output,
                                         List<WorldData> dataForSender,
                                         Set<Segment> affectedSegments)
    {
        if (!fillDeltas(world, dataForSender, list))
            return false;
        list.clear(); // so it will not be re sent to client
        return true;
    }

    /** {@inheritDoc} */
    @Override
    public final boolean isEmpty()
    {
        return list.isEmpty();
    }

    /** {@inheritDoc} */
    @SuppressWarnings("unchecked")
    @Override
    protected final boolean paramsEquals(Command cmd)
    {
        GenericDataRequest<ObjectType> other =
                (GenericDataRequest<ObjectType>) cmd;
        return other.list.equals(list);
    }

    /** {@inheritDoc} */
    @Override
    public final String toString()
    {
        StringBuilder buf = new StringBuilder();
        buf.append("DataRequest - ");
        buf.append(getType());

        for (RequestDesc desc : list)
        {
            desc.print(buf);
        }
        return buf.toString();
    }

}
