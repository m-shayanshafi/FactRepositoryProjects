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

import java.awt.Frame;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;

import napsack.Napsack;

public class AboutDialog extends SplashDialog {
	private final static String TITLE = "About";

	public AboutDialog(final Frame owner_) {
		super(owner_, TITLE, false, new AboutPanel());

		final JButton closeButton_ = ((AboutPanel) getRightComponent()).getCloseButtons().getButton(AboutPanel.CLOSE_BUTTON);

		setResizable(false);
		setDefaultCloseOperation(HIDE_ON_CLOSE);

		getRootPane().setDefaultButton(closeButton_);

		closeButton_.addActionListener(new ActionListener() {
			public void actionPerformed(final ActionEvent actionEvent_) {
				setVisible(false);
			}
		});
	}
}

