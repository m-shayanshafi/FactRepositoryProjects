package via.aventurica.model.zoom;

import java.util.ArrayList;

import via.aventurica.model.worksheet.Worksheet;


public class ZoomManager {

	@SuppressWarnings("unused")
	private final Worksheet ws; 
	
	//private static ZoomManager instance = new ZoomManager();
	private ArrayList<IZoomListener> listeners = new ArrayList<IZoomListener>(20); 
	private IZoomListener masterZoomListener; 
	private int currentZoomLevelIndex;  
	
	private final static double[] ZOOM_LEVELS = { 0.125, 0.25, 0.5, 1, 1.5, 2, 3, 4 };
	private final static int DEFAULT_ZOOM_LEVEL_INDEX = 3;
	
	/*
	public static ZoomManager getInstance() {
		return instance;
	} */
								
		
	public ZoomManager(Worksheet ws) {
		this.ws = ws; 
		currentZoomLevelIndex = DEFAULT_ZOOM_LEVEL_INDEX; 
	}
	
	public void resetZoom(){ 
		fireZoomLevelReset(currentZoomLevelIndex); 
		currentZoomLevelIndex = DEFAULT_ZOOM_LEVEL_INDEX; 
	}
	
	public boolean zoomOut() { 
		if(currentZoomLevelIndex > 0) { 
			fireZoomLevelUpdate(currentZoomLevelIndex--, currentZoomLevelIndex); 
			return true; 
		}
		return false; 
	}
	
	public boolean zoomIn() { 
		if(currentZoomLevelIndex < ZOOM_LEVELS.length-1) { 
			fireZoomLevelUpdate(currentZoomLevelIndex++, currentZoomLevelIndex); 
			return true; 
		}
		return false; 
	}
	
	private void fireZoomLevelUpdate(int oldIndex, int newIndex) { 
		double oldLevel = ZOOM_LEVELS[oldIndex];
		double newLevel = ZOOM_LEVELS[newIndex]; 
		for(IZoomListener l : listeners)
			l.zoomedTo(oldLevel, newLevel);
		masterZoomListener.zoomedTo(oldLevel, newLevel); 
	}
	
	private void fireZoomLevelReset(int oldIndex) {
		double oldLevel = ZOOM_LEVELS[oldIndex];
		for(IZoomListener l : listeners)
			l.zoomedTo(oldLevel, 1); 
		masterZoomListener.zoomedTo(oldLevel, 1); 
	}


	public final double getCurrentZoom() {
		return ZOOM_LEVELS[currentZoomLevelIndex]; 
	}
	
	public void addZoomListener(IZoomListener l) { 
		listeners.add(l); 
	}
	
	public void removeZoomListener(IZoomListener l) { 
		listeners.remove(l); 
	}


	public void setMasterZoomListener(IZoomListener master) {
		this.masterZoomListener = master; 
	}

	public void clear() {
		masterZoomListener = null; 
		listeners.clear(); 
	}
	
}
