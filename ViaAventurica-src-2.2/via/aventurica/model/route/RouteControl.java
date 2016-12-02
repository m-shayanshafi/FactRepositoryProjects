package via.aventurica.model.route;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.Locale;

import via.aventurica.model.route.RouteUpdateEvent.EventType;
import via.aventurica.model.worksheet.Worksheet;

public class RouteControl {
	private final static long serialVersionUID = 1L;
	
	private final Worksheet ws;
	private ArrayList<Route> routes = new ArrayList<Route>(3);
	private ArrayList<Route> routesRenderingSorted = new ArrayList<Route>(3); 
	
	private Route currentRoute; 
	
	private ArrayList<IRouteListener> routeListeners = new ArrayList<IRouteListener>(2);
	
	public final static DecimalFormat DISTANCE_FORMAT = new DecimalFormat("#,##0.00", new DecimalFormatSymbols(Locale.GERMAN));
	
	public RouteControl(Worksheet ws) { 
		this.ws = ws; 
		newRoute(); 
	}
	
	private void addRoute(Route r) { 
		routes.add(r);
		fireRouteChange(new RouteUpdateEvent(EventType.NEW_ROUTE, r));
		setCurrentRoute(r); 
	}
	
	public Route newRoute() { 
		addRoute(new Route(ws));
		return currentRoute; 
	}
	
	
	
	private void removeRoute(Route r) { 
		final boolean isCurrentRoute = r == currentRoute; 
		final int deletedIndex = routes.indexOf(r); 
		
		ws.zoomManager.removeZoomListener(r); 
		
		routes.remove(r); 
		routesRenderingSorted.remove(r);
		
		if(routes.size() == 0)
			newRoute();
		
		// muss danach geschehen, falls keine route existiert hat. 
		fireRouteChange(new RouteUpdateEvent(EventType.ROUTE_DELETED, r));
		
		if(isCurrentRoute) { 
			final int newIndex = routes.size() > deletedIndex ? deletedIndex : deletedIndex -1; 
			setCurrentRoute(routes.get(newIndex)); 
		}
		
	}
	
	
	public ArrayList<Route> getRoutes() {
		return routes;
	}
	

	public void clear() {
		routeListeners.clear(); 
	}
	
	public void addRouteListener(IRouteListener l) { 
		routeListeners.add(l); 
	}
	
	public void removeRouteListener(IRouteListener l) { 
		routeListeners.remove(l); 
	}
	
	
	public void setCurrentRoute(Route currentRoute) {
		this.currentRoute = currentRoute;
		routesRenderingSorted.remove(currentRoute); 
		routesRenderingSorted.add(currentRoute); 
		fireRouteChange(new RouteUpdateEvent(EventType.ROUTE_FOCUSED, currentRoute)); 
		
	}

	public Route getCurrentRoute() {
		return currentRoute; 
	}
	
	public ArrayList<Route> getRenderingQueue() { 
		return routesRenderingSorted; 
	}
	
	final void fireRouteChange(RouteUpdateEvent evt) { 
		for(IRouteListener l : routeListeners)
			l.routeChanged(evt); 
	}

	public int getRouteCount() {
		return routes.size();
	}

	public void removeCurrentRoute() {
		removeRoute(currentRoute);  
	}

	/**
	 * @return <code>true</code> wenn es mindestens eine Route mit mehr als einem Wegpunkt gibt. 
	 */
	public boolean hasVisibleRoutes() {
		for(Route r : routes)
			if(r.getWaypointCount() > 1)
				return true; 
		return false; 
	}

	
}
