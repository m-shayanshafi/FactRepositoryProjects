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

import java.lang.reflect.InvocationTargetException;
import javax.swing.JProgressBar;
import javax.swing.SwingUtilities;

import napsack.event.CommandCampaignAdapter;
import napsack.event.CommandCampaignEvent;
import napsack.event.CommandCampaignListener;
import napsack.event.CommandThreadEvent;
import napsack.event.CommandThreadAdapter;
import napsack.event.CommandThreadListener;

public class ProgressBar extends JProgressBar {
	private final CommandCampaignListener commandCampaignListener;
	private final CommandThreadListener commandThreadListener;

	public ProgressBar() {
		final Runnable incrementValue_ = new Runnable() {
			public void run() {
				setValue(getValue() + 1);
			}
		};

		commandCampaignListener = new CommandCampaignAdapter() {
			public void campaignAborted(final CommandCampaignEvent commandCampaignEvent_) {
				SwingUtilities.invokeLater(new Runnable() {
					public void run() {
						updateBounds(0, 0);
					}
				});
			}

			public void campaignLaunched(final CommandCampaignEvent commandCampaignEvent_) {
				SwingUtilities.invokeLater(new Runnable() {
					public void run() {
						updateBounds(0, commandCampaignEvent_.getNapsterServices().length);
					}
				});
			}
		};

		commandThreadListener = new CommandThreadAdapter() {
			public void threadRan(final CommandThreadEvent commandThreadEvent_) {
				boolean invoked_ = false;

				do {
					try {
						SwingUtilities.invokeAndWait(incrementValue_);
						invoked_ = true;
					} catch (InterruptedException interruptedException_) {
					} catch (InvocationTargetException invocationTargetException_) {
					}
				} while (!invoked_);
			}

			public void threadFailed(final CommandThreadEvent commandThreadEvent_) {
				threadRan(commandThreadEvent_);
			}
		};

		setStringPainted(false);
	}

	public CommandCampaignListener getCommandCampaignListener() {
		return commandCampaignListener;
	}

	public CommandThreadListener getCommandThreadListener() {
		return commandThreadListener;
	}

	public void updateBounds(final int min_, final int max_) {
		setMinimum(min_);
		setMaximum(max_);
		setValue(min_);
	}
}

