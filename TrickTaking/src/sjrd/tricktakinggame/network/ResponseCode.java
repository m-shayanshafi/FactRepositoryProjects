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
 * Code de réponse (de succès ou d'erreur)
 * @author sjrd
 */
public enum ResponseCode
{
	UnknownError(100, "Unknown error"),
	CardGameFatalError(101, "Card game fatal error"),
	Pong(200, "Pong", true),
	OK(201, "OK", true),
	FirstConnectionOK(202, "Connected, here is your session code", true),
	SecondConnectionOK(203, "Correct login/session-code", true),
	AdminConnectionOK(298, "Administration connection OK", true),
	LoggerConnectionOK(299, "Logger connection OK", true),
	StatusNoTable(204, "No table", true),
	StatusWaitingForPlayers(205, "Waiting for players", true),
	StatusNotPlaying(206, "Not playing", true),
	StatusAuctioning(207, "Auctioning", true),
	StatusPlaying(208, "Playing", true),
	Forbidden(300, "Forbidden"),
	UnknownRulesProviderID(301, "Unknown rules provider ID"),
	BadPlayerCount(302, "Bad player count"),
	TableIsFull(303, "Table was already full"),
	Protocol(400, "Protocol error", false, true),
	InvalidResponseLine(401, "Invalid response line", false, true),
	InvalidLogin(402, "Invalid login or password"),
	InvalidSessionCode(403, "Invalid session code"),
	UserAlreadyLogged(404, "User already logged"),
    InvalidAdmin(405, "Invalid admin");
	
	/**
	 * Code numérique
	 */
	private int code;
	
	/**
	 * Message par défaut
	 */
	private String defaultMessage;
	
	/**
	 * Indique si le code de réponse est un code de succès
	 */
	private boolean successful = false;
	
	/**
	 * Indique si le code de réponse est une erreur de protocole
	 */
	private boolean protocol = false;
	
	/**
	 * Initialise un code d'erreur
	 */
	ResponseCode(int aCode, String aDefaultMessage, boolean aSuccessful,
		boolean aProtocol)
	{
		assert !aSuccessful || !aProtocol;
		
		code = aCode;
		defaultMessage = aDefaultMessage;
		successful = aSuccessful;
		protocol = aProtocol;
	}
	
	/**
	 * Initialise un code d'erreur
	 */
	ResponseCode(int aCode, String aDefaultMessage, boolean aSuccessful)
	{
		this(aCode, aDefaultMessage, aSuccessful, false);
	}
	
	/**
	 * Initialise un code d'erreur
	 */
	ResponseCode(int aCode, String aDefaultMessage)
	{
		this(aCode, aDefaultMessage, false);
	}
	
	/**
	 * Code numérie
	 * @return Code numérique
	 */
	public int getCode()
	{
		return code;
	}
	
	/**
	 * Message par défaut
	 * @return Message par défaut
	 */
	public String getDefaultMessage()
	{
		return defaultMessage;
	}
	
	/**
	 * Indique si le code de réponse est un code de succès
	 * @return <tt>true</tt> pour un code de succès, <tt>false</tt> pour un
	 *         code d'erreur
	 */
	public boolean isSuccessful()
	{
		return successful;
	}
	
	/**
	 * Indique si le code de réponse est une erreur de protocole
	 * @return <tt>true</tt> pour une erreur de protocole, <tt>false</tt> sinon
	 */
	public boolean isProtocol()
	{
		return protocol;
	}
	
	/**
	 * Identifie un code d'erreur d'après son code numérie
	 * @param code Code numérique de l'erreur
	 * @return Code d'erreur correspondant, ou <tt>Protocol</tt> si non trouvé
	 */
	public static ResponseCode findByCode(int code)
	{
		for (ResponseCode responseCode: values())
			if (responseCode.getCode() == code)
				return responseCode;
		
		return Protocol;
	}
}
