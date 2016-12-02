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
package pl.org.minions.stigma.client.ui.swing.chat;

import java.awt.event.ActionEvent;
import java.text.MessageFormat;

import javax.swing.AbstractAction;

import pl.org.minions.stigma.chat.client.ChatTarget;
import pl.org.minions.utils.i18n.Translated;

/**
 * An implementation of {@link AbstractAction} that enables
 * selecting {@link ChatTarget chat targets} that the client
 * should listen to.
 */
class ChatTargetListenAction extends AbstractAction
{
    private static final long serialVersionUID = 1L;

    @Translated
    private static String LISTEN_TOOLTIP = "Listen to chat channel ''{0}''";

    private ChatTarget target;

    /**
     * Creates the action.
     * @param target
     *            chat target to silence
     */
    ChatTargetListenAction(ChatTarget target)
    {
        this.target = target;
        putValue(SELECTED_KEY, !target.isSilent());
        putValue(AbstractAction.SHORT_DESCRIPTION,
                 MessageFormat.format(LISTEN_TOOLTIP, target.getLabel()));
    }

    /** {@inheritDoc} */
    @Override
    public void actionPerformed(ActionEvent e)
    {
        target.setSilent(!(Boolean) getValue(SELECTED_KEY));
    }
}
