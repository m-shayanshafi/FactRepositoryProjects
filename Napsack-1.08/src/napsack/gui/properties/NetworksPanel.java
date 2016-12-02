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

package napsack.gui.properties;

import java.awt.Component;

import javax.swing.Box;
import javax.swing.BoxLayout;

import napsack.gui.GuiUtils;
import napsack.gui.MainFrame;
import napsack.util.properties.PropertyException;

public class NetworksPanel extends PropertiesPanel {
	private final ExcludedNetworksPanel excludedNetworksPanel;
	private final NetworkCriteriaPanel networkCriteriaPanel;

	public NetworksPanel(final MainFrame mainFrame_) {
		setLayout(new BoxLayout(this, BoxLayout.X_AXIS));

		excludedNetworksPanel = new ExcludedNetworksPanel(mainFrame_);
		networkCriteriaPanel = new NetworkCriteriaPanel();

		GuiUtils.setAllWidths(networkCriteriaPanel, networkCriteriaPanel.getPreferredSize().width);

		excludedNetworksPanel.setAlignmentY(Component.TOP_ALIGNMENT);
		networkCriteriaPanel.setAlignmentY(Component.TOP_ALIGNMENT);

		add(excludedNetworksPanel);
		add(Box.createHorizontalStrut(11));
		add(networkCriteriaPanel);
	}

	public ExcludedNetworksPanel getExcludedNetworksPanel() {
		return excludedNetworksPanel;
	}

	public NetworkCriteriaPanel getNetworkCriteriaPanel() {
		return networkCriteriaPanel;
	}

	public void setProperties() {
		getExcludedNetworksPanel().setProperties();
		getNetworkCriteriaPanel().setProperties();
	}

	public void validateProperties() throws PropertyException {
		final PropertiesPanel excludedNetworksPanel_ = getExcludedNetworksPanel();

		try {
			excludedNetworksPanel_.validateProperties();
		} catch (PropertyException propertyException_) {
			setInvalidComponent(excludedNetworksPanel_.getInvalidComponent());
			throw propertyException_; 
		}

		final PropertiesPanel networkCriteriaPanel_ = getNetworkCriteriaPanel();

		try {
			networkCriteriaPanel_.validateProperties();
		} catch (PropertyException propertyException_) {
			setInvalidComponent(networkCriteriaPanel_.getInvalidComponent());
			throw propertyException_; 
		}
	}
}

