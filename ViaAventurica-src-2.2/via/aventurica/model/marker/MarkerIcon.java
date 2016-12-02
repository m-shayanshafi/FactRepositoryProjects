package via.aventurica.model.marker;

import javax.swing.ImageIcon;

public class MarkerIcon {
	private final static long serialVersionUID = 1L;
	
	private ImageIcon icon; 
	private String filename;
	public MarkerIcon(ImageIcon icon, String filename) {
		super();
		this.icon = icon;
		this.filename = filename;
	}
	public ImageIcon getIcon() {
		return icon;
	}
	public void setIcon(ImageIcon icon) {
		this.icon = icon;
	}
	public String getFilename() {
		return filename;
	}
	public void setFilename(String filename) {
		this.filename = filename;
	} 
	
	
}
