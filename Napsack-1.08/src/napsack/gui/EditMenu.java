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

import java.lang.reflect.InvocationTargetException;

import javax.swing.JMenuItem;
import javax.swing.SwingUtilities;

import napsack.event.NapigatorEvent;
import napsack.event.NapigatorListener;
import napsack.servers.Napigator;

public class EditMenu extends NapsackMenu implements NapigatorListener {
	private final static String EDIT_MENU = "Edit";
	private final static String REFRESH_ITEM = "Refresh Napigator Data";

	private final static char EDIT_MNEMONIC = 'E';
	private final static char REFRESH_MNEMONIC = 'R';

	private JMenuItem refreshItem;

	public EditMenu(final MainFrame mainFrame_) {
		super(mainFrame_, EDIT_MENU, EDIT_MNEMONIC);

		Napigator.getInstance().addNapigatorListener(this);
	}

	protected void buildMenu() {
		final JMenuItem refreshItem_ = getRefreshItem();
		refreshItem_.setMnemonic(REFRESH_MNEMONIC);

		add(refreshItem_);

		refreshItem_.addActionListener(new RefreshNapigatorListener(getMainFrame()));
	}

	private JMenuItem getRefreshItem() {
		if (refreshItem == null) {
			refreshItem = new JMenuItem(REFRESH_ITEM);
		}

		return refreshItem;
	}

	public void napigatorRefreshed(final NapigatorEvent napigatorEvent_) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				getRefreshItem().setEnabled(true);
			}
		});
	}

	public void napigatorRefreshing(final NapigatorEvent napigatorEvent_) {
      final JMenuItem refreshItem_ = getRefreshItem();

      final Runnable disableItem_ = new Runnable() {
         public void run() {
            refreshItem_.setEnabled(false);
         }
      };

      if (SwingUtilities.isEventDispatchThread()) {
         disableItem_.run();
      } else {
         boolean invoked_ = false;

         while (!invoked_) {
            try {
               SwingUtilities.invokeAndWait(disableItem_);
               invoked_ = true;
            } catch (InterruptedException interruptedException_) {
            } catch (InvocationTargetException invocationTargetException_) {
               throw new RuntimeException("InvocationTargetException");
            }
         }
      }
	}
}

