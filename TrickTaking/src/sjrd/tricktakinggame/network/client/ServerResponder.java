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

import sjrd.tricktakinggame.cards.*;
import sjrd.tricktakinggame.remotable.*;
import sjrd.tricktakinggame.client.*;
import sjrd.tricktakinggame.network.*;
import sjrd.tricktakinggame.network.commands.*;

import static sjrd.util.ArrayUtils.*;

/**
 * Répondeur à un serveur
 * @author sjrd
 */
public class ServerResponder extends CommandBasedConnection
{
	/**
	 * Client associé
	 */
	Client client;
	
	/**
	 * Contrôleur du joueur
	 */
	PlayerController playerController = null;
	
	/**
	 * Crée un répondeur à un serveur
	 */
	public ServerResponder(Client aClient, Socket aSocket) throws IOException
	{
		super(aSocket);
		
		getThread().setName("responder");
		
		client = aClient;
		
		synchronized (commands)
		{
			commands.add(new CommandPing());
			commands.add(new CommandChooseAnnounce());
			commands.add(new CommandPlayTurn());
			commands.add(new CommandPause());
		}
	}
	
	/**
	 * Interrompt le thread de réponse
	 */
	void interrupt()
	{
		if (isExecutingCommand())
			getThread().interrupt();
	}

	/**
	 * {@inheritDoc}
	 * <p>
	 * Si le thread arrive à son terme, mais que le client est toujours
	 * connecté, le thread est relancé.
	 * </p>
	 */
	@Override
	protected void onThreadTerminated()
	{
		super.onThreadTerminated();
		
		if (client.getStatus().getStatusKind() != ClientStatusKind.Disconnected)
			recreateThread(false);
	}
	
	/**
	 * Client associé
	 * @return Client associé
	 */
	public Client getClient()
	{
		return client;
	}
	
	/**
	 * Contrôleur du joueur
	 * return Contrôleur du joueur
	 */
	public PlayerController getPlayerController()
	{
		return playerController;
	}
	
	/**
	 * Modifie le contrôleur du joueur
	 * @param value Nouveau contrôleur du joueur
	 */
	void setPlayerController(PlayerController value)
	{
		playerController = value;
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
	 * Commande CHOOSEANNOUNCE
	 * @author sjrd
	 */
	private class CommandChooseAnnounce implements Command
	{
		/**
    	 * {@inheritDoc}
		 */
		public String getName()
		{
			return "CHOOSEANNOUNCE";
		}

		/**
    	 * {@inheritDoc}
		 */
		public boolean isAvailable()
		{
			return playerController != null;
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
			Announce[] availableAnnounces = reader.readAnnounces(
				client.getGame());
			Announce announce;
			
			try
			{
				announce = playerController.chooseAnnounce(availableAnnounces);
			}
			catch (CardGameException error)
			{
				throw new NetworkException(ResponseCode.CardGameFatalError);
			}
			
			writer.writeResponse(ResponseCode.OK);
			writer.writeInteger(arrayIndexOf(availableAnnounces, announce));
		}
	}

	/**
	 * Commande PLAYTURN
	 * @author sjrd
	 */
	private class CommandPlayTurn implements Command
	{
		/**
    	 * {@inheritDoc}
		 */
		public String getName()
		{
			return "PLAYTURN";
		}

		/**
    	 * {@inheritDoc}
		 */
		public boolean isAvailable()
		{
			return playerController != null;
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
			Card playedCard;
			
			try
			{
				playedCard = playerController.playTurn();
			}
			catch (CardGameException error)
			{
				throw new NetworkException(ResponseCode.CardGameFatalError);
			}
			
			writer.writeResponse(ResponseCode.OK);
			writer.writeCard(playedCard);
		}
	}

	/**
	 * Commande PAUSE
	 * @author sjrd
	 */
	private class CommandPause implements Command
	{
		/**
    	 * {@inheritDoc}
		 */
		public String getName()
		{
			return "PAUSE";
		}

		/**
    	 * {@inheritDoc}
		 */
		public boolean isAvailable()
		{
			return playerController != null;
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
			try
			{
				playerController.pause();
				writer.writeResponse(ResponseCode.OK);
			}
			catch (CardGameException error)
			{
				throw new NetworkException(ResponseCode.CardGameFatalError);
			}
		}
	}
}
