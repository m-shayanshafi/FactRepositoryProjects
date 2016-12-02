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

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.AbstractAction;
import javax.swing.ImageIcon;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JToggleButton;
import javax.swing.event.MenuEvent;
import javax.swing.event.MenuListener;

import pl.org.minions.stigma.chat.ChatType;
import pl.org.minions.stigma.chat.client.ChatProcessor;
import pl.org.minions.stigma.chat.client.ChatTarget;
import pl.org.minions.stigma.chat.client.ChatTargetChangedListener;
import pl.org.minions.stigma.databases.Resourcer;

/**
 * Menu for displaying the list of available chat targets.
 */
class ChatTargetMenu extends JMenu
{
    private class ChatTargetMenuAction extends AbstractAction implements
                                                             ChatTargetChangedListener
    {
        private static final long serialVersionUID = 1L;

        ChatTargetMenuAction()
        {
            putValue(NAME, processor.getCurrentTarget().getLabel());
        }

        /** {@inheritDoc} */
        @Override
        public void actionPerformed(ActionEvent e)
        {
            //JMenu does not fire it's action.
        }

        /** {@inheritDoc} */
        @Override
        public void chatTargetChoosen(ChatTarget target)
        {
            putValue(NAME, target.getLabel());
        }
    }

    private static final long serialVersionUID = 1L;

    private static final String LISTEN_ICON_PATH = "img/client/icons/ear.png";
    private static final String LISTEN_ICON_ROLLOVER_PATH =
            "img/client/icons/ear_rollover.png";

    private static final ImageIcon LISTEN_ICON =
            Resourcer.loadIcon(LISTEN_ICON_PATH);
    private static final ImageIcon LISTEN_ICON_ROLLOVER =
            Resourcer.loadIcon(LISTEN_ICON_ROLLOVER_PATH);
    private ChatProcessor processor;

    /**
     * Constructor.
     * @param processor
     *            chat processor that provides and controls
     *            chat targets
     */
    ChatTargetMenu(ChatProcessor processor)
    {
        this.processor = processor;

        getPopupMenu().setLayout(new GridBagLayout());

        //This does nt help: setRolloverEnabled(true);

        //Hack to circumvent strange SynthMenuUI behavior.
        addMouseListener(new MouseAdapter()
        {
            @Override
            public void mouseExited(MouseEvent e)
            {
                getModel().setArmed(false);
            }

            @Override
            public void mouseEntered(MouseEvent e)
            {
                getModel().setArmed(true);
            }
        });

        final ChatTargetMenuAction action = new ChatTargetMenuAction();

        setAction(action);
        addMenuListener(new MenuListener()
        {
            @Override
            public void menuSelected(MenuEvent e)
            {
                refill();
            }

            @Override
            public void menuDeselected(MenuEvent e)
            {
            }

            @Override
            public void menuCanceled(MenuEvent e)
            {
            }
        });

        processor.addListener(action);
    }

    /** {@inheritDoc} */
    @Override
    protected Point getPopupMenuOrigin()
    {
        return new Point(0, -getPopupMenu().getPreferredSize().height);
    }

    private void refill()
    {
        this.removeAll();
        int row = 0;
        boolean whispers = false;
        for (ChatTarget target : processor.targets())
            if (!target.isEcho())
            {
                GridBagConstraints constraints = new GridBagConstraints();

                // Inserts separators before whispers
                if (whispers != (target.getType() == ChatType.WHISPER))
                {
                    constraints.gridx = 0;
                    constraints.gridy = row;
                    constraints.gridwidth = 2;
                    constraints.fill = GridBagConstraints.BOTH;
                    getPopupMenu().add(new JPopupMenu.Separator(), constraints);
                    constraints = new GridBagConstraints();

                    whispers = !whispers;
                    ++row;
                }

                constraints.gridx = 0;
                constraints.gridy = row;

                getPopupMenu().add(new JMenuItem(new ChatTargetSelectionAction(target,
                                                                               processor)),
                                   constraints);

                constraints = new GridBagConstraints();
                constraints.gridx = 1;
                constraints.gridy = row;

                final JToggleButton listenButton =
                        new JToggleButton(new ChatTargetListenAction(target));
                listenButton.setIcon(LISTEN_ICON);
                listenButton.setRolloverEnabled(true);
                listenButton.setRolloverIcon(LISTEN_ICON_ROLLOVER);
                getPopupMenu().add(listenButton, constraints);

                ++row;
            }
    }
}
