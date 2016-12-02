package via.aventurica.model.map;

import java.io.File;
import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;



class MapLoadingHandler extends DefaultHandler implements IMapSetConstants{
	private final static long serialVersionUID = 1L;
	
	String mapListTitle; 
	

	
	private MapCollection mapCollection; 
	private File currentMapListFolder; 
	
	
	/**
	 * Lädt ein Kartenset aus einer XML Konfigurationsdatei. 
	 * @param mapSetFile die XML Konfigurationsdatei
	 * @throws ParserConfigurationException 
	 * @throws SAXException
	 * @throws IOException
	 */
	public MapLoadingHandler(File mapListFile) throws ParserConfigurationException, SAXException, IOException {
		mapListTitle = mapListFile.getName(); 
		currentMapListFolder = mapListFile.getParentFile(); 
      	SAXParserFactory factory = SAXParserFactory.newInstance();
        SAXParser saxParser = factory.newSAXParser();
        saxParser.parse( mapListFile, this );

	}
	
	/**
	 * @return das geladene mapSet, oder <code>null</code> wenn beim Initialisieren der Klasse eine Exception aufgetreten ist. 
	 */
	public MapCollection getMapCollection() {
		return mapCollection;
	}
	
	@Override
	public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
		if(qName.equalsIgnoreCase(ROOT_ELEMENT))
		{
			String foundTitle = attributes.getValue(ROOT_ATT_NAME); 
			mapListTitle = iif(foundTitle, mapListTitle);   
			mapCollection = new MapCollection(mapListTitle, currentMapListFolder); 
		}
		else if(qName.equalsIgnoreCase(MAP_ELEMENT))
		{
			String title = iif(attributes.getValue(MAP_ATT_NAME),null);
			String image = iif(attributes.getValue(MAP_ATT_IMAGE), null);
			String preview = attributes.getValue(MAP_ATT_PREVIEW); 
			String pixelDistance = iif(attributes.getValue(MAP_ATT_PIXELS),"100");
			String realDistance = iif(attributes.getValue(MAP_ATT_REAL),"1"); 
			String units = iif(attributes.getValue(MAP_ATT_UNIT),"Meilen");
			String license = iif(attributes.getValue(MAP_ATT_LICENSE),"unbekannt");
			try { 

				
				if(title == null || image == null)
					throw new Exception(); 
				
				File mapFile = new File(mapCollection.getCollectionBaseFolder(), image); 
				if(!mapFile.exists())
					throw new Exception("MAP Datei konnte nicht gefunden werden"); 

				
				File previewFile = preview==null ? null : new File(mapCollection.getCollectionBaseFolder(), preview); 
				
				double pixelDistanceDbl = Double.valueOf(pixelDistance); 
				double realDistanceDbl = Double.valueOf(realDistance); 
				
				MapInfo info = new MapInfo(mapFile, previewFile, title, license, units, pixelDistanceDbl, realDistanceDbl);  
				mapCollection.add(info);
				
								
			} catch(Exception e) {
				System.err.println("FEHLER IN MAP: "+title);
				System.err.println(e.getMessage());
				e.printStackTrace(); 
			}
		}
	}


	
	private final static String iif(final String input, final String defaultValue)
	{
		if(input!=null && input.trim().length()>0)
			return input; 
		return defaultValue; 
	}
}
