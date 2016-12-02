package com.carolinarollergirls.scoreboard.file;

import java.io.*;
import java.text.*;
import java.util.*;

import com.carolinarollergirls.scoreboard.*;

public class ScoreBoardToFile extends AbstractScoreBoardFileIO
{
	public ScoreBoardToFile(String filename) throws IOException {
		this(filename, null);
	}

	public ScoreBoardToFile(String filename, String ext) throws IOException {
		ext = ((null == ext) ? "" : ("".equals(ext) ? "" : "."+ext));
		file = new File(getDirectory(), filename+ext);
		NumberFormat nf = NumberFormat.getInstance();
		nf.setMinimumIntegerDigits(3);
		int count = 0;
		while (file.exists())
			file = new File(getDirectory(), filename+"_"+(nf.format(count++))+ext);
		file.createNewFile();
		if (!file.isFile() || !file.canWrite())
			throw new IOException("Could not create writable file "+filename);
	}

	public File getFile() { return file; }

	protected File file = null;
}
