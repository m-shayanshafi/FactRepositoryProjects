package via.aventurica.view.utils;

import java.io.File;
import java.util.ArrayList;

import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;

import via.aventurica.ViaAventurica;
import via.aventurica.model.marker.MarkerIcon;

public class UserIcons extends DefaultListModel{
	private final static long serialVersionUID = 1L;
	
	private ArrayList<MarkerIcon> icons; 
	
	public final static UserIcons INSTANCE = new UserIcons();

//	private boolean isInitialized = false; 
	
	private UserIcons() {
		loadPictures(); 
		/*new Thread(new Runnable() { public void run() { loadPictures(); 
		}}).start(); */
	}
	
	private void loadPictures() {
		try {
			if(ViaAventurica.USER_ICONS.isDirectory())
				ViaAventurica.USER_ICONS.mkdir(); 
			String[] images = ViaAventurica.USER_ICONS.list(new ExtensionBasedFilenameFilter(".jpg", ".png", ".bmp", ".gif")); 
			icons = new ArrayList<MarkerIcon>(images.length); 
			for(String image : images) { 
				File imageFile = new File(ViaAventurica.USER_ICONS, image); 
				icons.add(new MarkerIcon(new ImageIcon(imageFile.getAbsolutePath()), image)); 
			}
			
		} catch(Exception e) { 
			
		} finally { 
//			isInitialized = true; 
		}
	}
	
//	public boolean isInitialized() {
//		return isInitialized;
//	}

	@Override
	public Object getElementAt(int index) {
		return icons.get(index);
	}

	@Override
	public int getSize() {
		
		return icons.size();
	}

	
	public static MarkerIcon forName(String filename) { 
		for(MarkerIcon i : INSTANCE.icons)
			if(i.getFilename().equals(filename))
				return i; 
		
		return null; 
	}
	
	
}
