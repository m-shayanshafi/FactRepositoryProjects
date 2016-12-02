package via.aventurica.view.mapcreator;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.MouseEvent;

import via.aventurica.view.utils.JImagePanel;
import via.aventurica.view.utils.MouseAdapterBackwardsCompatible;

public class MeasurementPanel extends JImagePanel {
	private final static long serialVersionUID = 1L;
	
	private Point startPoint = null; 
	private Point currentEndPoint = null;
	private Point p1; 
	private final static Color backgroundColor = new Color(1f, 1f, 1f, 0.7f);
	
	private int length; 
	private boolean isHorizontalRule = false; 
	
	
	
	private MouseAdapterBackwardsCompatible adp = new MouseAdapterBackwardsCompatible() { 
		
		 
		
		public void mousePressed(MouseEvent e) {
			startPoint = e.getPoint(); 
		};
		public void mouseReleased(MouseEvent e) {
			startPoint = currentEndPoint = null; 
			repaint(); 
			
		};
		public void mouseDragged(MouseEvent e) {
			currentEndPoint = e.getPoint();
			int dX = currentEndPoint.x - startPoint.x; 
			int dY = currentEndPoint.y - startPoint.y;
			
			
			isHorizontalRule = Math.abs(dX) >= Math.abs(dY);
			p1 = isHorizontalRule ? (dX > 0 ? startPoint : currentEndPoint) : (dY > 0 ? startPoint : currentEndPoint);   
			length = isHorizontalRule ? Math.abs(dX) : Math.abs(dY); 
			repaint(); 
		}
		
	}; 
	
	public MeasurementPanel() { 
		super(); 
		setAutoscrolls(true); 
		addMouseListener(adp); 
		addMouseMotionListener(adp); 
	}
	
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		if(startPoint != null && currentEndPoint != null) {
			g.setColor(backgroundColor); 
			if(isHorizontalRule) {
				g.setClip(p1.x, p1.y, length, 10); 
				g.fillRect(p1.x, p1.y, length, 10); 
				g.setColor(Color.BLACK);
				g.drawRect(p1.x, p1.y-1, length-1, 10); 
				if(length > 20) 
					g.drawString(length+" Pixel", p1.x+2, p1.y+9); 
			} else { 
				g.setClip(p1.x, p1.y, 10, length); 
				g.fillRect(p1.x, p1.y, 10, length); 
				g.setColor(Color.BLACK);
				g.drawRect(p1.x-1, p1.y, 10, length); 
				if(length > 20) {
					g.translate(p1.x, p1.y); 
					((Graphics2D)g).rotate(Math.PI/2d);
					g.drawString(length+" Pixel", 2, -1); 
				}
			}
		}
	}
	
	
	
	public int getSelectedLength() {
		return length;
	}

	
}
