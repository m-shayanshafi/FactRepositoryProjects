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

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;

import sjrd.tricktakinggame.remotable.*;

/**
 * Panel d'affichage des annonces
 * @author sjrd
 */
public class AuctionPanel extends JPanel
{
	/**
	 * ID de sérialisation
	 */
	private static final long serialVersionUID = 1;
	
	/**
	 * Jeu dont refléter les infos
	 */
	private RemotableGame game;
	
	/**
	 * Modèle de la table des annonces
	 */
	private AnnounceTableModel announceTableModel;
	
	/**
	 * Table des annonces
	 */
	private JTable announceTable;
	
	/**
	 * Panneau de scrolling de la table des annonces
	 */
	private JScrollPane announceTableScrollPane;
	
	/**
	 * Combo-box de choix d'une annonce à faire
	 */
	private JComboBox announceComboBox;
	
	/**
	 * Crée le panel
	 * @param aGame Jeu dont afficher les annonces
	 * @param aPlayer Joueur contrôlé
	 */
	public AuctionPanel(RemotableGame aGame)
	{
		super(new BorderLayout());
		
		game = aGame;
		
		build();
	}
	
	/**
	 * Construit le panel
	 */
	private void build()
	{
		// Table des annonces
		announceTableModel = new AnnounceTableModel(game);
		announceTable = new JTable(announceTableModel);
		announceTableScrollPane = new JScrollPane(announceTable);
		announceTable.setFillsViewportHeight(true);
		add(announceTable, BorderLayout.CENTER);
		
		// Combo-box de choix d'annonce
		announceComboBox = new JComboBox();
		add(announceComboBox, BorderLayout.SOUTH);
	}
	
	/**
	 * Met à jour l'affichage
	 */
	public void updateDisplay()
	{
		announceTableModel.updateDisplay();

		JScrollBar scrollBar =
			announceTableScrollPane.getVerticalScrollBar();
		scrollBar.setValue(scrollBar.getMaximum());
	}

	/**
	 * {@inheritDoc}
	 */
	public <A extends Announce> A chooseAnnounce(A ... availableAnnounces)
		throws CardGameException
	{
    	final String[] announceNames = new String[availableAnnounces.length+1];
    	final int[] chosen = new int[1];
    	
    	announceNames[0] = "Veuillez sélectionner une annonce";
    	for (int i = 0; i < availableAnnounces.length; i++)
    		announceNames[i+1] = availableAnnounces[i].getName();
    	
    	synchronized (chosen)
    	{
    		SwingUtilities.invokeLater(new Runnable()
    		{
    			public void run()
    			{
    				announceComboBox.setModel(new DefaultComboBoxModel(
    					announceNames));
    				announceComboBox.setSelectedIndex(0);
    				announceComboBox.addActionListener(
    					new AnnounceSelectionListener());
    			}

    			class AnnounceSelectionListener implements ActionListener
    			{
    				public void actionPerformed(ActionEvent event)
    				{
    					int index = announceComboBox.getSelectedIndex()-1;
    					if (index < 0)
    						return;
    					
    					announceComboBox.removeActionListener(this);
    					announceComboBox.setModel(new DefaultComboBoxModel());

    					synchronized (chosen)
    					{
    						chosen[0] = index;
    						chosen.notify();
    					}
    				}
    			}
    		});

    		try
    		{
    			chosen.wait();
    			return availableAnnounces[chosen[0]];
    		}
    		catch (InterruptedException error)
    		{
    			throw new CardGameInterruptedException(error);
    		}
    	}
	}
}
