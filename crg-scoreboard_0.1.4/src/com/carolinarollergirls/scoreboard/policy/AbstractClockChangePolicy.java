package com.carolinarollergirls.scoreboard.policy;

import java.util.*;

import com.carolinarollergirls.scoreboard.*;
import com.carolinarollergirls.scoreboard.model.*;
import com.carolinarollergirls.scoreboard.defaults.*;

public abstract class AbstractClockChangePolicy extends DefaultPolicyModel
{
	public AbstractClockChangePolicy() {
		super();
	}
	public AbstractClockChangePolicy(String id) {
		super(id);
	}

	protected void addClock(String c) {
		clockIds.add(c);
	}

	protected List<String> clockIds = new LinkedList<String>();
	protected Object changeLock = new Object();
}
