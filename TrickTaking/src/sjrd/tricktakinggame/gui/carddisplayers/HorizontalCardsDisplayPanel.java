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
 * Panel affichant une liste de cartes horizontalement
 * @author sjrd
 */
public class HorizontalCardsDisplayPanel extends CardsDisplayPanel
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
	public HorizontalCardsDisplayPanel(CardDisplayer aCardDisplayer,
		int aPlaceCount, Collection<? extends PosCard> aCards)
	{
		super(aCardDisplayer, aPlaceCount, aCards);
	}
	
	/**
	 * Crée le panel avec une liste préétablie de cartes à afficher
	 * @param aCardDisplayer Displayer de cartes
	 * @param aCards Liste des cartes à afficher (peut être <tt>null</tt>)
	 */
	public HorizontalCardsDisplayPanel(CardDisplayer aCardDisplayer,
		Collection<? extends PosCard> aCards)
	{
		super(aCardDisplayer, aCards);
	}
	
	/**
	 * Crée le panel
	 * @param aCardDisplayer Displayer de cartes
	 * @param aPlaceCount Nombre de places accordées aux cartes (> 0)
	 */
	public HorizontalCardsDisplayPanel(CardDisplayer aCardDisplayer,
		int aPlaceCount)
	{
		super(aCardDisplayer, aPlaceCount);
	}
	
	/**
	 * Crée le panel
	 * @param aCardDisplayer Displayer de cartes
	 */
	public HorizontalCardsDisplayPanel(CardDisplayer aCardDisplayer)
	{
		super(aCardDisplayer);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	protected Dimension computeDisplayDimension()
	{
		int width = ((getPlaceCount()-1) * overlayWidth) + cardWidth;
		int height = cardHeight;
		
		return new Dimension(width, height);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void contentsUpdated()
	{
		Dimension dimension = computeDisplayDimension();
		dimension.setSize(dimension.width+20, dimension.height+20);
		setPreferredSize(dimension);
		
		super.contentsUpdated();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected Point getCardPosition(Dimension displayDimension, int index)
	{
		return new Point(index * overlayWidth, 0);
	}
}
