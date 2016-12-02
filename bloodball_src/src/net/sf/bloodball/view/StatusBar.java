package net.sf.bloodball.view;

import java.awt.*;
import javax.swing.JLabel;
import net.sf.bloodball.resources.*;

public class StatusBar {
	private JLabel statusLabel = new JLabel(ResourceHandler.getString(ResourceKeys.WELCOME_MESSAGE));

	StatusBar() {
	}

	public Component getComponent() {
		return statusLabel;
	}

	public void setText(String text) {
		statusLabel.setText(text);
	}

	public void setTextColor(Color color) {
		statusLabel.setForeground(color);
	}
}