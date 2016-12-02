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

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;

import entities.Konstanten;

/**
 * Intro screen on startup
 * @author johannes
 *
 */
public class IntroPanel extends JPanel{
	
	public final int HEIGHT = 350;
	public final int WIDTH = 230;
	
	public IntroPanel(){
		
		this.setBackground(Color.BLACK);
		
		JLabel bild=new JLabel(new ImageIcon(ClassLoader.getSystemResource(Bilder.WELCOME)));
		JTextArea txt=new JTextArea();
		
		txt.setBackground(Color.BLACK);
		txt.setForeground(Color.LIGHT_GRAY);
		txt.setEditable(false);
		txt.setText(Konstanten.NAME+" "+Konstanten.VERSION+"\n"+Konstanten.TITEL);
		
		this.add(bild);
		this.add(txt);

	}


}
