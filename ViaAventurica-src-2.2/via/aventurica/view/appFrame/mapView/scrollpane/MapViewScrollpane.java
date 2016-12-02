package via.aventurica.view.appFrame.mapView.scrollpane;

import java.awt.Dimension;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

import javax.swing.JScrollPane;

import via.aventurica.ViaAventurica;
import via.aventurica.model.route.Route;
import via.aventurica.model.worksheet.IWorksheetListener;
import via.aventurica.model.worksheet.Worksheet;
import via.aventurica.model.zoom.IZoomListener;
import via.aventurica.view.appFrame.mapView.AddMarkerBehaviour;
import via.aventurica.view.appFrame.mapView.AddWaypointBehaviour;
import via.aventurica.view.appFrame.mapView.BasicMouseBehaviour;
import via.aventurica.view.appFrame.mapView.MapViewRenderer;
import via.aventurica.view.appFrame.mapView.MouseClickBehaviour;
import via.aventurica.view.appFrame.mapView.scrollpane.MapViewDecoration.Direction;

public class MapViewScrollpane extends JScrollPane implements IZoomListener, IWorksheetListener{
	private final static long serialVersionUID = 1L;
	
	private final MapViewRenderer renderer = new MapViewRenderer();
	private final MapViewDecoration horizontalRuler = new MapViewDecoration(Direction.RULER_HORIZONTAL), verticalRuler = new MapViewDecoration(Direction.RULER_VERTICAL); 
	
	private MouseClickBehaviour currentBehaviour; 
	
	private BasicMouseBehaviour addWaypointBehaviour = new AddWaypointBehaviour();  
	private BasicMouseBehaviour addMarkerBehaviour  = new AddMarkerBehaviour(); 
	
 
	private int imageWidth, imageHeight;
	
	private double currentZoomLevel = 1; 
	
	public MapViewScrollpane() {
		super();
		ViaAventurica.addWorksheetListener(this); 
		setOpaque(true); 
		
		setViewportView(renderer);
		setMouseClickBehaviour(MouseClickBehaviour.ADD_WAYPOINT); 
		setRowHeaderView(verticalRuler);
		setColumnHeaderView(horizontalRuler); 
		setCorner(UPPER_LEFT_CORNER, new MapViewDecoration(Direction.CORNER_LEFT_UPPER)); 
		setCorner(UPPER_RIGHT_CORNER, new MapViewDecoration(Direction.CORNER_RIGHT_UPPER));
		setCorner(LOWER_LEFT_CORNER, new MapViewDecoration(Direction.CORNER_LEFT_LOWER));
		horizontalScrollBar.setUnitIncrement(20); 
		verticalScrollBar.setUnitIncrement(20); 
		 
		//ZoomManager.getInstance().setMasterZoomListener(this); 
	}
	
	
	public void setMouseClickBehaviour(MouseClickBehaviour newBehaviour) { 
		if(currentBehaviour != newBehaviour) { 
			currentBehaviour = newBehaviour; 
			if(currentBehaviour == MouseClickBehaviour.ADD_WAYPOINT) {
				renderer.removeMouseListener(addMarkerBehaviour); 
				renderer.removeMouseMotionListener(addMarkerBehaviour); 
				renderer.addMouseListener(addWaypointBehaviour); 
				renderer.addMouseMotionListener(addWaypointBehaviour);
			} else if(currentBehaviour == MouseClickBehaviour.ADD_MARKER) { 
				renderer.removeMouseListener(addWaypointBehaviour); 
				renderer.removeMouseMotionListener(addWaypointBehaviour); 
				renderer.addMouseListener(addMarkerBehaviour); 
				renderer.addMouseMotionListener(addMarkerBehaviour);
			} else { 
				System.err.println("Unsupported MouseClickBehaviour");
			}
		}
	}
	
	
		
	
	public BufferedImage createScreenshot() { 
		return renderer.createScreenshot(); 
	}
	
	public void lookAt(int x, int y) {

		Rectangle visibleRect = viewport.getVisibleRect();
		
			 
		x-=visibleRect.width/2; 
		y-=visibleRect.height/2; 

		viewport.setViewPosition(new Point(x, y));     
	}



	public void zoomedTo(double oldZoomLevel, double newZoomLevel) {
		Rectangle viewRect = viewport.getViewRect(); 
		int centerX = (int)(( viewRect.x + viewRect.width / 2 ) * newZoomLevel / currentZoomLevel);
		int centerY = (int)(( viewRect.y + viewRect.height / 2 ) * newZoomLevel / currentZoomLevel);

		currentZoomLevel = newZoomLevel; 
	
		updateUI();
		lookAt(centerX, centerY);
	}
	
	
	public MapViewRenderer getRenderer() {
		return renderer;
	}
	
	
	public void worksheetChanged(Worksheet newWorksheet) {
		imageWidth = 0; 
		imageHeight = 0; 
		
		int lookAtX = 0, lookAtY = 0; 

	 	
		if(newWorksheet!=null) {
			imageWidth = newWorksheet.map.getWidth(); 
			imageHeight = newWorksheet.map.getHeight();
			newWorksheet.zoomManager.setMasterZoomListener(this); 
			lookAtX = imageWidth/2; 
			lookAtY = imageHeight/2; 
			if(newWorksheet.routeManager.hasVisibleRoutes()) { 
				Route r = newWorksheet.routeManager.getCurrentRoute();
				lookAtX = r.boundingBox.x + r.boundingBox.width/2; 
				lookAtY = r.boundingBox.y + r.boundingBox.height/2; 
			}
		}
		
		getViewport().setViewSize(new Dimension(imageWidth, imageHeight)); 
		updateUI(); 
		
		lookAt(lookAtX, lookAtY);
	}
	
	
}
