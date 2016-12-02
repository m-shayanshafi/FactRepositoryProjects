package via.aventurica;

import java.awt.Rectangle;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.GregorianCalendar;
import java.util.Properties;

import javax.swing.JOptionPane;

import via.aventurica.io.WebUpdateNotifier;
import via.aventurica.model.map.Map;
import via.aventurica.model.map.MapCollection;
import via.aventurica.model.map.MapInfo;
import via.aventurica.model.map.MapLoaderFactory;

public class ConfigManager {
	private final static long serialVersionUID = 1L;

	private static ConfigManager instance; 
	
	private final static File CONFIG_FILE=new File("viaaventurica.ini"); 
	private final Properties configProperties; 
	

	

	
	public Rectangle getWindowBounds() { 
		return new Rectangle(readInteger("va.window.posX", 100), readInteger("va.window.posY", 100), 
				readInteger("va.window.width", 800), readInteger("va.window.height", 600));  
	}
	
	
	public void setWindowBounds(final Rectangle rect) { 
		setPropertyValue("va.window.posX", rect.x); 
		setPropertyValue("va.window.posY", rect.y); 
		setPropertyValue("va.window.width", rect.width); 
		setPropertyValue("va.window.height", rect.height); 
	}
	
	public void setLastUpdateCheckDate(GregorianCalendar lastStartDate) { 
		setPropertyValue("va.lastUpdate", WebUpdateNotifier.RSS_DATE_FORMAT.format(lastStartDate.getTime())); 
	}
	
	public GregorianCalendar getLastUpdateCheckDate() { 
		String lastStartString = readString("va.lastUpdate", null); 
		GregorianCalendar cal = null; 
		if(lastStartString!=null) try {
			cal = new GregorianCalendar(); 
			cal.setTime(WebUpdateNotifier.RSS_DATE_FORMAT.parse(lastStartString));
		} catch(Exception e) { 
			e.printStackTrace(); 
		}
		return cal; 
		
	}
	
	public void setLastMap(final Map map) {
		if(map==null)
			return; 
		
		final String mapSetFolderName = map.getFile().getParentFile().getName();
		final String mapTitle = map.getTitle();
		
		setPropertyValue("va.lastmap.title", mapTitle); 
		setPropertyValue("va.lastmap.mapsetfolder", mapSetFolderName); 
	}
	
	public Map getLastMap() { 
		final String mapsetFolderName = readString("va.lastmap.mapsetfolder", "").trim();
		final String mapTitle = readString("va.lastmap.title", "").trim(); 
		
		if(mapsetFolderName.length() == 0 || mapTitle.length() == 0)
			return null; 
		
        
		try {
			MapCollection coll = MapLoaderFactory.getMapsFromFolder(mapsetFolderName);
		    MapInfo info = null; 
		    if(coll != null) { 	
		    	for(MapInfo i : coll) {
		    		if(i.getTitle().equals(mapTitle)) {
		        		info = i; 
		        		break; 
		        	}		
		    	}
		    }
		    if(info!=null)
		    	return MapLoaderFactory.loadMap(info); 
		} catch (Exception e) {
			return null; 
		}
		return null;
	}
	
	public final void save() { 
		OutputStream outS = null;
		try {
			outS = new FileOutputStream(CONFIG_FILE); 
			configProperties.store(outS, "ViaAventurica Konfiguration"); 
		} catch(Exception e) { 
			JOptionPane.showMessageDialog(null, "Konfigurationsdatei nicht beschreibbar: "+CONFIG_FILE.getAbsolutePath()+
					"\nbitte die Dateirechte kontrollieren und die Datei ggf. erstellen", 
					"Warnung", JOptionPane.WARNING_MESSAGE);
		} finally { 
			try {
				outS.close();
			} catch (IOException e) {
				e.printStackTrace();
			} 
		}
	}
	
	public static ConfigManager getInstance() { 
		if(instance == null)
			instance = new ConfigManager(); 
		return instance; 
	}
	
	
	private ConfigManager() {
		configProperties = new Properties();
		InputStream inStream = null; 
		try {
			if(!CONFIG_FILE.exists())
				CONFIG_FILE.createNewFile();
			inStream = new FileInputStream(CONFIG_FILE); 
			configProperties.load(inStream);
		} catch(Exception e) { 
			JOptionPane.showMessageDialog(null, "Konfigurationsdatei nicht gefunden: "+CONFIG_FILE.getAbsolutePath()+"\nbitte die Dateirechte kontrollieren und die Datei ggf. erstellen", "Warnung", JOptionPane.WARNING_MESSAGE); 
		} finally { 
			try {
				inStream.close();
			} catch (IOException e) {
				e.printStackTrace();
			} 
		}
	}
	
	
	private int readInteger(final String key, final int defaultValue) { 
		if(!configProperties.containsKey(key)) {
			System.err.println("Config File: Konnte schlüssel nicht finden: "+key+"		--> Default: "+defaultValue);
			configProperties.put(key, defaultValue); 
		}
		String val = configProperties.getProperty(key);
		try {
			return Integer.valueOf(val);
		} catch(Exception e) { 
			return defaultValue; 
		}
	}
	
	
	
	private String readString(final String key, final String defaultValue) { 
		if(!configProperties.containsKey(key)) {
			System.err.println("Config File: Konnte schlüssel nicht finden: "+key+"		--> Default: "+defaultValue);
			if(defaultValue!=null)
				configProperties.put(key, defaultValue); 
		}
		return configProperties.getProperty(key); 
	}
	
	private void setPropertyValue(final String key, Object value) { 
		configProperties.setProperty(key, value.toString());
	}
	
}
