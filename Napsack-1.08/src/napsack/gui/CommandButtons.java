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
import java.awt.Dimension;
import java.util.Map;
import java.util.HashMap;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JPanel;

public abstract class CommandButtons extends JPanel {
	protected final static int BUTTON_SPACING = 5;
	protected final static JButton[] BUTTON_ARRAY = new JButton[0];

	private static void validateAlignment(final int axis_, final float alignment_) {
		switch (axis_) {
			case BoxLayout.X_AXIS:
				if (alignment_ != Component.LEFT_ALIGNMENT && alignment_ != Component.RIGHT_ALIGNMENT && alignment_ != Component.CENTER_ALIGNMENT) {
					throw new IllegalArgumentException("Invalid alignment argument.");
				}
				break;
			case BoxLayout.Y_AXIS:
				if (alignment_ != Component.TOP_ALIGNMENT && alignment_ != Component.BOTTOM_ALIGNMENT && alignment_ != Component.CENTER_ALIGNMENT) {
					throw new IllegalArgumentException("Invalid alignment argument.");
				}
		}
	}

	private final Map buttons;

	protected CommandButtons(final String[] buttonNames_, final int axis_, final float alignment_) {
		validateAlignment(axis_, alignment_);
		setLayout(new BoxLayout(this, axis_));

		buttons = new HashMap((int) Math.ceil(buttonNames_.length / 0.75f), 0.75f);
		for (int i = 0; i < buttonNames_.length; ++i) {
			final JButton button_ = new JButton(buttonNames_[i]);
			buttons.put(buttonNames_[i], button_);
		}

		sizeButtonsUniformly();
		layoutButtons(buttonNames_, alignment_);
	}

	public JButton getButton(final String buttonName_) {
		return (JButton) getButtons().get(buttonName_);
	}

	protected abstract void layoutButtons(final String[] buttonNames_, final float alignment_);

	private void sizeButtonsUniformly() {
		final JButton[] buttons_ = (JButton[]) getButtons().values().toArray(BUTTON_ARRAY);
		int maxWidth_ = 0;
		int maxHeight_ = 0;

		for (int i = 0; i < buttons_.length; ++i) {
			final Dimension preferredSize_ = buttons_[i].getPreferredSize();

			maxWidth_ = Math.max(maxWidth_, preferredSize_.width);
			maxHeight_ = Math.max(maxHeight_, preferredSize_.height);
		}

		final Dimension commonSize_ = new Dimension(maxWidth_, maxHeight_);

		for (int i = 0; i < buttons_.length; ++i) {
			GuiUtils.setAllSizes(buttons_[i], commonSize_);
		}
	}

	protected Map getButtons() {
		return buttons;
	}
}

