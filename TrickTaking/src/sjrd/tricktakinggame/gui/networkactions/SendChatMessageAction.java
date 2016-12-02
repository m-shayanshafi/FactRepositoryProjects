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
package sjrd.tricktakinggame.gui.networkactions;

import java.awt.event.*;
import java.io.*;

import javax.swing.*;

import sjrd.tricktakinggame.gui.util.*;
import sjrd.tricktakinggame.network.client.*;
import sjrd.tricktakinggame.remotable.*;

/**
 * Action d'envoyer un message de chat
 * @author sjrd
 */
public class SendChatMessageAction extends AbstractAction
{
	/**
	 * ID de sérialisation
	 */
	private static final long serialVersionUID = 1;
	
	/**
	 * Client connecté
	 */
	private Client client;
	
	/**
	 * Fournisseur du message à envoyer
	 */
	private MessageProvider messageProvider;
	
	/**
	 * Crée l'action
	 * @param aOwner Panel propriétaire
	 * @param aMessageProvider Fournisseur du message à envoyer
	 * @param text Texte de l'action
	 */
	public SendChatMessageAction(Client aClient,
		MessageProvider aMessageProvider, String text)
	{
		super(text);
		client = aClient;
		messageProvider = aMessageProvider;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public void actionPerformed(ActionEvent event)
	{
		if (!isEnabled())
			return;
		
		Message message = messageProvider.fetchMessage();
		if (message == null)
			return;
		
		new SendChatMessageWorker(message).execute();
	}
	
	/**
	 * Worker pour envoyer un message de chat
	 * @author sjrd
	 */
	private class SendChatMessageWorker
		extends NetworkOperationWorker<Object, Object>
	{
		/**
		 * Message à envoyer
		 */
		private Message message;
		
		/**
		 * Crée le worker
		 * @param aMessage Message à envoyer
		 */
		public SendChatMessageWorker(Message aMessage)
		{
			super();
			message = aMessage;
		}
		
		/**
		 * {@inheritDoc}
		 */
		@Override
		protected Object doInBackground() throws IOException
		{
			client.getCommander().sendChatMessage(message);
			return null;
		}
	}
}
