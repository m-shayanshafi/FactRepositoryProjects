package via.aventurica.io;

import java.awt.Color;
import java.io.File;
import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

import via.aventurica.ViaAventurica;
import via.aventurica.model.map.Map;
import via.aventurica.model.map.MapCollection;
import via.aventurica.model.map.MapInfo;
import via.aventurica.model.map.MapLoaderFactory;
import via.aventurica.model.marker.Marker;
import via.aventurica.model.route.Route;
import via.aventurica.model.route.RoutePoint;
import via.aventurica.model.worksheet.Worksheet;
import via.aventurica.view.utils.UserIcons;

public class WorksheetIO extends BasicXMLIO {
	private final static long serialVersionUID = 1L;
	
	private enum Tag { 
		worksheet, 
		map, 
		routeList,
		route, 
		point,
		markerList,
		marker
	}
	
	private enum Attribute {
		version, 
		mapsetfolder, 
		title, 
		color, 
		x, 
		y, 
		semitransparent, 
		icon
	}
	

	private Worksheet loadedWorksheet; 
	private int foundRoutes = 0; 
	
	public WorksheetIO() { 
		super(); 
	}
	
	public void loadWorksheet(File f) throws IOException, SAXException, ParserConfigurationException { 
		readFrom(f);
		ViaAventurica.setCurrentWorksheet(loadedWorksheet); 
		loadedWorksheet = null; 
	}
	
	@Override
	void foundElement(String name, Attributes atts) {
		super.foundElement(name, atts);
		Tag element = Enum.valueOf(Tag.class, name); 
		
		if(element==null) {  
			System.err.println("Unbekanntes Element gefunden: "+name);
			return; 
		}
		
		
		if(element == Tag.worksheet) { 
			System.out.println("Lade ViaAventurica Worksheet. Version: "+getAtt(atts, Attribute.version));
		}
		
		
		else if(element == Tag.map) { 
			String mapsetFolder = getAtt(atts, Attribute.mapsetfolder);
			String mapTitle = getAtt(atts, Attribute.title);
			
			Map worksheetMap = loadMap(mapsetFolder, mapTitle);
			if(worksheetMap!=null) 
				loadedWorksheet = new Worksheet(worksheetMap); 
			
		} else if(element == Tag.route) {
			foundRoutes++; 
			
			Color routeColor = new Color(Integer.parseInt(getAtt(atts, Attribute.color))); 
			String routeName = getAtt(atts, Attribute.title); 

			if(foundRoutes > 1)
				loadedWorksheet.routeManager.newRoute();  

			loadedWorksheet.routeManager.getCurrentRoute()
				.setRouteColor(routeColor);
			loadedWorksheet.routeManager.getCurrentRoute()
				.setRouteName(routeName); 
		
		
		} else if(element == Tag.point) { 
			loadedWorksheet.routeManager.getCurrentRoute().addPoint(new RoutePoint(
				Integer.parseInt(getAtt(atts, Attribute.x)), 
				Integer.parseInt(getAtt(atts, Attribute.y)))); 
		
		
		} else if(element == Tag.marker) { 
			Marker foundMarker= new Marker(
    				Integer.parseInt(getAtt(atts, Attribute.x)), 
    				Integer.parseInt(getAtt(atts, Attribute.y)), 
    				getAtt(atts, Attribute.title) 
    			); 
			String filename = getAtt(atts, Attribute.icon);
			Color color = new Color(Integer.parseInt(getAtt(atts, Attribute.color))); 
			boolean semiTransparent = Boolean.valueOf(getAtt(atts, Attribute.semitransparent));  
			
			if(filename!=null && filename.trim().length() > 0)
				foundMarker.setMarkerIcon(UserIcons.forName(filename));
			
			foundMarker.setMarkerColor(color, semiTransparent); 
			loadedWorksheet.markerManager.addMarker(foundMarker); 
		}
		
	}
	
	
	public void saveWorksheet(Worksheet w, File f) throws IOException { 
		try { 
			writeTo(f);
			openTag(Tag.worksheet, false, 
				Attribute.version, "2");
			
			String mapSetFolderName = prepareString(w.map.getFile().getParentFile().getName());
			String mapTitle = prepareString(w.map.getTitle()); 
			
			openTag(Tag.map, true,
					Attribute.mapsetfolder, mapSetFolderName,
					Attribute.title, mapTitle
				);
			
			openTag(Tag.routeList, false); 
			 
			for(Route r : w.routeManager.getRoutes()){ 
				openTag(Tag.route, false, 
					Attribute.color, r.getRouteColor().getRGB(), 
					Attribute.title, r.getRouteName()
					);
				for(RoutePoint p : r.getWaypoints()) 
					openTag(Tag.point, true, 
							Attribute.x, (int)p.getX(), 
							Attribute.y, (int)p.getY()
					); 
					
				
				closeTag(); // Route 
			}
			closeTag(); // RouteList
			
			
			openTag(Tag.markerList, false); 
			for(Marker m : w.markerManager.listMarkers()) {
				openTag(Tag.marker, true,
					Attribute.x, (int)m.getSourceX(), 
					Attribute.y, (int)m.getSourceY(), 
					Attribute.title, m.getText(),
					Attribute.color, m.getMarkerColor().getRGB(), 
					Attribute.semitransparent, m.isSemiTransparent(), 
					Attribute.icon, m.getMarkerIcon() == null ? "" : m.getMarkerIcon().getFilename()
						); 
				
			}
			closeTag(); // Markers
			
			closeTag(); //worksheet
			
			 
		} catch(IOException ex) { 
			throw ex; 
		} finally {
			writingDone(); 
		} 
	}
	
	private Map loadMap(String mapsetFolder, String mapname) { 
		try { 
			MapCollection coll = MapLoaderFactory.getMapsFromFolder(mapsetFolder);
				        
			MapInfo info = null; 
			if(coll != null) {
				for(MapInfo i : coll) {
					if(i.getTitle().equals(mapname)) {
						info = i; 
						break; 
					}	        			
				}
			}
			
			if(info!=null) { 
				Worksheet currentWorksheet = ViaAventurica.getCurrentWorksheet(); 
				if(currentWorksheet.map.equals(info))
					return currentWorksheet.map;
				else { 
					// Speicher für die neue Map freimachen!
					ViaAventurica.setCurrentWorksheet(null); 
					return MapLoaderFactory.loadMap(info); 
				} 
				
			}
		} catch(Exception ex) { 
			
		}
		return null; 
	}
	
	private final String getAtt(Attributes a, Attribute att) {
		return a.getValue(att.toString()); 
	}
}
