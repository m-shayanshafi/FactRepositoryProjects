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

import java.awt.*;
import java.util.*;

import sjrd.tricktakinggame.gui.util.*;

import static sjrd.tricktakinggame.gui.util.CardDisplayer.*;

/**
 * Panel affichant une liste de cartes en cercle
 * @author sjrd
 */
public class CircleCardsDisplayPanel extends CardsDisplayPanel
{
	/**
	 * ID de sérialisation
	 */
	private static final long serialVersionUID = 1;
	
	/**
	 * Crée le panel avec une liste préétablie de cartes à afficher
	 * @param aCardDisplayer Displayer de cartes
	 * @param aPlaceCount Nombre de places accordées aux cartes (> 0)
	 * @param aCards Liste des cartes à afficher (peut être <tt>null</tt>)
	 */
	public CircleCardsDisplayPanel(CardDisplayer aCardDisplayer,
		int aPlaceCount, Collection<? extends PosCard> aCards)
	{
		super(aCardDisplayer, aPlaceCount, aCards);
		
		setPreferredSize(new Dimension(Integer.MAX_VALUE, Integer.MAX_VALUE));
	}
	
	/**
	 * Crée le panel avec une liste préétablie de cartes à afficher
	 * @param aCardDisplayer Displayer de cartes
	 * @param aCards Liste des cartes à afficher (peut être <tt>null</tt>)
	 */
	public CircleCardsDisplayPanel(CardDisplayer aCardDisplayer,
		Collection<? extends PosCard> aCards)
	{
		super(aCardDisplayer, aCards);
		
		setPreferredSize(new Dimension(Integer.MAX_VALUE, Integer.MAX_VALUE));
	}
	
	/**
	 * Crée le panel
	 * @param aCardDisplayer Displayer de cartes
	 * @param aPlaceCount Nombre de places accordées aux cartes (> 0)
	 */
	public CircleCardsDisplayPanel(CardDisplayer aCardDisplayer,
		int aPlaceCount)
	{
		super(aCardDisplayer, aPlaceCount);
		
		setPreferredSize(new Dimension(Integer.MAX_VALUE, Integer.MAX_VALUE));
	}
	
	/**
	 * Crée le panel
	 * @param aCardDisplayer Displayer de cartes
	 */
	public CircleCardsDisplayPanel(CardDisplayer aCardDisplayer)
	{
		super(aCardDisplayer);
		
		setPreferredSize(new Dimension(Integer.MAX_VALUE, Integer.MAX_VALUE));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected Dimension computeDisplayDimension()
	{
		return new Dimension(getWidth() - 20, getHeight() - 20);
	}
	
	/**
	 * Calcule l'angle de l'orientation à partir du centre d'une carte
	 * <p>
	 * L'angle retourné est mesuré en radians, et est orienté selon le cercle
	 * trigonométrique usuel, c'est-à-dire 0 rad à droite, et révolution dans
	 * le sens trigonométrique.
	 * </p>
	 * @param index Index de la carte
	 * @return Angle de l'orientation de cette carte
	 */
	protected double computeCardAngle(int index)
	{
		return -Math.PI/2.0 -
			(2*Math.PI * index / getPlaceCount());
	}
	
	/**
	 * Calcule la position du centre d'un objet
	 * @param displayDimension Dimension d'affichage
	 * @param objectWidth Largeur de l'objet
	 * @param objectHeight Hauteur de l'objet
	 * @param index Index dans le cercle
	 * @return Position du centre de l'objet
	 */
	protected Point getObjectCenterPosition(Dimension displayDimension,
		int objectWidth, int objectHeight, int index)
	{
		double centerX = displayDimension.width / 2;
		double centerY = displayDimension.height / 2;
		double xRadius = (displayDimension.width - objectWidth) / 2;
		double yRadius = (displayDimension.height - objectHeight) / 2;
		
		double angle = computeCardAngle(index);
		double x = centerX + xRadius * Math.cos(angle);
		double y = centerY - yRadius * Math.sin(angle);
		
		return new Point((int) x, (int) y);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected Point getCardPosition(Dimension displayDimension, int index)
	{
		Point cardCenterPos = getObjectCenterPosition(displayDimension,
			cardWidth, cardHeight, index);
		
		return new Point(cardCenterPos.x - cardWidth/2,
			cardCenterPos.y - cardHeight/2);
	}
}
