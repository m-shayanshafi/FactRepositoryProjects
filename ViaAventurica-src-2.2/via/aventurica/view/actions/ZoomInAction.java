package via.aventurica.view.actions;

import java.awt.event.ActionEvent;

import via.aventurica.view.utils.AppIcons;
import via.aventurica.view.utils.ExtendedWorksheetAction;

public class ZoomInAction extends ExtendedWorksheetAction {
	private final static long serialVersionUID = 1L;

	public ZoomInAction() {
		super("Karte vergrößern", AppIcons.ZOOM_IN);
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		getWorksheet().zoomManager.zoomIn(); 
		//if(currentZoomLevel <= 4) 
			//ViaAventurica.APPLICATION.setZoomLevel(currentZoomLevel*2); 
	}
}
