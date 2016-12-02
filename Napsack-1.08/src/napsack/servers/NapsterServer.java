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
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Arrays;
import java.util.ArrayList;
import java.util.List;

import napsack.commands.NapsterCommand;
import napsack.protocol.Message;
import napsack.protocol.MessageType;
import napsack.util.NapsterBytes;

public class NapsterServer implements NapsterService {
	private final static Message[] RETURNED_MESSAGES = new Message[0];

	private final ServiceAddress address;
	private final ServiceStatistics statistics;
	private volatile String parentNetworkName;
	private volatile Socket socket;

	public NapsterServer(final ServiceAddress address_, final ServiceStatistics statistics_) {
		address = address_;
		statistics = statistics_;
	}

	public boolean addNapsterService(final NapsterService napsterService_) {
		throw new IllegalArgumentException("NapsterServices may not be added to a NapsterServer.");
	}

	public void addNapsterService(final int index_, final NapsterService napsterService_) {
		throw new IllegalArgumentException("NapsterServices may not be added to a NapsterServer.");
	}

	public void connect() throws IOException {
		if (getSocket() == null) {
			final ServiceAddress address_ = getAddress();

			setSocket(new Socket(address_.getIp(), address_.getPort().intValue()));
		}
	}

	public void disconnect() throws IOException {
		final Socket socket_ = getSocket();

		if (socket_ != null) {
			socket_.shutdownInput();
			socket_.shutdownOutput();
			socket_.close();
			setSocket(null);
		}
	}

	public void execute(final NapsterCommand napsterCommand_) throws Exception {
		napsterCommand_.execute(this);
	}

	public ServiceAddress getAddress() {
		return address;
	}

	public String getIdentifier() {
		final ServiceAddress address_ = getAddress();

		return address_.getIp() + ":" + address_.getPort();
	}

	public String getParentNetworkName() {
		return parentNetworkName;
	}

	private Socket getSocket() {
		return socket;
	}

	public ServiceStatistics getStatistics() {
		return statistics;
	}

	private byte[] read(final int length_) throws IOException {
		int read_ = 0;
		int totalRead_ = 0;
		final byte[] bytes_ = new byte[length_];
		final InputStream inputStream_ = getSocket().getInputStream();

		do {
			read_ = inputStream_.read(bytes_, totalRead_, length_ - totalRead_);

			if (read_ < 0) {
				throw new IOException("EOF reached on socket.");
			}
		} while ((totalRead_ += read_) < length_);

		return bytes_;
	}

	private Message readMessage() throws IOException {
		final int length_ = NapsterBytes.byteArrayToInt(read(2)) + 2;
		final Message message_ = new Message(read(length_));

		return message_;
	}

	public NapsterService removeNapsterService(final int index_) {
		throw new IllegalArgumentException("NapsterServices may not be added to a NapsterServer.");
	}

	public boolean removeNapsterService(final NapsterService napsterService_) {
		throw new IllegalArgumentException("NapsterServices may not be added to a NapsterServer.");
	}

	public void sendMessage(final Message message_) throws IOException {
		getSocket().getOutputStream().write(message_.toBytes());
	}

	public Message[] sendMessage(final Message message_, final MessageType[] interestingMessages_, final MessageType[] terminatingMessages_) throws IOException {
		Message returnedMessage_ = null;
		MessageType returnedMessageType_ = null;
		List returnedMessages_ = new ArrayList();

		Arrays.sort(interestingMessages_);
		Arrays.sort(terminatingMessages_);

		sendMessage(message_);

		returnedMessage_ = readMessage();
		returnedMessageType_ = returnedMessage_.getType();

		while (Arrays.binarySearch(terminatingMessages_, returnedMessageType_) < 0) {
			if (Arrays.binarySearch(interestingMessages_, returnedMessageType_) >= 0) {
				returnedMessages_.add(returnedMessage_);
			}

			returnedMessage_ = readMessage();
			returnedMessageType_ = returnedMessage_.getType();
		}

		if (Arrays.binarySearch(interestingMessages_, returnedMessageType_) >= 0) {
			returnedMessages_.add(returnedMessage_);
		}

		return (Message[]) returnedMessages_.toArray(RETURNED_MESSAGES);
	}

	public void setParentNetworkName(final String parentNetworkName_) {
		parentNetworkName = parentNetworkName_;
	}

	private void setSocket(final Socket socket_) {
		socket = socket_;
	}

	public String toLongString() {
		return toString();
	}

	public String toString() {
		return getIdentifier();
	}
}

