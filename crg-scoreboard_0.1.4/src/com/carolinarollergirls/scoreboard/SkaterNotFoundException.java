package com.carolinarollergirls.scoreboard;

public class SkaterNotFoundException extends Exception
{
	public SkaterNotFoundException(String n) {
		super("Skater not found for name : " + n);
		name = n;
	}

	public String getName() { return name; }

	protected String name = "";
}
