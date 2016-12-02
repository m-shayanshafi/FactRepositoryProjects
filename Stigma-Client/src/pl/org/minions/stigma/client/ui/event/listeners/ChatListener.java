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
package pl.org.minions.stigma.client.ui.event.listeners;

import pl.org.minions.stigma.chat.Chat;
import pl.org.minions.stigma.chat.client.ProcessedChatListener;
import pl.org.minions.stigma.client.ui.event.UiEventListener;

/**
 * Interface for listeners of incoming chat messages.
 * Informs about all messages (in contrary to
 * {@link ProcessedChatListener}.
 */
public interface ChatListener extends UiEventListener
{
    /**
     * Called when chat is received.
     * @param chat
     *            received chat message
     */
    void chatReceived(Chat chat);
}
