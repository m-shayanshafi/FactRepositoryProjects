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
 * Connexion gérée dans un thread
 * <p>
 * Une fois la connexion établie, la méthode <tt>execute()</tt> est appelée en
 * boucle jusqu'à ce qu'une erreur I/O survienne, et ce dans un thread.
 * </p>
 * @author sjrd
 */
public abstract class ThreadedConnection extends NetworkConnection
{
	/**
	 * Verrou pour la modification de <tt>executor</tt>
	 * @see #executor
	 */
	private final Object executorLock = new Object();
	
	/**
	 * Exécuteur
	 */
	private Executor executor = null;

	/**
	 * Crée une connexion gérée dans un thread
	 * <p>
	 * Pour démarrer le thread ultérieurement lorsque <tt>createSuspended</tt>
	 * vaut <tt>true</tt>, utilisez l'objet thread renvoyé par
	 * <tt>getThread()</tt>.
	 * </p>
	 * @param aSocket {@inheritDoc}
	 * @param createSuspended <tt>true</tt> pour ne pas démarrer immédiatement
	 * @throws IOException {@inheritDoc}
	 */
	public ThreadedConnection(Socket aSocket, boolean createSuspended)
		throws IOException
	{
		super(aSocket);
		
		recreateThread(createSuspended);
	}

	/**
	 * Crée une connexion gérée dans un thread qui démarre immédiatement
	 * @param aSocket {@inheritDoc}
	 * @throws IOException {@inheritDoc}
	 */
	public ThreadedConnection(Socket aSocket) throws IOException
	{
		this(aSocket, false);
	}
	
	/**
	 * Thread d'exécution
	 * <p>
	 * Ce thread appelle en boucle la méthode <tt>execute()</tt>, jusqu'à ce
	 * qu'une de ces deux conditions soit satisfaite :
	 * </p>
	 * <ul>
	 *   <li>Le thread est marqué comme interrompu ;</li>
	 *   <li>Le socket géré est fermé.</li>
	 * </ul>
	 * @return Thread d'exécution
	 */
	protected Thread getThread()
	{
		synchronized (executorLock)
		{
			return executor;
		}
	}
	
	/**
	 * Recrée le thread d'exécution
	 * <p>
	 * Avant cela, l'ancient thread d'exécution est interrompu, au moyen de sa
	 * méthode <tt>interrupt()</tt>.
	 * </p>
	 * <p>
	 * <b>Attention :</b> quand l'ancien thread sera tout à fait terminé, la
	 * méthode <tt>onThreadTerminated()</tt> sera aussi appelée.
	 * </p>
	 * @param createSuspended
	 */
	protected void recreateThread(boolean createSuspended)
	{
		synchronized (executorLock)
		{
			if (executor != null)
				executor.interrupt();
			
			executor = new Executor();
			if (!createSuspended)
				executor.start();
		}
	}
	
	/**
	 * Méthode d'exécution de la connexion
	 * <p>
	 * Les classes descendantes de <tt>ThreadedConnection</tt> doivent
	 * surcharger cette méthode pour indiquer le comportement de la connexion.
	 * </p>
	 * @throws NetworkException Erreur réseau
	 * @throws IOException Erreur I/O
	 */
	protected abstract void execute() throws IOException, InterruptedException;

	/**
	 * Gère une erreur à transmettre sur le réseau déclenchée par la méthode
	 * <tt>execute()</tt>
	 * <p>
	 * L'implémentation par défaut dans <tt>ThreadedConnection</tt> envoie une
	 * réponse avec les code et message d'erreur de l'exception.
	 * </p>
	 * @param error Erreur déclenchée
	 */
	protected void onNetworkException(NetworkException error)
	{
		writer.writeResponse(error.getResponse());
	}

	/**
	 * Gère une erreur I/O déclenchée par la méthode <tt>execute()</tt>
	 * <p>
	 * L'implémentation par défaut dans <tt>ThreadedConnection</tt> ne fait
	 * rien.
	 * </p>
	 * @param error Erreur déclenchée
	 */
	protected void onIOException(IOException error)
	{
	}

	/**
	 * Déclenché lorsque le thread d'exécution se termine
	 * <p>
	 * L'implémentation par défaut dans <tt>ThreadedConnection</tt> ne fait
	 * rien.
	 * </p>
	 */
	protected void onThreadTerminated()
	{
	}

	/**
	 * Exécuteur du répondeur
	 * @author sjrd
	 */
	private class Executor extends Thread
	{
		/**
		 * {@inheritDoc}
		 */
		@Override
		public void run()
		{
			try
			{
				while (!interrupted() && !socket.isClosed())
				{
					try
					{
						execute();
					}
					catch (NetworkException error)
					{
						onNetworkException(error);
					}
					catch (InterruptedException ignore)
					{
					}
				}
			}
			catch (IOException error)
			{
				onIOException(error);
			}
			
			onThreadTerminated();
		}
	}
}
