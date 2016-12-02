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

import java.util.logging.*;
import java.io.*;
import java.net.*;

import sjrd.tricktakinggame.network.*;
import sjrd.tricktakinggame.network.commands.*;

import static sjrd.tricktakinggame.network.commands.ParameterKind.*;

/**
 * Connexion initiale avec un client
 * @author sjrd
 */
public class ClientInitialConnection extends CommandBasedConnection
{
	/**
	 * Serveur
	 */
	private Server server;

	/**
	 * Crée la connexion initiale avec le client
	 * @param aSocket Socket
	 */
	public ClientInitialConnection(Server aServer, Socket socket)
		throws IOException
	{
		super(socket, true);

		server = aServer;

		getThread().setName("initial-connection:" +
			socket.getRemoteSocketAddress());

		writer.writeResponse(ResponseCode.OK, "Welcome");

		synchronized (commands)
		{
			commands.add(new CommandFirstConnect());
			commands.add(new CommandSecondConnect());
			commands.add(new CommandAdmin());
			commands.add(new CommandLogger());
		}
		
		getThread().setDaemon(true);
		getThread().start();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void onNetworkException(NetworkException error)
	{
		Server.logger.info(
			"To " + socket.getRemoteSocketAddress() +
			" (initial connection): " + error.getResponse());

		super.onNetworkException(error);
		
		try
		{
			socket.close();
		}
		catch (IOException subError)
		{
			onIOException(subError);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void onIOException(IOException error)
	{
		Server.logger.warning(
			"I/O Error during initial connection with client. IP: " +
			socket.getRemoteSocketAddress());

		super.onIOException(error);
		
		try
		{
			socket.close();
		}
		catch (IOException subError)
		{
		}
	}

	/**
	 * Commande CONNECT
	 * @author sjrd
	 */
	private class CommandFirstConnect implements Command
	{
		/**
		 * {@inheritDoc}
		 */
		public String getName()
		{
			return "CONNECT";
		}

		/**
		 * {@inheritDoc}
		 */
		public boolean isAvailable()
		{
			return true;
		}

		/**
		 * {@inheritDoc}
		 */
		public ParameterKind[] getParameterKinds()
		{
			return new ParameterKind[] {pkString, pkString};
		}

		/**
		 * {@inheritDoc}
		 */
		public void execute(String[] parameters) throws IOException
		{
			// Read parameters
			String loginName = parameters[1];
			String passwordHash = parameters[2];
			
			// Create handler
			ClientHandler clientHandler = server.connectClient(socket,
				loginName, passwordHash);
			
			// Write response
			writer.writeResponse(ResponseCode.FirstConnectionOK);
			writer.println(clientHandler.getSessionCode());

			Server.logger.info("New client - IP: " +
				socket.getRemoteSocketAddress() + " - User name: " + loginName);
			
			// Interrupt the initial connection thread
			getThread().interrupt();
		}
	}

	/**
	 * Commande SNDCONNECT
	 * @author sjrd
	 */
	private class CommandSecondConnect implements Command
	{
		/**
		 * {@inheritDoc}
		 */
		public String getName()
		{
			return "SNDCONNECT";
		}

		/**
		 * {@inheritDoc}
		 */
		public boolean isAvailable()
		{
			return true;
		}

		/**
		 * {@inheritDoc}
		 */
		public ParameterKind[] getParameterKinds()
		{
			return new ParameterKind[] {pkString, pkInteger};
		}

		/**
		 * {@inheritDoc}
		 */
		public void execute(String[] parameters) throws IOException
		{
			String loginName;
			int sessionCode;

			// Read parameters
			loginName = parameters[1];
			sessionCode = Integer.parseInt(parameters[2]);

			// Find client handler
			ClientHandler client = server.findClientByLoginName(loginName);
			if (client == null)
				throw new NetworkException(ResponseCode.InvalidLogin);

			// Check session-code
			if (client.getSessionCode() != sessionCode)
				throw new NetworkException(ResponseCode.InvalidSessionCode);

			// Session-code matches
			client.setCommanderSocket(socket);
			writer.writeResponse(ResponseCode.SecondConnectionOK);

			Server.logger.info("Commander connected - IP: " +
				socket.getRemoteSocketAddress() + " - User name: " + loginName);
			
			// Interrupt the initial connection thread
			getThread().interrupt();
		}
	}

	/**
	 * Commande ADMIN
	 * @author sjrd
	 */
	private class CommandAdmin implements Command
	{
		/**
		 * {@inheritDoc}
		 */
		public String getName()
		{
			return "ADMIN";
		}

		/**
		 * {@inheritDoc}
		 */
		public boolean isAvailable()
		{
			return true;
		}

		/**
		 * {@inheritDoc}
		 */
		public ParameterKind[] getParameterKinds()
		{
			return new ParameterKind[] {pkString};
		}

		/**
		 * {@inheritDoc}
		 */
		public void execute(String[] parameters) throws IOException
		{
            // Check password
            String passwordHash = parameters[1];
            AdminChecker checker = server.getAdminChecker();
            if ((checker == null) || !checker.checkPassword(passwordHash))
                throw new NetworkException(ResponseCode.InvalidAdmin);
            
			// Write response
			writer.writeResponse(ResponseCode.AdminConnectionOK);

			Server.logger.info("Admin connected - IP: " +
				socket.getRemoteSocketAddress());
			
			// Begin admin thread
			new AdminResponder(server, socket);
			
			// Interrupt the initial connection thread
			getThread().interrupt();
		}
	}

	/**
	 * Commande LOGGER
	 * @author sjrd
	 */
	private class CommandLogger implements Command
	{
		/**
		 * {@inheritDoc}
		 */
		public String getName()
		{
			return "LOGGER";
		}

		/**
		 * {@inheritDoc}
		 */
		public boolean isAvailable()
		{
			return true;
		}

		/**
		 * {@inheritDoc}
		 */
		public ParameterKind[] getParameterKinds()
		{
			return new ParameterKind[] {pkString};
		}

		/**
		 * {@inheritDoc}
		 */
		public void execute(String[] parameters) throws IOException
		{
			// Read argument
			Level level = Level.OFF;
			
			try
			{
				level = Level.parse(parameters[1]);
			}
			catch (IllegalArgumentException error)
			{
				new Response(ResponseCode.Protocol,
					error.getMessage()).throwException();
			}
			
			// Write response
			writer.writeResponse(ResponseCode.LoggerConnectionOK);

			Server.logger.info("Logger "+level.getName()+" connected - IP: " +
				socket.getRemoteSocketAddress());
			
			// Create distant logger
			new DistantLogger(socket, level);
			
			// Interrupt the initial connection thread
			getThread().interrupt();
		}
	}
}
