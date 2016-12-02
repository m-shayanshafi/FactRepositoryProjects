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

import napsack.event.ExceptionEvent;
import napsack.event.ExceptionListener;
import napsack.event.CommandCampaignEvent;
import napsack.event.CommandCampaignListener;
import napsack.event.CommandThreadAdapter;
import napsack.event.CommandThreadEvent;
import napsack.event.CommandThreadListener;
import napsack.servers.Napigator;
import napsack.servers.NapsterService;
import napsack.util.properties.CommandAttemptsProperty;
import napsack.util.properties.CommandAttemptIntervalProperty;
import napsack.util.properties.ExcludedNetworksProperty;
import napsack.util.properties.MaxThreadsProperty;
import napsack.util.properties.NapsackProperties;

public class CommandCampaign extends Thread {
	private final NapsterCommand napsterCommand;
	private final int maxThreads;
	private final List commandCampaignListeners;
	private final List commandThreadListeners;
	private final List exceptionListeners;

	private int threadCount;
	private volatile boolean aborted;

	public CommandCampaign(final NapsterCommand napsterCommand_) {
		napsterCommand = napsterCommand_;
		maxThreads = ((Integer) MaxThreadsProperty.getInstance().getValue()).intValue();
		commandCampaignListeners = new ArrayList();
		commandThreadListeners = new ArrayList();
		exceptionListeners = new ArrayList();

		addCommandThreadListener(new CommandThreadAdapter() {
			public void threadRan(final CommandThreadEvent commandThreadEvent_) {
				decrementThreadCount();
			}

			public void threadFailed(final CommandThreadEvent commandThreadEvent_) {
				decrementThreadCount();
			}
		});

		setPriority(Math.min(NORM_PRIORITY + 1, MAX_PRIORITY));
	}

	public void abort() {
		setAborted(true);
		interrupt();
	}

	public void addExceptionListener(final ExceptionListener exceptionListener_) {
		final List exceptionListeners_ = getExceptionListeners();

		synchronized (exceptionListeners_) {
			exceptionListeners_.add(exceptionListener_);
		}
	}

	public void addCommandCampaignListener(final CommandCampaignListener commandCampaignListener_) {
		final List commandCampaignListeners_ = getCommandCampaignListeners();

		synchronized (commandCampaignListeners_) {
			commandCampaignListeners_.add(commandCampaignListener_);
		}
	}

	public void addCommandThreadListener(final CommandThreadListener commandThreadListener_) {
		final List commandThreadListeners_ = getCommandThreadListeners();

		synchronized (commandThreadListeners_) {
			commandThreadListeners_.add(commandThreadListener_);
		}
	}

	private synchronized void decrementThreadCount() {
		int maxThreads_ = getMaxThreads();
		--threadCount;

		if (maxThreads_ <= 0 || threadCount < maxThreads_) {
			notifyAll();
		}
	}

	public void fireCampaignAborted() {
		fireCampaignAborted(new CommandCampaignEvent(this, null));
	}

	public void fireCampaignAborted(final CommandCampaignEvent commandCampaignEvent_) {
		final List commandCampaignListeners_ = getCommandCampaignListeners();

		synchronized (commandCampaignListeners_) {
			final int size_ = commandCampaignListeners_.size();

			for (int i = 0; i < size_; ++i) {
				((CommandCampaignListener) commandCampaignListeners_.get(i)).campaignAborted(commandCampaignEvent_);
			}
		}
	}

	private void fireCampaignCompleted(final CommandCampaignEvent commandCampaignEvent_) {
		final List commandCampaignListeners_ = getCommandCampaignListeners();

		synchronized (commandCampaignListeners_) {
			final int size_ = commandCampaignListeners_.size();

			for (int i = 0; i < size_; ++i) {
				((CommandCampaignListener) commandCampaignListeners_.get(i)).campaignCompleted(commandCampaignEvent_);
			}
		}
	}

	private void fireCampaignLaunched(final CommandCampaignEvent commandCampaignEvent_) {
		final List commandCampaignListeners_ = getCommandCampaignListeners();

		synchronized (commandCampaignListeners_) {
			final int size_ = commandCampaignListeners_.size();

			for (int i = 0; i < size_; ++i) {
				((CommandCampaignListener) commandCampaignListeners_.get(i)).campaignLaunched(commandCampaignEvent_);
			}
		}
	}

	public void fireExceptionThrown(final ExceptionEvent exceptionEvent_) {
		final List exceptionListeners_ = getExceptionListeners();

		synchronized (exceptionListeners_) {
			final int size_ = exceptionListeners_.size();

			for (int i = 0; i < size_; ++i) {
				((ExceptionListener) exceptionListeners_.get(i)).exceptionThrown(exceptionEvent_);
			}
		}
	}

	private List getCommandThreadListeners() {
		return commandThreadListeners;
	}

	private List getCommandCampaignListeners() {
		return commandCampaignListeners;
	}

	private List getExceptionListeners() {
		return exceptionListeners;
	}

	private int getMaxThreads() {
		return maxThreads;
	}

	public NapsterCommand getNapsterCommand() {
		return napsterCommand;
	}

	private synchronized void incrementThreadCount() throws InterruptedException {
		int maxThreads_ = 0;

		while ((maxThreads_ = getMaxThreads()) > 0 && threadCount >= maxThreads_) {
			wait();
		}

		++threadCount;
	}

	private boolean isAborted() {
		return aborted;
	}

	public void removeExceptionListener(final ExceptionListener exceptionListener_) {
		final List exceptionListeners_ = getExceptionListeners();

		synchronized (exceptionListeners_) {
			exceptionListeners_.remove(exceptionListener_);
		}
	}

	public void removeCommandCampaignListener(final CommandCampaignListener commandCampaignListener_) {
		final List commandCampaignListeners_ = getCommandCampaignListeners();

		synchronized (commandCampaignListeners_) {
			commandCampaignListeners_.remove(commandCampaignListener_);
		}
	}

	public void removeCommandThreadListener(final CommandThreadListener commandThreadListener_) {
		final List commandThreadListeners_ = getCommandThreadListeners();

		synchronized (commandThreadListeners_) {
			commandThreadListeners_.remove(commandThreadListener_);
		}
	}

	public void run() {
		final NapsterCommand napsterCommand_ = getNapsterCommand();
		final List commandThreadListeners_ = getCommandThreadListeners();
		final List exceptionListeners_ = getExceptionListeners();
		final NapsackProperties napsackProperties_ = NapsackProperties.getInstance();
		final int commandAttempts_ = ((Integer) CommandAttemptsProperty.getInstance().getValue()).intValue();
		final int commandAttemptInterval_ = ((Integer) CommandAttemptIntervalProperty.getInstance().getValue()).intValue();
		NapsterService[] napsterServices_ = null;

		try {
      	napsterServices_ = Napigator.getInstance().getNapsterServices((String[]) ExcludedNetworksProperty.getInstance().getValue(), napsackProperties_.getMinStats());

			final CommandThread[] commandThreads_ = new CommandThread[napsterServices_.length];
			int i = 0;

			fireCampaignLaunched(new CommandCampaignEvent(this, napsterServices_));

			for (i = 0; i < napsterServices_.length && !isAborted(); ++i) {
				try {
					incrementThreadCount();
					commandThreads_[i] = new CommandThread(napsterServices_[i], (NapsterCommand) napsterCommand_.clone(), commandAttempts_, commandAttemptInterval_, commandThreadListeners_);
					commandThreads_[i].start();
				} catch (InterruptedException interruptedException_) {
				}

				yield();
			}

			for (int j = 0; j < i; ++j) {
				if (isAborted()) {
					commandThreads_[j].interrupt();
				}
			}

			for (int j = 0; j < i && !isAborted(); ++j) {
				boolean joined_ = false;

				do {
					try {
						commandThreads_[j].join();
						joined_ = true;
					} catch (InterruptedException interruptedException_) {
					}
				} while (!joined_);

				yield();
			}
		} catch (Exception exception_) {
			fireExceptionThrown(new ExceptionEvent(this, exception_));
		} finally {
			if (!isAborted()) {
				fireCampaignCompleted(new CommandCampaignEvent(this, napsterServices_));
			}
		}
	}

	private void setAborted(final boolean aborted_) {
		aborted = aborted_;
	}
}

