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

import napsack.event.CommandEvent;
import napsack.event.CommandListener;
import napsack.protocol.Message;
import napsack.servers.NapsterService;
import napsack.util.ReadWriteController;

public abstract class NapsterCommand implements Cloneable {
	private volatile Message[] returnedMessages;
	private final ReadWriteController listenerController;
	private final List commandListeners;

	protected NapsterCommand() {
		this(new ArrayList());
	}

	protected NapsterCommand(final List commandListeners_) {
		commandListeners = commandListeners_;
		listenerController = new ReadWriteController();
	}

	public void addCommandListener(final CommandListener commandListener_) {
		final ReadWriteController listenerController_ = getListenerController();

		synchronized (listenerController_) {
			listenerController_.waitForWritePermission();
			getCommandListeners().add(commandListener_);
			listenerController_.notifyAll();
		}
	}

	public abstract boolean addNapsterCommand(NapsterCommand napsterCommand_);

	public abstract void addNapsterCommand(int index_, NapsterCommand napsterCommand_);

	public Object clone() {
		NapsterCommand napsterCommand_ = null;

		try {
			napsterCommand_ = (NapsterCommand) super.clone();
			napsterCommand_.setReturnedMessages(null);
		} catch (CloneNotSupportedException cloneNotSupportedException_) {
			throw new RuntimeException("CloneNotSupportedException");
		}

		return napsterCommand_;
	}

	protected abstract void doExecute(NapsterService napsterService_) throws Exception;

	public final void execute(final NapsterService napsterService_) throws Exception {
		final CommandEvent commandEvent_ = new CommandEvent(this, napsterService_);

		try {
			fireCommandExecuting(commandEvent_);
			doExecute(napsterService_);
			fireCommandExecuted(commandEvent_);
		} catch (Exception exception_) {
			fireCommandFailed(new CommandEvent(this, napsterService_, exception_));

			throw exception_;
		}
	}

	private void fireCommandExecuted(final CommandEvent commandEvent_) {
		final ReadWriteController listenerController_ = getListenerController();
		final List commandListeners_ = getCommandListeners();

		listenerController_.incrementReaderCount();

		final int size_ = commandListeners_.size();
		for (int i = 0; i < size_; ++i) {
			((CommandListener) commandListeners_.get(i)).commandExecuted(commandEvent_);
		}

		listenerController_.decrementReaderCount();
	}


	private void fireCommandExecuting(final CommandEvent commandEvent_) {
		final ReadWriteController listenerController_ = getListenerController();
		final List commandListeners_ = getCommandListeners();

		listenerController_.incrementReaderCount();

		final int size_ = commandListeners_.size();
		for (int i = 0; i < size_; ++i) {
			((CommandListener) commandListeners_.get(i)).commandExecuting(commandEvent_);
		}

		listenerController_.decrementReaderCount();
	}

	private void fireCommandFailed(final CommandEvent commandEvent_) {
		final ReadWriteController listenerController_ = getListenerController();
		final List commandListeners_ = getCommandListeners();

		listenerController_.incrementReaderCount();

		final int size_ = commandListeners_.size();
		for (int i = 0; i < size_; ++i) {
			((CommandListener) commandListeners_.get(i)).commandFailed(commandEvent_);
		}

		listenerController_.decrementReaderCount();
	}

	private List getCommandListeners() {
		return commandListeners;
	}

	private ReadWriteController getListenerController() {
		return listenerController;
	}

	public Message[] getReturnedMessages() {
		return returnedMessages;
	}

	public void removeCommandListener(final CommandListener commandListener_) {
		final ReadWriteController listenerController_ = getListenerController();

		synchronized (listenerController_) {
			listenerController_.waitForWritePermission();
			getCommandListeners().remove(commandListener_);
			listenerController_.notifyAll();
		}
	}

	public abstract NapsterCommand removeNapsterCommand(int index_);

	public abstract boolean removeNapsterCommand(NapsterCommand napsterCommand_);

	public void reset() {
		setReturnedMessages(null);
	}

	protected void setReturnedMessages(final Message[] returnedMessages_) {
		returnedMessages = returnedMessages_;
	}
}

