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
package pl.org.minions.stigma.network.messaging.game;

import pl.org.minions.stigma.game.command.Command;
import pl.org.minions.stigma.network.Buffer;
import pl.org.minions.stigma.network.messaging.NetworkMessage;
import pl.org.minions.stigma.network.messaging.NetworkMessageType;

/**
 * Message transporting command.
 * @see pl.org.minions.stigma.game.command.Command
 */
public class CommandMessage extends NetworkMessage
{
    private Command command;

    private boolean asResponseForSender;

    private CommandMessage()
    {
        super(NetworkMessageType.COMMAND);
    }

    /**
     * Constructor provided for convenience. Same as {@code
     * CommandMessage(command, false)}.
     * @param command
     *            command to send
     */
    public CommandMessage(Command command)
    {
        this(command, false);
    }

    /**
     * Creates message transporting command. Command may be
     * "before" or "after" execution (with or without
     * deltas).
     * @param command
     *            command to send
     * @param asResponeForSender
     *            should be {@code true} if command must be
     *            encoded with deltas "for sender only" (
     *            {@link Command#setEncodeForSender(boolean)}
     *            )
     */
    public CommandMessage(Command command, boolean asResponeForSender)
    {
        this();
        this.command = command;
        this.asResponseForSender = asResponeForSender;
    }

    /**
     * Creates empty object (needed for parsing network
     * message) Object state and information are unknown.
     * @return empty object
     */
    public static CommandMessage create()
    {
        return new CommandMessage();
    }

    /** {@inheritDoc} */
    @Override
    protected boolean decodeParams(Buffer buffer)
    {
        command = Command.getCodec().decode(buffer);
        return command != null;
    }

    /** {@inheritDoc} */
    @Override
    protected boolean encodeParams(Buffer buffer)
    {
        boolean b;
        synchronized (command)
        {
            command.setEncodeForSender(asResponseForSender);
            b = Command.getCodec().encode(command, buffer);
        }
        return b;
    }

    /**
     * Returns command wrapped by this message.
     * @return command wrapped by this message.
     */
    public Command getCommand()
    {
        return command;
    }

    /** {@inheritDoc} */
    @Override
    protected int innerGetParamsLength()
    {
        int r;
        synchronized (command)
        {
            command.setEncodeForSender(asResponseForSender);
            r = command.getParamsLength();
        }
        return r;
    }
}
