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
package sjrd.swing;

import java.util.concurrent.*;

import javax.swing.*;

/**
 * Extension de <tt>SwingWorker</tt> avec vérification des exceptions générées
 * @author sjrd
 * @param <T> {@inheritDoc}
 * @param <V> {@inheritDoc}
 * @param <E> Type d'exception pouvant survenir dans <tt>doInBackground()</tt>
 */
public abstract class SafeExceptSwingWorker<T, V, E extends Exception>
	extends SwingWorker<T, V>
{
	/**
	 * {@inheritDoc}
	 */
	protected abstract T doInBackground() throws E;
	
	/**
	 * Récupère le résultat de l'exécution
	 * <p>
	 * À la différence de <tt>get()</tt>, cette méthode déclenche, en cas
	 * d'exception survenue dans <tt>doInBackground()</tt>, cette exception
	 * précise et non son encapsulation dans une <tt>ExecutionException</tt>.
	 * </p>
	 * @return Résultat de l'exécution
	 * @throws E Exception durant l'exécution de <tt>doInBackground()</tt>
	 * @throws InterruptedException {@inheritDoc}
	 */
	@SuppressWarnings("unchecked")
	protected T getResult() throws E, InterruptedException
	{
		try
		{
			return get();
		}
		catch (ExecutionException error)
		{
			Throwable cause = error.getCause();
			if (cause instanceof RuntimeException)
				throw (RuntimeException) cause;
			else
				throw (E) cause;
		}
	}
	
	/**
	 * Récupère le résultat de l'exécution
	 * @param interruptedDefault Résultat par défaut si interruption
	 * @return Résultat de l'exécution
	 * @throws E Exception durant l'exécution de <tt>doInBackground()</tt>
	 */
	protected T getResultDef(T interruptedDefault) throws E
	{
		try
		{
			return getResult();
		}
		catch (InterruptedException error)
		{
			return interruptedDefault;
		}
	}
	
	/**
	 * Récupère le résultat de l'exécution, sans tenir compte des interruptions
	 * <p>
	 * Toute exception de type <tt>InterruptedException</tt> se retrouve
	 * encapsulée dans une exception de type <tt>RuntimeException</tt>.
	 * </p>
	 * @return Résultat de l'exécution
	 * @throws E Exception durant l'exécution de <tt>doInBackground()</tt>
	 */
	protected T getResultNoInterrupt() throws E
	{
		try
		{
			return getResult();
		}
		catch (InterruptedException error)
		{
			throw new RuntimeException(error);
		}
	}
}
