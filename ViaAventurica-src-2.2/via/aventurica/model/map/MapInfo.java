package via.aventurica.model.map;

import java.io.File;
import java.io.Serializable;

/**
 * Speichert Metainformationen über eine bestimmte Karte. Stellt allerdings nicht 
 * das Bild selbst zur Verfügung. Die erbende Klasse {@link Map} enthält auch dieses.
 * 
 */
public class MapInfo implements Serializable {
	private final static long serialVersionUID = 1L;
	
	private final File file;
	private final File previewFile; 
	private final String title; 
	private final String licenseInfo; 
	private final String distanceUnit;
	private final double pixelSampleDistance; 
	private final double realSampleDistance;
	
	/**
	 * @param file dateiname zum Kartenbild
	 * @param previewFile dateiname zur miniaturvorschau oder <code>null</code>, wenn keine vorhanden ist.
	 * @param title titel der Karte
	 * @param licenseInfo lizenzinfos
	 * @param distanceUnit distanzeinheit
	 * @param pixelSampleDistance menge von pixel, die der realSampleDistance entsprechen
	 * @param realSampleDistance menge der distanzeinheit, die der pixelSampleDistance entsprechen. 
	 */
	protected MapInfo(final File file, final File previewFile, final String title, final String licenseInfo, final String distanceUnit, final double pixelSampleDistance, final double realSampleDistance) {
		super();
		this.file = file;
		this.previewFile = previewFile;
		this.title = title;
		this.licenseInfo = licenseInfo;
		this.distanceUnit = distanceUnit;
		this.pixelSampleDistance = pixelSampleDistance;
		this.realSampleDistance = realSampleDistance;
	}

	public static long getSerialVersionUID() {
		return serialVersionUID;
	}

	/**
	 * @return Datei, der Karte zurück
	 */
	public File getFile() {
		return file;
	}
	
	/**
	 * @return Datei der miniaturvorschau
	 */
	public File getPreviewFile() {
		return previewFile;
	}

	/**
	 * @return Titel der Karte, Beispielsweise "Gesamtkarte von Aventurien"
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * @return Informationen über Herkunft, Besitzer, etc.
	 */
	public String getLicenseInfo() {
		return licenseInfo;
	}

	/**
	 * @return Distanzeinheit, die für diese Karte gilt
	 */
	public String getDistanceUnit() {
		return distanceUnit;
	}

	/**
	 * @return gibt die Anzahl der Pixel zurück, die {@link #getRealSampleDistance()} in der Einheit {@link #getDistanceUnit()} entspricht. 
	 */
	public double getPixelSampleDistance() {
		return pixelSampleDistance;
	}

	/**
	 * @return die entfernung in der Einheit {@link #getDistanceUnit()}, die {@link #getPixelSampleDistance()} entspricht.
	 */
	public double getRealSampleDistance() {
		return realSampleDistance;
	} 
	
	@Override
	public boolean equals(Object obj) {
		if(obj instanceof MapInfo) { 
			MapInfo other = (MapInfo) obj; 
			return other.file.equals(file) 
					&& other.title.equals(title) 
					&& other.distanceUnit.equals(distanceUnit) 
					&& other.pixelSampleDistance == pixelSampleDistance 
					&& other.realSampleDistance == realSampleDistance; 
		} else { 
			return super.equals(obj);  
		}
	}
	
	
	
	
}
