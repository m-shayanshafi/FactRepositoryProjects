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

package napsack.commands;

import java.util.ArrayList;
import java.util.List;

import napsack.event.CommandThreadEvent;
import napsack.event.CommandThreadListener;
import napsack.event.ExceptionEvent;
import napsack.event.ExceptionListener;
import napsack.servers.NapsterService;

public class CommandThread extends Thread {
	private final NapsterService napsterService;
	private final NapsterCommand napsterCommand;
	private final int attempts;
	private final int attemptInterval;
	private final List commandThreadListeners;

	public CommandThread(final NapsterService napsterService_, final NapsterCommand napsterCommand_, final int attempts_, final int attemptInterval_) {
		this(napsterService_, napsterCommand_, attempts_, attemptInterval_, new ArrayList());
	}

	CommandThread(final NapsterService napsterService_, final NapsterCommand napsterCommand_, final int attempts_, final int attemptInterval_, final List commandThreadListeners_) {
		napsterService = napsterService_;
		napsterCommand = napsterCommand_;
		attempts = attempts_;
		attemptInterval = attemptInterval_;
		commandThreadListeners = commandThreadListeners_;
	}

	public void addCommandThreadListener(final CommandThreadListener commandThreadListener_) {
		final List commandThreadListeners_ = getCommandThreadListeners();

		synchronized (commandThreadListeners_) {
			commandThreadListeners_.add(commandThreadListener_);
		}
	}

	private void fireThreadFailed(final CommandThreadEvent commandThreadEvent_) {
		final List commandThreadListeners_ = getCommandThreadListeners();

		synchronized (commandThreadListeners_) {
			final int size_ = commandThreadListeners_.size();

			for (int i = 0; i  < size_; ++i) {
				((CommandThreadListener) commandThreadListeners_.get(i)).threadFailed(commandThreadEvent_);
			}
		}
	}

	private void fireThreadRan(final CommandThreadEvent commandThreadEvent_) {
		final List commandThreadListeners_ = getCommandThreadListeners();

		synchronized (commandThreadListeners_) {
			final int size_ = commandThreadListeners_.size();

			for (int i = 0; i  < size_; ++i) {
				((CommandThreadListener) commandThreadListeners_.get(i)).threadRan(commandThreadEvent_);
			}
		}
	}

	private void fireThreadRunning(final CommandThreadEvent commandThreadEvent_) {
		final List commandThreadListeners_ = getCommandThreadListeners();

		synchronized (commandThreadListeners_) {
			final int size_ = commandThreadListeners_.size();

			for (int i = 0; i  < size_; ++i) {
				((CommandThreadListener) commandThreadListeners_.get(i)).threadRunning(commandThreadEvent_);
			}
		}
	}

	private int getAttempts() {
		return attempts;
	}

	private int getAttemptInterval() {
		return attemptInterval;
	}

	private List getCommandThreadListeners() {
		return commandThreadListeners;
	}

	public NapsterService getNapsterService() {
		return napsterService;
	}

	public NapsterCommand getNapsterCommand() {
		return napsterCommand;
	}

	public void removeCommandThreadListener(final CommandThreadListener commandThreadListener_) {
		final List commandThreadListeners_ = getCommandThreadListeners();

		synchronized (commandThreadListeners_) {
			commandThreadListeners_.remove(commandThreadListener_);
		}
	}

	public void run() {
		final CommandThreadEvent commandThreadEvent_ = new CommandThreadEvent(this);
		boolean executed_ = false;

		try {
			fireThreadRunning(commandThreadEvent_);
			final NapsterService napsterService_ = getNapsterService();
			final NapsterCommand napsterCommand_ = getNapsterCommand();
			final int attempts_ = getAttempts();
			final int attemptIntervalMs_ = getAttemptInterval() * 1000;

			for (int i = 0; i < attempts_; ++i) {
				try {
					napsterService_.execute(napsterCommand_);
					executed_ = true;
					break;
				} catch (Exception exception_) {
					try {
						sleep(attemptIntervalMs_);
					} catch (InterruptedException interruptedException_) {
					}
				}
			}
		} finally {
			if (executed_) {
				fireThreadRan(commandThreadEvent_);
			} else {
				fireThreadFailed(commandThreadEvent_);
			}
		}
	}
}

