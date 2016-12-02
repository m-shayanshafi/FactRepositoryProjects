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
package sjrd.tricktakinggame.gui.carddisplayers;

import java.util.*;
import java.awt.*;
import java.awt.event.*;

import javax.swing.*;

import sjrd.tricktakinggame.cards.*;
import sjrd.tricktakinggame.gui.util.*;
import sjrd.tricktakinggame.gui.event.*;

import static sjrd.tricktakinggame.gui.util.CardDisplayer.*;

/**
 * Panel qui affiche une liste de cartes, sous forme graphique
 * @author sjrd
 */
public abstract class CardsDisplayPanel extends JPanel
{
	/**
	 * Nombre de places accordées pour les cartes
	 */
	private int placeCount;
	
	/**
	 * Indique s'il faut mettre à jour automatiquement <tt>placeCount</tt>
	 */
	private boolean autoUpdatePlaceCount;
	
	/**
	 * Cartes affichées
	 */
	private java.util.List<PosCard> cards;
	
	/**
	 * Displayer de cartes
	 */
	private CardDisplayer cardDisplayer;
	
	/**
	 * Crée le panel avec une liste préétablie de cartes à afficher
	 * @param aCardDisplayer Displayer de cartes
	 * @param aPlaceCount Nombre de places accordées aux cartes (> 0)
	 * @param aCards Liste des cartes à afficher (peut être <tt>null</tt>)
	 */
	public CardsDisplayPanel(CardDisplayer aCardDisplayer, int aPlaceCount,
		Collection<? extends PosCard> aCards)
	{
		super();
		
		assert aPlaceCount > 0;
		
		cardDisplayer = aCardDisplayer;
		
		placeCount = aPlaceCount;
		autoUpdatePlaceCount = false;
		
		if (aCards == null)
			cards = new ArrayList<PosCard>();
		else
			cards = new ArrayList<PosCard>(aCards);
		
		setOpaque(false);
		addMouseListener(new MouseListener());
		
		contentsUpdated();
	}
	
	/**
	 * Crée le panel avec une liste préétablie de cartes à afficher
	 * @param aCardDisplayer Displayer de cartes
	 * @param aCards Liste des cartes à afficher (peut être <tt>null</tt>)
	 */
	public CardsDisplayPanel(CardDisplayer aCardDisplayer,
		Collection<? extends PosCard> aCards)
	{
		this(aCardDisplayer,
			((aCards == null) || (aCards.size() == 0) ? 1 : aCards.size()),
			aCards);
		autoUpdatePlaceCount = true;
	}
	
	/**
	 * Crée le panel
	 * @param aCardDisplayer Displayer de cartes
	 * @param aPlaceCount Nombre de places accordées aux cartes (> 0)
	 */
	public CardsDisplayPanel(CardDisplayer aCardDisplayer, int aPlaceCount)
	{
		this(aCardDisplayer, aPlaceCount, null);
	}
	
	/**
	 * Crée le panel
	 * @param aCardDisplayer Displayer de cartes
	 */
	public CardsDisplayPanel(CardDisplayer aCardDisplayer)
	{
		this(aCardDisplayer, 1, null);
		autoUpdatePlaceCount = true;
	}
	
	/**
	 * Displayer de cartes
	 * @return Displayer de cartes
	 */
	public CardDisplayer getCardDisplayer()
	{
		return cardDisplayer;
	}
	
	/**
	 * Nombre de places accordées aux cartes
	 * @return Nombre de places accordées aux cartes
	 */
	public int getPlaceCount()
	{
		return placeCount;
	}
	
	/**
	 * Modifie le nombre de places accordées aux cartes
	 * <p>
	 * En effet de bord, la propriété <tt>AutoUpdatePlaceCount</tt> est
	 * positionnée à <tt>false</tt>.
	 * </p>
	 * @param value Nouveau nombre de places (> 0)
	 * @see #getAutoUpdatePlaceCount()
	 */
	public void setPlaceCount(int value)
	{
		assert value > 0;
		placeCount = value;
		autoUpdatePlaceCount = false;
		contentsUpdated();
	}
	
	/**
	 * Indique si le nombre de places est mis à jour automatiquement
	 * <p>
	 * Lorsque le nombre de places est mis à jour automatiquement, modifier la
	 * liste des cartes affichées modifie également le nombre de places. Ce
	 * nombre vaut alors le nombre de cartes affichées (ou 1 s'il n'y a pas de
	 * carte affichée du tout).
	 * </p>
	 * @return <tt>true</tt> si le nombre de places est mis à jour
	 *         automatiquement, <tt>false</tt> sinon
	 * @see #getPlaceCount()
	 */
	public boolean getAutoUpdatePlaceCount()
	{
		return autoUpdatePlaceCount;
	}
	
	/**
	 * Met à jour le nombre de places pour les cartes, si demandé
	 * @see #getPlaceCount()
	 * @see #getAutoUpdatePlaceCount()
	 */
	protected void updatePlaceCount()
	{
		if (autoUpdatePlaceCount)
		{
			placeCount = cards.size();
			if (placeCount == 0)
				placeCount = 1;
		}
	}
	
	/**
	 * Spécifie si le nombre de places doit être mis à jour automatiquement
	 * @param value <tt>true</tt> pour que le nombre de places soit mis à jour
	 *        automatiquement, <tt>false</tt> pour qu'il ne le soit pas
	 * @see #getAutoUpdatePlaceCount()
	 */
	public void setAutoUpdatePlaceCount(boolean value)
	{
		autoUpdatePlaceCount = value;
		updatePlaceCount();
	}
	
	/**
	 * Nombre de cartes affichées
	 * @return Nombre de cartes affichées
	 */
	public int getCardCount()
	{
		return cards.size();
	}
	
	/**
	 * Récupère la liste des cartes affichées
	 * @return Liste des cartes affichées
	 */
	public java.util.List<PosCard> getCards()
	{
		return new ArrayList<PosCard>(cards);
	}
	
	/**
	 * Met à jour les cartes à afficher
	 * @param value Nouvelle liste de cartes
	 */
	public void setCards(Collection<? extends PosCard> value)
	{
		cards.clear();
		cards.addAll(value);
		updatePlaceCount();
		
		contentsUpdated();
	}
	
	/**
	 * Met à jour les cartes à afficher, avec des positions automatiques
	 * <p>
	 * Les cartes reçoivent leurs positions d'après l'ordre dans lequel elles
	 * sont renvoyées par l'itérateur de la collection
	 * </p>
	 * @param cards Cartes à afficher
	 */
	public void setCardsAutoPos(Collection<? extends Card> cards)
	{
		setCards(PosCard.cardCollectionToPosCardCollection(cards));
	}
	
	/**
	 * Calcule la dimension de la surface d'affichage
	 * @return Dimension de la surface d'affichage
	 */
	protected abstract Dimension computeDisplayDimension();
	
	/**
	 * Calcule le point de base de dessin
	 * @param displayDimension Dimension de la surface d'affichage
	 * @return Point de base de dessin
	 * @see #computeDrawBasePoint()
	 */
	protected Point computeDrawBasePoint(Dimension displayDimension)
	{
		int baseX = (getWidth() - displayDimension.width) / 2;
		int baseY = (getHeight() - displayDimension.height) / 2;
		
		return new Point(baseX, baseY);
	}
	
	/**
	 * Calcule le point de base de dessin
	 * <p>
	 * Cette variable calcule elle-même la dimension de la surface d'affichage.
	 * </p>
	 * @return Point de base de dessin
	 * @see #computeDrawBasePoint(Dimension)
	 * @see #computeDisplayDimension()
	 */
	protected Point computeDrawBasePoint()
	{
		return computeDrawBasePoint(computeDisplayDimension());
	}
	
	/**
	 * Met à jour l'affichage avec les nouvelles cartes
	 */
	protected void contentsUpdated()
	{
		repaint();
	}
	
	/**
	 * Détermine la position d'une carte
	 * @param index Index de la carte
	 * @return Position d'affichage de la carte, relativement au point de base
	 */
	protected abstract Point getCardPosition(Dimension displayDimension,
		int index);
	
	/**
	 * Affiche les cartes sur un objet graphique
	 * <p>
	 * Cette méthode est appelée par <tt>paintComponent(Graphics)</tt> pour
	 * afficher les cartes.
	 * </p>
	 * <p>
	 * Vous pouvez surcharger cette méthode si vous désirez modifier ou
	 * empêcher l'affichage des cartes, sans perdre le bénéfice des
	 * implémentations de base de <tt>paintComponent(Graphics)</tt>
	 * </p>
	 * @param g Objet graphique
	 * @see #paintComponent(Graphics)
	 */
	protected void paintCards(Graphics g)
	{
		Dimension displayDimension = computeDisplayDimension();
		Point basePoint = computeDrawBasePoint(displayDimension);
		
		for (int i = 0; i < cards.size(); i++)
		{
			PosCard posCard = cards.get(i);
			
			Point cardPosition = getCardPosition(displayDimension,
				posCard.getPosition());
			
			cardDisplayer.drawCard(g, posCard.getCard(),
				basePoint.x + cardPosition.x, basePoint.y + cardPosition.y);
		}
	}
	
	/**
	 * {@inheritDoc}
	 * <p>
	 * Pour desinner les cartes, cette méthode appelle la méthode
	 * <tt>paintCards(Graphics)</tt>.
	 * </p>
	 * @see #paintCards(Graphics)
	 */
	@Override
	public void paintComponent(Graphics g)
	{
		super.paintComponent(g);
		
		paintCards(g);
	}
	
	/**
	 * Ajoute un listener de sélection de carte
	 * @param listener Listener
	 * @see #removeCardSelectionListener(CardSelectionListener)
	 * @see #getMessageListeners()
	 */
	public void addCardSelectionListener(CardSelectionListener listener)
	{
		listenerList.add(CardSelectionListener.class, listener);
	}
	
	/**
	 * Retire un listener de sélection de carte
	 * @param listener Listener
	 * @see #addCardSelectionListener(CardSelectionListener)
	 * @see #getMessageListeners()
	 */
	public void removeCardSelectionListener(CardSelectionListener listener)
	{
		listenerList.remove(CardSelectionListener.class, listener);
	}
	
	/**
	 * Liste des listeners de sélection de carte enregistrés
	 * @return Liste des listeners de sélection de carte enregistrés
	 * @see #addMessageListener(CardSelectionListener)
	 * @see #removeMessageListener(CardSelectionListener)
	 */
	public CardSelectionListener[] getCardSelectionListeners()
	{
		return listenerList.getListeners(CardSelectionListener.class);
	}
	
	/**
	 * Déclenche un événement de sélection de carte
	 * @param card Carte sélectionnée
	 * @see #addCardSelectionListener(CardSelectionListener)
	 * @see #removeMessageListener(CardSelectionListener)
	 * @see #getMessageListeners()
	 */
	protected void fireCardSelected(Card card)
	{
		for (CardSelectionListener listener: getCardSelectionListeners())
			listener.cardSelected(card);
	}

	/**
	 * Listener de la souris
	 * @author sjrd
	 */
	private class MouseListener extends MouseAdapter
	{
	    /**
	     * {@inheritDoc}
	     */
		@Override
	    public void mouseClicked(MouseEvent event)
	    {
	    	if (event.getClickCount() != 1)
	    		return;
	    	if (event.getButton() != MouseEvent.BUTTON1)
	    		return;
			
			Dimension displayDimension = computeDisplayDimension();
			Point basePoint = computeDrawBasePoint(displayDimension);
	    	
	    	int x = event.getX() - basePoint.x;
	    	int y = event.getY() - basePoint.y;
	    	Point clickPoint = new Point(x, y);
			
			for (int i = cards.size()-1; i >= 0; i--)
			{
				PosCard posCard = cards.get(i);
				
				Rectangle cardRect =
					new Rectangle(getCardPosition(displayDimension,
						posCard.getPosition()), cardDimension);
				
				if (cardRect.contains(clickPoint))
				{
					fireCardSelected(posCard.getCard());
					break;
				}
			}
	    }
	}
}
