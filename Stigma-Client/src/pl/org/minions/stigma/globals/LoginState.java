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
package pl.org.minions.stigma.globals;

/**
 * States representing login automata between client and
 * server. Available states transition are listed below.
 * 
 * <pre>
 * 0. INIT
 *      S: waits for new connection
 *      C: connects to server
 * 1. CONNECTED
 *      S: sends keys
 *      C: waits for keys
 * 2. KEYS_EXCHANGED
 *      S: waits for login/password
 *      C: sends encrypted login/password
 *      IF server receives proper login data GOTO 5
 *      ELSE IF bad client version GOTO 9
 *      ELSE GOTO 3
 * 3. FIRST_RETRY
 *      S: waits for login/password
 *      C: sends encrypted login/password
 *      IF server receives proper login data GOTO 5
 *      ELSE IF bad client version GOTO 9
 *      ELSE GOTO 4
 * 4. SECOND_RETRY
 *      S: waits for login/password
 *      C: send encrypted login/password
 *      IF server receives proper login data GOTO 5
 *      ELSE IF bad client version GOTO 9
 *      ELSE GOTO 8
 * 5. PASSWORD_ACCEPTED
 *      S: waits for 'ready' signal from client
 *      C: loads some world data, informs server when ready
 * 6. CHOOSING_ACTOR
 *      S: waits for client to choose actor
 *      C: chooses actor
 *      IF chosen actor is on available actor list GOTO 7
 *      ELSE GOTO 8
 * 7. LOGGED_IN
 *      login procedure succeeded
 *      END
 * 8. LOGIN_FAILED
 *      login procedure failed
 *      END
 * 9. BAD_CLIENT_VERSION
 *      login procedure failed
 *      END
 * </pre>
 * 
 * Any unexpected message etc leads to LOGIN_FAILED state.
 */
public enum LoginState
{
    /**
     * Login procedure was unsuccessful because of data send
     * by client. It's final state.
     */
    LOGIN_FAILED,
    /**
     * Login procedure was unsuccessful because of bad
     * client version. It's final state.
     */
    BAD_CLIENT_VERSION,

    /**
     * Client properly connected to server. It's initial
     * state.
     */
    CONNECTED,
    /** Server accepted connection and sent public key. */
    KEYS_EXCHANGED,
    /** Client authorization failed for the first time. */
    FIRST_RETRY,
    /**
     * Client authorization failed for the second time.
     * Third failure is final.
     */
    SECOND_RETRY,
    /**
     * Client authorization succeed. Server waits for
     * information from client that client is ready.
     */
    PASSWORD_ACCEPTED,
    /**
     * Client is ready, server sends available actors list,
     * user has time to choose actor.
     */
    CHOOSING_ACTOR,
    /**
     * Actor successfully chosen, whole procedure succeed.
     * It's final state.
     */
    LOGGED_IN,
}
