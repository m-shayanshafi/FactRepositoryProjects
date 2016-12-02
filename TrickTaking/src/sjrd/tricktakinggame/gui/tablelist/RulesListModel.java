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
package sjrd.tricktakinggame.gui.tablelist;

import java.io.*;

import javax.swing.*;

import sjrd.tricktakinggame.client.*;
import sjrd.tricktakinggame.network.client.*;
import sjrd.tricktakinggame.gui.util.*;

/**
 * Modèle de liste pour la liste des tables ouvertes
 * @author sjrd
 */
public class RulesListModel extends AbstractListModel
{
	/**
	 * ID de sérialisation
	 */
	private static final long serialVersionUID = 1;
	
	/**
	 * Règles disponibles sur le serveur, à la dernière récupération
	 */
	private RulesInfo[] availableRules = new RulesInfo[0];
	
	/**
	 * Crée le modèle de liste de tables
	 */
	public RulesListModel()
	{
		super();
	}

	/**
	 * {@inheritDoc}
	 */
	public int getSize()
	{
		return availableRules.length;
	}

	/**
	 * {@inheritDoc}
	 */
	public RulesInfo getElementAt(int index)
	{
		return availableRules[index];
	}
	
	/**
	 * Liste de tous les éléments
	 * @return Liste de tous les éléments
	 */
	public RulesInfo[] getElements()
	{
		return availableRules.clone();
	}
	
	/**
	 * Met à jour la liste des tables
	 * @param client Client
	 */
	public void update(Client client)
	{
		new UpdateWorker(client).execute();
	}
	
	/**
	 * Liste mise à jour
	 * @param aOpenTables Nouvelle liste de tables
	 */
	private void updated(RulesInfo[] aAvailableRules)
	{
		int oldSize = availableRules.length;
		int newSize = aAvailableRules.length;
		
		availableRules = aAvailableRules;
		
		if (newSize > oldSize)
		{
			fireIntervalAdded(this, oldSize, newSize-1);
			fireContentsChanged(this, 0, oldSize-1);
		}
		else if (newSize < oldSize)
		{
			fireIntervalRemoved(this, newSize, oldSize-1);
			fireContentsChanged(this, 0, newSize-1);
		}
		else
		{
			fireContentsChanged(this, 0, newSize-1);
		}
	}

	/**
	 * Worker pour mettre à jour
	 * @author sjrd
	 */
	private class UpdateWorker
		extends SilentNetworkOperationWorker<RulesInfo[], Object>
	{
		/**
		 * Client
		 */
		private Client client;
		
		/**
		 * Crée le worker
		 * @param aClient Client
		 */
		public UpdateWorker(Client aClient)
		{
			super();
			
			client = aClient;
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		protected void useResult(RulesInfo[] result)
		{
			updated(result);
		}
		
		/**
		 * {@inheritDoc}
		 */
		@Override
		protected RulesInfo[] doInBackground() throws IOException
		{
			return client.getCommander().listAvailableRules();
		}
	}
}
