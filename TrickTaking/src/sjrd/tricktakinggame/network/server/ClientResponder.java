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

import sjrd.tricktakinggame.cards.*;
import sjrd.tricktakinggame.game.*;
import sjrd.tricktakinggame.remotable.*;
import sjrd.tricktakinggame.rules.*;
import sjrd.tricktakinggame.network.*;
import sjrd.tricktakinggame.network.commands.*;

import static sjrd.tricktakinggame.network.commands.ParameterKind.*;

/**
 * Répondeur à un client
 * @author sjrd
 */
public class ClientResponder extends CommandBasedConnection
{
	/**
	 * Gestionnaire de client
	 */
	private ClientHandler handler;

	/**
	 * Serveur
	 */
	private Server server;

	/**
	 * Crée le répondeur d'un gestionnaire de client
	 * @param aHandler Gestionnaire du client
	 * @param aSocket Socket
	 */
	public ClientResponder(ClientHandler aHandler, Socket socket)
		throws IOException
	{
		super(socket);

		handler = aHandler;
		server = handler.getServer();

		getThread().setName("responder:" + handler.getLoginName());

		synchronized (commands)
		{
			commands.add(new CommandPing());
		}
	}

	/**
	 * Démarre réellement le répondeur du client
	 * <p>
	 * Avant l'appel à <tt>start()</tt>, la seule commande acceptée est la
	 * commande <tt>PING</tt>.
	 * </p>
	 */
	void start()
	{
		synchronized (commands)
		{
			commands.add(new CommandStatus());
			commands.add(new CommandFetchMessages());
			commands.add(new CommandListRules());
			commands.add(new CommandListTables());
			commands.add(new CommandCreateTable());
			commands.add(new CommandJoinTable());
			commands.add(new CommandLeaveTable());
			commands.add(new CommandGameStaticInfo());
			commands.add(new CommandBeginGame());
			commands.add(new CommandGameDynamicInfo());
			commands.add(new CommandChatMessage());
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void onNetworkException(NetworkException error)
	{
		Server.logger.info(
			"To " + socket.getRemoteSocketAddress() + " (" +
			handler.getLoginName() + "): "+ error.getResponse());

		super.onNetworkException(error);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void onIOException(IOException error)
	{
		Server.logger.severe(
			"I/O error in client responder execute() method");
		handler.tryClose();

		super.onIOException(error);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void onThreadTerminated()
	{
		handler.tryClose();

		super.onThreadTerminated();
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void executeCommand(Command command, String[] commandLine)
		throws IOException
	{
		if ((command instanceof CommandPing) ||
			(command instanceof CommandStatus))
		{
			super.executeCommand(command, commandLine);
		}
		else
		{
			String displayCommandLine = command.getName();
			for (int i = 1; i < commandLine.length; i++)
				displayCommandLine += " " + commandLine[i];

			Server.logger.finer(String.format("From player %s: doing %s",
				handler.getLoginName(), displayCommandLine));

			super.executeCommand(command, commandLine);

			Server.logger.finer(String.format("From player %s: done %s",
				handler.getLoginName(), displayCommandLine));
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
	 * Commande STATUS
	 * @author sjrd
	 */
	private class CommandStatus implements Command
	{
		/**
		 * {@inheritDoc}
		 */
		public String getName()
		{
			return "STATUS";
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
			Table table = handler.getTable();
			List<String> addInfo = new LinkedList<String>();

			if ((table == null) || table.isClosing())
			{
				// No table
				writer.writeResponse(ResponseCode.StatusNoTable);
			}
			else
			{
				// At table
				
				if (!table.isFull())
					writer.writeResponse(ResponseCode.StatusWaitingForPlayers);
				else if (!table.isPlaying())
					writer.writeResponse(ResponseCode.StatusNotPlaying);
				else if (table.getGame().isAuctioning())
					writer.writeResponse(ResponseCode.StatusAuctioning);
				else
					writer.writeResponse(ResponseCode.StatusPlaying);

				// Additional info
				
				int selfPosition = -1;

				addInfo.add("TableID=" + table.getID());
				addInfo.add("RulesName=" + table.getRulesProvider().getName());
				addInfo.add("AwaitedPlayerCount=" +
					table.getAwaitedPlayerCount());

				String[] playerNames = table.getPlayerNames();
				addInfo.add("PlayerCount=" + playerNames.length);
				for (int i = 0; i < playerNames.length; i++)
				{
					addInfo.add(String.format("PlayerName%d=%s", i,
						playerNames[i]));
					if (playerNames[i].equals(handler.getLoginName()))
						selfPosition = i;
				}
				
				addInfo.add("SelfPosition=" + selfPosition);
				addInfo.add("WantToBeginGame=" +
					(handler.doesWantToBeginGame() ? 1 : 0));
			}
			
			// In both cases, addInfo contains messages and updateTickCount
			addInfo.add("HasPendingMessages=" +
				(handler.hasPendingMessages() ? 1 : 0));
			addInfo.add("UpdateTickCount=" + handler.getUpdateTickCount());
			
			// Write addInfo
			writer.println(addInfo.size());
			for (String line: addInfo)
				writer.println(line);
		}
	}

	/**
	 * Commande FETCHMESSAGES
	 * @author sjrd
	 */
	private class CommandFetchMessages implements Command
	{
		/**
		 * {@inheritDoc}
		 */
		public String getName()
		{
			return "FETCHMESSAGES";
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
			List<Message> messages = handler.fetchPendingMessages();
			
			writer.writeResponse(ResponseCode.OK);
			writer.println(messages.size());
			
			for (Message message: messages)
			{
				writer.writeString(message.getSource().name());
				writer.writeString(message.getMessage());
			}
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
	 * Commande CREATETABLE
	 * @author sjrd
	 */
	private class CommandCreateTable implements Command
	{
		/**
		 * {@inheritDoc}
		 */
		public String getName()
		{
			return "CREATETABLE";
		}

		/**
		 * {@inheritDoc}
		 */
		public boolean isAvailable()
		{
			return handler.getTable() == null;
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
			String rulesProviderID = parameters[1];
			int playerCount = Integer.parseInt(parameters[2]);

			Table table = server.openTable(rulesProviderID, playerCount);
			table.addPlayer(handler);

			writer.writeResponse(ResponseCode.OK);
		}
	}

	/**
	 * Commande JOINTABLE
	 * @author sjrd
	 */
	private class CommandJoinTable implements Command
	{
		/**
		 * {@inheritDoc}
		 */
		public String getName()
		{
			return "JOINTABLE";
		}

		/**
		 * {@inheritDoc}
		 */
		public boolean isAvailable()
		{
			return handler.getTable() == null;
		}

		/**
		 * {@inheritDoc}
		 */
		public ParameterKind[] getParameterKinds()
		{
			return new ParameterKind[] {pkInteger};
		}

		/**
		 * {@inheritDoc}
		 */
		public void execute(String[] parameters) throws IOException
		{
			int tableID = Integer.parseInt(parameters[1]);

			Table table = server.findTableByID(tableID);

			if (table.addPlayer(handler))
				writer.writeResponse(ResponseCode.OK);
			else
				throw new NetworkException(ResponseCode.TableIsFull);
		}
	}

	/**
	 * Commande LEAVETABLE
	 * @author sjrd
	 */
	private class CommandLeaveTable implements Command
	{
		/**
		 * {@inheritDoc}
		 */
		public String getName()
		{
			return "LEAVETABLE";
		}

		/**
		 * {@inheritDoc}
		 */
		public boolean isAvailable()
		{
			return (handler.getTable() != null) &&
			(!handler.getTable().isPlaying());
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
			if (handler.getTable().removePlayer(handler))
				writer.writeResponse(ResponseCode.OK);
			else
				throw new NetworkException(ResponseCode.Forbidden);
		}
	}

	/**
	 * Commande GAMESTATICINFO
	 * @author sjrd
	 */
	private class CommandGameStaticInfo implements Command
	{
		/**
		 * {@inheritDoc}
		 */
		public String getName()
		{
			return "GAMESTATICINFO";
		}

		/**
		 * {@inheritDoc}
		 */
		public boolean isAvailable()
		{
			Table table = handler.getTable();
			return (table != null) && table.isFull();
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
			Game game = handler.getTable().getGame();

			writer.writeResponse(ResponseCode.OK);

			// Deck information

			Deck deck = game.getDeck();
			writer.println(deck.getOriginalSize());
			
			for (Card card: deck.originalCardsIterable())
			{
				writer.println(String.format("%s\t%d\t%s\t%s",
					card.getSuit().name(), card.getForce(),
					card.getName(), card.getDrawID()));
			}

			// Players and teams information

			writer.println(game.getTeamCount());
			writer.println(game.getPlayerCount());

			for (Player player: game.playersIterable())
			{
				writer.println(player.getName());
				writer.println(player.getTeam().getIndex());
			}
		}
	}

	/**
	 * Commande BEGINGAME
	 * @author sjrd
	 */
	private class CommandBeginGame implements Command
	{
		/**
		 * {@inheritDoc}
		 */
		public String getName()
		{
			return "BEGINGAME";
		}

		/**
		 * {@inheritDoc}
		 */
		public boolean isAvailable()
		{
			Table table = handler.getTable();
			return (table != null) && table.isFull() && !table.isPlaying();
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

			handler.setWantToBeginGame(true);
		}
	}

	/**
	 * Commande GAMEDYNAMICINFO
	 * @author sjrd
	 */
	private class CommandGameDynamicInfo implements Command
	{
		/**
		 * {@inheritDoc}
		 */
		public String getName()
		{
			return "GAMEDYNAMICINFO";
		}

		/**
		 * {@inheritDoc}
		 */
		public boolean isAvailable()
		{
			Table table = handler.getTable();
			return (table != null) && table.isFull();
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
			Game game = handler.getTable().getGame();
			Player destPlayer = game.getPlayers(
				handler.getPlayer().getPosition());
			
			writer.writeResponse(ResponseCode.OK);
			
			// Annonces faites

			List<Announce> announces = game.getAnnounces();
			
			writer.writeBoolean(announces != null);
			if (announces != null)
				writer.writeAnnounces(announces);

			// Autres informations globales sur le jeu

			writer.writeNullableString(game.getCurrentContractName());
			writer.println(game.getDealer().getPosition());
			writer.println(game.getActivePlayer().getPosition());
			writer.println(game.getPlayCount());
			
			// Informations sur les équipes
			
			for (Team team: game.teamsIteratable())
			{
				writer.println(team.getScore());
				
				List<Card> collectedCards = team.getCollectedCardsSnapshot();
				game.getRules().filterTeamCollectedCards(destPlayer, team,
					collectedCards);
				writer.writeCards(collectedCards);
			}
			
			// Informations sur les joueurs

			for (Player player: game.playersIterable())
			{
				// Cartes en main
				List<Card> cards = player.getCardsSnapshot();
				game.getRules().filterPlayerCards(destPlayer, player, cards);
				writer.writeCards(cards);
				
				// Carte jouée
				boolean hasPlayedCard = player.hasPlayedCard();
				writer.writeBoolean(hasPlayedCard);
				
				if (hasPlayedCard)
				{
					boolean isPlayedCardHidden = player.isPlayedCardHidden();
					Card playedCard = null;
					if (!isPlayedCardHidden)
						playedCard = player.getPlayedCard();
					
					writer.writeCard(playedCard);
					writer.writeBoolean(isPlayedCardHidden);
				}
				
				// Cartes ramassées
				List<Card> collectedCards = player.getCollectedCardsSnapshot();
				game.getRules().filterPlayerCollectedCards(destPlayer, player,
					collectedCards);
				writer.writeCards(collectedCards);
			}
		}
	}

	/**
	 * Commande CHATMESSAGE
	 * @author sjrd
	 */
	private class CommandChatMessage implements Command
	{
		/**
		 * {@inheritDoc}
		 */
		public String getName()
		{
			return "CHATMESSAGE";
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
			// Convertir la source
			MessageSource source;
			try
			{
				source = MessageSource.valueOf(parameters[1]);
				if ((source != MessageSource.Room) &&
					(source != MessageSource.Table))
					throw new NetworkException(ResponseCode.Forbidden);
			}
			catch (IllegalArgumentException error)
			{
				throw new NetworkProtocolException(error);
			}
			
			// Ecrire la réponse
			writer.writeResponse(ResponseCode.OK);
			
			// Créer le message
			Message message = new Message(source,
				String.format("%s> %s", handler.getLoginName(), parameters[2]));

			// Envoyer le message
			if (source == MessageSource.Table)
			{
				// Récupérer le jeu
				Table table = handler.getTable();
				if ((table == null) || !table.isFull())
					return;

				// Envoyer le message
				table.getGame().showMessageToAll(message);
			}
			else
			{
				handler.getServer().sendMessageToAll(message);
			}
		}
	}
}
