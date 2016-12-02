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

import java.io.PrintStream;
import java.util.Arrays;

import napsack.commands.SongQueryCommand;
import napsack.protocol.Message;
import napsack.protocol.MessageType;
import napsack.servers.NapsterService;
import napsack.text.SongQueryResultFormat;
import napsack.util.SongSorter;

public class SongQueryResults {
	private final String napsterServiceString;
	private final String queryString;
	private final Song[] songs;
	private SongSorter songSorter;
	
	public SongQueryResults(final NapsterService napsterService_, final SongQueryCommand songQueryCommand_) {
		napsterServiceString = napsterService_.toLongString();
		queryString = songQueryCommand_.getQueryMessage().getQueryString();
		songs = createSongs(songQueryCommand_.getReturnedMessages());
	}

	private Song[] createSongs(final Message[] messages_) {
		final Song[] songs_ = new Song[messages_.length];

		for (int i = 0; i < messages_.length; ++i) {
			songs_[i] = new SongImpl(messages_[i]);
		}

		return songs_;
	}

	public String getNapsterServiceString() {
		return napsterServiceString;
	}

	public String getQueryString() {
		return queryString;
	}

	public SongSorter getSongSorter() {
		return songSorter;
	}

	public Song[] getSongs() {
		return songs;
	}

	public void print(final SongQueryResultFormat songQueryResultFormat_) {
		print(songQueryResultFormat_, System.out);
	}

	public void print(final SongQueryResultFormat songQueryResultFormat_, final PrintStream printStream_) {
		final String napsterServiceString_ = getNapsterServiceString();
		final Song[] songs_ = getSongs();

		for (int i = 0; i < songs_.length; ++i) {
			printStream_.println(songQueryResultFormat_.format(napsterServiceString_, songs_[i]));
		}
	}

	public void setSongSorter(final SongSorter songSorter_) {
		if (songSorter_ != null && songSorter_.getSortingFields().length > 0) {
			Arrays.sort(getSongs(), songSorter_);
		}

		songSorter = songSorter_;
	}
}

