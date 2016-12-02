// Written by Chris Redekop <waveform@connection.sourceforge.net>
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
import java.util.Hashtable;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JSlider;
import javax.swing.JTextField;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import napsack.gui.GuiUtils;
import napsack.protocol.BitRate;
import napsack.protocol.BitRateConstraint;
import napsack.protocol.Comparison;
import napsack.protocol.Connection;
import napsack.protocol.ConnectionConstraint;
import napsack.protocol.Frequency;
import napsack.protocol.FrequencyConstraint;
import napsack.util.properties.BitRateConstraintProperty;
import napsack.util.properties.ConnectionConstraintProperty;
import napsack.util.properties.DelimiterProperty;
import napsack.util.properties.FrequencyConstraintProperty;
import napsack.util.properties.MaxResultsProperty;
import napsack.util.properties.PropertyException;

public class QueryCriteriaPanel extends PropertiesPanel {
	private final static String BIT_RATE_LABEL = "Bit Rate:";
	private final static String FREQUENCY_LABEL = "Frequency:";
	private final static String CONNECTION_TYPE_LABEL = "Connection:";
	private final static String MAX_RESULTS_LABEL = "Max. Results:";

	private final static char BIT_RATE_MNEMONIC = 'B';
	private final static char FREQUENCY_MNEMONIC = 'q';
	private final static char CONNECTION_MNEMONIC = 'C';
	private final static char MAX_RESULTS_MNEMONIC = 'M';
	
	private final static int FIELD_WIDTH = 3;
	private final static int MAX_RESULTS_MIN = 0;
	private final static int MAX_RESULTS_MAX = 100;

	final JComboBox bitRateComparisonComboBox;
	final JComboBox bitRateComboBox;
	final JComboBox frequencyComparisonComboBox;
	final JComboBox frequencyComboBox;
	final JComboBox connectionComparisonComboBox;
	final JComboBox connectionComboBox;
	final JSlider maxResultsSlider;
	final JTextField maxResultsTextField;

	private static Object[] padDropDown(final Object[] objects_) {
		final Object[] paddedObjects_ = new Object[objects_.length + 1];

		paddedObjects_[0] = "";
		System.arraycopy(objects_, 0, paddedObjects_, 1, objects_.length);

		return paddedObjects_;
	}

	public QueryCriteriaPanel() {
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

		final Object[] comparisons_ = padDropDown(Comparison.getValidComparisons());

		final JPanel bitRatePanel_ = new JPanel();
		final JLabel bitRateLabel_ = new JLabel(BIT_RATE_LABEL);
		bitRateComparisonComboBox = new JComboBox(comparisons_);
		bitRateComboBox = new JComboBox(padDropDown(BitRate.getValidBitRates()));

		final JPanel frequencyPanel_ = new JPanel();
		final JLabel frequencyLabel_ = new JLabel(FREQUENCY_LABEL);
		frequencyComparisonComboBox = new JComboBox(comparisons_);
		frequencyComboBox = new JComboBox(padDropDown(Frequency.getValidFrequencies()));

		final JPanel connectionPanel_ = new JPanel();
		final JLabel connectionLabel_ = new JLabel(CONNECTION_TYPE_LABEL);
		connectionComparisonComboBox = new JComboBox(comparisons_);
		connectionComboBox = new JComboBox(padDropDown(Connection.getValidConnections()));

		final JPanel maxResultsPanel_ = new JPanel();
		final JLabel maxResultsLabel_ = new JLabel(MAX_RESULTS_LABEL);
		maxResultsSlider = new JSlider(MAX_RESULTS_MIN, MAX_RESULTS_MAX);
		maxResultsTextField = new JTextField(FIELD_WIDTH);

		GuiUtils.setAllWidthsToMax(new JComponent[] {bitRateLabel_, frequencyLabel_, connectionLabel_, maxResultsLabel_});
		GuiUtils.setAllWidthsToMax(new JComponent[] {bitRateComparisonComboBox, frequencyComparisonComboBox, connectionComparisonComboBox});
		GuiUtils.setAllWidthsToMax(new JComponent[] {bitRateComboBox, frequencyComboBox, connectionComboBox});

		GuiUtils.setMaxHeight(bitRateComboBox, bitRateComboBox.getPreferredSize().height);
		GuiUtils.setMaxHeight(frequencyComboBox, frequencyComboBox.getPreferredSize().height);

		GuiUtils.setAllSizes(maxResultsTextField, maxResultsTextField.getPreferredSize());
		GuiUtils.setAllWidths(maxResultsSlider, connectionComparisonComboBox.getPreferredSize().width + connectionComboBox.getPreferredSize().width - maxResultsTextField.getPreferredSize().width - 2);

		bitRateLabel_.setAlignmentY(Component.TOP_ALIGNMENT);
		bitRateComparisonComboBox.setAlignmentY(Component.TOP_ALIGNMENT);
		bitRateComboBox.setAlignmentY(Component.TOP_ALIGNMENT);

		frequencyLabel_.setAlignmentY(Component.TOP_ALIGNMENT);
		frequencyComparisonComboBox.setAlignmentY(Component.TOP_ALIGNMENT);
		frequencyComboBox.setAlignmentY(Component.TOP_ALIGNMENT);

		connectionLabel_.setAlignmentY(Component.TOP_ALIGNMENT);
		connectionComparisonComboBox.setAlignmentY(Component.TOP_ALIGNMENT);
		connectionComboBox.setAlignmentY(Component.TOP_ALIGNMENT);

		maxResultsLabel_.setAlignmentY(Component.TOP_ALIGNMENT);
		maxResultsSlider.setAlignmentY(Component.TOP_ALIGNMENT);
		maxResultsTextField.setAlignmentY(Component.TOP_ALIGNMENT);

		bitRatePanel_.setLayout(new BoxLayout(bitRatePanel_, BoxLayout.X_AXIS));
		bitRatePanel_.add(bitRateLabel_);
		bitRatePanel_.add(Box.createHorizontalStrut(12));
		bitRatePanel_.add(bitRateComparisonComboBox);
		bitRatePanel_.add(Box.createHorizontalStrut(5));
		bitRatePanel_.add(bitRateComboBox);
		GuiUtils.setMaxHeight(bitRatePanel_, GuiUtils.getMaxHeight(new JComponent[] {bitRateLabel_, bitRateComparisonComboBox, bitRateComboBox}));

		frequencyPanel_.setLayout(new BoxLayout(frequencyPanel_, BoxLayout.X_AXIS));
		frequencyPanel_.add(frequencyLabel_);
		frequencyPanel_.add(Box.createHorizontalStrut(12));
		frequencyPanel_.add(frequencyComparisonComboBox);
		frequencyPanel_.add(Box.createHorizontalStrut(5));
		frequencyPanel_.add(frequencyComboBox);
		GuiUtils.setMaxHeight(frequencyPanel_, GuiUtils.getMaxHeight(new JComponent[] {frequencyLabel_, frequencyComparisonComboBox, frequencyComboBox}));

		connectionPanel_.setLayout(new BoxLayout(connectionPanel_, BoxLayout.X_AXIS));
		connectionPanel_.add(connectionLabel_);
		connectionPanel_.add(Box.createHorizontalStrut(12));
		connectionPanel_.add(connectionComparisonComboBox);
		connectionPanel_.add(Box.createHorizontalStrut(5));
		connectionPanel_.add(connectionComboBox);
		GuiUtils.setMaxHeight(connectionPanel_, GuiUtils.getMaxHeight(new JComponent[] {connectionLabel_, connectionComparisonComboBox, connectionComboBox}));

		maxResultsPanel_.setLayout(new BoxLayout(maxResultsPanel_, BoxLayout.X_AXIS));
		maxResultsPanel_.add(maxResultsLabel_);
		maxResultsPanel_.add(Box.createHorizontalStrut(12));
		maxResultsPanel_.add(maxResultsSlider);
		maxResultsPanel_.add(Box.createHorizontalStrut(6));
		maxResultsPanel_.add(maxResultsTextField);
		maxResultsPanel_.add(Box.createHorizontalStrut(1));
		GuiUtils.setMaxHeight(maxResultsPanel_, GuiUtils.getMaxHeight(new JComponent[] {maxResultsLabel_, maxResultsSlider, maxResultsTextField}));

		add(bitRatePanel_);
		add(Box.createVerticalStrut(5));
		add(frequencyPanel_);
		add(Box.createVerticalStrut(5));
		add(connectionPanel_);
		add(Box.createVerticalStrut(5));	
		add(maxResultsPanel_);
		add(Box.createVerticalGlue());

		bitRateLabel_.setLabelFor(bitRateComparisonComboBox);
		bitRateLabel_.setDisplayedMnemonic(BIT_RATE_MNEMONIC);

		frequencyLabel_.setLabelFor(frequencyComparisonComboBox);
		frequencyLabel_.setDisplayedMnemonic(FREQUENCY_MNEMONIC);

		connectionLabel_.setLabelFor(connectionComparisonComboBox);
		connectionLabel_.setDisplayedMnemonic(CONNECTION_MNEMONIC);

		maxResultsLabel_.setLabelFor(maxResultsSlider);
		maxResultsLabel_.setDisplayedMnemonic(MAX_RESULTS_MNEMONIC);
		maxResultsTextField.setEditable(false);
		maxResultsSlider.addChangeListener(new ChangeListener() {
			public void stateChanged(final ChangeEvent changeEvent_) {
				maxResultsTextField.setText(String.valueOf(padMaxResultsValue()));
			}
		});


		populateFields();
	}

	private String createConstraintString(String comparison_, String value_, final String delimiter_) {
		comparison_ = trimProperty(comparison_);
		value_ = trimProperty(value_);
		String constraintString_ = null;

		if (comparison_ != null || value_ != null) {
			final StringBuffer constraintBuffer_ = new StringBuffer();

			if (comparison_ != null) {
				constraintBuffer_.append(comparison_);
			}

			constraintBuffer_.append(delimiter_);

			if (value_ != null) {
				constraintBuffer_.append(value_);
			}

			constraintString_ = constraintBuffer_.toString();
		}

		return constraintString_;
	}

	private JComboBox getBitRateComparisonComboBox() {
		return bitRateComparisonComboBox;
	}

	private JComboBox getBitRateComboBox() {
		return bitRateComboBox;
	}

	private JComboBox getConnectionComparisonComboBox() {
		return connectionComparisonComboBox;
	}

	private JComboBox getConnectionComboBox() {
		return connectionComboBox;
	}

	private JComboBox getFrequencyComparisonComboBox() {
		return frequencyComparisonComboBox;
	}

	private JComboBox getFrequencyComboBox() {
		return frequencyComboBox;
	}

	private JSlider getMaxResultsSlider() {
		return maxResultsSlider;
	}

	private JTextField getMaxResultsTextField() {
		return maxResultsTextField;
	}

	private int padMaxResultsValue() {
		final int maxResultsValue_ = getMaxResultsSlider().getValue();

		return maxResultsValue_ == 0 ? 1 : maxResultsValue_;
	}

	private void populateFields() {
		final BitRateConstraint bitRateConstraint_ = (BitRateConstraint) BitRateConstraintProperty.getInstance().getValue();
		final FrequencyConstraint frequencyConstraint_ = (FrequencyConstraint) FrequencyConstraintProperty.getInstance().getValue();
		final ConnectionConstraint connectionConstraint_ = (ConnectionConstraint) ConnectionConstraintProperty.getInstance().getValue();
		final Integer maxResults_ = (Integer) MaxResultsProperty.getInstance().getValue();

		if (bitRateConstraint_ != null) {
			getBitRateComparisonComboBox().setSelectedItem(bitRateConstraint_.getComparison());
			getBitRateComboBox().setSelectedItem(bitRateConstraint_.getBitRate());
		}

		if (frequencyConstraint_ != null) {
			getFrequencyComparisonComboBox().setSelectedItem(frequencyConstraint_.getComparison());
			getFrequencyComboBox().setSelectedItem(frequencyConstraint_.getFrequency());
		}

		if (connectionConstraint_ != null) {
			getConnectionComparisonComboBox().setSelectedItem(connectionConstraint_.getComparison());
			getConnectionComboBox().setSelectedItem(connectionConstraint_.getConnection());
		}

		if (maxResults_ != null) {
			getMaxResultsSlider().setValue(maxResults_.intValue());
			getMaxResultsTextField().setText(maxResults_.toString());
		}
	}

	public void setProperties() {
		final String delimiter_ = ((String) DelimiterProperty.getInstance().getValue()).substring(0, 1);

		BitRateConstraintProperty.getInstance().setProperty(createConstraintString(getBitRateComparisonComboBox().getSelectedItem().toString(), getBitRateComboBox().getSelectedItem().toString(), delimiter_));
		FrequencyConstraintProperty.getInstance().setProperty(createConstraintString(getFrequencyComparisonComboBox().getSelectedItem().toString(), getFrequencyComboBox().getSelectedItem().toString(), delimiter_));
		ConnectionConstraintProperty.getInstance().setProperty(createConstraintString(getConnectionComparisonComboBox().getSelectedItem().toString(), getConnectionComboBox().getSelectedItem().toString(), delimiter_));
		MaxResultsProperty.getInstance().setProperty(trimProperty(getMaxResultsTextField().getText()));
	}

	public void validateProperties() throws PropertyException {
		final String delimiter_ = ((String) DelimiterProperty.getInstance().getValue()).substring(0, 1);

		final JComboBox bitRateComparisonComboBox_ = getBitRateComparisonComboBox();

		try {
			BitRateConstraintProperty.getInstance().validate(createConstraintString(bitRateComparisonComboBox_.getSelectedItem().toString(), getBitRateComboBox().getSelectedItem().toString(), delimiter_));
			setInvalidComponent(null);
		} catch (PropertyException propertyException_) {
			setInvalidComponent(bitRateComparisonComboBox_);
			throw propertyException_;
		}

		final JComboBox frequencyComparisonComboBox_ = getFrequencyComparisonComboBox();

		try {
			FrequencyConstraintProperty.getInstance().validate(createConstraintString(frequencyComparisonComboBox_.getSelectedItem().toString(), getFrequencyComboBox().getSelectedItem().toString(), delimiter_));
			setInvalidComponent(null);
		} catch (PropertyException propertyException_) {
			setInvalidComponent(frequencyComparisonComboBox_);
			throw propertyException_;
		}

		final JComboBox connectionComparisonComboBox_ = getConnectionComparisonComboBox();

		try {
			ConnectionConstraintProperty.getInstance().validate(createConstraintString(connectionComparisonComboBox_.getSelectedItem().toString(), getConnectionComboBox().getSelectedItem().toString(), delimiter_));
			setInvalidComponent(null);
		} catch (PropertyException propertyException_) {
			setInvalidComponent(connectionComparisonComboBox_);
			throw propertyException_;
		}
	}
}

