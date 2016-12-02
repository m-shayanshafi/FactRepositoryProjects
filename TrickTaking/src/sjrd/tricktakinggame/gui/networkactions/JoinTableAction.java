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

import java.io.*;
import java.awt.event.*;

import javax.swing.*;

import sjrd.tricktakinggame.client.*;
import sjrd.tricktakinggame.network.*;
import sjrd.tricktakinggame.network.client.*;
import sjrd.tricktakinggame.gui.client.*;
import sjrd.tricktakinggame.gui.util.*;

/**
 * Action de joindre une table
 * @author sjrd
 */
public class JoinTableAction extends AbstractAction
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
	 * Sélectionneur de table
	 */
	private TableSelector tableSelector;
	
	/**
	 * Crée l'action
	 * @param aOwner Panel propriétaire
	 * @param aTableSelector Sélectionneur de table
	 * @param text Texte de l'action
	 */
	public JoinTableAction(ClientSubPanel aOwner, TableSelector aTableSelector,
		String text)
	{
		super(text);
		owner = aOwner;
		tableSelector = aTableSelector;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public void actionPerformed(ActionEvent event)
	{
		owner.updateActionsEnabled();
		if (!isEnabled())
			return;
		
		TableInfo tableInfo = tableSelector.getSelectedTable();
		if (tableInfo == null)
			return;
		
		JoinTableWorker worker = new JoinTableWorker(tableInfo);
		owner.setCurrentWorker(worker);
		worker.execute();
	}
	
	/**
	 * Worker pour rejoindre la table
	 * @author sjrd
	 */
	private class JoinTableWorker
		extends NetworkOperationWorker<ClientStatus, Object>
	{
		/**
		 * Informations sur la table à rejoindre
		 */
		private TableInfo tableInfo;
		
		/**
		 * Crée le worker
		 * @param aTableInfo Informations sur la table à rejoindre
		 */
		public JoinTableWorker(TableInfo aTableInfo)
		{
			super();
			tableInfo = aTableInfo;
		}
		
		/**
		 * {@inheritDoc}
		 */
		@Override
		protected ClientStatus doInBackground() throws IOException
		{
			Client client = owner.getClient();
			client.getCommander().joinTable(tableInfo);
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
			
			if (code == ResponseCode.TableIsFull)
			{
				JOptionPane.showMessageDialog(null,
					"La table était déjà pleine.", "Table pleine",
					JOptionPane.ERROR_MESSAGE);
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
