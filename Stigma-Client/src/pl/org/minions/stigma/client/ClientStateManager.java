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

import pl.org.minions.stigma.client.MapLoader.MapLoadingObsrever;
import pl.org.minions.stigma.client.observers.LoginStateObserver;
import pl.org.minions.stigma.client.ui.ClientUI;
import pl.org.minions.stigma.globals.LoginState;
import pl.org.minions.utils.i18n.Translated;
import pl.org.minions.utils.logger.Log;

/**
 * Used by {@link Client} to manage {@link ClientState}
 * transitions.
 * <p>
 * Notifies {@link ClientUI} about change of client state.
 * <p>
 * Accepts calls from {@link ClientUI} about user input
 * needed to change client state.
 */
public class ClientStateManager implements
                               LoginStateObserver,
                               MapLoadingObsrever
{
    /**
     * An implementation of this interface can be provided
     * to {@link ClientStateManager} to handle all
     * {@link ClientState client states} not handled by
     * current {@link ClientUI}.
     * @see ClientUI#setClientState(ClientState)
     */
    public static interface ClientStateHandler
    {
        /**
         * Handle given client state.
         * @param state
         *            client to handle
         */
        void handleClientState(ClientState state);
    }

    @Translated
    private static String BAD_CLIENT_VERSION_MESSAGE =
            "Client version incompatible with server.";
    @Translated
    private static String LOGIN_FAILED_MESSAGE = "Authentication failed.";

    private Client client;
    private ClientUI ui;

    private ClientState state = ClientState.DISCONNECTED;

    private String disconnectionReason;

    private boolean authTrialsAvailable = true;

    private ClientStateHandler stateHandler = new AutoClientStateHandler();

    private boolean useAutoLogin;

    /**
     * Creates a new instance of {@link ClientStateManager}
     * and adds it as a login state observer.
     * @param client
     *            client to serve
     * @param ui
     *            client user interface
     */
    ClientStateManager(Client client, ClientUI ui)
    {
        this.ui = ui;
        this.client = client;

        assert this.client != null;
        assert this.ui != null;
        client.addLoginStateObserver(this);
        client.getMapLoader().addMapLoadingObsrever(this);
    }

    /** {@inheritDoc} */
    @Override
    public void loginStateChanged(LoginState newLoginState)
    {
        Log.logger.debug("New LoginState: " + newLoginState);
        switch (state)
        {
            case CONNECTING:
                switch (newLoginState)
                {
                    case CONNECTED:
                        //TODO: maybe indicate in some way
                        return;
                    case KEYS_EXCHANGED:
                        authTrialsAvailable = true;
                        setState(ClientState.AUTHENTICATION_PROMPT);
                        return;
                    case LOGIN_FAILED:
                        setDisconnectionReason(null);
                        setState(ClientState.DISCONNECTED);
                        return;
                    default:
                        break;
                }
                break;
            case AUTHENTICATION_PROCESSING:
                switch (newLoginState)
                {
                    case PASSWORD_ACCEPTED:
                        setState(ClientState.GAME_DATA_LOADING);
                        return;
                    case BAD_CLIENT_VERSION:
                        setDisconnectionReason(BAD_CLIENT_VERSION_MESSAGE);
                        setState(ClientState.DISCONNECTED);
                        return;
                    case LOGIN_FAILED:
                        authTrialsAvailable = false;
                        setDisconnectionReason(LOGIN_FAILED_MESSAGE);
                        //fall through
                    case FIRST_RETRY:
                    case SECOND_RETRY:
                        setState(ClientState.AUTHENTICATION_ERROR);
                        return;
                    default:
                        break;
                }
                break;
            case GAME_DATA_LOADING:
                if (newLoginState == LoginState.CHOOSING_ACTOR)
                {
                    setState(ClientState.ACTOR_PROMPT);
                    return;
                }
                else
                    break;
            case ACTOR_PROCESSING:
                if (newLoginState == LoginState.LOGGED_IN)
                {
                    setState(ClientState.MAP_DATA_LOADING);
                    return;
                }
                else
                    break;
            case LOGGED_OUT:
            case DISCONNECTED:
                switch (newLoginState)
                {
                    case CONNECTED: //This can happen
                        setState(ClientState.CONNECTING);
                        return;
                    case KEYS_EXCHANGED: //Ditto TODO: why
                        setState(ClientState.AUTHENTICATION_PROMPT);
                        return;
                    case LOGIN_FAILED: //This state can be set AFTER connection has been lost.
                        return;
                    default:
                        break;
                }
                break;
            default:
                break;
        }
        Log.logger.error("Unexpected LoginState: " + newLoginState
            + " while ClientState is: " + state);
    }

    /** {@inheritDoc} */
    @Override
    public void mapLoadingStateChanged()
    {
        switch (state)
        {
            case GAME_IN_PROGRESS:
                if (!client.getMapLoader().isMapReady())
                    setState(ClientState.MAP_DATA_LOADING);
                break;
            case ACTOR_PROCESSING:
            case MAP_DATA_LOADING:
                if (client.getMapLoader().isMapReady())
                    setState(ClientState.GAME_IN_PROGRESS);
                break;
            case DISCONNECTED:
            case LOGGED_OUT:
                //Map changes to null
                break;
            default:
                Log.logger.error("Unexpected map loading state change while in state: "
                    + state);
                //TODO
                break;
        }
    }

    /**
     * Attempt to connect again to server.
     * @return <code>true</code>
     */
    public boolean reconnect()
    {
        if (state == ClientState.DISCONNECTED
            || state == ClientState.LOGGED_OUT)
        {
            setState(ClientState.CONNECTING);

            return client.doReconnect();

        }
        else
            throw new IllegalStateException("Unexpected reconnect request while in state: "
                + state);
    }

    /**
     * Send authentication data to server.
     * @param username
     *            logging in player name
     * @param password
     *            player password
     */
    public void sendAuthenticationData(String username, String password)
    {
        if (state == ClientState.AUTHENTICATION_PROMPT
            || state == ClientState.AUTHENTICATION_ERROR)
        {
            client.doSendAuthData(username, password);
            setState(ClientState.AUTHENTICATION_PROCESSING);
        }
        else
            throw new IllegalStateException("Unexpected authentication data while in state: "
                + state);
    }

    /**
     * Send id of the chosen actor to server.
     * @param actorId
     *            id of the chosen actor
     */
    public void sendActorChoice(int actorId)
    {
        if (state == ClientState.ACTOR_PROMPT)
        {
            client.doSendChosenActorId(actorId);
            setState(ClientState.ACTOR_PROCESSING);
        }
        else
            throw new IllegalStateException("Unexpected actor choice when in state: "
                + state);
    }

    /**
     * Order client to log out from server.
     */
    public void logOut()
    {
        client.doLogOut();
        setState(ClientState.LOGGING_OUT);
    }

    /**
     * Server confirmed that actor logged out properly.
     */
    public void confirmLogOut()
    {
        setDisconnectionReason("Logged out");
        setState(ClientState.LOGGED_OUT);
    }

    /**
     * Connection to server has been terminated.
     */
    void connectionToServerLost()
    {
        switch (state)
        {
            case LOGGING_OUT:
                Log.logger.error("Connection to server lost before saving actor.");
                setDisconnectionReason("Disconnected before saving actor.");
                setState(ClientState.DISCONNECTED);
                break;
            case LOGGED_OUT:
            case DISCONNECTED:
                //yeah, we know already
                break;
            case AUTHENTICATION_ERROR:
                if (!authTrialsAvailable)
                    break;
                //fall through
            default:
                Log.logger.info("Disconnection for unknown reason.");
                setDisconnectionReason(null);
                setState(ClientState.DISCONNECTED);
                break;
        }
    }

    /**
     * Call when user acknowledges his error in entering
     * authentication data.
     */
    public void authErrorConfirm()
    {
        if (state == ClientState.AUTHENTICATION_ERROR)
        {
            if (authTrialsAvailable)
                setState(ClientState.AUTHENTICATION_PROMPT);
            else
                setState(ClientState.DISCONNECTED);
        }
        else
            throw new IllegalStateException("Unexpected authentication error confirmation while in state: "
                + state);
    }

    private synchronized void setState(ClientState newState)
    {
        assert newState != state;

        state = newState;

        Log.logger.debug("New ClientState: " + newState);
        if (useAutoLogin && !newState.equals(ClientState.GAME_IN_PROGRESS))
        {
            Log.logger.debug("Using autologin to handle state.");
            stateHandler.handleClientState(newState);
        }
        else if (!ui.setClientState(state))
        {
            Log.logger.warn("ClientState " + newState
                + " not handled by ClientUI implementation.");
            if (stateHandler != null)
            {
                Log.logger.info("Referring to ClientStateHandler.");
                stateHandler.handleClientState(state);
            }
            else
                Log.logger.warn("ClientStateHandler missing.");
        }
    }

    private void setDisconnectionReason(String reason)
    {
        Log.logger.debug("New disconnection reason: " + reason);
        disconnectionReason = reason;
    }

    /**
     * Sets new {@link ClientStateHandler}.
     * <p>
     * This handler will be referred to when current
     * ClientUI implementation does not handle new UIState
     * properly.
     * @param handler
     *            the UIState handler to set
     */
    public void setStateHandler(ClientStateHandler handler)
    {
        this.stateHandler = handler;
    }

    /**
     * Returns the reason of last disconnection.
     * @return disconnection reason description or
     *         <code>null</code> if the reason is unknown
     */
    public String getDisconnectionReason()
    {
        return disconnectionReason;
    }

    /**
     * Returns state of client.
     * @return state of client
     */
    public ClientState getClientState()
    {
        return state;
    }

    /**
     * Sets whether to use auto login.
     * @param autoLogin
     *            flag whether to use auto login
     */
    public void setAutoLogin(boolean autoLogin)
    {
        this.useAutoLogin = autoLogin;
    }
}
