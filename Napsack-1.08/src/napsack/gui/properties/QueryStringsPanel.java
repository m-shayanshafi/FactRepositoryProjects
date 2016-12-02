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
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.JLabel;

import napsack.gui.EditableList;
import napsack.util.StringUtils;
import napsack.util.properties.DelimiterProperty;
import napsack.util.properties.Property;
import napsack.util.properties.PropertyException;
import napsack.util.properties.QueriesProperty;

public class QueryStringsPanel extends PropertiesPanel {
	private final static String QUERY_STRINGS_LABEL = "Query Strings:";
	private final static char QUERY_STRINGS_MNEMONIC = 'S';
	private final static String[] STRING_ARRAY = new String[0];

	final EditableList queryStringsList;

	public QueryStringsPanel() {
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

		final JLabel queryStringsLabel_ = new JLabel(QUERY_STRINGS_LABEL);
		queryStringsList = new EditableList();
		
		queryStringsLabel_.setAlignmentX(Component.LEFT_ALIGNMENT);
		queryStringsList.setAlignmentX(Component.LEFT_ALIGNMENT);

		add(queryStringsLabel_);
		add(Box.createVerticalStrut(12));
		add(queryStringsList);

		queryStringsLabel_.setLabelFor(queryStringsList.getTextField());
		queryStringsLabel_.setDisplayedMnemonic(QUERY_STRINGS_MNEMONIC);

		final Property queriesProperty_ = QueriesProperty.getInstance();
		queriesProperty_.addPropertyChangeListener(new PropertyChangeListener() {
			public void propertyChange(final PropertyChangeEvent propertyChangeEvent_) {
				final DefaultListModel listModel_ = (DefaultListModel) queryStringsList.getList().getModel();
				final String[] newQueries_ = (String[]) queriesProperty_.getValue();

				listModel_.removeAllElements();

				for (int i = 0; i < newQueries_.length; ++i) {
					listModel_.addElement(newQueries_[i]);
				}
			}
		});

		populateFields();
	}

	private String createQueryString() {
      final Object[] queries_ = ((DefaultListModel) getQueryStringsList().getList().getModel()).toArray();
      String queryString_ = null;

      if (queries_.length > 0) {
         queryString_ = StringUtils.join(queries_, ((String) DelimiterProperty.getInstance().getValue()).substring(0, 1));
      }

      return queryString_;
	}

	public EditableList getQueryStringsList() {
		return queryStringsList;
	}

	private void populateFields() {
		final DefaultListModel queryStringsListModel_ = (DefaultListModel) getQueryStringsList().getList().getModel();
		final String[] queries_ = (String[]) QueriesProperty.getInstance().getValue();

		for (int i = 0; i < queries_.length; ++i) {
			queryStringsListModel_.addElement(queries_[i]);
		}
	}

	public void setProperties() {
		QueriesProperty.getInstance().setProperty(createQueryString());
	}

	public void validateProperties() throws PropertyException {
		try {	
			QueriesProperty.getInstance().validate(createQueryString());
			setInvalidComponent(null);
		} catch (PropertyException propertyException_) {
			setInvalidComponent(getQueryStringsList().getList());
			throw propertyException_;
		}
	}
}

