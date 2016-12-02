package net.sf.bloodball.view;

import de.vestrial.util.swing.JFrameHelper;
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.io.IOException;
import javax.swing.*;
import javax.swing.JDialog;
import javax.swing.JEditorPane;
import javax.swing.JScrollPane;
import net.sf.bloodball.BloodBall;
import net.sf.bloodball.resources.*;
import net.sf.bloodball.resources.ResourceHandler;

class AboutDialog extends JDialog {

	private static AboutDialog instance = new AboutDialog();

	private AboutDialog() {
		setTitle("Blood Ball v" + BloodBall.VERSION);
		getContentPane().setLayout(new BorderLayout());
		getContentPane().add(new JLabel("Homepage: http://sf.net/projects/bloodball/"), BorderLayout.NORTH);
		getContentPane().add(getLicensePane(), BorderLayout.CENTER);
		getContentPane().add(getOkButton(), BorderLayout.SOUTH);
		setSize(400, 300);
		JFrameHelper.centerOnScreen(this);
	}

	private JButton getOkButton() {
		return new JButton(new LocalizedAction(ResourceKeys.OK_BUTTON) {
			public void actionPerformed(ActionEvent e) {
				hide();
			}
		});
	}

	private JScrollPane getLicensePane() {
		JEditorPane licensePane;
		try {
			licensePane = new JEditorPane(ClassLoader.getSystemResource("help/license.htm"));
		} catch (IOException exception) {
			licensePane = new JEditorPane("text/plain", "license file not found");
		}
		licensePane.setEditable(false);
		return new JScrollPane(licensePane);
	}

	public static void showDialog() {
		instance.show();
	}
}
