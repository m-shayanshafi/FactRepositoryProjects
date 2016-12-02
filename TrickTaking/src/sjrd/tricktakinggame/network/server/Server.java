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
import java.util.logging.*;
import java.io.*;
import java.net.*;

import sjrd.tricktakinggame.network.*;
import sjrd.tricktakinggame.remotable.*;
import sjrd.tricktakinggame.rules.*;

/**
 * Serveur pour un jeu à plis
 * <p>
 * Le serveur tourne dans un non-daemon thread, et l'appel au constructeur n'est
 * donc pas bloquant et rend la main immédiatement.
 * </p>
 * @author sjrd
 */
public class Server
{
	/**
	 * Time-out pour la méthode accept() du serveur
	 */
	private static final int acceptTimeout = 5000;

	/**
	 * Logger pour la classe TrickTakingGameServer
	 */
	public static final Logger logger =
	    Logger.getLogger(Server.class.getName());

	/**
	 * Propriétaire du thread d'acceptation
	 */
	private final Server owner = this;
	
	/**
	 * Vérificateur de login
	 */
	private LoginChecker loginChecker = null;
    
    /**
     * Vérificateur d'admin
     */
    private AdminChecker adminChecker = null;

	/**
	 * Socket serveur
	 */
	protected ServerSocket server;

	/**
	 * Thread qui accepte les nouveaux clients
	 */
	protected AcceptThread acceptThread;
	
	/**
	 * Thread de shutdown de l'application
	 */
	private Thread shutdownThread;
	
	/**
	 * Fournisseurs de règles
	 */
	private final List<RulesProvider> rulesProviders =
		new LinkedList<RulesProvider>();

	/**
	 * Liste des clients (synchronisée)
	 */
	private final List<ClientHandler> clients = new LinkedList<ClientHandler>();
	
	/**
	 * Tables ouvertes
	 */
	private final List<Table> tables = new LinkedList<Table>();
	
	/**
	 * Prochain ID de table
	 */
	private int nextTableID = 0;
	
	/**
	 * Constructeur de classe
	 */
	static
	{
		logger.setLevel(Level.ALL);
		logger.setUseParentHandlers(false);
	}

	/**
	 * Ajoute un gestionnaire au logger du serveur de jeu à plis
	 * @see Logger#addHandler(Handler)
	 */
	public static void addLogHandler(Handler handler) throws SecurityException
	{
		logger.addHandler(handler);
	}

	/**
	 * Supprime un gestionnaire au logger du serveur de jeu à plis
	 * @see Logger#removeHandler(Handler)
	 */
	public static void removeLogHandler(Handler handler)
	    throws SecurityException
	{
		logger.removeHandler(handler);
	}

	/**
	 * Transforme une exception en erreur de jeu de cartes, et la logue
	 * @param className Classe d'origine (pour le log)
	 * @param methodName Méthode d'origine (pour le log)
	 * @param exception Exception
	 * @return CardGameFatalError Erreur encapsulant <tt>error</tt>
	 */
	public static CardGameException exceptionToCardGameError(String className,
	    String methodName, Exception exception)
	{
		CardGameException newError = new CardGameException(exception);
		logger.throwing(className, methodName, newError);
		return newError;
	}

	/**
	 * Crée un serveur de jeu à plis sur un port donné
	 * @param port Port à utiliser
	 * @throws IOException Impossible de démarrer le serveur
	 */
	public Server(int port) throws IOException
	{
		super();

		logger.info("Démarrage d'un serveur sur le port " + port);

		server = new ServerSocket(port);
		server.setSoTimeout(acceptTimeout);

		acceptThread = new AcceptThread();
		acceptThread.setDaemon(false);

		acceptThread.start();
		
		shutdownThread = new Thread()
		{
			@Override
			public void run()
			{
				terminate();
			}
		};
		
		Runtime.getRuntime().addShutdownHook(shutdownThread);
	}

	/**
	 * Crée un serveur de jeu à plis sur le port par défaut
	 * @see NetworkInfo#defaultPort
	 * @throws IOException Impossible de démarrer le serveur
	 */
	public Server() throws IOException
	{
		this(NetworkInfo.defaultPort);
	}
	
	/**
	 * Vérificateur de login
	 * @return Vérificateur de login (peut être <tt>null</tt>)
	 */
	synchronized public LoginChecker getLoginChecker()
	{
		return loginChecker;
	}
	
	/**
	 * Modifie le vérificateur de login
	 * @param value Nouveau vérificateur (peut être <tt>null</tt>)
	 */
	synchronized public void setLoginChecker(LoginChecker value)
	{
		loginChecker = value;
	}
    
    /**
     * Vérificateur d'admin
     * @return Vérificateur d'admin (peut être <tt>null</tt>)
     */
    synchronized public AdminChecker getAdminChecker()
    {
        return adminChecker;
    }
    
    /**
     * Modifie le vérificateur d'admin
     * @param value Nouveau vérificateur (peut être <tt>null</tt>)
     */
    synchronized public void setAdminChecker(AdminChecker value)
    {
        adminChecker = value;
    }
	
	/**
	 * Ajoute un fournisseur de règles
	 * @param provider Fournisseur
	 */
	public void addRulesProvider(RulesProvider provider)
	{
		synchronized (rulesProviders)
		{
			rulesProviders.add(provider);
		}
	}
	
	/**
	 * Retire un fournisseur de règles
	 * @param provider Fournisseur
	 */
	public void removeRulesProvider(RulesProvider provider)
	{
		synchronized (rulesProviders)
		{
			rulesProviders.remove(provider);
		}
	}
	
	/**
	 * Nombre de fournisseurs de règles
	 * @return Nombre de fournisseurs de règles
	 */
	public int getRulesProviderCount()
	{
		synchronized (rulesProviders)
		{
			return rulesProviders.size();
		}
	}
	
	/**
	 * Itérateur sur les fournisseurs de règles
	 * @return Itérateur sur les fournisseurs de règles
	 */
	public Iterator<RulesProvider> getRulesProvidersIterator()
	{
		return rulesProviders.iterator();
	}

	/**
	 * Itérable sur les fournisseurs de règles
	 * @return Itérable sur les fournisseurs de règles
	 */
	public Iterable<RulesProvider> getRulesProvidersIterable()
	{
		return rulesProviders;
	}
	
	/**
	 * Trouve un fournisseur de règles à partir de son ID
	 * @param id ID du fournisseur recherché
	 * @return Fournisseur correspondant, ou <tt>null</tt> si non trouvé
	 */
	public RulesProvider findRulesProviderByID(String id)
	{
		synchronized (rulesProviders)
		{
			for (RulesProvider provider: rulesProviders)
				if (provider.getID().equals(id))
					return provider;
		}
		
		return null;
	}

	/**
	 * Itérateur sur les clients
	 * @return Itérateur sur les clients
	 */
	public Iterator<ClientHandler> getClientsIterator()
	{
		return getClientsIterable().iterator();
	}

	/**
	 * Itérable sur les clients
	 * @return Itérable sur les clients
	 */
	public Iterable<ClientHandler> getClientsIterable()
	{
		synchronized (clients)
		{
			return new ArrayList<ClientHandler>(clients);
		}
	}

	/**
	 * Indique au serveur qu'un client a été correctement déconnecté
	 * @param client Gestionnaire du client déconnecté
	 */
	void clientDisconnected(ClientHandler client)
	{
		synchronized (clients)
		{
			clients.remove(client);
		}

		logger.info("Client disconnected - User name: " +
			client.getLoginName());
	}
	
	/**
	 * Nombre de tables ouvertes
	 * @return Nombre de tables ouvertes
	 */
	public int getTableCount()
	{
		return tables.size();
	}

	/**
	 * Itérateur sur les tables ouvertes
	 * @return Itérateur sur les tables ouvertes
	 */
	public Iterator<Table> getTablesIterator()
	{
		return tables.iterator();
	}

	/**
	 * Itérable sur les clients
	 * @return Itérable sur les tables ouvertes
	 */
	public Iterable<Table> getTablesIterable()
	{
		return tables;
	}
	
	/**
	 * Trouve une table à partir de son ID
	 * @param id ID de la table recherchée
	 * @return Table correspondante, ou <tt>null</tt> si non trouvée
	 */
	public Table findTableByID(int id)
	{
		for (Table table: tables)
			if (table.getID() == id)
				return table;
		
		return null;
	}
	
	/**
	 * Envoie un message à tous les clients
	 * @param message Message à envoyer
	 */
	public void sendMessageToAll(Message message)
	{
		for (ClientHandler client: getClientsIterable())
			client.sendMessage(message);
	}
	
	/**
	 * Ouvre une nouvelle table
	 * @param rulesProviderID ID du fournisseur de règles
	 * @param playerCount Nombre de joueurs
	 * @return Table nouvellement créée
	 */
	Table openTable(String rulesProviderID, int playerCount)
		throws NetworkException
	{
		// Find rules provider
		
		RulesProvider rulesProvider = findRulesProviderByID(rulesProviderID);
		if (rulesProvider == null)
			throw new NetworkException(ResponseCode.UnknownRulesProviderID);
		
		// Check player count validity
		
		if (playerCount < rulesProvider.getMinPlayerCount())
			throw new NetworkException(ResponseCode.BadPlayerCount,
				"Not enough players");
		if (playerCount > rulesProvider.getMaxPlayerCount())
			throw new NetworkException(ResponseCode.BadPlayerCount,
				"Too many players");
		
		// Create table
		
		synchronized (this)
		{
			Table table = new Table(this, nextTableID++, rulesProvider,
				playerCount);
			tables.add(table);
			
			logger.info("Table opened - ID: " + table.getID());
			
			return table;
		}
	}
	
	/**
	 * Notifie qu'une table a été fermée
	 * @param table Table fermée
	 */
	synchronized void tableClosed(Table table)
	{
		tables.remove(table);
		
		logger.info("Table closed - ID: " + table.getID());
	}

	/**
	 * Déconnecte tous les clients et termine le thread d'acceptation
	 * <p>
	 * <tt>terminate()</tt> arrête le thread qui accepte les clients. Ensuite,
	 * elle tente de déconnecter proprement les clients. Si elle n'y arrive pas,
	 * elle arrête brutalement le serveur.
	 * </p>
	 */
	public void terminate()
	{
		try
		{
			Runtime.getRuntime().removeShutdownHook(shutdownThread);
		}
		catch (IllegalStateException ignore)
		{
		}
		
		logger.info("Arrêt du serveur sur le port " + server.getLocalPort());

		acceptThread.terminateNow();

		for (ClientHandler client: getClientsIterable())
		{
			try
			{
				client.close();
			}
			catch (IOException error)
			{
				logger.warning("Erreur lors de l'arrêt d'un client");
			}
		}
	}
	
	/**
	 * Trouve un gestionnaire de client à partir de son nom de login
	 * @param loginName Nom de login du client
	 * @return Gestionnaire de ce client, ou <tt>null</tt> si non connecté
	 */
	public ClientHandler findClientByLoginName(String loginName)
	{
		synchronized (clients)
		{
			for (ClientHandler client: clients)
			{
				if (client.getLoginName().equals(loginName))
					return client;
			}
		}
		
		return null;
	}
	
	/**
	 * Connecte un client
	 * @param socket Socket du client
	 * @param loginName Nom de login du client
	 * @param passwordHash Hash du mot de passe
	 * @return Gestionnaire du nouveau client
	 * @throws IOException Erreur de communication
	 * @throws NetworkException Erreur réseau (comme mauvais mot de passe)
	 */
	ClientHandler connectClient(Socket socket, String loginName,
		String passwordHash) throws IOException
	{
		synchronized (clients)
		{
			// Check for user already logged
			if (findClientByLoginName(loginName) != null)
				throw new NetworkException(ResponseCode.UserAlreadyLogged);
			
			// Check login
			LoginChecker loginChecker = getLoginChecker();
			LoggedClient loggedClient;
			if (loginChecker == null)
				loggedClient = new DefaultLoggedClient(loginName);
			else
			{
				loggedClient = getLoginChecker().checkLogin(loginName,
					passwordHash);
				if (loggedClient == null)
					throw new NetworkException(ResponseCode.InvalidLogin);
			}

			// Create handler and return it
			ClientHandler result = new ClientHandler(this, loggedClient,
				socket);
			clients.add(result);
			return result;
		}
	}

	/**
	 * Classe interne pour le thread qui accepte les connexions
	 * @author sjrd
	 */
	private class AcceptThread extends Thread
	{
		/**
		 * Crée un nouveau thread qui accepte les connexions
		 */
		public AcceptThread()
		{
			super("accept");
		}

		/**
		 * Termine le thread et attend qu'il soit bel et bien terminé
		 */
		public void terminateNow()
		{
			interrupt();

			try
			{
				join();
			}
			catch (InterruptedException error)
			{
			}
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public void run()
		{
			while (!interrupted())
			{
				try
				{
					new ClientInitialConnection(owner, server.accept());
				}
				catch (SocketTimeoutException error)
				{
				}
				catch (IOException error)
				{
					logger.severe("Fatal I/O Error in accept thread: "
					    + error.getMessage());
					terminate();
				}
			}
		}
	}
}
