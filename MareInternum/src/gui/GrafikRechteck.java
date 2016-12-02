/*	 
*    This file is part of Mare Internum.
*
*	 Copyright (C) 2008,2009  Johannes Hoechstaedter
*
*    Mare Internum is free software: you can redistribute it and/or modify
*    it under the terms of the GNU General Public License as published by
*    the Free Software Foundation, either version 3 of the License, or
*    (at your option) any later version.
*
*    Mare Internum is distributed in the hope that it will be useful,
*    but WITHOUT ANY WARRANTY; without even the implied warranty of
*    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
*    GNU General Public License for more details.
*
*    You should have received a copy of the GNU General Public License
*    along with Mare Internum.  If not, see <http://www.gnu.org/licenses/>.
*
*/

package gui;

import java.awt.Color;
import java.awt.Graphics;

import javax.swing.JComponent;

/**
 * Class paints a rectangle.
 * 
 * @author johannes
 *
 */
public class GrafikRechteck extends JComponent{

	/**
	 * width of rectangle
	 */
	private int width = 0;
	
	/**
	 * height of rectangle
	 */
	private int height = 0;
	
	/**
	 * Color of border of rectangle
	 */
	private Color color = Color.LIGHT_GRAY;
	
	/**
	 * returns the color of border of rectangle
	 * @return
	 */
	public Color getFarbe() {
		return color;
	}
	
	/**
	 * sets color of border of rectangle
	 * @param farbe
	 */
	public void setFarbe(Color c) {
		this.color = c;
		this.repaint();
	}

	/**
	 * constructor
	 */
	public GrafikRechteck()
	{}
	
	@Override
	public void paintComponent(Graphics g){
		g.setColor(color);
		g.drawLine(0, 0, width, 0);
		g.drawLine(width, 0, width, height);
		g.drawLine(width, height, 0, height);
		g.drawLine(0, height, 0, 0);
		setSize(width + 10, height + 10);
	}
	
	/**
	 * sets width of rectangle
	 * @param fwidth
	 */
	public void setRectangleWidth(int width) {
		this.width =  width;
		this.repaint();
	}
	
	/**
	 * sets height of rectangle
	 * @param fheight
	 */
	public void setRectangleHeight(int height) {
		this.height = height;
		this.repaint();
	}
}
