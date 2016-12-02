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
package sjrd.util.appproperties;

/**
 * Exception déclenchée lorsqu'une propriété n'a pas été trouvée
 * @author sjrd
 */
public class PropertyNotFoundException extends PropertyException
{
	/**
	 * ID de sérialisation
	 */
	private static final long serialVersionUID = 1;
	
	/**
	 * Crée l'exception
	 */
	public PropertyNotFoundException()
	{
		super();
	}

	/**
	 * Crée l'exception
	 * @param message Message
	 */
	public PropertyNotFoundException(String message)
	{
		super(message);
	}

	/**
	 * Crée l'exception
	 * @param cause Cause
	 */
	public PropertyNotFoundException(Throwable cause)
	{
		super(cause);
	}

	/**
	 * Crée l'exception
	 * @param message Message
	 * @param cause Cause
	 */
	public PropertyNotFoundException(String message, Throwable cause)
	{
		super(message, cause);
	}
}
