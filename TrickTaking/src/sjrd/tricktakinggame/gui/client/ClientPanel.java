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

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;

import sjrd.tricktakinggame.client.*;
import sjrd.tricktakinggame.gui.connection.*;
import sjrd.tricktakinggame.gui.tablelist.*;
import sjrd.tricktakinggame.gui.util.*;
import sjrd.tricktakinggame.gui.gameplaying.*;
import sjrd.tricktakinggame.network.client.*;

/**
 * Panel maître du client
 * @author sjrd
 */
public class ClientPanel extends JPanel
{
	/**
	 * ID de sérialisation
	 */
	private static final long serialVersionUID = 1;
	
	/**
	 * Délai de mise à jour du statut
	 */
	private static final int UPDATE_STATUS_DELAY = 500;
	
	/**
	 * Client connecté
	 */
	private Client client;
	
	/**
	 * Panel à card-layout
	 */
	private JPanel cardPanel;
	
	/**
	 * Layout
	 */
	private CardLayout layout;
	
	/**
	 * Panel courant
	 */
	private ClientSubPanel currentPanel;
	
	/**
	 * Panel de liste des tables
	 */
	private TableListPanel tableListPanel;
	
	/**
	 * Panel de jeu
	 */
	private GamePlayingPanel gamePlayingPanel;
	
	/**
	 * Panel d'affichage des messages
	 */
	private MessagesPanel messagesPanel;
	
	/**
	 * Timer de mise à jour du statut du client
	 */
	private Timer updateStatusTimer;
	
	/**
	 * Crée le panel
	 */
	public ClientPanel(Client aClient)
	{
		super();
		
		client = aClient;
		
		build();
	}
	
	/**
	 * Construit le panel
	 */
	private void build()
	{
		setLayout(new BorderLayout());
		setPreferredSize(new Dimension(500, 740));
		
		cardPanel = new JPanel();
		layout = new CardLayout();
		cardPanel.setLayout(layout);
		
		tableListPanel = new TableListPanel(this);
		tableListPanel.setName("TableListPanel");
		cardPanel.add(tableListPanel, tableListPanel.getName());
		
		gamePlayingPanel = new GamePlayingPanel(this);
		gamePlayingPanel.setName("GamePlayingPanel");
		cardPanel.add(gamePlayingPanel, gamePlayingPanel.getName());
		
		updateStatusTimer = new Timer(UPDATE_STATUS_DELAY,
			new UpdateStatusTimerListener());
		updateStatusTimer.start();
		
		add(cardPanel, BorderLayout.CENTER);
		currentPanel = tableListPanel;

		messagesPanel = new MessagesPanel(getClient());
		add(messagesPanel, BorderLayout.SOUTH);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void addNotify()
	{
		super.addNotify();
		
		tableListPanel.activate();
	}
	
	/**
	 * Client connecté
	 * @return Client connecté
	 */
	public Client getClient()
	{
		return client;
	}
	
	/**
	 * Statut du client connecté
	 * @return Statut du client connecté
	 */
	public ClientStatus getClientStatus()
	{
		return client.getStatus();
	}
	
	/**
	 * Modifie le panel courant
	 * @param value Nouveau panel
	 */
	private void setCurrentPanel(ClientSubPanel value)
	{
		if (value == currentPanel)
			return;
		
		currentPanel.deactivate();
		
		currentPanel = value;
		
		layout.show(cardPanel, currentPanel.getName());
		currentPanel.activate();
	}
	
	/**
	 * Appelé lorsque le statut du client a été mis à jour
	 * <p>
	 * Le statut du client est mis à jour selon un timer fixé à un délai de
	 * <tt>UPDATE_STATUS_DELAY</tt>.
	 * </p>
	 * <p>
	 * En plus de cela, les sous-panels du panel client peuvent aussi appeler
	 * cette méthode s'ils ont une information récente sur le statut.
	 * </p>
	 * @param status Nouveau statut
	 */
	public void clientStatusUpdated(ClientStatus status)
	{
		ClientSubPanel newPanel = null;
		ClientStatusKind statusKind = status.getStatusKind();
		
		if (statusKind == ClientStatusKind.Disconnected)
		{
			disconnected();
			return;
		}
		
		if (statusKind == ClientStatusKind.NoTable)
			newPanel = tableListPanel;
		else if (statusKind == ClientStatusKind.WaitingForPlayers)
			newPanel = tableListPanel;
		else if (statusKind == ClientStatusKind.NotPlaying)
			newPanel = gamePlayingPanel;
		else if (statusKind == ClientStatusKind.Auctioning)
			newPanel = gamePlayingPanel;
		else if (statusKind == ClientStatusKind.Playing)
			newPanel = gamePlayingPanel;
		
		setCurrentPanel(newPanel);
		
		if (newPanel != null)
			newPanel.clientStatusUpdated(status);
		
		updateStatusTimer.restart();
	}
	
	/**
	 * Passe dans l'état déconnecté
	 */
	public void disconnected()
	{
		currentPanel.deactivate();
		((Window) getRootPane().getParent()).dispose();
		new ConnectionFrame();
	}

	/**
	 * Listener pour le timer de mise à jour du statut du client
     * @author sjrd
     */
    private class UpdateStatusTimerListener implements ActionListener
    {
	    /**
	     * {@inheritDoc}
	     */
	    public void actionPerformed(ActionEvent event)
	    {
	    	Client client = getClient();
	    	if (client == null)
	    		return;
	    	
	    	updateStatusTimer.stop();
		    
		    new FetchClientStatusWorker(client)
		    {
		    	@Override
		    	protected void useResult(ClientStatus result)
		    	{
		   			clientStatusUpdated(result);
		    	}
		    }.execute();
	    }
    }
}
