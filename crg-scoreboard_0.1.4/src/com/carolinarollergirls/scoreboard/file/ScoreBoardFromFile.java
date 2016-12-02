package com.carolinarollergirls.scoreboard.file;

import java.io.*;
import java.util.*;
import java.nio.charset.*;

import com.carolinarollergirls.scoreboard.*;

public abstract class ScoreBoardFromFile extends AbstractScoreBoardFileIO
{
	public ScoreBoardFromFile(String filename) throws IOException {
		file = new File(getDirectory(), filename);
		if (!file.isFile() || !file.canRead())
			throw new IOException("Could not read file "+filename);
	}

	public File getFile() { return file; }

	public boolean isLoading() { return loading; }

	public void start() {
		synchronized (loadLock) {
			if (loading)
				return;

			loading = true;
		}

		Runnable r = new Runnable() {
			public void run() { ScoreBoardFromFile.this.run(); }
		};
		loadingThread = new Thread(r);
		loadingThread.start();
	}

	public void stop() {
		synchronized (loadLock) {
			if (!loading)
				return;

			loading = false;
			loadingThread.interrupt();
			try {
				loadLock.wait(5000); /* This timeout should never be needed, but just in case don't wait forever */
			} catch ( InterruptedException iE ) {
			}
		}
	}

	protected abstract void process(char[] chars, int length);

	protected void run() {
		Reader reader = null;

		try {
			reader = new BufferedReader(new InputStreamReader(new FileInputStream(getFile()), Charset.forName("UTF-8")));

			char[] chars = new char[1024];
			int len = 0;
			while (-1 < (len = reader.read(chars, 0, 1024))) {
				process(chars, len);

				synchronized (loadLock) {
					if (!loading)
						break;
				}
			}

			if (loading)
				process(null, 0);
		} catch ( IOException ioE ) {
			System.err.println("IOException while loading from file : "+ioE.getMessage());
		} finally {
			synchronized (loadLock) {
				loading = false;
				loadLock.notifyAll();
			}

			try { reader.close(); } catch ( Exception e ) { }
		}
	}

	protected File file = null;
	protected boolean loading = false;
	protected Object loadLock = new Object();
	protected Thread loadingThread = null;
}
