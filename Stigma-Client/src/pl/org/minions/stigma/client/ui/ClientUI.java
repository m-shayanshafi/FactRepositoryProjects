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
package pl.org.minions.stigma.client.ui;

import pl.org.minions.stigma.client.Client;
import pl.org.minions.stigma.client.ClientState;
import pl.org.minions.stigma.client.ui.event.UiEventDispatcher;
import pl.org.minions.stigma.client.ui.event.UiEventRegistry;
import pl.org.minions.utils.ui.UI;

/**
 * Interface that should be implemented by any user
 * interface class to be used by the game {@link Client}.
 */
public interface ClientUI extends UI
{
    /**
     * Notifies UI that it should represent a new client
     * state.
     * @param state
     *            new state to set
     * @return <code>true</code> if the state is fully
     *         implemented in this ClientUI implementation,
     *         <code>false</code> if {@link Client} should
     *         not expect user input usually provided by the
     *         chosen state.
     */
    boolean setClientState(ClientState state);

    /**
     * Creates dispatcher for events that may be used to
     * connect UI parts with client. 'Events' are like
     * 'player actor walked'. They will be distributed using
     * set of listeners (see {@link UiEventRegistry}). When
     * UI does not want to use events this method should
     * return {@code null}. Dispatcher may differ between UI
     * implementation in a way events are dispatched - in
     * what thread etc.
     * @return new dispatcher for events or {@code null}
     *         when UI does not want to use events
     */
    UiEventDispatcher createDispatcher();

    /**
     * Returns implementation of {@link AreaView} provided
     * by this ClientUI. May return <code>null</code> if
     * AreaView is not ready or none is provided by this
     * ClientUI.
     * @return AreaView implementation, or <code>null</code>
     */
    AreaView getAreaView();

    /**
     * Method which should be called after setting client UI
     * to initialize it.
     */
    void initialize();
}
