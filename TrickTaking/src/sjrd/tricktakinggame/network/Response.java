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
 * Réponse réseau, avec un code de réponse et un message
 * @author sjrd
 */
public class Response
{
	/**
	 * Réponse inconnue
	 */
	public static final Response UnknownResponse = new Response();
	
	/**
	 * Code de réponse
	 */
	private ResponseCode code = ResponseCode.UnknownError;
	
	/**
	 * Message
	 */
	private String message;

	/**
	 * Crée une nouvelle réponse
	 * @param aCode Code de réponse
	 * @param aMessage Message
	 */
	public Response(ResponseCode aCode, String aMessage)
	{
		super();
		
		code = aCode;
		message = aMessage;
	}

	/**
	 * Crée une nouvelle réponse
	 * @param aNumericCode Code numérique de réponse
	 * @param aMessage Message
	 */
	public Response(int aNumericCode, String aMessage)
	{
		this(ResponseCode.findByCode(aNumericCode), aMessage);
	}

	/**
	 * Crée une nouvelle réponse
	 * @param aCode Code de réponse
	 */
	public Response(ResponseCode aCode)
	{
		this(aCode, aCode.getDefaultMessage());
	}

	/**
	 * Crée une nouvelle réponse
	 * @param aNumericCode Code numérique de réponse
	 */
	public Response(int aNumericCode)
	{
		this(ResponseCode.findByCode(aNumericCode));
	}

	/**
	 * Crée une nouvelle réponse
	 * @param aNumericCode Code numérique de réponse
	 * @param aMessage Message
	 */
	public Response(String responseLine)
	{
		super();

		try
		{
			int spacePos = responseLine.indexOf(' ');

			if (spacePos < 0)
			{
				code = ResponseCode.InvalidResponseLine;
				message = ResponseCode.InvalidResponseLine.getDefaultMessage() +
					": " + responseLine;
			}
			else
			{
				code = ResponseCode.findByCode(
					Integer.parseInt(responseLine.substring(0, spacePos)));
				message = responseLine.substring(spacePos + 1);
			}
		}
		catch (NumberFormatException error)
		{
			code = ResponseCode.InvalidResponseLine;
			message = ResponseCode.InvalidResponseLine.getDefaultMessage() +
				": " + responseLine;
		}
	}

	/**
	 * Crée une nouvelle réponse
	 */
	public Response()
	{
		this(ResponseCode.UnknownError);
	}
	
	/**
	 * Code de réponse
	 * @return Code de réponse
	 */
	public ResponseCode getCode()
	{
		return code;
	}

	/**
	 * Code numérique
	 * @return Code numérique
	 * @see sjrd.tricktakinggame.network.ResponseCode#getCode()
	 */
	public int getNumericCode()
	{
		return code.getCode();
	}
	
	/**
	 * Message
	 * @return Message
	 */
	public String getMessage()
	{
		return message;
	}
	
	/**
	 * Indique si la réponse a un code de succès
	 * @return <tt>true</tt> pour un code de succès, <tt>false</tt> pour un
	 *         code d'erreur
	 * @see sjrd.tricktakinggame.network.ResponseCode#isSuccessful()
	 */
	public boolean isSuccessful()
	{
		return code.isSuccessful();
	}
	
	/**
	 * Indique si la réponse est une erreur de protocole
	 * @return <tt>true</tt> pour une erreur de protocole, <tt>false</tt> sinon
	 * @see sjrd.tricktakinggame.network.ResponseCode#isProtocol()
	 */
	public boolean isProtocol()
	{
		return code.isProtocol();
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString()
	{
		return getNumericCode() + " " + message;
	}
	
	/**
	 * Convertit la réponse en exception
	 * @return Exception, ou <tt>null</tt> si ce n'est une réponse d'erreur
	 */
	public NetworkException toException()
	{
		if (isSuccessful())
			return null;
		if (isProtocol())
			return new NetworkProtocolException(this);
		else
			return new NetworkException(this);
	}
	
	/**
	 * Déclenche une erreur si la réponse est un code d'erreur
	 * @throws NetworkException La réponse est un code d'erreur
	 */
	public void throwException() throws NetworkException
	{
		NetworkException error = toException();
		if (error != null)
			throw error;
	}
}
