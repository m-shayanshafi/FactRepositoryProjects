package com.mgstudios.rpg.core;

import java.awt.EventQueue;

import com.mgstudios.rpg.launcher.LauncherRun;

public class Launch {
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				new LauncherRun();
			}
		});
	}
}
