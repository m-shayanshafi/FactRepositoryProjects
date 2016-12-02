// Written by Chris Redekop <waveform@minUsers.sourceforge.net>
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
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JTextField;

import napsack.gui.GuiUtils;
import napsack.gui.properties.PropertiesPanel;
import napsack.util.properties.MinFilesProperty;
import napsack.util.properties.MinGigsProperty;
import napsack.util.properties.MinUsersProperty;
import napsack.util.properties.PropertyException;

public class NetworkCriteriaPanel extends PropertiesPanel {
	private final static String MIN_FILES_LABEL = "Min. Files:";
	private final static String MIN_GIGS_LABEL = "Min. Gigs:";
	private final static String MIN_USERS_LABEL = "Min. Users:";

	private final static char MIN_FILES_MNEMONIC = 'M';
	private final static char MIN_GIGS_MNEMONIC = 'G';
	private final static char MIN_USERS_MNEMONIC = 'U';

	private final static int FIELD_WIDTH = 8;

	private JTextField minFilesField;
	private JTextField minGigsField;
	private JTextField minUsersField;

	public NetworkCriteriaPanel() {
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

		final JPanel minFilesPanel_ = new JPanel();
		final JLabel minFilesLabel_ = new JLabel(MIN_FILES_LABEL);
		minFilesField = new JTextField(FIELD_WIDTH);

		final JPanel minGigsPanel_ = new JPanel();
		final JLabel minGigsLabel_ = new JLabel(MIN_GIGS_LABEL);
		minGigsField = new JTextField(FIELD_WIDTH);

		final JPanel minUsersPanel_ = new JPanel();
		final JLabel minUsersLabel_ = new JLabel(MIN_USERS_LABEL);
		minUsersField = new JTextField(FIELD_WIDTH);

		GuiUtils.setAllWidthsToMax(new JComponent[] {minFilesLabel_, minGigsLabel_, minUsersLabel_});

		GuiUtils.setMaxHeight(minFilesField, minFilesField.getPreferredSize().height);
		GuiUtils.setMaxHeight(minGigsField, minGigsField.getPreferredSize().height);
		GuiUtils.setMaxHeight(minUsersField, minUsersField.getPreferredSize().height);

		minFilesLabel_.setAlignmentY(Component.TOP_ALIGNMENT);
		minFilesField.setAlignmentY(Component.TOP_ALIGNMENT);

		minGigsLabel_.setAlignmentY(Component.TOP_ALIGNMENT);
		minGigsField.setAlignmentY(Component.TOP_ALIGNMENT);

		minUsersLabel_.setAlignmentY(Component.TOP_ALIGNMENT);
		minUsersField.setAlignmentY(Component.TOP_ALIGNMENT);

		minFilesPanel_.setLayout(new BoxLayout(minFilesPanel_, BoxLayout.X_AXIS));
		minFilesPanel_.add(minFilesLabel_);
		minFilesPanel_.add(Box.createHorizontalStrut(12));
		minFilesPanel_.add(minFilesField);
		GuiUtils.setMaxHeight(minFilesPanel_, GuiUtils.getMaxHeight(new JComponent[] {minFilesLabel_, minFilesField}));

		minGigsPanel_.setLayout(new BoxLayout(minGigsPanel_, BoxLayout.X_AXIS));
		minGigsPanel_.add(minGigsLabel_);
		minGigsPanel_.add(Box.createHorizontalStrut(12));
		minGigsPanel_.add(minGigsField);
		GuiUtils.setMaxHeight(minGigsPanel_, GuiUtils.getMaxHeight(new JComponent[] {minGigsLabel_, minGigsField}));

		minUsersPanel_.setLayout(new BoxLayout(minUsersPanel_, BoxLayout.X_AXIS));
		minUsersPanel_.add(minUsersLabel_);
		minUsersPanel_.add(Box.createHorizontalStrut(12));
		minUsersPanel_.add(minUsersField);
		GuiUtils.setMaxHeight(minUsersPanel_, GuiUtils.getMaxHeight(new JComponent[] {minUsersLabel_, minUsersField}));

		add(minFilesPanel_);
		add(Box.createVerticalStrut(5));
		add(minGigsPanel_);
		add(Box.createVerticalStrut(5));
		add(minUsersPanel_);
		add(Box.createVerticalGlue());

		minFilesLabel_.setLabelFor(minFilesField);
		minFilesLabel_.setDisplayedMnemonic(MIN_FILES_MNEMONIC);

		minGigsLabel_.setLabelFor(minGigsField);
		minGigsLabel_.setDisplayedMnemonic(MIN_GIGS_MNEMONIC);

		minUsersLabel_.setLabelFor(minUsersField);
		minUsersLabel_.setDisplayedMnemonic(MIN_USERS_MNEMONIC);

		populateFields();
	}

	private JTextField getMinFilesField() {
		return minFilesField;
	}

	private JTextField getMinGigsField() {
		return minGigsField;
	}

	private JTextField getMinUsersField() {
		return minUsersField;
	}

	private void populateFields() {
		final Object minFiles_ = MinFilesProperty.getInstance().getValue();
		final Object minGigs_ = MinGigsProperty.getInstance().getValue();
		final Object minUsers_ = MinUsersProperty.getInstance().getValue();

		getMinFilesField().setText(minFiles_ == null ? "" : minFiles_.toString());
		getMinGigsField().setText(minGigs_ == null ? "" : minGigs_.toString());
		getMinUsersField().setText(minUsers_ == null ? "" : minUsers_.toString());
	}

	public void setProperties() {
		MinFilesProperty.getInstance().setProperty(trimProperty(getMinFilesField().getText()));
		MinGigsProperty.getInstance().setProperty(trimProperty(getMinGigsField().getText()));
		MinUsersProperty.getInstance().setProperty(trimProperty(getMinUsersField().getText()));
	}

	public void validateProperties() throws PropertyException {
		final JTextField minFilesField_ = getMinFilesField();

		try {
			MinFilesProperty.getInstance().validate(trimProperty(minFilesField_.getText()));
		} catch (PropertyException propertyException_) {
			setInvalidComponent(minFilesField_);
			throw propertyException_;
		}
			
		final JTextField minGigsField_ = getMinGigsField();

		try {
			MinGigsProperty.getInstance().validate(trimProperty(minGigsField_.getText()));
		} catch (PropertyException propertyException_) {
			setInvalidComponent(minGigsField_);
			throw propertyException_;
		}
			
		final JTextField minUsersField_ = getMinUsersField();

		try {
			MinUsersProperty.getInstance().validate(trimProperty(minUsersField_.getText()));
		} catch (PropertyException propertyException_) {
			setInvalidComponent(minUsersField_);
			throw propertyException_;
		}
	}
}

