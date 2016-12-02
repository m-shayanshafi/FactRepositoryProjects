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
package sjrd.tricktakinggame.client;

import sjrd.tricktakinggame.network.*;

import static sjrd.tricktakinggame.network.ResponseCode.*;

/**
 * Type de statut
 * @author sjrd
 */
public enum ClientStatusKind
{
	Disconnected(UnknownError), NoTable(StatusNoTable),
	WaitingForPlayers(StatusWaitingForPlayers), NotPlaying(StatusNotPlaying),
	Auctioning(StatusAuctioning), Playing(StatusPlaying);
	
	/**
	 * Code de réponse correspondant
	 */
	private ResponseCode responseCode;

	/**
	 * Crée le type de statut
	 * @param code Code de réponse correspondant
	 */
	ClientStatusKind(ResponseCode code)
	{
		responseCode = code;
	}
	
	/**
	 * Code de réponse correspondant
	 * @return Code de réponse correspondant
	 */
	public ResponseCode getResponseCode()
	{
		return responseCode;
	}
	
	/**
	 * Trouve un type de statut à partir du code de réponse correspondant
	 * @param code Code de réponse
	 * @return Statut correspondant, ou <tt>NoTable</tt> si non trouvé
	 */
	public static ClientStatusKind findStatusByResponseCode(ResponseCode code)
	{
		for (ClientStatusKind statusKind: values())
			if (statusKind.responseCode == code)
				return statusKind;
		
		return NoTable;
	}
}