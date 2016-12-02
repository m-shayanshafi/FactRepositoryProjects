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
import java.awt.event.KeyEvent;

import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.SwingConstants;
import javax.swing.border.EtchedBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import pl.org.minions.stigma.client.Client;
import pl.org.minions.stigma.license.LicenseInfoPanel;
import pl.org.minions.utils.i18n.StandardText;
import pl.org.minions.utils.i18n.Translated;

/**
 * Panel used to log actor in.
 */
public class LoginPanel extends JPanel
{
    @Translated
    private static String NAME = "Login";
    @Translated
    private static String WELCOME_STATUS_LABEL = "Welcome to Stigma!";
    @Translated
    private static String AUTHENTICATION_FAILED_STATUS_LABEL =
            "Authentication failed!";
    @Translated
    private static String LOGIN_LABEL = "Login:";
    @Translated
    private static String PASSWORD_LABEL = "Password:";

    private static final int PANEL_X = 250;
    private static final int PANEL_Y = 200;
    private static final int PANEL_HEIGHT = 200;
    private static final int PANEL_WIDTH = 300;

    private static final long serialVersionUID = 1L;

    private JLabel connectionStatusLabel;
    private JLabel loginLabel;
    private JLabel passwordLabel;
    private JTextField loginTextField;
    private JPasswordField passwordField;
    private JButton loginButton;
    private JButton aboutButton;

    /**
     * This is the default constructor.
     */
    public LoginPanel()
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
        this.setMinimumSize(new Dimension(PANEL_WIDTH, PANEL_HEIGHT));
        this.setSize(new Dimension(PANEL_WIDTH, PANEL_HEIGHT));
        this.setPreferredSize(new Dimension(PANEL_WIDTH, PANEL_HEIGHT));
        this.setLocation(PANEL_X, PANEL_Y);
        this.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED));
        this.setLayout(null);

        connectionStatusLabel = new JLabel();
        connectionStatusLabel.setText("status");
        connectionStatusLabel.setBounds(new Rectangle(45, 15, 211, 16));
        connectionStatusLabel.setBorder(BorderFactory.createEmptyBorder());

        loginLabel = new JLabel();
        loginLabel.setBounds(new Rectangle(45, 45, 211, 16));
        loginLabel.setText(LOGIN_LABEL);
        loginLabel.setBorder(BorderFactory.createEmptyBorder());

        loginTextField = new JTextField();
        loginTextField.setBounds(new Rectangle(45, 75, 211, 20));
        loginTextField.getDocument().addDocumentListener(new DocumentListener()
        {
            @Override
            public void removeUpdate(DocumentEvent e)
            {
                fieldsModifiedActionPerformed();
            }

            @Override
            public void insertUpdate(DocumentEvent e)
            {
                fieldsModifiedActionPerformed();
            }

            @Override
            public void changedUpdate(DocumentEvent e)
            {
                fieldsModifiedActionPerformed();
            }
        });

        passwordLabel = new JLabel();
        passwordLabel.setBounds(new Rectangle(45, 105, 211, 16));
        passwordLabel.setText(PASSWORD_LABEL);
        passwordLabel.setBorder(BorderFactory.createEmptyBorder());

        passwordField = new JPasswordField();
        passwordField.setBounds(new Rectangle(45, 135, 211, 20));
        passwordField.getDocument().addDocumentListener(new DocumentListener()
        {
            @Override
            public void removeUpdate(DocumentEvent e)
            {
                fieldsModifiedActionPerformed();
            }

            @Override
            public void insertUpdate(DocumentEvent e)
            {
                fieldsModifiedActionPerformed();
            }

            @Override
            public void changedUpdate(DocumentEvent e)
            {
                fieldsModifiedActionPerformed();
            }
        });

        loginButton = new JButton();
        loginButton.setText(StandardText.OK.get());
        loginButton.setBounds(new Rectangle(195, 165, 61, 16));
        loginButton.setHorizontalAlignment(SwingConstants.CENTER);
        loginButton.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                okButtonPressedActionPerformed();
            }
        });
        loginButton.setEnabled(false);

        this.getInputMap(WHEN_IN_FOCUSED_WINDOW)
            .put(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0), "enter");
        this.getActionMap().put("enter", new AbstractAction()
        {
            /**
             * Default UID.
             */
            private static final long serialVersionUID = 1L;

            @Override
            public void actionPerformed(ActionEvent e)
            {
                if (loginButton.isEnabled())
                {
                    okButtonPressedActionPerformed();
                }
            }
        });

        aboutButton = new JButton();
        aboutButton.setText(StandardText.ABOUT.get());
        aboutButton.setBounds(new Rectangle(45, 165, 74, 16));
        aboutButton.setHorizontalAlignment(SwingConstants.CENTER);
        aboutButton.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                LicenseInfoPanel.showFrame();
            }
        });

        this.add(connectionStatusLabel, null);
        this.add(loginLabel, null);
        this.add(loginTextField, null);
        this.add(passwordLabel, null);
        this.add(passwordField, null);
        this.add(aboutButton, null);
        this.add(loginButton, null);
        //CHECKSTYLE:ON

        //TODO remove that after tests
        loginTextField.setText("test");
        passwordField.setText("qwe");
    }

    private void fieldsModifiedActionPerformed()
    {
        if (loginTextField.getText() != null
            && !loginTextField.getText().isEmpty()
            && passwordField.getPassword() != null
            && passwordField.getPassword().length > 0)
            loginButton.setEnabled(true);
        else
            loginButton.setEnabled(false);
    }

    /**
     * Sets text on connection status label.
     * @param status
     *            status
     */
    public void setStatus(String status)
    {
        connectionStatusLabel.setText(status);
    }

    /** {@inheritDoc} */
    @Override
    public void requestFocus()
    {
        super.requestFocus();
        loginTextField.requestFocus();
    }

    private void okButtonPressedActionPerformed()
    {
        Client.globalInstance()
              .sendAuthData(loginTextField.getText(),
                            new String(passwordField.getPassword()));
        loginTextField.setText("");
        passwordField.setText("");
    }

    /**
     * Displays Welcome status.
     */
    public void setWelcomeStatus()
    {
        setStatus(WELCOME_STATUS_LABEL);
    }

    /**
     * Displays Authentication Failed status.
     */
    public void setAuthenticationFailedStatus()
    {
        setStatus(AUTHENTICATION_FAILED_STATUS_LABEL);
    }

    /**
     * Returns default panel name.
     * @return panel name
     */
    public static String getDefaultName()
    {
        return NAME;
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
