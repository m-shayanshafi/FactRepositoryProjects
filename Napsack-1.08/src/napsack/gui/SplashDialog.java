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

import java.awt.Container;
import java.awt.Frame;

import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JLabel;

public abstract class SplashDialog extends NapsackDialog {
	private final JComponent rightComponent;

	public SplashDialog(final Frame owner_, final String title_, final boolean modal_, final JComponent rightComponent_) {
		super(owner_, title_, modal_);
		final Container contentPane_ = getContentPane();

		contentPane_.setLayout(new BoxLayout(contentPane_, BoxLayout.X_AXIS));

		final JLabel leftComponent_ = new JLabel(new ImageIcon(ClassLoader.getSystemResource("splash-napsack.gif")));
		rightComponent = rightComponent_;

		contentPane_.add(leftComponent_);
		contentPane_.add(rightComponent);

		pack();
	}

	final JComponent getRightComponent() {
		return rightComponent;
	}
}

