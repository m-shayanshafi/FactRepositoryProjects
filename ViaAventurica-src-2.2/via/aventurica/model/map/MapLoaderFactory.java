package via.aventurica.model.map;

import java.awt.Cursor;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;
import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import via.aventurica.ViaAventurica;

public class MapLoaderFactory implements IMapSetConstants{
	private final static long serialVersionUID = 1L;
	
 
	
	public final static List<MapCollection> listMaps() {
		ArrayList<MapCollection> collections = new ArrayList<MapCollection>(); 
		for(File collectionFolder : MAPS_FOLDER.listFiles()) {
			if(collectionFolder.isDirectory()) {
				File configFile = new File(collectionFolder, COLLECTION_CONFIG_FILENAME); 
				try {
					 collections.add(new MapLoadingHandler(configFile).getMapCollection()); 
				} catch(Exception e) { 
					e.printStackTrace(); 
					collections.add(new MapCollection("FEHLER IN "+collectionFolder.getName(), collectionFolder));
				}
			}
		}
		return collections; 
	}
	
	
	public final static MapCollection getMapsFromFolder(String foldername) throws ParserConfigurationException, SAXException, IOException { 
		File configFile = new File(new File(MAPS_FOLDER, foldername), COLLECTION_CONFIG_FILENAME);
		if(configFile.exists()) { 
			return new MapLoadingHandler(configFile).getMapCollection(); 
		} 
		return null; 	
	}
		
	public final static Map loadMap(final MapInfo mapInfo) {
		try { 
			if(ViaAventurica.APPLICATION!=null) {
				ViaAventurica.APPLICATION.setCursor(new Cursor(Cursor.WAIT_CURSOR));
				ViaAventurica.setCurrentWorksheet(null); 
				//ViaAventurica.APPLICATION.unloadWorksheet(); 
			}
			return new Map(mapInfo, ImageIO.read(mapInfo.getFile())); 
		} catch(Exception e) { 
			e.printStackTrace(); 
		} finally { 
			if(ViaAventurica.APPLICATION!=null)
				ViaAventurica.APPLICATION.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
		}
		return null; 
	}
	
}
