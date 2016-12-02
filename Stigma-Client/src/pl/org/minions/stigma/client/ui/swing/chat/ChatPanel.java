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

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

import javax.swing.AbstractAction;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JDesktopPane;
import javax.swing.JMenuBar;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JToggleButton;
import javax.swing.KeyStroke;

import pl.org.minions.stigma.chat.client.ChatProcessor;
import pl.org.minions.stigma.chat.client.ChatTarget;
import pl.org.minions.stigma.chat.client.ChatTargetChangedListener;
import pl.org.minions.stigma.client.Client;
import pl.org.minions.stigma.databases.Resourcer;
import pl.org.minions.utils.i18n.Translated;

/**
 * Panel for sending and filtering chat.
 */
public class ChatPanel extends JPanel
{
    private static final long serialVersionUID = 1L;

    @Translated
    private static String MODE_BUTTON_TIP =
            "Activate/deactivate chat history window";
    @Translated
    private static String SEND_BUTTON_TIP = "Send";

    private static final String CHAT_ICON_PATH = "img/client/icons/chat.png";
    private static final Icon CHAT_ICON = Resourcer.loadIcon(CHAT_ICON_PATH);

    private static final String MENU_INNER_BUTTON = "MenuInnerButton";
    private static final int WIDTH = 576;
    private static final int HEIGHT = 24;

    private JMenuBar chooserBar;
    private JTextField chatField;
    private JButton sendButton;
    private JToggleButton modeButton;

    private ChatProcessor chatProcesor = Client.globalInstance()
                                               .getChatProcessor();
    private ChatTextPanel chatText;

    /**
     * Constructor.
     * @param parent
     *            chat panel parent (for displaying
     *            {@link ChatTargetChooser}.
     * @param chatText
     *            window displaying chat text, needed for
     *            changing it's mode
     */
    public ChatPanel(JDesktopPane parent, ChatTextPanel chatText)
    {
        super();
        this.chatText = chatText;

        initialize();
        updateChosenTarget(chatProcesor.getCurrentTarget());

        chatProcesor.addListener(new ChatTargetChangedListener()
        {
            @Override
            public void chatTargetChoosen(ChatTarget target)
            {
                updateChosenTarget(target);
            }
        });
    }

    private void updateChosenTarget(ChatTarget target)
    {
        //        chooseButton.setText(target.getLabel());
        chatField.requestFocus();
    }

    private void initialize()
    {
        this.setLayout(null);
        this.setSize(new Dimension(WIDTH, HEIGHT));

        this.add(getChooserBar(), null);
        this.add(getChatField(), null);
        this.add(getSendButton(), null);
        this.add(getModeButton(), null);
    }

    private JMenuBar getChooserBar()
    {
        if (chooserBar == null)
        {
            // CHECKSTYLE:OFF
            chooserBar = new JMenuBar();
            chooserBar.setLayout(new BorderLayout());
            chooserBar.setBounds(new Rectangle(0, 0, 80, HEIGHT));
            chooserBar.add(new ChatTargetMenu(chatProcesor));
            // CHECKSTYLE:ON
        }
        return chooserBar;
    }

    private JTextField getChatField()
    {
        if (chatField == null)
        {
            chatField = new JTextField();
            chatField.setBounds(new Rectangle(getChooserBar().getWidth(),
                                              0,
                                              WIDTH - 2 * HEIGHT
                                                  - getChooserBar().getWidth(),
                                              HEIGHT));

            String enterAction = "enter";
            chatField.getInputMap(WHEN_IN_FOCUSED_WINDOW)
                     .put(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0),
                          enterAction);
            chatField.getActionMap().put(enterAction, new AbstractAction()
            {
                private static final long serialVersionUID = 1L;

                @Override
                public void actionPerformed(ActionEvent e)
                {
                    sendPressed();
                }

            });
        }
        return chatField;
    }

    private JButton getSendButton()
    {
        if (sendButton == null)
        {
            sendButton = new JButton();
            sendButton.setName(MENU_INNER_BUTTON);
            sendButton.setBounds(new Rectangle(WIDTH - 2 * HEIGHT,
                                               0,
                                               HEIGHT,
                                               HEIGHT));
            sendButton.setToolTipText(SEND_BUTTON_TIP);
            sendButton.setIcon(CHAT_ICON);
            sendButton.addActionListener(new ActionListener()
            {
                @Override
                public void actionPerformed(ActionEvent e)
                {
                    sendPressed();
                }
            });
        }
        return sendButton;
    }

    private JToggleButton getModeButton()
    {
        if (modeButton == null)
        {
            modeButton = new JToggleButton();
            modeButton.setName(MENU_INNER_BUTTON);
            modeButton.setBounds(new Rectangle(WIDTH - HEIGHT,
                                               0,
                                               HEIGHT,
                                               HEIGHT));
            modeButton.setToolTipText(MODE_BUTTON_TIP);
            modeButton.addActionListener(new ActionListener()
            {
                @Override
                public void actionPerformed(ActionEvent e)
                {
                    chatText.setInteractiveMode(modeButton.getModel()
                                                          .isSelected());
                }
            });
        }
        return modeButton;
    }

    private void sendPressed()
    {
        if (!chatField.getText().isEmpty())
        {
            Client.globalInstance()
                  .sendChat(chatProcesor.create(chatField.getText()));
            chatField.setText("");
            chatField.requestFocus();
        }
    }
} //  @jve:decl-index=0:visual-constraint="10,10"
