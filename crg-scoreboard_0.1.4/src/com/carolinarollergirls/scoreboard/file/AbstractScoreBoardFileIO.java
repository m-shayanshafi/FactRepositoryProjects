package com.carolinarollergirls.scoreboard.file;

import java.io.*;
import java.util.*;

import com.carolinarollergirls.scoreboard.*;

public abstract class AbstractScoreBoardFileIO
{
	protected static File getDirectory() throws IOException {
		synchronized (ScoreBoardToFile.class) {
			if (null == directory) {
				String dirName = ScoreBoardManager.getProperties().getProperty(DIR_KEY);
				File dir = new File(dirName);
				if (!dir.isDirectory() && !dir.mkdirs())
					throw new IOException("Could not create directory "+dirName);
				else
					directory = dir;
			}
		}

		return directory;
	}

	public static List<File> getFiles() throws IOException {
		List<File> list = new LinkedList<File>();
		File[] files = getDirectory().listFiles();
		for (int i=0; i<files.length; i++)
			list.add(files[i]);
		Collections.sort(list);
		return list;
	}

	private static File directory = null;

	public static final String DIR_KEY = AbstractScoreBoardFileIO.class.getName() + ".directory";
}
