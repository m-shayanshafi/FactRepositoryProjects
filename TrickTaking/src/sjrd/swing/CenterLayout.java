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
package sjrd.swing;

import java.awt.*;

/**
 * Layout manager qui place l'<i>unique</i> composant au centre de son parent
 * <p>
 * Si ce layout manager est utilisé avec plus d'un composant dans le conteneur,
 * le résultat est imprévisible.
 * </p>
 * @author sjrd
 */
public class CenterLayout implements LayoutManager
{
	/**
	 * {@inheritDoc}
	 */
	public void addLayoutComponent(String name, Component comp)
	{
	}

	/**
	 * {@inheritDoc}
	 */
	public void removeLayoutComponent(Component comp)
	{
	}

	/**
	 * {@inheritDoc}
	 */
	public Dimension minimumLayoutSize(Container parent)
	{
		Insets insets = parent.getInsets();
		int width = insets.left + insets.right;
		int height = insets.top + insets.bottom;

		if (parent.getComponentCount() != 1)
			return new Dimension(width, height);
		
		Dimension childMinSize = parent.getComponent(0).getMinimumSize();
		
		return new Dimension(childMinSize.width + width,
			childMinSize.height + height);
	}

	/**
	 * {@inheritDoc}
	 */
	public Dimension preferredLayoutSize(Container parent)
	{
		Insets insets = parent.getInsets();
		int width = insets.left + insets.right;
		int height = insets.top + insets.bottom;

		if (parent.getComponentCount() != 1)
			return new Dimension(width, height);
		
		Dimension childPrefSize = parent.getComponent(0).getPreferredSize();
		
		return new Dimension(childPrefSize.width + width,
			childPrefSize.height + height);
	}

	/**
	 * {@inheritDoc}
	 */
	public void layoutContainer(Container parent)
	{
		if (parent.getComponentCount() != 1)
			return;
		Component child = parent.getComponent(0);
		
		Insets insets = parent.getInsets();
		int width = insets.left + insets.right;
		int height = insets.top + insets.bottom;
		
		int innerWidth = parent.getWidth() - width;
		int innerHeight = parent.getHeight() - height;
		
		Dimension childSize = child.getPreferredSize();
		if (innerWidth < childSize.width)
			childSize.width = innerWidth;
		if (innerHeight < childSize.height)
			childSize.height = innerHeight;
		
		child.setSize(childSize);
		
		int posX = insets.left + (innerWidth - childSize.width) / 2;
		int posY = insets.top + (innerHeight - childSize.height) / 2;
		child.setLocation(posX, posY);
	}
}
