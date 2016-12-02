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

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

public class EditableList extends JPanel {
	final static String ADD_BUTTON = "Add";
	final static String REMOVE_BUTTON = "Remove";
	final static String EDIT_BUTTON = "Edit";

	final static char ADD_MNEMONIC = 'A';
	final static char REMOVE_MNEMONIC = 'R';
	final static char EDIT_MNEMONIC = 'd';

	final JTextField textField;
	final JList list;
	final CommandButtons commandButtons;

	public EditableList() {
		setLayout(new BoxLayout(this, BoxLayout.X_AXIS));

		textField = new JTextField();
		final DefaultListModel listModel_ = new DefaultListModel();
		list = new JList(listModel_);
		commandButtons = new VerticalCommandButtons(new String[] {ADD_BUTTON, REMOVE_BUTTON, EDIT_BUTTON});
		final JPanel panel_ = new JPanel();
		final JScrollPane scrollPane_ = new JScrollPane(list);
		final JButton addButton_ = commandButtons.getButton(ADD_BUTTON);
		final JButton removeButton_ = commandButtons.getButton(REMOVE_BUTTON);
		final JButton editButton_ = commandButtons.getButton(EDIT_BUTTON);

		final ActionListener addListener_ = new ActionListener() {
			public void actionPerformed(final ActionEvent actionEvent_) {
				final String string_ = textField.getText();

				if (string_.trim().length() > 0 && !listModel_.contains(string_)) {
					listModel_.addElement(string_.trim());
				}

				textField.requestFocus();
				textField.selectAll();
			}
		};

		GuiUtils.setMaxSize(textField, scrollPane_.getMaximumSize().width, textField.getPreferredSize().height);

		panel_.setLayout(new BoxLayout(panel_, BoxLayout.Y_AXIS));
		panel_.add(textField);
		panel_.add(Box.createVerticalStrut(5));
		panel_.add(scrollPane_);

		add(panel_);
		add(Box.createHorizontalStrut(5));
		add(commandButtons);

		addButton_.setEnabled(false);
		removeButton_.setEnabled(false);
		editButton_.setEnabled(false);

		addButton_.setMnemonic(ADD_MNEMONIC);
		addButton_.addActionListener(addListener_);
		removeButton_.setMnemonic(REMOVE_MNEMONIC);
		removeButton_.addActionListener(new ActionListener() {
			public void actionPerformed(final ActionEvent actionEvent_) {
				final Object[] selectedRows_ = list.getSelectedValues();
				list.clearSelection();

				for (int i = 0; i < selectedRows_.length; ++i) {
					listModel_.removeElement(selectedRows_[i]);
				}

				list.requestFocus();
			}
		});
		editButton_.setMnemonic(EDIT_MNEMONIC);
		editButton_.addActionListener(new ActionListener() {
			public void actionPerformed(final ActionEvent actionEvent_) {
				final Object selectedRow_ =  list.getSelectedValue();
				list.clearSelection();

				listModel_.removeElement(selectedRow_);
				textField.setText(selectedRow_.toString());
				textField.requestFocus();
				textField.selectAll();
			}
		});

		textField.addActionListener(addListener_);
		textField.getDocument().addDocumentListener(new DocumentListener() {
			public void changedUpdate(final DocumentEvent documentEvent_) {
			}

			public void insertUpdate(final DocumentEvent documentEvent_) {
				updateAddButtonEnabled();
			}

			public void removeUpdate(final DocumentEvent documentEvent_) {
				updateAddButtonEnabled();
			}

			private void updateAddButtonEnabled() {
				if (textField.getText().trim().length() > 0) {
					addButton_.setEnabled(true);
				} else {
					addButton_.setEnabled(false);
				}
			}
		});
		list.addListSelectionListener(new ListSelectionListener() {
			public void valueChanged(final ListSelectionEvent listSelectionEvent_) {
				final int selected_ = list.getSelectedIndices().length;

				if (selected_ < 1) {
					editButton_.setEnabled(false);
					removeButton_.setEnabled(false);
				} else if (selected_ == 1) {
					editButton_.setEnabled(true);
					removeButton_.setEnabled(true);
				} else {
					editButton_.setEnabled(false);
					removeButton_.setEnabled(true);
				}
			}
		});
	}

	public CommandButtons getCommandButtons() {
		return commandButtons;
	}

	public JList getList() {
		return list;
	}

	public JTextField getTextField() {
		return textField;
	}
}

