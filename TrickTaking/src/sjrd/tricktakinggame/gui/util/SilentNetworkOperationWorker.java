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

import sjrd.tricktakinggame.network.*;

/**
 * Worker pour des opérations réseau qui passe sous silence les exceptions
 * @author sjrd
 */
public abstract class SilentNetworkOperationWorker<T, V> extends
    NetworkOperationWorker<T, V>
{
	/**
	 * {@inheritDoc}
	 * <p>
	 * Dans <tt>SilentNetworkOperationWorker</tt>, cette méthode ne fait rien.
	 * </p>
	 */
	protected void onNetworkException(NetworkException error)
	{
	}

	/**
	 * {@inheritDoc}
	 * <p>
	 * Dans <tt>SilentNetworkOperationWorker</tt>, cette méthode ne fait rien.
	 * </p>
	 */
	protected void onIOException(IOException error)
	{
	}
}
