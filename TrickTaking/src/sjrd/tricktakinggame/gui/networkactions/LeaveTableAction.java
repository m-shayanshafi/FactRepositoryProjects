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
import sjrd.tricktakinggame.network.*;
import sjrd.tricktakinggame.network.client.*;
import sjrd.tricktakinggame.gui.client.*;
import sjrd.tricktakinggame.gui.util.*;

/**
 * Action de quitter la table
 * @author sjrd
 */
public class LeaveTableAction extends AbstractAction
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
	public LeaveTableAction(ClientSubPanel aOwner, String text)
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
		
		LeaveTableWorker worker = new LeaveTableWorker();
		owner.setCurrentWorker(worker);
		worker.execute();
	}
	
	/**
	 * Worker pour quitter la table
	 * @author sjrd
	 */
	private class LeaveTableWorker
		extends NetworkOperationWorker<ClientStatus, Object>
	{
		/**
		 * {@inheritDoc}
		 */
		@Override
		protected ClientStatus doInBackground() throws IOException
		{
			Client client = owner.getClient();
			client.getCommander().leaveTable();
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
		protected void onNetworkException(NetworkException error)
		{
			ResponseCode code = error.getResponse().getCode();
			
			if (code == ResponseCode.Forbidden)
			{
				JOptionPane.showMessageDialog(null,
					"Vous n'avez pas le droit de quitter la table maintenant.",
					"Action interdite", JOptionPane.ERROR_MESSAGE);
			}
			else
			{
				super.onNetworkException(error);
			}
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
