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

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import java.lang.reflect.InvocationTargetException;

public class SongField implements Comparable {
	private final static Map SONG_FIELDS = new HashMap(9, 1.0f);
	private final static SongField[] SONG_FIELD_ARRAY = new SongField[0];
	private final static Class[] CLASSES = new Class[0];
	private final static Object[] OBJECTS = new Object[0];

	public final static SongField FILE_NAME = new SongField("File Name", "getFileName", 'F', Object.class);
	public final static SongField MD5 = new SongField("MD5", "getMd5", 'M', Object.class);
	public final static SongField SIZE = new SongField("Size", "getSize", 'S', Number.class);
	public final static SongField BIT_RATE = new SongField("Bit Rate", "getBitRate", 'B', Number.class);
	public final static SongField FREQUENCY = new SongField("Frequency", "getFrequency", 'q', Number.class);
	public final static SongField LENGTH = new SongField("Length", "getLength", 'L', Number.class);
	public final static SongField USER = new SongField("User", "getUser", 'U', Object.class);
	public final static SongField CONNECTION = new SongField("Connection", "getConnection", 'C', Object.class);
	public final static SongField WEIGHT = new SongField("Weight", "getWeight", 'W', Number.class);

	public static SongField getSongField(final String key_) {
		return (SongField) SONG_FIELDS.get(key_);
	}

   public static SongField[] getValidSongFields() {
      final SongField[] validSongFields_ = (SongField[]) SONG_FIELDS.values().toArray(SONG_FIELD_ARRAY);
      Arrays.sort(validSongFields_);

      return validSongFields_;
   }

	private final String description;
	private final String getter;
	private final char mnemonic;
	private final Class columnClass;

	private SongField(final String description_, final String getter_, final char mnemonic_, final Class columnClass_) {
		description = description_;
		getter = getter_;
		mnemonic = mnemonic_;
		columnClass = columnClass_;

		SONG_FIELDS.put(description, this);
	}

	public int compareTo(final Object object_) {
		if (this == object_) {
			return 0;
		}

		if (getClass() != object_.getClass()) {
			throw new ClassCastException();
		}
		
		return getDescription().compareTo(((SongField) object_).getDescription());
	}

	public Object get(final Song song_) {
		Object object_ = null;

		try {
			object_ = Song.class.getMethod(getGetter(), CLASSES).invoke(song_, OBJECTS);
		} catch (IllegalAccessException illegalAccessException_) {
			throw new RuntimeException("IllegalAccessException");
		} catch (IllegalArgumentException illegalArgumentException_) {
			throw new RuntimeException("IllegalArgumentException");
		} catch (InvocationTargetException invocationTargetException_) {
			throw new RuntimeException("InvocationTargetException");
		} catch (NoSuchMethodException noSuchMethodException_) {
			throw new RuntimeException("NoSuchMethodException");
		} catch (SecurityException securityException_) {
			throw new RuntimeException("SecurityException");
		}

		return object_;
	}

	public Class getColumnClass() {
		return columnClass;
	}

	private String getGetter() {
		return getter;
	}

	public String getDescription() {
		return description;
	}

	public char getMnemonic() {
		return mnemonic;
	}

	public String toString() {
		return getDescription();
	}
}

