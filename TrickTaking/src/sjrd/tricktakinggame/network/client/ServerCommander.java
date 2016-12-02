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
package sjrd.tricktakinggame.network.client;

import java.util.*;
import java.io.*;
import java.net.*;
import java.nio.channels.*;

import sjrd.tricktakinggame.cards.*;
import sjrd.tricktakinggame.remotable.*;
import sjrd.tricktakinggame.network.*;
import sjrd.tricktakinggame.client.*;

/**
 * Commandeur du serveur
 * @author sjrd
 */
public class ServerCommander extends NetworkConnection
	implements RemoteGame.Updater
{
	/**
	 * Crée un commandeur de serveur
	 */
	public ServerCommander(Socket aSocket) throws IOException
	{
		super(aSocket);
	}

	/**
	 * Récupère le statut du client
	 * @return Statut du client
	 */
	synchronized ClientStatus fetchStatus() throws IOException
	{
		try
		{
			writer.writeCommand("STATUS");
			Response response = reader.readResponse();

			String[] addInfo = new String[reader.readInteger()];
			for (int i = 0; i < addInfo.length; i++)
				addInfo[i] = reader.readLine();

			return new ClientStatus(ClientStatusKind.findStatusByResponseCode(
				response.getCode()), addInfo);
		}
		catch (ClosedChannelException error)
		{
			return new ClientStatus(ClientStatusKind.Disconnected);
		}
	}

	/**
	 * Récupère les messages en attente
	 * @return Messages en attente
	 */
	synchronized List<Message> fetchMessages() throws IOException
	{
		writer.writeCommand("FETCHMESSAGES");
		reader.readResponse();

		int messageCount = reader.readInteger();

		List<Message> result = new ArrayList<Message>(messageCount);
		for (int i = 0; i < messageCount; i++)
		{
			String sourceLine = reader.readString();
			String message = reader.readString();
			
			try
			{
				result.add(new Message(MessageSource.valueOf(sourceLine),
					message));
			}
			catch (IllegalArgumentException ignore)
			{
			}
		}

		return result;
	}
	
	/**
	 * Liste les fournisseurs de règles disponibles
	 * @return Liste des informations sur chaques règles
	 */
	synchronized public RulesInfo[] listAvailableRules() throws IOException
	{
		writer.writeCommand("LISTRULES");
		reader.readResponse();
		
		int count = Integer.parseInt(reader.readLine());
		RulesInfo[] rulesInfo = new RulesInfo[count];
		
		for (int i = 0; i < count; i++)
		{
			StringTokenizer line = new StringTokenizer(reader.readLine(), "\t");
			
			String id = line.nextToken();
			String name = line.nextToken();
			int minPlayerCount = Integer.parseInt(line.nextToken());
			int maxPlayerCount = Integer.parseInt(line.nextToken());
			
			rulesInfo[i] = new RulesInfo(id, name, minPlayerCount,
				maxPlayerCount);
		}
		
		return rulesInfo;
	}
	
	/**
	 * Liste les tables ouvertes
	 * @param rulesInfo Liste des informations sur les règles (peut être
	 *        <tt>null</tt>)
	 * @return Liste des informations sur chaque table ouverte
	 */
	synchronized public TableInfo[] listTables(RulesInfo[] rulesInfo)
		throws IOException
	{
		writer.writeCommand("LISTTABLES");
		reader.readResponse();
		
		int count = Integer.parseInt(reader.readLine());
		TableInfo[] tablesInfo = new TableInfo[count];
		
		if (rulesInfo == null)
			rulesInfo = new RulesInfo[0];
		
		for (int i = 0; i < count; i++)
		{
			StringTokenizer line = new StringTokenizer(reader.readLine(), "\t");
			
			int id = Integer.parseInt(line.nextToken());
			String rulesID = line.nextToken();
			int awaitedPlayerCount = Integer.parseInt(line.nextToken());
			int playerCount = Integer.parseInt(line.nextToken());
			
			tablesInfo[i] = new TableInfo(id, rulesInfo, rulesID,
				awaitedPlayerCount, playerCount);
		}
		
		return tablesInfo;
	}
	
	/**
	 * Liste les tables ouvertes
	 * @return Liste des informations sur chaque table ouverte
	 */
	public TableInfo[] listTables() throws IOException
	{
		return listTables(null);
	}
	
	/**
	 * Crée une table
	 * @param rulesInfo Règles associées à la table
	 * @param playerCount nombre de joueurs
	 */
	synchronized public void createTable(RulesInfo rulesInfo, int playerCount)
		throws IOException
	{
		writer.writeCommand("CREATETABLE", rulesInfo.getID(), playerCount);
		reader.readResponse();
	}
	
	/**
	 * Rejoint une table
	 * @param tableInfo Table à rejoindre
	 */
	synchronized public void joinTable(TableInfo tableInfo)
		throws IOException
	{
		writer.writeCommand("JOINTABLE", tableInfo.getID());
		reader.readResponse();
	}
	
	/**
	 * Quitte la table courante
	 */
	synchronized public void leaveTable()
		throws IOException
	{
		writer.writeCommand("LEAVETABLE");
		reader.readResponse();
	}
	
	/**
	 * Récupère les informations statiques sur le jeu
	 * @return Jeu ainsi construit
	 */
	synchronized RemoteGame fetchGameStaticInfo() throws IOException
	{
		writer.writeCommand("GAMESTATICINFO");
		reader.readResponse();
		
		// Lire le paquet
		
		int cardCount = reader.readInteger();
		List<Card> cards = new ArrayList<Card>(cardCount);
		
		for (int i = 0; i < cardCount; i++)
		{
			String line = reader.readLine();
			
			try
			{
				StringTokenizer tokenizer = new StringTokenizer(line, "\t");
				if (tokenizer.countTokens() != 4)
					throw new NetworkProtocolException(
						"Bad card description line");
				
				Suit suit = Suit.valueOf(tokenizer.nextToken());
				int force = Integer.parseInt(tokenizer.nextToken());
				String name = tokenizer.nextToken();
				String drawID = tokenizer.nextToken();
				
				cards.add(new Card(suit, force, name, drawID));
			}
			catch (IllegalArgumentException error)
			{
				throw new NetworkProtocolException("Bad card description line");
			}
		}
		
		Deck deck = new Deck(cards);
		
		// Lire les nombres d'équipes et de joueurs
		int teamCount = reader.readInteger();
		int playerCount = reader.readInteger();
		
		// Lire les noms et équipes des joueurs
		String[] playerNames = new String[playerCount];
		int[] playerTeams = new int[playerCount];
		for (int i = 0; i < playerCount; i++)
		{
			playerNames[i] = reader.readLine();
			playerTeams[i] = reader.readInteger();
		}
		
		// Créer et renvoyer le jeu
		return new RemoteGame(this, deck, teamCount, playerNames, playerTeams);
	}

	/**
	 * Demande de commander la partie
	 */
	synchronized public void beginGame() throws IOException
	{
		writer.writeCommand("BEGINGAME");
		reader.readResponse();
	}
	
	/**
	 * {@inheritDoc}
	 */
	synchronized public void updateGame(RemoteGame game) throws IOException
	{
		writer.writeCommand("GAMEDYNAMICINFO");
		reader.readResponse();
		
		// Annonces faites
		
		List<Announce> announces;
		
		if (reader.readBoolean())
			announces = reader.readAnnounceList(game);
		else
			announces = null;
		
		// Autres informations globales sur le jeu

		String currentContractName = reader.readNullableString();
		RemotePlayer dealer = game.getPlayers(reader.readInteger());
		RemotePlayer activePlayer = game.getPlayers(reader.readInteger());
		int playCount = reader.readInteger();
		
		game.updateData(announces, currentContractName, dealer, activePlayer,
			playCount);
		
		// Informations sur les équipes
		
		for (RemoteTeam team: game.teamsIteratable())
		{
			int score = reader.readInteger();
			List<Card> collectedCards = reader.readCardList(game.getDeck());
			
			team.updateData(score, collectedCards);
		}

		// Informations sur les joueurs

		for (RemotePlayer player: game.playersIterable())
		{
			Card playedCard = null;
			boolean isPlayedCardHidden = false;
			
			List<Card> cards = reader.readCardList(game.getDeck());
			
			boolean hasPlayedCard = reader.readBoolean();
			if (hasPlayedCard)
			{
				playedCard = reader.readCard(game.getDeck());
				isPlayedCardHidden = reader.readBoolean();
			}
			
			List<Card> collectedCards = reader.readCardList(game.getDeck());
			
			player.updateData(cards, hasPlayedCard, playedCard,
				isPlayedCardHidden, collectedCards);
		}
	}

    /**
	 * {@inheritDoc}
     */
    synchronized public void sendChatMessage(Message message) throws IOException
    {
    	writer.writeCommand("CHATMESSAGE", message.getSource().name(),
    		message.getMessage());
    	
    	reader.readResponse();
    }
}
