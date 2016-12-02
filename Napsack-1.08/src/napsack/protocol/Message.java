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

import java.io.UnsupportedEncodingException;

import napsack.util.NapsterBytes;
import napsack.util.StringUtils;

public class Message {
	private final MessageType type;
	private final byte[] data;

	public Message(final byte[] typeAndData_) {
		final byte[] type_ = new byte[2];
		final int dataLength_ = typeAndData_.length - 2;

		System.arraycopy(typeAndData_, 0, type_, 0, 2);
		type = MessageType.getMessageType(NapsterBytes.byteArrayToInt(type_));

		data = new byte[dataLength_];
		System.arraycopy(typeAndData_, 2, data, 0, dataLength_);
	}

	public Message(final MessageType type_, final String data_) {
		byte[] tempData_ = null;

		try {
			tempData_ = data_.getBytes(StringUtils.ENCODING);
		} catch (UnsupportedEncodingException unsupportedCodingException_) {
			tempData_ = data_.getBytes();
		}

		type = type_;
		data = tempData_;
	}

	public byte[] getData() {
		return data;
	}

	public MessageType getType() {
		return type;
	}

	public byte[] toBytes() {
		final byte[] data_ = getData();
		final byte[] bytes_ = new byte[data_.length + 4];

		System.arraycopy(NapsterBytes.intToByteArray(data_.length), 0, bytes_, 0, 2);
		System.arraycopy(NapsterBytes.intToByteArray(getType().getCode()), 0, bytes_, 2, 2);
		System.arraycopy(data_, 0, bytes_, 4, data_.length);

		return bytes_;
	}

	public String toString() {
		String string_ = null;

		try {
			string_ = new String(getData(), StringUtils.ENCODING);
		} catch (UnsupportedEncodingException unsupportedEncodingException_) {
			string_ = new String(getData());
		}

		return string_;
	}

	public String toString(final int maxLength_) {
		return StringUtils.crop(toString(), maxLength_);
	}
}

