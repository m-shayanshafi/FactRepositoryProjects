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
package sjrd.tricktakinggame.gui.util;

import java.util.*;
import java.io.*;
import java.net.*;
import java.awt.*;

import javax.imageio.*;

import sjrd.tricktakinggame.cards.*;

/**
 * Displayer de cartes
 * @author sjrd
 */
public class CardDisplayer
{
	/**
	 * Format des fichiers des cartes
	 */
	public static final String frontFileFormat = "images/front/%s/%s.png";
	
	/**
	 * Ensemble de cartes par défaut
	 */
	public static final String defaultCardSet = "simple";
	
	/**
	 * Format des fichiers des dos de cartes
	 */
	public static final String backFileFormat = "images/back/%s.png";
	
	/**
	 * Largeur d'une carte
	 */
	public static final int cardWidth = 80;
	
	/**
	 * Hauteur d'une carte
	 */
	public static final int cardHeight = 113;
	
	/**
	 * Dimension d'une carte
	 */
	public static final Dimension cardDimension =
		new Dimension(cardWidth, cardHeight);
	
	/**
	 * Distance dont séparer les cartes quand elles sont superposées
	 */
	public static final int overlayWidth = 20;
	
	/**
	 * Ensemble de cartes
	 */
	private String cardSet = defaultCardSet;
	
	/**
	 * Images chargées et en conservées en cache
	 */
	private Map<String, Image> cachedImages = new HashMap<String, Image>();
	
	/**
	 * Crée un displayer de cartes
	 * @param aCardSet Ensemble de cartes à utiliser
	 */
	public CardDisplayer(String aCardSet)
	{
		super();
		
		cardSet = aCardSet;
	}
	
	/**
	 * Crée un displayer avec l'ensemble de cartes par défaut
	 */
	public CardDisplayer()
	{
		this(defaultCardSet);
	}
	
	/**
	 * Nom de fichier correspondant à une carte
	 * @param card Carte
	 * @return Nom de fichier correspondant à la carte
	 */
	public String getCardFileName(Card card)
	{
		if (card == null)
			return String.format(backFileFormat, cardSet);
		else
			return String.format(frontFileFormat, cardSet, card.getDrawID());
	}
	
	/**
	 * Charge l'image d'une carte
	 * <p>
	 * Pour des raisons de performance, cette méthode travaille avec un cache.
	 * Des appels successifs pour la même carte ne seront pas pénalisant.
	 * </p>
	 * @param card Carte
	 * @return Image de la carte chargée
	 * @throws IOException Erreur au chargement de l'image
	 */
	public Image loadImage(Card card) throws IOException
	{
		String drawID;
		if (card == null)
			drawID = "";
		else
			drawID = card.getDrawID();
		
		Image cardImage = cachedImages.get(drawID);
		
		if (cardImage == null)
		{
			String fileName = getCardFileName(card);
			URL imageURL = getClass().getResource(fileName);

			if (imageURL != null)
				//cardImage = Toolkit.getDefaultToolkit().getImage(imageURL);
				cardImage = ImageIO.read(imageURL);
			
			if (cardImage == null)
				throw new IOException("Can't read file " + fileName);
			cachedImages.put(drawID, cardImage);
		}
		
		return cardImage;
	}
	
	/**
	 * Dessine une carte sur un canevas
	 * <p>
	 * <b>Attention :</b> Cette méthode ignore toute erreur de lecture du
	 * fichier !
	 * </p>
	 * @param g Canevas
	 * @param card Carte à dessiner
	 * @param x Position x
	 * @param y Position y
	 */
	public void drawCard(Graphics g, Card card, int x, int y)
	{
		try
		{
			Image cardImage = loadImage(card);
			
			g.drawImage(cardImage, x, y, cardWidth, cardHeight, null);
		}
		catch (IOException error)
		{
			error.printStackTrace();
			return;
		}
	}
}
