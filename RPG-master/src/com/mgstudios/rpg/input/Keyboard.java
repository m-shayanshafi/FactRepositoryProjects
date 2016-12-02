package com.mgstudios.rpg.input;

import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class Keyboard implements KeyListener, FocusListener {
	public boolean[] key = new boolean[68836];
	public boolean up, down, left, right;
	
	public void update() {
		up = key[KeyEvent.VK_UP] || key[KeyEvent.VK_W];
		down = key[KeyEvent.VK_DOWN] || key[KeyEvent.VK_S];
		left = key[KeyEvent.VK_LEFT] || key[KeyEvent.VK_A];
		right = key[KeyEvent.VK_RIGHT] || key[KeyEvent.VK_D];
	}
	
	@Override
	public void focusGained(FocusEvent e) {}

	@Override
	public void focusLost(FocusEvent e) {
		for (int i = 0; i < key.length; i++) {
			key[i] = false;
		}
	}

	@Override
	public void keyPressed(KeyEvent e) {
		int keyCode = e.getKeyCode();
		
		if (keyCode > 0 && keyCode < key.length) {
			key[keyCode] = true;
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
		int keyCode = e.getKeyCode();
		
		if (keyCode > 0 && keyCode < key.length) {
			key[keyCode] = false;
		}
	}

	@Override
	public void keyTyped(KeyEvent e) {}
}
