package via.aventurica.model.map;

import java.io.File;

public interface IMapSetConstants {
	
	final static File MAPS_FOLDER = new File("./maps/");
	final static String COLLECTION_CONFIG_FILENAME="collection.va.xml";
	
	final static String ROOT_ELEMENT="mapset";
	final static String ROOT_ATT_NAME="name";
	
	final static String MAP_ELEMENT="map"; 
	final static String MAP_ATT_NAME="title"; 
	final static String MAP_ATT_PREVIEW="preview"; 
	final static String MAP_ATT_IMAGE="image"; 
	final static String MAP_ATT_PIXELS="pixel-distance"; 
	final static String MAP_ATT_REAL="real-distance"; 
	final static String MAP_ATT_UNIT="distance-units";
	final static String MAP_ATT_LICENSE="license";
}
