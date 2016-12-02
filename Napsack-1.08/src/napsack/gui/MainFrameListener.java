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

import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.SwingConstants;

import napsack.util.StringUtils;
import napsack.util.properties.DelimiterProperty;
import napsack.util.properties.NapsackProperties;
import napsack.util.properties.PropertyException;
import napsack.util.properties.SaveOnExitProperty;

public class MainFrameListener extends WindowAdapter implements ComponentListener {
	private final static String TITLE = "Warning";
	private final static String STORE_INTRO = "Cannot save settings: ";
	private final static String QUESTION = "Exit Napsack without saving settings?";

	private final MainFrame mainFrame;
	private final String[] propertyMessages;
	private final String[] storeMessages;

	public MainFrameListener(final MainFrame mainFrame_) {
		mainFrame = mainFrame_;

		propertyMessages = new String[] {null, QUESTION};
		storeMessages = new String[] {null, QUESTION};
	}

	public void close() {
		final MainFrame mainFrame_ = getMainFrame();
		final SearchPanel searchPanel_ = mainFrame_.getSearchPanel();
		int store_ = JOptionPane.YES_OPTION;
		int exit_ = JOptionPane.YES_OPTION;

		if (((Boolean) SaveOnExitProperty.getInstance().getValue()).booleanValue()) {
			try {
				searchPanel_.validateProperties();
				searchPanel_.setProperties();
			} catch (PropertyException propertyException_) {
				final String[] propertyMessages_ = getPropertyMessages();

				propertyMessages_[0] = STORE_INTRO + propertyException_.getMessage();

				store_ = JOptionPane.NO_OPTION;

				exit_ = JOptionPane.showConfirmDialog(mainFrame_, propertyMessages_, TITLE, JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
			}

			if (store_ == JOptionPane.YES_OPTION) {
				try {
					NapsackProperties.getInstance().storeProperties();
				} catch (IOException ioException_) {
					final String[] storeMessages_ = getStoreMessages();
					storeMessages_[0] = STORE_INTRO + ioException_.getMessage();

					exit_ = JOptionPane.showConfirmDialog(mainFrame_, storeMessages_, TITLE, JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
				}
			}
		}

		if (exit_ == JOptionPane.YES_OPTION) {
      	// Comment out the lines between <comment> and </comment> tags for
      	// Java 1.2.2.  This will prevent Napsack from (optionally) saving
      	// its settings on exit unless it is shutdown through the file menu or
			// through standard window controls.

      	// <comment>
			Runtime.getRuntime().removeShutdownHook(mainFrame_.getShutdownHook());
			// </comment> 

			System.exit(0);
		}
	}

	public void componentHidden(final ComponentEvent compontentEvent_) {
	}

	public void componentMoved(final ComponentEvent compontentEvent_) {
	}

	public void componentResized(final ComponentEvent compontentEvent_) {
	}

	public void componentShown(final ComponentEvent compontentEvent_) {
	}

	private MainFrame getMainFrame() {
		return mainFrame;
	}

	private String[] getPropertyMessages() {
		return propertyMessages;
	}

	private String[] getStoreMessages() {
		return storeMessages;
	}

	public void windowClosing(final WindowEvent windowEvent_) {
		close();
	}
}

