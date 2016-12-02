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
package pl.org.minions.stigma.game.command.special;

import java.util.Arrays;
import java.util.List;
import java.util.Set;

import pl.org.minions.stigma.game.command.Command;
import pl.org.minions.stigma.game.command.CommandType;
import pl.org.minions.stigma.game.data.WorldData;
import pl.org.minions.stigma.game.event.Event;
import pl.org.minions.stigma.game.map.MapInstance;
import pl.org.minions.stigma.game.map.MapInstance.Segment;
import pl.org.minions.stigma.game.world.ExtendedWorld;
import pl.org.minions.stigma.network.Buffer;

/**
 * "Some events appeared without any apparent reason"
 * command. Should be created only on server. Events won't
 * be coded, so client cannot change world on server side.
 */
public class ApplyEvents extends Command
{
    private MapInstance.Segment segment;
    private List<Event> eventsToApply;

    /**
     * Constructor.
     * @param segment
     *            segment on which events appeared
     * @param eventsToApply
     *            events which should be send
     */
    public ApplyEvents(Segment segment, List<Event> eventsToApply)
    {
        this();
        this.segment = segment;
        this.eventsToApply = eventsToApply;
    }

    /**
     * Constructor.
     * @param segment
     *            segment on which events appeared
     * @param events
     *            events which should be send
     */
    public ApplyEvents(Segment segment, Event... events)
    {
        this();
        this.segment = segment;
        this.eventsToApply = Arrays.asList(events);
    }

    private ApplyEvents()
    {
        super(CommandType.APPLY_EVENTS);
    }

    /** {@inheritDoc} */
    @Override
    protected boolean commandSpecificDecode(Buffer buffer)
    {
        return true;
    }

    /** {@inheritDoc} */
    @Override
    protected boolean commandSpecificEncode(Buffer buffer)
    {
        return true;
    }

    /** {@inheritDoc} */
    @Override
    protected int commandSpecificLength()
    {
        return 0;
    }

    /** {@inheritDoc} */
    @Override
    protected boolean innerExecute(ExtendedWorld world,
                                   List<Event> output,
                                   List<WorldData> dataForSender,
                                   Set<Segment> affectedSegments)
    {
        output.addAll(eventsToApply);
        if (segment != null)
            affectedSegments.add(segment);
        return true;
    }

    /** {@inheritDoc} */
    @Override
    protected boolean paramsEquals(Command cmd)
    {
        // This command should not be compared
        return false;
    }

    /** {@inheritDoc} */
    @Override
    public String toString()
    {
        return "ApplyEvents";
    }

    /**
     * Creates empty object (needed for decoding).
     * @return empty object
     */
    public static Command create()
    {
        return new ApplyEvents();
    }

    /**
     * Performs dummy execute - should be called when we
     * need this command just to synchronize existing data,
     * not to provide real modifications.
     */
    public void dummyExecute()
    {
        execute(null); // for THIS command this SHOULD work
    }

}
