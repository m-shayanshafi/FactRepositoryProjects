package com.mgstudios.rpg.launcher;

import java.awt.Container;
import java.awt.Dimension;

import javax.swing.JFrame;

public class LauncherRun extends JFrame {
	private static final long serialVersionUID = 1L;
	
	public static final String TITLE = "RPG Game";
	
	private Launcher launcher;
	private JFrame frame;
	private Container contentPane;
	
	public LauncherRun() {
		launcher = new Launcher();
		frame = new JFrame();
		contentPane = getContentPane();
		
		setTitle(TITLE);
		
		frame.setUndecorated(true);
		frame.setSize(new Dimension(launcher.width, launcher.height));
		frame.setLocationRelativeTo(null);
		frame.setDefaultCloseOperation(EXIT_ON_CLOSE);
		frame.setResizable(false);
		frame.setVisible(true);
		
		contentPane.add(launcher);
		frame.add(contentPane);
		
		launcher.start(frame);
	}
}
