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

import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JOptionPane;

import napsack.Napsack;
import napsack.gui.properties.LoginPanel;
import napsack.util.properties.PropertyException;

public class LoginDialog extends SplashDialog {
	private final static String EXCEPTION_TITLE = "Error";
	private final static String WARNING_INTRO = "Cannot save settings: ";
	private final static String WARNING_TITLE = "Warning";

	public LoginDialog(final Frame owner_) {
		super(owner_, Napsack.APPLICATION_IDENTIFIER, true, new LoginPanel());

		setResizable(false);
		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);

		final LoginPanel loginPanel_ = (LoginPanel) getRightComponent();
		final CommandButtons commandButtons_ = loginPanel_.getCommandButtons();
		final JButton okButton_ = commandButtons_.getButton(LoginPanel.OK_BUTTON);

		okButton_.addActionListener(new ActionListener() {
			public void actionPerformed(final ActionEvent actionEvent_) {
				try {
					loginPanel_.validateProperties();
					loginPanel_.setProperties();
					loginPanel_.saveProperties();
					hide();
					dispose();
				} catch (PropertyException propertyException_) {
					loginPanel_.getInvalidComponent().requestFocus();
					JOptionPane.showMessageDialog(LoginDialog.this, propertyException_.getMessage(), EXCEPTION_TITLE, JOptionPane.ERROR_MESSAGE);
				} catch (IOException ioException_) {
					JOptionPane.showMessageDialog(LoginDialog.this, WARNING_INTRO + ioException_.getMessage(), WARNING_TITLE, JOptionPane.WARNING_MESSAGE);
					hide();
					dispose();
				}
			}
		});

		commandButtons_.getButton(LoginPanel.CANCEL_BUTTON).addActionListener(new ActionListener() {
			public void actionPerformed(final ActionEvent actionEvent_) {
				((MainFrame) getOwner()).getMainFrameListener().close();
			}
		});

		getRootPane().setDefaultButton(okButton_);
	}
}

