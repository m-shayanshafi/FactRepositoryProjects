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

package napsack.servers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import napsack.commands.NapsterCommand;
import napsack.protocol.Message;
import napsack.protocol.MessageType;

public class NapsterNetwork implements NapsterService {
	private final String identifier;
	private final List napsterServices;
	private volatile String parentNetworkName;
	private volatile NapsterService currentNapsterService;

	public NapsterNetwork(final String identifier_) {
		identifier = identifier_;
		napsterServices = new ArrayList();

		setParentNetworkName(identifier_);
	}

	public boolean addNapsterService(final NapsterService napsterService_) {
		boolean add_ = false;

		final List napsterServices_ = getNapsterServices();
		synchronized (napsterServices_) {
			add_ = napsterServices_.add(napsterService_);
		}

		if (add_) {
			napsterService_.setParentNetworkName(getIdentifier());
		}

		return add_;
	}
		
	public void addNapsterService(int index_, NapsterService napsterService_) {
		napsterService_.setParentNetworkName(getIdentifier());

		final List napsterServices_ = getNapsterServices();
		synchronized (napsterServices_) {
			napsterServices_.add(index_, napsterService_);
		}
	}

	public void connect() throws IOException {
		final NapsterService currentNapsterService_ = getCurrentNapsterService();

		if (currentNapsterService_ == null) {
			throw new IOException("The " + getIdentifier() + " network is empty.");
		}

		currentNapsterService_.connect();
	}

	public void disconnect() throws IOException {
		final NapsterService currentNapsterService_ = getCurrentNapsterService();

		if (currentNapsterService_ == null) {
			throw new IOException("The " + getIdentifier() + " network is empty.");
		}

		currentNapsterService_.disconnect();
	}

	public void execute(final NapsterCommand napsterCommand_) throws IOException {
		final NapsterService currentNapsterService_ = getCurrentNapsterService();

		if (currentNapsterService_ == null) {
			throw new IOException("The " + getIdentifier() + " network is empty.");
		}

		final List napsterServices_ = getNapsterServices();
		boolean executed_ = false;

		synchronized (napsterServices_) {
			final int size_ = napsterServices_.size();

			for (int i = napsterServices_.indexOf(currentNapsterService_), j = 0; j < size_; i = (i + 1) % size_, ++j) {
				try {
					setCurrentNapsterService((NapsterService) napsterServices_.get(i));
					napsterCommand_.execute(this);
					executed_ = true;
					break;
				} catch (Exception exception_) {
					try {
						disconnect();
					} catch (IOException ioException_) {
					} finally {
						napsterCommand_.reset();
					}
				}
			} 

			if (!executed_) {
				setCurrentNapsterService(currentNapsterService_);
				throw new IOException("Cannot communicate with the " + getIdentifier() + " network.");
			}
		}
	}

	public ServiceAddress getAddress() {
		final NapsterService currentNapsterService_ = getCurrentNapsterService();
		ServiceAddress address_ = null;

		if (currentNapsterService_ != null) {
			address_ = currentNapsterService_.getAddress();
		}

		return address_;
	}

	public NapsterService getCurrentNapsterService() {
		if (currentNapsterService == null) {
			synchronized (this) {
				if (currentNapsterService == null) {
					final List napsterServices_ = getNapsterServices();
					synchronized (napsterServices_) {
						if (napsterServices_.size() > 0) {
							setCurrentNapsterService((NapsterService) napsterServices_.get(0));
						}
					}
				}
			}
		}

		return currentNapsterService;
	}

	public String getIdentifier() {
		return identifier;
	}

	private List getNapsterServices() {
		return napsterServices;
	}

	public String getParentNetworkName() {
		return parentNetworkName;
	}

	public ServiceStatistics getStatistics() {
		final NapsterService currentNapsterService_ = getCurrentNapsterService();
		ServiceStatistics statistics_ = null;

		if (currentNapsterService_ != null) {
			statistics_ = currentNapsterService_.getStatistics();
		}

		return statistics_;
	}

	public NapsterService removeNapsterService(final int index_) {
		NapsterService napsterService_ = null;
		final List napsterServices_ = getNapsterServices();
		synchronized (napsterServices_) {
			napsterService_ = (NapsterService) napsterServices_.remove(index_);
		}

		if (napsterService_ != null) {
			napsterService_.setParentNetworkName(null);

			if (napsterService_.equals(getCurrentNapsterService())) {
				setCurrentNapsterService(null);
			}
		}

		return napsterService_;
	}

	public boolean removeNapsterService(final NapsterService napsterService_) {
		boolean remove_ = false;
		final List napsterServices_ = getNapsterServices();
		synchronized (napsterServices_) {
			remove_ = getNapsterServices().remove(napsterService_);
		}

		if (remove_) {
			napsterService_.setParentNetworkName(null);

			if (napsterService_.equals(getCurrentNapsterService())) {
				setCurrentNapsterService(null);
			}
		}

		return remove_;
	}

	public void sendMessage(final Message message_) throws IOException {
		final NapsterService currentNapsterService_ = getCurrentNapsterService();

		if (currentNapsterService_ == null) {
			throw new IOException("The " + getIdentifier() + " network is empty.");
		}

		currentNapsterService_.sendMessage(message_);
	}

	public Message[] sendMessage(final Message message_, final MessageType[] interestingMessages_, final MessageType[] terminatingMessages_) throws IOException {
		final NapsterService currentNapsterService_ = getCurrentNapsterService();

		if (currentNapsterService_ == null) {
			throw new IOException("The " + getIdentifier() + " network is empty.");
		}

		return currentNapsterService_.sendMessage(message_, interestingMessages_, terminatingMessages_);
	}

	private void setCurrentNapsterService(final NapsterService currentNapsterService_) {
		currentNapsterService = currentNapsterService_;
	}

	public void setParentNetworkName(final String parentNetworkName_) {
		parentNetworkName = parentNetworkName_;
	}

	public String toLongString() {
		String longString_ = getIdentifier();
		final NapsterService currentNapsterService_ = getCurrentNapsterService();

		if (currentNapsterService_ != null) {
			final StringBuffer longStringBuffer_ = new StringBuffer(longString_);

			longStringBuffer_.append(" (");
			longStringBuffer_.append(currentNapsterService_.toLongString());
			longStringBuffer_.append(")");

			longString_ = longStringBuffer_.toString();
		}

		return longString_;
	}

	public String toString() {
		return getIdentifier();
	}
}

