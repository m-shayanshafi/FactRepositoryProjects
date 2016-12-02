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

import pl.org.minions.stigma.chat.client.ChatProcessor;
import pl.org.minions.stigma.chat.client.ChatTarget;
import pl.org.minions.stigma.chat.client.ChatTargetChangedListener;
import pl.org.minions.utils.i18n.Translated;

/**
 * An implementation of {@link AbstractAction} that enables
 * selecting a {@link ChatTarget chat target} that the
 * client
 * should speak to.
 */
class ChatTargetSelectionAction extends AbstractAction implements
                                                      ChatTargetChangedListener
{
    private static final long serialVersionUID = 1L;

    @Translated
    private static String SPEAK_TOOLTIP = "Speak to chat channel ''{0}''";

    private ChatTarget target;
    private ChatProcessor processor;

    /**
     * Creates an action.
     * @param target
     *            affected chat target
     * @param processor
     *            chat processor
     */
    ChatTargetSelectionAction(ChatTarget target, ChatProcessor processor)
    {
        this.target = target;
        this.processor = processor;

        putValue(NAME, target.getLabel());
        putValue(SELECTED_KEY, target == processor.getCurrentTarget());
        putValue(SHORT_DESCRIPTION,
                 MessageFormat.format(SPEAK_TOOLTIP, target.getLabel()));

        processor.addListener(this);
    }

    /** {@inheritDoc} */
    @Override
    public void actionPerformed(ActionEvent e)
    {
        processor.setCurrentTarget(target.getIdx());
    }

    /** {@inheritDoc} */
    @Override
    public void chatTargetChoosen(ChatTarget target)
    {
        putValue(SELECTED_KEY, target == this.target);
    }
}
