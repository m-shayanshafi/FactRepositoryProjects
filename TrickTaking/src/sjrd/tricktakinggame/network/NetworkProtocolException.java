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

/**
 * Erreur de protocole réseau
 * @author sjrd
 */
public class NetworkProtocolException extends NetworkException
{
	/**
	 * ID de sérialisation
	 */
	private static final long serialVersionUID = 1;

	/**
	 * {@inheritDoc}
	 */
	public NetworkProtocolException()
	{
		super(ResponseCode.Protocol);
	}

	/**
	 * {@inheritDoc}
	 */
	public NetworkProtocolException(String msg)
	{
		super(ResponseCode.Protocol, msg);
	}

	/**
	 * {@inheritDoc}
	 */
	public NetworkProtocolException(Throwable cause)
	{
		super(ResponseCode.Protocol, cause);
	}

	/**
	 * {@inheritDoc}
	 */
	public NetworkProtocolException(String msg, Throwable cause)
	{
		super(ResponseCode.Protocol, msg, cause);
	}

	/**
	 * {@inheritDoc}
	 */
	public NetworkProtocolException(Response aResponse)
	{
		super(aResponse);
	}

	/**
	 * {@inheritDoc}
	 */
	public NetworkProtocolException(Response aResponse, Throwable cause)
	{
		super(aResponse, cause);
	}
}
