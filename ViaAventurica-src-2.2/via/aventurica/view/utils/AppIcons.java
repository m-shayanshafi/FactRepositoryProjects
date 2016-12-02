package via.aventurica.view.utils;


import java.awt.Image;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

/**
 * Eine Sammlung von allen Icons der Applikation. Als Enum, zum Zugreifen auf ein Icon, kann folgende
 * Syntax verwendet werden: <code>ImageCollection.ICON_NAME.ICON</code>
 */
public enum AppIcons {
	
	APPLICATION_ICON("icon_small.png"), 
	NEW_ROUTE("new.png"),
	UNDO_ROUTE("undo.png"),
	LOAD_ROUTE("load.png"), 
	SAVE_ROUTE("save.png"), 
	MAP_CHOOSE("map.png"), 
	ZOOM_IN("zoom_in.png"), 
	ZOOM_OUT("zoom_out.png"), 
	ZOOM_RESET("zoom_reset.png"), 
	ABOUT("information.png"),
	HELP("help.png"),
	SCREENSHOT_CLIPBOARD("copy.png"), 
	SCREENSHOT_SAVE("save.png"),
	SCREENSHOT_GENERAL("screenshot.png"),
	NOTE("note.png"), 
	ACCEPT("accept.png"),
	DELETE("bin.png"), 
	_DROPDOWN_ARROW("dropdownarrow.gif"), 
	;
	
	
	/**
	 * Das Icon, welches diesem Icon Typ zugeordnet ist. 
	 */
	public final ImageIcon ICON; 
	
	
	/////////////////////////////////////////////////////////////////////////////////////////
	// Initialisierer 
	private final static String BASEPATH="via/aventurica/view/icons/"; 
	private final static String NOTFOUND_PATH = "icons/notfound.png";  
	
	private AppIcons(final String imageName)
	{
		
		Image loadedImage = null; 
		try { 
			loadedImage = ImageIO.read(ClassLoader.getSystemResourceAsStream(BASEPATH+imageName)); 
		} catch(Exception e)
		{
			System.err.println(this.name()+": Konnte Icon nicht öffnen: "+BASEPATH+imageName);
			try {
				
				loadedImage = ImageIO.read(ClassLoader.getSystemResourceAsStream(NOTFOUND_PATH));
			} catch (IOException e1) {
				System.err.println(this.name()+": SCHWERWIEGEND: konnte Standardfehlerbild: "+NOTFOUND_PATH+" nicht laden");
				loadedImage= null; 
			}  
			 
		}
		
		ICON = new ImageIcon(loadedImage); 
	}
	
}
