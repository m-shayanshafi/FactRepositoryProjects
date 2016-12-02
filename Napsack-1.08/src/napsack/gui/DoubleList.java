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

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.JComponent;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

public class DoubleList extends JPanel {
	private final static float WIDTH_SCALE = 1.5f;
	private final static float HEIGHT_SCALE = 2;

	private final JList leftList;
	private final JComponent centerComponent;
	private final JList rightList;

	public DoubleList(final JComponent centerComponent_) {
		setLayout(new BoxLayout(this, BoxLayout.X_AXIS));

		final Dimension minCenterSize_ = centerComponent_.getMinimumSize();
		final Dimension preferredCenterSize_ = centerComponent_.getPreferredSize();

		final Dimension preferredScrollPaneSize_ = new Dimension((int) (preferredCenterSize_.width * WIDTH_SCALE), (int) (preferredCenterSize_.height * HEIGHT_SCALE));

		leftList = new JList(new DefaultListModel());
		centerComponent = centerComponent_;
		rightList = new JList(new DefaultListModel());

		final JScrollPane leftScrollPane_ = new JScrollPane(leftList) {
			public Dimension getPreferredSize() {
				return preferredScrollPaneSize_;
			}
		};

		final JScrollPane rightScrollPane_ = new JScrollPane(rightList) {
			public Dimension getPreferredSize() {
				return preferredScrollPaneSize_;
			}
		};

		leftScrollPane_.setAlignmentY(Component.CENTER_ALIGNMENT);
		centerComponent.setAlignmentY(Component.CENTER_ALIGNMENT);
		rightScrollPane_.setAlignmentY(Component.CENTER_ALIGNMENT);

		add(leftScrollPane_);
		add(Box.createHorizontalStrut(5));
		add(centerComponent);
		add(Box.createHorizontalStrut(5));
		add(rightScrollPane_);
	}

	public JComponent getCenterComponent() {
		return centerComponent;
	}

	public JList getLeftList() {
		return leftList;
	}

	public JList getRightList() {
		return rightList;
	}
}

