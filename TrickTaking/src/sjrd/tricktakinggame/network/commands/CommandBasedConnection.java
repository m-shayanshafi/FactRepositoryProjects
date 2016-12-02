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
package sjrd.tricktakinggame.network.commands;

import java.util.*;
import java.io.*;
import java.net.*;

import sjrd.tricktakinggame.network.*;

/**
 * Connexion basée sur la réponse à des commandes
 * @author sjrd
 */
public class CommandBasedConnection extends ThreadedConnection
{
	/**
	 * Liste des commandes
	 */
	protected final List<Command> commands = new Vector<Command>();
	
	/**
	 * Verrou de la propriété <tt>executingCommand</tt>
	 */
	private final Object executingCommandLock = new Object();
	
	/**
	 * Indique si une commande est en cours d'exécution
	 */
	private boolean executingCommand = false;

	/**
	 * Crée une connexion base sur la réponse à des commandes
	 * @param aSocket {@inheritDoc}
	 * @param createSuspended {@inheritDoc}
	 * @throws IOException {@inheritDoc}
	 */
	public CommandBasedConnection(Socket aSocket, boolean createSuspended)
		throws IOException
	{
		super(aSocket, createSuspended);
	}
	
	/**
	 * Crée une connexion base sur la réponse à des commandes
	 * @param aSocket {@inheritDoc}
	 * @throws IOException {@inheritDoc}
	 */
	public CommandBasedConnection(Socket aSocket) throws IOException
	{
		super(aSocket);
	}
	
	/**
	 * Indique si une commande est en train d'être exécutée
	 * @return <tt>true</tt> si en train d'exécuter une commande, <tt>false</tt>
	 *         sinon
	 */
	public boolean isExecutingCommand()
	{
		synchronized (executingCommandLock)
		{
			return executingCommand;
		}
	}
	
	/**
	 * Modifie l'état d'exécution de commande
	 * @param value <tt>true</tt> si en train d'exécuter une commande,
	 *        <tt>false</tt> sinon
	 */
	private void setExecutingCommand(boolean value)
	{
		synchronized (executingCommandLock)
		{
			executingCommand = value;
		}
	}
	
	/**
	 * Trouve une commande à partir de son nom
	 * @param commandName Nom de la commande
	 * @return Commande correspondant, ou <tt>null</tt> si non trouvée
	 */
	protected Command findCommandByName(String commandName)
	{
		synchronized (commands)
		{
			for (Command command: commands)
				if (command.getName().equals(commandName))
					return command;
		}
		
		return null;
	}
	
	/**
	 * Vérifie les paramètres pour une commande
	 * @param command Commande
	 * @param commandLine Ligne de commande
	 * @throws NetworkProtocolException Paramètres invalides
	 */
	protected void checkParameters(Command command, String[] commandLine)
		throws NetworkProtocolException
	{
		ParameterKind[] paramKinds = command.getParameterKinds();
		if (paramKinds != null)
		{
			// Check parameter count

			if (commandLine.length-1 < paramKinds.length)
				throw new NetworkProtocolException(
					"Not enough parameters for command " + command.getName());

			if (commandLine.length-1 > paramKinds.length)
				throw new NetworkProtocolException(
					"Too many parameters for command " + command.getName());

			// Check parameters kinds

			for (int i = 0; i < paramKinds.length; i++)
			{
				if (paramKinds[i] == ParameterKind.pkInteger)
				{
					// Integer parameter
					try
					{
						Integer.parseInt(commandLine[i+1]);
					}
					catch (NumberFormatException error)
					{
						throw new NetworkProtocolException(error);
					}
				}
			}
		}
	}
	
	/**
	 * Exécute une commande
	 * @param command Commande à exécuter
	 * @param commandLine Ligne de commande
	 * @throws NetworkProtocolException Erreur de protocole
	 * @throws NetworkException Erreur de réseau
	 * @throws IOException Erreur de communication
	 */
	protected void executeCommand(Command command, String[] commandLine)
		throws IOException
	{
		command.execute(commandLine);
	}

	/**
	 * Lit et exécute une commande
	 * @see sjrd.tricktakinggame.network.ThreadedConnection#execute()
	 */
	@Override
	protected void execute() throws IOException
	{
		String[] commandLine = reader.readCommand();
		if (commandLine == null)
		{
			close();
			return;
		}
		
		// Find the command
		String commandName = commandLine[0];
		Command command = findCommandByName(commandName);
		if (command == null)
			throw new NetworkProtocolException("Unknown command: " +
				commandName);
		
		// Check availability
		if (!command.isAvailable())
			throw new NetworkProtocolException("Command unavailable: " +
				commandName);

		// Check parameter kinds
		checkParameters(command, commandLine);

		// Execute the command
		setExecutingCommand(true);
		try
		{
			executeCommand(command, commandLine);
		}
		finally
		{
			setExecutingCommand(false);
		}
	}
}
