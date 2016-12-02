package via.aventurica.model.marker;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;

import javax.swing.JLabel;
import javax.swing.SwingUtilities;

import via.aventurica.model.zoom.IZoomListener;
import via.aventurica.view.appFrame.markerDialog.MarkerDialog;
import via.aventurica.view.utils.MouseAdapterBackwardsCompatible;

public class Marker extends JLabel implements IZoomListener {

	private static final long serialVersionUID = 1L;
	private Color markerColor; 
	private boolean isSemiTransparent; 
	private MarkerIcon markerIcon; 
	
	private double currentZoomLevel = 1; 
	
	private int sourceX, sourceY;
	private Rectangle bounds = new Rectangle(); 
	
	public int getSourceX() {
		return sourceX;
	}
	
	public int getSourceY() {
		return sourceY;
	}
	
	private final static MouseAdapterBackwardsCompatible ml = new MouseAdapterBackwardsCompatible() {
		public void mouseClicked(MouseEvent e) {
			int clickCount = e.getClickCount(); 
			Marker concernedMarker = (Marker)e.getSource(); 
			if(clickCount==2) { 
				/*String newName = JOptionPane.showInputDialog("Welche Beschriftung soll der Marker tragen?", concernedMarker.getText() );
				if(newName != null)
					concernedMarker.setText(newName); */
				if(new MarkerDialog(concernedMarker, null).getUserData()!=null) 
					concernedMarker.updateBounds(); 
				
				e.consume(); 
			}
		 }
		private Point lastPoint; 
		@Override
		public void mousePressed(MouseEvent e) {
			
			lastPoint = e.getPoint(); 
			SwingUtilities.convertPointToScreen(lastPoint, (Marker)e.getSource());
		}
		
		@Override
		public void mouseDragged(MouseEvent e) {
			Point newPoint = e.getPoint(); 
			SwingUtilities.convertPointToScreen(newPoint, (Marker)e.getSource());
			int dX = newPoint.x - lastPoint.x; 
			int dY = newPoint.y - lastPoint.y; 
			((Marker)(e.getSource())).move(dX, dY); 
			lastPoint = newPoint; 
		}
	}; 

	public Marker(int sourceX, int sourceY, String text) {
		super(text);
		this.sourceX = sourceX;
		this.sourceY = sourceY;
		updateBounds(); 
		addMouseListener(ml);
		addMouseMotionListener(ml);
		
		setMarkerColor(Color.WHITE, true); 
	} 

	private void updateBounds() { 
		bounds.setBounds((int)((double)sourceX*currentZoomLevel), (int)((double)sourceY*currentZoomLevel), getPreferredSize().width, getPreferredSize().height);
		setBounds(bounds); 
	}
	
	@Override
	public Rectangle getBounds() {
		return bounds; 
	}
	
	@Override
	public void move(int dx, int dy) {
		sourceX+=dx;
		sourceY+=dy; 
		
		updateBounds(); 		
	}
	
	public void setPosition(int x, int y) { 
		this.sourceX = x; 
		this.sourceY = y; 
		
		updateBounds();
	}
	
	@Override
	public Rectangle getBounds(Rectangle rv) {
		rv.setBounds(bounds); 
		return rv; 
	}

	public void zoomedTo(double oldZoomLevel, double newZoomLevel) {
		this.currentZoomLevel = newZoomLevel; 
		
		updateBounds(); 
		/*bounds.x = (int)((double)sourceX * newZoomLevel); 
		bounds.y = (int)((double)sourceY * newZoomLevel); 
		setBounds(bounds); */
	}
	
	@Override
	public String toString() {
		return "["+sourceX+"/"+sourceY+"]: "+getText();
	}
	
	//private final static Color backgroundColor = new Color(255, 255, 255, 128); 
	
	@Override
	protected void paintComponent(Graphics g) {
		g.setColor(getBackground()); 
		g.fillRect(0, 0, getWidth(), getHeight()); 
		super.paintComponent(g);
	}

	public void setMarkerIcon(MarkerIcon selectedIcon) {
		this.markerIcon = selectedIcon;
		setIcon(markerIcon != null ? markerIcon.getIcon() : null); 
		updateBounds();
	}
	
	
	public MarkerIcon getMarkerIcon() {
		return markerIcon;
	}
	
	
	public void setMarkerColor(Color markerColor, boolean semiTransparent) {
		this.markerColor = markerColor;
		this.isSemiTransparent = semiTransparent; 
		 
		if(markerColor!=null) { 
			setBackground(new Color(markerColor.getRed(), markerColor.getGreen(), markerColor.getBlue(), semiTransparent ? 128 : 255));
		} else { 
			this.markerColor = Color.WHITE; 
			setBackground(markerColor); 
		}
		setOpaque(!isSemiTransparent);
	}
	
	
	public Color getMarkerColor() {
		return markerColor;
	}
	
	public boolean isSemiTransparent() {
		return isSemiTransparent;
	}
	

	
	
	
}
