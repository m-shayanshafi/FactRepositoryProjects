package via.aventurica.model.map;

import java.io.File;
import java.util.ArrayList;


/**
 * Eine Gruppierung von mehreren {@link MapInfo}s, die einen Titel nd einen Pfad hat  
 */
public class MapCollection extends ArrayList<MapInfo>{
	private final static long serialVersionUID = 1L;
	private final String collectionName; 
	private final File collectionBaseFolder; 
	
	public MapCollection(final String collectionName, final File collectionBaseFolder) {
		super(); 
		this.collectionName = collectionName; 
		this.collectionBaseFolder = collectionBaseFolder; 
		
	}
	
	public String getCollectionName() {
		return collectionName;
	}
	
	public File getCollectionBaseFolder() {
		return collectionBaseFolder;
	}
	
	@Override
	public String toString() {
		return collectionName+" ("+size()+" Karten)";
	}
	
	public static boolean isValidCollectionName(final String name) {
		if(name.contains("/") || name.contains("\\"))
			return false; 
			
		File testFile = new File(MapLoaderFactory.MAPS_FOLDER, name); 
		boolean success = testFile.mkdir(); 
		
		if(success)
			testFile.delete();
		
		return success; 
	}

	public static boolean collectionExists(String collectionTitle) {
		if(collectionTitle.contains(File.separator) || collectionTitle.contains("/"))
			return false; 
		return new File(MapLoaderFactory.MAPS_FOLDER, collectionTitle).isDirectory(); 
	}

	
}
