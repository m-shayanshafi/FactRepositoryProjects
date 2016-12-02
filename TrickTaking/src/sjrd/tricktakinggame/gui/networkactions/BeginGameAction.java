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

import sjrd.tricktakinggame.client.*;
import sjrd.tricktakinggame.network.client.*;
import sjrd.tricktakinggame.gui.client.*;
import sjrd.tricktakinggame.gui.util.*;

/**
 * Action de commencer une partie
 * @author sjrd
 */
public class BeginGameAction extends AbstractAction
{
	/**
	 * ID de sérialisation
	 */
	private static final long serialVersionUID = 1;
	
	/**
	 * Panel propriétaire
	 */
	private ClientSubPanel owner;
	
	/**
	 * Crée l'action
	 * @param text Texte de l'action
	 */
	public BeginGameAction(ClientSubPanel aOwner, String text)
	{
		super(text);
		owner = aOwner;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public void actionPerformed(ActionEvent event)
	{
		owner.updateActionsEnabled();
		if (!isEnabled())
			return;
		
		BeginGameWorker worker = new BeginGameWorker();
		owner.setCurrentWorker(worker);
		worker.execute();
	}
	
	/**
	 * Worker pour commencer une partie
	 * @author sjrd
	 */
	private class BeginGameWorker
		extends NetworkOperationWorker<ClientStatus, Object>
	{
		/**
		 * {@inheritDoc}
		 */
		@Override
		protected ClientStatus doInBackground() throws IOException
		{
			Client client = owner.getClient();
			client.getCommander().beginGame();
			return client.fetchStatus();
		}
		
		/**
		 * {@inheritDoc}
		 */
		@Override
		protected void useResult(ClientStatus result)
		{
			owner.getOwner().clientStatusUpdated(result);
		}
		
		/**
		 * {@inheritDoc}
		 */
		@Override
		protected void done()
		{
			owner.updateActionsEnabled();
			super.done();
		}
	}
}
