/*
 * TrickTakingGame - Trick-taking games platform on-line
 * Copyright (C) 2008  Sébastien Doeraene
 * All Rights Reserved
 *
 * This file is part of TrickTakingGame.
 *
 * TrickTakingGame is free software: you can redistribute it and/or modify it
 * under the terms of the GNU General Public License as published by the Free
 * Software Foundation, either version 3 of the License, or (at your option) any
 * later version.
 *
 * TrickTakingGame is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for
 * more details.
 *
 * You should have received a copy of the GNU General Public License along with
 * TrickTakingGame.  If not, see <http://www.gnu.org/licenses/>.
 */
package sjrd.tricktakinggame.gui.connection;

import java.awt.*;

import javax.swing.*;

import sjrd.swing.*;

/**
 * Fenêtre de connexion
 * @author sjrd
 */
public class ConnectionFrame extends JFrame
{
	/**
	 * ID de sérialisation
	 */
	private static final long serialVersionUID = 1;
	
	/**
	 * Champ hôte
	 */
	JTextField hostField;
	
	/**
	 * Champ login
	 */
	JTextField loginNameField;
	
	/**
	 * Champ mot de passe
	 */
	JTextField passwordField;
	
	/**
	 * Action de connexion
	 */
	private ConnectAction connectAction;
	
	/**
	 * Bouton de connexion
	 */
	private JButton connectButton;
	
	/**
	 * Crée une nouvelle fenêtre
	 */
	public ConnectionFrame()
	{
		super();
		
		build();

		setVisible(true);
	}
	
	/**
	 * Construit la fenêtre
	 */
	private void build()
	{
		setTitle("Connexion");
		setLocationRelativeTo(null);
		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		setLayout(new CenterLayout());

		hostField = new JTextField("tricktakinggame.game-host.org", 100);
		hostField.setMaximumSize(new Dimension(250, 25));
		loginNameField = new JTextField(100);
		loginNameField.setMaximumSize(new Dimension(250, 25));
		passwordField = new JPasswordField(100);
		passwordField.setMaximumSize(new Dimension(250, 25));
		
		Box innerBox = new Box(BoxLayout.Y_AXIS);

		innerBox.add(new JLabel("Serveur hôte :"));
		innerBox.add(hostField);
		innerBox.add(new JLabel("Pseudonyme :"));
		innerBox.add(loginNameField);
		innerBox.add(new JLabel("Mot de passe :"));
		innerBox.add(passwordField);
		
		innerBox.add(Box.createVerticalStrut(20));
		
		connectAction = new ConnectAction(this, "Connexion");
		connectButton = new JButton(connectAction);
		connectButton.setMaximumSize(new Dimension(200, 30));
		innerBox.add(connectButton);
		getRootPane().setDefaultButton(connectButton);
		
		innerBox.setPreferredSize(innerBox.getMinimumSize());
		add(innerBox);
		
		setPreferredSize(new Dimension(200, 250));
		setSize(getPreferredSize());
	}
}
