package via.aventurica.model.marker;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.swing.JComponent;

import via.aventurica.ViaAventurica;
import via.aventurica.model.worksheet.Worksheet;

public class MarkerManager {
	private ArrayList<Marker> markers = new ArrayList<Marker>(); 

	private final Worksheet ws; 
	
	
	public MarkerManager(Worksheet ws) {
		this.ws = ws; 
	}
	

	
	public List<Marker> listMarkers() {
		return Collections.unmodifiableList(markers); 
	}
	
	
	private JComponent getLayoutComponent() { 
		return ViaAventurica.APPLICATION.getMapComponent(); 
	}
	
	public void addMarker(Marker m) {
		markers.add(m); 
		 
		ws.zoomManager.addZoomListener(m); 
		m.zoomedTo(1, ws.zoomManager.getCurrentZoom()); 
		JComponent layoutComponent = getLayoutComponent(); 
		layoutComponent.add(m);
		layoutComponent.repaint(m.getBounds());
	}
	
	public void removeMarker(Marker m) { 
		markers.remove(m); 
		ws.zoomManager.removeZoomListener(m); 
		JComponent layoutComponent = getLayoutComponent(); 
		layoutComponent.remove(m); 
		layoutComponent.revalidate(); 
		layoutComponent.repaint(m.getBounds()); 
	}
	
	public void clear(){ 
		while(markers.size() > 0)
			removeMarker(markers.get(0));
	}
}
