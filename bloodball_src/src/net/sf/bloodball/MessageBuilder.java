package net.sf.bloodball;

import java.text.MessageFormat;
import net.sf.bloodball.model.*;
import net.sf.bloodball.resources.ResourceHandler;

public abstract class MessageBuilder {
	
	public abstract String buildMessage(Game game);

	public static String buildMessage(String resourceKey, Object[] params) {
		MessageFormat message = new MessageFormat(getResourceString(resourceKey));
		return message.format(params);
	}

	public static String buildMessage(String resourceKey, int parameter) {
		return buildMessage(resourceKey, new Integer(parameter));
	}

	public static String buildMessage(String resourceKey, Object parameter) {
		return buildMessage(resourceKey, new Object[] { parameter });
	}

	public static String getResourceString(String name) {
		return ResourceHandler.getString(name);
	}
}