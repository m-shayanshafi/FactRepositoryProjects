package via.aventurica.view.appFrame.mapView;

import java.awt.Cursor;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import javax.swing.JScrollBar;
import javax.swing.SwingUtilities;

import via.aventurica.ViaAventurica;

public class BasicMouseBehaviour implements MouseListener, MouseMotionListener {
	private final static long serialVersionUID = 1L;

	private int lastX, lastY; 
	
	private JScrollBar horizontalScrollBar, verticalScrollBar; 
	
	private boolean mouseWasDragged = false; 
	private boolean pressWasPopupTrigger = false; 
	
	public void mousePressed(MouseEvent e) {
		mouseWasDragged = false; 
		pressWasPopupTrigger = e.isPopupTrigger(); 
		if(isDraggingEvent(e)) {  
			 
			if(horizontalScrollBar==null) { 
				horizontalScrollBar = ViaAventurica.APPLICATION.getMapView().getHorizontalScrollBar(); 
				verticalScrollBar = ViaAventurica.APPLICATION.getMapView().getVerticalScrollBar(); }
			
			Point click = e.getPoint(); 
			SwingUtilities.convertPointToScreen(click, getSource(e)); 
			lastX = click.x; 
			lastY = click.y; 
			
			
		}
	}
	
	public void mouseDragged(MouseEvent e) {
		
		if(isDraggingEvent(e)) {
			if(!mouseWasDragged) { 
				mouseWasDragged = true;
				ViaAventurica.APPLICATION.setCursor(new Cursor(Cursor.MOVE_CURSOR));
			}
			Point click = e.getPoint(); 
			SwingUtilities.convertPointToScreen(click, getSource(e)); 
			
			final int newX = click.x; 
			final int newY = click.y; 
			
			final int dX = lastX - newX; 
			final int dY = lastY - newY;
					
			if (dX != 0 && horizontalScrollBar.getValue() + dX > horizontalScrollBar.getMinimum() &&
					horizontalScrollBar.getValue() + horizontalScrollBar.getVisibleAmount() + dX < horizontalScrollBar.getMaximum()) {
					horizontalScrollBar.setValue(horizontalScrollBar.getValue() + dX);
				}
				
				if (dY != 0 && verticalScrollBar.getValue() + dY > verticalScrollBar.getMinimum() &&
					verticalScrollBar.getValue() + verticalScrollBar.getVisibleAmount() + dY < verticalScrollBar.getMaximum()) {
					verticalScrollBar.setValue(verticalScrollBar.getValue() + dY);
				}
			
			
			lastX = newX; 
			lastY = newY; 
		}
	}

	public void mouseReleased(MouseEvent e) {
		if(!mouseWasDragged && ( pressWasPopupTrigger || e.isPopupTrigger() ) ) {
			getSource(e).showPopup(e.getPoint()); 
		}
		if(isDraggingEvent(e)) { 
			ViaAventurica.APPLICATION.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
		}
	}
	
	public void mouseClicked(MouseEvent e) {
	}

	public void mouseEntered(MouseEvent e) {
	}

	public void mouseExited(MouseEvent e) {
	}

	public void mouseMoved(MouseEvent e) {
	}
	
	
	private final boolean isDraggingEvent(MouseEvent e) { 
		return SwingUtilities.isRightMouseButton(e); 
	}
	
	final MapViewRenderer getSource(final MouseEvent e) { 
		return (MapViewRenderer)e.getSource(); 
	}
}
