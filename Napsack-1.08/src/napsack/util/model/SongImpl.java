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

package napsack.util.model;

import java.io.UnsupportedEncodingException;
import java.text.DecimalFormat;
import java.util.StringTokenizer;

import napsack.protocol.BitRate;
import napsack.protocol.Connection;
import napsack.protocol.Frequency;
import napsack.protocol.Message;
import napsack.util.StringUtils;

public class SongImpl implements Song {
	private final static Integer ZERO = new Integer(0);

   final static float MB = 1048576f;
   final static DecimalFormat SIZE_FORMAT = new DecimalFormat("###.## MB");
   final static DecimalFormat MIN_FORMAT = new DecimalFormat("##0");
   final static DecimalFormat SEC_FORMAT = new DecimalFormat("00");

	public class SongView implements Song {
		private String length;
		private String size;

		public SongView() {
			final int length_ = SongImpl.this.length.intValue();

			size = SIZE_FORMAT.format(SongImpl.this.size.intValue() / MB);
			length = MIN_FORMAT.format(length_ / 60) + ":" + SEC_FORMAT.format(length_ % 60);
		}

		public Object getBitRate() {
			return bitRate.getCode();
		}

		public Object getConnection() {
			return connection;
		}

		public Object getFileName() {
			return fileName;
		}

		public Object getFrequency() {
			return frequency.getCode();
		}

		public Object getMd5() {
			return md5;
		}

		public Object getLength() {
			return length;
		}

		public Object getSize() {
			return size;
		}

		public Object getUser() {
			return user;
		}

		public Song getView() {
			return this;
		}

		public Object getWeight() {
			return weight;
		}
	}

	private final BitRate bitRate;
	private final Connection connection;
	private final String fileName;
	private final Frequency frequency;
	private final Integer length;
	private final String md5;
	private final Integer size;
	private final String user;
	private SongView view;
	private final Integer weight;

	public SongImpl(final BitRate bitRate_, final Connection connection_, final String fileName_, final Frequency frequency_, final Integer length_, final String md5_, final Integer size_, final String user_, final Integer weight_) {
		bitRate = bitRate_;
		connection = connection_;
		frequency = frequency_;
		fileName = fileName_;
		length = length_;
		md5 = md5_;
		size = size_;
		user = user_;
		weight = weight_;
	}

	public SongImpl(final Message message_) {
      String data_;

      try {
         data_ = new String(message_.getData(), StringUtils.ENCODING);
      } catch (UnsupportedEncodingException unsupportedEncodingException_) {
         data_ = new String(message_.getData());
      }

      final int endSong_ = data_.lastIndexOf('\"');
      int beginSong_ = 1;

		// Handle the special case that a backslash exists past the end of the
 		// file name.
		if (data_.indexOf('\\') > -1 && (beginSong_ = data_.lastIndexOf('\\') + 1) > endSong_) {
			final String tempFileName_ = data_.substring(0, endSong_);
			beginSong_ = tempFileName_.indexOf('\\') > -1 ? tempFileName_.lastIndexOf('\\') : 1;
		}

      fileName = data_.substring(beginSong_, endSong_);

      final StringTokenizer properties_ = new StringTokenizer(data_.substring(endSong_ + 2), " ");

      md5 = properties_.nextToken();
		size = Integer.valueOf(properties_.nextToken());
		bitRate = BitRate.createBitRateByCode(Integer.valueOf(properties_.nextToken()));
		frequency = Frequency.createFrequencyByCode(Integer.valueOf(properties_.nextToken()));
		length = Integer.valueOf(properties_.nextToken());
		user = properties_.nextToken();
		properties_.nextToken();
		connection = Connection.getConnectionByCode(Integer.valueOf(properties_.nextToken()));

		if (properties_.hasMoreTokens()) {
			weight = Integer.valueOf(properties_.nextToken());
		} else {
			weight = ZERO;
		}
	}

	public Object getBitRate() {
		return bitRate;
	}

	public Object getConnection() {
		return connection;
	}

	public Object getFileName() {
		return fileName;
	}

	public Object getFrequency() {
		return frequency;
	}

	public Object getLength() {
		return length;
	}

	public Object getMd5() {
		return md5;
	}

	public Object getSize() {
		return size;
	}

	public Object getUser() {
		return user;
	}

	public Song getView() {
		if (view == null) {
			view = new SongView();
		}

		return view;
	}

	public Object getWeight() {
		return weight;
	}
}

