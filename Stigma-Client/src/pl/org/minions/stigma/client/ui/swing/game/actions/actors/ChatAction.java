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
package pl.org.minions.stigma.client.ui.swing.game.actions.actors;

import java.awt.event.ActionEvent;

import pl.org.minions.stigma.chat.client.ChatProcessor;
import pl.org.minions.stigma.client.Client;
import pl.org.minions.utils.i18n.Translated;

/**
 * ActorAction implementation for changing chat whisper
 * target.
 */
public class ChatAction extends ActorAction
{
    private static final long serialVersionUID = 1L;
    @Translated
    private static String NAME = "Whisper";
    @Translated
    private static String DESCRIPTION = "Set as the whisper target";

    /**
     * Constructor.
     */
    public ChatAction()
    {
        super(NAME, DESCRIPTION);
    }

    /** {@inheritDoc} */
    @Override
    public void actionPerformed(ActionEvent e)
    {
        ChatProcessor processor = Client.globalInstance().getChatProcessor();
        processor.setCurrentTarget(processor.getWhisperTarget(getActor().getId()));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected boolean supportsNpcs()
    {
        return false;
    }

}
