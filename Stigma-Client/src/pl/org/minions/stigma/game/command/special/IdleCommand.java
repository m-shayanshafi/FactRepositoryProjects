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

import java.util.List;
import java.util.Set;

import pl.org.minions.stigma.game.command.Command;
import pl.org.minions.stigma.game.command.CommandType;
import pl.org.minions.stigma.game.data.WorldData;
import pl.org.minions.stigma.game.event.Event;
import pl.org.minions.stigma.game.map.MapInstance.Segment;
import pl.org.minions.stigma.game.world.ExtendedWorld;
import pl.org.minions.stigma.network.Buffer;
import pl.org.minions.utils.logger.Log;

/**
 * Special class for representing "no action". Cannot be
 * encoded, decoded or executed. Should be used for
 * transferring 'NOOP' as 'next action' (for example on
 * client), to distinguish 'noop' from 'null'.
 */
public class IdleCommand extends Command
{
    /**
     * Constructor.
     */
    public IdleCommand()
    {
        super(CommandType.IDLE_COMMAND);
    }

    /** {@inheritDoc} */
    @Override
    protected boolean commandSpecificDecode(Buffer buffer)
    {
        Log.logger.fatal("Decoding IdleCommand");
        return false;
    }

    /** {@inheritDoc} */
    @Override
    protected boolean commandSpecificEncode(Buffer buffer)
    {
        Log.logger.fatal("Encoding IdleCommand");
        return false;
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
        Log.logger.fatal("Executing IdleCommand");
        return false;
    }

    /** {@inheritDoc} */
    @Override
    protected boolean paramsEquals(Command cmd)
    {
        return true;
    }

    /** {@inheritDoc} */
    @Override
    public String toString()
    {
        return "IdleCommand";
    }

}
