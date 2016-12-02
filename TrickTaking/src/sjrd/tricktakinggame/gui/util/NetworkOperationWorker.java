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

import javax.swing.*;

import sjrd.swing.*;
import sjrd.tricktakinggame.network.*;

/**
 * Worker pour une opération ayant lieu <i>via</i> le réseau
 * @author sjrd
 */
public abstract class NetworkOperationWorker<T, V>
	extends SafeExceptSwingWorker<T, V, IOException>
{
	/**
	 * Utilise le résultat de l'opération
	 * <p>
	 * L'implémentation par défaut ne fait rien.
	 * </p>
	 * @param result Résultat de l'opération
	 */
	protected void useResult(T result)
	{
	}
	
	/**
	 * Déclenché en cas d'exception de type <tt>NetworkException</tt>
	 * <p>
	 * L'implémentation par défaut affiche un message d'erreur.
	 * </p>
	 * @param error Exception générée
	 */
	protected void onNetworkException(NetworkException error)
	{
		JOptionPane.showMessageDialog(null,
			error.getMessage(), error.getClass().getName(),
			JOptionPane.ERROR_MESSAGE);
	}
	
	/**
	 * Déclenché en cas d'exception I/O générique
	 * <p>
	 * L'implémentation par défaut affiche un message d'erreur.
	 * </p>
	 * @param error Exception générée
	 */
	protected void onIOException(IOException error)
	{
		JOptionPane.showMessageDialog(null,
			error.getMessage(), error.getClass().getName(),
			JOptionPane.ERROR_MESSAGE);
	}
	
	/**
	 * Déclenché en cas d'exception <tt>InterruptedException</tt>
	 * <p>
	 * L'implémentation par défaut ne fait rien.
	 * </p>
	 * @param error Exception générée
	 */
	protected void onInterruptedException(InterruptedException error)
	{
	}
	
	/**
	 * Termine le worker dans l'EDT
	 * <p>
	 * L'implémentation de cette méthode dans <tt>NetworkOperationWorker</tt>
	 * tente de récupérer le résultat de l'opération. Si elle y parvient, elle
	 * appelle <tt>useResult(T)</tt> avec ce résultat en paramètre. En cas
	 * d'erreur, une des trois méthodes
	 * <tt>onNetworkException(NetworkException)</tt>,
	 * <tt>onIOException(IOException)</tt> ou
	 * <tt>onInterruptedException(InterruptedException)</tt>, selon le type
	 * d'exception.
	 * </p>
	 */
	@Override
	protected void done()
	{
		try
		{
			useResult(getResult());
		}
		catch (NetworkException error)
		{
			onNetworkException(error);
		}
		catch (IOException error)
		{
			onIOException(error);
		}
		catch (InterruptedException error)
		{
			onInterruptedException(error);
		}
	}
}
