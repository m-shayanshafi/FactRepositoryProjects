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

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.JToggleButton;

import pl.org.minions.stigma.chat.client.ChatProcessor;
import pl.org.minions.stigma.chat.client.ChatTarget;
import pl.org.minions.stigma.databases.Resourcer;

/**
 * Component displaying one chat target. Allows to choose
 * that target as default and to change it "silent"
 * property.
 */
class ChatTargetComponent extends JPanel
{
    private static final long serialVersionUID = 1L;

    private static final String LISTEN_ICON_PATH = "img/client/icons/ear.png";
    private static final String LISTEN_ICON_ROLLOVER_PATH =
            "img/client/icons/ear_rollover.png";

    private static final ImageIcon LISTEN_ICON =
            Resourcer.loadIcon(LISTEN_ICON_PATH);
    private static final ImageIcon LISTEN_ICON_ROLLOVER =
            Resourcer.loadIcon(LISTEN_ICON_ROLLOVER_PATH);

    private ChatTarget target;
    private JToggleButton chooseButton;
    private JToggleButton listenButton;
    private ChatProcessor processor;

    /**
     * Constructor.
     * @param target
     *            target to be displayed by this component
     * @param processor
     *            chat processor (for setting current target
     */
    public ChatTargetComponent(ChatTarget target, ChatProcessor processor)
    {
        this.target = target;
        this.processor = processor;
        initialize();
    }

    private void initialize()
    {
        this.setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
        this.add(getChooseButton(), null);
        this.add(Box.createHorizontalGlue(), null);
        this.add(getListenButton(), null);
        this.setSize(this.getPreferredSize());
    }

    /**
     * This method initializes chooseButton
     * @return
     */
    private JToggleButton getChooseButton()
    {
        if (chooseButton == null)
        {
            chooseButton =
                    new JToggleButton(new ChatTargetSelectionAction(target,
                                                                    processor));
        }
        return chooseButton;
    }

    /**
     * This method initializes listenButton
     * @return
     */
    private JToggleButton getListenButton()
    {
        if (listenButton == null)
        {
            listenButton =
                    new JToggleButton(new ChatTargetListenAction(target));
            listenButton.setIcon(LISTEN_ICON);
            listenButton.setRolloverEnabled(true);
            listenButton.setRolloverIcon(LISTEN_ICON_ROLLOVER);
        }
        return listenButton;
    }

} //  @jve:decl-index=0:visual-constraint="10,10"
