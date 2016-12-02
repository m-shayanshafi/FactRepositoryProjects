package net.sf.bloodball.resources;

import javax.swing.*;

public abstract class LocalizedAction extends AbstractAction {

	public LocalizedAction() {
		super();
	}

	public LocalizedAction(String resourceName) {
		super(ResourceHandler.getString(resourceName));
	}

	public LocalizedAction(String resourceName, Icon icon) {
		super(ResourceHandler.getString(resourceName), icon);
	}

}
