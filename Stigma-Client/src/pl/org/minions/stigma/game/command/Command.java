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
package pl.org.minions.stigma.game.command;

import java.text.MessageFormat;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import pl.org.minions.stigma.game.command.data.sink.DataRequestSinks;
import pl.org.minions.stigma.game.data.WorldData;
import pl.org.minions.stigma.game.event.Event;
import pl.org.minions.stigma.game.map.MapInstance;
import pl.org.minions.stigma.game.map.MapInstance.Segment;
import pl.org.minions.stigma.game.world.ExtendedWorld;
import pl.org.minions.stigma.game.world.World;
import pl.org.minions.stigma.globals.SizeOf;
import pl.org.minions.stigma.network.Buffer;
import pl.org.minions.stigma.network.messaging.NetworkObject;
import pl.org.minions.stigma.network.messaging.NetworkObjectCodec;
import pl.org.minions.utils.logger.Log;

/**
 * Class representing "command" which modifies world. Effect
 * of command is stored in list of {@link WorldData}, which
 * can be send to client, or applied to any other world
 * instance.
 */
public abstract class Command extends NetworkObject<CommandType>
{
    private static final NetworkObjectCodec<CommandType, Command> CODEC =
            new NetworkObjectCodec<CommandType, Command>(CommandType.class);

    private static final int CONST_SIZE = SizeOf.INT // for requesterId
        + SizeOf.SHORT // for data for sender count (or for 0 if none)
        + SizeOf.SHORT; // for event count 

    private List<Event> events;
    private List<WorldData> dataForSender;
    private Set<Segment> affectedSegments;
    private boolean encodeForSender;
    private int requesterId;
    private boolean decoded;

    /**
     * Creates command with given type.
     * @param type
     *            type of command.
     */
    protected Command(CommandType type)
    {
        super(type);
    }

    /**
     * Returns codec for encoding/decoding all commands.
     * @return codec for encoding/decoding all commands.
     */
    public static NetworkObjectCodec<CommandType, Command> getCodec()
    {
        return CODEC;
    }

    /**
     * Returns iterable that enables iteration over all
     * affected segments. A provided iterator will iterate
     * through segments from which originally affected
     * segments were visible - "neighbors segments".
     * <p>
     * This function will work only after execution.
     * <p>
     * This method is not intended to work on client.
     * <p>
     * Do not call {@link Iterator#remove()} on provided
     * iterator.
     * @return iterable over all affected segments
     */
    public final Iterable<Segment> affectedSegments()
    {
        return affectedSegments;
    }

    /**
     * Applies command's deltas to given world.
     * @param world
     *            world to modify
     * @param requestSink
     *            sink for requests for actor data updates,
     *            may be {@code null}, for use on client
     *            only
     * @return {@code true} when every delta was applied
     *         successfully
     */
    public final boolean apply(World world, DataRequestSinks requestSink)
    {
        for (WorldData d : dataForSender)
            // on server apply only some, after decoding on client, apply all
            if (!d.getType().isClientOnly() || decoded)
            {
                if (!d.apply(world, requestSink))
                {
                    if (Log.isTraceEnabled())
                        Log.logger.trace(MessageFormat.format("Applying of data (for sender only): {0} failed for command: {1}",
                                                              d,
                                                              this));
                    return false;
                }
                else if (Log.isTraceEnabled())
                {
                    Log.logger.trace(MessageFormat.format("Applying of data (for sender only): {0} succeeded for command: {1}",
                                                          d,
                                                          this));
                }
            }

        for (Event e : events)
            if (!e.apply(world, requestSink))
            {
                if (Log.isTraceEnabled())
                    Log.logger.trace(MessageFormat.format("Applying of event: {0} failed for command: {1}",
                                                          e,
                                                          this));
                return false;
            }
            else if (Log.isTraceEnabled())
            {
                Log.logger.trace(MessageFormat.format("Applying of event: {0} succeeded for command: {1}",
                                                      e,
                                                      this));
            }

        return true;
    }

    /**
     * Decodes derived command specific parameters from
     * given buffer.
     * @param buffer
     *            buffer to decode from
     * @return {@code true} when decoding was successful
     */
    protected abstract boolean commandSpecificDecode(Buffer buffer);

    /**
     * Encodes derived command specific parameters on given
     * buffer.
     * @param buffer
     *            buffer to encode to
     * @return {@code true} when encoding was successful
     */
    protected abstract boolean commandSpecificEncode(Buffer buffer);

    /**
     * Returns bytes count for encoding derived command's
     * parameters.
     * @return bytes count for encoding derived command's
     *         parameters.
     */
    protected abstract int commandSpecificLength();

    /** {@inheritDoc} */
    @Override
    protected final boolean decodeParams(Buffer buffer)
    {
        this.requesterId = buffer.decodeInt();

        if (!commandSpecificDecode(buffer))
            return false;

        int len = buffer.decodeShort();
        events = new LinkedList<Event>();
        for (int i = 0; i < len; ++i)
        {
            Event e = Event.getCodec().decode(buffer);
            if (e == null)
                return false;
            events.add(e);
        }

        len = buffer.decodeShort();
        dataForSender = new LinkedList<WorldData>();
        for (int i = 0; i < len; ++i)
        {
            WorldData d = WorldData.getCodec().decode(buffer);
            if (d == null)
                return false;
            dataForSender.add(d);
        }
        decoded = true;

        return true;
    }

    /**
     * Returns an iterable that allows iteration over events
     * caused by this command.
     * <p>
     * Result is proper only after execution of command (on
     * after decoding executed command).
     * <p>
     * Do not call {@link Iterator#remove()} on provided
     * iterator.
     * @return iterable of events caused by this command.
     */
    public final Iterable<Event> events()
    {
        return events;
    }

    /**
     * Returns an iterable that allows iteration over data
     * updates caused by this command.
     * <p>
     * Result is proper only after execution of command (on
     * after decoding executed command).
     * <p>
     * Do not call {@link Iterator#remove()} on provided
     * iterator.
     * @return iterable of data updates caused by this
     *         command.
     */
    public final Iterable<WorldData> datas()
    {
        return dataForSender;
    }

    /** {@inheritDoc} */
    @Override
    protected final boolean encodeParams(Buffer buffer)
    {
        buffer.encode(requesterId);

        if (!commandSpecificEncode(buffer))
            return false;

        if (events == null)
        {
            buffer.encode((short) 0);
            buffer.encode((short) 0);
            return true;
        }

        int size = events.size();
        buffer.encode((short) size);

        for (Event e : events)
            if (!Event.getCodec().encode(e, buffer))
                return false;

        if (encodeForSender)
        {
            size = dataForSender.size();
            buffer.encode((short) size);

            for (WorldData d : dataForSender)
                if (!WorldData.getCodec().encode(d, buffer))
                    return false;
        }
        else
            buffer.encode((short) 0);

        return true;
    }

    /**
     * Compares two commands. Does not compare associated
     * deltas, but only type and parameters.
     * @param obj
     *            other object to compare
     * @return {@code true} only when {@code obj} is command
     *         with exactly the same meaning as {@code this}
     */
    @Override
    public boolean equals(Object obj)
    {
        if (obj == null)
            return false;
        if (this == obj)
            return true;
        if (!(obj instanceof Command))
            return false;
        Command cmd = (Command) obj;
        if (cmd.getType() != getType())
            return false;
        if (cmd.getRequesterId() != getRequesterId())
            return false;
        return paramsEquals(cmd);
    }

    /**
     * Executes command on given world. Generates deltas of
     * world and list of affected map segments. Does not
     * modify world.
     * @param world
     *            world to execute command on
     * @return {@code true} if command was executed
     *         successfully
     */
    public final boolean execute(ExtendedWorld world)
    {
        events = new LinkedList<Event>();
        dataForSender = new LinkedList<WorldData>();
        encodeForSender = false;
        decoded = false;

        Set<MapInstance.Segment> tempAffectedSegments =
                new HashSet<MapInstance.Segment>();

        if (innerExecute(world, events, dataForSender, tempAffectedSegments))
        {
            affectedSegments = new HashSet<MapInstance.Segment>();
            for (MapInstance.Segment s : tempAffectedSegments)
            {
                for (Segment s1 : s.neighborhood())
                    affectedSegments.add(s1);
            }
            return true;
        }
        else
        {
            // 'reverts transaction'
            events.clear();
            dataForSender.clear();
            return false;
        }
    }

    /**
     * Returns id of actor who requested execution of this
     * command.
     * @return id of actor who requested execution of this
     *         command
     */
    public final int getRequesterId()
    {
        return requesterId;
    }

    /**
     * Checks whether command failed or not. It means there
     * are no deltas to apply.
     * @return {@code true} when command failed.
     */
    public boolean hasFailed()
    {
        return this.events.isEmpty() && this.dataForSender.isEmpty();
    }

    /** {@inheritDoc} */
    @Override
    public int hashCode()
    {
        // just to remove checkstyle warnings, maybe someday more complicated implementation
        return super.hashCode();
    }

    /**
     * Real execute of command. Must be implemented in
     * subclasses.
     * @param world
     *            [in] world on which deltas should be
     *            generated
     * @param output
     *            [out] list of events generated by this
     *            command
     * @param dataForSender
     *            [out] list of data which should be send to
     *            command author, they will be applied
     *            BEFORE "output"
     * @param affectedSegments
     *            [out] list of map segments affected by
     *            this command
     * @return {@code true} when command executed
     *         successfully
     */
    protected abstract boolean innerExecute(ExtendedWorld world,
                                            List<Event> output,
                                            List<WorldData> dataForSender,
                                            Set<MapInstance.Segment> affectedSegments);

    /** {@inheritDoc} */
    @Override
    protected final int innerGetParamsLength()
    {
        int r = CONST_SIZE;
        r += commandSpecificLength();
        if (events == null) // wasn't executed
            return r;
        for (Event e : events)
            r += e.getParamsLength();
        if (encodeForSender)
            for (WorldData d : dataForSender)
                r += d.getParamsLength();
        return r;
    }

    /**
     * Compares parameters of two commands.
     * @param cmd
     *            command to compare, should be in proper
     *            type (no {@code instanceof} needed)
     * @return {@code true} only when parameters are exactly
     *         the same
     */
    protected abstract boolean paramsEquals(Command cmd);

    /**
     * Changes command encoding behavior. If parameter is
     * {@code true} all next encoding will encode
     * "deltas for sender only".
     * @param encodeForSender
     *            new value of "encode deltas for sender"
     *            parameter
     */
    public final void setEncodeForSender(boolean encodeForSender)
    {
        this.encodeForSender = encodeForSender;
    }

    /**
     * Sets id of actor who requested execution of this
     * command. Should be called only on server, and called
     * only once.
     * @param requesterId
     *            new id of requester
     */
    public final void setRequesterId(int requesterId)
    {
        this.requesterId = requesterId;
    }

    /** {@inheritDoc} */
    @Override
    public abstract String toString();
}
