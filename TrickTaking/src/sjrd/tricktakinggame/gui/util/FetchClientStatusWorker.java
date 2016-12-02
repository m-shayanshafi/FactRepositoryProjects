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
package sjrd.tricktakinggame.gui.util;

import java.io.*;

import sjrd.tricktakinggame.client.*;
import sjrd.tricktakinggame.network.client.*;

/**
 * Worker de base pour récupérer le statut d'un client
 * <p>
 * Une invocation de ce type de worker mettra à jour le statut du client.
 * </p>
 * <p>
 * Il est encore possible de dériver des sous-classes et de surcharger la
 * méthode <tt>useResult(ClientStatus)</tt>, qui n'est pas utilisée par cette
 * classe, pour faire des actions supplémentaires à la réception du nouveau
 * statut.
 * </p>
 * @author sjrd
 */
public class FetchClientStatusWorker
	extends SilentNetworkOperationWorker<ClientStatus, Object>
{
	/**
	 * Client dont récupérer le statut
	 */
	private Client client;
	
	/**
	 * Crée le worker
	 * @param aClient Client dont récupérer le statut
	 */
	public FetchClientStatusWorker(Client aClient)
	{
		super();
		client = aClient;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected ClientStatus doInBackground() throws IOException
	{
		return client.fetchStatus();
	}
}
