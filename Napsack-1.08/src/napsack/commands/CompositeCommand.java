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

import napsack.servers.NapsterService;
import napsack.util.ReadWriteController;

public class CompositeCommand extends NapsterCommand {
	private volatile List napsterCommands;
	private volatile ReadWriteController commandController;

	public CompositeCommand() {
		napsterCommands = new ArrayList();
		commandController = new ReadWriteController();
	}

	public boolean addNapsterCommand(final NapsterCommand napsterCommand_) {
		final ReadWriteController commandController_ = getCommandController();
		boolean add_ = false;
	
		synchronized (commandController_) {
			commandController_.waitForWritePermission();
			add_ = getNapsterCommands().add(napsterCommand_);
			commandController_.notifyAll();
		}

		return add_;
	}
	
	public void addNapsterCommand(final int index_, final NapsterCommand napsterCommand_) {
		final ReadWriteController commandController_ = getCommandController();
	
		synchronized (commandController_) {
			commandController_.waitForWritePermission();
			getNapsterCommands().add(index_, napsterCommand_);
			commandController_.notifyAll();
		}
	}

	public Object clone() {
		CompositeCommand compositeCommand_ = null;

		compositeCommand_ = (CompositeCommand) super.clone();

		compositeCommand_.setCommandController(new ReadWriteController());
		compositeCommand_.setNapsterCommands(new ArrayList());

		final ReadWriteController commandController_ = getCommandController();
		final List napsterCommands_ = getNapsterCommands();

		commandController_.incrementReaderCount();

		final int size_ = napsterCommands_.size();
		for (int i = 0; i < size_; ++i) {
			compositeCommand_.addNapsterCommand((NapsterCommand) ((NapsterCommand) napsterCommands_.get(i)).clone());
		}

		commandController_.decrementReaderCount();

		return compositeCommand_;
	}

	public void doExecute(final NapsterService napsterService_) throws Exception {
		final ReadWriteController commandController_ = getCommandController();
		final List napsterCommands_ = getNapsterCommands();

		commandController_.incrementReaderCount();

		final int size_ = napsterCommands_.size();
		for (int i = 0; i < size_; i++) {
			((NapsterCommand) napsterCommands_.get(i)).execute(napsterService_);
		}

		commandController_.decrementReaderCount();
	}
		
	protected ReadWriteController getCommandController() {
		return commandController;
	}

	protected List getNapsterCommands() {
		return napsterCommands;
	}

	public NapsterCommand removeNapsterCommand(final int index_) {
		final ReadWriteController commandController_ = getCommandController();
		NapsterCommand napsterCommand_;
	
		synchronized (commandController_) {
			commandController_.waitForWritePermission();
			napsterCommand_ = (NapsterCommand) getNapsterCommands().remove(index_);
			commandController_.notifyAll();
		}

		return napsterCommand_;
	}

	public boolean removeNapsterCommand(final NapsterCommand napsterCommand_) {
		final ReadWriteController commandController_ = getCommandController();
		boolean remove_ = false;

		synchronized (commandController_) {
			commandController_.waitForWritePermission();
			remove_ = getNapsterCommands().remove(napsterCommand_);
			commandController_.notifyAll();
		}

		return remove_;
	}

	public void reset() {
		super.reset();

		final ReadWriteController commandController_ = getCommandController();
		final List napsterCommands_ = getNapsterCommands();

		commandController_.incrementReaderCount();

		final int size_ = napsterCommands_.size();
		for (int i = 0; i < size_; i++) {
			((NapsterCommand) napsterCommands_.get(i)).reset();
		}

		commandController_.decrementReaderCount();
	}

	private void setCommandController(final ReadWriteController commandController_) {
		commandController = commandController_;
	}

	private void setNapsterCommands(final List napsterCommands_) {
		napsterCommands = napsterCommands_;
	}
}

