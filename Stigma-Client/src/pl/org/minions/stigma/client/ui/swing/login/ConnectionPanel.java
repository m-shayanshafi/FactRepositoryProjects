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
package pl.org.minions.stigma.client.ui.swing.login;

import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTextPane;

import pl.org.minions.stigma.client.Client;
import pl.org.minions.utils.i18n.Translated;

/**
 * Panel that enables user to request the client to connect
 * to server if connection was lost or never established.
 */
public class ConnectionPanel extends JPanel
{
    /**
     * Reason of presenting ConnectionPanel.
     */
    public enum ConnectionPanelReason
    {
        LOGGED_OUT, CONNECTION_LOST
    }

    private static final long serialVersionUID = 1L;

    @Translated
    private static String FAILED_TITLE = "Connection Failed!";
    @Translated
    private static String LOGGED_OUT_TITLE = "Logged out";
    @Translated
    private static String RECONNECT_BUTTON = "RECONNECT"; //  @jve:decl-index=0:
    @Translated
    private static String FAILED_LABEL =
            "Unable to establish connection or disconnected."; //  @jve:decl-index=0:
    @Translated
    private static String LOGGED_OUT_LABEL = "Logged out from server.";
    @Translated
    private static String WRONG_REASON_LABEL =
            "Unknown conncetion lost reason.";

    private static final int PANEL_X = 250;
    private static final int PANEL_Y = 200;
    private static final int PANEL_HEIGHT = 200;
    private static final int PANEL_WIDTH = 300;

    private JButton reconectButton;
    private JTextPane reconnectLabel;

    /**
     * This is the default constructor.
     */
    public ConnectionPanel()
    {
        super();
        initialize();
    }

    /**
     * This method initializes this.
     */
    private void initialize()
    {
        //CHECKSTYLE:OFF
        this.setSize(PANEL_WIDTH, PANEL_HEIGHT);
        this.setPreferredSize(new Dimension(PANEL_WIDTH, PANEL_HEIGHT));
        this.setLayout(null);
        this.setLocation(PANEL_X, PANEL_Y);

        reconnectLabel = new JTextPane();
        reconnectLabel.setBackground(this.getBackground());
        reconnectLabel.setContentType("text/html");

        reconnectLabel.setText("<center>" + WRONG_REASON_LABEL + "</center>");

        reconnectLabel.setEditable(false);
        reconnectLabel.setBounds(new Rectangle(45, 45, 216, 61));
        reconnectLabel.setBorder(BorderFactory.createEmptyBorder());
        reconnectLabel.setFocusable(false);
        reconnectLabel.setOpaque(false);

        reconectButton = new JButton();
        reconectButton.setBounds(new Rectangle(45, 120, 216, 31));
        reconectButton.setText(RECONNECT_BUTTON);
        reconectButton.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                Client.globalInstance().reconnect();
            }
        });

        this.add(reconnectLabel, null);
        this.add(reconectButton, null);
        //CHECKSTYLE:ON
    }

    /**
     * Returns default panel name.
     * @return panel name
     */
    public static String getDefaultName()
    {
        return FAILED_TITLE;
    }

    /**
     * Sets text on panel with for panel showing reason.
     * @param reason
     *            reason of showing the panel
     */
    public void setReason(ConnectionPanelReason reason)
    {
        switch (reason)
        {
            case CONNECTION_LOST:
                reconnectLabel.setText("<center>" + FAILED_LABEL + "</center>");
                break;
            case LOGGED_OUT:
                reconnectLabel.setText("<center>" + LOGGED_OUT_LABEL
                    + "</center>");
                break;
            default:
                reconnectLabel.setText("<center>" + WRONG_REASON_LABEL
                    + "</center>");
        }
    }

    /**
     * Returns title for selected reason.
     * @param reason
     *            reason of showing panel
     * @return string representing title for selected reason
     */
    public static String getTitle(ConnectionPanelReason reason)
    {
        switch (reason)
        {
            case CONNECTION_LOST:
                return FAILED_TITLE;
            case LOGGED_OUT:
                return LOGGED_OUT_TITLE;
            default:
        }
        return FAILED_TITLE;
    }

    /**
     * Returns default panel height.
     * @return panel height
     */
    public static int getDefaultHeight()
    {
        return PANEL_HEIGHT;
    }

    /**
     * Returns default panel width.
     * @return panel width
     */
    public static int getDefaultWidth()
    {
        return PANEL_WIDTH;
    }

    /**
     * Returns default panel position X coordinate.
     * @return X coordinate
     */
    public static int getDefaultPositionX()
    {
        return PANEL_X;
    }

    /**
     * Returns default panel position Y coordinate.
     * @return X coordinate
     */
    public static int getDefaultPositionY()
    {
        return PANEL_Y;
    }
}
