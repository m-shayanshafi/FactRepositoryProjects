package via.aventurica.view.appFrame.mapView;

import java.awt.Dimension;
import java.awt.Insets;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.MouseEvent;

import javax.swing.SwingUtilities;

import via.aventurica.ViaAventurica;
import via.aventurica.model.marker.Marker;
import via.aventurica.model.worksheet.Worksheet;
import via.aventurica.view.appFrame.markerDialog.MarkerDialog;

public class AddMarkerBehaviour extends BasicMouseBehaviour {
	private final static long serialVersionUID = 1L;
	
	@Override
	public void mouseClicked(MouseEvent e) {
		Worksheet ws = ViaAventurica.getCurrentWorksheet(); 
		super.mouseClicked(e); 
		if(SwingUtilities.isLeftMouseButton(e)) {  
			double currentZoom = ws.zoomManager.getCurrentZoom();
			 
			Marker newMarker = new MarkerDialog(null, createOpenPoint(e)).getUserData(); 
			if(newMarker!=null) {
				newMarker.setPosition((int)((double)e.getX()/currentZoom), (int)((double)e.getY()/currentZoom)); 
				ws.markerManager.addMarker(newMarker);
			}
		}
	}
	
	private Point createOpenPoint(MouseEvent e) { 
		Point p = e.getPoint(); 
		SwingUtilities.convertPointToScreen(p, getSource(e)); 
		final int dialogBottom = p.y + MarkerDialog.DIALOG_HEIGHT; 
		final int dialogRight  = p.x + MarkerDialog.DIALOG_WIDTH; 
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		Insets i = Toolkit.getDefaultToolkit().getScreenInsets(getSource(e).getGraphicsConfiguration()); 
		if(screenSize.height - i.bottom <= dialogBottom)
			p.y = screenSize.height - MarkerDialog.DIALOG_HEIGHT - i.bottom; 
		if(screenSize.width - i.right <= dialogRight)
			p.x = screenSize.width - MarkerDialog.DIALOG_WIDTH - i.right; 
		
		return p; 
	}
	
}
