package com.carolinarollergirls.scoreboard.xml;

import java.util.*;

import org.w3c.dom.*;

import com.carolinarollergirls.scoreboard.*;
import com.carolinarollergirls.scoreboard.model.*;

public class RealtimeScoreBoardXmlConverter extends ScoreBoardXmlConverter
{
	/**
	 * Process the given document at its appropriate time based on its timestamp.
	 * <p>
	 * This is obviously not synchronized, and designed for single-threaded operation,
	 * one timestamped document at a time.  The processing of each document will be delayed until
	 * proper execution time, based on its timestamp relative to the first processed document's timestamp.
	 */
	public void processDocumentRealtime(ScoreBoardModel scoreBoardModel, Document document) {
		waitUntilRunTime(document);

		processDocument(scoreBoardModel, document);
	}

	/**
	 * This resets the start timestamp.  This will be reset to the next document's relative timestamp.
	 */
	public void reset() {
		startDocTime = 0;
		startRealTime = 0;
	}

	/**
	 * Wait until the given document's relative timestamp time.
	 */
	protected void waitUntilRunTime(Document document) {
		try {
			long docTime = editor.getSystemTime(document);
			long realTime = new Date().getTime();

			if (startDocTime == 0 || startRealTime == 0) {
				startDocTime = docTime;
				startRealTime = realTime;
			}

			if (docTime < startDocTime)
				throw new NumberFormatException("Invalid document timestamp ("+docTime+"), less than start document time ("+startDocTime+")");

			long elapsedDocTime = docTime - startDocTime;
			long elapsedRealTime = realTime - startRealTime;
			long sleepTime = elapsedDocTime - elapsedRealTime;

			if (sleepTime > 0)
				Thread.sleep(sleepTime);
		} catch ( NumberFormatException nfE ) {
			System.err.println("No system time found for document; executing now.  Error: "+nfE.getMessage());
		} catch ( InterruptedException iE ) {
			System.err.println("Interrupted while waiting for document time to occur.  Error: "+iE.getMessage());
		}
	}

	protected long startDocTime = 0;
	protected long startRealTime = 0;
}
