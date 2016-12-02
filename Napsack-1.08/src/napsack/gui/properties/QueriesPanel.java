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
import napsack.util.properties.PropertyException;

public class QueriesPanel extends PropertiesPanel {
	private final QueryStringsPanel queryStringsPanel;
	private final QueryCriteriaPanel queryCriteriaPanel;

	public QueriesPanel() {
		setLayout(new BoxLayout(this, BoxLayout.X_AXIS));

		queryStringsPanel = new QueryStringsPanel();
		queryCriteriaPanel = new QueryCriteriaPanel();

		GuiUtils.setAllWidths(queryCriteriaPanel, queryCriteriaPanel.getPreferredSize().width);

      queryStringsPanel.setAlignmentY(Component.TOP_ALIGNMENT);
      queryCriteriaPanel.setAlignmentY(Component.TOP_ALIGNMENT);

		add(queryStringsPanel);
		add(Box.createHorizontalStrut(11));
		add(queryCriteriaPanel);
	}

	private QueryCriteriaPanel getQueryCriteriaPanel() {
		return queryCriteriaPanel;
	}

	private QueryStringsPanel getQueryStringsPanel() {
		return queryStringsPanel;
	}

	public void setProperties() {
		getQueryStringsPanel().setProperties();
		getQueryCriteriaPanel().setProperties();
	}

	public void validateProperties() throws PropertyException {
		final PropertiesPanel queryStringsPanel_ = getQueryStringsPanel();

		try {
			queryStringsPanel_.validateProperties();
		} catch (PropertyException propertyException_) {
			setInvalidComponent(queryStringsPanel_.getInvalidComponent());
			throw propertyException_;
		}

		final PropertiesPanel queryCriteriaPanel_ = getQueryCriteriaPanel();

		try {
			queryCriteriaPanel_.validateProperties();
		} catch (PropertyException propertyException_) {
			setInvalidComponent(queryCriteriaPanel_.getInvalidComponent());
			throw propertyException_;
		}
	}
}

