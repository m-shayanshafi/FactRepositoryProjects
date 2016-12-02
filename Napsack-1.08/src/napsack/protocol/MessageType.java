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

package napsack.protocol;

import java.util.Hashtable;

public class MessageType implements Comparable {
	private final static Hashtable MESSAGE_TYPES = new Hashtable();

	public final static MessageType LOGIN_ERROR = getMessageType(0);
	public final static MessageType LOGIN = getMessageType(2);
	public final static MessageType LOGIN_ACK = getMessageType(3);
	public final static MessageType VERSION_CHECK = getMessageType(4);
	public final static MessageType AUTO_UPGRADE = getMessageType(5);
	public final static MessageType NEW_USER_LOGIN = getMessageType(6);
	public final static MessageType NICK_CHECK = getMessageType(7);
	public final static MessageType NICK_NOT_REGISTERED = getMessageType(8);
	public final static MessageType NICK_ALREADY_REGISTERED = getMessageType(9);
	public final static MessageType INVALID_NICK = getMessageType(10);
	public final static MessageType SEARCH_REQUEST = getMessageType(200);
	public final static MessageType SEARCH_RESPONSE = getMessageType(201);
	public final static MessageType END_OF_SEARCH_RESPONSE = getMessageType(202);
	public final static MessageType ERROR = getMessageType(404);

	private final int code;

	private MessageType(final int code_) {
		code = code_;
	}

	public int compareTo(Object object_) {
		if (this == object_) {
			return 0;
		}

		if (getClass() != object_.getClass()) {
			throw new ClassCastException();
		}

		final int thisCode_ = getCode();
		final int otherCode_ = ((MessageType) object_).getCode();

		return thisCode_ > otherCode_ ? 1 : thisCode_ < otherCode_ ? -1 : 0;
	}

	public static MessageType getMessageType(final int code_) {
		final Integer intCode_ = new Integer(code_);
		MessageType messageType_ = null;

		if ((messageType_ = (MessageType) MESSAGE_TYPES.get(intCode_)) == null) {
			synchronized (MessageType.class) {
				if ((messageType_ = (MessageType) MESSAGE_TYPES.get(intCode_)) == null) {
					MESSAGE_TYPES.put(intCode_, messageType_ = new MessageType(code_));
				}
			}
		}

		return messageType_;
	}

	public int getCode() {
		return code;
	}
}

