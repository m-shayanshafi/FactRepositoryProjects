/* Arena.java
   This abstract class shows the field, players, and ball.  It should be
   instantiated with either the Field class or the Rink class.
   
   Copyright (C) 2001  Jefferson Montgomery

   This program is free software; you can redistribute it and/or modify
   it under the terms of the GNU General Public License as published by
   the Free Software Foundation; either version 2 of the License, or
   (at your option) any later version.

   This program is distributed in the hope that it will be useful,
   but WITHOUT ANY WARRANTY; without even the implied warranty of
   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
   GNU General Public License for more details.

   You should have received a copy of the GNU General Public License
   along with this program; if not, write to the 
   Free Software Foundation, Inc., 
   59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
*/

package soccer.client.view;

import java.awt.*;
import javax.swing.JPanel;

import soccer.client.World;
import soccer.common.Vector2d;

public abstract class Arena extends JPanel {
	protected World world = null;

	public abstract void user2soccer(Vector2d p);
	public abstract void soccer2user(Vector2d p);

	// set focus traversable to true, this lets the user tab to this component.
	public boolean isFocusable() {
		return true;
	}

	public void setWorld(World world) {
		this.world = world;
	}

	public World getWorld() {
		return world;
	}

	// set font size to be equal as the player size
	protected void setFont(Graphics2D g2, int space, int minFontSize) {
		boolean fontFits = false;
		Font font = g2.getFont();
		FontMetrics fontMetrics = g2.getFontMetrics();
		int size = font.getSize();
		String name = font.getName();
		int style = font.getStyle();

		while (!fontFits) {
			if (fontMetrics.getHeight() <= space) {
				fontFits = true;
			} else {
				if (size <= minFontSize) {
					fontFits = true;
				} else {
					g2.setFont(font = new Font(name, style, --size));
					fontMetrics = g2.getFontMetrics();
				}
			}
		}

		return;
	}
}
