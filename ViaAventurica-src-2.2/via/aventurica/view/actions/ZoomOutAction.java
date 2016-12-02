package via.aventurica.view.actions;

import java.awt.event.ActionEvent;

import via.aventurica.view.utils.AppIcons;
import via.aventurica.view.utils.ExtendedWorksheetAction;

public class ZoomOutAction extends ExtendedWorksheetAction{
	private final static long serialVersionUID = 1L;
	
	public ZoomOutAction() {
		super("Karte verkleinern", AppIcons.ZOOM_OUT); 
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		getWorksheet().zoomManager.zoomOut();  
	}
}
