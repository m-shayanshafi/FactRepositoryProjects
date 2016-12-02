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
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import napsack.gui.properties.QueriesPanel;
import napsack.gui.properties.NetworksPanel;
import napsack.gui.properties.PropertiesPanel;
import napsack.util.properties.NapsackProperties;
import napsack.util.properties.PropertyException;

public class SearchPanel extends JPanel {
	final static String SEARCH_BUTTON = "Search";
	final static String STOP_BUTTON = "Stop";
	final static String QUERIES_PANEL = "Queries";	
	final static String NETWORKS_PANEL = "Networks";	

	private final MainFrame mainFrame;
	private final JTabbedPane searchPane;
	private final QueriesPanel queriesPanel;
	private NetworksPanel networksPanel;
	private final CommandButtons commandButtons;

	public SearchPanel(final MainFrame mainFrame_) {
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

		mainFrame = mainFrame_;
		searchPane = new JTabbedPane();
		queriesPanel = new QueriesPanel();
		commandButtons = new HorizontalCommandButtons(new String[] {SEARCH_BUTTON, STOP_BUTTON}, Component.RIGHT_ALIGNMENT);

		final JButton searchButton_ = commandButtons.getButton(SEARCH_BUTTON);
		final JButton cancelButton_ = commandButtons.getButton(STOP_BUTTON);

		queriesPanel.setBorder(BorderFactory.createEmptyBorder(12, 12, 11, 11));
		searchPane.add(QUERIES_PANEL, queriesPanel);

		searchPane.add(NETWORKS_PANEL, new Container());
		
		commandButtons.setBorder(BorderFactory.createEmptyBorder(17, 12, 11, 11));

		add(searchPane);
		add(commandButtons);

		searchPane.addChangeListener(new ChangeListener() {
			public void stateChanged(final ChangeEvent changeEvent_) {
				final NetworksPanel networksPanel_ = getNetworksPanel();
				networksPanel_.setBorder(BorderFactory.createEmptyBorder(12, 12, 11, 11));

				searchPane.setComponentAt(1, networksPanel_);

				searchPane.removeChangeListener(this);
			}
		});

	}

	public CommandButtons getCommandButtons() {
		return commandButtons;
	}

	private MainFrame getMainFrame() {
		return mainFrame;
	}

	public NetworksPanel getNetworksPanel() {
		if (networksPanel == null) {
			networksPanel = new NetworksPanel(getMainFrame());
		}

      return networksPanel;
	}

	public QueriesPanel getQueriesPanel() {
		return queriesPanel;
	}

	private JTabbedPane getSearchPane() {
		return searchPane;
	}

	public void setProperties() {
		getQueriesPanel().setProperties();
		getNetworksPanel().setProperties();
	}

   public void validateProperties() throws PropertyException {
      final PropertiesPanel queriesPanel_ = getQueriesPanel();

      try {
         queriesPanel_.validateProperties();
      } catch (PropertyException propertyException_) {
			if (isShowing()) {
         	getSearchPane().setSelectedComponent(queriesPanel_);
         	queriesPanel_.getInvalidComponent().requestFocus();
			}

         throw propertyException_;
      }

      final PropertiesPanel networksPanel_ = getNetworksPanel();

      try {
         networksPanel_.validateProperties();
      } catch (PropertyException propertyException_) {
			if (isShowing()) {
         	getSearchPane().setSelectedComponent(networksPanel_);
         	networksPanel_.getInvalidComponent().requestFocus();
			}

         throw propertyException_;
      }
   }
}

