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

import java.util.*;
import java.io.*;
import java.net.*;

import sjrd.tricktakinggame.network.*;
import sjrd.tricktakinggame.network.commands.*;
import sjrd.tricktakinggame.remotable.*;
import sjrd.tricktakinggame.rules.*;

import static sjrd.tricktakinggame.network.commands.ParameterKind.*;

/**
 * Répondeur à un administrateur
 * @author sjrd
 */
public class AdminResponder extends CommandBasedConnection
{
	/**
	 * Serveur
	 */
	private Server server;
	
	/**
	 * Crée le répondeur
	 * @param aServer Serveur
	 * @param aSocket Socket
	 */
	public AdminResponder(Server aServer, Socket aSocket) throws IOException
	{
		super(aSocket, true);

		server = aServer;

		getThread().setName("admin-responder");

		synchronized (commands)
		{
			commands.add(new CommandHelp());
			commands.add(new CommandPing());
			commands.add(new CommandTerminate());
			commands.add(new CommandListRules());
			commands.add(new CommandListTables());
			commands.add(new CommandListClients());
			commands.add(new CommandSendMessage());
		}
		
		getThread().start();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void onNetworkException(NetworkException error)
	{
		Server.logger.info(
			"To " + socket.getRemoteSocketAddress() + " (admin): " +
			error.getResponse());

		super.onNetworkException(error);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void onIOException(IOException error)
	{
		Server.logger.severe(
			"I/O error in admin responder execute() method");
		
		try
		{
			socket.close();
		}
		catch (IOException ignore)
		{
		}

		super.onIOException(error);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void onThreadTerminated()
	{
		Server.logger.info("Admin disconnected");

		super.onThreadTerminated();
	}

	/**
	 * Commande HELP
	 * @author sjrd
	 */
	private class CommandHelp implements Command
	{
		/**
		 * {@inheritDoc}
		 */
		public String getName()
		{
			return "HELP";
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
			return new ParameterKind[] {};
		}

		/**
		 * {@inheritDoc}
		 */
		public void execute(String[] parameters) throws IOException
		{
			writer.writeResponse(ResponseCode.OK);
			
			writer.println("Available commands:");

			synchronized (commands)
			{
				for (Command command: commands)
				{
					String line = command.getName();
					ParameterKind[] params = command.getParameterKinds();
					
					if (params == null)
						line += " ...";
					else
					{
						for (ParameterKind param: params)
							line += " " + param.toString();
					}
					
					writer.println(line);
				}
			}
		}
	}

	/**
	 * Commande PING
	 * @author sjrd
	 */
	private class CommandPing implements Command
	{
		/**
		 * {@inheritDoc}
		 */
		public String getName()
		{
			return "PING";
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
			return new ParameterKind[] {};
		}

		/**
		 * {@inheritDoc}
		 */
		public void execute(String[] parameters) throws IOException
		{
			writer.writeResponse(ResponseCode.Pong);
		}
	}

	/**
	 * Commande TERMINATE
	 * @author sjrd
	 */
	private class CommandTerminate implements Command
	{
		/**
		 * {@inheritDoc}
		 */
		public String getName()
		{
			return "TERMINATE";
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
			return new ParameterKind[] {};
		}

		/**
		 * {@inheritDoc}
		 */
		public void execute(String[] parameters) throws IOException
		{
			writer.writeResponse(ResponseCode.OK, "Will terminate soon");
			
			server.terminate();
		}
	}

	/**
	 * Commande LISTRULES
	 * @author sjrd
	 */
	private class CommandListRules implements Command
	{
		/**
		 * {@inheritDoc}
		 */
		public String getName()
		{
			return "LISTRULES";
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
			return new ParameterKind[] {};
		}

		/**
		 * {@inheritDoc}
		 */
		public void execute(String[] parameters) throws IOException
		{
			writer.writeResponse(ResponseCode.OK);
			writer.println(server.getRulesProviderCount());

			for (RulesProvider provider: server.getRulesProvidersIterable())
			{
				String line = provider.getID() + "\t";
				line += provider.getName() + "\t";
				line += provider.getMinPlayerCount() + "\t";
				line += provider.getMaxPlayerCount();

				writer.println(line);
			}
		}
	}

	/**
	 * Commande LISTTABLES
	 * @author sjrd
	 */
	private class CommandListTables implements Command
	{
		/**
		 * {@inheritDoc}
		 */
		public String getName()
		{
			return "LISTTABLES";
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
			return new ParameterKind[] {};
		}

		/**
		 * {@inheritDoc}
		 */
		public void execute(String[] parameters) throws IOException
		{
			writer.writeResponse(ResponseCode.OK);
			writer.println(server.getTableCount());

			for (Table table: server.getTablesIterable())
			{
				String line = table.getID() + "\t";
				line += table.getRulesProvider().getName() + "\t";
				line += table.getAwaitedPlayerCount() + "\t";
				line += table.getPlayerCount();

				writer.println(line);
			}
		}
	}

	/**
	 * Commande LISTCLIENTS
	 * @author sjrd
	 */
	private class CommandListClients implements Command
	{
		/**
		 * {@inheritDoc}
		 */
		public String getName()
		{
			return "LISTCLIENTS";
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
			return new ParameterKind[] {};
		}

		/**
		 * {@inheritDoc}
		 */
		public void execute(String[] parameters) throws IOException
		{
			List<ClientHandler> clients = new LinkedList<ClientHandler>();
			for (ClientHandler client: server.getClientsIterable())
				clients.add(client);
			
			writer.writeResponse(ResponseCode.OK);
			writer.println(clients.size());
			
			for (ClientHandler client: clients)
			{
				String line = client.getLoginName() + "\t";
				line +=
					client.getResponder().getSocket().getRemoteSocketAddress();
				line += "\t";
				line +=
					client.getCommander().getSocket().getRemoteSocketAddress();
				writer.println(line);
			}
		}
	}

	/**
	 * Commande SENDMESSAGE
	 * @author sjrd
	 */
	private class CommandSendMessage implements Command
	{
		/**
		 * {@inheritDoc}
		 */
		public String getName()
		{
			return "SENDMESSAGE";
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
			// Ecrire la réponse
			writer.writeResponse(ResponseCode.OK);
			
			// Créer le message
			Message message = new Message(MessageSource.Admin,
				String.format("Admin> %s", parameters[1]));

			// Envoyer le message
			server.sendMessageToAll(message);
		}
	}
}
