package inf101.games.gui;

import inf101.games.Main101Games;

import java.net.URL;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.ImageIcon;

/**
 * Laster inn bilder fra filsystemet eller JAR-filen.
 * 
 * Søker gjennom en søkesti og diverse mulig file extensions.
 * 
 * @author Anya Helene Bagge
 *
 */
public class ImageLoader {
	public static final List<String> imagePaths = Arrays.asList("../", "");
	public static final List<String> extensions = Arrays.asList("", ".jpg", ".png", ".gif", ".JPG", ".PNG", ".GIF");
	public static final Map<String, ImageIcon> images = new HashMap<String, ImageIcon>();
	
	/**
	 * Finner og returner et bilde.
	 * 
	 * Søker automatisk også i "..", og med forskjellige file extensions.
	 * 
	 * Mellomlagrer bildene, så det er trygt å kalle denne ofte.
	 * 
	 * @param fileName Navnet på bildefilen, inkludert katalognavn
	 * @return Et bilde
	 */
	public static ImageIcon getImage(String fileName) {
		if(images.containsKey(fileName))
			return images.get(fileName);
		for(String imagePath : imagePaths) {
			for(String extension : extensions) {
				URL url = Main101Games.class.getResource(imagePath + fileName + extension);
				if(url != null) {
					ImageIcon icon = new ImageIcon(url);
					images.put(fileName, icon);
					return icon;
				}
				
			}
		}
		throw new NoSuchIconError(fileName);
	}
}
