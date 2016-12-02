package flands;

import java.awt.Dialog;
import java.awt.Frame;
import java.awt.GridBagLayout;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JTextField;

/**
 * Display a simple About dialog (from the Help menu).
 * @author Jonathan Mann
 */
public class AboutDialog extends JDialog implements ActionListener {
	private String[] message =
	{
			"Fabled Lands App (FLApp)",
			"Version 1.0.4",
			"Book text \u00a9 Dave Morris & Jamie Thomson",
			"Illustrations \u00a9 Russ Nicholson",
			"Source code by Jonathan Mann"
	};

	private static String title = "About FLApp";
	public AboutDialog(Frame parent) {
		super(parent, title, true);
		init(parent);
	}
	public AboutDialog(Dialog parent) {
		super(parent, title, true);
		init(parent);
	}

	private void init(Window parent) {
		JComponent[] labels = new JComponent[message.length+1];
		for (int l = 0; l < message.length; l++) {
			labels[l] = new JLabel(message[l], JLabel.CENTER);
		}
		JTextField website = new JTextField("http://flapp.sourceforge.net/");
		website.setEditable(false);
		//website.setBorder(null);
		labels[message.length] = website;
		
		GridBagLayout gbl = new GridBagLayout();
		getContentPane().setLayout(gbl);
		GBC gbc = new GBC();
		
		gbc.setWeight(1, 0)
		    .setNoFill()
		    .setAnchor(GBC.CENTER)
			.setInsets(12, 12, 0, 11);
		for (int l = 0; l < labels.length; l++) {
			gbc.addComp(getContentPane(), labels[l], gbl, 0, l);
			if (l == 0) gbc.insets.top = 5;
		}
		
		JButton closeButton = new JButton("Close");
		closeButton.addActionListener(this);
		gbc.setWeight(1, 1)
			.setNoFill()
			.setAnchor(GBC.SOUTH)
			.setInsets(11, 12, 11, 11)
			.addComp(getContentPane(), closeButton, gbl, 0, labels.length);
		
		pack();
		setLocationRelativeTo(parent);
	}

	public void actionPerformed(ActionEvent evt) {
		setVisible(false);
	}
}
