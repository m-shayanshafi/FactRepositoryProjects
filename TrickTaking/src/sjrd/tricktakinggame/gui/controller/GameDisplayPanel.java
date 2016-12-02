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

import java.util.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;

import sjrd.tricktakinggame.cards.*;
import sjrd.tricktakinggame.remotable.*;
import sjrd.tricktakinggame.gui.carddisplayers.*;
import sjrd.tricktakinggame.gui.util.*;

/**
 * Panel d'affichage du centre du contrôleur
 * @author sjrd
 */
public class GameDisplayPanel extends CircleCardsDisplayPanel
{
	/**
	 * ID de sérialisation
	 */
	private static final long serialVersionUID = 1;
	
	/**
	 * Largeur de la bande utilisée pour afficher les infos sur les joueurs
	 */
	protected static final int playersInfoCircleWidth = 120;
	
	/**
	 * Hauteur de la bande utilisée pour afficher les infos sur les joueurs
	 */
	protected static final int playersInfoCircleHeight = 55;
	
	/**
	 * Jeu dont refléter les infos
	 */
	private RemotableGame game;
	
	/**
	 * Joueur dont afficher la vue
	 */
	private RemotablePlayer player;
	
	/**
	 * Infos de chaque joueur
	 */
	private PlayerDisplayInfo[] playersInfo = null;
	
	/**
	 * Crée le panel
	 * @param aCardDisplayer Displayer de cartes
	 * @param aGame Jeu dont refléter les infos
	 * @param aPlayer Joueur dont afficher la vue
	 */
	public GameDisplayPanel(CardDisplayer aCardDisplayer,
	    RemotableGame aGame, RemotablePlayer aPlayer)
	{
		super(aCardDisplayer, aGame.getPlayerCount());
		
		game = aGame;
		player = aPlayer;
		playersInfo = new PlayerDisplayInfo[game.getPlayerCount()];
		
		for (int i = 0; i < playersInfo.length; i++)
			playersInfo[i] = new PlayerDisplayInfo(game.getPlayers(i));
		
		build();
	}
	
	/**
	 * Construit le panel
	 */
	private void build()
	{
		setLayout(new BorderLayout());
		
		// Listener pour l'affichage des hint de cartes
		MouseListener mouseListener = new MouseListener();
		addMouseListener(mouseListener);
		addMouseMotionListener(mouseListener);
	}
	
	/**
	 * Joueur contrôlé
	 * @return Joueur contrôlé
	 */
	private RemotablePlayer getControlledPlayer()
	{
		return player;
	}
	
	/**
	 * Met à jour l'affichage
	 */
	public void updateDisplay()
	{
		// Cartes jouées par les joueurs
		
		java.util.List<PosCard> playedCards =
			new ArrayList<PosCard>(game.getPlayerCount());

		int selfPos = player.getPosition();
		int activePlayerPos = game.getActivePlayer().getPosition();
		for (int i = 0; i < game.getPlayerCount(); i++)
		{
			RemotablePlayer player = game.getCyclicPlayers(activePlayerPos + i);
			
			if (player.hasPlayedCard())
			{
				int position = player.getPosition() - selfPos;
				
				if (player.isPlayedCardHidden())
					playedCards.add(new PosCard(position, null));
				else
					playedCards.add(new PosCard(position,
						player.getPlayedCard()));
			}
		}
		
		setCards(playedCards);
		
		// Informations sur les joueurs
		
		for (PlayerDisplayInfo playerInfo: playersInfo)
			playerInfo.updateInfo();
	}

	/**
	 * Calcule la dimension totale d'affichage
	 * @return Dimension totale d'affichage
	 */
	protected Dimension computeEntireDisplayDimension()
	{
		return super.computeDisplayDimension();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected Dimension computeDisplayDimension()
	{
		Dimension entireDimension = computeEntireDisplayDimension();
		
		return new Dimension(entireDimension.width - 2*playersInfoCircleWidth,
			entireDimension.height - 2*playersInfoCircleHeight);
	}

	/**
	 * Calcule la position d'affichage des infos d'un joueur
	 * @param displayDimension Dimension d'affichage
	 * @param index Index du joueur, par rapport au joueur actif
	 */
	protected Point getPlayerInfoPosition(Dimension displayDimension, int index)
	{
		Point infoCenterPos = getObjectCenterPosition(displayDimension,
			playersInfoCircleWidth, playersInfoCircleHeight, index);
		return infoCenterPos;
	}
	
	/**
	 * Affiche une liste de chaînes de caractères les unes en-dessous des autres
	 * @param g Contexte graphique
	 * @param strings Liste des chaînes de caractères
	 * @param x Abscisse du point central de dessin
	 * @param y Ordonnée du point central de dessin
	 * @return Rectangle d'affichage
	 */
	protected Rectangle drawMultiLine(Graphics g, String[] strings, int x,
		int y)
	{
		FontMetrics metrics = g.getFontMetrics();
		
		int[] relXPositions = new int[strings.length];
		int[] relYPositions = new int[strings.length];
		int maxWidth = 0;
		int totalHeight = 0;
		
		for (int i = 0; i < strings.length; i++)
		{
			Rectangle2D bounds = metrics.getStringBounds(strings[i], g);
			int width = (int) bounds.getWidth();
			int height = (int) bounds.getHeight();
			
			relXPositions[i] = - width / 2;
			relYPositions[i] = totalHeight + height;
			totalHeight += height;
			
			if (width > maxWidth)
				maxWidth = width;
		}
		
		y -= totalHeight / 2;
		
		for (int i = 0; i < strings.length; i++)
		{
			int xPos = x + relXPositions[i];
			int yPos = y + relYPositions[i];
			g.drawString(strings[i], xPos, yPos);
		}
		
		return new Rectangle(x - maxWidth/2, y, maxWidth, totalHeight);
	}
	
	/**
	 * Affiche les infos des joueurs sur un objet graphique
	 * <p>
	 * Cette méthode est appelée par <tt>paintComponent(Graphics)</tt>.
	 * </p>
	 * @param g Objet graphique
	 * @see #paintComponent(Graphics)
	 */
	protected void paintPlayersInfo(Graphics g)
	{
		// Informations sur les joueurs
		
		assert playersInfo != null;
		
		Dimension displayDimension = computeEntireDisplayDimension();
		Point basePoint = computeDrawBasePoint(displayDimension);

		for (PlayerDisplayInfo playerInfo: playersInfo)
		{
			Point infoPos = getPlayerInfoPosition(displayDimension,
				playerInfo.getIndex());
			playerInfo.setBounds(drawMultiLine(g, playerInfo.getInfo(),
				basePoint.x + infoPos.x, basePoint.y + infoPos.y));
		}
		
		// Contrat courant
		
		if (game.getCurrentContractName() != null)
		{
			int lineHeight = g.getFontMetrics().getHeight();
			g.drawString(game.getCurrentContractName(), 10, 10 + lineHeight);
		}
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void paintComponent(Graphics g)
	{
		super.paintComponent(g);
		
		paintPlayersInfo(g);
	}
	
	/**
	 * Trouve les infos d'affichage d'un joueur à partir d'une position
	 * @param x Abscisse
	 * @param y Ordonnée
	 * @return Infos d'affichage du joueur, ou <tt>null</tt> si non trouvé
	 */
	protected PlayerDisplayInfo findPlayerDisplayInfoByPos(int x, int y)
	{
		for (PlayerDisplayInfo playerInfo: playersInfo)
			if (playerInfo.getBounds().contains(x, y))
				return playerInfo;
		
		return null;
	}
	
	/**
	 * Informations affichées sur un joueur
	 * @author sjrd
	 */
	protected class PlayerDisplayInfo
	{
		/**
		 * Joueur
		 */
		private RemotablePlayer player;
		
		/**
		 * Informations sous forme de chaînes
		 */
		private String[] strInfo;
		
		/**
		 * Bords d'affichage
		 */
		private Rectangle bounds;
		
		/**
		 * Crée les infos
		 * @param aPlayer Joueur
		 */
		public PlayerDisplayInfo(RemotablePlayer aPlayer)
		{
			super();
			
			player = aPlayer;
			strInfo = new String[0];
			bounds = new Rectangle();
		}
		
		/**
		 * Met à jour les infos sur le joueur
		 */
		public void updateInfo()
		{
			String[] info = new String[3];
			int i = 0;
			
			int cardCount = player.getCardCount();
			int collectedTrickCount = player.getCollectedTrickCount();
			
			info[i++] = player.getName();
			if (cardCount > 0)
				info[i++] = String.format("%d carte(s) en main", cardCount);
			if (collectedTrickCount > 0)
				info[i++] = String.format("%d pli(s) rammassé(s)",
					collectedTrickCount);
			
			strInfo = Arrays.copyOf(info, i);
		}
		
		/**
		 * Joueur
		 * @return Joueur
		 */
		public RemotablePlayer getPlayer()
		{
			return player;
		}
		
		/**
		 * Index d'affichage
		 * @return Index d'affichage
		 */
		public int getIndex()
		{
			return player.getPosition() -
				getControlledPlayer().getPosition();
		}
		
		/**
		 * Informations sous forme de chaînes
		 * @return Informations sous forme de chaînes
		 */
		public String[] getInfo()
		{
			return strInfo.clone();
		}
		
		/**
		 * Bornes d'affichage
		 * @return Bornes d'affichage
		 */
		public Rectangle getBounds()
		{
			return (Rectangle) bounds.clone();
		}
		
		/**
		 * Modifie les bornes d'affichage
		 * @param value Nouvelles bornes
		 */
		public void setBounds(Rectangle value)
		{
			bounds = value;
		}
	}

	/**
	 * Listener de la souris
	 * @author sjrd
	 */
	private class MouseListener extends MouseAdapter
	{
		/**
		 * Fenêtre d'affichage de cartes courante
		 */
		private CardsDisplayWindow currentCardsWindow = null;
		
		/**
		 * Joueur dont les cartes sont actuellement affichées dans une fenêtre
		 */
		private PlayerDisplayInfo currentWindowPlayer = null;

	    /**
	     * {@inheritDoc}
	     */
		@Override
	    public void mouseExited(MouseEvent event)
	    {
	    	if (currentCardsWindow != null)
	    		currentCardsWindow.dispose();
	    }

	    /**
	     * {@inheritDoc}
	     */
		@Override
	    public void mouseMoved(MouseEvent event)
		{
			// Trouver les infos de joueurs dont afficher les cartes
			PlayerDisplayInfo playerInfo = findPlayerDisplayInfoByPos(
				event.getX(), event.getY());
			
			// Pas de changement
			if (playerInfo == currentWindowPlayer)
				return;
			
			// Cacher la fenêtre qui était affichée avant
			if (currentCardsWindow != null)
			{
				currentCardsWindow.setVisible(false);
				currentCardsWindow.dispose();
			}
			
			// Ne pas afficher d'infos vides
			if ((playerInfo != null) &&
				(playerInfo.getPlayer().getCardCount() == 0))
				playerInfo = null;
			
			// Mettre à jour les infos
			currentCardsWindow = null;
			currentWindowPlayer = playerInfo;
			
			// Afficher la nouvelle fenêtre
			if (playerInfo != null)
			{
				Collection<Card> cards =
					playerInfo.getPlayer().getCardsSnapshot();
				CardsDisplayWindow window = new CardsDisplayWindow(
					new HorizontalCardsDisplayPanel(getCardDisplayer(),
						PosCard.cardCollectionToPosCardCollection(cards)));
				
				window.setSize(window.getPreferredSize());

				Rectangle playerBounds = playerInfo.getBounds();
				Point locationOnScreen = getLocationOnScreen();
				playerBounds.translate(locationOnScreen.x, locationOnScreen.y);
				
				int x = playerBounds.x + playerBounds.width / 2;
				int y = playerBounds.y + playerBounds.height + 3;
				
				window.setLocation(x - window.getWidth() / 2, y);
				window.setVisible(true);
				
				currentCardsWindow = window;
			}
		}
	}
}
