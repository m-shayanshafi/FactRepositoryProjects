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
package sjrd.tricktakinggame.gui.connection;

import java.awt.event.*;
import java.io.*;
import java.net.*;

import javax.swing.*;

import sjrd.tricktakinggame.gui.client.*;
import sjrd.tricktakinggame.gui.util.*;
import sjrd.tricktakinggame.network.*;
import sjrd.tricktakinggame.network.client.*;

import static sjrd.util.HashUtils.*;

/**
 * Action de connexion
 * @author sjrd
 */
class ConnectAction extends AbstractAction
{
	/**
	 * ID de sérialisation
	 */
	private static final long serialVersionUID = 1;
	
	/**
	 * Panel propriétaire
	 */
	private ConnectionFrame owner;
	
	/**
	 * Crée une action de connexion
	 * @param text Texte de l'action
	 */
	public ConnectAction(ConnectionFrame aOwner, String text)
	{
		super(text);
		owner = aOwner;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public void actionPerformed(ActionEvent event)
	{
		setEnabled(false);
		new ConnectWorker(owner.hostField.getText(),
			owner.loginNameField.getText(),
			md5String(owner.passwordField.getText())).execute();
	}

	/**
	 * Worker pour la connexion au serveur
	 * @author sjrd
	 */
	private class ConnectWorker
		extends NetworkOperationWorker<Client, Object>
	{
		/**
		 * Nom d'hôte
		 */
		private String hostName;
		
		/**
		 * Port du serveur
		 */
		private int serverPort;
		
		/**
		 * Nom de login
		 */
		private String loginName;
		
		/**
		 * Mot de passe hashé
		 */
		private String passwordHash;
		
		/**
		 * Crée un worker pour la connexion au serveur
		 * @param aHostName Nom d'hôte
		 * @param aServerPort Port du serveur
		 * @param aLoginName Nom de login
		 * @param aPasswordHash Mot de passe hashé
		 */
		public ConnectWorker(String aHostName, int aServerPort,
			String aLoginName, String aPasswordHash)
		{
			super();
			
			hostName = aHostName;
			serverPort = aServerPort;
			loginName = aLoginName;
			passwordHash = aPasswordHash;
		}
		
		/**
		 * Crée un worker pour la connexion au serveur
		 * @param aHostName Nom d'hôte
		 * @param aLoginName Nom de login
		 * @param aPasswordHash Mot de passe hashé
		 */
		public ConnectWorker(String aHostName, String aLoginName,
			String aPasswordHash)
		{
			this(aHostName, NetworkInfo.defaultPort, aLoginName, aPasswordHash);
		}
		
		/**
		 * {@inheritDoc}
		 */
		@Override
		protected Client doInBackground() throws IOException
		{
			return new Client(hostName, serverPort, loginName, passwordHash);
		}
		
		/**
		 * {@inheritDoc}
		 */
		@Override
		protected void useResult(Client result)
		{
			new ClientFrame(result);
			owner.dispose();
		}
		
		/**
		 * {@inheritDoc}
		 */
		@Override
		protected void onNetworkException(NetworkException error)
		{
			ResponseCode code = error.getResponse().getCode();
			
			if (code == ResponseCode.InvalidLogin)
			{
				JOptionPane.showMessageDialog(null,
					"Nom de login incorrect.",
					"Login incorrect", JOptionPane.ERROR_MESSAGE);
			}
			else if (code == ResponseCode.UserAlreadyLogged)
			{
				JOptionPane.showMessageDialog(null,
					"Cet utilisateur est déjà connecté.",
					"Utilisateur déjà connecté", JOptionPane.ERROR_MESSAGE);
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
		protected void onIOException(IOException error)
		{
			if (error instanceof UnknownHostException)
			{
				JOptionPane.showMessageDialog(null,
					"Nom d'hôte du serveur inconnu. Vérifiez l'orthographe " +
					"de celui-ci, et vérifiez que vous êtes bien connecté à " +
					"Internet.",
					"Nom d'hôte inconnu", JOptionPane.ERROR_MESSAGE);
			}
			else if (error instanceof ConnectException)
			{
				JOptionPane.showMessageDialog(null,
					"Connexion refusée. Le serveur est peut-être éteint.",
					"Connexion refusée", JOptionPane.ERROR_MESSAGE);
			}
			else
			{
				super.onIOException(error);
			}
		}
		
		/**
		 * {@inheritDoc}
		 */
		@Override
		protected void done()
		{
			setEnabled(true);
			super.done();
		}
	}
}
