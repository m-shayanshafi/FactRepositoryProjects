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
import java.awt.Font;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import napsack.Napsack;

public class AboutPanel extends JPanel {
	public final static String CLOSE_BUTTON = "Close";

	private final static String COPYRIGHT = "Copyright (C) 2000, 2001 Chris Redekop";
	private final static String INFO_1 = "A specialized multi-threaded client for";
	private final static String INFO_2 = "broadcasting Napster queries across";
	private final static String INFO_3 = "multiple servers.";
	private final static String URL = "http://sourceforge.net/projects/napsack";

	private final CommandButtons closeButtons;

	public AboutPanel() {
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

		final JPanel labelPanel_ = new JPanel();
		final JLabel titleLabel_ = new JLabel(Napsack.APPLICATION_IDENTIFIER, SwingConstants.CENTER);
		final Font titleFont_ = titleLabel_.getFont();
		final JLabel copyrightLabel_ = new JLabel(COPYRIGHT, SwingConstants.LEFT);
		final JLabel info1Label_ = new JLabel(INFO_1, SwingConstants.LEFT);
		final JLabel info2Label_ = new JLabel(INFO_2, SwingConstants.LEFT);
		final JLabel info3Label_ = new JLabel(INFO_3, SwingConstants.LEFT);
		final JLabel urlLabel_ = new JLabel(URL, SwingConstants.LEFT);
		closeButtons = new HorizontalCommandButtons(new String[] {CLOSE_BUTTON}, Component.RIGHT_ALIGNMENT);

		GuiUtils.setAllWidthsToMax(new JLabel[] {titleLabel_, copyrightLabel_, info1Label_, info2Label_, info3Label_, urlLabel_});
		titleLabel_.setFont(new Font(titleFont_.getName(), titleFont_.getStyle(), (int) Math.ceil(titleFont_.getSize() * 1.5f)));

		urlLabel_.setBackground(java.awt.Color.blue);
		labelPanel_.setLayout(new BoxLayout(labelPanel_, BoxLayout.Y_AXIS));
		labelPanel_.add(Box.createVerticalGlue());
		labelPanel_.add(titleLabel_);
		labelPanel_.add(Box.createVerticalStrut(12));
		labelPanel_.add(copyrightLabel_);
		labelPanel_.add(Box.createVerticalStrut(12));
		labelPanel_.add(info1Label_);
		labelPanel_.add(Box.createVerticalStrut(6));
		labelPanel_.add(info2Label_);
		labelPanel_.add(Box.createVerticalStrut(6));
		labelPanel_.add(info3Label_);
		labelPanel_.add(Box.createVerticalStrut(12));
		labelPanel_.add(urlLabel_);
		labelPanel_.add(Box.createVerticalGlue());
		labelPanel_.setBorder(BorderFactory.createEmptyBorder(12, 12, 18, 12));

		closeButtons.setBorder(BorderFactory.createEmptyBorder(0, 11, 11, 11));

		labelPanel_.setAlignmentX(Component.LEFT_ALIGNMENT);
		closeButtons.setAlignmentX(Component.LEFT_ALIGNMENT);

		add(labelPanel_);
		add(closeButtons);
	}

	public CommandButtons getCloseButtons() {
		return closeButtons;
	}
}

