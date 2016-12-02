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

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import javax.swing.SwingUtilities;

import napsack.servers.Napigator;

public class RefreshNapigatorListener implements ActionListener {
	private final MainFrame mainFrame;

	public RefreshNapigatorListener(final MainFrame mainFrame_) {
		mainFrame = mainFrame_;
	}

	public void actionPerformed(final ActionEvent actionEvent_) {
		refreshNetworks();
	}

	public MainFrame getMainFrame() {
		return mainFrame;
	}

	public void refreshNetworks() {
      new Thread(new Runnable() {
         public void run() {
            try {
               Napigator.getInstance().refreshNapsterServices();
            } catch (IOException ioException_) {
					final String exceptionMessage_ = ioException_.getMessage();

					SwingUtilities.invokeLater(new Runnable() {
						public void run() {
               		getMainFrame().showWarningMessage(exceptionMessage_);
						}
					});	
            }
         }
      }).start();
	}
}

