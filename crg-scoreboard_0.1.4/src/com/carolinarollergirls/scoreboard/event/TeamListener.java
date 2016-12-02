package com.carolinarollergirls.scoreboard.event;

import java.util.*;

public interface TeamListener extends EventListener
{
	public void teamChange(TeamEvent event);
}

