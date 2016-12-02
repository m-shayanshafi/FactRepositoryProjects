package via.aventurica.model.route;

import java.awt.Color;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;

import javax.swing.JOptionPane;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import via.aventurica.ViaAventurica;
import via.aventurica.model.map.Map;
import via.aventurica.model.map.MapCollection;
import via.aventurica.model.map.MapInfo;
import via.aventurica.model.map.MapLoaderFactory;
import via.aventurica.model.marker.Marker;
import via.aventurica.model.marker.MarkerManager;
import via.aventurica.model.worksheet.Worksheet;
import via.aventurica.view.utils.UserIcons;

public class RouteIO {
	private final static long serialVersionUID = 1L;
	
	public static void saveRoute(File saveFile) throws IOException { 
		
		Worksheet ws = ViaAventurica.getCurrentWorksheet(); 
		
		OutputStreamWriter w = new OutputStreamWriter(new BufferedOutputStream(new FileOutputStream(saveFile)), "UTF-8");
		Map currentMap = ws.map;
		MarkerManager markerControl = ws.markerManager;//= ViaAventurica.APPLICATION.getMapView().getMarkerControl(); 
		
		String mapSetFolderName = prepareString(currentMap.getFile().getParentFile().getName());
		String mapTitle = prepareString(currentMap.getTitle()); 
		
		w.write("<route mapsetfolder=\""+mapSetFolderName+"\" maptitle=\""+mapTitle+"\">\n");
		 
		for(RoutePoint p : ws.routeManager.getCurrentRoute().getWaypoints()) { 
			w.write("	<point x=\""+p.x+"\" y=\""+p.y+"\" />\n"); 
		}
		
		for(Marker marker : markerControl.listMarkers()) {
			String markerIconFilename = marker.getMarkerIcon() == null ? "" : marker.getMarkerIcon().getFilename(); 
			w.write("	<marker x=\""+marker.getSourceX()+
						"\" y=\""+marker.getSourceY()+
						"\" title=\""+prepareString(marker.getText())+
						"\" iconfile=\""+prepareString(markerIconFilename)+
						"\" color=\""+marker.getMarkerColor().getRGB()+
						"\" semitransparent=\""+marker.isSemiTransparent()+"\"/>\n");
		}
		
		w.write("</route>"); 
		w.close(); 
		
	}

	public static void loadRoute(File selectedFile) throws SAXException, IOException, ParserConfigurationException {
      	SAXParserFactory factory = SAXParserFactory.newInstance();
        SAXParser saxParser = factory.newSAXParser();
        
        final ArrayList<RoutePoint> routeList = new ArrayList<RoutePoint>();
        final StringBuffer mapDirectory = new StringBuffer(), mapTitle = new StringBuffer();
        //final MarkerManager control = ViaAventurica.getCurrentWorksheet().markerManager; //ViaAventurica.APPLICATION.getMapView().getMarkerControl(); 
        System.out.println("Lade Route");
        final ArrayList<Marker> markers = new ArrayList<Marker>();
        saxParser.parse(selectedFile, new DefaultHandler() {	
        	
        	@Override
        	public void startElement(String uri, String localName, String name, Attributes attributes) throws SAXException {
     
        		if(name.equals("route")) {
  
        			mapDirectory.append(attributes.getValue("mapsetfolder"));
        			mapTitle.append(attributes.getValue("maptitle")); 
        		} else if(name.equals("point")) {
        			routeList.add(
        					new RoutePoint(
        							Integer.parseInt(attributes.getValue("x")), 
        							Integer.parseInt(attributes.getValue("y")))); 
        		} else if(name.equals("marker")) { 
        			
        			Marker foundMarker= new Marker(
            				Integer.parseInt(attributes.getValue("x")), 
            				Integer.parseInt(attributes.getValue("y")), 
            				attributes.getValue("title")
            			); 
        			String filename = attributes.getValue("iconfile");
        			String colorStr  = attributes.getValue("color"); 
        			String semiTransparentStr = attributes.getValue("semitransparent"); 
        			
        			if(filename!=null && filename.trim().length() > 0)
        				foundMarker.setMarkerIcon(UserIcons.forName(filename));
        			if(colorStr!=null && semiTransparentStr!=null) { 
        				try { 
        					Color markerColor = new Color(Integer.parseInt(colorStr)); 
        					boolean isSemiTransparent = Boolean.valueOf(semiTransparentStr); 
        					foundMarker.setMarkerColor(markerColor, isSemiTransparent); 
        				} catch(Exception e) { }
        			}

        			markers.add(foundMarker);  
        			
        		}
        	}
        });
        
        MapCollection coll = MapLoaderFactory.getMapsFromFolder(mapDirectory.toString());
        
        MapInfo info = null; 
        if(coll != null) {
        	
        	for(MapInfo i : coll) {
        		if(i.getTitle().equals(mapTitle.toString())) {
        			info = i; 
        			break; 
        		}
        			
        	}
        }
        
        if(info!=null) { 
        	Map currentMap = ViaAventurica.getCurrentWorksheet().map;
        	Worksheet ws = new Worksheet(currentMap.equals(info) ? currentMap : MapLoaderFactory.loadMap(info));
        	for(Marker m : markers)
        		ws.markerManager.addMarker(m); 
        	
        	Route r = ws.routeManager.newRoute(); 
        	
        	for(RoutePoint p : routeList)
        		r.addPoint(p); 
        	
        	ViaAventurica.setCurrentWorksheet(ws); 
      //  	if(currentMap.equals(info)); 
        	//Worksheet ws = new Worksheet()
        	//ViaAventurica.setCurrentWorksheet(ws); 
        	//ViaAventurica.APPLICATION.setMap(MapLoaderFactory.loadMap(info)); 
/*        	RouteManager.loadPath(routeList);
        	for(Marker m : markers) { 
        		control.addMarker(m); 
        	} */
        } else { 
        	JOptionPane.showMessageDialog(ViaAventurica.APPLICATION, "Die Route konnte nicht geladen werden:\nDie Karte bzw. das Kartenset wurde nicht gefunden", "Fehler beim Laden", JOptionPane.ERROR_MESSAGE); 
        }
        
        
	}
	
	private final static String prepareString(final String data) { 
		return data.replaceAll("\"", "&quot;").replaceAll("<", "&lt;").replaceAll(">", "&gt;"); 
	}
}
