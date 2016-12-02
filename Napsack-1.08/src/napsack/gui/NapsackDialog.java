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

import javax.swing.JDialog;

public class NapsackDialog extends JDialog {
	public NapsackDialog(final Frame owner_, final String title_, final boolean modal_) {
		super(owner_, title_, modal_);
	}

	public void setVisible(final boolean visible_) {
		if (visible_) {
			final Rectangle parentBounds_ = getOwner().getBounds();
			final Dimension size_ = getSize();

			setLocation(Math.max(0, parentBounds_.x + (int) ((parentBounds_.width - size_.width) / 2.0f)), Math.max(0, parentBounds_.y + (int) ((parentBounds_.height - size_.height) / 2.0f)));
		}

		super.setVisible(visible_);
	}
}

