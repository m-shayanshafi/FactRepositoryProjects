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

import javax.swing.JTextField;
import javax.swing.SwingUtilities;

import napsack.commands.SongQueryCommand;
import napsack.event.CommandAdapter;
import napsack.event.CommandCampaignEvent;
import napsack.event.CommandCampaignListener;
import napsack.event.CommandEvent;
import napsack.event.CommandListener;

public class StatusBar extends JTextField {
	private CommandCampaignListener commandCampaignListener;
	private CommandListener connectListener;
	private CommandListener songQueryListener;
	private CommandListener disconnectListener;

	public StatusBar() {
		commandCampaignListener = new CommandCampaignListener() {
			public void campaignAborted(final CommandCampaignEvent commandCampaignEvent_) {
				setTextLater("");
			}

			public void campaignCompleted(final CommandCampaignEvent commandCampaignEvent_) {
				setTextLater("");
			}

			public void campaignLaunched(final CommandCampaignEvent commandCompaignEvent_) {
				setTextLater("");
			}
		};
				
		connectListener = new CommandAdapter() {
			public void commandExecuted(final CommandEvent commandEvent_) {
				setTextLater("Connected to " + commandEvent_.getNapsterService().getIdentifier() + ".");
			}

			public void commandExecuting(final CommandEvent commandEvent_) {
				setTextLater("Connecting to " + commandEvent_.getNapsterService().getIdentifier() + ".");
			}

			public void commandFailed(final CommandEvent commandEvent_) {
				setTextLater("Could not connect to " + commandEvent_.getNapsterService().getIdentifier() + ".");
			}
		};

		songQueryListener = new CommandAdapter() {
			public void commandExecuted(final CommandEvent commandEvent_) {
				setTextLater("Queried " + commandEvent_.getNapsterService().getIdentifier() + " for " +  ((SongQueryCommand) commandEvent_.getSource()).getQueryMessage().getQueryString() + ".");
			}

			public void commandExecuting(final CommandEvent commandEvent_) {
				setTextLater("Querying " + commandEvent_.getNapsterService().getIdentifier() + " for " +  ((SongQueryCommand) commandEvent_.getSource()).getQueryMessage().getQueryString() + ".");
			}

			public void commandFailed(final CommandEvent commandEvent_) {
				setTextLater("Could not query " + commandEvent_.getNapsterService().getIdentifier() + " for " +  ((SongQueryCommand) commandEvent_.getSource()).getQueryMessage().getQueryString() + ".");
			}
		};

		disconnectListener = new CommandAdapter() {
			public void commandExecuted(final CommandEvent commandEvent_) {
				setTextLater("Disconnected from " + commandEvent_.getNapsterService().getIdentifier() + ".");
			}

			public void commandExecuting(final CommandEvent commandEvent_) {
				setTextLater("Disonnecting from " + commandEvent_.getNapsterService().getIdentifier() + ".");
			}

			public void commandFailed(final CommandEvent commandEvent_) {
				setTextLater("Could not disconnect from " + commandEvent_.getNapsterService().getIdentifier() + ".");
			}
		};

		setEditable(false);
	}

	final CommandCampaignListener getCommandCampaignListener() {
		return commandCampaignListener;
	}

	final CommandListener getConnectListener() {
		return connectListener;
	}

	final CommandListener getDisconnectListener() {
		return disconnectListener;
	}

	final CommandListener getSongQueryListener() {
		return songQueryListener;
	}

	private final void setTextLater(final String text_) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				setText(text_);
			}
		});
	}
}

