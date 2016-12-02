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

import sjrd.tricktakinggame.remotable.*;
import sjrd.tricktakinggame.network.*;

/**
 * Gestionnaire d'un client
 * @author sjrd
 */
public class ClientHandler
{
	/**
	 * Générateur de codes de session
	 */
	private static final Random sessionCodeGenerator = new Random();
	
	/**
	 * Serveur
	 */
	private Server server;

	/**
	 * Client loggé
	 */
	private LoggedClient loggedClient;

	/**
	 * Répondeur
	 */
	private ClientResponder responder;

	/**
	 * Commandeur
	 */
	private ClientCommander commander = null;

	/**
	 * Code de session
	 */
	private int sessionCode;
	
	/**
	 * Table
	 */
	private Table table = null;
	
	/**
	 * Joueur associé, quand une partie est en cours
	 */
	private RemotablePlayer player = null;
	
	/**
	 * Indique si le joueur a demandé de commencer la partie
	 */
	private boolean wantToBeginGame = false;
	
	/**
	 * Messages en attente
	 */
	private List<Message> pendingMessages = new LinkedList<Message>();
	
	/**
	 * Nombre de ticks d'update
	 */
	private int updateTickCount = 0;

	/**
	 * Crée un gestionnaire de client
	 * @param aServer Serveur
	 * @param aLoggedClient Client loggé
	 * @param aSocket Socket
	 */
	public ClientHandler(Server aServer, LoggedClient aLoggedClient,
		Socket socket) throws IOException
	{
		super();
		
		server = aServer;

		loggedClient = aLoggedClient;
		responder = new ClientResponder(this, socket);

		sessionCode = sessionCodeGenerator.nextInt(65535) + 1;
	}

	/**
	 * Renseigne le socket du commandeur
	 * @param aSocket Socket
	 */
	void setCommanderSocket(Socket socket)
	    throws IOException, NetworkProtocolException
	{
		if (commander != null)
			throw new NetworkProtocolException(
				"Second connection alreay done");
		commander = new ClientCommander(this, socket);
		responder.start();
	}
	
	/**
	 * Serveur auquel est rattaché ce client
	 * @return Serveur auquel est rattaché ce client
	 */
	public Server getServer()
	{
		return server;
	}

	/**
	 * Nom de login
	 * @return Nom de login
	 */
	public String getLoginName()
	{
		return loggedClient.getLoginName();
	}

	/**
	 * Répondeur
	 * @return Répondeur
	 */
	protected ClientResponder getResponder()
	{
		return responder;
	}

	/**
	 * Commandeur
	 * @return Commandeur
	 */
	protected ClientCommander getCommander()
	{
		return commander;
	}

	/**
	 * Code de session correspond à ce client
	 * @return Code de session correspond à ce client
	 */
	public int getSessionCode()
	{
		return sessionCode;
	}
	
	/**
	 * Joueur associé
	 * @return Joueur associé
	 */
	synchronized public RemotablePlayer getPlayer()
	{
		return player;
	}
	
	/**
	 * Modifie le joueur associé
	 * @param value Nouveau joueur associé
	 */
	synchronized void setPlayer(RemotablePlayer value)
	{
		player = value;
	}
	
	/**
	 * Table
	 * @return Table
	 */
	synchronized public Table getTable()
	{
		return table;
	}
	
	/**
	 * Modifie la table
	 * @param value Nouvelle table
	 */
	synchronized void setTable(Table value)
	{
		table = value;
		wantToBeginGame = false;
	}
	
	/**
	 * Modifie l'indication de commencer la partie
	 * @param value Nouvelle valeur
	 */
	synchronized void setWantToBeginGame(boolean value)
	{
		wantToBeginGame = value;
		if (wantToBeginGame)
			table.notifyBeginGame();
	}
	
	/**
	 * Teste si le joueur veut commencer la partie
	 * @return <tt>true</tt> s'il veut commencer la partie, <tt>false</tt> sinon
	 */
	synchronized public boolean doesWantToBeginGame()
	{
		return wantToBeginGame;
	}
	
	/**
	 * Envoie un message au client
	 * <p>
	 * Cet appel est asynchrône et n'attend pas que le client ait reçu le
	 * message.
	 * </p>
	 * @param message Message à envoyer
	 */
	public void sendMessage(Message message)
	{
		synchronized (pendingMessages)
		{
			pendingMessages.add(message);
		}
	}
	
	/**
	 * Teste si ce client a des messages en attente
	 * @return <tt>true</tt> s'il a des messages en attente, <tt>false</tt>
	 *         sinon
	 */
	boolean hasPendingMessages()
	{
		synchronized (pendingMessages)
		{
			return !pendingMessages.isEmpty();
		}
	}
	
	/**
	 * Récupère les messages en attente
	 * @return Liste des messages en attente
	 */
	List<Message> fetchPendingMessages()
	{
		synchronized (pendingMessages)
		{
			List<Message> result = new ArrayList<Message>(pendingMessages);
			pendingMessages.clear();
			return result;
		}
	}
	
	/**
	 * Notifie d'une mise à jour
	 * <p>
	 * Cet appel est asynchrône et n'attend pas que le client ait mis à jour.
	 * </p>
	 */
	synchronized public void notifyUpdate()
	{
		updateTickCount++;
	}
	
	/**
	 * Nombre de ticks d'update
	 * @return Nombre de ticks d'update
	 */
	synchronized public int getUpdateTickCount()
	{
		return updateTickCount;
	}

	/**
	 * Clôt le client
	 */
	void close() throws IOException
	{
		if (table != null)
		{
			if (!table.removePlayer(this))
			{
				if (table.getGame() != null)
					table.getGame().showMessageToAll(MessageSource.RulesError,
						String.format("%s s'est déconnecté", getLoginName()));
				table.close();
			}
		}
		
		if (commander != null)
			commander.close();
		responder.close();
		
		server.clientDisconnected(this);
		
		loggedClient.disconnected();
	}
	
	/**
	 * Essaye de clore le client
	 * @return <tt>true</tt> si la cloture a réussi, <tt>false</tt> sinon
	 */
	boolean tryClose()
	{
		try
		{
			close();
			return true;
		}
		catch (IOException error)
		{
			return false;
		}
	}
}
