/**
 *   Stigma - Multiplayer online RPG - http://stigma.sourceforge.net
 *   Copyright (C) 2005-2009 Minions Studio
 *
 *   This program is free software: you can redistribute it and/or modify
 *   it under the terms of the GNU General Public License as published by
 *   the Free Software Foundation, either version 3 of the License, or
 *   (at your option) any later version.
 *
 *   This program is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *   GNU General Public License for more details.
 *
 *   You should have received a copy of the GNU General Public License
 *   along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *   
 */
package pl.org.minions.stigma.client;

/**
 * Represents various states of client user interface.
 */
public enum ClientState
{
    /**
     * Client is not connected to server. This is either
     * initial state, or because connection failed for some
     * reason.
     * <p>
     * Option to manually try establishing connection should
     * be available, or an automatic timed connection
     * attempt.
     * @see ClientStateManager#reconnect()
     */
    DISCONNECTED,
    /**
     * Client attempts to connect to server.
     */
    CONNECTING,
    /**
     * Client is connected to server. The user should
     * provide user name and password in order to proceed.
     * <p>
     * At this point there should be option to present
     * license information.
     * @see ClientStateManager#sendAuthenticationData(String,
     *      String)
     */
    AUTHENTICATION_PROMPT,
    /**
     * User name and password have been sent to server.
     * <p>
     * Awaiting server reply.
     */
    AUTHENTICATION_PROCESSING,
    /**
     * Either user name or password provided was invalid.
     * <p>
     * Option to try again should be present.
     * @see ClientStateManager#authErrorConfirm()
     */
    AUTHENTICATION_ERROR,
    /**
     * Server accepted user name and password. Some
     * necessary game data is being downloaded.
     * <p>
     * UI implementations are encouraged display loading
     * progress.
     */
    GAME_DATA_LOADING,
    /**
     * Authenticated user needs to select one of his
     * available actors.
     * <p>
     * The list of available actors should be refreshed
     * either manually or automatically by UI
     * implementation.
     * @see ClientStateManager#sendActorChoice(int)
     */
    ACTOR_PROMPT,
    /**
     * Actor choice has been sent to server. Awaiting server
     * reply.
     */
    ACTOR_PROCESSING,
    /**
     * Map data is being loaded.
     */
    MAP_DATA_LOADING,
    /**
     * Main game screen. An authenticated user plays using
     * chosen actor on a loaded map.
     */
    GAME_IN_PROGRESS,
    /**
     * Actor starts to log out but he is not disconnected
     * yet.
     */
    LOGGING_OUT,
    /**
     * Actor is properly logged out.
     */
    LOGGED_OUT,
}
