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
 * Action de création de table
 * @author sjrd
 */
public class CreateTableAction extends AbstractAction
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
	 * Sélectionneur de règles
	 */
	private RulesSelector rulesSelector;
	
	/**
	 * Crée l'action
	 * @param text Texte de l'action
	 */
	public CreateTableAction(ClientSubPanel aOwner,
		RulesSelector aRulesSelector, String text)
	{
		super(text);
		owner = aOwner;
		rulesSelector = aRulesSelector;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public void actionPerformed(ActionEvent event)
	{
		owner.updateActionsEnabled();
		if (!isEnabled())
			return;
		
		RulesInfo rulesInfo = rulesSelector.getSelectedRules();
		int playerCount = rulesSelector.getSelectedPlayerCount();
		
		if ((playerCount < rulesInfo.getMinPlayerCount()) ||
			(playerCount > rulesInfo.getMaxPlayerCount()))
		{
			JOptionPane.showMessageDialog(null,
				String.format(
					"Vous devez entrer un nombre de joueurs entre %d et " +
					"%d compris", rulesInfo.getMinPlayerCount(),
					rulesInfo.getMaxPlayerCount()),
				"Nombre de joueurs invalide", JOptionPane.ERROR_MESSAGE);
			return;
		}
		
		CreateTableWorker worker = new CreateTableWorker(rulesInfo,
			playerCount);
		owner.setCurrentWorker(worker);
		worker.execute();
	}
	
	/**
	 * Worker pour créer la table
	 * @author sjrd
	 */
	private class CreateTableWorker
		extends NetworkOperationWorker<ClientStatus, Object>
	{
		/**
		 * Informations sur les règles à utiliser
		 */
		private RulesInfo rulesInfo;
		
		/**
		 * Nombre de joueurs à la table
		 */
		private int playerCount;
		
		/**
		 * Crée le worker
		 * @param aRulesInfo Informations sur les règles à utiliser
		 * @param aPlayerCount Nombre de joueurs à la table
		 */
		public CreateTableWorker(RulesInfo aRulesInfo, int aPlayerCount)
		{
			super();
			rulesInfo = aRulesInfo;
			playerCount = aPlayerCount;
		}
		
		/**
		 * {@inheritDoc}
		 */
		@Override
		protected ClientStatus doInBackground() throws IOException
		{
			Client client = owner.getClient();
			client.getCommander().createTable(rulesInfo, playerCount);
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
			
			if (code == ResponseCode.BadPlayerCount)
			{
				JOptionPane.showMessageDialog(null,
					"Nombre de joueurs incorrect.",
					"Nombre de joueurs incorrect",
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
