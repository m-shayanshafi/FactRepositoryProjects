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
public class TableListModel extends AbstractListModel
{
	/**
	 * ID de sérialisation
	 */
	private static final long serialVersionUID = 1;
	
	/**
	 * Règles disponibles sur le serveur, à la dernière récupération
	 */
	private RulesInfo[] availableRules = null;

	/**
	 * Tables ouvertes sur le serveur, à la dernière récupération
	 */
	private TableInfo[] openTables = new TableInfo[0];
	
	/**
	 * Crée le modèle de liste de tables
	 */
	public TableListModel()
	{
		super();
	}
	
	/**
	 * Spécifie les informations sur les règles à utiliser
	 * <p>
	 * Cette nouvelle information ne sera prise en compte qu'à la prochaine
	 * mise à jour des tables disponibles (avec <tt>update(Client)</tt>
	 * </p>
	 * @param rulesInfo Informations sur les règles
	 * @see #update(Client)
	 */
	public void setRulesInfo(RulesInfo[] rulesInfo)
	{
		availableRules = rulesInfo;
	}

	/**
	 * {@inheritDoc}
	 */
	public int getSize()
	{
		return openTables.length;
	}

	/**
	 * {@inheritDoc}
	 */
	public TableInfo getElementAt(int index)
	{
		return openTables[index];
	}
	
	/**
	 * Liste de tous les éléments
	 * @return Liste de tous les éléments
	 */
	public TableInfo[] getElements()
	{
		return openTables.clone();
	}
	
	/**
	 * Met à jour la liste des tables
	 * @param client Client
	 */
	public void update(Client client)
	{
		new UpdateWorker(client, availableRules).execute();
	}
	
	/**
	 * Liste mise à jour
	 * @param aOpenTables Nouvelle liste de tables
	 */
	private void updated(TableInfo[] aOpenTables)
	{
		int oldSize = openTables.length;
		int newSize = aOpenTables.length;
		
		openTables = aOpenTables;
		
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
		extends SilentNetworkOperationWorker<TableInfo[], Object>
	{
		/**
		 * Client
		 */
		private Client client;
		
		/**
		 * Informations sur les règles disponibles
		 */
		private RulesInfo[] availableRules;
		
		/**
		 * Crée le worker
		 * @param aClient Client
		 */
		public UpdateWorker(Client aClient, RulesInfo[] aAvailableRules)
		{
			super();
			
			client = aClient;
			availableRules = aAvailableRules;
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		protected void useResult(TableInfo[] result)
		{
			updated(result);
		}
		
		/**
		 * {@inheritDoc}
		 */
		@Override
		protected TableInfo[] doInBackground() throws IOException
		{
			return client.getCommander().listTables(availableRules);
		}
	}
}
