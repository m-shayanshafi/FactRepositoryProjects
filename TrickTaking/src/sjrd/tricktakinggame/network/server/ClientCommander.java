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

import java.io.*;
import java.net.*;

import sjrd.tricktakinggame.remotable.*;
import sjrd.tricktakinggame.cards.*;
import sjrd.tricktakinggame.network.*;

/**
 * Commandeur d'un client
 * @author sjrd
 */
public class ClientCommander extends NetworkConnection
{
	/**
	 * Gestionnaire de client
	 */
	private ClientHandler handler;

	/**
	 * Contrôleur de joueur
	 */
	public final PlayerController playerController =
		new PlayerRemoteController();
	
	/**
	 * Crée le commandeur d'un gestionnaire de client
	 * @param aSocket Socket
	 */
	public ClientCommander(ClientHandler aHandler, Socket socket)
		throws IOException
	{
		super(socket);

		handler = aHandler;
	}

	/**
	 * Contrôleur de joueur à distance
     * @author sjrd
     */
    public class PlayerRemoteController implements PlayerController
    {
    	/**
    	 * {@inheritDoc}
    	 */
    	synchronized public <A extends Announce> A chooseAnnounce(
    		A ... availableAnnounces) throws CardGameException
    	{
	    	Server.logger.fine(String.format("Player %s -> ChooseAnnounce()",
	    		handler.getLoginName()));
	    	
	    	writer.writeCommand("CHOOSEANNOUNCE");
	    	writer.writeAnnounces(availableAnnounces);

	    	try
	    	{
	    		reader.readResponse();
	    		int resultIndex = reader.readInteger();
	    		
	    		if ((resultIndex < 0) ||
	    			(resultIndex >= availableAnnounces.length))
	    		{
	    			throw Server.exceptionToCardGameError(
	    				PlayerRemoteController.class.getName(),
	    				"chooseAnnounce()",
	    				new NetworkProtocolException("Invalid index"));
	    		}

	    		handler.getTable().getGame().checkInterrupted();
	    		
	    		A result = availableAnnounces[resultIndex];

	    		Server.logger.fine(String.format(
	    			"Player %s -> ChooseAnnounce() -> %s",
	    			handler.getLoginName(), result.toString()));

	    		return result;
	    	}
	    	catch (IOException error)
	    	{
	    		throw Server.exceptionToCardGameError(
	    			PlayerRemoteController.class.getName(), "chooseAnnounce()",
	    			error);
	    	}
    	}

	    /**
    	 * {@inheritDoc}
	     */
	    synchronized public Card playTurn() throws CardGameException
	    {
	    	Server.logger.fine(String.format("Player %s -> PlayTurn()",
	    		handler.getLoginName()));
	    	
	    	writer.writeCommand("PLAYTURN");

	    	try
	    	{
	    		reader.readResponse();
	    		Deck deck = handler.getTable().getGame().getDeck();
	    		Card result = reader.readCard(deck);

	    		handler.getTable().getGame().checkInterrupted();

		    	Server.logger.fine(String.format(
		    		"Player %s -> PlayTurn() -> %s",
		    		handler.getLoginName(), result.getLongName()));
	    		
	    		return result;
	    	}
	    	catch (IOException error)
	    	{
	    		throw Server.exceptionToCardGameError(
	    			PlayerRemoteController.class.getName(), "playTurn()",
	    			error);
	    	}
	    }

	    /**
	     * {@inheritDoc}
	     */
	    synchronized public void pause() throws CardGameException
	    {
	    	Server.logger.finer(String.format("Player %s -> Pause()",
	    		handler.getLoginName()));
	    	
	    	writer.writeCommand("PAUSE");

	    	try
	    	{
	    		reader.readResponse();

	    		handler.getTable().getGame().checkInterrupted();

		    	Server.logger.finer(String.format(
		    		"Player %s -> Pause() -> Done",
		    		handler.getLoginName()));
	    	}
	    	catch (IOException error)
	    	{
	    		throw Server.exceptionToCardGameError(
	    			PlayerRemoteController.class.getName(), "pause()",
	    			error);
	    	}
	    }

	    /**
    	 * {@inheritDoc}
	     */
	    public void notifyUpdate()
	    {
	    	handler.notifyUpdate();
	    }

	    /**
    	 * {@inheritDoc}
	     */
	    public void showMessage(Message message)
	    {
	    	handler.sendMessage(message);
	    }
    }
}
