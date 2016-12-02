package com.carolinarollergirls.scoreboard.policy;

import java.util.*;

import com.carolinarollergirls.scoreboard.*;
import com.carolinarollergirls.scoreboard.model.*;
import com.carolinarollergirls.scoreboard.defaults.*;

public abstract class AbstractSkaterChangePolicy extends DefaultPolicyModel
{
	public AbstractSkaterChangePolicy() {
		super();
	}
	public AbstractSkaterChangePolicy(String id) {
		super(id);
	}

	protected Object changeLock = new Object();
}
