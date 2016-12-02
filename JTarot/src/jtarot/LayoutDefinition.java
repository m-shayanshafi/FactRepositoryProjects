/*
 *  :tabSize=4:indentSize=2:noTabs=true:
 *  :folding=explicit:collapseFolds=1:
 *
 *  Copyright (C) 2006 Free Tarot Foundation.  This program is free
 *  software; you can redistribute it and/or modify it under the terms of the
 *  GNU General Public License as published by the Free Software Foundation;
 *  either version 2 of the License, or (at your option) any later version. This
 *  program is distributed in the hope that it will be useful, but WITHOUT ANY
 *  WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 *  FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 *  details. You should have received a copy of the GNU General Public License
 *  along with this program; if not, write to the Free Software Foundation,
 *  Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA For more
 *  information, surf to www.lazy8.nu/tarot or email tarot@lazy8.nu
 */
 
package jtarot;

import java.awt.*;
import javax.swing.*;
public class LayoutDefinition {
	public Dimension preferredsize;
	public double[] layoutxy;
	public int[] showHiddenOrder;
	public Dimension printoutsize;
	public int[] printoutgraphicxy;
	public String keyName;

	public String LayoutNameTranslated = "";
	public String LayoutDescription = "";
	public String[] cardlayoutMeanings;
	public String[] cardlayoutNames;


	LayoutDefinition( double[] layoutxy, int[] showHiddenOrder, Dimension prefsize,
			Dimension printoutsize, int[] printoutgraphicxy, String keyName ) {
		this.layoutxy = layoutxy;
		this.showHiddenOrder = showHiddenOrder;
		this.preferredsize = prefsize;
		this.printoutsize = printoutsize;
		this.printoutgraphicxy = printoutgraphicxy;
		this.keyName = keyName;

		cardlayoutMeanings = new String[layoutxy.length / 3];
		cardlayoutNames = new String[layoutxy.length / 3];
		for ( int i = 0; i < cardlayoutNames.length; i++ ) {
			cardlayoutNames[i] = "";
			cardlayoutMeanings[i] = "";
		}
	}
}

