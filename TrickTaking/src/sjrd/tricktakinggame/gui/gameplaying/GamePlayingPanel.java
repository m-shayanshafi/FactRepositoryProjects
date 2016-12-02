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
package sjrd.tricktakinggame.gui.gameplaying;

import java.io.*;
import java.awt.*;

import javax.swing.*;

import sjrd.tricktakinggame.remotable.*;
import sjrd.tricktakinggame.client.*;
import sjrd.tricktakinggame.gui.util.*;
import sjrd.tricktakinggame.gui.client.*;
import sjrd.tricktakinggame.gui.controller.*;

/**
 * Panel de jeu
 * @author sjrd
 */
public class GamePlayingPanel extends ClientSubPanel
	implements PlayerControllerCreator
{
	/**
	 * ID de sérialisation
	 */
	private static final long serialVersionUID = 1;
	
	/**
	 * Contrôleur du joueur
	 */
	private GUIController controllerPanel = null;

	/**
	 * Crée un panel de liste des tables
	 * @param aOwner Panel client propriétaire
	 */
	public GamePlayingPanel(ClientPanel aOwner)
	{
		super(aOwner, new BorderLayout());

		build();
	}

	/**
	 * Construit le panel
	 */
	private void build()
	{
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void activate()
	{
		super.activate();
		
		getClient().setPlayerControllerCreator(this);
		
		new NetworkOperationWorker<Object, Object>()
		{
			@Override
			protected Object doInBackground() throws IOException
			{
				getClient().fetchGame();
				return null;
			}
			
			@Override
			protected void useResult(Object result)
			{
				controllerPanel.activate();
				controllerPanel.updateDisplay();
			}
		}.execute();
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void deactivate()
	{
		controllerPanel.deactivate();
		
		remove(controllerPanel);
		controllerPanel = null;

		super.deactivate();
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void clientStatusUpdated(ClientStatus status)
	{
		super.clientStatusUpdated(status);
		
		if (controllerPanel != null)
			controllerPanel.clientStatusUpdated(status);
	}

	/**
	 * Crée un contrôleur de joueur
	 * @param player Joueur correspondant
	 * @return Contrôleur pour ce joueur
	 */
	public PlayerController createPlayerController(final RemotablePlayer player)
	{
    	try
    	{
    		SwingUtilities.invokeAndWait(new Runnable()
    		{
    			public void run()
    			{
    				controllerPanel = new GUIController(getOwner(),
    					getClient().getGame(), player);
    				add(controllerPanel, BorderLayout.CENTER);
    			}
    		});
    	}
    	catch (Exception error)
    	{
    		throw new RuntimeException(error);
    	}
    	
		return controllerPanel;
	}
}
