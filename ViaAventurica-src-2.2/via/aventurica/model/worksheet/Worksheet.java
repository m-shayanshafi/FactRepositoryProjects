package via.aventurica.model.worksheet;

import via.aventurica.model.map.Map;
import via.aventurica.model.marker.MarkerManager;
import via.aventurica.model.route.RouteControl;
import via.aventurica.model.zoom.ZoomManager;

/**
 * Ein Worksheet ist die Root-Entity für die, am Bildschirm befindliche Karte. 
 * In dem Worksheet
 */
public class Worksheet {
	
	public final Map map;
	public final RouteControl routeManager; 
	public final MarkerManager markerManager; 
	public final ZoomManager zoomManager; 
	
	private final static long serialVersionUID = 1L;

	public Worksheet(final Map map) { 
		//this(map, new MarkerManager(ViaAventurica.APPLICATION.get))
		this.map = map;
		zoomManager = new ZoomManager(this);
		markerManager = new MarkerManager(this);
		routeManager = new RouteControl(this); 
		
	}
	
	
	
	
	public void unload() { 
		markerManager.clear(); 
		zoomManager.clear(); 
		routeManager.clear(); 
	}
	
	
	@Override
	public String toString() {
		
		return "Worksheet: "+map.getFile().getAbsolutePath(); 
	}
	
	
}
