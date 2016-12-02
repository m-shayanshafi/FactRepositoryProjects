package via.aventurica.model.route;

import java.awt.Color;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.geom.GeneralPath;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import via.aventurica.model.route.RouteUpdateEvent.EventType;
import via.aventurica.model.worksheet.Worksheet;
import via.aventurica.model.zoom.IZoomListener;

/**
 * Eine Route ist ein Set von Punkten auf der Karte. 
 * Sie hat einen anfangs und einen Endpunkt und eine Länge. 
 */
public class Route implements IZoomListener {

	private final static long serialVersionUID = 1L;
	
	/**
	 * Das Worksheet, zu dem diese Route gehört. 
	 */
	private Worksheet ws; 
	/**
	 * Die Pixel-Koordinaten der Wegpunkte, zoomunabhängig für Zoom-Faktor 1. 
	 */
	private ArrayList<RoutePoint> waypoints; 
	/**
	 * Die Farbe die diese Route haben soll.
	 */
	private Color routeColor = Color.RED;
	
	private double zoomLevel = 1; 
	
	/**
	 * Umrechnungsfaktor zwischen Pixel und echter Distanz
	 */
	public final double referenceRatio; 
	/**
	 * Vorberechneter Pfad (für Java2D) der immer aktualisiert wird, wenn sich Punkte ändern oder gezoomt wird. 
	 * Der Pfad muss nur so, wie er ist, gezeichnet werden. 
	 */
	public final GeneralPath waypointPath = new GeneralPath(); 
	/**
	 * Die BoundingBox des {@link #waypointPath}, auch diese wird immer aktualisiert
	 */
	public final Rectangle boundingBox = new Rectangle(); 
	
	private String routeName = "Reise"; 
	
	private RoutePoint currentEndpointZoomed; 
	
	public Route(Worksheet ws) {
		this.ws = ws;
		ws.zoomManager.addZoomListener(this); 
		waypoints = new ArrayList<RoutePoint>(50);
		referenceRatio = ws.map.getRealSampleDistance() / ws.map.getPixelSampleDistance();
		
		createEventForCurrentRoute(EventType.NEW_ROUTE); 
	}
	
	public Route(Worksheet ws, Color routeColor) { 
		this(ws); 
		setRouteColor(routeColor); 
	}
	
	public Route(Worksheet ws, Color routeColor, String routeName) { 
		this(ws, routeColor); 
		setRouteName(routeName); 
	}
	
	public void setRouteName(String routeName) {
		this.routeName = routeName;
		createEventForCurrentRoute(EventType.NAME_CHANGED); 
	}
	
	public String getRouteName() {
		return routeName;
	}
	
	public Route(Worksheet ws, ArrayList<RoutePoint> waypoints) { 
		this(ws); 
		this.waypoints.addAll(waypoints);
		createEventForCurrentRoute(EventType.POINT_ADDED); 
		
	}
	
	public Route(Worksheet ws, ArrayList<RoutePoint> waypoints, Color routeColor) {
		this(ws, waypoints); 
		setRouteColor(routeColor);  
	}
	
	public void setRouteColor(Color routeColor) {
		this.routeColor = routeColor;
		createEventForCurrentRoute(EventType.ROUTE_COLOR_CHANGED); 
	}
	
	public Color getRouteColor() {
		return routeColor;
	}
	
	/**
	 * @return die Anzahl der Wegpunkte der Karte
	 */
	public int getWaypointCount() { 
		return waypoints.size(); 
	}
		
	/**
	 * Gibt die aktuelle Länge dieser Route in der einheit der Karte zurück.
	 * @return die länge dieser Route in der Einheit der Karte. 
	 */
	public double getDistance() {
		double totalLength=0; 
		Point lastPoint=null; 
		for(Point currentPoint : waypoints)
		{
			if(lastPoint!=null)
				totalLength+=Point.distance(lastPoint.x, lastPoint.y, currentPoint.x, currentPoint.y); 
			lastPoint=currentPoint; 
		}
		return totalLength * referenceRatio; 
	}
	
	public void addPoint(RoutePoint p) { 
		if(waypoints.size()==0) { 
			waypoints.add(p);
			dismissPath(true);
		} else {
			waypoints.add(p);
			waypointPath.lineTo(scaleValue(p.x), scaleValue(p.y)); 
			dismissBoundingBox(); 
		}
		
		dismissZoomedEndpoint(); 
		createEventForCurrentRoute(EventType.POINT_ADDED); 
		
	}
	
	public void removeLastPoint() { 
		waypoints.remove(waypoints.size()-1); 
		dismissPath(false);
		createEventForCurrentRoute(EventType.POINT_REMOVED);
		dismissBoundingBox(); 
		dismissZoomedEndpoint(); 
		 
	}
	
	private void dismissZoomedEndpoint() {
		int wpCount = waypoints.size(); 
		if(wpCount==0)  {
			currentEndpointZoomed = null;
			return; 
		}
		Point p = waypoints.get(wpCount-1); 
		 
		currentEndpointZoomed = new RoutePoint((int)(p.x * zoomLevel), (int)(p.y * zoomLevel)); 
	}
	
	private void dismissPath(final boolean rebuildBoundingBox) { 
		currentEndpointZoomed = null; 
	
		if(waypoints.size() > 0) {
			waypointPath.reset();
			Point first = waypoints.get(0); 
			waypointPath.moveTo(scaleValue(first.x), scaleValue(first.y));
			for(Point p : waypoints) { 
				waypointPath.lineTo(scaleValue(p.x), scaleValue(p.y)); 
			}
			  
			if(rebuildBoundingBox)
				dismissBoundingBox(); 
		} 
	}
	
	private void dismissBoundingBox() { 
		boundingBox.setBounds(waypointPath.getBounds()); 
		boundingBox.grow(10, 10); 
	}
	
	private final int scaleValue(int value) { 
		return (int)(value * zoomLevel); 
	}
	
	private void createEventForCurrentRoute(RouteUpdateEvent.EventType type) { 
		if(ws != null && ws.routeManager != null)
			ws.routeManager.fireRouteChange(new RouteUpdateEvent(type, this)); 
	}

	public void zoomedTo(double oldZoomLevel, double newZoomLevel) {
		zoomLevel = newZoomLevel; 
		dismissPath(true); 
		dismissZoomedEndpoint(); 
	}
	
	public List<RoutePoint> getWaypoints() {
		return Collections.unmodifiableList(waypoints);
	}
	
	public RoutePoint getUnzoomedEndpoint() { 
		if(waypoints.size()>0)
			return waypoints.get(waypoints.size()-1);
		return null; 
	}
	
	public RoutePoint getZoomedEndpoint() { 
		return currentEndpointZoomed; 
	}

	public Point getUnzoomedStartPoint() {
		return waypoints.get(0); 
	}
	
	@Override
	public String toString() {
		return routeName; 
	}
}
