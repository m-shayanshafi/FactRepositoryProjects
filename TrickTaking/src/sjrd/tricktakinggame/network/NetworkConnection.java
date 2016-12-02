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
package sjrd.tricktakinggame.network;

import java.io.*;
import java.net.*;

/**
 * Connexion réseau
 * @author sjrd
 */
public class NetworkConnection
{
	/**
	 * Socket
	 */
	protected Socket socket;

	/**
	 * Ecrivain
	 */
	protected NetworkWriter writer;

	/**
	 * Lecteur
	 */
	protected NetworkReader reader;

	/**
	 * Crée une nouvelle connexion réseau
	 * @param aSocket
	 * @throws IOException
	 */
	public NetworkConnection(Socket aSocket) throws IOException
	{
		super();

		socket = aSocket;
		writer = new NetworkWriter(socket.getOutputStream());
		reader = new NetworkReader(socket.getInputStream());
	}

	/**
	 * Ferme la connexion
	 */
	public void close() throws IOException
	{
		socket.close();
	}
	
	/**
	 * Socket
	 * @return Socket
	 */
	public Socket getSocket()
	{
		return socket;
	}
	
	/**
	 * Ecrivain
	 * @return Ecrivain
	 */
	public NetworkWriter getWriter()
	{
		return writer;
	}
	
	/**
	 * Lecteur
	 * @return Lecteur
	 */
	public NetworkReader getReader()
	{
		return reader;
	}
}
