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

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.IOException;

import javax.swing.JCheckBoxMenuItem;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;

import napsack.util.Switch;
import napsack.util.properties.NapsackProperties;
import napsack.util.properties.PropertyException;
import napsack.util.properties.SaveOnExitProperty;

public class FileMenu extends NapsackMenu {
	private final static String FILE_MENU = "File";
	private final static String SAVE_SETTINGS = "Save Settings";
	private final static String SAVE_SETTINGS_ON_EXIT_ITEM = "Save Settings Before Exiting";
	private final static String EXIT_ITEM = "Exit";

	private final static char FILE_MNEMONIC = 'F';
	private final static char SAVE_SETTINGS_MNEMONIC = 'S';
	private final static char SAVE_SETTINGS_ON_EXIT_MNEMONIC = 'v';
	private final static char EXIT_MNEMONIC = 'x';

	private final static String WARNING_INTRO = "Cannot save settings: ";
	private final static String TITLE = "Warning";

	public FileMenu(final MainFrame mainFrame_) {
		super(mainFrame_, FILE_MENU, FILE_MNEMONIC);
	}

	protected void buildMenu() {
		final JMenuItem saveSettings_ = new JMenuItem(SAVE_SETTINGS);
		final JCheckBoxMenuItem saveSettingsOnExit_ = new JCheckBoxMenuItem(SAVE_SETTINGS_ON_EXIT_ITEM);
		final JMenuItem exit_ = new JMenuItem(EXIT_ITEM);

		saveSettings_.setMnemonic(SAVE_SETTINGS_MNEMONIC);
		saveSettingsOnExit_.setMnemonic(SAVE_SETTINGS_ON_EXIT_MNEMONIC);
		saveSettingsOnExit_.setSelected(((Boolean) SaveOnExitProperty.getInstance().getValue()).booleanValue());
		exit_.setMnemonic(EXIT_MNEMONIC);

		add(saveSettings_);
		add(saveSettingsOnExit_);
		addSeparator();
		add(exit_);

		saveSettings_.addActionListener(new ActionListener() {
			public void actionPerformed(final ActionEvent actionEvent_) {
				final MainFrame mainFrame_ = getMainFrame();

				try {
					final SearchPanel searchPanel_ = mainFrame_.getSearchPanel();

					searchPanel_.validateProperties();
					searchPanel_.setProperties();
					NapsackProperties.getInstance().storeProperties();
				} catch (PropertyException propertyException_) {
					JOptionPane.showMessageDialog(mainFrame_, WARNING_INTRO + propertyException_.getMessage(), TITLE, JOptionPane.WARNING_MESSAGE);
				} catch (IOException ioException_) {
					JOptionPane.showMessageDialog(mainFrame_, WARNING_INTRO + ioException_.getMessage(), TITLE, JOptionPane.WARNING_MESSAGE);
				}
			}
		});

		saveSettingsOnExit_.addItemListener(new ItemListener() {
			public void itemStateChanged(final ItemEvent itemEvent_) {
				if (saveSettingsOnExit_.isSelected()) {
					SaveOnExitProperty.getInstance().setProperty(Switch.ON.toString());
				} else {
					SaveOnExitProperty.getInstance().setProperty(Switch.OFF.toString());

					try {
						NapsackProperties.getInstance().storeSaveOnExit();
					} catch (IOException ioException_) {
					}
				}
			}
		});

		exit_.addActionListener(new ActionListener() {
			public void actionPerformed(final ActionEvent actionEvent_) {
				getMainFrame().getMainFrameListener().close();
			}
		});
	}
}

