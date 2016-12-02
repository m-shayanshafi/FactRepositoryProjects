package via.aventurica.view.actions;

import java.awt.event.ActionEvent;

import via.aventurica.view.utils.AppIcons;
import via.aventurica.view.utils.ExtendedWorksheetAction;

public class ZoomResetAction extends ExtendedWorksheetAction{
	private final static long serialVersionUID = 1L;
	
	
	public ZoomResetAction() {
		super("Kartengröße zurücksetzen", AppIcons.ZOOM_RESET); 
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		getWorksheet().zoomManager.resetZoom(); 
	}
	
}
