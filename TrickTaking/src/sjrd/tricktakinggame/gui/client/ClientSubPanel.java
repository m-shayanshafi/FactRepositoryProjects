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

import javax.swing.*;

import sjrd.tricktakinggame.client.*;
import sjrd.tricktakinggame.network.client.*;

/**
 * Classe de base pour les sous-panels de <tt>ClientPanel</tt>
 * @author sjrd
 * @see sjrd.tricktakinggame.gui.client.ClientPanel
 */
public abstract class ClientSubPanel extends JPanel
{
	/**
	 * Panel propriétaire
	 */
	private ClientPanel owner;
	
	/**
	 * Client courant
	 */
	private Client client;
	
	/**
	 * Worker courant
	 */
	private SwingWorker<?, ?> currentWorker = null;
	
	/**
	 * {@inheritDoc}
	 */
	public ClientSubPanel(ClientPanel aOwner, LayoutManager layout,
		boolean isDoubleBuffered)
	{
		super(layout, isDoubleBuffered);
		
		owner = aOwner;
		client = owner.getClient();
	}

	/**
	 * {@inheritDoc}
	 */
	public ClientSubPanel(ClientPanel aOwner, LayoutManager layout)
	{
		this(aOwner, layout, true);
	}

	/**
	 * {@inheritDoc}
	 */
	public ClientSubPanel(ClientPanel aOwner, boolean isDoubleBuffered)
	{
		this(aOwner, new FlowLayout(), isDoubleBuffered);
	}

	/**
	 * {@inheritDoc}
	 */
	public ClientSubPanel(ClientPanel aOwner)
	{
		this(aOwner, true);
	}
    
    /**
     * Panel propriétaire
     * @return Panel propriétaire
     */
    public ClientPanel getOwner()
    {
    	return owner;
    }

	/**
	 * Client courant
	 * @return Client courant
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
	 * Active le panel
	 */
	public void activate()
	{
	}
	
	/**
	 * Désactive le panel
	 */
	public void deactivate()
	{
	}
	
	/**
	 * Appelée lorsque le statut du client est mis à jour
	 * <p>
	 * <b>Attention :</b> que le statut du client ait été mis à jour ne signifie
	 * pas qu'il a changé.
	 * </p>
	 * <p>
	 * Cette méthode n'est appelée que lorsque ce panel est activé.
	 * </p>
	 * @param status Nouveau statut du client
	 */
	public void clientStatusUpdated(ClientStatus status)
	{
	}
	
	/**
	 * Met à jour la disponibilité des actions
	 */
	public void updateActionsEnabled()
	{
	}
	
	/**
	 * Modifie le worker courant
	 * @param value Nouveau worker courant
	 */
	public void setCurrentWorker(SwingWorker<?, ?> value)
	{
		synchronized (this)
		{
			currentWorker = value;
		}
		
		if (SwingUtilities.isEventDispatchThread())
			updateActionsEnabled();
		else
		{
			SwingUtilities.invokeLater(new Runnable()
			{
				public void run()
				{
					updateActionsEnabled();
				}
			});
		}
	}
	
	/**
	 * Teste si le panel est en train de travailler sur une action
	 * @return <tt>true</tt> si une action est en cours, <tt>false</tt> sinon
	 */
	synchronized public boolean isWorking()
	{
		return (currentWorker != null) && (!currentWorker.isDone());
	}
}
