package via.aventurica.view.actions.context;

import java.awt.Point;
import java.awt.event.ActionEvent;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

import via.aventurica.ViaAventurica;
import via.aventurica.model.marker.Marker;
import via.aventurica.model.route.RouteControl;
import via.aventurica.model.route.RoutePoint;

public class CreateDistanceMarkerAction extends ExtendedPopupAction{
	private final static long serialVersionUID = 1L;

	private final static DecimalFormat format = new DecimalFormat("#,##0.00", new DecimalFormatSymbols(Locale.GERMAN));
	
	public CreateDistanceMarkerAction() { 
		super("Entfernungsmarker für letzten Punkt"); 
	}
	
	@Override
	public void actionPerformed(ActionEvent e, Point mapPoint) {
		
		RouteControl r = ViaAventurica.getCurrentWorksheet().routeManager; 
		//RouteManager rm = RouteManager.getCurrentRoute(); 
		RoutePoint endPoint = r.getCurrentRoute().getUnzoomedEndpoint();
		if(endPoint!=null) { 
			endPoint.x+=5; 
			Marker marker = new Marker(endPoint.x, endPoint.y, format.format(r.getCurrentRoute().getDistance())+" "+ViaAventurica.getCurrentWorksheet().map.getDistanceUnit());
			ViaAventurica.getCurrentWorksheet().markerManager.addMarker(marker);
		}
		/*double zoomLevel = ZoomManager.getInstance().getCurrentZoom(); 
		RoutePoint p = new RoutePoint(
				(int)Math.round((double)mapPoint.x / zoomLevel), 
				(int)Math.round((double)mapPoint.y / zoomLevel));
		RouteManager.getCurrentRoute().addWaypoint(p); */
	}
}
