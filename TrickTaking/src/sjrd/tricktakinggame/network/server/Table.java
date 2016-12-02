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

import sjrd.tricktakinggame.game.*;
import sjrd.tricktakinggame.remotable.*;
import sjrd.tricktakinggame.rules.*;

/**
 * Table de jeu
 * @author sjrd
 */
public class Table
{
	/**
	 * Serveur propriétaire
	 */
	private Server server;

	/**
	 * ID de la table, unique dans le serveur
	 */
	private int tableID;
	
	/**
	 * Fournisseur des règles
	 */
	private RulesProvider rulesProvider;
	
	/**
	 * Nombre de joueurs attendus à cette table
	 */
	private int awaitedPlayerCount;

	/**
	 * Gestionnaire des clients qui seront les joueurs
	 */
	private List<ClientHandler> clientHandlers;
	
	/**
	 * Créateur des contrôleurs de joueur
	 */
	private final ControllerCreator controllerCreator =
		new ControllerCreator();
	
	/**
	 * Règles du jeu en cours
	 */
	private Rules rules = null;
	
	/**
	 * Jeu en cours
	 */
	private Game game = null;
	
	/**
	 * Thread de jeu d'une partie
	 */
	private GamePlayingThread gamePlayingThread = new GamePlayingThread();
	
	/**
	 * Indique si une partie est en cours
	 */
	private boolean playing = false;
	
	/**
	 * Construit une nouvelle table
	 * @param aServer Serveur propriétaire
	 * @param id ID de la table
	 * @param aRulesProvider Fournisseur des règles
	 * @param aAwaitedPlayerCount Nombre de joueurs attendus à cette table
	 */
	public Table(Server aServer, int id,
		RulesProvider aRulesProvider, int aAwaitedPlayerCount)
	{
		super();
		
		server = aServer;
		tableID = id;

		assert aAwaitedPlayerCount >= aRulesProvider.getMinPlayerCount();
		assert aAwaitedPlayerCount <= aRulesProvider.getMaxPlayerCount();
		
		rulesProvider = aRulesProvider;
		awaitedPlayerCount = aAwaitedPlayerCount;
		
		clientHandlers = new ArrayList<ClientHandler>(awaitedPlayerCount);
	}
	
	/**
	 * Serveur propriétaire
	 * @return Serveur propriétaire
	 */
	public Server getServer()
	{
		return server;
	}
	
	/**
	 * ID de la table
	 * @return ID de la table
	 */
	public int getID()
	{
		return tableID;
	}
	
	/**
	 * Fournisseur des règles
	 * @return Fournisseur des règles
	 */
	public RulesProvider getRulesProvider()
	{
		return rulesProvider;
	}
	
	/**
	 * Nombre attendu de joueurs
	 * @return Nombre attendu de joueurs
	 */
	public int getAwaitedPlayerCount()
	{
		return awaitedPlayerCount;
	}
	
	/**
	 * Nombre de joueurs actuellement à cette table
	 * @return Nombre de joueurs actuellement à cette table
	 */
	synchronized public int getPlayerCount()
	{
		return clientHandlers.size();
	}
	
	/**
	 * Construit un tableau des noms des joueurs à cette table
	 * @return Tableau des noms des joueurs
	 */
	synchronized public String[] getPlayerNames()
	{
		String[] result = new String[clientHandlers.size()];
		for (int i = 0; i < clientHandlers.size(); i++)
			result[i] = clientHandlers.get(i).getLoginName();
		return result;
	}
	
	/**
	 * Indique si la table est complète
	 * <p>
	 * Lorsque la table est complète, il est possible de démarrer une partie,
	 * si ce n'est pas encore fait.
	 * </p>
	 * @return <tt>true</tt> si la table est complète, <tt>false</tt> sinon
	 */
	synchronized public boolean isFull()
	{
		return clientHandlers.size() == awaitedPlayerCount;
	}
	
	/**
	 * Ajoute un joueur à la table, si elle n'est pas déjà complète
	 * <p>
	 * Si cet ajout complète la table, les règles et le jeu sont créés.
	 * </p>
	 * @param clientHandler Gestionnaire du client qui sera le joueur
	 * @return <tt>true</tt> si le joueur a bien été ajouté, <tt>false</tt> si
	 *         la table était déjà complète
	 */
	synchronized boolean addPlayer(ClientHandler clientHandler)
	{
		if (isFull() || isClosing())
			return false;
		
		clientHandlers.add(clientHandler);
		clientHandler.setTable(this);
		
		if (isFull())
		{
			rules = rulesProvider.newRules(awaitedPlayerCount);
			game = new Game(rules, controllerCreator, getPlayerNames());
			gamePlayingThread.start();
		}

		return true;
	}
	
	/**
	 * Supprime un joueur de la table, si la partie n'a pas encore commencé
	 * <p>
	 * Un joueur n'a pas le droit de se retirer de la table si une partie est
	 * en cours.
	 * </p>
	 * <p>
	 * Si, après la suppression, il ne reste plus de joueur à cette table,
	 * celle-ci est fermée.
	 * </p>
	 * <p>
	 * Si, d'autre part, la table était complète et avait donc commencé à,
	 * jouer, la table est également fermée.
	 * </p>
	 * @param clientHandler Gestionnaire du client du joueur
	 * @return <tt>true</tt> si le joueur a été retiré, <tt>false</tt> si la
	 *         partie avait déjà commencé
	 */
	synchronized boolean removePlayer(ClientHandler clientHandler)
	{
		if (isPlaying())
			return false;
		
		boolean wasFull = isFull();
		
		clientHandlers.remove(clientHandler);
		clientHandler.setTable(null);
		
		if ((clientHandlers.isEmpty()) || wasFull)
			close();
		
		return true;
	}
	
	/**
	 * Règles du jeu à la table
	 * @return Règles du jeu à la table
	 */
	Rules getRules()
	{
		return rules;
	}
	
	/**
	 * Jeu à la table
	 * @return Jeu à la table
	 */
	Game getGame()
	{
		return game;
	}
	
	/**
	 * Indique si la table doit se fermer
	 * @return <tt>true</tt> si la table doit se fermer, <tt>false</tt> sinon
	 */
	public boolean isClosing()
	{
		return gamePlayingThread.isInterrupted();
	}
	
	/**
	 * Demande la fermeture de la table
	 */
	synchronized void close()
	{
		gamePlayingThread.interrupt();
		
		for (ClientHandler clientHandler: clientHandlers)
			clientHandler.setTable(null);
		server.tableClosed(this);
	}
	
	/**
	 * Indique si une partie est en cours
	 * @return
	 */
	synchronized public boolean isPlaying()
	{
		return playing;
	}
	
	/**
	 * Modifie l'indication de partie en cours
	 * @param value <tt>true</tt> si une partie en cours, <tt>false</tt> sinon
	 */
	synchronized private void setPlaying(boolean value)
	{
		playing = value;
	}
	
	/**
	 * Indique qu'un joueur a demandé de commencer le jeu
	 */
	synchronized void notifyBeginGame()
	{
		if (!isFull() || isClosing() || isPlaying())
			return;
		
		synchronized (gamePlayingThread)
		{
			gamePlayingThread.notify();
		}
	}

	/**
	 * Créateur des contrôleurs de joueur pour la table
	 * @author sjrd
	 */
	private class ControllerCreator implements PlayerControllerCreator
	{
		/**
		 * @see PlayerControllerCreator#createPlayerController(Player)
		 */
		public PlayerController createPlayerController(RemotablePlayer player)
		{
			ClientHandler clientHandler =
				clientHandlers.get(player.getPosition());
			
			clientHandler.setPlayer(player);
			return clientHandler.getCommander().playerController; 
		}
	}

	/**
	 * Thread exécutant une partie
     * @author sjrd
     */
    private class GamePlayingThread extends Thread
    {
    	/**
    	 * Crée un nouveau thread exécutant une partie
    	 *
    	 */
    	public GamePlayingThread()
    	{
    		super("gamePlaying:table-" + getID());
    	}
    	
    	/**
    	 * Exécution du thread
    	 */
    	@Override
	    public void run()
	    {
		    while (!interrupted())
		    {
		    	try
		    	{
		    		synchronized (this)
		    		{
		    			wait();
		    		}
		    		
		    		// Teste s'il faut commencer un jeu
		    		
		    		boolean ok = true;
		    		
		    		for (ClientHandler handler: clientHandlers)
		    		{
		    			if (!handler.doesWantToBeginGame())
		    			{
		    				ok = false;
		    				break;
		    			}
		    		}
		    		
		    		// Commence le jeu
		    		
		    		if (ok)
		    		{
		    			setPlaying(true);
		    			
		    			for (ClientHandler handler: clientHandlers)
		    				handler.setWantToBeginGame(false);
		    			
		    			try
		    			{
		    				game.playGame();
		    			}
		    			catch (CardGameException error)
		    			{
		    				Server.logger.severe(
		    					"Card game fatal error: " + error.getMessage());
		    				close();
		    			}
		    			finally
		    			{
		    				setPlaying(false);
		    			}
		    		}
		    	}
		    	catch (InterruptedException ignore)
		    	{
		    	}
		    }
	    }
    }
}
