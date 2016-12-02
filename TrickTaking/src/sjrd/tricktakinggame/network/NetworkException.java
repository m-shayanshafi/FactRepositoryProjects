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

/**
 * Exception pouvant passer par le réseau
 * @author sjrd
 */
public class NetworkException extends IOException
{
	/**
	 * ID de sérialisation
	 */
	private static final long serialVersionUID = 1;
	
	/**
	 * Réponse
	 */
	private Response response = Response.UnknownResponse;

	/**
	 * {@inheritDoc}
	 */
	public NetworkException()
	{
		super();
	}

	/**
	 * {@inheritDoc}
	 */
	public NetworkException(String message)
	{
		super(message);
	}

	/**
	 * {@inheritDoc}
	 */
	public NetworkException(Throwable cause)
	{
		super(cause);
	}

	/**
	 * {@inheritDoc}
	 */
	public NetworkException(String message, Throwable cause)
	{
		super(message, cause);
	}

	/**
	 * {@inheritDoc}
	 * @param aResponse Réponse
	 */
	public NetworkException(Response aResponse)
	{
		this(aResponse.getMessage());
		response = aResponse;
	}

	/**
	 * {@inheritDoc}
	 * @param aResponse Réponse
	 */
	public NetworkException(Response aResponse, Throwable cause)
	{
		this(aResponse.getMessage(), cause);
		response = aResponse;
	}

	/**
	 * {@inheritDoc}
	 * @param aErrorCode Code d'erreur
	 */
	public NetworkException(ResponseCode aErrorCode)
	{
		this(new Response(aErrorCode));
	}

	/**
	 * {@inheritDoc}
	 * @param aErrorCode Code d'erreur
	 */
	public NetworkException(ResponseCode aErrorCode, String message)
	{
		this(new Response(aErrorCode, message));
	}

	/**
	 * {@inheritDoc}
	 * @param aErrorCode Code d'erreur
	 */
	public NetworkException(ResponseCode aErrorCode, Throwable cause)
	{
		this(new Response(aErrorCode), cause);
	}

	/**
	 * {@inheritDoc}
	 * @param aErrorCode Code d'erreur
	 */
	public NetworkException(ResponseCode aErrorCode, String message,
		Throwable cause)
	{
		this(new Response(aErrorCode, message), cause);
	}
	
	/**
	 * Réponse
	 * @return Réponse
	 */
	public Response getResponse()
	{
		return response;
	}
}
