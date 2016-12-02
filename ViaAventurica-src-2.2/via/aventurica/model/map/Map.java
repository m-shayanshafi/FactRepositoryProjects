package via.aventurica.model.map;

import java.awt.image.BufferedImage;

/**
 * Repräsentiert eine geladene Karte. 
 * Die Klasse erbt von {@link MapInfo}, 
 * und stellt noch das Bild der Karte zur Verfügung. 
 */
public class Map extends MapInfo{
	private final static long serialVersionUID = 1L;
	
	private final BufferedImage map; 
	
	/**
	 * @param mapInfo Information zur Karte
	 * @param map das Bild zur Karte
	 */
	protected Map(final MapInfo mapInfo, final BufferedImage map) {
		super(mapInfo.getFile(), mapInfo.getPreviewFile(), mapInfo.getTitle(), mapInfo.getLicenseInfo(), mapInfo.getDistanceUnit(), mapInfo.getPixelSampleDistance(), mapInfo.getRealSampleDistance()); 
		this.map = map; 
	}
	
	/**
	 * @return die Karte als Bild
	 */
	public BufferedImage getMap() {
		return map;
	}
	
	public int getWidth() { 
		return map.getWidth(); 
	}
	
	public int getHeight() { 
		return map.getHeight();
	}
	
	@Override
	protected void finalize() throws Throwable {
		super.finalize();
	}
	
}
