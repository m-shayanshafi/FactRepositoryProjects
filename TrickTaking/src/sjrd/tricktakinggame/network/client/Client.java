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

import java.io.*;
import java.net.*;

import javax.swing.event.*;

import sjrd.tricktakinggame.remotable.*;
import sjrd.tricktakinggame.client.*;
import sjrd.tricktakinggame.network.*;

/**
 * Client de jeu de cartes
 * @author sjrd
 */
public class Client
{
	/**
	 * Liste de listeners
	 */
	protected final EventListenerList listenerList = new EventListenerList();
	
	/**
	 * Hôte du serveur
	 */
	private String serverHost;
	
	/**
	 * Port de communication
	 */
	private int serverPort;
	
	/**
	 * Nom de login
	 */
	private String loginName;
	
	/**
	 * Hash du mot de passe
	 */
	private String passwordHash;
	
	/**
	 * Code de session
	 */
	private int sessionCode;
	
	/**
	 * Commandeur du serveur
	 */
	private ServerCommander commander = null;
	
	/**
	 * Répondeur au serveur
	 */
	private ServerResponder responder = null;
    
    /**
     * Thread de shutdown de l'application
     */
    private Thread shutdownThread;
	
	/**
	 * Statut
	 */
	private ClientStatus status = null;
	
	/**
	 * Dernier nombre de ticks d'update
	 */
	private int lastUpdateTickCount = 0;
	
	/**
	 * Créateur de contrôleur de joueur
	 */
	private PlayerControllerCreator playerControllerCreator = null;
	
	/**
	 * Jeu
	 */
	private RemoteGame game = null;
	
	/**
	 * Joueur
	 */
	private RemotePlayer player = null;
	
	/**
	 * Crée un nouveau client
	 * @param aServerHost Hôte du serveur
	 * @param aServerPort Port de communication
	 * @param aLoginName Nom de login
	 * @param aPasswordHash Mot de passe hashé
	 */
	public Client(String aServerHost, int aServerPort, String aLoginName,
		String aPasswordHash) throws IOException
	{
		super();
		
		serverHost = aServerHost;
		serverPort = aServerPort;
		loginName = aLoginName;
		passwordHash = aPasswordHash;
		
		Socket commanderSocket = null, responderSocket = null;
		try
		{
			// Connect commander socket
			commanderSocket = new Socket(serverHost, serverPort);
			NetworkConnection commanderConnection =
				new NetworkConnection(commanderSocket);
			NetworkReader commanderReader = commanderConnection.getReader();
			NetworkWriter commanderWriter = commanderConnection.getWriter();

			// Expected: 201 Welcome
			commanderReader.readResponse();

			// CONNECT loginName passwordHash
			commanderWriter.writeCommand("CONNECT", loginName,
				passwordHash);
			commanderReader.readResponse();
			sessionCode = Integer.parseInt(commanderReader.readLine());

			// Connect responder socket
			responderSocket = new Socket(serverHost, serverPort);
			NetworkConnection responderConnection =
				new NetworkConnection(responderSocket);
			NetworkReader responderReader = responderConnection.getReader();
			NetworkWriter responderWriter = responderConnection.getWriter();

			// Expected: 201 Welcome
			responderReader.readResponse();

			// SNDCONNECT loginName sessionCode
			responderWriter.writeCommand("SNDCONNECT", loginName,
				new Integer(sessionCode).toString());
			responderReader.readResponse();
			
			// Create commander and responder
			commander = new ServerCommander(commanderSocket);
			responder = new ServerResponder(this, responderSocket);
		}
		finally
		{
			if ((commander == null) || (responder == null))
			{
				if (commanderSocket != null)
					commanderSocket.close();
				if (responderSocket != null)
					responderSocket.close();
			}
		}
        
        shutdownThread = new Thread()
        {
            @Override
            public void run()
            {
                close();
            }
        };
        
        Runtime.getRuntime().addShutdownHook(shutdownThread);
	}
	
	/**
	 * Ferme le client
	 */
	public void close()
	{
        try
        {
            Runtime.getRuntime().removeShutdownHook(shutdownThread);
        }
        catch (IllegalStateException ignore)
        {
        }
        
		try
		{
			commander.close();
		}
		catch (IOException error)
		{
		}
		
		try
		{
			responder.close();
		}
		catch (IOException error)
		{
		}
	}
	
	/**
	 * Commandeur du serveur
	 * @return Commandeur du serveur
	 */
	public ServerCommander getCommander()
	{
		return commander;
	}
	
	/**
	 * Interrompt le répondeur au serveur
	 * <p>
	 * Celui-ci sera automatiquement relancé dès qu'il sera terminé, pour
	 * autant que le client soit toujours connecté.
	 * </p>
	 */
	public void interruptResponder()
	{
		responder.interrupt();
	}
	
	/**
	 * Récupère le statut du client
	 * @return Nouveau statut
	 */
	public ClientStatus fetchStatus() throws IOException
	{
		ClientStatus newStatus = commander.fetchStatus();
		
		synchronized (this)
		{
			status = newStatus;
			
			if (newStatus.getUpdateTickCount() > lastUpdateTickCount)
			{
				lastUpdateTickCount = newStatus.getUpdateTickCount();
				if (game != null)
				{
					game.update();
					responder.getPlayerController().notifyUpdate();
				}
			}
		}
		
		if (newStatus.hasPendingMessages())
		{
			for (Message message: commander.fetchMessages())
				fireMessageReceived(message);
		}

		return newStatus;
	}
	
	/**
	 * Récupère le statut obtenu au dernier appel de <tt>fetchStatus()</tt>
	 * @return Statut
	 */
	synchronized public ClientStatus getStatus()
	{
		return status;
	}
	
	/**
	 * Créateur de contrôleur de joueur
	 * @return Créateur de contrôleur de joueur
	 */
	public PlayerControllerCreator getPlayerControllerCreator()
	{
		return playerControllerCreator;
	}
	
	/**
	 * Modifie le créateur de contrôleur de joueur
	 * @param value Nouveau créateur de contrôleur de joueur
	 */
	public void setPlayerControllerCreator(PlayerControllerCreator value)
	{
		playerControllerCreator = value;
	}
	
	/**
	 * Récupère le jeu, et le joueur
	 * <p>
	 * Il est nécessaire que le créateur de contrôleur de joueur ait été
	 * renseigner avant d'appeler cette méthode.
	 * </p>
	 */
	public void fetchGame() throws IOException
	{
		assert playerControllerCreator != null;
		
		ClientStatus status = commander.fetchStatus();
		
		game = commander.fetchGameStaticInfo();
		player = game.getPlayers(status.getSelfPosition());
		responder.setPlayerController(
			playerControllerCreator.createPlayerController(player));
	}
	
	/**
	 * Jeu maître
	 * @return Jeu maître
	 */
	public RemoteGame getGame()
	{
		return game;
	}
	
	/**
	 * Joueur
	 * @return Joueur
	 */
	public RemotePlayer getPlayer()
	{
		return player;
	}
	
	/**
	 * Ajoute un listener de réception de message
	 * @param listener Listener
	 * @see #removeMessageListener(MessageListener)
	 * @see #getMessageListeners()
	 */
	public void addMessageListener(MessageListener listener)
	{
		listenerList.add(MessageListener.class, listener);
	}
	
	/**
	 * Retire un listener de réception de message
	 * @param listener Listener
	 * @see #addMessageListener(MessageListener)
	 * @see #getMessageListeners()
	 */
	public void removeMessageListener(MessageListener listener)
	{
		listenerList.remove(MessageListener.class, listener);
	}
	
	/**
	 * Liste des listeners de réception de message enregistrés
	 * @return Liste des listeners de réception de message enregistrés
	 * @see #addMessageListener(MessageListener)
	 * @see #removeMessageListener(MessageListener)
	 */
	public MessageListener[] getMessageListeners()
	{
		return listenerList.getListeners(MessageListener.class);
	}
	
	/**
	 * Signale la réception d'un message à tous les listeners de message
	 * @param message Message reçu
	 * @see #addMessageListener(MessageListener)
	 * @see #removeMessageListener(MessageListener)
	 * @see #getMessageListeners()
	 */
	protected void fireMessageReceived(Message message)
	{
		for (MessageListener listener: getMessageListeners())
			listener.messageReceived(message);
	}
}
