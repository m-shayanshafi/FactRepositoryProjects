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
package sjrd.tricktakinggame.gui.controller;

import java.io.*;
import java.awt.*;

import javax.swing.*;

import sjrd.swing.*;
import sjrd.tricktakinggame.cards.*;
import sjrd.tricktakinggame.client.*;
import sjrd.tricktakinggame.remotable.*;
import sjrd.tricktakinggame.network.client.*;
import sjrd.tricktakinggame.gui.util.*;
import sjrd.tricktakinggame.gui.carddisplayers.*;
import sjrd.tricktakinggame.gui.client.*;
import sjrd.tricktakinggame.gui.event.*;
import sjrd.tricktakinggame.gui.networkactions.*;

/**
 * Panneau d'affichage du jeu
 * @author sjrd
 */
public class GUIController extends ClientSubPanel implements PlayerController
{
	/**
	 * ID de sérialisation
	 */
	private static final long serialVersionUID = 1;
	
	/**
	 * Temps de pause
	 */
	private static final int PAUSE_DELAY = 2000;
	
	/**
	 * Nom du panel demandant le début de partie
	 */
	private static final String beginGamePanelName = "BeginGame";
	
	/**
	 * Nom du panel de l'enchère
	 */
	private static final String auctionPanelName = "Announces";
	
	/**
	 * Nom du panel d'affichage du jeu
	 */
	private static final String gameDisplayPanelName = "GameDisplay";
	
	/**
	 * Jeu à afficher
	 */
	private RemoteGame game;
	
	/**
	 * Joueur principale
	 */
	private RemotablePlayer player;
	
	/**
	 * Displayer de cartes
	 */
	private CardDisplayer cardDisplayer = new CardDisplayer();
	
	/**
	 * Modèle de la liste des scores des équipes
	 */
	private TeamScoreListModel teamScoreListModel;
	
	/**
	 * Liste d'affichage des scores des équipes
	 */
	private JList teamScoreList;
	
	/**
	 * Layout du panel affiché au centre
	 */
	private CardLayout centerLayout;
	
	/**
	 * Panel affiché au centre
	 */
	private JPanel centerPanel;
	
	/**
	 * Action de commencer une partie
	 */
	private BeginGameAction beginGameAction;
	
	/**
	 * Action de quitter la table
	 */
	private LeaveTableAction leaveTableAction;
	
	/**
	 * Panel contenant les boutons pour le démarrage d'une partie
	 */
	private JPanel beginGamePanel;
	
	/**
	 * Panel d'affichage de l'enchère
	 */
	private AuctionPanel auctionPanel;
	
	/**
	 * Panel d'affichage des cartes jouées pendant ce pli
	 */
	private GameDisplayPanel gameDisplayPanel;
	
	/**
	 * Panel d'affichage des cartes du joueur
	 */
	private HorizontalCardsDisplayPanel playerCardsPanel;
	
	/**
	 * Crée le panel
	 * @param aPlayer Joueur principal
	 */
	public GUIController(ClientPanel aOwner, RemoteGame aGame,
		RemotablePlayer aPlayer)
	{
		super(aOwner, new BorderLayout());
		
		game = aGame;
		player = aPlayer;
		
		build();
		
		for (Card card: game.getDeck().originalCardsIterable())
		{
			try
			{
				cardDisplayer.loadImage(card);
			}
			catch (IOException error)
			{
				System.err.println("Can't load image for card: " + card);
			}
		}
	}
	
	/**
	 * Construit le panel
	 */
	private void build()
	{
		setBackground(new Color(0, 128, 0));
		
		// Scores des équipes
		
		teamScoreListModel = new TeamScoreListModel(game);
		teamScoreList = new JList(teamScoreListModel);
		teamScoreList.setOpaque(false);
		add(teamScoreList, BorderLayout.NORTH);
		
		// Panel de demande de commencer une partie
		
		beginGameAction = new BeginGameAction(this, "Commencer une partie...");
		leaveTableAction = new LeaveTableAction(this, "Quitter la table");
		
		JPanel innerPanel = new JPanel();
		innerPanel.setLayout(new BoxLayout(innerPanel,
			BoxLayout.Y_AXIS));
		innerPanel.setOpaque(false);
		innerPanel.setAlignmentY(Component.CENTER_ALIGNMENT);
		
		JButton button = new JButton(beginGameAction);
		button.setAlignmentX(Component.CENTER_ALIGNMENT);
		innerPanel.add(button);
		innerPanel.add(Box.createVerticalStrut(10));
		button = new JButton(leaveTableAction);
		button.setAlignmentX(Component.CENTER_ALIGNMENT);
		innerPanel.add(button);
		
		beginGamePanel = new JPanel(new CenterLayout());
		beginGamePanel.setOpaque(false);
		beginGamePanel.add(innerPanel);
		
		// Panel d'affichage des annonces
		
		auctionPanel = new AuctionPanel(game);
		
		// Panel d'affichage du jeu
		
		gameDisplayPanel = new GameDisplayPanel(cardDisplayer, game, player);
		
		// Panel du centre
		
		centerLayout = new CardLayout();
		centerPanel = new JPanel(centerLayout);
		centerPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		centerPanel.setOpaque(false);
		centerPanel.add(beginGamePanel, beginGamePanelName);
		centerPanel.add(auctionPanel, auctionPanelName);
		centerPanel.add(gameDisplayPanel, gameDisplayPanelName);
		
		add(centerPanel, BorderLayout.CENTER);
		
		// Panel d'affichage des cartes du joueur contrôlé
		
		playerCardsPanel = new HorizontalCardsDisplayPanel(cardDisplayer);
		add(playerCardsPanel, BorderLayout.SOUTH);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void deactivate()
	{
		getClient().interruptResponder();
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void clientStatusUpdated(ClientStatus status)
	{
		super.clientStatusUpdated(status);
		
		ClientStatusKind statusKind = status.getStatusKind();

		updateActionsEnabled();

		if (statusKind == ClientStatusKind.NotPlaying)
			centerLayout.show(centerPanel, beginGamePanelName);
		else if (statusKind == ClientStatusKind.Auctioning)
			centerLayout.show(centerPanel, auctionPanelName);
		else if (statusKind == ClientStatusKind.Playing)
			centerLayout.show(centerPanel, gameDisplayPanelName);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void updateActionsEnabled()
	{
		ClientStatus status = getClientStatus();
		
		if (isWorking() ||
			(status.getStatusKind() != ClientStatusKind.NotPlaying))
		{
			beginGameAction.setEnabled(false);
			leaveTableAction.setEnabled(false);
		}
		else
		{
			beginGameAction.setEnabled(!status.doesWantToBeginGame());
			leaveTableAction.setEnabled(true);
		}
	}
	
	/**
	 * Met à jour l'affichage
	 */
	public void updateDisplay()
	{
		teamScoreListModel.updateDisplay();
		
		gameDisplayPanel.updateDisplay();
		
		auctionPanel.updateDisplay();
		
		playerCardsPanel.setCardsAutoPos(player.getCardsSnapshot());
	}
	
	/**
	 * Ajoute un listener de réception de message
	 * @param listener Listener
	 * @see #removeMessageListener(MessageListener)
	 * @see #getMessageListeners()
	 */
	public void addMessageListener(MessageListener listener)
	{
		listenerList.add(MessageListener.class, listener);
	}
	
	/**
	 * Retire un listener de réception de message
	 * @param listener Listener
	 * @see #addMessageListener(MessageListener)
	 * @see #getMessageListeners()
	 */
	public void removeMessageListener(MessageListener listener)
	{
		listenerList.remove(MessageListener.class, listener);
	}
	
	/**
	 * Liste des listeners de réception de message enregistrés
	 * @return Liste des listeners de réception de message enregistrés
	 * @see #addMessageListener(MessageListener)
	 * @see #removeMessageListener(MessageListener)
	 */
	public MessageListener[] getMessageListeners()
	{
		return listenerList.getListeners(MessageListener.class);
	}
	
	/**
	 * Signale la réception d'un message à tous les listeners de message
	 * @param message Message reçu
	 * @see #addMessageListener(MessageListener)
	 * @see #removeMessageListener(MessageListener)
	 * @see #getMessageListeners()
	 */
	protected void fireMessageReceived(Message message)
	{
		for (MessageListener listener: getMessageListeners())
			listener.messageReceived(message);
	}

	/**
	 * {@inheritDoc}
	 */
	public <A extends Announce> A chooseAnnounce(A ... availableAnnounces)
		throws CardGameException
	{
    	return auctionPanel.chooseAnnounce(availableAnnounces);
	}

    /**
     * {@inheritDoc}
     */
    public Card playTurn() throws CardGameException
    {
    	final Card[] chosen = new Card[1];
    	
    	synchronized (chosen)
    	{
    		SwingUtilities.invokeLater(new Runnable()
    		{
    			public void run()
    			{
    				playerCardsPanel.addCardSelectionListener(
    					new CardListener());
    			}

    			class CardListener implements CardSelectionListener
    			{
    				public void cardSelected(Card card)
    				{
    					playerCardsPanel.removeCardSelectionListener(this);

    					synchronized (chosen)
    					{
    						chosen[0] = card;
    						chosen.notify();
    					}
    				}
    			}
    		});

    		try
    		{
    			chosen.wait();
    			return chosen[0];
    		}
    		catch (InterruptedException error)
    		{
    			throw new CardGameInterruptedException(error);
    		}
    	}
    }

    /**
     * {@inheritDoc}
     */
    public int messageDialog(final String message, final String[] buttons)
        throws CardGameException
    {
    	final int[] chosen = new int[1];

    	try
    	{
    		SwingUtilities.invokeAndWait(new Runnable()
    		{
    			public void run()
    			{
    				do
    				{
    			    	chosen[0] = JOptionPane.showOptionDialog(null,
    			    		message, "Message", JOptionPane.DEFAULT_OPTION,
    			    		JOptionPane.PLAIN_MESSAGE, null, buttons, null);
    				} while (chosen[0] < 0);
    			}
    		});
    	}
    	catch (Exception error)
    	{
    		throw new CardGameException(error);
    	}
    	
		return chosen[0];
    }

    /**
     * {@inheritDoc}
     */
    public void showMessage(Message message)
    {
    	fireMessageReceived(message);
    }

    /**
     * {@inheritDoc}
     */
    public void notifyUpdate()
    {
    	new UpdateWorker().execute();
    }
    
    /**
     * {@inheritDoc}
     */
    public void pause() throws CardGameException
    {
    	try
    	{
    		Thread.sleep(PAUSE_DELAY);
    	}
    	catch (InterruptedException error)
    	{
			throw new CardGameInterruptedException(error);
    	}
    }
    
    /**
     * Worker pour mettre à jour l'affichage
     * @author sjrd
     */
    private class UpdateWorker extends SwingWorker<Object, Object>
    {
    	/**
    	 * {@inheritDoc}
    	 */
    	@Override
    	protected Object doInBackground()
    	{
    		game.ensureUpdated();
    		return null;
    	}
    	
    	/**
    	 * {@inheritDoc}
    	 */
    	@Override
    	protected void done()
    	{
    		updateDisplay();
    	}
    }
}
