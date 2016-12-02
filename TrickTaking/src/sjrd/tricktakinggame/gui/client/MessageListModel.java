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

import java.util.*;

import javax.swing.*;

import sjrd.tricktakinggame.remotable.*;

/**
 * Modèle de liste de messages
 * @author sjrd
 */
public class MessageListModel extends AbstractListModel
{
	/**
	 * ID de sérialisation
	 */
	private static final long serialVersionUID = 1;
	
	/**
	 * Messages reçus
	 */
	private List<Message> messages = new ArrayList<Message>();

	/**
	 * {@inheritDoc}
	 */
	public Message getElementAt(int index)
	{
		return messages.get(index);
	}

	/**
	 * {@inheritDoc}
	 */
	public int getSize()
	{
		return messages.size();
	}
	
	/**
	 * Efface tous les messages de la liste
	 */
	public void clear()
	{
		int oldSize = getSize();
		messages.clear();
		fireIntervalRemoved(this, 0, oldSize-1);
	}
	
	/**
	 * Ajoute un message à la liste
	 * @param message Message à ajouter
	 */
	public void add(Message message)
	{
		int oldSize = getSize();
		messages.add(message);
		fireIntervalAdded(this, oldSize, oldSize);
	}
}
