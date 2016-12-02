package via.aventurica.view.appFrame.mapView;

import java.awt.event.MouseEvent;

import javax.swing.SwingUtilities;

import via.aventurica.ViaAventurica;
import via.aventurica.model.route.RoutePoint;
import via.aventurica.model.worksheet.Worksheet;

public class AddWaypointBehaviour extends BasicMouseBehaviour {
	private final static long serialVersionUID = 1L;
	
	private boolean inAdjustingMode = false;  
	
	public void mousePressed(MouseEvent e) { 
		super.mousePressed(e); 
		if(isValidClick(e)) {
			inAdjustingMode = true;
			getSource(e).previewLineTo(e.getPoint()); 
		} else if(isCancelClick(e)) {
			inAdjustingMode = false; 
			getSource(e).dismissPreview(); 
		}
	}
	
	
	
	public void mouseDragged(MouseEvent e) {
		super.mouseDragged(e); 
		if(inAdjustingMode) {
			getSource(e).previewLineTo(e.getPoint()); 
		} 
	}
	
	public void mouseReleased(MouseEvent e) {
		Worksheet ws = ViaAventurica.getCurrentWorksheet();
		super.mouseReleased(e); 
		if(inAdjustingMode) {
			double zoomLevel = ws.zoomManager.getCurrentZoom(); 
			getSource(e).dismissPreview(); 
			ws.routeManager.getCurrentRoute().addPoint(
					new RoutePoint(
							(int)Math.round((double)e.getPoint().x / zoomLevel), 
							(int)Math.round((double)e.getPoint().y / zoomLevel)));
			inAdjustingMode = false; 
		} 
	}

	private final boolean isValidClick(MouseEvent e) { 
		return ViaAventurica.getCurrentWorksheet()!=null && SwingUtilities.isLeftMouseButton(e);  
	}
	
	private final boolean isCancelClick(MouseEvent e) { 
		return inAdjustingMode && SwingUtilities.isRightMouseButton(e);
	}

	
}
