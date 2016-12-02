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

import java.awt.Component;
import java.util.Map;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;

public class HorizontalCommandButtons extends CommandButtons {
	public HorizontalCommandButtons(final String[] buttonNames_) {
		this(buttonNames_, Component.CENTER_ALIGNMENT); 
	}

	public HorizontalCommandButtons(final String[] buttonNames_, final float alignment_) {
		super(buttonNames_, BoxLayout.X_AXIS, alignment_);

		if (buttonNames_.length > 0) {
			GuiUtils.setMaxHeight(this, ((JButton) getButtons().get(buttonNames_[0])).getMaximumSize().height);
		}
	}

	protected void layoutButtons(final String[] buttonNames_, final float alignment_) {
		final Map buttons_ = getButtons();

		if (alignment_ == Component.RIGHT_ALIGNMENT || alignment_ == Component.CENTER_ALIGNMENT) {
			add(Box.createHorizontalGlue());
		}

		if (buttonNames_.length > 0) {
			add((JButton) buttons_.get(buttonNames_[0]));

			for (int i = 1; i < buttonNames_.length; ++i) {
				add(Box.createHorizontalStrut(BUTTON_SPACING));
				add((JButton) buttons_.get(buttonNames_[i]));
			}
		}

		if (alignment_ == Component.LEFT_ALIGNMENT || alignment_ == Component.CENTER_ALIGNMENT) {
			add(Box.createHorizontalGlue());
		}
	}
}

