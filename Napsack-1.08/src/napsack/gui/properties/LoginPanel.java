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
import java.io.IOException;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import napsack.Napsack;
import napsack.gui.CommandButtons;
import napsack.gui.GuiUtils;
import napsack.gui.HorizontalCommandButtons;
import napsack.util.properties.NapsackProperties;
import napsack.util.properties.NickProperty;
import napsack.util.properties.PasswordProperty;
import napsack.util.properties.PropertyException;
import napsack.util.properties.QueriesProperty;

public class LoginPanel extends PropertiesPanel {
	public final static String OK_BUTTON = "OK";
	public final static String CANCEL_BUTTON = "Cancel";

	private final static String TITLE = "Warning";
	private final static String WARNING_INTRO = "Cannot save settings: ";

	private final static String PROMPT_1 = "Napsack requires the following";
	private final static String PROMPT_2 = "properties to query Napster services.";
	private final static String NICK_LABEL = "Nick:";
	private final static String PASSWORD_LABEL = "Password:";
	private final static String QUERY_LABEL = "Query String:";
	private final static String SAVE_LABEL = "Save Settings:";
	private final static char NICK_MNEMONIC = 'N';
	private final static char PASSWORD_MNEMONIC = 'P';
	private final static char QUERY_MNEMONIC = 'Q';
	private final static char SAVE_MNEMONIC = 'S';

	private final static int FIELD_WIDTH = 10;

	private final CommandButtons commandButtons;
	private final JTextField nickField;
	private final JTextField passwordField;
	private final JTextField queryField;
	private final JCheckBox saveCheckBox;

	public LoginPanel() {
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

		final JPanel inputPanel_ = new JPanel();

		final JLabel prompt1_ = new JLabel(PROMPT_1);
		final JLabel prompt2_ = new JLabel(PROMPT_2);

		final JPanel nickPanel_ = new JPanel();
		final JLabel nickLabel_ = new JLabel(NICK_LABEL);
		nickField = new JTextField(FIELD_WIDTH);

		final JPanel passwordPanel_ = new JPanel();
		final JLabel passwordLabel_ = new JLabel(PASSWORD_LABEL);
		passwordField = new JTextField(FIELD_WIDTH);

		final JPanel queryPanel_ = new JPanel();
		final JLabel queryLabel_ = new JLabel(QUERY_LABEL);
		queryField = new JTextField(FIELD_WIDTH);

		final JPanel savePanel_ = new JPanel();
		final JLabel saveLabel_ = new JLabel(SAVE_LABEL);
		saveCheckBox = new JCheckBox();

		commandButtons = new HorizontalCommandButtons(new String[] {OK_BUTTON, CANCEL_BUTTON}, Component.RIGHT_ALIGNMENT);

		GuiUtils.setAllWidthsToMax(new JLabel[] {nickLabel_, passwordLabel_, queryLabel_, saveLabel_});
		GuiUtils.setMaxHeight(nickField, nickField.getPreferredSize().height);
		GuiUtils.setMaxHeight(passwordField, passwordField.getPreferredSize().height);
		GuiUtils.setMaxHeight(queryField, queryField.getPreferredSize().height);
		GuiUtils.setMaxHeight(saveCheckBox, saveCheckBox.getPreferredSize().height);

		nickLabel_.setAlignmentY(Component.TOP_ALIGNMENT);
		nickField.setAlignmentY(Component.TOP_ALIGNMENT);

		passwordLabel_.setAlignmentY(Component.TOP_ALIGNMENT);
		passwordField.setAlignmentY(Component.TOP_ALIGNMENT);

		queryLabel_.setAlignmentY(Component.TOP_ALIGNMENT);
		queryField.setAlignmentY(Component.TOP_ALIGNMENT);

		saveLabel_.setAlignmentY(Component.TOP_ALIGNMENT);
		saveCheckBox.setAlignmentY(Component.TOP_ALIGNMENT);

		nickPanel_.setLayout(new BoxLayout(nickPanel_, BoxLayout.X_AXIS));
		nickPanel_.add(nickLabel_);
		nickPanel_.add(Box.createHorizontalStrut(12));
		nickPanel_.add(nickField);
      GuiUtils.setMaxHeight(nickPanel_, GuiUtils.getMaxHeight(new JComponent[] {nickLabel_, nickField}));

		passwordPanel_.setLayout(new BoxLayout(passwordPanel_, BoxLayout.X_AXIS));
		passwordPanel_.add(passwordLabel_);
		passwordPanel_.add(Box.createHorizontalStrut(12));
		passwordPanel_.add(passwordField);
      GuiUtils.setMaxHeight(passwordPanel_, GuiUtils.getMaxHeight(new JComponent[] {passwordLabel_, passwordField}));

		queryPanel_.setLayout(new BoxLayout(queryPanel_, BoxLayout.X_AXIS));
		queryPanel_.add(queryLabel_);
		queryPanel_.add(Box.createHorizontalStrut(12));
		queryPanel_.add(queryField);
      GuiUtils.setMaxHeight(queryPanel_, GuiUtils.getMaxHeight(new JComponent[] {queryLabel_, queryField}));

		savePanel_.setLayout(new BoxLayout(savePanel_, BoxLayout.X_AXIS));
		savePanel_.add(saveLabel_);
		savePanel_.add(Box.createHorizontalStrut(12));
		savePanel_.add(saveCheckBox);
      GuiUtils.setMaxHeight(savePanel_, GuiUtils.getMaxHeight(new JComponent[] {saveLabel_, saveCheckBox}));

		prompt1_.setAlignmentX(Component.LEFT_ALIGNMENT);
		prompt2_.setAlignmentX(Component.LEFT_ALIGNMENT);
		nickPanel_.setAlignmentX(Component.LEFT_ALIGNMENT);
		passwordPanel_.setAlignmentX(Component.LEFT_ALIGNMENT);
		queryPanel_.setAlignmentX(Component.LEFT_ALIGNMENT);
		savePanel_.setAlignmentX(Component.LEFT_ALIGNMENT);
		
		inputPanel_.setLayout(new BoxLayout(inputPanel_, BoxLayout.Y_AXIS));
		inputPanel_.add(Box.createVerticalGlue());
		inputPanel_.add(prompt1_);
		inputPanel_.add(Box.createVerticalStrut(6));
		inputPanel_.add(prompt2_);
		inputPanel_.add(Box.createVerticalStrut(12));
		inputPanel_.add(nickPanel_);
		inputPanel_.add(Box.createVerticalStrut(5));
		inputPanel_.add(passwordPanel_);
		inputPanel_.add(Box.createVerticalStrut(5));
		inputPanel_.add(queryPanel_);
		inputPanel_.add(Box.createVerticalStrut(5));
		inputPanel_.add(savePanel_);
		inputPanel_.add(Box.createVerticalGlue());
		inputPanel_.setBorder(BorderFactory.createEmptyBorder(12, 12, 17, 11));

		commandButtons.setBorder(BorderFactory.createEmptyBorder(0, 11, 11, 11));

		inputPanel_.setAlignmentX(Component.LEFT_ALIGNMENT);
		commandButtons.setAlignmentX(Component.LEFT_ALIGNMENT);

		add(inputPanel_);
		add(commandButtons);

		nickLabel_.setLabelFor(nickField);
		nickLabel_.setDisplayedMnemonic(NICK_MNEMONIC);

		passwordLabel_.setLabelFor(passwordField);
		passwordLabel_.setDisplayedMnemonic(PASSWORD_MNEMONIC);

		queryLabel_.setLabelFor(queryField);
		queryLabel_.setDisplayedMnemonic(QUERY_MNEMONIC);

		saveLabel_.setLabelFor(saveCheckBox);
		saveLabel_.setDisplayedMnemonic(SAVE_MNEMONIC);
	}

	public CommandButtons getCommandButtons() {
		return commandButtons;
	}

	private JTextField getNickField() {
		return nickField;
	}

	private JTextField getPasswordField() {
		return passwordField;
	};

	private JTextField getQueryField() {
		return queryField;
	};

	private JCheckBox getSaveCheckBox() {
		return saveCheckBox;
	};

	public void saveProperties() throws IOException {
		if (getSaveCheckBox().isSelected()) {
			NapsackProperties.getInstance().storeProperties();
		}
	}

	public void setProperties() {
		NickProperty.getInstance().setProperty(getNickField().getText());
		PasswordProperty.getInstance().setProperty(getPasswordField().getText());
		QueriesProperty.getInstance().setProperty(getQueryField().getText());
	}

	public void validateProperties() throws PropertyException {
		final JTextField nickField_ = getNickField();

		try {
			NickProperty.getInstance().validate(nickField_.getText());
		} catch (PropertyException propertyException_) {
			setInvalidComponent(nickField_);
			throw propertyException_;
		}

		final JTextField passwordField_ = getPasswordField();

		try {
			PasswordProperty.getInstance().validate(passwordField_.getText());
		} catch (PropertyException propertyException_) {
			setInvalidComponent(passwordField_);
			throw propertyException_;
		}
		
		final JTextField queryField_ = getQueryField();
		try {
			QueriesProperty.getInstance().validate(queryField_.getText());
		} catch (PropertyException propertyException_) {
			setInvalidComponent(queryField_);
			throw propertyException_;
		}
	}
}

