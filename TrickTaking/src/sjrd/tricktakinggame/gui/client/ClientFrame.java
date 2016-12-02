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
package sjrd.tricktakinggame.gui.client;

import javax.swing.*;

import sjrd.tricktakinggame.network.client.*;

/**
 * Fenêtre principale du client
 * @author sjrd
 */
public class ClientFrame extends JFrame
{
	/**
	 * ID de sérialisation
	 */
	private static final long serialVersionUID = 1;
	
	/**
	 * Panel maître
	 */
	private ClientPanel mainPanel;
	
	/**
	 * Crée une nouvelle fenêtre
	 */
	public ClientFrame(Client client)
	{
		super();
		
		mainPanel = new ClientPanel(client);
		build();

		setVisible(true);
	}
	
	/**
	 * Construit la fenêtre
	 */
	private void build()
	{
		setTitle("Jeux de cartes à plis");
		setSize(mainPanel.getPreferredSize());
		setLocationRelativeTo(null);
		setResizable(true);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setContentPane(mainPanel);
	}
}
