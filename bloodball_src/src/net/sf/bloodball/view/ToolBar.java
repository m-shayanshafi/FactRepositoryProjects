package net.sf.bloodball.view;

import java.awt.Component;
import javax.swing.*;

public class ToolBar {
	private JToolBar toolBar = new JToolBar() {
		public boolean isFocusCycleRoot() {
			return true;
		}
	};

	ToolBar() {
	}

	public void addTool(JButton tool) {
		toolBar.add(tool);
	}

	public Component getComponent() {
		return toolBar;
	}
}