// Written by Chris Redekop <waveform@users.sourceforge.net>
// Released under the conditions of the GPL (See below).
//
// Thanks to Travis Whitton for the Gnap Fetch code used by Napsack.
//
// Also thanks to Radovan Garabik for the pynap code used by Napsack.
//
// Napsack - a specialized client for launching cross-server Napster queries.
// Copyright (C) 2000-2002 Chris Redekop
// 
// This program is free software; you can redistribute it and/or
// modify it under the terms of the GNU General Public License
// as published by the Free Software Foundation; either version 2
// of the License, or (at your option) any later version.
// 
// This program is distributed in the hope that it will be useful,
// but WITHOUT ANY WARRANTY; without even the implied warranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
// GNU General Public License for more details.
//
// You should have received a copy of the GNU General Public License
// along with this program; if not, write to the Free Software
// Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA

package napsack.gui;

import javax.swing.JMenu;
import javax.swing.event.MenuEvent;
import javax.swing.event.MenuListener;

public abstract class NapsackMenu extends JMenu {
	private final MainFrame mainFrame;

	public NapsackMenu(final MainFrame mainFrame_, final String name_, final char mnemonic_) {
		super(name_);
		setMnemonic(mnemonic_);

		mainFrame = mainFrame_;

		addMenuListener(new MenuListener() {
			public void menuCanceled(final MenuEvent menuEvent_) {
			}
			
			public void menuDeselected(final MenuEvent menuEvent_) {
			}
			
			public void menuSelected(final MenuEvent menuEvent_) {
				buildMenu();
				removeMenuListener(this);
			}
		});
	}

	protected abstract void buildMenu();

	protected MainFrame getMainFrame() {
		return mainFrame;
	}
}

