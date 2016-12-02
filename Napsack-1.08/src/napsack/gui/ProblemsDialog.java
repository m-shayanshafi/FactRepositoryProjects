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
import java.awt.Component;
import java.awt.Container;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;

import napsack.commands.NapsterCommand;
import napsack.event.CommandAdapter;
import napsack.event.CommandCampaignEvent;
import napsack.event.CommandCampaignListener;
import napsack.event.CommandEvent;
import napsack.event.CommandListener;
import napsack.gui.problems.ProblemsTree;
import napsack.util.model.ConnectError;
import napsack.util.model.DisconnectError;
import napsack.util.model.Error;
import napsack.util.model.SongQueryError;


public class ProblemsDialog extends NapsackDialog implements CommandCampaignListener {
	private final static String TITLE = "Problems";
	private final static boolean MODAL = false;
	private final static String CLEAR_BUTTON = "Clear";
	private final static String CLOSE_BUTTON = "Close";

	private final ProblemsTree problemsTree;
	private final CommandListener connectListener;
	private final CommandListener songQueryListener;
	private final CommandListener disconnectListener;

	public ProblemsDialog(final Frame owner_) {
		super(owner_, TITLE, MODAL);

		problemsTree = new ProblemsTree();

		connectListener = new CommandAdapter() {
			public void commandFailed(final CommandEvent commandEvent_) {
				final Error error_ = new ConnectError(commandEvent_.getNapsterService(), (NapsterCommand) commandEvent_.getSource(), commandEvent_.getException());

				SwingUtilities.invokeLater(new Runnable() {
					public void run() {
						problemsTree.addError(error_);
					}
				});
			}
		};
		songQueryListener = new CommandAdapter() {
			public void commandFailed(final CommandEvent commandEvent_) {
				final Error error_ = new SongQueryError(commandEvent_.getNapsterService(), (NapsterCommand) commandEvent_.getSource(), commandEvent_.getException());

				SwingUtilities.invokeLater(new Runnable() {
					public void run() {
						problemsTree.addError(error_);
					}
				});
			}
		};
		disconnectListener = new CommandAdapter() {
			public void commandFailed(final CommandEvent commandEvent_) {
				final Error error_ = new DisconnectError(commandEvent_.getNapsterService(), (NapsterCommand) commandEvent_.getSource(), commandEvent_.getException());
				SwingUtilities.invokeLater(new Runnable() {
					public void run() {
						problemsTree.addError(error_);
					}
				});
			}
		};

		final Container contentPane_ = getContentPane();
		final CommandButtons buttons_ = new HorizontalCommandButtons(new String[] {CLEAR_BUTTON, CLOSE_BUTTON}, Component.RIGHT_ALIGNMENT);
		final JButton clearButton_ = buttons_.getButton(CLEAR_BUTTON);
		final JButton closeButton_ = buttons_.getButton(CLOSE_BUTTON);
		final JPanel problemsPanel_ = new JPanel(new GridLayout(1, 1));

		setDefaultCloseOperation(HIDE_ON_CLOSE);

		clearButton_.addActionListener(new ActionListener() {
			public void actionPerformed(final ActionEvent actionEvent_) {
				problemsTree.reset();
			}
		});

		closeButton_.addActionListener(new ActionListener() {
			public void actionPerformed(final ActionEvent actionEvent_) {
            setVisible(false);
         }
      });

		problemsPanel_.add(new JScrollPane(problemsTree));

		problemsPanel_.setBorder(BorderFactory.createEmptyBorder(12, 12, 0, 11));
		buttons_.setBorder(BorderFactory.createEmptyBorder(17, 12, 11, 11));

		contentPane_.setLayout(new BoxLayout(contentPane_, BoxLayout.Y_AXIS));
		contentPane_.add(problemsPanel_);
		contentPane_.add(buttons_);

		getRootPane().setDefaultButton(closeButton_);

		pack();
	}

	public void campaignAborted(final CommandCampaignEvent commandCampaignEvent_) {
	}

	public void campaignCompleted(final CommandCampaignEvent commandCampaignEvent_) {
	}

	public void campaignLaunched(final CommandCampaignEvent commandCampaignEvent_) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				getProblemsTree().reset();
			}
		});
	}

	public CommandListener getConnectListener() {
		return connectListener;
	}

	public CommandListener getDisconnectListener() {
		return disconnectListener;
	}

	public ProblemsTree getProblemsTree() {
		return problemsTree;
	}

	public CommandListener getSongQueryListener() {
		return songQueryListener;
	}
}

