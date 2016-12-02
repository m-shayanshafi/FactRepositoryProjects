package via.aventurica.model.map;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;


public class MapSetWriter implements IMapSetConstants {
	private final static long serialVersionUID = 1L;
	
	public static void main(String[] args) {
		storeCollection(MapLoaderFactory.listMaps().get(0)); 
	}
	
	
	public final static void addMap( MapCollection collectionToAdd, final File file, final File previewFile, final String title, final String licenseInfo, final String distanceUnit, final double pixelSampleDistance, final double realSampleDistance) { 
		MapInfo newMap = new MapInfo(
				file, 
				previewFile, 
				title, 
				licenseInfo, 
				distanceUnit, 
				pixelSampleDistance,
				realSampleDistance);

		collectionToAdd.add(newMap); 
		storeCollection(collectionToAdd); 
	}
	
	private final static void storeCollection(MapCollection collection) {
		File configFile = new File(collection.getCollectionBaseFolder(), COLLECTION_CONFIG_FILENAME);
		File configFileBackup = new File(collection.getCollectionBaseFolder(), "BACKUP"+System.currentTimeMillis()+COLLECTION_CONFIG_FILENAME);
		configFile.renameTo(configFileBackup); 
		configFile = new File(collection.getCollectionBaseFolder(), COLLECTION_CONFIG_FILENAME); 
		Writer output = null; 
		try { 
			output = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(configFile), "UTF-8"));
			output.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n"); 
			startTag(output, ROOT_ELEMENT, false, ROOT_ATT_NAME, collection.getCollectionName()); 
			for(MapInfo map : collection) { 
				startTag(output, MAP_ELEMENT, true, 
						MAP_ATT_NAME, map.getTitle(), 
						MAP_ATT_LICENSE, map.getLicenseInfo(), 
						MAP_ATT_IMAGE, map.getFile().getName(), 
						MAP_ATT_PREVIEW, map.getPreviewFile()!=null ? map.getPreviewFile().getName() : "",
						MAP_ATT_PIXELS, ""+map.getPixelSampleDistance(), 
						MAP_ATT_REAL, ""+map.getRealSampleDistance(), 
						MAP_ATT_UNIT, map.getDistanceUnit()
				);
			}
			endTag(output, ROOT_ELEMENT); 
			output.flush(); 
			configFileBackup.delete(); 
		} catch(IOException exc) { 
			configFile.delete(); 
			configFileBackup.renameTo(configFile); 
		} finally { 
			if(output!=null)
				try {
					output.close();
				} catch (IOException e) { } 
		}

		
	}
	
	private final static void startTag(Writer w, String name, boolean isClosed, String... paras) throws IOException { 
		w.write("<");  
		w.write(name);  
		if(paras.length > 0) { 
			w.write(" "); 
			for(int i=0; i< paras.length; i+=2) { 
				w.write(paras[i]); 
				w.write("=\""); 
				w.write(prepareString(paras[i+1])); 
				w.write("\" "); 
			}
		}
		if(isClosed)
			w.write("/"); 
		w.write(">\n");  
	}
	
	private final static void endTag(Writer output, String name) throws IOException { 
		output.write("</"); 
		output.write(name); 
		output.write(">"); 
	}
	
	private final static String prepareString(final String data) { 
		return data.replaceAll("\"", "&quot;").replaceAll("<", "&lt;").replaceAll(">", "&gt;"); 
	}
	
}
