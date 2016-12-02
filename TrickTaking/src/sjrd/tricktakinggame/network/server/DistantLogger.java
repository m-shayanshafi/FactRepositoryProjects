/*
 * TrickTakingGame - Trick-taking games platform on-line
 * Copyright (C) 2008  Sébastien Doeraene
 * All Rights Reserved
 *
 * This file is part of TrickTakingGame.
 *
 * TrickTakingGame is free software: you can redistribute it and/or modify it
 * under the terms of the GNU General Public License as published by the Free
 * Software Foundation, either version 3 of the License, or (at your option) any
 * later version.
 *
 * TrickTakingGame is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for
 * more details.
 *
 * You should have received a copy of the GNU General Public License along with
 * TrickTakingGame.  If not, see <http://www.gnu.org/licenses/>.
 */
package sjrd.tricktakinggame.network.server;

import java.io.*;
import java.net.*;
import java.util.logging.*;

import sjrd.tricktakinggame.network.*;

/**
 * Logger distant
 * @author sjrd
 */
public class DistantLogger extends NetworkConnection
{
	/**
	 * Gestionnaire
	 */
	private final LogHandler handler = new LogHandler();
	
	/**
	 * Référence au logger pour les classes internes
	 */
	private final DistantLogger owner = this;
	
	/**
	 * Crée le logger
	 * @param aSocket Socket
	 */
	public DistantLogger(Socket aSocket, Level level) throws IOException
	{
		super(aSocket);
		
		handler.setLevel(level);
		handler.setFormatter(new SimpleFormatter());
		Server.addLogHandler(handler);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void close() throws IOException
	{
		Server.removeLogHandler(handler);

		super.close();
	}

	/**
	 * Gestionnaire de log
	 * @author sjrd
	 */
	public class LogHandler extends Handler
	{
		/**
		 * {@inheritDoc}
		 */
		@Override
		public void close() throws SecurityException
		{
			try
			{
				owner.close();
			}
			catch (IOException ignore)
			{
			}
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public void flush()
		{
			writer.flush();
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public void publish(LogRecord record)
		{
			if (isLoggable(record))
			{
				writer.print(getFormatter().format(record));
				if (writer.checkError())
					close();
			}
		}
	}
}
