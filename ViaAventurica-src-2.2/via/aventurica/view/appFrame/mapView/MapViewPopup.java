package via.aventurica.view.appFrame.mapView;

import java.awt.Component;
import java.awt.Point;

import javax.swing.JPopupMenu;

import via.aventurica.view.actions.UndoRouteAction;
import via.aventurica.view.actions.context.CreateDistanceMarkerAction;

public class MapViewPopup extends JPopupMenu {
	private final static long serialVersionUID = 1L;
	
	private static Point mapPoint; 
	
	@Override
	public void show(Component invoker, int x, int y) {
		super.show(invoker, x, y);
		mapPoint = new Point(x, y); 
	}
	
	public Point getMapPoint() {
		return mapPoint;
	}

	
	public MapViewPopup() { 
		super();  
		add(new CreateDistanceMarkerAction()); 
		addSeparator(); 
		add(new UndoRouteAction()); 
	}
	
	public static Point getLastMapPoint() { 
		return mapPoint; 
	}
}
