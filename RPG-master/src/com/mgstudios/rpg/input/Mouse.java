package com.mgstudios.rpg.input;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

public class Mouse implements MouseListener, MouseMotionListener {
	public static int xPos, yPos, button = -1;
	
	@Override
	public void mouseDragged(MouseEvent e) {
		xPos = e.getX();
		yPos = e.getY();
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		xPos = e.getX();
		yPos = e.getY();
	}

	@Override
	public void mouseClicked(MouseEvent e) {}

	@Override
	public void mouseEntered(MouseEvent e) {}

	@Override
	public void mouseExited(MouseEvent e) {}

	@Override
	public void mousePressed(MouseEvent e) {
		button = e.getButton();
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		button = -1;
	}
}
